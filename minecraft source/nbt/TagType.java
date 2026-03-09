/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public interface TagType<T extends Tag> {
/*    */   T load(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   StreamTagVisitor.ValueResult parse(DataInput paramDataInput, StreamTagVisitor paramStreamTagVisitor, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   default void parseRoot(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 12 */     switch (output.visitRootEntry(this)) { case CONTINUE:
/* 13 */         parse(input, output, accounter);
/*    */         break;
/*    */       case BREAK:
/* 16 */         skip(input, accounter);
/*    */         break; }
/*    */   
/*    */   }
/*    */   
/*    */   void skip(DataInput paramDataInput, int paramInt, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   void skip(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException;
/*    */   
/*    */   String getName();
/*    */   
/*    */   String getPrettyName();
/*    */   
/*    */   public static interface StaticSize<T extends Tag>
/*    */     extends TagType<T> {
/* 31 */     default void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(size()); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException { input.skipBytes(size() * count); }
/*    */     
/*    */     int size();
/*    */   }
/*    */   
/*    */   public static interface VariableSize<T extends Tag>
/*    */     extends TagType<T>
/*    */   {
/*    */     default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 45 */       for (int i = 0; i < count; i++) {
/* 46 */         skip(input, accounter);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   static TagType<EndTag> createInvalid(final int id) {
/* 52 */     return new TagType<EndTag>()
/*    */       {
/* 54 */         private IOException createException() { return new IOException("Invalid tag id: " + id); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 59 */         public EndTag load(DataInput input, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 64 */         public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 69 */         public void skip(DataInput input, int count, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 74 */         public void skip(DataInput input, NbtAccounter accounter) throws IOException { throw createException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 79 */         public String getName() { return "INVALID[" + id + "]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 84 */         public String getPrettyName() { return "UNKNOWN_" + id; }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\TagType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */