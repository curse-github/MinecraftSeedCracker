/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EatBlockGoal
/*    */   extends Goal
/*    */ {
/*    */   private static final int EAT_ANIMATION_TICKS = 40;
/* 21 */   private static final Predicate<BlockState> IS_EDIBLE = state -> state.is(BlockTags.EDIBLE_FOR_SHEEP);
/*    */   
/*    */   private final Mob mob;
/*    */   private final Level level;
/*    */   private int eatAnimationTick;
/*    */   
/*    */   public EatBlockGoal(Mob mob) {
/* 28 */     this.mob = mob;
/* 29 */     this.level = mob.level();
/* 30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 35 */     if (this.mob.getRandom().nextInt(adjustedTickDelay(this.mob.isBaby() ? 50 : 1000)) != 0) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     BlockPos pos = this.mob.blockPosition();
/* 40 */     if (IS_EDIBLE.test(this.level.getBlockState(pos))) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (this.level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 51 */     this.eatAnimationTick = adjustedTickDelay(40);
/* 52 */     this.level.broadcastEntityEvent(this.mob, (byte)10);
/* 53 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public void stop() { this.eatAnimationTick = 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public boolean canContinueToUse() { return (this.eatAnimationTick > 0); }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public int getEatAnimationTick() { return this.eatAnimationTick; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 72 */     this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
/* 73 */     if (this.eatAnimationTick != adjustedTickDelay(4)) {
/*    */       return;
/*    */     }
/*    */     
/* 77 */     BlockPos pos = this.mob.blockPosition();
/*    */     
/* 79 */     if (IS_EDIBLE.test(this.level.getBlockState(pos))) {
/* 80 */       if (((Boolean)getServerLevel(this.level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 81 */         this.level.destroyBlock(pos, false);
/*    */       }
/* 83 */       this.mob.ate();
/*    */     } else {
/* 85 */       BlockPos below = pos.below();
/* 86 */       if (this.level.getBlockState(below).is(Blocks.GRASS_BLOCK)) {
/* 87 */         if (((Boolean)getServerLevel(this.level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 88 */           this.level.levelEvent(2001, below, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
/* 89 */           this.level.setBlock(below, Blocks.DIRT.defaultBlockState(), 2);
/*    */         } 
/* 91 */         this.mob.ate();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\EatBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */