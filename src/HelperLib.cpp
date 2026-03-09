#include "HelperLib.h"

std::string bytesToBase16(const std::vector<unsigned char>& bytes) {
    std::string result;
    for (unsigned int i = 0; i < bytes.size(); i++) {
        unsigned char num = bytes[i];
        unsigned char char1 = num >> 4;
        unsigned char char2 = num & 0xf;
        result += ((char1 < 10) ? ('0' + char1):('A' + (char1 - 10)));
        result += ((char2 < 10) ? ('0' + char2):('A' + (char2 - 10)));
    }
    return result;
}
std::string wordsToBase16(const std::vector<unsigned int>& words) {
    std::string result;
    for (unsigned int i = 0; i < words.size(); i++) {
        for (int j = 3; j >= 0; j--) {
            unsigned char num = (words[i] >> (j * 8)) & 0xff;
            unsigned char char1 = num >> 4;
            unsigned char char2 = num & 0xf;
            result += ((char1 < 10) ? ('0' + char1):('A' + (char1 - 10)));
            result += ((char2 < 10) ? ('0' + char2):('A' + (char2 - 10)));
        }
    }
    return result;
}
std::string wordsToBase16(const std::vector<unsigned long long int>& words) {
    std::string result;
    for (unsigned int i = 0; i < words.size(); i++) {
        for (int j = 7; j >= 0; j--) {
            unsigned char num = (words[i] >> (j * 8)) & 0xff;
            unsigned char char1 = num >> 4;
            unsigned char char2 = num & 0xf;
            result += ((char1 < 10) ? ('0' + char1):('A' + (char1 - 10)));
            result += ((char2 < 10) ? ('0' + char2):('A' + (char2 - 10)));
        }
    }
    return result;
}
std::string bytesToBase16(const unsigned char* bytes, const unsigned int& bytesSize) {
    std::string result;
    for (unsigned int i = 0; i < bytesSize; i++) {
        unsigned char num = bytes[i];
        unsigned char char1 = num >> 4;
        unsigned char char2 = num & 0xf;
        result += ((char1 < 10) ? ('0' + char1):('A' + (char1 - 10)));
        result += ((char2 < 10) ? ('0' + char2):('A' + (char2 - 10)));
    }
    return result;
}

std::vector<unsigned char> strToBytes(const std::string& str) {
    return std::vector<unsigned char>(str.cbegin(), str.cend());
}
std::string bytesToStr(const std::vector<unsigned char>& vec) {
    return std::string(vec.cbegin(), vec.cend());
}

std::vector<unsigned char> concat(std::vector<unsigned char> a, const std::vector<unsigned char>& b) {
    for (size_t i = 0; i < b.size(); i++)
        a.push_back(b[i]);
    return a;
}

bool match(const unsigned char* a, const unsigned char* b, const unsigned int& size) {
    for (size_t i = 0; i < size; i++) if (a[i] != b[i]) return false;
    return true;
}
bool match(const std::vector<unsigned char>& a, const std::vector<unsigned char>& b) {
    if (a.size() != b.size()) return false;
    for (size_t i = 0; i < a.size(); i++) if (a[i] != b[i]) return false;
    return true;
}
bool match(const std::string& a, const std::string& b) {
    if (a.size() != b.size()) return false;
    for (size_t i = 0; i < a.size(); i++) if (a[i] != b[i]) return false;
    return true;
}