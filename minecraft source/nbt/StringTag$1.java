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
/*    */ class null
/*    */   extends Object
/*    */   implements TagType.VariableSize<StringTag>
/*    */ {
/* 19 */   public StringTag load(DataInput input, NbtAccounter accounter) throws IOException { return StringTag.valueOf(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static String readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 28 */     accounter.accountBytes(36L);
/*    */ 
/*    */     
/* 31 */     String data = input.readUTF();
/* 32 */     accounter.accountBytes(2L, data.length());
/* 33 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void skip(DataInput input, NbtAccounter accounter) throws IOException { StringTag.skipString(input); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public String getName() { return "STRING"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public String getPrettyName() { return "TAG_String"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\StringTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */