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
/*    */   implements TagType.VariableSize<ByteArrayTag>
/*    */ {
/* 23 */   public ByteArrayTag load(DataInput input, NbtAccounter accounter) throws IOException { return new ByteArrayTag(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static byte[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 32 */     accounter.accountBytes(24L);
/* 33 */     int length = input.readInt();
/* 34 */     accounter.accountBytes(1L, length);
/* 35 */     byte[] data = new byte[length];
/* 36 */     input.readFully(data);
/* 37 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(input.readInt() * 1); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String getName() { return "BYTE[]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public String getPrettyName() { return "TAG_Byte_Array"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ByteArrayTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */