/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ public class ClimbOnTopOfPowderSnowGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private final Level level;
/*    */   
/*    */   public ClimbOnTopOfPowderSnowGoal(Mob mob, Level level) {
/* 18 */     this.mob = mob;
/* 19 */     this.level = level;
/* 20 */     setFlags(EnumSet.of(Goal.Flag.JUMP));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 25 */     boolean inPowderSnow = (this.mob.wasInPowderSnow || this.mob.isInPowderSnow);
/* 26 */     if (!inPowderSnow || !this.mob.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
/* 27 */       return false;
/*    */     }
/* 29 */     BlockPos above = this.mob.blockPosition().above();
/* 30 */     BlockState aboveBlockState = this.level.getBlockState(above);
/* 31 */     return (aboveBlockState.is(Blocks.POWDER_SNOW) || aboveBlockState.getCollisionShape(this.level, above) == Shapes.empty());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean requiresUpdateEveryTick() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void tick() { this.mob.getJumpControl().jump(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\ClimbOnTopOfPowderSnowGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */