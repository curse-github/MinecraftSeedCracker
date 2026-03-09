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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements TagType.StaticSize<ByteTag>
/*    */ {
/* 28 */   public ByteTag load(DataInput input, NbtAccounter accounter) throws IOException { return ByteTag.valueOf(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static byte readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 37 */     accounter.accountBytes(9L);
/* 38 */     return input.readByte();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int size() { return 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public String getName() { return "BYTE"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public String getPrettyName() { return "TAG_Byte"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ByteTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */