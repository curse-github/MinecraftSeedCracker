/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.RecipeCraftingHolder;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AbstractFurnaceBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFurnaceBlockEntity
/*     */   extends BaseContainerBlockEntity
/*     */   implements WorldlyContainer, StackedContentsCompatible, RecipeCraftingHolder
/*     */ {
/*     */   protected static final int SLOT_INPUT = 0;
/*     */   protected static final int SLOT_FUEL = 1;
/*     */   protected static final int SLOT_RESULT = 2;
/*     */   public static final int DATA_LIT_TIME = 0;
/*  51 */   private static final int[] SLOTS_FOR_UP = { 0 };
/*     */ 
/*     */   
/*  54 */   private static final int[] SLOTS_FOR_DOWN = { 2, 1 };
/*     */ 
/*     */   
/*  57 */   private static final int[] SLOTS_FOR_SIDES = { 1 };
/*     */   
/*     */   public static final int DATA_LIT_DURATION = 1;
/*     */   
/*     */   public static final int DATA_COOKING_PROGRESS = 2;
/*     */   
/*     */   public static final int DATA_COOKING_TOTAL_TIME = 3;
/*     */   
/*     */   public static final int NUM_DATA_VALUES = 4;
/*     */   
/*     */   public static final int BURN_TIME_STANDARD = 200;
/*     */   public static final int BURN_COOL_SPEED = 2;
/*  69 */   private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
/*     */   
/*     */   private static final short DEFAULT_COOKING_TIMER = 0;
/*     */   
/*     */   private static final short DEFAULT_COOKING_TOTAL_TIME = 0;
/*     */   private static final short DEFAULT_LIT_TIME_REMAINING = 0;
/*     */   private static final short DEFAULT_LIT_TOTAL_TIME = 0;
/*  76 */   protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   
/*     */   private int litTimeRemaining;
/*     */   private int litTotalTime;
/*     */   private int cookingTimer;
/*     */   private int cookingTotalTime;
/*     */   
/*  83 */   protected final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int dataId) {
/*  86 */         switch (dataId) {
/*     */           case 0:
/*  88 */             return AbstractFurnaceBlockEntity.this.litTimeRemaining;
/*     */           case 1:
/*  90 */             return AbstractFurnaceBlockEntity.this.litTotalTime;
/*     */           case 2:
/*  92 */             return AbstractFurnaceBlockEntity.this.cookingTimer;
/*     */           case 3:
/*  94 */             return AbstractFurnaceBlockEntity.this.cookingTotalTime;
/*     */         } 
/*     */ 
/*     */         
/*  98 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(int dataId, int value) {
/* 103 */         switch (dataId) {
/*     */           case 0:
/* 105 */             AbstractFurnaceBlockEntity.this.litTimeRemaining = value;
/*     */             break;
/*     */           case 1:
/* 108 */             AbstractFurnaceBlockEntity.this.litTotalTime = value;
/*     */             break;
/*     */           case 2:
/* 111 */             AbstractFurnaceBlockEntity.this.cookingTimer = value;
/*     */             break;
/*     */           case 3:
/* 114 */             AbstractFurnaceBlockEntity.this.cookingTotalTime = value;
/*     */             break;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       public int getCount() { return 4; }
/*     */     };
/*     */ 
/*     */   
/* 127 */   private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap();
/*     */   
/*     */   private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;
/*     */   
/*     */   protected AbstractFurnaceBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, RecipeType<? extends AbstractCookingRecipe> recipeType) {
/* 132 */     super(type, worldPosition, blockState);
/* 133 */     this.quickCheck = RecipeManager.createCheck(recipeType);
/*     */   }
/*     */ 
/*     */   
/* 137 */   private boolean isLit() { return (this.litTimeRemaining > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 142 */     super.loadAdditional(input);
/*     */     
/* 144 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 145 */     ContainerHelper.loadAllItems(input, this.items);
/*     */     
/* 147 */     this.cookingTimer = input.getShortOr("cooking_time_spent", (short)0);
/* 148 */     this.cookingTotalTime = input.getShortOr("cooking_total_time", (short)0);
/* 149 */     this.litTimeRemaining = input.getShortOr("lit_time_remaining", (short)0);
/* 150 */     this.litTotalTime = input.getShortOr("lit_total_time", (short)0);
/*     */     
/* 152 */     this.recipesUsed.clear();
/* 153 */     this.recipesUsed.putAll((Map)input.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 158 */     super.saveAdditional(output);
/* 159 */     output.putShort("cooking_time_spent", (short)this.cookingTimer);
/* 160 */     output.putShort("cooking_total_time", (short)this.cookingTotalTime);
/* 161 */     output.putShort("lit_time_remaining", (short)this.litTimeRemaining);
/* 162 */     output.putShort("lit_total_time", (short)this.litTotalTime);
/*     */     
/* 164 */     ContainerHelper.saveAllItems(output, this.items);
/*     */     
/* 166 */     output.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity entity) {
/* 171 */     boolean wasLit = entity.isLit();
/* 172 */     boolean changed = false;
/*     */     
/* 174 */     if (entity.isLit())
/*     */     {
/* 176 */       entity.litTimeRemaining--;
/*     */     }
/*     */     
/* 179 */     ItemStack fuel = (ItemStack)entity.items.get(1);
/* 180 */     ItemStack ingredient = (ItemStack)entity.items.get(0);
/* 181 */     boolean hasIngredient = !ingredient.isEmpty();
/* 182 */     boolean hasFuel = !fuel.isEmpty();
/*     */     
/* 184 */     if (entity.isLit() || (hasFuel && hasIngredient)) {
/*     */       RecipeHolder<? extends AbstractCookingRecipe> recipe;
/*     */       
/* 187 */       SingleRecipeInput input = new SingleRecipeInput(ingredient);
/* 188 */       if (hasIngredient) {
/* 189 */         recipe = (RecipeHolder)entity.quickCheck.getRecipeFor(input, level).orElse(null);
/*     */       } else {
/* 191 */         recipe = null;
/*     */       } 
/* 193 */       int maxStackSize = entity.getMaxStackSize();
/* 194 */       if (!entity.isLit() && canBurn(level.registryAccess(), recipe, input, entity.items, maxStackSize)) {
/*     */         
/* 196 */         entity.litTimeRemaining = entity.getBurnDuration(level.fuelValues(), fuel);
/* 197 */         entity.litTotalTime = entity.litTimeRemaining;
/*     */         
/* 199 */         if (entity.isLit()) {
/* 200 */           changed = true;
/*     */           
/* 202 */           if (hasFuel) {
/* 203 */             Item fuelItem = fuel.getItem();
/* 204 */             fuel.shrink(1);
/* 205 */             if (fuel.isEmpty()) {
/* 206 */               entity.items.set(1, fuelItem.getCraftingRemainder());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 212 */       if (entity.isLit() && canBurn(level.registryAccess(), recipe, input, entity.items, maxStackSize)) {
/* 213 */         entity.cookingTimer++;
/*     */         
/* 215 */         if (entity.cookingTimer == entity.cookingTotalTime) {
/* 216 */           entity.cookingTimer = 0;
/* 217 */           entity.cookingTotalTime = getTotalCookTime(level, entity);
/* 218 */           if (burn(level.registryAccess(), recipe, input, entity.items, maxStackSize)) {
/* 219 */             entity.setRecipeUsed(recipe);
/*     */           }
/* 221 */           changed = true;
/*     */         } 
/*     */       } else {
/* 224 */         entity.cookingTimer = 0;
/*     */       } 
/* 226 */     } else if (!entity.isLit() && entity.cookingTimer > 0) {
/* 227 */       entity.cookingTimer = Mth.clamp(entity.cookingTimer - 2, 0, entity.cookingTotalTime);
/*     */     } 
/*     */     
/* 230 */     if (wasLit != entity.isLit()) {
/* 231 */       changed = true;
/* 232 */       state = (BlockState)state.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(entity.isLit()));
/* 233 */       level.setBlock(pos, state, 3);
/*     */     } 
/*     */     
/* 236 */     if (changed) {
/* 237 */       setChanged(level, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean canBurn(RegistryAccess registryAccess, RecipeHolder<? extends AbstractCookingRecipe> recipe, SingleRecipeInput input, NonNullList<ItemStack> items, int maxStackSize) {
/* 242 */     if (((ItemStack)items.get(0)).isEmpty() || recipe == null) {
/* 243 */       return false;
/*     */     }
/* 245 */     ItemStack burnResult = ((AbstractCookingRecipe)recipe.value()).assemble(input, registryAccess);
/* 246 */     if (burnResult.isEmpty()) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     ItemStack resultItemStack = (ItemStack)items.get(2);
/* 251 */     if (resultItemStack.isEmpty()) {
/* 252 */       return true;
/*     */     }
/* 254 */     if (!ItemStack.isSameItemSameComponents(resultItemStack, burnResult)) {
/* 255 */       return false;
/*     */     }
/* 257 */     if (resultItemStack.getCount() < maxStackSize && resultItemStack.getCount() < resultItemStack.getMaxStackSize()) {
/* 258 */       return true;
/*     */     }
/* 260 */     return (resultItemStack.getCount() < burnResult.getMaxStackSize());
/*     */   }
/*     */   
/*     */   private static boolean burn(RegistryAccess registryAccess, RecipeHolder<? extends AbstractCookingRecipe> recipe, SingleRecipeInput input, NonNullList<ItemStack> items, int maxStackSize) {
/* 264 */     if (recipe == null || !canBurn(registryAccess, recipe, input, items, maxStackSize)) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     ItemStack inputItemStack = (ItemStack)items.get(0);
/* 269 */     ItemStack result = ((AbstractCookingRecipe)recipe.value()).assemble(input, registryAccess);
/* 270 */     ItemStack resultItemStack = (ItemStack)items.get(2);
/* 271 */     if (resultItemStack.isEmpty()) {
/* 272 */       items.set(2, result.copy());
/* 273 */     } else if (ItemStack.isSameItemSameComponents(resultItemStack, result)) {
/* 274 */       resultItemStack.grow(1);
/*     */     } 
/*     */     
/* 277 */     if (inputItemStack.is(Blocks.WET_SPONGE.asItem()) && !((ItemStack)items.get(1)).isEmpty() && ((ItemStack)items.get(1)).is(Items.BUCKET)) {
/* 278 */       items.set(1, new ItemStack(Items.WATER_BUCKET));
/*     */     }
/*     */     
/* 281 */     inputItemStack.shrink(1);
/* 282 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 286 */   protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) { return fuelValues.burnDuration(itemStack); }
/*     */ 
/*     */   
/*     */   private static int getTotalCookTime(ServerLevel level, AbstractFurnaceBlockEntity entity) {
/* 290 */     SingleRecipeInput input = new SingleRecipeInput(entity.getItem(0));
/* 291 */     return ((Integer)entity.quickCheck.getRecipeFor(input, level).map(recipeHolder -> Integer.valueOf(((AbstractCookingRecipe)recipeHolder.value()).cookingTime())).orElse(Integer.valueOf(200))).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(Direction direction) {
/* 296 */     if (direction == Direction.DOWN)
/* 297 */       return SLOTS_FOR_DOWN; 
/* 298 */     if (direction == Direction.UP) {
/* 299 */       return SLOTS_FOR_UP;
/*     */     }
/* 301 */     return SLOTS_FOR_SIDES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 307 */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return canPlaceItem(slot, itemStack); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 312 */     if (direction == Direction.DOWN && slot == 1) {
/* 313 */       return (itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET));
/*     */     }
/*     */     
/* 316 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public int getContainerSize() { return this.items.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   protected NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 336 */     ItemStack oldStack = (ItemStack)this.items.get(slot);
/* 337 */     boolean same = (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(oldStack, itemStack));
/* 338 */     this.items.set(slot, itemStack);
/* 339 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*     */     
/* 341 */     if (slot == 0 && !same) { Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 342 */         this.cookingTotalTime = getTotalCookTime(serverLevel, this);
/* 343 */         this.cookingTimer = 0;
/* 344 */         setChanged(); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 350 */     if (slot == 2) {
/* 351 */       return false;
/*     */     }
/* 353 */     if (slot == 1) {
/* 354 */       ItemStack fuelSlot = (ItemStack)this.items.get(1);
/* 355 */       return (this.level.fuelValues().isFuel(itemStack) || (itemStack.is(Items.BUCKET) && !fuelSlot.is(Items.BUCKET)));
/*     */     } 
/* 357 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRecipeUsed(RecipeHolder<?> recipeUsed) {
/* 362 */     if (recipeUsed != null) {
/* 363 */       ResourceKey<Recipe<?>> id = recipeUsed.id();
/* 364 */       this.recipesUsed.addTo(id, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 370 */   public RecipeHolder<?> getRecipeUsed() { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void awardUsedRecipes(Player player, List<ItemStack> itemStacks) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
/* 379 */     List<RecipeHolder<?>> recipesToAward = getRecipesToAwardAndPopExperience(player.level(), player.position());
/* 380 */     player.awardRecipes(recipesToAward);
/* 381 */     for (RecipeHolder<?> recipe : recipesToAward) {
/* 382 */       player.triggerRecipeCrafted(recipe, this.items);
/*     */     }
/* 384 */     this.recipesUsed.clear();
/*     */   }
/*     */   
/*     */   public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 position) {
/* 388 */     List<RecipeHolder<?>> recipesToAward = Lists.newArrayList();
/* 389 */     for (ObjectIterator objectIterator = this.recipesUsed.reference2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry = (Reference2IntMap.Entry)objectIterator.next();
/* 390 */       level.recipeAccess().byKey((ResourceKey)entry.getKey()).ifPresent(recipe -> {
/* 391 */             recipesToAward.add(recipe);
/* 392 */             createExperience(level, position, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).experience());
/*     */           }); }
/*     */     
/* 395 */     return recipesToAward;
/*     */   }
/*     */   
/*     */   private static void createExperience(ServerLevel level, Vec3 position, int amount, float value) {
/* 399 */     int xpReward = Mth.floor(amount * value);
/* 400 */     float xpFraction = Mth.frac(amount * value);
/* 401 */     if (xpFraction != 0.0F && level.random.nextFloat() < xpFraction) {
/* 402 */       xpReward++;
/*     */     }
/*     */     
/* 405 */     ExperienceOrb.award(level, position, xpReward);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 410 */     for (ItemStack itemStack : this.items) {
/* 411 */       contents.accountStack(itemStack);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 417 */     super.preRemoveSideEffects(pos, state);
/* 418 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 419 */       getRecipesToAwardAndPopExperience(serverLevel, Vec3.atCenterOf(pos)); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\AbstractFurnaceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */