/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ 
/*    */ public class ForceUnmount
/*    */   extends Behavior<LivingEntity> {
/* 10 */   public ForceUnmount() { super(ImmutableMap.of()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity body) { return body.isPassenger(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected void start(ServerLevel level, LivingEntity body, long timestamp) { body.unRide(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\ForceUnmount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */