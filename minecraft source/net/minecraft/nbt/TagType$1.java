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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements TagType<EndTag>
/*    */ {
/* 54 */   private IOException createException() { return new IOException("Invalid tag id: " + id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public EndTag load(DataInput input, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public void skip(DataInput input, int count, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public void skip(DataInput input, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public String getName() { return "INVALID[" + id + "]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   public String getPrettyName() { return "UNKNOWN_" + id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\TagType$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */