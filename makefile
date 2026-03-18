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
./tmp/StrongholdStructure.o: makeTmp ./src/StrongholdStructure.cpp
	$(O_BUILD) ./tmp/StrongholdStructure.o -c ./src/StrongholdStructure.cpp
./tmp/RandomSolver.o: makeTmp ./src/RandomSolver.cpp
	$(O_BUILD) ./tmp/RandomSolver.o -c ./src/RandomSolver.cpp
./tmp/Biome.o: makeTmp ./src/Biome.cpp
	$(O_BUILD) ./tmp/Biome.o -c ./src/Biome.cpp
./tmp/Noise.o: makeTmp ./src/Noise.cpp
	$(O_BUILD) ./tmp/Noise.o -c ./src/Noise.cpp

./tmp/Lib.a: makeTmp ./tmp/Lib.o
	$(LIB_BUILD) ./tmp/Lib.a ./tmp/Lib.o
./tmp/MinecraftLib.a: makeTmp ./tmp/MinecraftLib.o
	$(LIB_BUILD) ./tmp/MinecraftLib.a ./tmp/MinecraftLib.o
./tmp/Random.a: makeTmp ./tmp/Random.o
	$(LIB_BUILD) ./tmp/Random.a ./tmp/Random.o
./tmp/StrongholdStructure.a: makeTmp ./tmp/StrongholdStructure.o
	$(LIB_BUILD) ./tmp/StrongholdStructure.a ./tmp/StrongholdStructure.o
./tmp/RandomSolver.a: makeTmp ./tmp/RandomSolver.o
	$(LIB_BUILD) ./tmp/RandomSolver.a ./tmp/RandomSolver.o
./tmp/Biome.a: makeTmp ./tmp/Biome.o
	$(LIB_BUILD) ./tmp/Biome.a ./tmp/Biome.o
./tmp/Noise.a: makeTmp ./tmp/Noise.o
	$(LIB_BUILD) ./tmp/Noise.a ./tmp/Noise.o

main.exe: ./src/main.cpp ./tmp/MinecraftLib.a ./tmp/StrongholdStructure.a ./tmp/RandomSolver.a ./tmp/Random.a ./tmp/Lib.a ./tmp/Biome.a ./tmp/Noise.a
	$(O_BUILD) ./main.exe ./src/main.cpp ./tmp/MinecraftLib.a ./tmp/StrongholdStructure.a ./tmp/RandomSolver.a ./tmp/Random.a ./tmp/Lib.a ./tmp/Biome.a ./tmp/Noise.a