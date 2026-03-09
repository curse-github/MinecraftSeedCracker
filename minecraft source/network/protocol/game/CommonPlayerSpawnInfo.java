/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public final class CommonPlayerSpawnInfo extends Record {
/*    */   private final Holder<DimensionType> dimensionType;
/*    */   private final ResourceKey<Level> dimension;
/*    */   private final long seed;
/*    */   private final GameType gameType;
/*    */   private final GameType previousGameType;
/*    */   
/* 16 */   public CommonPlayerSpawnInfo(Holder<DimensionType> dimensionType, ResourceKey<Level> dimension, long seed, GameType gameType, GameType previousGameType, boolean isDebug, boolean isFlat, Optional<GlobalPos> lastDeathLocation, int portalCooldown, int seaLevel) { this.dimensionType = dimensionType; this.dimension = dimension; this.seed = seed; this.gameType = gameType; this.previousGameType = previousGameType; this.isDebug = isDebug; this.isFlat = isFlat; this.lastDeathLocation = lastDeathLocation; this.portalCooldown = portalCooldown; this.seaLevel = seaLevel; } private final boolean isDebug; private final boolean isFlat; private final Optional<GlobalPos> lastDeathLocation; private final int portalCooldown; private final int seaLevel; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<DimensionType> dimensionType() { return this.dimensionType; } public ResourceKey<Level> dimension() { return this.dimension; } public long seed() { return this.seed; } public GameType gameType() { return this.gameType; } public GameType previousGameType() { return this.previousGameType; } public boolean isDebug() { return this.isDebug; } public boolean isFlat() { return this.isFlat; } public Optional<GlobalPos> lastDeathLocation() { return this.lastDeathLocation; } public int portalCooldown() { return this.portalCooldown; } public int seaLevel() { return this.seaLevel; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CommonPlayerSpawnInfo(RegistryFriendlyByteBuf input) {
/* 29 */     this((Holder)DimensionType.STREAM_CODEC
/* 30 */         .decode(input), input
/* 31 */         .readResourceKey(Registries.DIMENSION), input
/* 32 */         .readLong(), 
/* 33 */         GameType.byId(input.readByte()), 
/* 34 */         GameType.byNullableId(input.readByte()), input
/* 35 */         .readBoolean(), input
/* 36 */         .readBoolean(), input
/* 37 */         .readOptional(FriendlyByteBuf::readGlobalPos), input
/* 38 */         .readVarInt(), input
/* 39 */         .readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(RegistryFriendlyByteBuf output) {
/* 44 */     DimensionType.STREAM_CODEC.encode(output, this.dimensionType);
/* 45 */     output.writeResourceKey(this.dimension);
/* 46 */     output.writeLong(this.seed);
/* 47 */     output.writeByte(this.gameType.getId());
/* 48 */     output.writeByte(GameType.getNullableId(this.previousGameType));
/* 49 */     output.writeBoolean(this.isDebug);
/* 50 */     output.writeBoolean(this.isFlat);
/* 51 */     output.writeOptional(this.lastDeathLocation, FriendlyByteBuf::writeGlobalPos);
/* 52 */     output.writeVarInt(this.portalCooldown);
/* 53 */     output.writeVarInt(this.seaLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\CommonPlayerSpawnInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */