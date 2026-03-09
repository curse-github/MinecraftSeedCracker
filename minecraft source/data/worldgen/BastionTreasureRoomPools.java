/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ 
/*     */ public class BastionTreasureRoomPools {
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  14 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  15 */     Holder.Reference reference1 = processorLists.getOrThrow(ProcessorLists.TREASURE_ROOMS);
/*  16 */     Holder.Reference reference2 = processorLists.getOrThrow(ProcessorLists.HIGH_WALL);
/*  17 */     Holder.Reference reference3 = processorLists.getOrThrow(ProcessorLists.BOTTOM_RAMPART);
/*  18 */     Holder.Reference reference4 = processorLists.getOrThrow(ProcessorLists.HIGH_RAMPART);
/*  19 */     Holder.Reference reference5 = processorLists.getOrThrow(ProcessorLists.ROOF);
/*     */     
/*  21 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  22 */     Holder.Reference reference6 = pools.getOrThrow(Pools.EMPTY);
/*     */     
/*  24 */     Pools.register(context, "bastion/treasure/bases", new StructureTemplatePool(reference6, 
/*     */           
/*  26 */           ImmutableList.of(
/*  27 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/lava_basin", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     Pools.register(context, "bastion/treasure/stairs", new StructureTemplatePool(reference6, 
/*     */           
/*  34 */           ImmutableList.of(
/*  35 */             Pair.of(StructurePoolElement.single("bastion/treasure/stairs/lower_stairs", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     Pools.register(context, "bastion/treasure/bases/centers", new StructureTemplatePool(reference6, 
/*     */           
/*  42 */           ImmutableList.of(
/*  43 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_0", reference1), Integer.valueOf(1)), 
/*  44 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_1", reference1), Integer.valueOf(1)), 
/*  45 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_2", reference1), Integer.valueOf(1)), 
/*  46 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     Pools.register(context, "bastion/treasure/brains", new StructureTemplatePool(reference6, 
/*     */           
/*  53 */           ImmutableList.of(
/*  54 */             Pair.of(StructurePoolElement.single("bastion/treasure/brains/center_brain", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Pools.register(context, "bastion/treasure/walls", new StructureTemplatePool(reference6, 
/*     */           
/*  61 */           ImmutableList.of(
/*  62 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/lava_wall", reference1), Integer.valueOf(1)), 
/*  63 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/entrance_wall", reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     Pools.register(context, "bastion/treasure/walls/outer", new StructureTemplatePool(reference6, 
/*     */           
/*  70 */           ImmutableList.of(
/*  71 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/top_corner", reference2), Integer.valueOf(1)), 
/*  72 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/mid_corner", reference2), Integer.valueOf(1)), 
/*  73 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/bottom_corner", reference2), Integer.valueOf(1)), 
/*  74 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/outer_wall", reference2), Integer.valueOf(1)), 
/*  75 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/medium_outer_wall", reference2), Integer.valueOf(1)), 
/*  76 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/tall_outer_wall", reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     Pools.register(context, "bastion/treasure/walls/bottom", new StructureTemplatePool(reference6, 
/*     */           
/*  83 */           ImmutableList.of(
/*  84 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_0", reference1), Integer.valueOf(1)), 
/*  85 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_1", reference1), Integer.valueOf(1)), 
/*  86 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_2", reference1), Integer.valueOf(1)), 
/*  87 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     Pools.register(context, "bastion/treasure/walls/mid", new StructureTemplatePool(reference6, 
/*     */           
/*  94 */           ImmutableList.of(
/*  95 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_0", reference1), Integer.valueOf(1)), 
/*  96 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_1", reference1), Integer.valueOf(1)), 
/*  97 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_2", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     Pools.register(context, "bastion/treasure/walls/top", new StructureTemplatePool(reference6, 
/*     */           
/* 104 */           ImmutableList.of(
/* 105 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/main_entrance", reference1), Integer.valueOf(1)), 
/* 106 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/wall_0", reference1), Integer.valueOf(1)), 
/* 107 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/wall_1", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     Pools.register(context, "bastion/treasure/connectors", new StructureTemplatePool(reference6, 
/*     */           
/* 114 */           ImmutableList.of(
/* 115 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_middle", reference1), Integer.valueOf(1)), 
/* 116 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_top", reference1), Integer.valueOf(1)), 
/* 117 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_top_entrance", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     Pools.register(context, "bastion/treasure/entrances", new StructureTemplatePool(reference6, 
/*     */           
/* 124 */           ImmutableList.of(
/* 125 */             Pair.of(StructurePoolElement.single("bastion/treasure/entrances/entrance_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     Pools.register(context, "bastion/treasure/ramparts", new StructureTemplatePool(reference6, 
/*     */           
/* 132 */           ImmutableList.of(
/* 133 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/mid_wall_main", reference1), Integer.valueOf(1)), 
/* 134 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/mid_wall_side", reference1), Integer.valueOf(1)), 
/* 135 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/bottom_wall_0", reference3), Integer.valueOf(1)), 
/* 136 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/top_wall", reference4), Integer.valueOf(1)), 
/* 137 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/lava_basin_side", reference1), Integer.valueOf(1)), 
/* 138 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/lava_basin_main", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     Pools.register(context, "bastion/treasure/corners/bottom", new StructureTemplatePool(reference6, 
/*     */           
/* 145 */           ImmutableList.of(
/* 146 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/bottom/corner_0", reference1), Integer.valueOf(1)), 
/* 147 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/bottom/corner_1", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     Pools.register(context, "bastion/treasure/corners/edges", new StructureTemplatePool(reference6, 
/*     */           
/* 154 */           ImmutableList.of(
/* 155 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/bottom", reference2), Integer.valueOf(1)), 
/* 156 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/middle", reference2), Integer.valueOf(1)), 
/* 157 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/top", reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     Pools.register(context, "bastion/treasure/corners/middle", new StructureTemplatePool(reference6, 
/*     */           
/* 164 */           ImmutableList.of(
/* 165 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/middle/corner_0", reference1), Integer.valueOf(1)), 
/* 166 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/middle/corner_1", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     Pools.register(context, "bastion/treasure/corners/top", new StructureTemplatePool(reference6, 
/*     */           
/* 173 */           ImmutableList.of(
/* 174 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/top/corner_0", reference1), Integer.valueOf(1)), 
/* 175 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/top/corner_1", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     Pools.register(context, "bastion/treasure/extensions/large_pool", new StructureTemplatePool(reference6, 
/*     */           
/* 182 */           ImmutableList.of(
/* 183 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", reference1), Integer.valueOf(1)), 
/* 184 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", reference1), Integer.valueOf(1)), 
/* 185 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/fire_room", reference1), Integer.valueOf(1)), 
/* 186 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_0", reference1), Integer.valueOf(1)), 
/* 187 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_1", reference1), Integer.valueOf(1)), 
/* 188 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_2", reference1), Integer.valueOf(1)), 
/* 189 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_3", reference1), Integer.valueOf(1)), 
/* 190 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/roofed_bridge", reference1), Integer.valueOf(1)), 
/* 191 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     Pools.register(context, "bastion/treasure/extensions/small_pool", new StructureTemplatePool(reference6, 
/*     */           
/* 198 */           ImmutableList.of(
/* 199 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", reference1), Integer.valueOf(1)), 
/* 200 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/fire_room", reference1), Integer.valueOf(1)), 
/* 201 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", reference1), Integer.valueOf(1)), 
/* 202 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_0", reference1), Integer.valueOf(1)), 
/* 203 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_1", reference1), Integer.valueOf(1)), 
/* 204 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_2", reference1), Integer.valueOf(1)), 
/* 205 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     Pools.register(context, "bastion/treasure/extensions/houses", new StructureTemplatePool(reference6, 
/*     */           
/* 212 */           ImmutableList.of(
/* 213 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/house_0", reference1), Integer.valueOf(1)), 
/* 214 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/house_1", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     Pools.register(context, "bastion/treasure/roofs", new StructureTemplatePool(reference6, 
/*     */           
/* 221 */           ImmutableList.of(
/* 222 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/wall_roof", reference5), Integer.valueOf(1)), 
/* 223 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/corner_roof", reference5), Integer.valueOf(1)), 
/* 224 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/center_roof", reference5), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\BastionTreasureRoomPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */