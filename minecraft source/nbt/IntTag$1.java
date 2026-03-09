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
/*    */   implements TagType.StaticSize<IntTag>
/*    */ {
/* 30 */   public IntTag load(DataInput input, NbtAccounter accounter) throws IOException { return IntTag.valueOf(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static int readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 39 */     accounter.accountBytes(12L);
/* 40 */     return input.readInt();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public int size() { return 4; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public String getName() { return "INT"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String getPrettyName() { return "TAG_Int"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\IntTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */