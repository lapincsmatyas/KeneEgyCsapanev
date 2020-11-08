CC=afl-g++

output: main.o Ciff.o BmpGenerator.o CaffParser.o
	$(CC) main.o Ciff.o BmpGenerator.o CaffParser.o -o output
main.o: main.cpp
	$(CC) -c main.cpp
BmpGenerator.o: BmpGenerator.cpp BmpGenerator.h
	$(CC) -c BmpGenerator.cpp
Ciff.o: Ciff.cpp Ciff.h
	$(CC) -c Ciff.cpp
CaffParser.o: CaffParser.cpp CaffParser.h
	$(CC) -c CaffParser.cpp
clean: 
	rm *.o output
