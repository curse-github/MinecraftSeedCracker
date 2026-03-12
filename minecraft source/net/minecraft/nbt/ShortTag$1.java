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
/*    */ class null
/*    */   extends Object
/*    */   implements TagType.StaticSize<ShortTag>
/*    */ {
/* 30 */   public ShortTag load(DataInput input, NbtAccounter accounter) throws IOException { return ShortTag.valueOf(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static short readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 39 */     accounter.accountBytes(10L);
/* 40 */     return input.readShort();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public int size() { return 2; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public String getName() { return "SHORT"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String getPrettyName() { return "TAG_Short"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ShortTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */