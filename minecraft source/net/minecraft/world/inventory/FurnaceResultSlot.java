/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ 
/*    */ public class FurnaceResultSlot extends Slot {
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   
/*    */   public FurnaceResultSlot(Player player, Container container, int slot, int x, int y) {
/* 14 */     super(container, slot, x, y);
/* 15 */     this.player = player;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack remove(int amount) {
/* 25 */     if (hasItem()) {
/* 26 */       this.removeCount += Math.min(amount, getItem().getCount());
/*    */     }
/* 28 */     return super.remove(amount);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 33 */     checkTakeAchievements(carried);
/* 34 */     super.onTake(player, carried);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack picked, int count) {
/* 39 */     this.removeCount += count;
/* 40 */     checkTakeAchievements(picked);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack carried) {
/* 45 */     carried.onCraftedBy(this.player, this.removeCount);
/* 46 */     Player player1 = this.player; if (player1 instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player1;
/* 47 */       Container container = this.container; if (container instanceof AbstractFurnaceBlockEntity) { AbstractFurnaceBlockEntity abstractFurnaceBlockEntity = (AbstractFurnaceBlockEntity)container;
/* 48 */         abstractFurnaceBlockEntity.awardUsedRecipesAndPopExperience(serverPlayer); }
/*    */        }
/*    */     
/* 51 */     this.removeCount = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\FurnaceResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */