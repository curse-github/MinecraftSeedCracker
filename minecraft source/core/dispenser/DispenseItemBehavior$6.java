/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.CandleBlock;
/*     */ import net.minecraft.world.level.block.CandleCakeBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.TntBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends OptionalDispenseItemBehavior
/*     */ {
/*     */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 234 */     ServerLevel level = source.level();
/*     */     
/* 236 */     setSuccess(true);
/*     */     
/* 238 */     Direction facing = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 239 */     BlockPos targetPos = source.pos().relative(facing);
/* 240 */     BlockState target = level.getBlockState(targetPos);
/* 241 */     if (BaseFireBlock.canBePlacedAt(level, targetPos, facing)) {
/* 242 */       level.setBlockAndUpdate(targetPos, BaseFireBlock.getState(level, targetPos));
/* 243 */       level.gameEvent(null, GameEvent.BLOCK_PLACE, targetPos);
/* 244 */     } else if (CampfireBlock.canLight(target) || CandleBlock.canLight(target) || CandleCakeBlock.canLight(target)) {
/* 245 */       level.setBlockAndUpdate(targetPos, (BlockState)target.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 246 */       level.gameEvent(null, GameEvent.BLOCK_CHANGE, targetPos);
/* 247 */     } else if (target.getBlock() instanceof TntBlock) {
/* 248 */       if (TntBlock.prime(level, targetPos)) {
/* 249 */         level.removeBlock(targetPos, false);
/*     */       } else {
/* 251 */         setSuccess(false);
/*     */       } 
/*     */     } else {
/* 254 */       setSuccess(false);
/*     */     } 
/*     */     
/* 257 */     if (isSuccess())
/* 258 */       dispensed.hurtAndBreak(1, level, null, item -> {
/*     */           
/*     */           }); 
/* 261 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$6.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */