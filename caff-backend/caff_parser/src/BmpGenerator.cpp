//
// Created by lapincsmatyas on 2020. 11. 08..
//

#include <iostream>
#include <fstream>
#include "BmpGenerator.h"

using namespace std;

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

void BmpGenerator::voidGenerateBmp(std::string filename, Ciff ciff) {
    //cout << endl << "Starting BMP generation" << endl;

    ofstream ofs;
    ofs.open(filename + ".bmp");

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

    //cout << "BMP image generated with name " << filename << ".bmp" << endl;
    ofs.close();
}