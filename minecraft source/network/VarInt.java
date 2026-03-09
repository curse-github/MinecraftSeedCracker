/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ public class VarInt {
/*    */   public static final int MAX_VARINT_SIZE = 5;
/*    */   private static final int DATA_BITS_MASK = 127;
/*    */   private static final int CONTINUATION_BIT_MASK = 128;
/*    */   private static final int DATA_BITS_PER_BYTE = 7;
/*    */   
/*    */   public static int getByteSize(int value) {
/* 12 */     for (int i = 1; i < 5; i++) {
/* 13 */       if ((value & -1 << i * 7) == 0) {
/* 14 */         return i;
/*    */       }
/*    */     } 
/* 17 */     return 5;
/*    */   }
/*    */ 
/*    */   
/* 21 */   public static boolean hasContinuationBit(byte in) { return ((in & 0x80) == 128); }
/*    */   
/*    */   public static int read(ByteBuf input) {
/*    */     byte in;
/* 25 */     int out = 0;
/* 26 */     int bytes = 0;
/*    */     do {
/* 28 */       in = input.readByte();
/*    */       
/* 30 */       out |= (in & 0x7F) << bytes++ * 7;
/*    */       
/* 32 */       if (bytes > 5) {
/* 33 */         throw new RuntimeException("VarInt too big");
/*    */       }
/*    */     }
/* 36 */     while (hasContinuationBit(in));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return out;
/*    */   }
/*    */   
/*    */   public static ByteBuf write(ByteBuf output, int value) {
/*    */     while (true) {
/* 46 */       if ((value & 0xFFFFFF80) == 0) {
/* 47 */         output.writeByte(value);
/* 48 */         return output;
/*    */       } 
/*    */       
/* 51 */       output.writeByte(value & 0x7F | 0x80);
/* 52 */       value >>>= 7;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\VarInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */