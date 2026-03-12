/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import net.minecraft.util.ClassInstanceMultiMap;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntitySection<T extends EntityAccess> extends Object {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ClassInstanceMultiMap<T> storage;
/*    */   private Visibility chunkStatus;
/*    */   
/*    */   public EntitySection(Class<T> entityClass, Visibility chunkStatus) {
/* 20 */     this.chunkStatus = chunkStatus;
/* 21 */     this.storage = new ClassInstanceMultiMap(entityClass);
/*    */   }
/*    */ 
/*    */   
/* 25 */   public void add(T entity) { this.storage.add(entity); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public boolean remove(T entity) { return this.storage.remove(entity); }
/*    */ 
/*    */   
/*    */   public AbortableIterationConsumer.Continuation getEntities(AABB bb, AbortableIterationConsumer<T> entities) {
/* 33 */     for (Iterator iterator = this.storage.iterator(); iterator.hasNext(); ) { T entity = (T)(EntityAccess)iterator.next();
/* 34 */       if (entity.getBoundingBox().intersects(bb) && 
/* 35 */         entities.accept(entity).shouldAbort()) {
/* 36 */         return AbortableIterationConsumer.Continuation.ABORT;
/*    */       } }
/*    */ 
/*    */     
/* 40 */     return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */   }
/*    */   
/*    */   public <U extends T> AbortableIterationConsumer.Continuation getEntities(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<? super U> consumer) {
/* 44 */     Collection<? extends T> foundEntities = this.storage.find(type.getBaseClass());
/* 45 */     if (foundEntities.isEmpty()) {
/* 46 */       return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */     }
/* 48 */     for (Iterator iterator = foundEntities.iterator(); iterator.hasNext(); ) { T entity = (T)(EntityAccess)iterator.next();
/* 49 */       U maybeEntity = (U)(EntityAccess)type.tryCast(entity);
/* 50 */       if (maybeEntity != null && entity.getBoundingBox().intersects(bb) && 
/* 51 */         consumer.accept(maybeEntity).shouldAbort()) {
/* 52 */         return AbortableIterationConsumer.Continuation.ABORT;
/*    */       } }
/*    */ 
/*    */     
/* 56 */     return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/* 60 */   public boolean isEmpty() { return this.storage.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public Stream<T> getEntities() { return this.storage.stream(); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public Visibility getStatus() { return this.chunkStatus; }
/*    */ 
/*    */   
/*    */   public Visibility updateChunkStatus(Visibility chunkStatus) {
/* 72 */     Visibility prev = this.chunkStatus;
/* 73 */     this.chunkStatus = chunkStatus;
/* 74 */     return prev;
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForDebug
/* 79 */   public int size() { return this.storage.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntitySection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */