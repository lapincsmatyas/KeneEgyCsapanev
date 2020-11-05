cmake_minimum_required(VERSION 3.17)
project(caff_parser)

set(CMAKE_CXX_STANDARD 14)
set(PROJECT_HEADERS src/Animation.h src/CAFF.h src/Credits.h src/Header.h)
set(PROJECT_SOURCES src/Animation.cpp src/CAFF.cpp src/Credits.cpp src/Header.cpp src/main.cpp)

add_executable(caff_parser ${PROJECT_SOURCES} ${PROJECT_HEADERS})