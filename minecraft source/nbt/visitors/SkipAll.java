/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public interface SkipAll extends StreamTagVisitor {
/*  7 */   public static final SkipAll INSTANCE = new SkipAll()
/*    */     {
/*    */     
/*    */     };
/*    */   
/* 12 */   default StreamTagVisitor.ValueResult visitEnd() { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   default StreamTagVisitor.ValueResult visit(String value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   default StreamTagVisitor.ValueResult visit(byte value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   default StreamTagVisitor.ValueResult visit(short value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   default StreamTagVisitor.ValueResult visit(int value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   default StreamTagVisitor.ValueResult visit(long value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   default StreamTagVisitor.ValueResult visit(float value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   default StreamTagVisitor.ValueResult visit(double value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   default StreamTagVisitor.ValueResult visit(byte[] value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   default StreamTagVisitor.ValueResult visit(int[] value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   default StreamTagVisitor.ValueResult visit(long[] value) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   default StreamTagVisitor.ValueResult visitList(TagType<?> elementType, int size) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   default StreamTagVisitor.EntryResult visitElement(TagType<?> type, int index) { return StreamTagVisitor.EntryResult.SKIP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   default StreamTagVisitor.EntryResult visitEntry(TagType<?> type) { return StreamTagVisitor.EntryResult.SKIP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   default StreamTagVisitor.EntryResult visitEntry(TagType<?> type, String id) { return StreamTagVisitor.EntryResult.SKIP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   default StreamTagVisitor.ValueResult visitContainerEnd() { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 92 */   default StreamTagVisitor.ValueResult visitRootEntry(TagType<?> type) { return StreamTagVisitor.ValueResult.CONTINUE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\visitors\SkipAll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */