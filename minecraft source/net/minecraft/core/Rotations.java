/*    */ package net.minecraft.core;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class Rotations extends Record {
/*    */   private final float x;
/*    */   private final float y;
/*    */   private final float z;
/*    */   
/* 13 */   public float x() { return this.x; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/Rotations;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/Rotations; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Rotations;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/Rotations; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/Rotations;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/Rotations;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public float y() { return this.y; } public float z() { return this.z; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final Codec<Rotations> CODEC = Codec.FLOAT.listOf().comapFlatMap(input -> 
/* 19 */       Util.fixedSize(input, 3).map(()), rotations -> 
/* 20 */       List.of(Float.valueOf(rotations.x()), Float.valueOf(rotations.y()), Float.valueOf(rotations.z())));
/*    */ 
/*    */   
/* 23 */   public static final StreamCodec<ByteBuf, Rotations> STREAM_CODEC = new StreamCodec<ByteBuf, Rotations>()
/*    */     {
/*    */       public Rotations decode(ByteBuf input) {
/* 26 */         return new Rotations(input
/* 27 */             .readFloat(), input
/* 28 */             .readFloat(), input
/* 29 */             .readFloat());
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void encode(ByteBuf output, Rotations value) {
/* 35 */         output.writeFloat(value.x);
/* 36 */         output.writeFloat(value.y);
/* 37 */         output.writeFloat(value.z);
/*    */       }
/*    */     };
/*    */   
/*    */   public Rotations(float x, float y, float z) {
/* 42 */     x = (Float.isInfinite(x) || Float.isNaN(x)) ? 0.0F : (x % 360.0F);
/* 43 */     y = (Float.isInfinite(y) || Float.isNaN(y)) ? 0.0F : (y % 360.0F);
/* 44 */     z = (Float.isInfinite(z) || Float.isNaN(z)) ? 0.0F : (z % 360.0F);
/*    */     this.x = x;
/*    */     this.y = y;
/*    */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Rotations.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */