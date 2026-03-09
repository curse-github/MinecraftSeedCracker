/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OceanMonumentSimpleRoom
/*      */   extends OceanMonumentPieces.OceanMonumentPiece
/*      */ {
/*      */   private int mainDesign;
/*      */   
/*      */   public OceanMonumentSimpleRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/*  857 */     super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, 1, orientation, definition, 1, 1, 1);
/*  858 */     this.mainDesign = random.nextInt(3);
/*      */   }
/*      */ 
/*      */   
/*  862 */   public OceanMonumentSimpleRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, tag); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  867 */     if (this.roomDefinition.index / 25 > 0) {
/*  868 */       generateDefaultFloor(level, chunkBB, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */     }
/*  870 */     if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/*  871 */       generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */     }
/*      */     
/*  874 */     boolean centerPillar = (this.mainDesign != 0 && random.nextBoolean() && !this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()] && !this.roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && this.roomDefinition.countOpenings() > 1);
/*      */     
/*  876 */     if (this.mainDesign == 0) {
/*      */       
/*  878 */       generateBox(level, chunkBB, 0, 1, 0, 2, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  879 */       generateBox(level, chunkBB, 0, 3, 0, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  880 */       generateBox(level, chunkBB, 0, 2, 0, 0, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  881 */       generateBox(level, chunkBB, 1, 2, 0, 2, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  882 */       placeBlock(level, LAMP_BLOCK, 1, 2, 1, chunkBB);
/*      */ 
/*      */       
/*  885 */       generateBox(level, chunkBB, 5, 1, 0, 7, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  886 */       generateBox(level, chunkBB, 5, 3, 0, 7, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  887 */       generateBox(level, chunkBB, 7, 2, 0, 7, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  888 */       generateBox(level, chunkBB, 5, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  889 */       placeBlock(level, LAMP_BLOCK, 6, 2, 1, chunkBB);
/*      */ 
/*      */       
/*  892 */       generateBox(level, chunkBB, 0, 1, 5, 2, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  893 */       generateBox(level, chunkBB, 0, 3, 5, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  894 */       generateBox(level, chunkBB, 0, 2, 5, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  895 */       generateBox(level, chunkBB, 1, 2, 7, 2, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  896 */       placeBlock(level, LAMP_BLOCK, 1, 2, 6, chunkBB);
/*      */ 
/*      */       
/*  899 */       generateBox(level, chunkBB, 5, 1, 5, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  900 */       generateBox(level, chunkBB, 5, 3, 5, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  901 */       generateBox(level, chunkBB, 7, 2, 5, 7, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  902 */       generateBox(level, chunkBB, 5, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  903 */       placeBlock(level, LAMP_BLOCK, 6, 2, 6, chunkBB);
/*      */       
/*  905 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  906 */         generateBox(level, chunkBB, 3, 3, 0, 4, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  908 */         generateBox(level, chunkBB, 3, 3, 0, 4, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  909 */         generateBox(level, chunkBB, 3, 2, 0, 4, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  910 */         generateBox(level, chunkBB, 3, 1, 0, 4, 1, 1, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  912 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  913 */         generateBox(level, chunkBB, 3, 3, 7, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  915 */         generateBox(level, chunkBB, 3, 3, 6, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  916 */         generateBox(level, chunkBB, 3, 2, 7, 4, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  917 */         generateBox(level, chunkBB, 3, 1, 6, 4, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  919 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  920 */         generateBox(level, chunkBB, 0, 3, 3, 0, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  922 */         generateBox(level, chunkBB, 0, 3, 3, 1, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  923 */         generateBox(level, chunkBB, 0, 2, 3, 0, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  924 */         generateBox(level, chunkBB, 0, 1, 3, 1, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  926 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  927 */         generateBox(level, chunkBB, 7, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  929 */         generateBox(level, chunkBB, 6, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  930 */         generateBox(level, chunkBB, 7, 2, 3, 7, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  931 */         generateBox(level, chunkBB, 6, 1, 3, 7, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  933 */     } else if (this.mainDesign == 1) {
/*      */       
/*  935 */       generateBox(level, chunkBB, 2, 1, 2, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  936 */       generateBox(level, chunkBB, 2, 1, 5, 2, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  937 */       generateBox(level, chunkBB, 5, 1, 5, 5, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  938 */       generateBox(level, chunkBB, 5, 1, 2, 5, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  939 */       placeBlock(level, LAMP_BLOCK, 2, 2, 2, chunkBB);
/*  940 */       placeBlock(level, LAMP_BLOCK, 2, 2, 5, chunkBB);
/*  941 */       placeBlock(level, LAMP_BLOCK, 5, 2, 5, chunkBB);
/*  942 */       placeBlock(level, LAMP_BLOCK, 5, 2, 2, chunkBB);
/*      */ 
/*      */       
/*  945 */       generateBox(level, chunkBB, 0, 1, 0, 1, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  946 */       generateBox(level, chunkBB, 0, 1, 1, 0, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  947 */       generateBox(level, chunkBB, 0, 1, 7, 1, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  948 */       generateBox(level, chunkBB, 0, 1, 6, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  949 */       generateBox(level, chunkBB, 6, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  950 */       generateBox(level, chunkBB, 7, 1, 6, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  951 */       generateBox(level, chunkBB, 6, 1, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  952 */       generateBox(level, chunkBB, 7, 1, 1, 7, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  953 */       placeBlock(level, BASE_GRAY, 1, 2, 0, chunkBB);
/*  954 */       placeBlock(level, BASE_GRAY, 0, 2, 1, chunkBB);
/*  955 */       placeBlock(level, BASE_GRAY, 1, 2, 7, chunkBB);
/*  956 */       placeBlock(level, BASE_GRAY, 0, 2, 6, chunkBB);
/*  957 */       placeBlock(level, BASE_GRAY, 6, 2, 7, chunkBB);
/*  958 */       placeBlock(level, BASE_GRAY, 7, 2, 6, chunkBB);
/*  959 */       placeBlock(level, BASE_GRAY, 6, 2, 0, chunkBB);
/*  960 */       placeBlock(level, BASE_GRAY, 7, 2, 1, chunkBB);
/*  961 */       if (!this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  962 */         generateBox(level, chunkBB, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  963 */         generateBox(level, chunkBB, 1, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  964 */         generateBox(level, chunkBB, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  966 */       if (!this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  967 */         generateBox(level, chunkBB, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  968 */         generateBox(level, chunkBB, 1, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  969 */         generateBox(level, chunkBB, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  971 */       if (!this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  972 */         generateBox(level, chunkBB, 0, 3, 1, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  973 */         generateBox(level, chunkBB, 0, 2, 1, 0, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  974 */         generateBox(level, chunkBB, 0, 1, 1, 0, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  976 */       if (!this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  977 */         generateBox(level, chunkBB, 7, 3, 1, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  978 */         generateBox(level, chunkBB, 7, 2, 1, 7, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  979 */         generateBox(level, chunkBB, 7, 1, 1, 7, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  981 */     } else if (this.mainDesign == 2) {
/*  982 */       generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  983 */       generateBox(level, chunkBB, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  984 */       generateBox(level, chunkBB, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  985 */       generateBox(level, chunkBB, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  987 */       generateBox(level, chunkBB, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  988 */       generateBox(level, chunkBB, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  989 */       generateBox(level, chunkBB, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/*  990 */       generateBox(level, chunkBB, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/*  992 */       generateBox(level, chunkBB, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  993 */       generateBox(level, chunkBB, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  994 */       generateBox(level, chunkBB, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  995 */       generateBox(level, chunkBB, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  997 */       generateBox(level, chunkBB, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  998 */       generateBox(level, chunkBB, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  999 */       generateBox(level, chunkBB, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1000 */       generateBox(level, chunkBB, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1002 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1003 */         generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1005 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1006 */         generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1008 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1009 */         generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1011 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1012 */         generateWaterBox(level, chunkBB, 7, 1, 3, 7, 2, 4);
/*      */       }
/*      */     } 
/* 1015 */     if (centerPillar) {
/* 1016 */       generateBox(level, chunkBB, 3, 1, 3, 4, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1017 */       generateBox(level, chunkBB, 3, 2, 3, 4, 2, 4, BASE_GRAY, BASE_GRAY, false);
/* 1018 */       generateBox(level, chunkBB, 3, 3, 3, 4, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$OceanMonumentSimpleRoom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */