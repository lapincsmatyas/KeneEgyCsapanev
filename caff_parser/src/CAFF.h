//
// Created by lapincsmatyas on 2020. 11. 01..
//

#ifndef CAFF_PARSER_CAFF_H
#define CAFF_PARSER_CAFF_H

#include "Header.h"
#include "Credits.h"
#include "Animation.h"

class CAFF {
public:
    Header header;
    Credits credits;
    Animation animation;
};


#endif //CAFF_PARSER_CAFF_H
