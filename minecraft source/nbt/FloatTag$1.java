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
/*    */ class null
/*    */   extends Object
/*    */   implements TagType.StaticSize<FloatTag>
/*    */ {
/* 22 */   public FloatTag load(DataInput input, NbtAccounter accounter) throws IOException { return FloatTag.valueOf(readAccounted(input, accounter)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(readAccounted(input, accounter)); }
/*    */ 
/*    */   
/*    */   private static float readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 31 */     accounter.accountBytes(12L);
/* 32 */     return input.readFloat();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int size() { return 4; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public String getName() { return "FLOAT"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String getPrettyName() { return "TAG_Float"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\FloatTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */