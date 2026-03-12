/*     */ package net.minecraft.world.level.storage.loot;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LootTable {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  37 */   public static final Codec<ResourceKey<LootTable>> KEY_CODEC = ResourceKey.codec(Registries.LOOT_TABLE);
/*     */   
/*  39 */   public static final ContextKeySet DEFAULT_PARAM_SET = LootContextParamSets.ALL_PARAMS;
/*     */   
/*     */   public static final long RANDOMIZE_SEED = 0L;
/*     */   
/*  43 */   public static final Codec<LootTable> DIRECT_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Codec<Holder<LootTable>> CODEC = RegistryFileCodec.create(Registries.LOOT_TABLE, DIRECT_CODEC);
/*     */   
/*  53 */   public static final LootTable EMPTY = new LootTable(LootContextParamSets.EMPTY, Optional.empty(), List.of(), List.of());
/*     */   
/*     */   private final ContextKeySet paramSet;
/*     */   
/*     */   private final Optional<Identifier> randomSequence;
/*     */   
/*     */   private final List<LootPool> pools;
/*     */   private final List<LootItemFunction> functions;
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   
/*     */   private LootTable(ContextKeySet paramSet, Optional<Identifier> randomSequence, List<LootPool> pools, List<LootItemFunction> functions) {
/*  64 */     this.paramSet = paramSet;
/*  65 */     this.randomSequence = randomSequence;
/*  66 */     this.pools = pools;
/*  67 */     this.functions = functions;
/*  68 */     this.compositeFunction = LootItemFunctions.compose(functions);
/*     */   }
/*     */   
/*     */   public static Consumer<ItemStack> createStackSplitter(ServerLevel level, Consumer<ItemStack> output) {
/*  72 */     return result -> {
/*  73 */         if (!result.isItemEnabled(level.enabledFeatures())) {
/*     */           return;
/*     */         }
/*  76 */         if (result.getCount() < result.getMaxStackSize()) {
/*  77 */           output.accept(result);
/*     */         } else {
/*  79 */           int count = result.getCount();
/*  80 */           while (count > 0) {
/*  81 */             ItemStack copy = result.copyWithCount(Math.min(result.getMaxStackSize(), count));
/*  82 */             count -= copy.getCount();
/*  83 */             output.accept(copy);
/*     */           } 
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*  90 */   public void getRandomItemsRaw(LootParams params, Consumer<ItemStack> output) { getRandomItemsRaw((new LootContext.Builder(params)).create(this.randomSequence), output); }
/*     */ 
/*     */   
/*     */   public void getRandomItemsRaw(LootContext context, Consumer<ItemStack> output) {
/*  94 */     LootContext.VisitedEntry<?> breadcrumb = LootContext.createVisitedEntry(this);
/*  95 */     if (context.pushVisitedElement(breadcrumb)) {
/*  96 */       Consumer<ItemStack> decoratedOutput = LootItemFunction.decorate(this.compositeFunction, output, context);
/*  97 */       for (LootPool pool : this.pools) {
/*  98 */         pool.addRandomItems(decoratedOutput, context);
/*     */       }
/* 100 */       context.popVisitedElement(breadcrumb);
/*     */     } else {
/* 102 */       LOGGER.warn("Detected infinite loop in loot tables");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 107 */   public void getRandomItems(LootParams params, long optionalLootTableSeed, Consumer<ItemStack> output) { getRandomItemsRaw((new LootContext.Builder(params)).withOptionalRandomSeed(optionalLootTableSeed).create(this.randomSequence), createStackSplitter(params.getLevel(), output)); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public void getRandomItems(LootParams params, Consumer<ItemStack> output) { getRandomItemsRaw(params, createStackSplitter(params.getLevel(), output)); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void getRandomItems(LootContext context, Consumer<ItemStack> output) { getRandomItemsRaw(context, createStackSplitter(context.getLevel(), output)); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params, RandomSource randomSource) { return getRandomItems((new LootContext.Builder(params)).withOptionalRandomSource(randomSource).create(this.randomSequence)); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params, long optionalLootTableSeed) { return getRandomItems((new LootContext.Builder(params)).withOptionalRandomSeed(optionalLootTableSeed).create(this.randomSequence)); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params) { return getRandomItems((new LootContext.Builder(params)).create(this.randomSequence)); }
/*     */ 
/*     */   
/*     */   private ObjectArrayList<ItemStack> getRandomItems(LootContext context) {
/* 131 */     ObjectArrayList<ItemStack> result = new ObjectArrayList<ItemStack>();
/* 132 */     Objects.requireNonNull(result); getRandomItems(context, result::add);
/* 133 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 137 */   public ContextKeySet getParamSet() { return this.paramSet; }
/*     */ 
/*     */   
/*     */   public void validate(ValidationContext context) {
/* 141 */     for (int i = 0; i < this.pools.size(); i++) {
/* 142 */       ((LootPool)this.pools.get(i)).validate(context.forChild(new ProblemReporter.IndexedFieldPathElement("pools", i)));
/*     */     }
/*     */     
/* 145 */     for (int i = 0; i < this.functions.size(); i++) {
/* 146 */       ((LootItemFunction)this.functions.get(i)).validate(context.forChild(new ProblemReporter.IndexedFieldPathElement("functions", i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void fill(Container container, LootParams params, long optionalRandomSeed) {
/* 151 */     LootContext context = (new LootContext.Builder(params)).withOptionalRandomSeed(optionalRandomSeed).create(this.randomSequence);
/* 152 */     ObjectArrayList<ItemStack> itemStacks = getRandomItems(context);
/* 153 */     RandomSource random = context.getRandom();
/* 154 */     List<Integer> availableSlots = getAvailableSlots(container, random);
/* 155 */     shuffleAndSplitItems(itemStacks, availableSlots.size(), random);
/* 156 */     for (ObjectListIterator objectListIterator = itemStacks.iterator(); objectListIterator.hasNext(); ) { ItemStack itemStack = (ItemStack)objectListIterator.next();
/* 157 */       if (availableSlots.isEmpty()) {
/* 158 */         LOGGER.warn("Tried to over-fill a container");
/*     */         
/*     */         return;
/*     */       } 
/* 162 */       if (itemStack.isEmpty()) {
/* 163 */         container.setItem(((Integer)availableSlots.remove(availableSlots.size() - 1)).intValue(), ItemStack.EMPTY); continue;
/*     */       } 
/* 165 */       container.setItem(((Integer)availableSlots.remove(availableSlots.size() - 1)).intValue(), itemStack); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void shuffleAndSplitItems(ObjectArrayList<ItemStack> result, int availableSlots, RandomSource random) {
/* 171 */     List<ItemStack> splittableItems = Lists.newArrayList();
/* 172 */     for (ObjectListIterator objectListIterator = result.iterator(); objectListIterator.hasNext(); ) {
/* 173 */       ItemStack itemStack = (ItemStack)objectListIterator.next();
/* 174 */       if (itemStack.isEmpty()) {
/* 175 */         objectListIterator.remove(); continue;
/* 176 */       }  if (itemStack.getCount() > 1) {
/* 177 */         splittableItems.add(itemStack);
/* 178 */         objectListIterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     while (availableSlots - result.size() - splittableItems.size() > 0 && !splittableItems.isEmpty()) {
/* 183 */       ItemStack itemStack = (ItemStack)splittableItems.remove(Mth.nextInt(random, 0, splittableItems.size() - 1));
/* 184 */       int remove = Mth.nextInt(random, 1, itemStack.getCount() / 2);
/* 185 */       ItemStack copy = itemStack.split(remove);
/*     */       
/* 187 */       if (itemStack.getCount() > 1 && random.nextBoolean()) {
/* 188 */         splittableItems.add(itemStack);
/*     */       } else {
/* 190 */         result.add(itemStack);
/*     */       } 
/*     */       
/* 193 */       if (copy.getCount() > 1 && random.nextBoolean()) {
/* 194 */         splittableItems.add(copy); continue;
/*     */       } 
/* 196 */       result.add(copy);
/*     */     } 
/*     */ 
/*     */     
/* 200 */     result.addAll(splittableItems);
/*     */     
/* 202 */     Util.shuffle(result, random);
/*     */   }
/*     */   
/*     */   private List<Integer> getAvailableSlots(Container container, RandomSource random) {
/* 206 */     ObjectArrayList<Integer> slots = new ObjectArrayList<Integer>();
/*     */     
/* 208 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 209 */       if (container.getItem(i).isEmpty()) {
/* 210 */         slots.add(Integer.valueOf(i));
/*     */       }
/*     */     } 
/*     */     
/* 214 */     Util.shuffle(slots, random);
/* 215 */     return slots;
/*     */   }
/*     */   
/*     */   public static class Builder extends Object implements FunctionUserBuilder<Builder> {
/* 219 */     private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();
/*     */     
/* 221 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */     
/* 223 */     private ContextKeySet paramSet = LootTable.DEFAULT_PARAM_SET;
/* 224 */     private Optional<Identifier> randomSequence = Optional.empty();
/*     */     
/*     */     public Builder withPool(LootPool.Builder pool) {
/* 227 */       this.pools.add(pool.build());
/* 228 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setParamSet(ContextKeySet paramSet) {
/* 232 */       this.paramSet = paramSet;
/* 233 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRandomSequence(Identifier key) {
/* 237 */       this.randomSequence = Optional.of(key);
/* 238 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder function) {
/* 243 */       this.functions.add(function.build());
/* 244 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 249 */     public Builder unwrap() { return this; }
/*     */ 
/*     */ 
/*     */     
/* 253 */     public LootTable build() { return new LootTable(this.paramSet, this.randomSequence, this.pools.build(), this.functions.build()); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 258 */   public static Builder lootTable() { return new Builder(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */