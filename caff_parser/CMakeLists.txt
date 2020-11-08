cmake_minimum_required(VERSION 3.17)
project(caff_parser)

set(CMAKE_CXX_STANDARD 14)
set(PROJECT_SOURCES src/main.cpp src/BmpGenerator.cpp src/Ciff.cpp src/CaffParser.cpp)
set(PROJECT_HEADERS src/ciff.h src/BmpGenerator.h src/Ciff.h src/CaffParser.h)

add_executable(caff_parser ${PROJECT_SOURCES} ${PROJECT_HEADERS})