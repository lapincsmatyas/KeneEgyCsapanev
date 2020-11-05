#include <iostream>
#include <fstream>
#include <cstring>
#include "CAFF.h"

using namespace std;

//structures for BMP headers
//TODO: credit
#pragma pack( push, 1 )
struct BITMAPFILEHEADER {
    uint16_t file_type{0x4D42};
    uint32_t file_size{0};
    uint16_t reserved1{0};
    uint16_t reserved2{0};
    uint32_t offset_data{0};
};
struct BITMAPINFOHEADER{
    uint32_t size{0};
    int32_t width{0};
    int32_t height{0};
    uint16_t planes{1};
    uint16_t bit_count{24};
    int32_t compression{0};
    uint32_t size_image{ 0 };                // 0 - for uncompressed images
    int32_t x_pixels_per_meter{ 0 };
    int32_t y_pixels_per_meter{ 0 };
    uint32_t colors_used{ 0 };               // No. color indexes in the color table. Use 0 for the max number of colors allowed by bit_count
    uint32_t colors_important{ 0 };
};
#pragma pack( pop )

//Helper functions to convert bytes to integers or longs
unsigned long byteArrayToLong_8(char *bytes) {
    unsigned long result = 0;
    for (unsigned int i = 0; i < 8; i++) {
        result |= (bytes[i] & 0xFF) << (8 * i);
    }
    return result;
}
unsigned short byteArrayToInt_2(char *bytes) {
    unsigned short result = (((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF));
    return result;
}

//BMP generation
void writeHeader(ostream& out, unsigned int width, unsigned int height){
    //TODO: ha nem 4-gyel osztható a szélesség, akkor +0-kat kell hozzáfűzni
    if (width % 4 != 0) {
        cerr << "ERROR: There is a windows-imposed requirement on BMP that the width be a multiple of 4." << endl;
        cerr << "Your width does not meet this requirement, hence this will fail.  You can fix this" << endl;
        cerr << "by increasing the width to a multiple of 4." << endl;
        exit(1);
    }

    BITMAPFILEHEADER tWBFH{};
    tWBFH.file_size = (14 + 40 + (width*height*3));
    tWBFH.offset_data = 14 + 40;

    BITMAPINFOHEADER tW2BH{};
    tW2BH.size = 40;
    tW2BH.width = width;
    tW2BH.height = -height;

    out.write((char*)(&tWBFH),sizeof(BITMAPFILEHEADER));
    out.write((char*)(&tW2BH),sizeof(BITMAPINFOHEADER));
}
void generateBmpFromCiff(ifstream &rf, unsigned int width, unsigned int height, int index){
    cout << endl << "*   BMP generation   *" << endl;

    int number_of_pixels = width*height;
    cout << "Starting BMP generation" << endl;
    cout << "Width: " << width << ", Height: " << height << ", Number of pixels: " << number_of_pixels << ", number of components: " << number_of_pixels * 3 << endl;

    ofstream ofs;
    string filename = "ciff.bmp";
    ofs.open(filename);
    writeHeader(ofs, width, height);

    for(int i = 0; i < height * width; i++) {

            char blue;
            rf.read((char *) &blue, 1);

            char green;
            rf.read((char *) &green, 1);

            char red;
            rf.read((char *) &red, 1);

            ofs << red << green << blue;
    }

    ofs.close();
}

//parser for the CIFF part of file
void parseCiff(ifstream &rf, int index) {
    int read_bytes = 0;
    cout << endl << "*   CIFF   *" << endl;
    char ciff_header_magic[5];
    rf.read((char *) ciff_header_magic, 4); read_bytes += 4;
    ciff_header_magic[4] = '\0';

    cout << "CIFF magic: " << ciff_header_magic << endl;
    if(strcmp(ciff_header_magic, "CIFF") != 0){
        //TODO: error
    }

    char header_size_array[8];
    rf.read((char *) header_size_array, 8); ; read_bytes += 8;
    unsigned long header_size = byteArrayToLong_8(header_size_array);

    cout << "Size of CIFF header: " << header_size << endl;

    char content_size_array[8];
    rf.read((char *) content_size_array, 8); ; read_bytes += 8;
    unsigned long content_size = byteArrayToLong_8(content_size_array);
    //TODO: check if this is the real content size

    cout << "Size of CIFF content: " << content_size << endl;

    char width_array[8];
    rf.read((char *) width_array, 8); ; read_bytes += 8;
    unsigned long width = byteArrayToLong_8(width_array);

    cout << "Width of CIFF: " << width << endl;

    char height_array[8];
    rf.read((char *) height_array, 8); read_bytes += 8;
    unsigned long height = byteArrayToLong_8(height_array);

    cout << "Height of CIFF: " << height << endl;

    // if (width * height *3 != content_size) error

    string caption;
    char act_char;
    rf.read((char *) &act_char, 1); read_bytes++;
    while(act_char != '\n') {
        caption += act_char;
        rf.read((char *) &act_char, 1); read_bytes++;
    }
    cout << "Caption of CIFF: " << caption << endl;

    string tags;
    rf.read((char *) &act_char, 1); read_bytes++;
    while(read_bytes < header_size){
        tags += act_char;
        rf.read((char *) &act_char, 1); read_bytes++;
    }
    cout << "Tags of CIFF: " << tags << endl;
    //TODO: (optional) make it an array of tags

    generateBmpFromCiff(rf, width, height, index);
}

//parser for an animation block
void parseAnimation(ifstream &rf, int index) {
    cout << "************" << endl;
    cout << "*   ANIM   *" << endl;
    cout << "************" << endl << endl;
    char duration_array[8];
    rf.read((char *) duration_array, 8);
    unsigned long duration = byteArrayToLong_8(duration_array);

    cout << "Duration of CIFF in milliseconds: " << duration << endl;

    parseCiff(rf, index);
}

//parser for credits block
void parseCredits(ifstream &rf, unsigned long length) {
    cout << "************" << endl;
    cout << "*  CREDITS *" << endl;
    cout << "************" << endl << endl;

    //TODO: use unsigned char everywhere
    char create_Y_array[2];
    rf.read((char *) create_Y_array, 2); length -= 2;

    char create_M_char;
    rf.read((char *) &create_M_char, 1);  length -= 1;

    char create_D_char;
    rf.read((char *) &create_D_char, 1);  length -= 1;

    char create_h_char;
    rf.read((char *) &create_h_char, 1);  length -= 1;

    char create_m_char;
    rf.read((char *) &create_m_char, 1);  length -= 1;

    unsigned int create_M = (int) create_M_char;
    unsigned int create_D = (int) create_D_char;
    unsigned int create_h = (int) create_h_char;
    unsigned int create_m = (int) create_m_char;

    unsigned short create_Y = byteArrayToInt_2(create_Y_array);
    //todo: check if valid dates

    cout << "Date of creation: " << create_Y << "." << create_M << "." << create_D << " " << create_h << ":" << create_m
         << endl;

    char creator_length_array[8];
    rf.read((char *) &creator_length_array, 8);  length -= 8;
    unsigned long creator_length = byteArrayToLong_8(creator_length_array);
    cout << "Size of creator name: " << creator_length << endl;
    //TODO check length

    char creator[creator_length + 1];
    rf.read((char *) creator, creator_length);  length -= creator_length;
    creator[creator_length] = '\0';
    cout << "Creator of CAFF: " << creator << endl;

    if(length != 0){
        //TODO error
    }
}

//parser for header block
unsigned long parseHeaderBlock(ifstream &rf){
    unsigned char block_id_char;
    rf.read((char *) &block_id_char, 1);
    int block_id = (int) block_id_char;

    if(block_id != 1) {
        //TODO: error
    }

    char block_length_array[8];
    rf.read((char *) block_length_array, 8);
    unsigned long block_length = byteArrayToLong_8(block_length_array);
    //TODO: check if block_length number of bytes were read before next block

    cout << "************" << endl;
    cout << "*  HEADER  *" << endl;
    cout << "************" << endl << endl;

    char header_magic[5];
    rf.read((char *) header_magic, 4);
    header_magic[4] = '\0';

    cout << "Header magic: " << header_magic << endl;

    if(strcmp(header_magic, "CAFF") != 0){
        //TODO: error
    }

    char header_size_array[8];
    rf.read((char *) &header_size_array, 8);
    unsigned long header_size = byteArrayToLong_8(header_size_array);
    cout << "Size of header: " << header_size << endl;

    char num_anim_array[8];
    rf.read((char *) &num_anim_array, 8);
    unsigned long num_anim = byteArrayToLong_8(num_anim_array);
    cout << "Number of CIFFs in animation: " << num_anim << endl;

    return num_anim;
}

//parser for a generic block
void parseBlock(ifstream &rf, int index) {
    unsigned char block_id_char;
    rf.read((char *) &block_id_char, 1);
    int block_id = (int) block_id_char;

    char block_length_array[8];
    rf.read((char *) block_length_array, 8);
    unsigned long block_length = byteArrayToLong_8(block_length_array);
    //TODO: check if block_length number of bytes were read before next block

    cout << endl << "*******************************";
    cout << endl << "Reading block with id: " << block_id << ", and size: " << block_length << endl << endl;

    switch ((int) block_id) {
        case 2:
            parseCredits(rf, block_length);
            break;
        case 3:
            parseAnimation(rf, index);
            break;
        default:
            //todo invalid id
            break;
    }
}

int main() {
    ifstream rf("../caff-files/1.caff", ios::out | ios::binary);
    if (!rf) {
        cout << "Cannot open file!" << endl;
        return 1;
    }

    //Header parsing
    //This is always the first block and contains the number of CIFF-s in the file
    unsigned int num_anim = parseHeaderBlock(rf);

    //Parse rest of file, which contains blocks in an unspecified order

    for(int i = 0; i < num_anim + 1; i++){
        parseBlock(rf, i);
    }

    //TODO: biztos hogy num_anim mennyiségű blokkot olvastunk, amiben szerepel a credits

    return 0;
}
