#include <iostream>
#include <fstream>
#include <cstring>
#include <vector>

using namespace std;

class Ciff {
public:
    unsigned int header_size{0};
    unsigned int content_size{0};
    unsigned int width{0};
    unsigned int height{0};
    std::string caption{""};
    std::string tags{""};
    std::vector<char> pixels{0};
};


//structures for BMP headers
#pragma pack( push, 1 )
struct BITMAPFILEHEADER {
    unsigned short file_type{0x4D42};
    unsigned int file_size{0};
    unsigned short reserved1{0};
    unsigned short reserved2{0};
    unsigned int offset_data{0};
};
struct BITMAPINFOHEADER {
    unsigned int size{0};
    int width{0};
    int height{0};
    unsigned short planes{1};
    unsigned short bit_count{24};
    int compression{0};
    unsigned int size_image{0};
    int x_pixels_per_meter{0};
    int y_pixels_per_meter{0};
    unsigned int colors_used{0};
    unsigned int colors_important{0};
};
#pragma pack( pop )

void handleError(string e) {
    throw e;
}

//Helper functions to convert bytes to integers or longs
unsigned long byteArrayToLong_8(const char *bytes) {
    unsigned long result = 0;
    for (unsigned int i = 0; i < 8; i++) {
        result |= (bytes[i] & 0xFF) << (8 * i);
    }
    return result;
}

unsigned short byteArrayToInt_2(const unsigned char *bytes) {
    unsigned short result = (((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF));
    return result;
}

//TODO: make it a separate function to collect bmp data
void generateBmpFromCiff(Ciff ciff) {
    cout << endl << "*   BMP generation   *" << endl;

    ofstream ofs;
    string filename = "ciff.bmp";
    ofs.open(filename);

    BITMAPFILEHEADER bitmapfileheader{};
    bitmapfileheader.file_size = (sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + (ciff.width * ciff.height * 3));
    bitmapfileheader.offset_data = (sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER));

    BITMAPINFOHEADER bitmapinfoheader{};
    bitmapinfoheader.size = sizeof(BITMAPINFOHEADER);
    bitmapinfoheader.width = ciff.width;
    bitmapinfoheader.height = -ciff.height;

    ofs.write((char *) (&bitmapfileheader), sizeof(BITMAPFILEHEADER));
    ofs.write((char *) (&bitmapinfoheader), sizeof(BITMAPINFOHEADER));

    int pad = 0;
    if ((ciff.width * 3) % 4 != 0) {
        pad = 4 - ((ciff.width * 3) % 4);
    }

    for (int i = 0; i < ciff.height; i++) {
        for(int j = 0; j < ciff.width*3; j++){
            ofs << ciff.pixels[i * (ciff.width*3) + j];
        }
        for(int padding = 0; padding < pad; padding++) ofs << 0x00;
    }

    ofs.close();
}

vector<char> createPixelArray(ifstream &rf, unsigned int width, unsigned int height){
    vector<char> pixel_array;
    char act[3] = {0,0,0};
    for (int i = 0; i < height * width; i++) {
            rf.read((char *) &act[0], 1);
            rf.read((char *) &act[1], 1);
            rf.read((char *) &act[2], 1);

            pixel_array.push_back(act[2]);
            pixel_array.push_back(act[1]);
            pixel_array.push_back(act[0]);
    }
    return pixel_array;
}

//parser for the CIFF part of file
void parseCiff(ifstream &rf) {
    int header_read_bytes_begin = rf.tellg();

    cout << endl << "*   CIFF   *" << endl;
    char ciff_header_magic[5];
    rf.read((char *) ciff_header_magic, 4);
    ciff_header_magic[4] = '\0';

    cout << "CIFF magic: " << ciff_header_magic << endl;
    if (strcmp(ciff_header_magic, "CIFF") != 0) {
        handleError("CIFF magic is not CIFF");
    }

    char header_size_array[8];
    rf.read((char *) header_size_array, 8);
    unsigned long header_size = byteArrayToLong_8(header_size_array);

    cout << "Size of CIFF header: " << header_size << endl;

    char content_size_array[8];
    rf.read((char *) content_size_array, 8);
    unsigned long content_size = byteArrayToLong_8(content_size_array);

    cout << "Size of CIFF content: " << content_size << endl;

    char width_array[8];
    rf.read((char *) width_array, 8);
    unsigned long width = byteArrayToLong_8(width_array);

    cout << "Width of CIFF: " << width << endl;

    char height_array[8];
    rf.read((char *) height_array, 8);
    unsigned long height = byteArrayToLong_8(height_array);

    cout << "Height of CIFF: " << height << endl;

    if((width * height * 3) != content_size){
        handleError("Invalid content size or width and size given");
    }

    string caption;
    char act_char;
    rf.read((char *) &act_char, 1);
    while (((int)rf.tellg() - header_read_bytes_begin) < header_size && act_char != '\n') {
        caption += act_char;
        rf.read((char *) &act_char, 1);
    }
    if(act_char != '\n'){
        handleError("Caption does not contain the end character");
    }
    cout << "Caption of CIFF: " << caption << endl;

    string tags;
    rf.read((char *) &act_char, 1);
    while (((int)rf.tellg() - header_read_bytes_begin) < header_size) {
        tags += act_char;
        rf.read((char *) &act_char, 1);
    }
    if(act_char != '\0'){
        handleError("Invalid end of tags");
    }

    cout << "Tags of CIFF: " << tags << endl;

    int begin = rf.tellg();
    vector<char> temp_array = createPixelArray(rf, width, height);
    int end = rf.tellg();

    if(end - begin != content_size){
        handleError("Not valid size of bmp read");
    }
    /*
    if(ciff.header_size == 0) {
        ciff.content_size = content_size;
        ciff.header_size = header_size;
        ciff.width = width;
        ciff.height = height;
        ciff.tags = tags;
        ciff.caption = caption;
        ciff.pixels = temp_array;
    }
     */
}

//parser for an animation block
void parseAnimation(ifstream &rf) {
    cout << "*   ANIM   *" << endl;
    char duration_array[8];
    rf.read((char *) duration_array, 8);
    unsigned long duration = byteArrayToLong_8(duration_array);

    cout << "Duration of CIFF in milliseconds: " << duration << endl;

    return parseCiff(rf);
}

void validateDate(unsigned int year, unsigned int month, unsigned int day, unsigned int hour, unsigned int minute) {
    if(year < 1987 || year > 2020){
        handleError("Invalid year");
    }

    if(month <= 0 || month > 12){
        handleError("Invalid month");
    }

    if (((month == 1) || (month == 3) || (month == 5) || (month == 7) ||
        (month == 8) || (month == 10) || (month == 12)) && day > 31) {
        handleError("This month has only 31 days");
    }

    if ( ((month == 4) || (month == 6) || (month == 9) || (month == 11)) && day > 30) {
        handleError("This month has only 30 days");
    } else if (((month == 2) && (year % 4 == 0)) && day > 29) {
        handleError("This month has only 29 days in leap years");
    } else if (((month == 2) && (year % 4 != 0)) && day > 28) {
        handleError("This month has only 28 days");
    };

    if(hour < 0 || hour > 24) {
        handleError("Invalid hour");
    }

    if(minute < 0 || minute >= 60){
        handleError("Invalid minute");
    }
}

//parser for credits block
void parseCredits(ifstream &rf) {
    int begin = rf.tellg();

    cout << "*  CREDITS *" << endl;

    //TODO: use unsigned char everywhere
    unsigned char create_Y_array[2];
    rf.read((char *) create_Y_array, 2);

    unsigned char create_M_char;
    rf.read((char *) &create_M_char, 1);

    unsigned char create_D_char;
    rf.read((char *) &create_D_char, 1);

    unsigned char create_h_char;
    rf.read((char *) &create_h_char, 1);

    unsigned char create_m_char;
    rf.read((char *) &create_m_char, 1);

    unsigned int create_M = (int) create_M_char;
    unsigned int create_D = (int) create_D_char;
    unsigned int create_h = (int) create_h_char;
    unsigned int create_m = (int) create_m_char;

    unsigned short create_Y = byteArrayToInt_2(create_Y_array);

    validateDate(create_Y, create_M, create_D, create_h, create_m);

    cout << "Date of creation: " << create_Y << "." << create_M << "." << create_D << " " << create_h << ":" << create_m
         << endl;

    char creator_length_array[8];
    rf.read((char *) &creator_length_array, 8);
    unsigned long creator_length = byteArrayToLong_8(creator_length_array);
    cout << "Size of creator name: " << creator_length << endl;

    char creator[creator_length + 1];
    rf.read((char *) creator, creator_length);
    creator[creator_length] = '\0';
    cout << "Creator of CAFF: " << creator << endl;

    int end = rf.tellg();
    if ((end - begin) != (creator_length + 8 + 6)) {
        handleError("Length of credits was invalid");
    }
}

//parser for header block
unsigned long parseHeaderBlock(ifstream &rf) {
    unsigned char block_id_char;
    rf.read((char *) &block_id_char, 1);
    int block_id = (int) block_id_char;

    if (block_id != 1) {
        handleError("The id of header was not 1");
    }

    char block_length_array[8];
    rf.read((char *) block_length_array, 8);
    unsigned long block_length = byteArrayToLong_8(block_length_array);

    unsigned long read_bytes_data = 0;

    cout << "*  HEADER  *" << endl;

    char header_magic[5];
    rf.read((char *) header_magic, 4);
    read_bytes_data += 4;
    header_magic[4] = '\0';

    cout << "Header magic: " << header_magic << endl;

    if (strcmp(header_magic, "CAFF") != 0) {
        handleError("Magic in header was not CAFF");
    }

    char header_size_array[8];
    rf.read((char *) &header_size_array, 8);
    read_bytes_data += 8;
    unsigned long header_size = byteArrayToLong_8(header_size_array);
    cout << "Size of header: " << header_size << endl;

    char num_anim_array[8];
    rf.read((char *) &num_anim_array, 8);
    read_bytes_data += 8;
    unsigned long num_anim = byteArrayToLong_8(num_anim_array);
    cout << "Number of CIFFs in animation: " << num_anim << endl;

    if (read_bytes_data != block_length) {
        handleError("Size of header block was not the number of bytes read");
    } else if (read_bytes_data != header_size) {
        handleError("Size of header was not the number of bytes read");
    }

    return num_anim;
}

//parser for a generic block
int parseBlock(ifstream &rf, int index, bool credits_read) {
    int begin = rf.tellg();
    unsigned char block_id_char;
    rf.read((char *) &block_id_char, 1);
    unsigned int block_id = (unsigned int) block_id_char;

    if (!(block_id == 1 || block_id == 2 || block_id == 3)) {
        handleError("Invalid block id");
    }

    if (credits_read && block_id == 2) {
        handleError("Multiple credits blocks in file");
    }

    char block_length_array[8];
    rf.read((char *) block_length_array, 8);
    unsigned long block_length = byteArrayToLong_8(block_length_array);
    //TODO: check if block_length number of bytes were read before next block

    cout << endl;
    cout << endl << "Reading block with id: " << block_id << ", and size: " << block_length << endl << endl;

    switch ((int) block_id) {
        case 2:
            parseCredits(rf);
            break;
        case 3:
            parseAnimation(rf);
            break;
    }
    int end = rf.tellg();

    //check if block has the size specified in block length
    if ((end - begin - 8 - 1) != block_length) {
        handleError("Length of block not valid");
    }

    return block_id;
}

int main(int argc, char *argv[]) {
    string current_exec_name = argv[0];
    string file_name = "";
    if(argc == 2){
        file_name = argv[1];
    } else {
        handleError("Invalid arguments");
    }

    ifstream rf(file_name, ios::out | ios::binary);
    if (!rf) {
        cout << "Cannot open file!" << endl;
        return 1;
    }

    try {
        //Header parsing
        //This is always the first block and contains the number of CIFF-s in the file
        unsigned int num_anim = parseHeaderBlock(rf);

        //Parse rest of file, which contains blocks in an unspecified order
        bool credits_read = false;
        int animations_read = 0;

        for (int i = 0; animations_read < num_anim && (i < num_anim + 1); i++) {
            int type = parseBlock(rf, i, credits_read);
            if (type == 2) {
                credits_read = true;
            } else if (type == 3) {
                animations_read++;
            }
        }
        if (!credits_read) {
            handleError("The file does not contain credits block");
        }

        if(rf.peek() != EOF){
            handleError("The file has additional invalid content");
        }



        Ciff ciff{};
        generateBmpFromCiff(ciff);

    } catch (string& e) {
        cout << endl << "******** ERROR ********" << endl;
        cout << "An error was caught:" << endl;
        cout << e << endl;
        cout << "***********************" << endl;
    }
    /*
    ciff.pixels.clear();
    ciff.caption.clear();
    ciff.tags.clear();
    */
    return 0;
}
