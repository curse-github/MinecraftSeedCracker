/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Spliterator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
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
/*    */ public abstract class ListBacked<T>
/*    */   extends Object
/*    */   implements HolderSet<T>
/*    */ {
/*    */   protected abstract List<Holder<T>> contents();
/*    */   
/* 43 */   public int size() { return contents().size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public Spliterator<Holder<T>> spliterator() { return contents().spliterator(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public Iterator<Holder<T>> iterator() { return contents().iterator(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public Stream<Holder<T>> stream() { return contents().stream(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public Optional<Holder<T>> getRandomElement(RandomSource random) { return Util.getRandomSafe(contents(), random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public Holder<T> get(int index) { return (Holder)contents().get(index); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 73 */   public boolean canSerializeIn(HolderOwner<T> owner) { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderSet$ListBacked.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */