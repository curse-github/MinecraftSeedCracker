#ifndef __HELPER
#define __HELPER

#include <vector>
#include <string>

std::string bytesToBase16(const std::vector<unsigned char>& bytes);
std::string wordsToBase16(const std::vector<unsigned int>& words);
std::string wordsToBase16(const std::vector<unsigned long long int>& words);
std::string bytesToBase16(const unsigned char* bytes, const unsigned int& bytesSize);

std::vector<unsigned char> strToBytes(const std::string& str);
std::string bytesToStr(const std::vector<unsigned char>& vec);

std::vector<unsigned char> concat(std::vector<unsigned char> a, const std::vector<unsigned char>& b);

bool match(const unsigned char* a, const unsigned char* b, const unsigned int& size);
bool match(const std::vector<unsigned char>& a, const std::vector<unsigned char>& b);
bool match(const std::string& a, const std::string& b);

#endif// __HELPER