/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.stream.Stream;
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
/*    */   implements ValueInput.ValueInputList
/*    */ {
/*    */   null(ValueInputContextHelper this$0) {}
/*    */   
/* 21 */   public boolean isEmpty() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public Stream<ValueInput> stream() { return Stream.empty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Iterator<ValueInput> iterator() { return Collections.emptyIterator(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\ValueInputContextHelper$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */