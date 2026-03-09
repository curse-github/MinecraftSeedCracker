/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.animal.frog.Frog;
/*    */ 
/*    */ public class Croak
/*    */   extends Behavior<Frog>
/*    */ {
/*    */   private static final int CROAK_TICKS = 60;
/*    */   private static final int TIME_OUT_DURATION = 100;
/*    */   private int croakCounter;
/*    */   
/* 18 */   public Croak() { super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 100); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected boolean checkExtraStartConditions(ServerLevel level, Frog body) { return (body.getPose() == Pose.STANDING); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected boolean canStillUse(ServerLevel level, Frog body, long timestamp) { return (this.croakCounter < 60); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Frog body, long timestamp) {
/* 35 */     if (body.isInLiquid()) {
/*    */       return;
/*    */     }
/* 38 */     body.setPose(Pose.CROAKING);
/* 39 */     this.croakCounter = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected void stop(ServerLevel level, Frog body, long timestamp) { body.setPose(Pose.STANDING); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected void tick(ServerLevel level, Frog body, long timestamp) { this.croakCounter++; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Croak.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */