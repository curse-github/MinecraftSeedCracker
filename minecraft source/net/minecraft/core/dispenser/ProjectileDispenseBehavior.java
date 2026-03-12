/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ProjectileItem;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ 
/*    */ public class ProjectileDispenseBehavior
/*    */   extends DefaultDispenseItemBehavior
/*    */ {
/*    */   private final ProjectileItem projectileItem;
/*    */   private final ProjectileItem.DispenseConfig dispenseConfig;
/*    */   
/*    */   public ProjectileDispenseBehavior(Item item) {
/* 19 */     if (item instanceof ProjectileItem) { projectileItem = (ProjectileItem)item; }
/* 20 */     else { throw new IllegalArgumentException(String.valueOf(item) + " not instance of " + String.valueOf(item)); }
/*    */     
/* 22 */     this.projectileItem = projectileItem;
/* 23 */     this.dispenseConfig = projectileItem.createDispenseConfig();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 28 */     ServerLevel level = source.level();
/* 29 */     Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 30 */     Position position = this.dispenseConfig.positionFunction().getDispensePosition(source, direction);
/*    */     
/* 32 */     Projectile.spawnProjectileUsingShoot(this.projectileItem
/* 33 */         .asProjectile(level, position, dispensed, direction), level, dispensed, direction
/*    */         
/* 35 */         .getStepX(), direction.getStepY(), direction.getStepZ(), this.dispenseConfig
/* 36 */         .power(), this.dispenseConfig.uncertainty());
/*    */     
/* 38 */     dispensed.shrink(1);
/*    */     
/* 40 */     return dispensed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   protected void playSound(BlockSource source) { source.level().levelEvent(this.dispenseConfig.overrideDispenseEvent().orElse(1002), source.pos(), 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\ProjectileDispenseBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */