/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AnvilMenu
/*     */   extends ItemCombinerMenu
/*     */ {
/*     */   public static final int INPUT_SLOT = 0;
/*     */   public static final int ADDITIONAL_SLOT = 1;
/*     */   public static final int RESULT_SLOT = 2;
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final boolean DEBUG_COST = false;
/*     */   
/*     */   public static final int MAX_NAME_LENGTH = 50;
/*     */   
/*     */   private int repairItemCountCost;
/*     */   private String itemName;
/*  39 */   private final DataSlot cost = DataSlot.standalone();
/*     */ 
/*     */   
/*     */   private boolean onlyRenaming = false;
/*     */   
/*     */   private static final int COST_FAIL = 0;
/*     */   
/*     */   private static final int COST_BASE = 1;
/*     */   
/*     */   private static final int COST_ADDED_BASE = 1;
/*     */   
/*     */   private static final int COST_REPAIR_MATERIAL = 1;
/*     */   
/*     */   private static final int COST_REPAIR_SACRIFICE = 2;
/*     */   
/*     */   private static final int COST_INCOMPATIBLE_PENALTY = 1;
/*     */   
/*     */   private static final int COST_RENAME = 1;
/*     */   
/*     */   private static final int INPUT_SLOT_X_PLACEMENT = 27;
/*     */   
/*     */   private static final int ADDITIONAL_SLOT_X_PLACEMENT = 76;
/*     */   
/*     */   private static final int RESULT_SLOT_X_PLACEMENT = 134;
/*     */   
/*     */   private static final int SLOT_Y_PLACEMENT = 47;
/*     */ 
/*     */   
/*  67 */   public AnvilMenu(int containerId, Inventory inventory) { this(containerId, inventory, ContainerLevelAccess.NULL); }
/*     */ 
/*     */   
/*     */   public AnvilMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
/*  71 */     super(MenuType.ANVIL, containerId, inventory, access, createInputSlotDefinitions());
/*     */     
/*  73 */     addDataSlot(this.cost);
/*     */   }
/*     */ 
/*     */   
/*  77 */   private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() { return ItemCombinerMenuSlotDefinition.create()
/*  78 */       .withSlot(0, 27, 47, itemStack -> true)
/*  79 */       .withSlot(1, 76, 47, itemStack -> true)
/*  80 */       .withResultSlot(2, 134, 47)
/*  81 */       .build(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected boolean isValidBlock(BlockState state) { return state.is(BlockTags.ANVIL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   protected boolean mayPickup(Player player, boolean hasItem) { return ((player.hasInfiniteMaterials() || player.experienceLevel >= this.cost.get()) && this.cost.get() > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onTake(Player player, ItemStack carried) {
/*  96 */     if (!player.hasInfiniteMaterials()) {
/*  97 */       player.giveExperienceLevels(-this.cost.get());
/*     */     }
/*     */     
/* 100 */     if (this.repairItemCountCost > 0) {
/* 101 */       ItemStack addition = this.inputSlots.getItem(1);
/* 102 */       if (!addition.isEmpty() && addition.getCount() > this.repairItemCountCost) {
/* 103 */         addition.shrink(this.repairItemCountCost);
/* 104 */         this.inputSlots.setItem(1, addition);
/*     */       } else {
/* 106 */         this.inputSlots.setItem(1, ItemStack.EMPTY);
/*     */       } 
/* 108 */     } else if (!this.onlyRenaming) {
/* 109 */       this.inputSlots.setItem(1, ItemStack.EMPTY);
/*     */     } 
/* 111 */     this.cost.set(0);
/* 112 */     if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 113 */       if (!StringUtil.isBlank(this.itemName) && 
/* 114 */         !this.inputSlots.getItem(0).getHoverName().getString().equals(this.itemName))
/*     */       {
/* 116 */         serverPlayer.getTextFilter().processStreamMessage(this.itemName); }  }
/*     */     
/* 118 */     this.inputSlots.setItem(0, ItemStack.EMPTY);
/*     */     
/* 120 */     this.access.execute((level, pos) -> {
/* 121 */           BlockState state = level.getBlockState(pos);
/* 122 */           if (!player.hasInfiniteMaterials() && state.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
/* 123 */             BlockState newBlockState = AnvilBlock.damage(state);
/* 124 */             if (newBlockState == null) {
/* 125 */               level.removeBlock(pos, false);
/* 126 */               level.levelEvent(1029, pos, 0);
/*     */             } else {
/* 128 */               level.setBlock(pos, newBlockState, 2);
/* 129 */               level.levelEvent(1030, pos, 0);
/*     */             } 
/*     */           } else {
/* 132 */             level.levelEvent(1030, pos, 0);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void createResult() {
/* 139 */     ItemStack input = this.inputSlots.getItem(0);
/* 140 */     this.onlyRenaming = false;
/* 141 */     this.cost.set(1);
/* 142 */     int price = 0;
/* 143 */     long tax = 0L;
/* 144 */     int namingCost = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (input.isEmpty() || !EnchantmentHelper.canStoreEnchantments(input)) {
/* 151 */       this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 152 */       this.cost.set(0);
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     ItemStack result = input.copy();
/* 157 */     ItemStack addition = this.inputSlots.getItem(1);
/*     */     
/* 159 */     ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(result));
/*     */     
/* 161 */     tax += ((Integer)input.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue() + ((Integer)addition.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     this.repairItemCountCost = 0;
/*     */     
/* 168 */     if (!addition.isEmpty()) {
/* 169 */       boolean usingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);
/*     */       
/* 171 */       if (result.isDamageableItem() && input.isValidRepairItem(addition)) {
/* 172 */         int repairAmount = Math.min(result.getDamageValue(), result.getMaxDamage() / 4);
/* 173 */         if (repairAmount <= 0) {
/* 174 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 175 */           this.cost.set(0);
/*     */           return;
/*     */         } 
/* 178 */         int count = 0;
/* 179 */         while (repairAmount > 0 && count < addition.getCount()) {
/* 180 */           int resultDamage = result.getDamageValue() - repairAmount;
/* 181 */           result.setDamageValue(resultDamage);
/* 182 */           price++;
/*     */           
/* 184 */           repairAmount = Math.min(result.getDamageValue(), result.getMaxDamage() / 4);
/* 185 */           count++;
/*     */         } 
/* 187 */         this.repairItemCountCost = count;
/*     */       } else {
/* 189 */         if (!usingBook && (!result.is(addition.getItem()) || !result.isDamageableItem())) {
/* 190 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 191 */           this.cost.set(0);
/*     */           return;
/*     */         } 
/* 194 */         if (result.isDamageableItem() && !usingBook) {
/* 195 */           int remaining1 = input.getMaxDamage() - input.getDamageValue();
/* 196 */           int remaining2 = addition.getMaxDamage() - addition.getDamageValue();
/* 197 */           int additional = remaining2 + result.getMaxDamage() * 12 / 100;
/* 198 */           int remaining = remaining1 + additional;
/* 199 */           int resultDamage = result.getMaxDamage() - remaining;
/* 200 */           if (resultDamage < 0) {
/* 201 */             resultDamage = 0;
/*     */           }
/*     */           
/* 204 */           if (resultDamage < result.getDamageValue()) {
/* 205 */             result.setDamageValue(resultDamage);
/* 206 */             price += 2;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 213 */         ItemEnchantments additionalEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(addition);
/* 214 */         boolean isAnyEnchantmentCompatible = false;
/* 215 */         boolean isAnyEnchantmentNotCompatible = false;
/*     */         
/* 217 */         for (Object2IntMap.Entry<Holder<Enchantment>> entry : additionalEnchantments.entrySet()) {
/* 218 */           Holder<Enchantment> enchantmentHolder = (Holder)entry.getKey();
/* 219 */           int current = enchantments.getLevel(enchantmentHolder);
/* 220 */           int level = entry.getIntValue();
/* 221 */           level = (current == level) ? (level + 1) : Math.max(level, current);
/*     */           
/* 223 */           Enchantment enchantment = (Enchantment)enchantmentHolder.value();
/* 224 */           boolean compatible = enchantment.canEnchant(input);
/* 225 */           if (this.player.hasInfiniteMaterials() || input.is(Items.ENCHANTED_BOOK)) {
/* 226 */             compatible = true;
/*     */           }
/*     */           
/* 229 */           for (Holder<Enchantment> other : enchantments.keySet()) {
/* 230 */             if (!other.equals(enchantmentHolder) && !Enchantment.areCompatible(enchantmentHolder, other)) {
/* 231 */               compatible = false;
/* 232 */               price++;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 239 */           if (!compatible) {
/* 240 */             isAnyEnchantmentNotCompatible = true;
/*     */             continue;
/*     */           } 
/* 243 */           isAnyEnchantmentCompatible = true;
/* 244 */           if (level > enchantment.getMaxLevel()) {
/* 245 */             level = enchantment.getMaxLevel();
/*     */           }
/* 247 */           enchantments.set(enchantmentHolder, level);
/* 248 */           int fee = enchantment.getAnvilCost();
/*     */           
/* 250 */           if (usingBook) {
/* 251 */             fee = Math.max(1, fee / 2);
/*     */           }
/*     */           
/* 254 */           price += fee * level;
/*     */           
/* 256 */           if (input.getCount() > 1) {
/* 257 */             price = 40;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 264 */         if (isAnyEnchantmentNotCompatible && !isAnyEnchantmentCompatible) {
/*     */           
/* 266 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 267 */           this.cost.set(0);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 273 */     if (this.itemName == null || StringUtil.isBlank(this.itemName)) {
/* 274 */       if (input.has(DataComponents.CUSTOM_NAME)) {
/* 275 */         namingCost = 1;
/*     */         
/* 277 */         price += namingCost;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 282 */         result.remove(DataComponents.CUSTOM_NAME);
/*     */       } 
/* 284 */     } else if (!this.itemName.equals(input.getHoverName().getString())) {
/* 285 */       namingCost = 1;
/*     */       
/* 287 */       price += namingCost;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       result.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
/*     */     } 
/*     */     
/* 295 */     int finalPrice = (price <= 0) ? 0 : (int)Mth.clamp(tax + price, 0L, 2147483647L);
/* 296 */     this.cost.set(finalPrice);
/* 297 */     if (price <= 0)
/*     */     {
/*     */ 
/*     */       
/* 301 */       result = ItemStack.EMPTY;
/*     */     }
/* 303 */     if (namingCost == price && namingCost > 0) {
/* 304 */       if (this.cost.get() >= 40)
/*     */       {
/*     */ 
/*     */         
/* 308 */         this.cost.set(39);
/*     */       }
/* 310 */       this.onlyRenaming = true;
/*     */     } 
/* 312 */     if (this.cost.get() >= 40 && !this.player.hasInfiniteMaterials())
/*     */     {
/*     */ 
/*     */       
/* 316 */       result = ItemStack.EMPTY;
/*     */     }
/*     */     
/* 319 */     if (!result.isEmpty()) {
/* 320 */       int baseCost = ((Integer)result.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue();
/* 321 */       if (baseCost < ((Integer)addition.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue()) {
/* 322 */         baseCost = ((Integer)addition.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue();
/*     */       }
/*     */       
/* 325 */       if (namingCost != price || namingCost == 0) {
/* 326 */         baseCost = calculateIncreasedRepairCost(baseCost);
/*     */       }
/*     */       
/* 329 */       result.set(DataComponents.REPAIR_COST, Integer.valueOf(baseCost));
/* 330 */       EnchantmentHelper.setEnchantments(result, enchantments.toImmutable());
/*     */     } 
/*     */     
/* 333 */     this.resultSlots.setItem(0, result);
/*     */     
/* 335 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 343 */   public static int calculateIncreasedRepairCost(int baseCost) { return (int)Math.min(baseCost * 2L + 1L, 2147483647L); }
/*     */ 
/*     */   
/*     */   public boolean setItemName(String name) {
/* 347 */     String validatedName = validateName(name);
/* 348 */     if (validatedName == null || validatedName.equals(this.itemName)) {
/* 349 */       return false;
/*     */     }
/*     */     
/* 352 */     this.itemName = validatedName;
/*     */     
/* 354 */     if (getSlot(2).hasItem()) {
/* 355 */       ItemStack itemStack = getSlot(2).getItem();
/*     */       
/* 357 */       if (StringUtil.isBlank(validatedName)) {
/* 358 */         itemStack.remove(DataComponents.CUSTOM_NAME);
/*     */       } else {
/* 360 */         itemStack.set(DataComponents.CUSTOM_NAME, Component.literal(validatedName));
/*     */       } 
/*     */     } 
/*     */     
/* 364 */     createResult();
/* 365 */     return true;
/*     */   }
/*     */   
/*     */   private static String validateName(String name) {
/* 369 */     String filteredName = StringUtil.filterText(name);
/* 370 */     if (filteredName.length() <= 50) {
/* 371 */       return filteredName;
/*     */     }
/* 373 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 377 */   public int getCost() { return this.cost.get(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\AnvilMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */