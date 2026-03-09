/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public final class Piece
/*    */   extends Record {
/*    */   private final BoundingBox boundingBox;
/*    */   private final boolean isStart;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugStructureInfo$Piece;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugStructureInfo$Piece; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugStructureInfo$Piece;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugStructureInfo$Piece; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugStructureInfo$Piece;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugStructureInfo$Piece;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 20 */   public Piece(BoundingBox boundingBox, boolean isStart) { this.boundingBox = boundingBox; this.isStart = isStart; } public BoundingBox boundingBox() { return this.boundingBox; } public boolean isStart() { return this.isStart; }
/* 21 */   public static final StreamCodec<ByteBuf, Piece> STREAM_CODEC = StreamCodec.composite(BoundingBox.STREAM_CODEC, Piece::boundingBox, ByteBufCodecs.BOOL, Piece::isStart, Piece::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugStructureInfo$Piece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */