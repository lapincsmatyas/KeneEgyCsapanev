//
// Created by lapincsmatyas on 2020. 11. 08..
//
#include <iostream>
#include <cstring>
#include <fstream>
#include <stdexcept>

#include "CaffParser.h"
#include "Ciff.h"

using namespace std;

CaffParser::CaffParser(ifstream &rf) : rf(rf) {
}

void CaffParser::handleError(string e) {
    throw e;
}

//Helper functions to convert bytes to integers or longs
unsigned long CaffParser::byteArrayToLong_8(const char *bytes) {
    unsigned long result = 0;
    for (unsigned int i = 0; i < 8; i++) {
        result |= (bytes[i] & 0xFF) << (8 * i);
    }
    return result;
}

unsigned short CaffParser::byteArrayToInt_2(const unsigned char *bytes) {
    unsigned short result = (((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF));
    return result;
}

vector<char> CaffParser::createPixelArray(unsigned int width, unsigned int height) {
    vector<char> pixel_array;
    char act[3] = {0, 0, 0};
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
void CaffParser::parseCiff() {
    int header_read_bytes_begin = rf.tellg();

    //cout << endl << "*   CIFF   *" << endl;
    char ciff_header_magic[5];
    rf.read((char *) ciff_header_magic, 4);
    ciff_header_magic[4] = '\0';

    //cout << "CIFF magic: " << ciff_header_magic << endl;
    if (strcmp(ciff_header_magic, "CIFF") != 0) {
        throw invalid_argument("CIFF magic is not CIFF");
    }

    char header_size_array[8];
    rf.read((char *) header_size_array, 8);
    unsigned long header_size = byteArrayToLong_8(header_size_array);

    //cout << "Size of CIFF header: " << header_size << endl;

    char content_size_array[8];
    rf.read((char *) content_size_array, 8);
    unsigned long content_size = byteArrayToLong_8(content_size_array);

    //cout << "Size of CIFF content: " << content_size << endl;

    char width_array[8];
    rf.read((char *) width_array, 8);
    unsigned long width = byteArrayToLong_8(width_array);

    //cout << "WIDTH " << width << endl;

    char height_array[8];
    rf.read((char *) height_array, 8);
    unsigned long height = byteArrayToLong_8(height_array);

    //cout << "HEIGHT " << height << endl;

    if ((width * height * 3) != content_size) {
        throw invalid_argument("Invalid content size or width and size given");
    }

    string caption;
    char act_char;
    rf.read((char *) &act_char, 1);
    while (((int) rf.tellg() - header_read_bytes_begin) < header_size && act_char != '\n') {
        caption += act_char;
        rf.read((char *) &act_char, 1);
    }
    if (act_char != '\n') {
        throw invalid_argument("Caption does not contain the end character");
    }
    //cout << "CAPTION " << caption << endl;

    string tags;
    rf.read((char *) &act_char, 1);
    while (((int) rf.tellg() - header_read_bytes_begin) < header_size) {
        tags += act_char;
        if(act_char == '\0') tags += '$';
        rf.read((char *) &act_char, 1);
    }
    if (act_char != '\0') {
        throw invalid_argument("Invalid end of tags");
    }

    //cout << "TAGS " << tags << endl;

    int begin = rf.tellg();
    vector<char> temp_array = createPixelArray(width, height);
    int end = rf.tellg();

    if (end - begin != content_size) {
        throw invalid_argument("Not valid size of bmp read");
    }

    Ciff ciff{};
    ciff.content_size = content_size;
    ciff.header_size = header_size;
    ciff.width = width;
    ciff.height = height;
    ciff.tags = tags;
    ciff.caption = caption;
    ciff.pixels = temp_array;
    ciffs.push_back(ciff);
}

//parser for an animation block
void CaffParser::parseAnimation() {
    //cout << "*   ANIM   *" << endl;
    char duration_array[8];
    rf.read((char *) duration_array, 8);
    unsigned long duration = byteArrayToLong_8(duration_array);

    //cout << "Duration of CIFF in milliseconds: " << duration << endl;

    return parseCiff();
}

void CaffParser::validateDate(unsigned int year, unsigned int month, unsigned int day, unsigned int hour,
                              unsigned int minute) {
    if (year < 1987 || year > 2020) {
        throw invalid_argument("Invalid year");
    }

    if (month <= 0 || month > 12) {
        throw invalid_argument("Invalid month");
    }

    if (((month == 1) || (month == 3) || (month == 5) || (month == 7) ||
         (month == 8) || (month == 10) || (month == 12)) && day > 31) {
        throw invalid_argument("This month has only 31 days");
    }

    if (((month == 4) || (month == 6) || (month == 9) || (month == 11)) && day > 30) {
        throw invalid_argument("This month has only 30 days");
    } else if (((month == 2) && (year % 4 == 0)) && day > 29) {
        throw invalid_argument("This month has only 29 days in leap years");
    } else if (((month == 2) && (year % 4 != 0)) && day > 28) {
        throw invalid_argument("This month has only 28 days");
    };

    if (hour < 0 || hour > 24) {
        throw invalid_argument("Invalid hour");
    }

    if (minute < 0 || minute >= 60) {
        throw invalid_argument("Invalid minute");
    }
}

//parser for credits block
void CaffParser::parseCredits() {
    int begin = rf.tellg();

    //cout << "*  CREDITS *" << endl;

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

    //cout << "Date of creation: " << create_Y << "." << create_M << "." << create_D << " " << create_h << ":" << create_m << endl;

    char creator_length_array[8];
    rf.read((char *) &creator_length_array, 8);
    unsigned long creator_length = byteArrayToLong_8(creator_length_array);
    //cout << "Size of creator name: " << creator_length << endl;

    char creator[creator_length + 1];
    rf.read((char *) creator, creator_length);
    creator[creator_length] = '\0';
    //cout << "CREATOR " << creator << endl;

    int end = rf.tellg();
    if ((end - begin) != (creator_length + 8 + 6)) {
        throw invalid_argument("Length of credits was invalid");
    }
}

//parser for header block
unsigned long CaffParser::parseHeaderBlock() {
    unsigned char block_id_char;
    rf.read((char *) &block_id_char, 1);
    int block_id = (int) block_id_char;

    if (block_id != 1) {
        throw invalid_argument("The id of header was not 1");
    }

    char block_length_array[8];
    rf.read((char *) block_length_array, 8);
    unsigned long block_length = byteArrayToLong_8(block_length_array);

    unsigned long read_bytes_data = 0;

    //cout << "*  HEADER  *" << endl;

    char header_magic[5];
    rf.read((char *) header_magic, 4);
    read_bytes_data += 4;
    header_magic[4] = '\0';

    //cout << "Header magic: " << header_magic << endl;

    if (strcmp(header_magic, "CAFF") != 0) {
        throw invalid_argument("Magic in header was not CAFF");
    }

    char header_size_array[8];
    rf.read((char *) &header_size_array, 8);
    read_bytes_data += 8;
    unsigned long header_size = byteArrayToLong_8(header_size_array);
    //cout << "Size of header: " << header_size << endl;

    char num_anim_array[8];
    rf.read((char *) &num_anim_array, 8);
    read_bytes_data += 8;
    unsigned long num_anim = byteArrayToLong_8(num_anim_array);
    //cout << "Number of CIFFs in animation: " << num_anim << endl;

    if (read_bytes_data != block_length) {
        throw invalid_argument("Size of header block was not the number of bytes read");
    } else if (read_bytes_data != header_size) {
        throw invalid_argument("Size of header was not the number of bytes read");
    }

    return num_anim;
}

//parser for a generic block
int CaffParser::parseBlock(int index, bool credits_read) {
    int begin = rf.tellg();
    unsigned char block_id_char;
    rf.read((char *) &block_id_char, 1);
    unsigned int block_id = (unsigned int) block_id_char;

    if (!(block_id == 1 || block_id == 2 || block_id == 3)) {
        throw invalid_argument("Invalid block id");
    }

    if (credits_read && block_id == 2) {
        throw invalid_argument("Multiple credits blocks in file");
    }

    char block_length_array[8];
    rf.read((char *) block_length_array, 8);
    unsigned long block_length = byteArrayToLong_8(block_length_array);
    //TODO: check if block_length number of bytes were read before next block

    // cout << endl << "Reading block with id: " << block_id << ", and size: " << block_length << " :" << endl;

    switch ((int) block_id) {
        case 2:
            parseCredits();
            break;
        case 3:
            parseAnimation();
            break;
    }
    int end = rf.tellg();

    //check if block has the size specified in block length
    if ((end - begin - 8 - 1) != block_length) {
        throw invalid_argument("Length of block not valid");
    }

    return block_id;
}

void CaffParser::parseCaff() {
    try{
        //Header parsing
        //This is always the first block and contains the number of CIFF-s in the file
        unsigned int num_anim = parseHeaderBlock();

        //Parse rest of file, which contains blocks in an unspecified order
        bool credits_read = false;
        int animations_read = 0;

        for (int i = 0; animations_read < num_anim && (i < num_anim + 1); i++) {
            int type = parseBlock(i, credits_read);
            if (type == 2) {
                credits_read = true;
            } else if (type == 3) {
                animations_read++;
            }
        }
        if (!credits_read) {
            throw invalid_argument("The file does not contain credits block");
        }

        if (rf.peek() != EOF) {
            throw invalid_argument("The file has additional invalid content");
        }

    } catch (invalid_argument& e) {
        throw e;
    }
}