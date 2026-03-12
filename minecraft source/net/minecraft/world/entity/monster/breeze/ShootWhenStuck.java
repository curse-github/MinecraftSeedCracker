/*    */ package net.minecraft.world.entity.monster.breeze;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ public class ShootWhenStuck
/*    */   extends Behavior<Breeze>
/*    */ {
/* 16 */   public ShootWhenStuck() { super(Map.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREEZE_JUMP_INHALING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_JUMP_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_SHOOT, MemoryStatus.VALUE_ABSENT)); }
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
/* 27 */   protected boolean checkExtraStartConditions(ServerLevel level, Breeze breeze) { return (breeze.isPassenger() || breeze.isInWater() || breeze.getEffect(MobEffects.LEVITATION) != null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected boolean canStillUse(ServerLevel level, Breeze body, long timestamp) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected void start(ServerLevel level, Breeze breeze, long timestamp) { breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\ShootWhenStuck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */