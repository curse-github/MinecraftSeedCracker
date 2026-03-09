/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.VillagePlacements;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ 
/*     */ public class TaigaVillagePools {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/taiga/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/taiga/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  20 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  21 */     Holder.Reference reference1 = placedFeatures.getOrThrow(VillagePlacements.SPRUCE_VILLAGE);
/*  22 */     Holder.Reference reference2 = placedFeatures.getOrThrow(VillagePlacements.PINE_VILLAGE);
/*  23 */     Holder.Reference reference3 = placedFeatures.getOrThrow(VillagePlacements.PILE_PUMPKIN_VILLAGE);
/*  24 */     Holder.Reference reference4 = placedFeatures.getOrThrow(VillagePlacements.PATCH_TAIGA_GRASS_VILLAGE);
/*  25 */     Holder.Reference reference5 = placedFeatures.getOrThrow(VillagePlacements.PATCH_BERRY_BUSH_VILLAGE);
/*     */     
/*  27 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  28 */     Holder.Reference reference6 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_10_PERCENT);
/*  29 */     Holder.Reference reference7 = processorLists.getOrThrow(ProcessorLists.ZOMBIE_TAIGA);
/*  30 */     Holder.Reference reference8 = processorLists.getOrThrow(ProcessorLists.STREET_SNOWY_OR_TAIGA);
/*  31 */     Holder.Reference reference9 = processorLists.getOrThrow(ProcessorLists.FARM_TAIGA);
/*     */     
/*  33 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  34 */     Holder.Reference reference10 = pools.getOrThrow(Pools.EMPTY);
/*  35 */     Holder.Reference reference11 = pools.getOrThrow(TERMINATORS_KEY);
/*     */     
/*  37 */     context.register(START, new StructureTemplatePool(reference10, 
/*     */           
/*  39 */           ImmutableList.of(
/*  40 */             Pair.of(StructurePoolElement.legacy("village/taiga/town_centers/taiga_meeting_point_1", reference6), Integer.valueOf(49)), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/taiga/town_centers/taiga_meeting_point_2", reference6), Integer.valueOf(49)), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_1", reference7), Integer.valueOf(1)), 
/*  43 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_2", reference7), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     Pools.register(context, "village/taiga/streets", new StructureTemplatePool(reference11, 
/*     */           
/*  50 */           ImmutableList.of(
/*  51 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_01", reference8), Integer.valueOf(2)), 
/*  52 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_02", reference8), Integer.valueOf(2)), 
/*  53 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_03", reference8), Integer.valueOf(2)), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_01", reference8), Integer.valueOf(4)), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_02", reference8), Integer.valueOf(4)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_03", reference8), Integer.valueOf(4)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_04", reference8), Integer.valueOf(7)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_05", reference8), Integer.valueOf(7)), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_06", reference8), Integer.valueOf(4)), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_01", reference8), Integer.valueOf(1)), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_02", reference8), Integer.valueOf(1)), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_03", reference8), Integer.valueOf(2)), new Pair[] {
/*  63 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_04", reference8), Integer.valueOf(2)), 
/*  64 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_05", reference8), Integer.valueOf(2)), 
/*  65 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_06", reference8), Integer.valueOf(2)), 
/*  66 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/turn_01", reference8), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  71 */     Pools.register(context, "village/taiga/zombie/streets", new StructureTemplatePool(reference11, 
/*     */           
/*  73 */           ImmutableList.of(
/*  74 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_01", reference8), Integer.valueOf(2)), 
/*  75 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_02", reference8), Integer.valueOf(2)), 
/*  76 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_03", reference8), Integer.valueOf(2)), 
/*  77 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_01", reference8), Integer.valueOf(4)), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_02", reference8), Integer.valueOf(4)), 
/*  79 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_03", reference8), Integer.valueOf(4)), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_04", reference8), Integer.valueOf(7)), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_05", reference8), Integer.valueOf(7)), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_06", reference8), Integer.valueOf(4)), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_01", reference8), Integer.valueOf(1)), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_02", reference8), Integer.valueOf(1)), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_03", reference8), Integer.valueOf(2)), new Pair[] {
/*  86 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_04", reference8), Integer.valueOf(2)), 
/*  87 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_05", reference8), Integer.valueOf(2)), 
/*  88 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_06", reference8), Integer.valueOf(2)), 
/*  89 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/turn_01", reference8), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  94 */     Pools.register(context, "village/taiga/houses", new StructureTemplatePool(reference11, 
/*     */           
/*  96 */           ImmutableList.of(
/*  97 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_1", reference6), Integer.valueOf(4)), 
/*  98 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_2", reference6), Integer.valueOf(4)), 
/*  99 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_3", reference6), Integer.valueOf(4)), 
/* 100 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_4", reference6), Integer.valueOf(4)), 
/* 101 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_5", reference6), Integer.valueOf(4)), 
/* 102 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_1", reference6), Integer.valueOf(2)), 
/* 103 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_2", reference6), Integer.valueOf(2)), 
/* 104 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_3", reference6), Integer.valueOf(2)), 
/* 105 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_4", reference6), Integer.valueOf(2)), 
/* 106 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_butcher_shop_1", reference6), Integer.valueOf(2)), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tool_smith_1", reference6), Integer.valueOf(2)), 
/* 108 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fletcher_house_1", reference6), Integer.valueOf(2)), new Pair[] { 
/* 109 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_shepherds_house_1", reference6), Integer.valueOf(2)), 
/* 110 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_house_1", reference6), Integer.valueOf(1)), 
/* 111 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_2", reference6), Integer.valueOf(1)), 
/* 112 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fisher_cottage_1", reference6), Integer.valueOf(3)), 
/* 113 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tannery_1", reference6), Integer.valueOf(2)), 
/* 114 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_cartographer_house_1", reference6), Integer.valueOf(2)), 
/* 115 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_library_1", reference6), Integer.valueOf(2)), 
/* 116 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_masons_house_1", reference6), Integer.valueOf(2)), 
/* 117 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_1", reference6), Integer.valueOf(2)), 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_2", reference6), Integer.valueOf(2)), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_temple_1", reference6), Integer.valueOf(2)), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_1", reference9), Integer.valueOf(6)), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_2", reference9), Integer.valueOf(6)), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_farm_1", reference6), Integer.valueOf(1)), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_animal_pen_1", reference6), Integer.valueOf(2)), 
/* 124 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     Pools.register(context, "village/taiga/zombie/houses", new StructureTemplatePool(reference11, 
/*     */           
/* 131 */           ImmutableList.of(
/* 132 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_1", reference7), Integer.valueOf(4)), 
/* 133 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_2", reference7), Integer.valueOf(4)), 
/* 134 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_3", reference7), Integer.valueOf(4)), 
/* 135 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_4", reference7), Integer.valueOf(4)), 
/* 136 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_5", reference7), Integer.valueOf(4)), 
/* 137 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_1", reference7), Integer.valueOf(2)), 
/* 138 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_2", reference7), Integer.valueOf(2)), 
/* 139 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_3", reference7), Integer.valueOf(2)), 
/* 140 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_4", reference7), Integer.valueOf(2)), 
/* 141 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_butcher_shop_1", reference7), Integer.valueOf(2)), 
/* 142 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_tool_smith_1", reference7), Integer.valueOf(2)), 
/* 143 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fletcher_house_1", reference7), Integer.valueOf(2)), new Pair[] { 
/* 144 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_shepherds_house_1", reference7), Integer.valueOf(2)), 
/* 145 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_house_1", reference7), Integer.valueOf(1)), 
/* 146 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_fisher_cottage_1", reference7), Integer.valueOf(2)), 
/* 147 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tannery_1", reference7), Integer.valueOf(2)), 
/* 148 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_cartographer_house_1", reference7), Integer.valueOf(2)), 
/* 149 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_library_1", reference7), Integer.valueOf(2)), 
/* 150 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_masons_house_1", reference7), Integer.valueOf(2)), 
/* 151 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_1", reference7), Integer.valueOf(2)), 
/* 152 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_weaponsmith_2", reference7), Integer.valueOf(2)), 
/* 153 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_temple_1", reference7), Integer.valueOf(2)), 
/* 154 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_1", reference7), Integer.valueOf(6)), 
/* 155 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_large_farm_2", reference7), Integer.valueOf(6)), 
/* 156 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_farm_1", reference7), Integer.valueOf(1)), 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_animal_pen_1", reference7), Integer.valueOf(2)), 
/* 158 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     context.register(TERMINATORS_KEY, new StructureTemplatePool(reference10, 
/*     */           
/* 165 */           ImmutableList.of(
/* 166 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", reference8), Integer.valueOf(1)), 
/* 167 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", reference8), Integer.valueOf(1)), 
/* 168 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", reference8), Integer.valueOf(1)), 
/* 169 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", reference8), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     Pools.register(context, "village/taiga/decor", new StructureTemplatePool(reference10, 
/*     */           
/* 176 */           ImmutableList.of(
/* 177 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_lamp_post_1"), Integer.valueOf(10)), 
/* 178 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_1"), Integer.valueOf(4)), 
/* 179 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_2"), Integer.valueOf(1)), 
/* 180 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_3"), Integer.valueOf(1)), 
/* 181 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_4"), Integer.valueOf(1)), 
/* 182 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_5"), Integer.valueOf(2)), 
/* 183 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_6"), Integer.valueOf(1)), 
/* 184 */             Pair.of(StructurePoolElement.feature(reference1), Integer.valueOf(4)), 
/* 185 */             Pair.of(StructurePoolElement.feature(reference2), Integer.valueOf(4)), 
/* 186 */             Pair.of(StructurePoolElement.feature(reference3), Integer.valueOf(2)), 
/* 187 */             Pair.of(StructurePoolElement.feature(reference4), Integer.valueOf(4)), 
/* 188 */             Pair.of(StructurePoolElement.feature(reference5), Integer.valueOf(1)), new Pair[] {
/* 189 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(4))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/* 194 */     Pools.register(context, "village/taiga/zombie/decor", new StructureTemplatePool(reference10, 
/*     */           
/* 196 */           ImmutableList.of(
/* 197 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_1"), Integer.valueOf(4)), 
/* 198 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_2"), Integer.valueOf(1)), 
/* 199 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_3"), Integer.valueOf(1)), 
/* 200 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_4"), Integer.valueOf(1)), 
/* 201 */             Pair.of(StructurePoolElement.feature(reference1), Integer.valueOf(4)), 
/* 202 */             Pair.of(StructurePoolElement.feature(reference2), Integer.valueOf(4)), 
/* 203 */             Pair.of(StructurePoolElement.feature(reference3), Integer.valueOf(2)), 
/* 204 */             Pair.of(StructurePoolElement.feature(reference4), Integer.valueOf(4)), 
/* 205 */             Pair.of(StructurePoolElement.feature(reference5), Integer.valueOf(1)), 
/* 206 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(4))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     Pools.register(context, "village/taiga/villagers", new StructureTemplatePool(reference10, 
/*     */           
/* 213 */           ImmutableList.of(
/* 214 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/nitwit"), Integer.valueOf(1)), 
/* 215 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/baby"), Integer.valueOf(1)), 
/* 216 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     Pools.register(context, "village/taiga/zombie/villagers", new StructureTemplatePool(reference10, 
/*     */           
/* 223 */           ImmutableList.of(
/* 224 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 225 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\TaigaVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */