/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BrewingStandMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BrewingStandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BrewingStandBlockEntity
/*     */   extends BaseContainerBlockEntity
/*     */   implements WorldlyContainer
/*     */ {
/*     */   private static final int INGREDIENT_SLOT = 3;
/*     */   private static final int FUEL_SLOT = 4;
/*  34 */   private static final int[] SLOTS_FOR_UP = { 3 };
/*     */ 
/*     */   
/*  37 */   private static final int[] SLOTS_FOR_DOWN = { 0, 1, 2, 3 };
/*     */ 
/*     */   
/*  40 */   private static final int[] SLOTS_FOR_SIDES = { 0, 1, 2, 4 };
/*     */   
/*     */   public static final int FUEL_USES = 20;
/*     */   
/*     */   public static final int DATA_BREW_TIME = 0;
/*     */   
/*     */   public static final int DATA_FUEL_USES = 1;
/*     */   
/*     */   public static final int NUM_DATA_VALUES = 2;
/*     */   
/*     */   private static final short DEFAULT_BREW_TIME = 0;
/*     */   
/*     */   private static final byte DEFAULT_FUEL = 0;
/*  53 */   private static final Component DEFAULT_NAME = Component.translatable("container.brewing");
/*     */   
/*  55 */   private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
/*     */   
/*     */   private int brewTime;
/*     */   private boolean[] lastPotionCount;
/*     */   private Item ingredient;
/*     */   private int fuel;
/*     */   
/*  62 */   protected final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int dataId) {
/*  65 */         switch (dataId) { case 0: case 1:  }  return 
/*     */ 
/*     */           
/*  68 */           0;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void set(int dataId, int value) {
/*  74 */         switch (dataId) { case 0:
/*  75 */             BrewingStandBlockEntity.this.brewTime = value; break;
/*  76 */           case 1: BrewingStandBlockEntity.this.fuel = value;
/*     */             break; }
/*     */       
/*     */       }
/*     */ 
/*     */       
/*  82 */       public int getCount() { return 2; }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  87 */   public BrewingStandBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.BREWING_STAND, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public int getContainerSize() { return this.items.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
/*     */ 
/*     */   
/*     */   public static void serverTick(Level level, BlockPos pos, BlockState selfState, BrewingStandBlockEntity entity) {
/* 111 */     ItemStack fuel = (ItemStack)entity.items.get(4);
/* 112 */     if (entity.fuel <= 0 && fuel.is(ItemTags.BREWING_FUEL)) {
/* 113 */       entity.fuel = 20;
/* 114 */       fuel.shrink(1);
/* 115 */       setChanged(level, pos, selfState);
/*     */     } 
/*     */     
/* 118 */     boolean brewable = isBrewable(level.potionBrewing(), entity.items);
/* 119 */     boolean isBrewing = (entity.brewTime > 0);
/* 120 */     ItemStack ingredient = (ItemStack)entity.items.get(3);
/* 121 */     if (isBrewing) {
/* 122 */       entity.brewTime--;
/*     */       
/* 124 */       boolean isDoneBrewing = (entity.brewTime == 0);
/* 125 */       if (isDoneBrewing && brewable) {
/*     */         
/* 127 */         doBrew(level, pos, entity.items);
/* 128 */       } else if (!brewable || !ingredient.is(entity.ingredient)) {
/* 129 */         entity.brewTime = 0;
/*     */       } 
/* 131 */       setChanged(level, pos, selfState);
/* 132 */     } else if (brewable && entity.fuel > 0) {
/* 133 */       entity.fuel--;
/* 134 */       entity.brewTime = 400;
/* 135 */       entity.ingredient = ingredient.getItem();
/* 136 */       setChanged(level, pos, selfState);
/*     */     } 
/*     */     
/* 139 */     boolean[] newCount = entity.getPotionBits();
/* 140 */     if (!Arrays.equals(newCount, entity.lastPotionCount)) {
/* 141 */       entity.lastPotionCount = newCount;
/* 142 */       BlockState state = selfState;
/* 143 */       if (!(state.getBlock() instanceof BrewingStandBlock)) {
/*     */         return;
/*     */       }
/* 146 */       for (int i = 0; i < BrewingStandBlock.HAS_BOTTLE.length; i++) {
/* 147 */         state = (BlockState)state.setValue(BrewingStandBlock.HAS_BOTTLE[i], Boolean.valueOf(newCount[i]));
/*     */       }
/* 149 */       level.setBlock(pos, state, 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean[] getPotionBits() {
/* 155 */     boolean[] result = new boolean[3];
/* 156 */     for (int potion = 0; potion < 3; potion++) {
/* 157 */       if (!((ItemStack)this.items.get(potion)).isEmpty()) {
/* 158 */         result[potion] = true;
/*     */       }
/*     */     } 
/* 161 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean isBrewable(PotionBrewing potionBrewing, NonNullList<ItemStack> items) {
/* 165 */     ItemStack ingredient = (ItemStack)items.get(3);
/* 166 */     if (ingredient.isEmpty()) {
/* 167 */       return false;
/*     */     }
/*     */     
/* 170 */     if (!potionBrewing.isIngredient(ingredient)) {
/* 171 */       return false;
/*     */     }
/*     */     
/* 174 */     for (int dest = 0; dest < 3; dest++) {
/* 175 */       ItemStack itemStack = (ItemStack)items.get(dest);
/* 176 */       if (!itemStack.isEmpty())
/*     */       {
/*     */ 
/*     */         
/* 180 */         if (potionBrewing.hasMix(itemStack, ingredient))
/* 181 */           return true; 
/*     */       }
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   private static void doBrew(Level level, BlockPos pos, NonNullList<ItemStack> items) {
/* 188 */     ItemStack ingredient = (ItemStack)items.get(3);
/* 189 */     PotionBrewing potionBrewing = level.potionBrewing();
/*     */     
/* 191 */     for (int dest = 0; dest < 3; dest++) {
/* 192 */       items.set(dest, potionBrewing.mix(ingredient, (ItemStack)items.get(dest)));
/*     */     }
/*     */     
/* 195 */     ingredient.shrink(1);
/* 196 */     ItemStack remainder = ingredient.getItem().getCraftingRemainder();
/* 197 */     if (!remainder.isEmpty()) {
/* 198 */       if (ingredient.isEmpty()) {
/* 199 */         ingredient = remainder;
/*     */       } else {
/* 201 */         Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder);
/*     */       } 
/*     */     }
/*     */     
/* 205 */     items.set(3, ingredient);
/*     */     
/* 207 */     level.levelEvent(1035, pos, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 212 */     super.loadAdditional(input);
/*     */     
/* 214 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 215 */     ContainerHelper.loadAllItems(input, this.items);
/*     */     
/* 217 */     this.brewTime = input.getShortOr("BrewTime", (short)0);
/* 218 */     if (this.brewTime > 0) {
/* 219 */       this.ingredient = ((ItemStack)this.items.get(3)).getItem();
/*     */     }
/* 221 */     this.fuel = input.getByteOr("Fuel", (byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 226 */     super.saveAdditional(output);
/*     */     
/* 228 */     output.putShort("BrewTime", (short)this.brewTime);
/* 229 */     ContainerHelper.saveAllItems(output, this.items);
/*     */     
/* 231 */     output.putByte("Fuel", (byte)this.fuel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 236 */     if (slot == 3) {
/* 237 */       PotionBrewing potionBrewing = (this.level != null) ? this.level.potionBrewing() : PotionBrewing.EMPTY;
/* 238 */       return potionBrewing.isIngredient(itemStack);
/*     */     } 
/*     */     
/* 241 */     if (slot == 4) {
/* 242 */       return itemStack.is(ItemTags.BREWING_FUEL);
/*     */     }
/*     */     
/* 245 */     return ((itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE)) && getItem(slot).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(Direction direction) {
/* 250 */     if (direction == Direction.UP) {
/* 251 */       return SLOTS_FOR_UP;
/*     */     }
/* 253 */     if (direction == Direction.DOWN) {
/* 254 */       return SLOTS_FOR_DOWN;
/*     */     }
/* 256 */     return SLOTS_FOR_SIDES;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 261 */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return canPlaceItem(slot, itemStack); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 266 */     if (slot == 3) {
/* 267 */       return itemStack.is(Items.GLASS_BOTTLE);
/*     */     }
/* 269 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 274 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new BrewingStandMenu(containerId, inventory, this, this.dataAccess); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BrewingStandBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */