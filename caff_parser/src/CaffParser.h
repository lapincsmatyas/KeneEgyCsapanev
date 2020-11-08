//
// Created by lapincsmatyas on 2020. 11. 08..
//

#ifndef CAFF_PARSER_CAFFPARSER_H
#define CAFF_PARSER_CAFFPARSER_H

#include <vector>
#include "Ciff.h"

class CaffParser {
public:
    std::vector<Ciff> ciffs;
    std::ifstream& rf;
    CaffParser(std::string filename, std::ifstream &rf);
    void parseCaff();
private:
    unsigned long byteArrayToLong_8(const char* bytes);
    unsigned short byteArrayToInt_2(const unsigned char *bytes);
    std::vector<char> createPixelArray(unsigned int width, unsigned int height);
    void parseCiff();
    void parseAnimation();
    void validateDate(unsigned int year, unsigned int month, unsigned int day, unsigned int hour, unsigned int minute);
    void parseCredits();
    unsigned long parseHeaderBlock();
    int parseBlock(int index, bool credits_read);
    void handleError(std::string e);
};


#endif //CAFF_PARSER_CAFFPARSER_H
