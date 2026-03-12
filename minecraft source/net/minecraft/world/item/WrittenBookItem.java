/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class WrittenBookItem
/*    */   extends Item {
/* 11 */   public WrittenBookItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 16 */     ItemStack itemStack = player.getItemInHand(hand);
/* 17 */     player.openItemGui(itemStack, hand);
/* 18 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 19 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\WrittenBookItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */