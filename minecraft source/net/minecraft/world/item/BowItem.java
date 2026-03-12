/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BowItem
/*    */   extends ProjectileWeaponItem
/*    */ {
/*    */   public static final int MAX_DRAW_DURATION = 20;
/*    */   public static final int DEFAULT_RANGE = 15;
/*    */   
/* 24 */   public BowItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
/* 29 */     if (!(entity instanceof Player)) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     Player player = (Player)entity;
/* 34 */     ItemStack projectile = player.getProjectile(itemStack);
/*    */     
/* 36 */     if (projectile.isEmpty()) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     int timeHeld = getUseDuration(itemStack, entity) - remainingTime;
/* 41 */     float pow = getPowerForTime(timeHeld);
/* 42 */     if (pow < 0.1D) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     List<ItemStack> firedProjectiles = draw(itemStack, projectile, player);
/* 47 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!firedProjectiles.isEmpty()) {
/* 48 */         shoot(serverLevel, player, player.getUsedItemHand(), itemStack, firedProjectiles, pow * 3.0F, 1.0F, (pow == 1.0F), null);
/*    */       } }
/*    */     
/* 51 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + pow * 0.5F);
/* 52 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   protected void shootProjectile(LivingEntity shooter, Projectile projectileEntity, int index, float power, float uncertainty, float angle, LivingEntity targetOverrride) { projectileEntity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, power, uncertainty); }
/*    */ 
/*    */   
/*    */   public static float getPowerForTime(int timeHeld) {
/* 62 */     float pow = timeHeld / 20.0F;
/* 63 */     pow = (pow * pow + pow * 2.0F) / 3.0F;
/* 64 */     if (pow > 1.0F) {
/* 65 */       pow = 1.0F;
/*    */     }
/* 67 */     return pow;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public int getUseDuration(ItemStack itemStack, LivingEntity user) { return 72000; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) { return ItemUseAnimation.BOW; }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 82 */     ItemStack itemStack = player.getItemInHand(hand);
/* 83 */     boolean foundProjectile = !player.getProjectile(itemStack).isEmpty();
/* 84 */     if (player.hasInfiniteMaterials() || foundProjectile) {
/* 85 */       player.startUsingItem(hand);
/* 86 */       return InteractionResult.CONSUME;
/*    */     } 
/* 88 */     return InteractionResult.FAIL;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 93 */   public Predicate<ItemStack> getAllSupportedProjectiles() { return ARROW_ONLY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 98 */   public int getDefaultProjectileRange() { return 15; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */