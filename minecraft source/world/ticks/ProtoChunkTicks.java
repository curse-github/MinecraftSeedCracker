/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class ProtoChunkTicks<T> extends Object implements TickContainerAccess<T>, SerializableTickContainer<T> {
/* 11 */   private final List<SavedTick<T>> ticks = Lists.newArrayList();
/*    */ 
/*    */   
/* 14 */   private final Set<SavedTick<?>> ticksPerPosition = new ObjectOpenCustomHashSet(SavedTick.UNIQUE_TICK_HASH);
/*    */ 
/*    */ 
/*    */   
/*    */   public void schedule(ScheduledTick<T> tick) {
/* 19 */     SavedTick<T> newTick = new SavedTick<T>(tick.type(), tick.pos(), 0, tick.priority());
/* 20 */     schedule(newTick);
/*    */   }
/*    */   
/*    */   private void schedule(SavedTick<T> newTick) {
/* 24 */     if (this.ticksPerPosition.add(newTick)) {
/* 25 */       this.ticks.add(newTick);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean hasScheduledTick(BlockPos pos, T type) { return this.ticksPerPosition.contains(SavedTick.probe(type, pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int count() { return this.ticks.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public List<SavedTick<T>> pack(long currentTick) { return this.ticks; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public List<SavedTick<T>> scheduledTicks() { return List.copyOf(this.ticks); }
/*    */ 
/*    */   
/*    */   public static <T> ProtoChunkTicks<T> load(List<SavedTick<T>> ticks) {
/* 49 */     ProtoChunkTicks<T> result = new ProtoChunkTicks<T>();
/* 50 */     Objects.requireNonNull(result); ticks.forEach(result::schedule);
/* 51 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\ProtoChunkTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */