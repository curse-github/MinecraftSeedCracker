/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 122 */     Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 123 */     BlockPos pos = source.pos().relative(direction);
/* 124 */     ServerLevel serverLevel = source.level();
/* 125 */     Consumer<ArmorStand> postSpawnConfig = EntityType.appendDefaultStackConfig(armorStand -> armorStand.setYRot(direction.toYRot()), serverLevel, dispensed, null);
/* 126 */     ArmorStand armorStand = (ArmorStand)EntityType.ARMOR_STAND.spawn(serverLevel, postSpawnConfig, pos, EntitySpawnReason.DISPENSER, false, false);
/* 127 */     if (armorStand != null) {
/* 128 */       dispensed.shrink(1);
/*     */     }
/* 130 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */