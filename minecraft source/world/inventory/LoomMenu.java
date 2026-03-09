/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BannerPatternTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ 
/*     */ 
/*     */ public class LoomMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private static final int PATTERN_NOT_SET = -1;
/*     */   private static final int INV_SLOT_START = 4;
/*     */   private static final int INV_SLOT_END = 31;
/*     */   private static final int USE_ROW_SLOT_START = 31;
/*     */   private static final int USE_ROW_SLOT_END = 40;
/*     */   private final ContainerLevelAccess access;
/*  36 */   private final DataSlot selectedBannerPatternIndex = DataSlot.standalone();
/*  37 */   private List<Holder<BannerPattern>> selectablePatterns = List.of();
/*     */   private Runnable slotUpdateListener = () -> {
/*     */     
/*     */     };
/*     */   private final HolderGetter<BannerPattern> patternGetter;
/*     */   private final Slot bannerSlot;
/*     */   private final Slot dyeSlot;
/*     */   private final Slot patternSlot;
/*     */   private final Slot resultSlot;
/*     */   private long lastSoundTime;
/*     */   
/*  48 */   private final Container inputContainer = new SimpleContainer(3)
/*     */     {
/*     */       public void setChanged() {
/*  51 */         super.setChanged();
/*  52 */         LoomMenu.this.slotsChanged(this);
/*  53 */         LoomMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*     */   
/*  57 */   private final Container outputContainer = new SimpleContainer(1)
/*     */     {
/*     */       public void setChanged() {
/*  60 */         super.setChanged();
/*  61 */         LoomMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  66 */   public LoomMenu(int containerId, Inventory inventory) { this(containerId, inventory, ContainerLevelAccess.NULL); }
/*     */ 
/*     */   
/*     */   public LoomMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
/*  70 */     super(MenuType.LOOM, containerId);
/*  71 */     this.access = access;
/*     */     
/*  73 */     this.bannerSlot = addSlot(new Slot(this, this.inputContainer, 0, 13, 26)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  76 */             return itemStack.getItem() instanceof net.minecraft.world.item.BannerItem;
/*     */           }
/*     */         });
/*     */     
/*  80 */     this.dyeSlot = addSlot(new Slot(this, this.inputContainer, 1, 33, 26)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  83 */             return itemStack.getItem() instanceof DyeItem;
/*     */           }
/*     */         });
/*     */     
/*  87 */     this.patternSlot = addSlot(new Slot(this, this.inputContainer, 2, 23, 45)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  90 */             return itemStack.has(DataComponents.PROVIDES_BANNER_PATTERNS);
/*     */           }
/*     */         });
/*     */     
/*  94 */     this.resultSlot = addSlot(new Slot(this.outputContainer, 0, 143, 57)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  97 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/* 102 */             LoomMenu.this.bannerSlot.remove(1);
/* 103 */             LoomMenu.this.dyeSlot.remove(1);
/* 104 */             if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem()) {
/* 105 */               LoomMenu.this.selectedBannerPatternIndex.set(-1);
/*     */             }
/* 107 */             access.execute((level, pos) -> {
/*     */                   
/* 109 */                   long gameTime = level.getGameTime();
/* 110 */                   if (LoomMenu.this.lastSoundTime != gameTime) {
/* 111 */                     level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 112 */                     LoomMenu.this.lastSoundTime = gameTime;
/*     */                   } 
/*     */                 });
/*     */ 
/*     */             
/* 117 */             super.onTake(player, carried);
/*     */           }
/*     */         });
/*     */     
/* 121 */     addStandardInventorySlots(inventory, 8, 84);
/*     */     
/* 123 */     addDataSlot(this.selectedBannerPatternIndex);
/*     */     
/* 125 */     this.patternGetter = inventory.player.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public boolean stillValid(Player player) { return stillValid(this.access, player, Blocks.LOOM); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean clickMenuButton(Player player, int buttonId) {
/* 135 */     if (buttonId >= 0 && buttonId < this.selectablePatterns.size()) {
/* 136 */       this.selectedBannerPatternIndex.set(buttonId);
/* 137 */       setupResultSlot((Holder)this.selectablePatterns.get(buttonId));
/* 138 */       return true;
/*     */     } 
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   private List<Holder<BannerPattern>> getSelectablePatterns(ItemStack patternStack) {
/* 144 */     if (patternStack.isEmpty()) {
/* 145 */       return (List)this.patternGetter.get(BannerPatternTags.NO_ITEM_REQUIRED).map(ImmutableList::copyOf).orElse(ImmutableList.of());
/*     */     }
/* 147 */     TagKey<BannerPattern> providedPatterns = (TagKey)patternStack.get(DataComponents.PROVIDES_BANNER_PATTERNS);
/* 148 */     if (providedPatterns != null) {
/* 149 */       return (List)this.patternGetter.get(providedPatterns).map(ImmutableList::copyOf).orElse(ImmutableList.of());
/*     */     }
/* 151 */     return List.of();
/*     */   }
/*     */ 
/*     */   
/* 155 */   private boolean isValidPatternIndex(int selectedPattern) { return (selectedPattern >= 0 && selectedPattern < this.selectablePatterns.size()); }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/*     */     Holder<BannerPattern> patternToDisplay;
/* 160 */     ItemStack bannerStack = this.bannerSlot.getItem();
/* 161 */     ItemStack dyeStack = this.dyeSlot.getItem();
/* 162 */     ItemStack patternStack = this.patternSlot.getItem();
/*     */     
/* 164 */     if (bannerStack.isEmpty() || dyeStack.isEmpty()) {
/* 165 */       this.resultSlot.set(ItemStack.EMPTY);
/* 166 */       this.selectablePatterns = List.of();
/* 167 */       this.selectedBannerPatternIndex.set(-1);
/*     */       
/*     */       return;
/*     */     } 
/* 171 */     int selectedPattern = this.selectedBannerPatternIndex.get();
/* 172 */     boolean validPatternIndex = isValidPatternIndex(selectedPattern);
/* 173 */     List<Holder<BannerPattern>> previousSelectablePatterns = this.selectablePatterns;
/* 174 */     this.selectablePatterns = getSelectablePatterns(patternStack);
/*     */     
/* 176 */     if (this.selectablePatterns.size() == 1) {
/*     */       
/* 178 */       this.selectedBannerPatternIndex.set(0);
/* 179 */       patternToDisplay = (Holder)this.selectablePatterns.get(0);
/* 180 */     } else if (!validPatternIndex) {
/* 181 */       this.selectedBannerPatternIndex.set(-1);
/* 182 */       patternToDisplay = null;
/*     */     } else {
/* 184 */       Holder<BannerPattern> selectedValue = (Holder)previousSelectablePatterns.get(selectedPattern);
/* 185 */       int newSelectedIndex = this.selectablePatterns.indexOf(selectedValue);
/* 186 */       if (newSelectedIndex != -1) {
/* 187 */         patternToDisplay = selectedValue;
/* 188 */         this.selectedBannerPatternIndex.set(newSelectedIndex);
/*     */       } else {
/* 190 */         patternToDisplay = null;
/* 191 */         this.selectedBannerPatternIndex.set(-1);
/*     */       } 
/*     */     } 
/*     */     
/* 195 */     if (patternToDisplay != null) {
/* 196 */       BannerPatternLayers patterns = (BannerPatternLayers)bannerStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 197 */       boolean hasMaxPatterns = (patterns.layers().size() >= 6);
/* 198 */       if (hasMaxPatterns) {
/* 199 */         this.selectedBannerPatternIndex.set(-1);
/* 200 */         this.resultSlot.set(ItemStack.EMPTY);
/*     */       } else {
/* 202 */         setupResultSlot(patternToDisplay);
/*     */       } 
/*     */     } else {
/* 205 */       this.resultSlot.set(ItemStack.EMPTY);
/*     */     } 
/* 207 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */   
/* 211 */   public List<Holder<BannerPattern>> getSelectablePatterns() { return this.selectablePatterns; }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public int getSelectedBannerPatternIndex() { return this.selectedBannerPatternIndex.get(); }
/*     */ 
/*     */ 
/*     */   
/* 219 */   public void registerUpdateListener(Runnable slotUpdateListener) { this.slotUpdateListener = slotUpdateListener; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 224 */     ItemStack clicked = ItemStack.EMPTY;
/* 225 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 226 */     if (slot != null && slot.hasItem()) {
/* 227 */       ItemStack stack = slot.getItem();
/* 228 */       clicked = stack.copy();
/*     */       
/* 230 */       if (slotIndex == this.resultSlot.index) {
/* 231 */         if (!moveItemStackTo(stack, 4, 40, true)) {
/* 232 */           return ItemStack.EMPTY;
/*     */         }
/* 234 */         slot.onQuickCraft(stack, clicked);
/* 235 */       } else if (slotIndex == this.dyeSlot.index || slotIndex == this.bannerSlot.index || slotIndex == this.patternSlot.index) {
/* 236 */         if (!moveItemStackTo(stack, 4, 40, false)) {
/* 237 */           return ItemStack.EMPTY;
/*     */         }
/* 239 */       } else if (stack.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 240 */         if (!moveItemStackTo(stack, this.bannerSlot.index, this.bannerSlot.index + 1, false)) {
/* 241 */           return ItemStack.EMPTY;
/*     */         }
/* 243 */       } else if (stack.getItem() instanceof DyeItem) {
/* 244 */         if (!moveItemStackTo(stack, this.dyeSlot.index, this.dyeSlot.index + 1, false)) {
/* 245 */           return ItemStack.EMPTY;
/*     */         }
/* 247 */       } else if (stack.has(DataComponents.PROVIDES_BANNER_PATTERNS)) {
/* 248 */         if (!moveItemStackTo(stack, this.patternSlot.index, this.patternSlot.index + 1, false)) {
/* 249 */           return ItemStack.EMPTY;
/*     */         }
/* 251 */       } else if (slotIndex >= 4 && slotIndex < 31) {
/* 252 */         if (!moveItemStackTo(stack, 31, 40, false)) {
/* 253 */           return ItemStack.EMPTY;
/*     */         }
/* 255 */       } else if (slotIndex >= 31 && slotIndex < 40 && 
/* 256 */         !moveItemStackTo(stack, 4, 31, false)) {
/* 257 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 261 */       if (stack.isEmpty()) {
/* 262 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 264 */         slot.setChanged();
/*     */       } 
/* 266 */       if (stack.getCount() == clicked.getCount()) {
/* 267 */         return ItemStack.EMPTY;
/*     */       }
/* 269 */       slot.onTake(player, stack);
/*     */     } 
/*     */     
/* 272 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 277 */     super.removed(player);
/* 278 */     this.access.execute((level, pos) -> clearContainer(player, this.inputContainer));
/*     */   }
/*     */   
/*     */   private void setupResultSlot(Holder<BannerPattern> pattern) {
/* 282 */     ItemStack bannerStack = this.bannerSlot.getItem();
/* 283 */     ItemStack dyeStack = this.dyeSlot.getItem();
/* 284 */     ItemStack result = ItemStack.EMPTY;
/*     */     
/* 286 */     if (!bannerStack.isEmpty() && !dyeStack.isEmpty()) {
/* 287 */       result = bannerStack.copyWithCount(1);
/*     */       
/* 289 */       DyeColor patternColor = ((DyeItem)dyeStack.getItem()).getDyeColor();
/* 290 */       result.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY, layers -> (
/* 291 */           new BannerPatternLayers.Builder()).addAll(layers).add(pattern, patternColor).build());
/*     */     } 
/*     */     
/* 294 */     if (!ItemStack.matches(result, this.resultSlot.getItem())) {
/* 295 */       this.resultSlot.set(result);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 300 */   public Slot getBannerSlot() { return this.bannerSlot; }
/*     */ 
/*     */ 
/*     */   
/* 304 */   public Slot getDyeSlot() { return this.dyeSlot; }
/*     */ 
/*     */ 
/*     */   
/* 308 */   public Slot getPatternSlot() { return this.patternSlot; }
/*     */ 
/*     */ 
/*     */   
/* 312 */   public Slot getResultSlot() { return this.resultSlot; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\LoomMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */