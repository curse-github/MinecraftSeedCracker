/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Slime;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface NearbySlimes
/*    */ {
/*    */   int count(int paramInt);
/*    */   
/*    */   private static NearbySlimes closeTo(LivingEntity mob) {
/* 60 */     return maxResults -> {
/* 61 */         List<Slime> slimesNearby = new ArrayList<Slime>();
/* 62 */         mob.level().getEntities(EntityType.SLIME, mob.getBoundingBox().inflate(2.0D), (), slimesNearby, maxResults);
/* 63 */         return slimesNearby.size();
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\OozingMobEffect$NearbySlimes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */