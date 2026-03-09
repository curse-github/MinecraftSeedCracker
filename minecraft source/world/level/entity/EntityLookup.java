/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntityLookup<T extends EntityAccess>
/*    */   extends Object {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 18 */   private final Int2ObjectMap<T> byId = new Int2ObjectLinkedOpenHashMap();
/* 19 */   private final Map<UUID, T> byUuid = Maps.newHashMap();
/*    */   
/*    */   public <U extends T> void getEntities(EntityTypeTest<T, U> type, AbortableIterationConsumer<U> consumer) {
/* 22 */     for (ObjectIterator objectIterator = this.byId.values().iterator(); objectIterator.hasNext(); ) { T entity = (T)(EntityAccess)objectIterator.next();
/* 23 */       U maybeEntity = (U)(EntityAccess)type.tryCast(entity);
/* 24 */       if (maybeEntity != null && 
/* 25 */         consumer.accept(maybeEntity).shouldAbort()) {
/*    */         return;
/*    */       } }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public Iterable<T> getAllEntities() { return Iterables.unmodifiableIterable(this.byId.values()); }
/*    */ 
/*    */   
/*    */   public void add(T entity) {
/* 37 */     UUID uuid = entity.getUUID();
/* 38 */     if (this.byUuid.containsKey(uuid)) {
/* 39 */       LOGGER.warn("Duplicate entity UUID {}: {}", uuid, entity);
/*    */       return;
/*    */     } 
/* 42 */     this.byUuid.put(uuid, entity);
/* 43 */     this.byId.put(entity.getId(), entity);
/*    */   }
/*    */   
/*    */   public void remove(T entity) {
/* 47 */     this.byUuid.remove(entity.getUUID());
/* 48 */     this.byId.remove(entity.getId());
/*    */   }
/*    */ 
/*    */   
/* 52 */   public T getEntity(int id) { return (T)(EntityAccess)this.byId.get(id); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public T getEntity(UUID id) { return (T)(EntityAccess)this.byUuid.get(id); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public int count() { return this.byUuid.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntityLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */