/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Slot
/*    */ {
/* 64 */   null(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*    */ 
/*    */   
/* 67 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 72 */     carried.onCraftedBy(player, carried.getCount());
/* 73 */     StonecutterMenu.this.resultContainer.awardUsedRecipes(player, getRelevantItems());
/*    */ 
/*    */     
/* 76 */     ItemStack remaining = StonecutterMenu.this.inputSlot.remove(1);
/* 77 */     if (!remaining.isEmpty()) {
/* 78 */       StonecutterMenu.this.setupResultSlot(StonecutterMenu.this.selectedRecipeIndex.get());
/*    */     }
/*    */     
/* 81 */     access.execute((level, pos) -> {
/*    */           
/* 83 */           long gameTime = level.getGameTime();
/* 84 */           if (StonecutterMenu.this.lastSoundTime != gameTime) {
/* 85 */             level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 86 */             StonecutterMenu.this.lastSoundTime = gameTime;
/*    */           } 
/*    */         });
/*    */     
/* 90 */     super.onTake(player, carried);
/*    */   }
/*    */   
/*    */   private List<ItemStack> getRelevantItems() {
/* 94 */     return List.of(StonecutterMenu.this.inputSlot
/* 95 */         .getItem());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\StonecutterMenu$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */