/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.crafting.CampfireCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CampfireBlockEntity
/*     */   extends BlockEntity
/*     */   implements Clearable {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int BURN_COOL_SPEED = 2;
/*     */   private static final int NUM_SLOTS = 4;
/*  48 */   private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
/*  49 */   private final int[] cookingProgress = new int[4];
/*  50 */   private final int[] cookingTime = new int[4];
/*     */ 
/*     */   
/*  53 */   public CampfireBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.CAMPFIRE, worldPosition, blockState); }
/*     */ 
/*     */   
/*     */   public static void cookTick(ServerLevel level, BlockPos pos, BlockState state, CampfireBlockEntity entity, RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> recipeCache) {
/*  57 */     boolean changed = false;
/*  58 */     for (int slot = 0; slot < entity.items.size(); slot++) {
/*  59 */       ItemStack itemStack = (ItemStack)entity.items.get(slot);
/*  60 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/*  64 */         changed = true;
/*  65 */         entity.cookingProgress[slot] = entity.cookingProgress[slot] + 1;
/*  66 */         if (entity.cookingProgress[slot] >= entity.cookingTime[slot]) {
/*  67 */           SingleRecipeInput input = new SingleRecipeInput(itemStack);
/*     */           
/*  69 */           ItemStack result = (ItemStack)recipeCache.getRecipeFor(input, level).map(r -> ((CampfireCookingRecipe)r.value()).assemble(input, level.registryAccess())).orElse(itemStack);
/*  70 */           if (result.isItemEnabled(level.enabledFeatures())) {
/*  71 */             Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
/*  72 */             entity.items.set(slot, ItemStack.EMPTY);
/*  73 */             level.sendBlockUpdated(pos, state, state, 3);
/*  74 */             level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  79 */     if (changed) {
/*  80 */       setChanged(level, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void cooldownTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity entity) {
/*  85 */     boolean changed = false;
/*     */     
/*  87 */     for (int slot = 0; slot < entity.items.size(); slot++) {
/*  88 */       if (entity.cookingProgress[slot] > 0) {
/*  89 */         changed = true;
/*  90 */         entity.cookingProgress[slot] = Mth.clamp(entity.cookingProgress[slot] - 2, 0, entity.cookingTime[slot]);
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (changed) {
/*  95 */       setChanged(level, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void particleTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity entity) {
/* 100 */     RandomSource random = level.random;
/*     */     
/* 102 */     if (random.nextFloat() < 0.11F) {
/* 103 */       for (int i = 0; i < random.nextInt(2) + 2; i++) {
/* 104 */         CampfireBlock.makeParticles(level, pos, ((Boolean)state.getValue(CampfireBlock.SIGNAL_FIRE)).booleanValue(), false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 109 */     int rotation = ((Direction)state.getValue(CampfireBlock.FACING)).get2DDataValue();
/* 110 */     for (int slot = 0; slot < entity.items.size(); slot++) {
/* 111 */       if (!((ItemStack)entity.items.get(slot)).isEmpty() && random.nextFloat() < 0.2F) {
/* 112 */         Direction direction = Direction.from2DDataValue(Math.floorMod(slot + rotation, 4));
/* 113 */         float distanceFromCenter = 0.3125F;
/*     */         
/* 115 */         double x = pos.getX() + 0.5D - (direction.getStepX() * 0.3125F) + (direction.getClockWise().getStepX() * 0.3125F);
/* 116 */         double y = pos.getY() + 0.5D;
/* 117 */         double z = pos.getZ() + 0.5D - (direction.getStepZ() * 0.3125F) + (direction.getClockWise().getStepZ() * 0.3125F);
/*     */         
/* 119 */         for (int i = 0; i < 4; i++) {
/* 120 */           level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 127 */   public NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 132 */     super.loadAdditional(input);
/*     */     
/* 134 */     this.items.clear();
/* 135 */     ContainerHelper.loadAllItems(input, this.items);
/*     */     
/* 137 */     input.getIntArray("CookingTimes").ifPresentOrElse(cookingTimes -> 
/* 138 */         System.arraycopy(cookingTimes, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, cookingTimes.length)), () -> 
/* 139 */         Arrays.fill(this.cookingProgress, 0));
/*     */ 
/*     */     
/* 142 */     input.getIntArray("CookingTotalTimes").ifPresentOrElse(cookingTimes -> 
/* 143 */         System.arraycopy(cookingTimes, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, cookingTimes.length)), () -> 
/* 144 */         Arrays.fill(this.cookingTime, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 150 */     super.saveAdditional(output);
/*     */     
/* 152 */     ContainerHelper.saveAllItems(output, this.items, true);
/*     */     
/* 154 */     output.putIntArray("CookingTimes", this.cookingProgress);
/* 155 */     output.putIntArray("CookingTotalTimes", this.cookingTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 165 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 166 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
/* 167 */       ContainerHelper.saveAllItems(output, this.items, true);
/* 168 */       CompoundTag compoundTag = output.buildResult();
/* 169 */       reporter.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 173 */      } public boolean placeFood(ServerLevel serverLevel, LivingEntity sourceEntity, ItemStack placeItem) { for (int slot = 0; slot < this.items.size(); slot++) {
/* 174 */       ItemStack item = (ItemStack)this.items.get(slot);
/* 175 */       if (item.isEmpty()) {
/* 176 */         Optional<RecipeHolder<CampfireCookingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(placeItem), serverLevel);
/* 177 */         if (recipe.isEmpty())
/*     */         {
/* 179 */           return false;
/*     */         }
/*     */         
/* 182 */         this.cookingTime[slot] = ((CampfireCookingRecipe)((RecipeHolder)recipe.get()).value()).cookingTime();
/* 183 */         this.cookingProgress[slot] = 0;
/*     */         
/* 185 */         this.items.set(slot, placeItem.consumeAndReturn(1, sourceEntity));
/* 186 */         serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(sourceEntity, getBlockState()));
/*     */         
/* 188 */         markUpdated();
/*     */         
/* 190 */         return true;
/*     */       } 
/*     */     } 
/* 193 */     return false; }
/*     */ 
/*     */   
/*     */   private void markUpdated() {
/* 197 */     setChanged();
/* 198 */     getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public void clearContent() { this.items.clear(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 208 */     if (this.level != null) {
/* 209 */       Containers.dropContents(this.level, pos, getItems());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 215 */     super.applyImplicitComponents(components);
/* 216 */     ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(getItems());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 221 */     super.collectImplicitComponents(components);
/* 222 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 227 */   public void removeComponentsFromTag(ValueOutput output) { output.discard("Items"); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\CampfireBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */