/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class Giant
/*    */   extends Monster {
/* 12 */   public Giant(EntityType<? extends Giant> type, Level level) { super(type, level); }
/*    */ 
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 16 */     return Monster.createMonsterAttributes()
/* 17 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 18 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/* 19 */       .add(Attributes.ATTACK_DAMAGE, 50.0D)
/* 20 */       .add(Attributes.CAMERA_DISTANCE, 16.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return level.getPathfindingCostFromLightLevels(pos); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Giant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */