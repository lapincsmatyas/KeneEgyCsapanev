#include "CaffParser.h"

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

    ifstream rf(file_name, ios::out | ios::binary);
    if (!rf) {
        cout << "Cannot open file!" << endl;
        return 1;
    }
    free(argv);
    */

    CaffParser caffParser{"1.caff"};

    /*
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
    ciff.pixels.clear();
    ciff.caption.clear();
    ciff.tags.clear();
    */
    return 0;
}
