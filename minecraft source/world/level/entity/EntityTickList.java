/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class EntityTickList
/*    */ {
/* 13 */   private Int2ObjectMap<Entity> active = new Int2ObjectLinkedOpenHashMap();
/* 14 */   private Int2ObjectMap<Entity> passive = new Int2ObjectLinkedOpenHashMap();
/*    */   private Int2ObjectMap<Entity> iterated;
/*    */   
/*    */   private void ensureActiveIsNotIterated() {
/* 18 */     if (this.iterated == this.active) {
/* 19 */       this.passive.clear();
/* 20 */       for (ObjectIterator objectIterator = Int2ObjectMaps.fastIterable(this.active).iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<Entity> entry = (Int2ObjectMap.Entry)objectIterator.next();
/* 21 */         this.passive.put(entry.getIntKey(), (Entity)entry.getValue()); }
/*    */       
/* 23 */       Int2ObjectMap<Entity> tmp = this.active;
/* 24 */       this.active = this.passive;
/* 25 */       this.passive = tmp;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Entity entity) {
/* 30 */     ensureActiveIsNotIterated();
/* 31 */     this.active.put(entity.getId(), entity);
/*    */   }
/*    */   
/*    */   public void remove(Entity entity) {
/* 35 */     ensureActiveIsNotIterated();
/* 36 */     this.active.remove(entity.getId());
/*    */   }
/*    */ 
/*    */   
/* 40 */   public boolean contains(Entity entity) { return this.active.containsKey(entity.getId()); }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<Entity> output) {
/* 44 */     if (this.iterated != null)
/*    */     {
/* 46 */       throw new UnsupportedOperationException("Only one concurrent iteration supported");
/*    */     }
/*    */     
/* 49 */     this.iterated = this.active;
/*    */     
/*    */     try {
/* 52 */       for (ObjectIterator objectIterator = this.active.values().iterator(); objectIterator.hasNext(); ) { Entity entity = (Entity)objectIterator.next();
/* 53 */         output.accept(entity); }
/*    */     
/*    */     } finally {
/* 56 */       this.iterated = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntityTickList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */