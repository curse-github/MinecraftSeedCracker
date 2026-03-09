/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CatRelaxOnOwnerGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Cat cat;
/*     */   private Player ownerPlayer;
/*     */   private BlockPos goalPos;
/*     */   private int onBedTicks;
/*     */   
/* 552 */   public CatRelaxOnOwnerGoal(Cat cat) { this.cat = cat; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 557 */     if (!this.cat.isTame()) {
/* 558 */       return false;
/*     */     }
/*     */     
/* 561 */     if (this.cat.isOrderedToSit()) {
/* 562 */       return false;
/*     */     }
/*     */     
/* 565 */     LivingEntity owner = this.cat.getOwner();
/* 566 */     if (owner instanceof Player) { Player playerOwner = (Player)owner;
/* 567 */       this.ownerPlayer = playerOwner;
/*     */       
/* 569 */       if (!owner.isSleeping()) {
/* 570 */         return false;
/*     */       }
/*     */       
/* 573 */       if (this.cat.distanceToSqr(this.ownerPlayer) > 100.0D) {
/* 574 */         return false;
/*     */       }
/*     */       
/* 577 */       BlockPos ownerPos = this.ownerPlayer.blockPosition();
/* 578 */       BlockState ownerPosState = this.cat.level().getBlockState(ownerPos);
/* 579 */       if (ownerPosState.is(BlockTags.BEDS)) {
/* 580 */         this.goalPos = (BlockPos)ownerPosState.getOptionalValue(BedBlock.FACING).map(bedDir -> ownerPos.relative(bedDir.getOpposite())).orElseGet(() -> new BlockPos(ownerPos));
/* 581 */         return !spaceIsOccupied();
/*     */       }  }
/*     */     
/* 584 */     return false;
/*     */   }
/*     */   
/*     */   private boolean spaceIsOccupied() {
/* 588 */     List<Cat> cats = this.cat.level().getEntitiesOfClass(Cat.class, (new AABB(this.goalPos)).inflate(2.0D));
/* 589 */     for (Cat otherCat : cats) {
/* 590 */       if (otherCat != this.cat && (otherCat.isLying() || otherCat.isRelaxStateOne())) {
/* 591 */         return true;
/*     */       }
/*     */     } 
/* 594 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 599 */   public boolean canContinueToUse() { return (this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !spaceIsOccupied()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 604 */     if (this.goalPos != null) {
/* 605 */       this.cat.setInSittingPose(false);
/* 606 */       this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 612 */     this.cat.setLying(false);
/*     */     
/* 614 */     if (this.ownerPlayer.getSleepTimer() >= 100 && this.cat
/* 615 */       .level().getRandom().nextFloat() < ((Float)this.cat.level().environmentAttributes().getValue(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, this.cat.position())).floatValue()) {
/* 616 */       giveMorningGift();
/*     */     }
/*     */     
/* 619 */     this.onBedTicks = 0;
/* 620 */     this.cat.setRelaxStateOne(false);
/* 621 */     this.cat.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private void giveMorningGift() {
/* 625 */     RandomSource random = this.cat.getRandom();
/* 626 */     BlockPos.MutableBlockPos catPos = new BlockPos.MutableBlockPos();
/* 627 */     catPos.set(this.cat.isLeashed() ? this.cat.getLeashHolder().blockPosition() : this.cat.blockPosition());
/* 628 */     this.cat.randomTeleport((catPos.getX() + random.nextInt(11) - 5), (catPos.getY() + random.nextInt(5) - 2), (catPos.getZ() + random.nextInt(11) - 5), false);
/*     */     
/* 630 */     catPos.set(this.cat.blockPosition());
/* 631 */     this.cat.dropFromGiftLootTable(getServerLevel(this.cat), BuiltInLootTables.CAT_MORNING_GIFT, (level, itemStack) -> 
/* 632 */         level.addFreshEntity(new ItemEntity(level, catPos.getX() - Mth.sin((this.cat.yBodyRot * 0.017453292F)), catPos.getY(), catPos.getZ() + Mth.cos((this.cat.yBodyRot * 0.017453292F)), itemStack)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 638 */     if (this.ownerPlayer != null && this.goalPos != null) {
/* 639 */       this.cat.setInSittingPose(false);
/* 640 */       this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/* 641 */       if (this.cat.distanceToSqr(this.ownerPlayer) < 2.5D) {
/* 642 */         this.onBedTicks++;
/* 643 */         if (this.onBedTicks > adjustedTickDelay(16)) {
/* 644 */           this.cat.setLying(true);
/* 645 */           this.cat.setRelaxStateOne(false);
/*     */         } else {
/* 647 */           this.cat.lookAt(this.ownerPlayer, 45.0F, 45.0F);
/* 648 */           this.cat.setRelaxStateOne(true);
/*     */         } 
/*     */       } else {
/* 651 */         this.cat.setLying(false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\feline\Cat$CatRelaxOnOwnerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */