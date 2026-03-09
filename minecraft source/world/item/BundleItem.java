/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public class BundleItem
/*     */   extends Item
/*     */ {
/*     */   public static final int MAX_SHOWN_GRID_ITEMS_X = 4;
/*     */   public static final int MAX_SHOWN_GRID_ITEMS_Y = 3;
/*     */   public static final int MAX_SHOWN_GRID_ITEMS = 12;
/*     */   public static final int OVERFLOWING_MAX_SHOWN_GRID_ITEMS = 11;
/*  36 */   private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
/*  37 */   private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);
/*     */   
/*     */   private static final int TICKS_AFTER_FIRST_THROW = 10;
/*     */   private static final int TICKS_BETWEEN_THROWS = 2;
/*     */   private static final int TICKS_MAX_THROW_DURATION = 200;
/*     */   
/*  43 */   public BundleItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */   
/*     */   public static float getFullnessDisplay(ItemStack itemStack) {
/*  47 */     BundleContents contents = (BundleContents)itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/*  48 */     return contents.weight().floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction clickAction, Player player) {
/*  53 */     BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
/*  54 */     if (initialContents == null) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     ItemStack other = slot.getItem();
/*  59 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/*     */ 
/*     */     
/*  62 */     if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
/*  63 */       if (contents.tryTransfer(slot, player) > 0) {
/*  64 */         playInsertSound(player);
/*     */       } else {
/*  66 */         playInsertFailSound(player);
/*     */       } 
/*  68 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/*  69 */       broadcastChangesOnContainerMenu(player);
/*  70 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
/*  75 */       ItemStack itemStack = contents.removeOne();
/*  76 */       if (itemStack != null) {
/*  77 */         ItemStack remainder = slot.safeInsert(itemStack);
/*  78 */         if (remainder.getCount() > 0) {
/*  79 */           contents.tryInsert(remainder);
/*     */         } else {
/*  81 */           playRemoveOneSound(player);
/*     */         } 
/*     */       } 
/*  84 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/*  85 */       broadcastChangesOnContainerMenu(player);
/*  86 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) {
/*  97 */     if (clickAction == ClickAction.PRIMARY && other.isEmpty()) {
/*  98 */       toggleSelectedItem(self, -1);
/*  99 */       return false;
/*     */     } 
/* 101 */     BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
/* 102 */     if (initialContents == null) {
/* 103 */       return false;
/*     */     }
/* 105 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/*     */ 
/*     */     
/* 108 */     if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
/* 109 */       if (slot.allowModification(player) && contents.tryInsert(other) > 0) {
/* 110 */         playInsertSound(player);
/*     */       } else {
/* 112 */         playInsertFailSound(player);
/*     */       } 
/* 114 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/* 115 */       broadcastChangesOnContainerMenu(player);
/* 116 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
/* 121 */       if (slot.allowModification(player)) {
/* 122 */         ItemStack removed = contents.removeOne();
/* 123 */         if (removed != null) {
/* 124 */           playRemoveOneSound(player);
/* 125 */           carriedItem.set(removed);
/*     */         } 
/*     */       } 
/* 128 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/* 129 */       broadcastChangesOnContainerMenu(player);
/* 130 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     toggleSelectedItem(self, -1);
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 140 */     player.startUsingItem(hand);
/* 141 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private void dropContent(Level level, Player player, ItemStack itemStack) {
/* 145 */     if (dropContent(itemStack, player)) {
/* 146 */       playDropContentsSound(level, player);
/* 147 */       player.awardStat(Stats.ITEM_USED.get(this));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBarVisible(ItemStack stack) {
/* 153 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 154 */     return (contents.weight().compareTo(Fraction.ZERO) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBarWidth(ItemStack stack) {
/* 159 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/*     */     
/* 161 */     return Math.min(1 + Mth.mulAndTruncate(contents.weight(), 12), 13);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBarColor(ItemStack stack) {
/* 166 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 167 */     return (contents.weight().compareTo(Fraction.ONE) >= 0) ? FULL_BAR_COLOR : BAR_COLOR;
/*     */   }
/*     */   
/*     */   public static void toggleSelectedItem(ItemStack stack, int selectedItem) {
/* 171 */     BundleContents initialContents = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
/* 172 */     if (initialContents == null) {
/*     */       return;
/*     */     }
/*     */     
/* 176 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/* 177 */     contents.toggleSelectedItem(selectedItem);
/* 178 */     stack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/*     */   }
/*     */   
/*     */   public static boolean hasSelectedItem(ItemStack stack) {
/* 182 */     BundleContents contents = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
/* 183 */     return (contents != null && contents.getSelectedItem() != -1);
/*     */   }
/*     */   
/*     */   public static int getSelectedItem(ItemStack stack) {
/* 187 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 188 */     return contents.getSelectedItem();
/*     */   }
/*     */   
/*     */   public static ItemStack getSelectedItemStack(ItemStack stack) {
/* 192 */     BundleContents contents = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
/* 193 */     if (contents != null && contents.getSelectedItem() != -1) {
/* 194 */       return contents.getItemUnsafe(contents.getSelectedItem());
/*     */     }
/* 196 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumberOfItemsToShow(ItemStack stack) {
/* 201 */     BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 202 */     return contents.getNumberOfItemsToShow();
/*     */   }
/*     */   
/*     */   private boolean dropContent(ItemStack bundle, Player player) {
/* 206 */     BundleContents contents = (BundleContents)bundle.get(DataComponents.BUNDLE_CONTENTS);
/* 207 */     if (contents == null || contents.isEmpty()) {
/* 208 */       return false;
/*     */     }
/*     */     
/* 211 */     Optional<ItemStack> itemStack = removeOneItemFromBundle(bundle, player, contents);
/* 212 */     if (itemStack.isPresent()) {
/* 213 */       player.drop((ItemStack)itemStack.get(), true);
/* 214 */       return true;
/*     */     } 
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<ItemStack> removeOneItemFromBundle(ItemStack self, Player player, BundleContents initialContents) {
/* 220 */     BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
/* 221 */     ItemStack removed = contents.removeOne();
/* 222 */     if (removed != null) {
/* 223 */       playRemoveOneSound(player);
/* 224 */       self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
/* 225 */       return Optional.of(removed);
/*     */     } 
/* 227 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
/* 232 */     if (livingEntity instanceof Player) { Player player = (Player)livingEntity;
/* 233 */       int useDuration = getUseDuration(itemStack, livingEntity);
/* 234 */       boolean isFirstTick = (ticksRemaining == useDuration);
/* 235 */       if (isFirstTick || (ticksRemaining < useDuration - 10 && ticksRemaining % 2 == 0)) {
/* 236 */         dropContent(level, player, itemStack);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 243 */   public int getUseDuration(ItemStack itemStack, LivingEntity entity) { return 200; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) { return ItemUseAnimation.BUNDLE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
/* 253 */     TooltipDisplay display = (TooltipDisplay)bundle.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/* 254 */     if (!display.shows(DataComponents.BUNDLE_CONTENTS)) {
/* 255 */       return Optional.empty();
/*     */     }
/* 257 */     return Optional.ofNullable((BundleContents)bundle.get(DataComponents.BUNDLE_CONTENTS)).map(net.minecraft.world.inventory.tooltip.BundleTooltip::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDestroyed(ItemEntity entity) {
/* 262 */     BundleContents contents = (BundleContents)entity.getItem().get(DataComponents.BUNDLE_CONTENTS);
/* 263 */     if (contents == null) {
/*     */       return;
/*     */     }
/* 266 */     entity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 267 */     ItemUtils.onContainerDestroyed(entity, contents.itemsCopy());
/*     */   }
/*     */ 
/*     */   
/* 271 */   public static List<BundleItem> getAllBundleItemColors() { return Stream.of(new Item[] { Items.BUNDLE, Items.WHITE_BUNDLE, Items.ORANGE_BUNDLE, Items.MAGENTA_BUNDLE, Items.LIGHT_BLUE_BUNDLE, Items.YELLOW_BUNDLE, Items.LIME_BUNDLE, Items.PINK_BUNDLE, Items.GRAY_BUNDLE, Items.LIGHT_GRAY_BUNDLE, Items.CYAN_BUNDLE, Items.BLACK_BUNDLE, Items.BROWN_BUNDLE, Items.GREEN_BUNDLE, Items.RED_BUNDLE, Items.BLUE_BUNDLE, Items.PURPLE_BUNDLE
/*     */ 
/*     */         
/* 274 */         }).map(item -> (BundleItem)item)
/* 275 */       .toList(); }
/*     */ 
/*     */   
/*     */   public static Item getByColor(DyeColor color) {
/* 279 */     switch (color) { default: throw new MatchException(null, null);case WHITE: case ORANGE: case MAGENTA: case LIGHT_BLUE: case YELLOW: case LIME: case PINK: case GRAY: case LIGHT_GRAY: case CYAN: case BLUE: case BROWN: case GREEN: case RED: case BLACK: case PURPLE: break; }  return 
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
/* 295 */       Items.PURPLE_BUNDLE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 300 */   private static void playRemoveOneSound(Entity entity) { entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F); }
/*     */ 
/*     */ 
/*     */   
/* 304 */   private static void playInsertSound(Entity entity) { entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F); }
/*     */ 
/*     */ 
/*     */   
/* 308 */   private static void playInsertFailSound(Entity entity) { entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/* 312 */   private static void playDropContentsSound(Level level, Entity entity) { level.playSound(null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F); }
/*     */ 
/*     */   
/*     */   private void broadcastChangesOnContainerMenu(Player player) {
/* 316 */     AbstractContainerMenu containerMenu = player.containerMenu;
/* 317 */     if (containerMenu != null)
/* 318 */       containerMenu.slotsChanged(player.getInventory()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BundleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */