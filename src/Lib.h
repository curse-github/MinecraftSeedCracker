#ifndef __LIB
#define __LIB

const double PI = 3.141592653589;
const double TAU = 6.28318530718;

unsigned int myMin(const unsigned int& a, const unsigned int& b);
unsigned int myMax(const unsigned int& a, const unsigned int& b);
int myMin(const int& a, const int& b);
int myMax(const int& a, const int& b);
int myMin(const double& a, const double& b);
int myMax(const double& a, const double& b);
int myAbs(const int& x);

#pragma region structs
template <typename T>
struct Nullable {
    protected:
    T* value;
    public:
    bool hasValue;
    Nullable() : value(nullptr), hasValue(false) {};
    Nullable(T* _value) : value(_value), hasValue(_value == nullptr) {};
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
    Vec2(const int& _x, const int& _z) : x(_x), z(_z) {}
    Vec2(const Vec2& copy) : x(copy.x), z(copy.z) {}
    Vec2 operator+(const Vec2& rhs) const {
        return Vec2(x + rhs.x, z + rhs.z);
    }
    Vec2 operator-(const Vec2& rhs) const {
        return Vec2(x - rhs.x, z - rhs.z);
    }
    int magnitude() const {
        return x*x + z*z;
    }
};
struct Vec2D {
    double x;
    double z;
    Vec2D(const double& _x, const double& _z) : x(_x), z(_z) {}
    Vec2D(const Vec2D& copy) : x(copy.x), z(copy.z) {}
    Vec2D operator+(const Vec2D& rhs) const {
        return Vec2D(x + rhs.x, z + rhs.z);
    }
    Vec2D operator+(const Vec2& rhs) const {
        return Vec2D(x + rhs.x, z + rhs.z);
    }
    Vec2D operator-(const Vec2D& rhs) const {
        return Vec2D(x - rhs.x, z - rhs.z);
    }
    Vec2D operator-(const Vec2& rhs) const {
        return Vec2D(x - rhs.x, z - rhs.z);
    }
    double magnitude() const {
        return x*x + z*z;
    }
};
struct Vec3 {
    int x;
    int y;
    int z;
    Vec3() : x(0), y(0), z(0) {}
    Vec3(const int& _x, const int& _y, const int& _z) : x(_x), y(_y), z(_z) {}
    Vec3 operator+(const Vec3& rhs) const {
        return Vec3(x + rhs.x, y + rhs.y, z + rhs.z);
    }
    Vec3 operator-(const Vec3& rhs) const {
        return Vec3(x - rhs.x, y - rhs.y, z - rhs.z);
    }
    Vec3(const Vec3& copy) : x(copy.x), y(copy.y), z(copy.z) {}
    int magnitude() const {
        return x*x + y*y + z*z;
    }
};
struct Vec3D {
    double x;
    double y;
    double z;
    Vec3D() : x(0), y(0), z(0) {}
    Vec3D(const double& _x, const double& _y, const double& _z) : x(_x), y(_y), z(_z) {}
    Vec3D operator+(const Vec3D& rhs) const {
        return Vec3D(x + rhs.x, y + rhs.y, z + rhs.z);
    }
    Vec3D operator+(const Vec3& rhs) const {
        return Vec3D(x + rhs.x, y + rhs.y, z + rhs.z);
    }
    Vec3D operator-(const Vec3D& rhs) const {
        return Vec3D(x - rhs.x, y - rhs.y, z - rhs.z);
    }
    Vec3D operator-(const Vec3& rhs) const {
        return Vec3D(x - rhs.x, y - rhs.y, z - rhs.z);
    }
    Vec3D(const Vec3D& copy) : x(copy.x), y(copy.y), z(copy.z) {
    }
    double magnitude() const {
        return x*x + y*y + z*z;
    }
};

#endif// __LIB