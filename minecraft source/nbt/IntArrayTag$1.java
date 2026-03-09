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
/*    */   implements TagType.VariableSize<IntArrayTag>
/*    */ {
/* 23 */   public IntArrayTag load(DataInput input, NbtAccounter accounter) throws IOException { return new IntArrayTag(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static int[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 32 */     accounter.accountBytes(24L);
/*    */     
/* 34 */     int length = input.readInt();
/* 35 */     accounter.accountBytes(4L, length);
/* 36 */     int[] data = new int[length];
/* 37 */     for (int i = 0; i < length; i++) {
/* 38 */       data[i] = input.readInt();
/*    */     }
/* 40 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(input.readInt() * 4); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public String getName() { return "INT[]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String getPrettyName() { return "TAG_Int_Array"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\IntArrayTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */