/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SpawnEggItem;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
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
/*     */ class null
/*     */   extends DefaultDispenseItemBehavior
/*     */ {
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*  97 */     Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/*     */     
/*  99 */     EntityType<?> type = ((SpawnEggItem)dispensed.getItem()).getType(dispensed);
/* 100 */     if (type == null) {
/* 101 */       return dispensed;
/*     */     }
/*     */     try {
/* 104 */       type.spawn(source.level(), dispensed, null, source.pos().relative(direction), EntitySpawnReason.DISPENSER, (direction != Direction.UP), false);
/* 105 */     } catch (Exception e) {
/* 106 */       LOGGER.error("Error while dispensing spawn egg from dispenser at {}", source.pos(), e);
/* 107 */       return ItemStack.EMPTY;
/*     */     } 
/* 109 */     dispensed.shrink(1);
/* 110 */     source.level().gameEvent(null, GameEvent.ENTITY_PLACE, source.pos());
/* 111 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */