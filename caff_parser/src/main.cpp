#include <fstream>
#include <iostream>
#include "CaffParser.h"
#include "BmpGenerator.h"

using namespace std;

int main(int argc, char *argv[]) {
    /*
    string current_exec_name = argv[0];
    string file_name = "";
    if(argc == 2){
        file_name = argv[1];
    } else {
        // handleError("Invalid arguments");
    }
    */
    ifstream rf("../caff-files/1.caff", ios::out | ios::binary);
    if (!rf) {
        cout << "Cannot open file!" << endl;
        return 1;
    }
    free(argv);

    CaffParser caffParser{"../caff-files/1.caff", rf};
    caffParser.parseCaff();

    BmpGenerator bmpGenerator{};

    for (auto & ciff : caffParser.ciffs){
        bmpGenerator.voidGenerateBmp(ciff.caption, ciff);
    }

    return 0;
}
