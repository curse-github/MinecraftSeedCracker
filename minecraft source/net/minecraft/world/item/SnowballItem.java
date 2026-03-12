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
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SnowballItem
/*    */   extends Item implements ProjectileItem {
/* 18 */   public static float PROJECTILE_SHOOT_POWER = 1.5F;
/*    */ 
/*    */   
/* 21 */   public SnowballItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 26 */     ItemStack itemStack = player.getItemInHand(hand);
/* 27 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/* 28 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 29 */       Projectile.spawnProjectileFromRotation(Snowball::new, serverLevel, itemStack, player, 0.0F, PROJECTILE_SHOOT_POWER, 1.0F); }
/*    */     
/* 31 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 32 */     itemStack.consume(1, player);
/* 33 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) { return new Snowball(level, position.x(), position.y(), position.z(), itemStack); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SnowballItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */