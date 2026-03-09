/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
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
/*     */ class null
/*     */   extends OptionalDispenseItemBehavior
/*     */ {
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 137 */     BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/* 138 */     List<AbstractChestedHorse> entities = source.level().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(pos), entity -> (entity.isAlive() && !entity.hasChest()));
/*     */     
/* 140 */     for (AbstractChestedHorse abstractChestedHorse : entities) {
/* 141 */       if (abstractChestedHorse.isTamed()) {
/* 142 */         SlotAccess slot = abstractChestedHorse.getSlot(499);
/* 143 */         if (slot != null && slot.set(dispensed)) {
/* 144 */           dispensed.shrink(1);
/* 145 */           setSuccess(true);
/* 146 */           return dispensed;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     return super.execute(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */