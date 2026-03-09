/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PotionSlot
/*     */   extends Slot
/*     */ {
/* 134 */   public PotionSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public boolean mayPlace(ItemStack itemStack) { return mayPlaceItem(itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/* 149 */     Optional<Holder<Potion>> potion = ((PotionContents)carried.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/* 150 */     if (potion.isPresent() && player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 151 */       CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, (Holder)potion.get()); }
/*     */     
/* 153 */     super.onTake(player, carried);
/*     */   }
/*     */ 
/*     */   
/* 157 */   public static boolean mayPlaceItem(ItemStack itemStack) { return (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public Identifier getNoItemIcon() { return BrewingStandMenu.EMPTY_SLOT_POTION; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\BrewingStandMenu$PotionSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */