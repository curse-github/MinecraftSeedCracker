/*     */ package net.minecraft.core.cauldron;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LayeredCauldronBlock;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public interface CauldronInteraction {
/*     */   public static final class InteractionMap extends Record {
/*     */     private final String name;
/*     */     private final Map<Item, CauldronInteraction> map;
/*     */     
/*  37 */     public InteractionMap(String name, Map<Item, CauldronInteraction> map) { this.name = name; this.map = map; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  37 */       //   0	7	0	this	Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;
/*  37 */       //   0	8	1	o	Ljava/lang/Object; } public Map<Item, CauldronInteraction> map() { return this.map; }
/*     */   }
/*  39 */   public static final Map<String, InteractionMap> INTERACTIONS = new Object2ObjectArrayMap(); public static final Codec<InteractionMap> CODEC; public static final InteractionMap EMPTY; public static final InteractionMap WATER; public static final InteractionMap LAVA; public static final InteractionMap POWDER_SNOW; static  {
/*  40 */     Objects.requireNonNull(INTERACTIONS); CODEC = Codec.stringResolver(InteractionMap::name, INTERACTIONS::get);
/*     */     
/*  42 */     EMPTY = newInteractionMap("empty");
/*  43 */     WATER = newInteractionMap("water");
/*  44 */     LAVA = newInteractionMap("lava");
/*  45 */     POWDER_SNOW = newInteractionMap("powder_snow");
/*     */   }
/*     */   static InteractionMap newInteractionMap(String name) {
/*  48 */     Object2ObjectOpenHashMap<Item, CauldronInteraction> map = new Object2ObjectOpenHashMap<Item, CauldronInteraction>();
/*  49 */     map.defaultReturnValue((state, level, pos, player, hand, itemInHand) -> InteractionResult.TRY_WITH_EMPTY_HAND);
/*  50 */     InteractionMap interactionMap = new InteractionMap(name, map);
/*  51 */     INTERACTIONS.put(name, interactionMap);
/*  52 */     return interactionMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void bootStrap() {
/*  59 */     empty = EMPTY.map();
/*  60 */     addDefaultInteractions(empty);
/*     */     
/*  62 */     empty.put(Items.POTION, (state, level, pos, player, hand, itemInHand) -> {
/*  63 */           PotionContents potion = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/*  64 */           if (potion == null || !potion.is(Potions.WATER)) {
/*  65 */             return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */           }
/*     */           
/*  68 */           if (!level.isClientSide()) {
/*  69 */             Item usedItem = itemInHand.getItem();
/*  70 */             player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, new ItemStack(Items.GLASS_BOTTLE)));
/*  71 */             player.awardStat(Stats.USE_CAULDRON);
/*  72 */             player.awardStat(Stats.ITEM_USED.get(usedItem));
/*     */             
/*  74 */             level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
/*  75 */             level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  76 */             level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
/*     */           } 
/*     */           
/*  79 */           return InteractionResult.SUCCESS;
/*     */         });
/*     */ 
/*     */     
/*  83 */     Map<Item, CauldronInteraction> water = WATER.map();
/*  84 */     addDefaultInteractions(water);
/*     */     
/*  86 */     water.put(Items.BUCKET, (state, level, pos, player, hand, itemInHand) -> 
/*  87 */         fillBucket(state, level, pos, player, hand, itemInHand, new ItemStack(Items.WATER_BUCKET), (), SoundEvents.BUCKET_FILL));
/*     */ 
/*     */     
/*  90 */     water.put(Items.GLASS_BOTTLE, (state, level, pos, player, hand, itemInHand) -> {
/*  91 */           if (!level.isClientSide()) {
/*  92 */             Item usedItem = itemInHand.getItem();
/*  93 */             player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
/*  94 */             player.awardStat(Stats.USE_CAULDRON);
/*  95 */             player.awardStat(Stats.ITEM_USED.get(usedItem));
/*  96 */             LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*  97 */             level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  98 */             level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
/*     */           } 
/* 100 */           return InteractionResult.SUCCESS;
/*     */         });
/*     */     
/* 103 */     water.put(Items.POTION, (state, level, pos, player, hand, itemInHand) -> {
/* 104 */           if (((Integer)state.getValue(LayeredCauldronBlock.LEVEL)).intValue() == 3) {
/* 105 */             return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */           }
/*     */           
/* 108 */           PotionContents potion = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/* 109 */           if (potion == null || !potion.is(Potions.WATER)) {
/* 110 */             return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */           }
/*     */           
/* 113 */           if (!level.isClientSide()) {
/* 114 */             player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, new ItemStack(Items.GLASS_BOTTLE)));
/* 115 */             player.awardStat(Stats.USE_CAULDRON);
/* 116 */             player.awardStat(Stats.ITEM_USED.get(itemInHand.getItem()));
/*     */             
/* 118 */             level.setBlockAndUpdate(pos, (BlockState)state.cycle(LayeredCauldronBlock.LEVEL));
/* 119 */             level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 120 */             level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
/*     */           } 
/* 122 */           return InteractionResult.SUCCESS;
/*     */         });
/*     */     
/* 125 */     water.put(Items.LEATHER_BOOTS, CauldronInteraction::dyedItemIteration);
/* 126 */     water.put(Items.LEATHER_LEGGINGS, CauldronInteraction::dyedItemIteration);
/* 127 */     water.put(Items.LEATHER_CHESTPLATE, CauldronInteraction::dyedItemIteration);
/* 128 */     water.put(Items.LEATHER_HELMET, CauldronInteraction::dyedItemIteration);
/* 129 */     water.put(Items.LEATHER_HORSE_ARMOR, CauldronInteraction::dyedItemIteration);
/* 130 */     water.put(Items.WOLF_ARMOR, CauldronInteraction::dyedItemIteration);
/*     */     
/* 132 */     water.put(Items.WHITE_BANNER, CauldronInteraction::bannerInteraction);
/* 133 */     water.put(Items.GRAY_BANNER, CauldronInteraction::bannerInteraction);
/* 134 */     water.put(Items.BLACK_BANNER, CauldronInteraction::bannerInteraction);
/* 135 */     water.put(Items.BLUE_BANNER, CauldronInteraction::bannerInteraction);
/* 136 */     water.put(Items.BROWN_BANNER, CauldronInteraction::bannerInteraction);
/* 137 */     water.put(Items.CYAN_BANNER, CauldronInteraction::bannerInteraction);
/* 138 */     water.put(Items.GREEN_BANNER, CauldronInteraction::bannerInteraction);
/* 139 */     water.put(Items.LIGHT_BLUE_BANNER, CauldronInteraction::bannerInteraction);
/* 140 */     water.put(Items.LIGHT_GRAY_BANNER, CauldronInteraction::bannerInteraction);
/* 141 */     water.put(Items.LIME_BANNER, CauldronInteraction::bannerInteraction);
/* 142 */     water.put(Items.MAGENTA_BANNER, CauldronInteraction::bannerInteraction);
/* 143 */     water.put(Items.ORANGE_BANNER, CauldronInteraction::bannerInteraction);
/* 144 */     water.put(Items.PINK_BANNER, CauldronInteraction::bannerInteraction);
/* 145 */     water.put(Items.PURPLE_BANNER, CauldronInteraction::bannerInteraction);
/* 146 */     water.put(Items.RED_BANNER, CauldronInteraction::bannerInteraction);
/* 147 */     water.put(Items.YELLOW_BANNER, CauldronInteraction::bannerInteraction);
/*     */     
/* 149 */     water.put(Items.WHITE_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 150 */     water.put(Items.GRAY_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 151 */     water.put(Items.BLACK_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 152 */     water.put(Items.BLUE_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 153 */     water.put(Items.BROWN_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 154 */     water.put(Items.CYAN_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 155 */     water.put(Items.GREEN_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 156 */     water.put(Items.LIGHT_BLUE_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 157 */     water.put(Items.LIGHT_GRAY_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 158 */     water.put(Items.LIME_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 159 */     water.put(Items.MAGENTA_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 160 */     water.put(Items.ORANGE_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 161 */     water.put(Items.PINK_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 162 */     water.put(Items.PURPLE_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 163 */     water.put(Items.RED_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/* 164 */     water.put(Items.YELLOW_SHULKER_BOX, CauldronInteraction::shulkerBoxInteraction);
/*     */ 
/*     */     
/* 167 */     Map<Item, CauldronInteraction> lava = LAVA.map();
/* 168 */     lava.put(Items.BUCKET, (state, level, pos, player, hand, itemInHand) -> 
/* 169 */         fillBucket(state, level, pos, player, hand, itemInHand, new ItemStack(Items.LAVA_BUCKET), (), SoundEvents.BUCKET_FILL_LAVA));
/*     */ 
/*     */     
/* 172 */     addDefaultInteractions(lava);
/*     */     
/* 174 */     Map<Item, CauldronInteraction> powderSnow = POWDER_SNOW.map();
/* 175 */     powderSnow.put(Items.BUCKET, (state, level, pos, player, hand, itemInHand) -> 
/* 176 */         fillBucket(state, level, pos, player, hand, itemInHand, new ItemStack(Items.POWDER_SNOW_BUCKET), (), SoundEvents.BUCKET_FILL_POWDER_SNOW));
/*     */ 
/*     */     
/* 179 */     addDefaultInteractions(powderSnow);
/*     */   }
/*     */   
/*     */   static void addDefaultInteractions(Map<Item, CauldronInteraction> interactionMap) {
/* 183 */     interactionMap.put(Items.LAVA_BUCKET, CauldronInteraction::fillLavaInteraction);
/* 184 */     interactionMap.put(Items.WATER_BUCKET, CauldronInteraction::fillWaterInteraction);
/* 185 */     interactionMap.put(Items.POWDER_SNOW_BUCKET, CauldronInteraction::fillPowderSnowInteraction);
/*     */   }
/*     */   
/*     */   static InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand, ItemStack newItem, Predicate<BlockState> canFill, SoundEvent soundEvent) {
/* 189 */     if (!canFill.test(state)) {
/* 190 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 193 */     if (!level.isClientSide()) {
/* 194 */       Item itemUsed = itemInHand.getItem();
/* 195 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, newItem));
/* 196 */       player.awardStat(Stats.USE_CAULDRON);
/* 197 */       player.awardStat(Stats.ITEM_USED.get(itemUsed));
/*     */       
/* 199 */       level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
/* 200 */       level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 201 */       level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
/*     */     } 
/* 203 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   static InteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand, BlockState newState, SoundEvent soundEvent) {
/* 207 */     if (!level.isClientSide()) {
/* 208 */       Item itemUsed = itemInHand.getItem();
/* 209 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, new ItemStack(Items.BUCKET)));
/* 210 */       player.awardStat(Stats.FILL_CAULDRON);
/* 211 */       player.awardStat(Stats.ITEM_USED.get(itemUsed));
/*     */       
/* 213 */       level.setBlockAndUpdate(pos, newState);
/* 214 */       level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 215 */       level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
/*     */     } 
/* 217 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/* 221 */   private static InteractionResult fillWaterInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) { return emptyBucket(level, pos, player, hand, itemInHand, (BlockState)Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, Integer.valueOf(3)), SoundEvents.BUCKET_EMPTY); }
/*     */ 
/*     */ 
/*     */   
/* 225 */   private static InteractionResult fillLavaInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) { return isUnderWater(level, pos) ? InteractionResult.CONSUME : emptyBucket(level, pos, player, hand, itemInHand, Blocks.LAVA_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   private static InteractionResult fillPowderSnowInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) { return isUnderWater(level, pos) ? InteractionResult.CONSUME : emptyBucket(level, pos, player, hand, itemInHand, (BlockState)Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, Integer.valueOf(3)), SoundEvents.BUCKET_EMPTY_POWDER_SNOW); }
/*     */ 
/*     */   
/*     */   private static InteractionResult shulkerBoxInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 233 */     Block block = Block.byItem(itemInHand.getItem());
/* 234 */     if (!(block instanceof net.minecraft.world.level.block.ShulkerBoxBlock)) {
/* 235 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 238 */     if (!level.isClientSide()) {
/* 239 */       ItemStack cleanedShulkerBox = itemInHand.transmuteCopy(Blocks.SHULKER_BOX, 1);
/* 240 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, cleanedShulkerBox, false));
/* 241 */       player.awardStat(Stats.CLEAN_SHULKER_BOX);
/* 242 */       LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */     } 
/*     */     
/* 245 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static InteractionResult bannerInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 249 */     BannerPatternLayers patterns = (BannerPatternLayers)itemInHand.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 250 */     if (patterns.layers().isEmpty()) {
/* 251 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 254 */     if (!level.isClientSide()) {
/* 255 */       ItemStack cleanedBanner = itemInHand.copyWithCount(1);
/* 256 */       cleanedBanner.set(DataComponents.BANNER_PATTERNS, patterns.removeLast());
/* 257 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemInHand, player, cleanedBanner, false));
/* 258 */       player.awardStat(Stats.CLEAN_BANNER);
/* 259 */       LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */     } 
/*     */     
/* 262 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static InteractionResult dyedItemIteration(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) {
/* 266 */     if (!itemInHand.is(ItemTags.DYEABLE)) {
/* 267 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 270 */     if (!itemInHand.has(DataComponents.DYED_COLOR)) {
/* 271 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 274 */     if (!level.isClientSide()) {
/* 275 */       itemInHand.remove(DataComponents.DYED_COLOR);
/* 276 */       player.awardStat(Stats.CLEAN_ARMOR);
/* 277 */       LayeredCauldronBlock.lowerFillLevel(state, level, pos);
/*     */     } 
/*     */     
/* 280 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean isUnderWater(Level level, BlockPos pos) {
/* 284 */     FluidState fluidState = level.getFluidState(pos.above());
/* 285 */     return fluidState.is(FluidTags.WATER);
/*     */   }
/*     */   
/*     */   InteractionResult interact(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\cauldron\CauldronInteraction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */