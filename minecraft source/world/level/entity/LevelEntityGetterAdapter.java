/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class LevelEntityGetterAdapter<T extends EntityAccess>
/*    */   extends Object
/*    */   implements LevelEntityGetter<T> {
/*    */   private final EntityLookup<T> visibleEntities;
/*    */   private final EntitySectionStorage<T> sectionStorage;
/*    */   
/*    */   public LevelEntityGetterAdapter(EntityLookup<T> visibleEntities, EntitySectionStorage<T> sectionStorage) {
/* 15 */     this.visibleEntities = visibleEntities;
/* 16 */     this.sectionStorage = sectionStorage;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public T get(int id) { return (T)this.visibleEntities.getEntity(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public T get(UUID id) { return (T)this.visibleEntities.getEntity(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Iterable<T> getAll() { return this.visibleEntities.getAllEntities(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public <U extends T> void get(EntityTypeTest<T, U> type, AbortableIterationConsumer<U> consumer) { this.visibleEntities.getEntities(type, consumer); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void get(AABB bb, Consumer<T> output) { this.sectionStorage.getEntities(bb, AbortableIterationConsumer.forConsumer(output)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public <U extends T> void get(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<U> consumer) { this.sectionStorage.getEntities(type, bb, consumer); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\LevelEntityGetterAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */