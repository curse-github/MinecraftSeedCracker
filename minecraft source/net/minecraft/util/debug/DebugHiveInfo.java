/*    */ package net.minecraft.util.debug;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ 
/*    */ public final class DebugHiveInfo extends Record {
/*    */   private final Block type;
/*    */   private final int occupantCount;
/*    */   private final int honeyLevel;
/*    */   private final boolean sedated;
/*    */   
/* 10 */   public DebugHiveInfo(Block type, int occupantCount, int honeyLevel, boolean sedated) { this.type = type; this.occupantCount = occupantCount; this.honeyLevel = honeyLevel; this.sedated = sedated; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugHiveInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugHiveInfo; } public Block type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugHiveInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugHiveInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugHiveInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugHiveInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int occupantCount() { return this.occupantCount; } public int honeyLevel() { return this.honeyLevel; } public boolean sedated() { return this.sedated; }
/* 11 */   public static final StreamCodec<RegistryFriendlyByteBuf, DebugHiveInfo> STREAM_CODEC = StreamCodec.composite(
/* 12 */       ByteBufCodecs.registry(Registries.BLOCK), DebugHiveInfo::type, ByteBufCodecs.VAR_INT, DebugHiveInfo::occupantCount, ByteBufCodecs.VAR_INT, DebugHiveInfo::honeyLevel, ByteBufCodecs.BOOL, DebugHiveInfo::sedated, DebugHiveInfo::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DebugHiveInfo pack(BeehiveBlockEntity beehive) {
/* 20 */     return new DebugHiveInfo(beehive
/* 21 */         .getBlockState().getBlock(), beehive
/* 22 */         .getOccupantCount(), 
/* 23 */         BeehiveBlockEntity.getHoneyLevel(beehive.getBlockState()), beehive
/* 24 */         .isSedated());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugHiveInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */