/*    */ package net.minecraft.core.dispenser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.BaseRailBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartDispenseItemBehavior extends DefaultDispenseItemBehavior {
/*    */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior;
/*    */   
/*    */   public MinecartDispenseItemBehavior(EntityType<? extends AbstractMinecart> entityType) {
/* 19 */     this.defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.entityType = entityType;
/*    */   }
/*    */   private final EntityType<? extends AbstractMinecart> entityType;
/*    */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*    */     double yOffset;
/* 28 */     Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 29 */     ServerLevel level = source.level();
/* 30 */     Vec3 center = source.center();
/*    */ 
/*    */ 
/*    */     
/* 34 */     double spawnX = center.x() + direction.getStepX() * 1.125D;
/* 35 */     double spawnY = Math.floor(center.y()) + direction.getStepY();
/* 36 */     double spawnZ = center.z() + direction.getStepZ() * 1.125D;
/*    */     
/* 38 */     BlockPos front = source.pos().relative(direction);
/* 39 */     BlockState blockFront = level.getBlockState(front);
/*    */ 
/*    */     
/* 42 */     if (blockFront.is(BlockTags.RAILS)) {
/* 43 */       if (getRailShape(blockFront).isSlope()) {
/* 44 */         yOffset = 0.6D;
/*    */       } else {
/* 46 */         yOffset = 0.1D;
/*    */       } 
/* 48 */     } else if (blockFront.isAir()) {
/* 49 */       BlockState blockBelow = level.getBlockState(front.below());
/* 50 */       if (blockBelow.is(BlockTags.RAILS)) {
/* 51 */         if (direction == Direction.DOWN || !getRailShape(blockBelow).isSlope()) {
/* 52 */           yOffset = -0.9D;
/*    */         } else {
/* 54 */           yOffset = -0.4D;
/*    */         } 
/*    */       } else {
/* 57 */         return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*    */       } 
/*    */     } else {
/* 60 */       return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*    */     } 
/*    */     
/* 63 */     Vec3 spawnPos = new Vec3(spawnX, spawnY + yOffset, spawnZ);
/* 64 */     AbstractMinecart minecart = AbstractMinecart.createMinecart(level, spawnPos.x, spawnPos.y, spawnPos.z, this.entityType, EntitySpawnReason.DISPENSER, dispensed, null);
/* 65 */     if (minecart != null) {
/* 66 */       level.addFreshEntity(minecart);
/* 67 */       dispensed.shrink(1);
/*    */     } 
/* 69 */     return dispensed;
/*    */   }
/*    */   
/*    */   private static RailShape getRailShape(BlockState blockFront) {
/* 73 */     Block block = blockFront.getBlock(); BaseRailBlock railBlock = (BaseRailBlock)block; return (block instanceof BaseRailBlock) ? (RailShape)blockFront.getValue(railBlock.getShapeProperty()) : RailShape.NORTH_SOUTH;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 78 */   protected void playSound(BlockSource source) { source.level().levelEvent(1000, source.pos(), 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\MinecartDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */