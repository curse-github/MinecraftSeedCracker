/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ 
/*     */ public class PoiTypes
/*     */ {
/*  25 */   public static final ResourceKey<PoiType> ARMORER = createKey("armorer");
/*  26 */   public static final ResourceKey<PoiType> BUTCHER = createKey("butcher");
/*  27 */   public static final ResourceKey<PoiType> CARTOGRAPHER = createKey("cartographer");
/*  28 */   public static final ResourceKey<PoiType> CLERIC = createKey("cleric");
/*  29 */   public static final ResourceKey<PoiType> FARMER = createKey("farmer");
/*  30 */   public static final ResourceKey<PoiType> FISHERMAN = createKey("fisherman");
/*  31 */   public static final ResourceKey<PoiType> FLETCHER = createKey("fletcher");
/*  32 */   public static final ResourceKey<PoiType> LEATHERWORKER = createKey("leatherworker");
/*  33 */   public static final ResourceKey<PoiType> LIBRARIAN = createKey("librarian");
/*  34 */   public static final ResourceKey<PoiType> MASON = createKey("mason");
/*  35 */   public static final ResourceKey<PoiType> SHEPHERD = createKey("shepherd");
/*  36 */   public static final ResourceKey<PoiType> TOOLSMITH = createKey("toolsmith");
/*  37 */   public static final ResourceKey<PoiType> WEAPONSMITH = createKey("weaponsmith");
/*  38 */   public static final ResourceKey<PoiType> HOME = createKey("home");
/*  39 */   public static final ResourceKey<PoiType> MEETING = createKey("meeting");
/*  40 */   public static final ResourceKey<PoiType> BEEHIVE = createKey("beehive");
/*  41 */   public static final ResourceKey<PoiType> BEE_NEST = createKey("bee_nest");
/*  42 */   public static final ResourceKey<PoiType> NETHER_PORTAL = createKey("nether_portal");
/*  43 */   public static final ResourceKey<PoiType> LODESTONE = createKey("lodestone");
/*  44 */   public static final ResourceKey<PoiType> LIGHTNING_ROD = createKey("lightning_rod");
/*  45 */   public static final ResourceKey<PoiType> TEST_INSTANCE = createKey("test_instance");
/*     */ 
/*     */   
/*  48 */   private static final Set<BlockState> BEDS = (Set)ImmutableList.of(Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, new Block[] { Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED
/*  49 */       }).stream()
/*  50 */     .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
/*  51 */     .filter(state -> (state.getValue(BedBlock.PART) == BedPart.HEAD))
/*  52 */     .collect(ImmutableSet.toImmutableSet());
/*     */ 
/*     */   
/*  55 */   private static final Set<BlockState> CAULDRONS = (Set)ImmutableList.of(Blocks.CAULDRON, Blocks.LAVA_CAULDRON, Blocks.WATER_CAULDRON, Blocks.POWDER_SNOW_CAULDRON)
/*  56 */     .stream()
/*  57 */     .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
/*  58 */     .collect(ImmutableSet.toImmutableSet());
/*     */ 
/*     */   
/*  61 */   private static final Set<BlockState> LIGHTNING_RODS = (Set)ImmutableList.of(Blocks.LIGHTNING_ROD, Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WEATHERED_LIGHTNING_ROD, Blocks.OXIDIZED_LIGHTNING_ROD, Blocks.WAXED_LIGHTNING_ROD, Blocks.WAXED_EXPOSED_LIGHTNING_ROD, Blocks.WAXED_WEATHERED_LIGHTNING_ROD, Blocks.WAXED_OXIDIZED_LIGHTNING_ROD)
/*  62 */     .stream()
/*  63 */     .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
/*  64 */     .collect(ImmutableSet.toImmutableSet());
/*     */   
/*  66 */   private static final Map<BlockState, Holder<PoiType>> TYPE_BY_STATE = Maps.newHashMap();
/*     */ 
/*     */   
/*  69 */   private static Set<BlockState> getBlockStates(Block block) { return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static ResourceKey<PoiType> createKey(String name) { return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.withDefaultNamespace(name)); }
/*     */ 
/*     */   
/*     */   private static PoiType register(Registry<PoiType> registry, ResourceKey<PoiType> id, Set<BlockState> matchingStates, int maxTickets, int validRange) {
/*  77 */     PoiType value = new PoiType(matchingStates, maxTickets, validRange);
/*  78 */     Registry.register(registry, id, value);
/*  79 */     registerBlockStates(registry.getOrThrow(id), matchingStates);
/*  80 */     return value;
/*     */   }
/*     */   
/*     */   private static void registerBlockStates(Holder<PoiType> type, Set<BlockState> matchingStates) {
/*  84 */     matchingStates.forEach(blockState -> {
/*  85 */           Holder<PoiType> previous = (Holder)TYPE_BY_STATE.put(blockState, type);
/*  86 */           if (previous != null) {
/*  87 */             throw (IllegalStateException)Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", new Object[] { blockState })));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*  93 */   public static Optional<Holder<PoiType>> forState(BlockState state) { return Optional.ofNullable((Holder)TYPE_BY_STATE.get(state)); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static boolean hasPoi(BlockState state) { return TYPE_BY_STATE.containsKey(state); }
/*     */ 
/*     */   
/*     */   public static PoiType bootstrap(Registry<PoiType> registry) {
/* 101 */     register(registry, ARMORER, getBlockStates(Blocks.BLAST_FURNACE), 1, 1);
/* 102 */     register(registry, BUTCHER, getBlockStates(Blocks.SMOKER), 1, 1);
/* 103 */     register(registry, CARTOGRAPHER, getBlockStates(Blocks.CARTOGRAPHY_TABLE), 1, 1);
/* 104 */     register(registry, CLERIC, getBlockStates(Blocks.BREWING_STAND), 1, 1);
/* 105 */     register(registry, FARMER, getBlockStates(Blocks.COMPOSTER), 1, 1);
/* 106 */     register(registry, FISHERMAN, getBlockStates(Blocks.BARREL), 1, 1);
/* 107 */     register(registry, FLETCHER, getBlockStates(Blocks.FLETCHING_TABLE), 1, 1);
/* 108 */     register(registry, LEATHERWORKER, CAULDRONS, 1, 1);
/* 109 */     register(registry, LIBRARIAN, getBlockStates(Blocks.LECTERN), 1, 1);
/* 110 */     register(registry, MASON, getBlockStates(Blocks.STONECUTTER), 1, 1);
/* 111 */     register(registry, SHEPHERD, getBlockStates(Blocks.LOOM), 1, 1);
/* 112 */     register(registry, TOOLSMITH, getBlockStates(Blocks.SMITHING_TABLE), 1, 1);
/* 113 */     register(registry, WEAPONSMITH, getBlockStates(Blocks.GRINDSTONE), 1, 1);
/* 114 */     register(registry, HOME, BEDS, 1, 1);
/* 115 */     register(registry, MEETING, getBlockStates(Blocks.BELL), 32, 6);
/* 116 */     register(registry, BEEHIVE, getBlockStates(Blocks.BEEHIVE), 0, 1);
/* 117 */     register(registry, BEE_NEST, getBlockStates(Blocks.BEE_NEST), 0, 1);
/* 118 */     register(registry, NETHER_PORTAL, getBlockStates(Blocks.NETHER_PORTAL), 0, 1);
/* 119 */     register(registry, LODESTONE, getBlockStates(Blocks.LODESTONE), 0, 1);
/* 120 */     register(registry, TEST_INSTANCE, getBlockStates(Blocks.TEST_INSTANCE_BLOCK), 0, 1);
/* 121 */     return register(registry, LIGHTNING_ROD, LIGHTNING_RODS, 0, 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */