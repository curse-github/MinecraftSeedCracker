/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.FurnaceBlock;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BedPart;
/*    */ 
/*    */ public class CatSitOnBlockGoal
/*    */   extends MoveToBlockGoal {
/*    */   public CatSitOnBlockGoal(Cat cat, double speedModifier) {
/* 18 */     super(cat, speedModifier, 8);
/* 19 */     this.cat = cat;
/*    */   }
/*    */   
/*    */   private final Cat cat;
/*    */   
/* 24 */   public boolean canUse() { return (this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 29 */     super.start();
/* 30 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 35 */     super.stop();
/* 36 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 41 */     super.tick();
/*    */     
/* 43 */     this.cat.setInSittingPose(isReachedTarget());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 48 */     if (!level.isEmptyBlock(pos.above())) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     BlockState blockState = level.getBlockState(pos);
/*    */ 
/*    */     
/* 55 */     if (blockState.is(Blocks.CHEST))
/* 56 */       return (ChestBlockEntity.getOpenCount(level, pos) < 1); 
/* 57 */     if (blockState.is(Blocks.FURNACE) && ((Boolean)blockState.getValue(FurnaceBlock.LIT)).booleanValue()) {
/* 58 */       return true;
/*    */     }
/* 60 */     return blockState.is(BlockTags.BEDS, s -> ((Boolean)s.getOptionalValue(BedBlock.PART).map(()).orElse(Boolean.valueOf(true))).booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\CatSitOnBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */