/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ExperienceBottleItem
/*    */   extends Item implements ProjectileItem {
/* 18 */   public ExperienceBottleItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 23 */     ItemStack itemStack = player.getItemInHand(hand);
/* 24 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/* 25 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 26 */       Projectile.spawnProjectileFromRotation(ThrownExperienceBottle::new, serverLevel, itemStack, player, -20.0F, 0.7F, 1.0F); }
/*    */     
/* 28 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 29 */     itemStack.consume(1, player);
/* 30 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) { return new ThrownExperienceBottle(level, position.x(), position.y(), position.z(), itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public ProjectileItem.DispenseConfig createDispenseConfig() { return ProjectileItem.DispenseConfig.builder()
/* 41 */       .uncertainty(ProjectileItem.DispenseConfig.DEFAULT.uncertainty() * 0.5F)
/* 42 */       .power(ProjectileItem.DispenseConfig.DEFAULT.power() * 1.25F)
/* 43 */       .build(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ExperienceBottleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */