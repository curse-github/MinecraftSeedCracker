/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements TagType<EndTag>
/*    */ {
/*    */   public EndTag load(DataInput input, NbtAccounter accounter) {
/* 15 */     accounter.accountBytes(8L);
/* 16 */     return EndTag.INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) {
/* 21 */     accounter.accountBytes(8L);
/* 22 */     return output.visitEnd();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void skip(DataInput input, int count, NbtAccounter accounter) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) {}
/*    */ 
/*    */ 
/*    */   
/* 35 */   public String getName() { return "END"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public String getPrettyName() { return "TAG_End"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\EndTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */