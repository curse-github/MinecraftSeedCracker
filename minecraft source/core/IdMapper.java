/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public class IdMapper<T>
/*    */   extends Object
/*    */   implements IdMap<T>
/*    */ {
/*    */   private int nextId;
/*    */   private final Reference2IntMap<T> tToId;
/*    */   private final List<T> idToT;
/*    */   
/* 20 */   public IdMapper() { this(512); }
/*    */ 
/*    */   
/*    */   public IdMapper(int expectedSize) {
/* 24 */     this.idToT = Lists.newArrayListWithExpectedSize(expectedSize);
/* 25 */     this.tToId = new Reference2IntOpenHashMap(expectedSize);
/* 26 */     this.tToId.defaultReturnValue(-1);
/*    */   }
/*    */   
/*    */   public void addMapping(T thing, int id) {
/* 30 */     this.tToId.put(thing, id);
/*    */ 
/*    */     
/* 33 */     while (this.idToT.size() <= id) {
/* 34 */       this.idToT.add(null);
/*    */     }
/*    */     
/* 37 */     this.idToT.set(id, thing);
/*    */     
/* 39 */     if (this.nextId <= id) {
/* 40 */       this.nextId = id + 1;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 45 */   public void add(T thing) { addMapping(thing, this.nextId); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public int getId(T thing) { return this.tToId.getInt(thing); }
/*    */ 
/*    */ 
/*    */   
/*    */   public final T byId(int id) {
/* 55 */     if (id >= 0 && id < this.idToT.size()) {
/* 56 */       return (T)this.idToT.get(id);
/*    */     }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public Iterator<T> iterator() { return Iterators.filter(this.idToT.iterator(), Objects::nonNull); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public boolean contains(int id) { return (byId(id) != null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 73 */   public int size() { return this.tToId.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\IdMapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */