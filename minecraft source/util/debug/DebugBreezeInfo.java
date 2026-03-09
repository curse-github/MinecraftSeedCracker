/*    */ package net.minecraft.util.debug;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public final class DebugBreezeInfo extends Record {
/*    */   private final Optional<Integer> attackTarget;
/*    */   private final Optional<BlockPos> jumpTarget;
/*    */   
/* 10 */   public DebugBreezeInfo(Optional<Integer> attackTarget, Optional<BlockPos> jumpTarget) { this.attackTarget = attackTarget; this.jumpTarget = jumpTarget; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugBreezeInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo; } public Optional<Integer> attackTarget() { return this.attackTarget; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugBreezeInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugBreezeInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<BlockPos> jumpTarget() { return this.jumpTarget; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, DebugBreezeInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT
/* 15 */       .apply(ByteBufCodecs::optional), DebugBreezeInfo::attackTarget, BlockPos.STREAM_CODEC
/* 16 */       .apply(ByteBufCodecs::optional), DebugBreezeInfo::jumpTarget, DebugBreezeInfo::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugBreezeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */