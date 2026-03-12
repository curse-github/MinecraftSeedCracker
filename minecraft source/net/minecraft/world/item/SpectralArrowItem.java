/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SpectralArrowItem
/*    */   extends ArrowItem
/*    */ {
/* 14 */   public SpectralArrowItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity owner, ItemStack firedFromWeapon) { return new SpectralArrow(level, owner, itemStack.copyWithCount(1), firedFromWeapon); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 24 */     SpectralArrow arrow = new SpectralArrow(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
/* 25 */     arrow.pickup = AbstractArrow.Pickup.ALLOWED;
/* 26 */     return arrow;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SpectralArrowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */