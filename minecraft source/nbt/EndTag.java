/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public final class EndTag
/*    */   implements Tag
/*    */ {
/*    */   private static final int SELF_SIZE_IN_BYTES = 8;
/*    */   
/* 12 */   public static final TagType<EndTag> TYPE = new TagType<EndTag>()
/*    */     {
/*    */       public EndTag load(DataInput input, NbtAccounter accounter) {
/* 15 */         accounter.accountBytes(8L);
/* 16 */         return EndTag.INSTANCE;
/*    */       }
/*    */ 
/*    */       
/*    */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) {
/* 21 */         accounter.accountBytes(8L);
/* 22 */         return output.visitEnd();
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void skip(DataInput input, int count, NbtAccounter accounter) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public void skip(DataInput input, NbtAccounter accounter) {}
/*    */ 
/*    */ 
/*    */       
/* 35 */       public String getName() { return "END"; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 40 */       public String getPrettyName() { return "TAG_End"; }
/*    */     };
/*    */ 
/*    */   
/* 44 */   public static final EndTag INSTANCE = new EndTag();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(DataOutput output) throws IOException {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public int sizeInBytes() { return 8; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public byte getId() { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public TagType<EndTag> getType() { return TYPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     StringTagVisitor visitor = new StringTagVisitor();
/* 71 */     visitor.visitEnd(this);
/* 72 */     return visitor.build();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public EndTag copy() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   public void accept(TagVisitor visitor) { visitor.visitEnd(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visitEnd(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\EndTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */