/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class NameTagItem
/*    */   extends Item {
/* 13 */   public NameTagItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
/* 18 */     Component customName = (Component)itemStack.get(DataComponents.CUSTOM_NAME);
/* 19 */     if (customName != null && target.getType().canSerialize()) {
/* 20 */       if (!player.level().isClientSide() && target.isAlive()) {
/* 21 */         target.setCustomName(customName);
/* 22 */         if (target instanceof Mob) { Mob mob = (Mob)target;
/* 23 */           mob.setPersistenceRequired(); }
/*    */ 
/*    */         
/* 26 */         itemStack.shrink(1);
/*    */       } 
/*    */       
/* 29 */       return InteractionResult.SUCCESS;
/*    */     } 
/* 31 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\NameTagItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */