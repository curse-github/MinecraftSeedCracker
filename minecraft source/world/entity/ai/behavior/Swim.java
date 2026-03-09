/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class Swim<T extends Mob>
/*    */   extends Behavior<T>
/*    */ {
/*    */   private final float chance;
/*    */   
/*    */   public Swim(float chance) {
/* 15 */     super(ImmutableMap.of());
/* 16 */     this.chance = chance;
/*    */   }
/*    */ 
/*    */   
/* 20 */   public static <T extends Mob> boolean shouldSwim(T mob) { return ((mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold()) || mob.isInLava()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) { return shouldSwim(body); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) { return checkExtraStartConditions(level, body); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 35 */     if (body.getRandom().nextFloat() < this.chance)
/* 36 */       body.getJumpControl().jump(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Swim.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */