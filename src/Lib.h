#ifndef __LIB
#define __LIB

const double PI =  3.14159265359;
const double TAU = 6.283185307179586476925286766559;

#pragma region structs
template <typename T>
struct Nullable {
    protected:
    T* value;
    public:
    bool hasValue;
    Nullable() : value(nullptr), hasValue(false) {};
    Nullable(T* _value) : value(_value), hasValue(_value != nullptr) {};
    Nullable(const Nullable<T>& copy) = delete;
    Nullable(Nullable<T>&& move) = delete;
    Nullable<T>& operator=(const Nullable<T>& copy) = delete;
    Nullable<T>& operator=(Nullable<T>&& move) = delete;
    T& getValue() {
        return *value;
    }
    const T& getValue() const {
        return *value;
    }
};
template <typename T>
struct NonOwningNullable : public Nullable<T> {
    NonOwningNullable() : Nullable<T>() {}
    NonOwningNullable(T* _value) : Nullable<T>(_value) {}
    NonOwningNullable(const Nullable<T>& copy) = delete;
    NonOwningNullable(NonOwningNullable<T>&& move) : Nullable<T>(move.value) {
        move.value = nullptr;
        move.hasValue = false;
    }
    ~NonOwningNullable() {}
    void operator=(T* _value) {
        this->value = _value;
        this->hasValue = (_value == nullptr);
    }
    operator NonOwningNullable<const T>() && {
        NonOwningNullable<T> nullable = NonOwningNullable<const T>((const T*)this->value);
        this->value = nullptr;
        this->hasValue = false;
        return (NonOwningNullable<const T>&&)nullable;
    }
    template <typename U>
    operator NonOwningNullable<U>() {
        return NonOwningNullable<U>((U*)this->value);
    }
};
template <typename T>
struct OwningNullable : public Nullable<T> {
    OwningNullable() : Nullable<T>() {}
    OwningNullable(T* _value) : Nullable<T>(_value) {}
    OwningNullable(OwningNullable<T>&& move) : Nullable<T>(move.value) {
        move.value = nullptr;
        move.hasValue = false;
    }
    ~OwningNullable() {
        if (this->hasValue) delete this->value;
    }
    void operator=(T* _value) {
        if (this->hasValue) delete this->value;
        this->value = _value;
        this->hasValue = true;
    }
    template <typename U>
    operator OwningNullable<U>() && {
        OwningNullable<U> nullable = OwningNullable<U>((U*)this->value);
        this->value = nullptr;
        this->hasValue = false;
        return (OwningNullable<U>&&)nullable;
    }
    T* takeValue() {
        T* tmp = this->value;
        this->value = nullptr;
        this->hasValue = false;
        return tmp;
    }
};
struct Vec2 {
    int x;
    int z;
    Vec2();
    Vec2(const int& _x, const int& _z);
    Vec2(const Vec2& copy);
    Vec2 operator+(const Vec2& rhs) const;
    Vec2 operator-(const Vec2& rhs) const;
    int magnitude() const;
};
struct Vec2D {
    double x;
    double z;
    Vec2D();
    Vec2D(const double& _x, const double& _z);
    Vec2D(const Vec2D& copy);
    Vec2D operator+(const Vec2D& rhs) const;
    Vec2D operator+(const Vec2& rhs) const;
    Vec2D operator-(const Vec2D& rhs) const;
    Vec2D operator-(const Vec2& rhs) const;
    double magnitude() const;
};
struct Vec3 {
    int x;
    int y;
    int z;
    Vec3();
    Vec3(const int& _x, const int& _y, const int& _z);
    Vec3(const Vec3& copy);
    Vec3 operator+(const Vec3& rhs) const;
    Vec3 operator-(const Vec3& rhs) const;
    int magnitude() const;
};
struct Vec3D {
    double x;
    double y;
    double z;
    Vec3D();
    Vec3D(const double& _x, const double& _y, const double& _z);
    Vec3D operator+(const Vec3D& rhs) const;
    Vec3D operator+(const Vec3& rhs) const;
    Vec3D operator-(const Vec3D& rhs) const;
    Vec3D operator-(const Vec3& rhs) const;
    Vec3D(const Vec3D& copy);
    double magnitude() const;
};

#include <string>// for std::string
#include <vector>// for std::vector<T>
#include <cmath>// for std::pow and std::sin

const unsigned long long int maxWord = std::pow(2, 32);
std::vector<unsigned int> md5Raw(const unsigned char* rawBytes, const unsigned int& rawSize);
std::vector<unsigned int> md5Vector(const std::vector<unsigned char>& vec);
std::string md5Str(std::string str);

#endif// __LIB