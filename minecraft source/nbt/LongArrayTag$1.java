/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
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
/*    */   implements TagType.VariableSize<LongArrayTag>
/*    */ {
/* 23 */   public LongArrayTag load(DataInput input, NbtAccounter accounter) throws IOException { return new LongArrayTag(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static long[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 32 */     accounter.accountBytes(24L);
/* 33 */     int length = input.readInt();
/* 34 */     accounter.accountBytes(8L, length);
/* 35 */     long[] data = new long[length];
/* 36 */     for (int i = 0; i < length; i++) {
/* 37 */       data[i] = input.readLong();
/*    */     }
/* 39 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(input.readInt() * 8); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String getName() { return "LONG[]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public String getPrettyName() { return "TAG_Long_Array"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\LongArrayTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */