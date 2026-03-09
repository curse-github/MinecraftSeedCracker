/*    */ package net.minecraft.core.dispenser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BoatDispenseItemBehavior extends DefaultDispenseItemBehavior {
/*    */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior;
/*    */   
/*    */   public BoatDispenseItemBehavior(EntityType<? extends AbstractBoat> type) {
/* 16 */     this.defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*    */ 
/*    */ 
/*    */     
/* 20 */     this.type = type;
/*    */   }
/*    */   private final EntityType<? extends AbstractBoat> type;
/*    */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*    */     double yOffset;
/* 25 */     Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 26 */     ServerLevel level = source.level();
/* 27 */     Vec3 center = source.center();
/*    */     
/* 29 */     double justOutsideDispenser = 0.5625D + this.type.getWidth() / 2.0D;
/* 30 */     double spawnX = center.x() + direction.getStepX() * justOutsideDispenser;
/* 31 */     double spawnY = center.y() + (direction.getStepY() * 1.125F);
/* 32 */     double spawnZ = center.z() + direction.getStepZ() * justOutsideDispenser;
/*    */     
/* 34 */     BlockPos frontPos = source.pos().relative(direction);
/*    */ 
/*    */     
/* 37 */     if (level.getFluidState(frontPos).is(FluidTags.WATER)) {
/* 38 */       yOffset = 1.0D;
/* 39 */     } else if (level.getBlockState(frontPos).isAir() && level.getFluidState(frontPos.below()).is(FluidTags.WATER)) {
/* 40 */       yOffset = 0.0D;
/*    */     } else {
/* 42 */       return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*    */     } 
/*    */     
/* 45 */     AbstractBoat boat = (AbstractBoat)this.type.create(level, EntitySpawnReason.DISPENSER);
/*    */     
/* 47 */     if (boat != null) {
/* 48 */       boat.setInitialPos(spawnX, spawnY + yOffset, spawnZ);
/* 49 */       EntityType.createDefaultStackConfig(level, dispensed, null).accept(boat);
/* 50 */       boat.setYRot(direction.toYRot());
/* 51 */       level.addFreshEntity(boat);
/*    */       
/* 53 */       dispensed.shrink(1);
/*    */     } 
/* 55 */     return dispensed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected void playSound(BlockSource source) { source.level().levelEvent(1000, source.pos(), 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\BoatDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */