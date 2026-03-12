/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.CandleBlock;
/*    */ import net.minecraft.world.level.block.CandleCakeBlock;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FireChargeItem
/*    */   extends Item
/*    */   implements ProjectileItem {
/* 28 */   public FireChargeItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 33 */     Level level = context.getLevel();
/* 34 */     BlockPos pos = context.getClickedPos();
/* 35 */     BlockState blockState = level.getBlockState(pos);
/* 36 */     boolean used = false;
/*    */     
/* 38 */     if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
/* 39 */       playSound(level, pos);
/* 40 */       level.setBlockAndUpdate(pos, (BlockState)blockState.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 41 */       level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
/* 42 */       used = true;
/*    */     } else {
/* 44 */       pos = pos.relative(context.getClickedFace());
/* 45 */       if (BaseFireBlock.canBePlacedAt(level, pos, context.getHorizontalDirection())) {
/* 46 */         playSound(level, pos);
/* 47 */         level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
/* 48 */         level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, pos);
/* 49 */         used = true;
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     if (used) {
/* 54 */       context.getItemInHand().shrink(1);
/* 55 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 58 */     return InteractionResult.FAIL;
/*    */   }
/*    */   
/*    */   private void playSound(Level level, BlockPos pos) {
/* 62 */     RandomSource random = level.getRandom();
/* 63 */     level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 68 */     RandomSource random = level.getRandom();
/* 69 */     double dirX = random.triangle(direction.getStepX(), 0.11485000000000001D);
/* 70 */     double dirY = random.triangle(direction.getStepY(), 0.11485000000000001D);
/* 71 */     double dirZ = random.triangle(direction.getStepZ(), 0.11485000000000001D);
/* 72 */     Vec3 dir = new Vec3(dirX, dirY, dirZ);
/* 73 */     SmallFireball fireball = new SmallFireball(level, position.x(), position.y(), position.z(), dir.normalize());
/* 74 */     fireball.setItem(itemStack);
/* 75 */     return fireball;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void shoot(Projectile projectile, double xd, double yd, double zd, float pow, float uncertainty) {}
/*    */ 
/*    */ 
/*    */   
/* 85 */   public ProjectileItem.DispenseConfig createDispenseConfig() { return ProjectileItem.DispenseConfig.builder()
/* 86 */       .positionFunction((source, direction) -> DispenserBlock.getDispensePosition(source, 1.0D, Vec3.ZERO))
/* 87 */       .uncertainty(6.6666665F)
/* 88 */       .power(1.0F)
/* 89 */       .overrideDispenseEvent(1018)
/* 90 */       .build(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\FireChargeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */