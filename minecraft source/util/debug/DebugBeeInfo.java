/*    */ package net.minecraft.util.debug;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public final class DebugBeeInfo extends Record {
/*    */   private final Optional<BlockPos> hivePos;
/*    */   private final Optional<BlockPos> flowerPos;
/*    */   private final int travelTicks;
/*    */   private final List<BlockPos> blacklistedHives;
/*    */   
/* 11 */   public DebugBeeInfo(Optional<BlockPos> hivePos, Optional<BlockPos> flowerPos, int travelTicks, List<BlockPos> blacklistedHives) { this.hivePos = hivePos; this.flowerPos = flowerPos; this.travelTicks = travelTicks; this.blacklistedHives = blacklistedHives; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugBeeInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBeeInfo; } public Optional<BlockPos> hivePos() { return this.hivePos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugBeeInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBeeInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugBeeInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugBeeInfo;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<BlockPos> flowerPos() { return this.flowerPos; } public int travelTicks() { return this.travelTicks; } public List<BlockPos> blacklistedHives() { return this.blacklistedHives; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, DebugBeeInfo> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC
/* 18 */       .apply(ByteBufCodecs::optional), DebugBeeInfo::hivePos, BlockPos.STREAM_CODEC
/* 19 */       .apply(ByteBufCodecs::optional), DebugBeeInfo::flowerPos, ByteBufCodecs.VAR_INT, DebugBeeInfo::travelTicks, BlockPos.STREAM_CODEC
/*    */       
/* 21 */       .apply(ByteBufCodecs.list()), DebugBeeInfo::blacklistedHives, DebugBeeInfo::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean hasHive(BlockPos hivePos) { return (this.hivePos.isPresent() && hivePos.equals(this.hivePos.get())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugBeeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */