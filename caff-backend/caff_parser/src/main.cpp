#include <fstream>
#include <iostream>
#include "CaffParser.h"
#include "BmpGenerator.h"

using namespace std;

int main(int argc, char *argv[]) {

    string current_exec_name = argv[0];
    string file_name = "";
    string output_file_name = "";
    if(argc == 3){
        file_name = argv[1];
        output_file_name = argv[2];
    } else {
        cout << "Invalid arguments";
        return 1;
    }

    ifstream rf(file_name, ios::out | ios::binary);
    if (!rf) {
        cout << "Cannot open file!" << endl;
        return 1;
    }

    try {
        CaffParser caffParser{rf};
        caffParser.parseCaff();

        BmpGenerator bmpGenerator{};
        if(!caffParser.ciffs.empty())
            bmpGenerator.voidGenerateBmp(output_file_name, caffParser.ciffs[0]);
    }
    catch (invalid_argument& e) {
        cout << "Error during parsing: " << e.what();
    }

    return 0;
}
