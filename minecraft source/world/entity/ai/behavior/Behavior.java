/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Behavior<E extends LivingEntity>
/*     */   extends Object
/*     */   implements BehaviorControl<E>
/*     */ {
/*     */   public static final int DEFAULT_DURATION = 60;
/*     */   protected final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*     */   private Status status;
/*     */   private long endTimestamp;
/*     */   private final int minDuration;
/*     */   private final int maxDuration;
/*     */   
/*  24 */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) { this(entryCondition, 60); }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int timeOutDuration) { this(entryCondition, timeOutDuration, timeOutDuration); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration, int maxDuration) {
/*     */     this.status = Status.STOPPED;
/*  35 */     this.minDuration = minDuration;
/*  36 */     this.maxDuration = maxDuration;
/*  37 */     this.entryCondition = entryCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public Status getStatus() { return this.status; }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/*  47 */     if (hasRequiredMemories(body) && checkExtraStartConditions(level, body)) {
/*  48 */       this.status = Status.RUNNING;
/*  49 */       int duration = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/*  50 */       this.endTimestamp = timestamp + duration;
/*  51 */       start(level, body, timestamp);
/*  52 */       return true;
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, E body, long timestamp) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/*  65 */     if (!timedOut(timestamp) && canStillUse(level, body, timestamp)) {
/*  66 */       tick(level, body, timestamp);
/*     */     } else {
/*  68 */       doStop(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void doStop(ServerLevel level, E body, long timestamp) {
/*  80 */     this.status = Status.STOPPED;
/*  81 */     stop(level, body, timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected boolean timedOut(long timestamp) { return (timestamp > this.endTimestamp); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   protected boolean checkExtraStartConditions(ServerLevel level, E body) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public String debugString() { return getClass().getSimpleName(); }
/*     */ 
/*     */   
/*     */   protected boolean hasRequiredMemories(E body) {
/* 123 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
/* 124 */       MemoryModuleType<?> memoryType = (MemoryModuleType)entry.getKey();
/* 125 */       MemoryStatus requiredStatus = (MemoryStatus)entry.getValue();
/* 126 */       if (!body.getBrain().checkMemory(memoryType, requiredStatus)) {
/* 127 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public enum Status {
/* 135 */     STOPPED,
/* 136 */     RUNNING;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Behavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */