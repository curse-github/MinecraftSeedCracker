/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<ByteBuf, Rotations>
/*    */ {
/*    */   public Rotations decode(ByteBuf input) {
/* 26 */     return new Rotations(input
/* 27 */         .readFloat(), input
/* 28 */         .readFloat(), input
/* 29 */         .readFloat());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void encode(ByteBuf output, Rotations value) {
/* 35 */     output.writeFloat(value.x);
/* 36 */     output.writeFloat(value.y);
/* 37 */     output.writeFloat(value.z);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Rotations$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */