/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public class SleepInBed
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   public static final int COOLDOWN_AFTER_BEING_WOKEN = 100;
/*    */   private long nextOkStartTime;
/*    */   
/* 26 */   public SleepInBed() { super(ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity body) {
/* 34 */     if (body.isPassenger()) {
/* 35 */       return false;
/*    */     }
/* 37 */     Brain<?> brain = body.getBrain();
/*    */     
/* 39 */     GlobalPos target = (GlobalPos)brain.getMemory(MemoryModuleType.HOME).get();
/* 40 */     if (level.dimension() != target.dimension()) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     Optional<Long> lastWokenMemory = brain.getMemory(MemoryModuleType.LAST_WOKEN);
/* 45 */     if (lastWokenMemory.isPresent()) {
/* 46 */       long timeSinceLastWoken = level.getGameTime() - ((Long)lastWokenMemory.get()).longValue();
/* 47 */       if (timeSinceLastWoken > 0L && timeSinceLastWoken < 100L)
/*    */       {
/* 49 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 53 */     BlockState blockState = level.getBlockState(target.pos());
/* 54 */     return (target.pos().closerToCenterThan(body.position(), 2.0D) && blockState.is(BlockTags.BEDS) && !((Boolean)blockState.getValue(BedBlock.OCCUPIED)).booleanValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, LivingEntity body, long timestamp) {
/* 59 */     Optional<GlobalPos> memory = body.getBrain().getMemory(MemoryModuleType.HOME);
/*    */     
/* 61 */     if (memory.isEmpty()) {
/* 62 */       return false;
/*    */     }
/*    */     
/* 65 */     BlockPos bedPos = ((GlobalPos)memory.get()).pos();
/* 66 */     return (body.getBrain().isActive(Activity.REST) && body.getY() > bedPos.getY() + 0.4D && bedPos.closerToCenterThan(body.position(), 1.14D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, LivingEntity body, long timestamp) {
/* 71 */     if (timestamp > this.nextOkStartTime) {
/* 72 */       Brain<?> brain = body.getBrain();
/*    */       
/* 74 */       if (brain.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
/* 75 */         Optional<List<LivingEntity>> nearestEntities; Set<GlobalPos> doors = (Set)brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get();
/*    */         
/* 77 */         if (brain.hasMemoryValue(MemoryModuleType.NEAREST_LIVING_ENTITIES)) {
/* 78 */           nearestEntities = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*    */         } else {
/* 80 */           nearestEntities = Optional.empty();
/*    */         } 
/*    */         
/* 83 */         InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(level, body, null, null, doors, nearestEntities);
/*    */       } 
/* 85 */       body.startSleeping(((GlobalPos)body.getBrain().getMemory(MemoryModuleType.HOME).get()).pos());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 91 */   protected boolean timedOut(long timestamp) { return false; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, LivingEntity body, long timestamp) {
/* 96 */     if (body.isSleeping()) {
/* 97 */       body.stopSleeping();
/* 98 */       this.nextOkStartTime = timestamp + 40L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SleepInBed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */