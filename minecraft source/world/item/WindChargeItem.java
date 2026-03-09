/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WindChargeItem extends Item implements ProjectileItem {
/* 22 */   public static float PROJECTILE_SHOOT_POWER = 1.5F;
/*    */ 
/*    */   
/* 25 */   public WindChargeItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 30 */     ItemStack stack = player.getItemInHand(hand);
/*    */     
/* 32 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 33 */       Projectile.spawnProjectileFromRotation((source, l, itemStack) -> 
/* 34 */           new WindCharge(player, level, player
/*    */             
/* 36 */             .position().x(), player.getEyePosition().y(), player.position().z()), serverLevel, stack, player, 0.0F, PROJECTILE_SHOOT_POWER, 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */     
/* 43 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 44 */     stack.consume(1, player);
/*    */     
/* 46 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 51 */     RandomSource random = level.getRandom();
/* 52 */     double dirX = random.triangle(direction.getStepX(), 0.11485000000000001D);
/* 53 */     double dirY = random.triangle(direction.getStepY(), 0.11485000000000001D);
/* 54 */     double dirZ = random.triangle(direction.getStepZ(), 0.11485000000000001D);
/* 55 */     Vec3 dir = new Vec3(dirX, dirY, dirZ);
/* 56 */     WindCharge windCharge = new WindCharge(level, position.x(), position.y(), position.z(), dir);
/* 57 */     windCharge.setDeltaMovement(dir);
/* 58 */     return windCharge;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void shoot(Projectile projectile, double xd, double yd, double zd, float pow, float uncertainty) {}
/*    */ 
/*    */ 
/*    */   
/* 68 */   public ProjectileItem.DispenseConfig createDispenseConfig() { return ProjectileItem.DispenseConfig.builder()
/* 69 */       .positionFunction((source, direction) -> DispenserBlock.getDispensePosition(source, 1.0D, Vec3.ZERO))
/* 70 */       .uncertainty(6.6666665F)
/* 71 */       .power(1.0F)
/* 72 */       .overrideDispenseEvent(1051)
/* 73 */       .build(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\WindChargeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */