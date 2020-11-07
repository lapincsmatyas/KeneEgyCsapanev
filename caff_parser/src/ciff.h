//
// Created by lapincsmatyas on 2020. 11. 07..
//

#ifndef CAFF_PARSER_CIFF_H
#define CAFF_PARSER_CIFF_H

#include <string>
#include <vector>

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


#endif //CAFF_PARSER_CIFF_H
