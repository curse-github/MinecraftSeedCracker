LIB_BUILD = ar rcs
O_BUILD = g++ -O3 -march=native -funroll-loops -flto -I ./src -o

makeTmp:
	- mkdir tmp

./tmp/Lib.o: makeTmp ./src/Lib.cpp
	$(O_BUILD) ./tmp/Lib.o -c ./src/Lib.cpp
./tmp/MinecraftLib.o: makeTmp ./src/MinecraftLib.cpp
	$(O_BUILD) ./tmp/MinecraftLib.o -c ./src/MinecraftLib.cpp
./tmp/Random.o: makeTmp ./src/Random.cpp
	$(O_BUILD) ./tmp/Random.o -c ./src/Random.cpp
./tmp/Finder.o: makeTmp ./src/Finder.cpp
	$(O_BUILD) ./tmp/Finder.o -c ./src/Finder.cpp
./tmp/StrongholdStructure.o: makeTmp ./src/StrongholdStructure.cpp
	$(O_BUILD) ./tmp/StrongholdStructure.o -c ./src/StrongholdStructure.cpp
./tmp/RandomSolver.o: makeTmp ./src/RandomSolver.cpp
	$(O_BUILD) ./tmp/RandomSolver.o -c ./src/RandomSolver.cpp

./tmp/Lib.a: makeTmp ./tmp/Lib.o
	$(LIB_BUILD) ./tmp/Lib.a ./tmp/Lib.o
./tmp/MinecraftLib.a: makeTmp ./tmp/MinecraftLib.o
	$(LIB_BUILD) ./tmp/MinecraftLib.a ./tmp/MinecraftLib.o
./tmp/Random.a: makeTmp ./tmp/Random.o
	$(LIB_BUILD) ./tmp/Random.a ./tmp/Random.o
./tmp/Finder.a: makeTmp ./tmp/Finder.o
	$(LIB_BUILD) ./tmp/Finder.a ./tmp/Finder.o
./tmp/StrongholdStructure.a: makeTmp ./tmp/StrongholdStructure.o
	$(LIB_BUILD) ./tmp/StrongholdStructure.a ./tmp/StrongholdStructure.o
./tmp/RandomSolver.a: makeTmp ./tmp/RandomSolver.o
	$(LIB_BUILD) ./tmp/RandomSolver.a ./tmp/RandomSolver.o

main.exe: ./src/main.cpp ./tmp/MinecraftLib.a ./tmp/Finder.a ./tmp/StrongholdStructure.a ./tmp/RandomSolver.a ./tmp/Random.a ./tmp/Lib.a
	$(O_BUILD) ./main.exe ./src/main.cpp ./tmp/MinecraftLib.a ./tmp/Finder.a ./tmp/StrongholdStructure.a ./tmp/RandomSolver.a ./tmp/Random.a ./tmp/Lib.a