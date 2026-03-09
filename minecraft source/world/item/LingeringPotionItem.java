/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class LingeringPotionItem
/*    */   extends ThrowablePotionItem {
/* 17 */   public LingeringPotionItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 22 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/* 23 */     return super.use(level, player, hand);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected AbstractThrownPotion createPotion(ServerLevel level, LivingEntity owner, ItemStack itemStack) { return new ThrownLingeringPotion(level, owner, itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected AbstractThrownPotion createPotion(Level level, Position position, ItemStack itemStack) { return new ThrownLingeringPotion(level, position.x(), position.y(), position.z(), itemStack); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\LingeringPotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */