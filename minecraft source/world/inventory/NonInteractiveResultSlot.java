/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class NonInteractiveResultSlot
/*    */   extends Slot
/*    */ {
/* 12 */   public NonInteractiveResultSlot(Container container, int id, int x, int y) { super(container, id, x, y); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onQuickCraft(ItemStack picked, ItemStack original) {}
/*    */ 
/*    */ 
/*    */   
/* 21 */   public boolean mayPickup(Player player) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public Optional<ItemStack> tryRemove(int amount, int maxAmount, Player player) { return Optional.empty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public ItemStack safeTake(int amount, int maxAmount, Player player) { return ItemStack.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public ItemStack safeInsert(ItemStack stack) { return stack; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public ItemStack safeInsert(ItemStack inputStack, int inputAmount) { return safeInsert(inputStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean allowModification(Player player) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public ItemStack remove(int amount) { return ItemStack.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {}
/*    */ 
/*    */ 
/*    */   
/* 65 */   public boolean isHighlightable() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public boolean isFake() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\NonInteractiveResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */