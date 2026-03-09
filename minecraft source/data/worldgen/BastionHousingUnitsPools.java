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
/*     */ public class BastionHousingUnitsPools {
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  14 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  15 */     Holder.Reference reference1 = processorLists.getOrThrow(ProcessorLists.HOUSING);
/*     */     
/*  17 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  18 */     Holder.Reference reference2 = pools.getOrThrow(Pools.EMPTY);
/*     */     
/*  20 */     Pools.register(context, "bastion/units/center_pieces", new StructureTemplatePool(reference2, 
/*     */           
/*  22 */           ImmutableList.of(
/*  23 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_0", reference1), Integer.valueOf(1)), 
/*  24 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_1", reference1), Integer.valueOf(1)), 
/*  25 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_2", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     Pools.register(context, "bastion/units/pathways", new StructureTemplatePool(reference2, 
/*     */           
/*  32 */           ImmutableList.of(
/*  33 */             Pair.of(StructurePoolElement.single("bastion/units/pathways/pathway_0", reference1), Integer.valueOf(1)), 
/*  34 */             Pair.of(StructurePoolElement.single("bastion/units/pathways/pathway_wall_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     Pools.register(context, "bastion/units/walls/wall_bases", new StructureTemplatePool(reference2, 
/*     */           
/*  41 */           ImmutableList.of(
/*  42 */             Pair.of(StructurePoolElement.single("bastion/units/walls/wall_base", reference1), Integer.valueOf(1)), 
/*  43 */             Pair.of(StructurePoolElement.single("bastion/units/walls/connected_wall", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     Pools.register(context, "bastion/units/stages/stage_0", new StructureTemplatePool(reference2, 
/*     */           
/*  50 */           ImmutableList.of(
/*  51 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_0", reference1), Integer.valueOf(1)), 
/*  52 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_1", reference1), Integer.valueOf(1)), 
/*  53 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_2", reference1), Integer.valueOf(1)), 
/*  54 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Pools.register(context, "bastion/units/stages/stage_1", new StructureTemplatePool(reference2, 
/*     */           
/*  61 */           ImmutableList.of(
/*  62 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_0", reference1), Integer.valueOf(1)), 
/*  63 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_1", reference1), Integer.valueOf(1)), 
/*  64 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_2", reference1), Integer.valueOf(1)), 
/*  65 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     Pools.register(context, "bastion/units/stages/rot/stage_1", new StructureTemplatePool(reference2, 
/*     */           
/*  73 */           ImmutableList.of(
/*  74 */             Pair.of(StructurePoolElement.single("bastion/units/stages/rot/stage_1_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     Pools.register(context, "bastion/units/stages/stage_2", new StructureTemplatePool(reference2, 
/*     */           
/*  81 */           ImmutableList.of(
/*  82 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_2_0", reference1), Integer.valueOf(1)), 
/*  83 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_2_1", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     Pools.register(context, "bastion/units/stages/stage_3", new StructureTemplatePool(reference2, 
/*     */           
/*  90 */           ImmutableList.of(
/*  91 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_0", reference1), Integer.valueOf(1)), 
/*  92 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_1", reference1), Integer.valueOf(1)), 
/*  93 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_2", reference1), Integer.valueOf(1)), 
/*  94 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     Pools.register(context, "bastion/units/fillers/stage_0", new StructureTemplatePool(reference2, 
/*     */           
/* 101 */           ImmutableList.of(
/* 102 */             Pair.of(StructurePoolElement.single("bastion/units/fillers/stage_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     Pools.register(context, "bastion/units/edges", new StructureTemplatePool(reference2, 
/*     */           
/* 109 */           ImmutableList.of(
/* 110 */             Pair.of(StructurePoolElement.single("bastion/units/edges/edge_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     Pools.register(context, "bastion/units/wall_units", new StructureTemplatePool(reference2, 
/*     */           
/* 117 */           ImmutableList.of(
/* 118 */             Pair.of(StructurePoolElement.single("bastion/units/wall_units/unit_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     Pools.register(context, "bastion/units/edge_wall_units", new StructureTemplatePool(reference2, 
/*     */           
/* 125 */           ImmutableList.of(
/* 126 */             Pair.of(StructurePoolElement.single("bastion/units/wall_units/edge_0_large", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     Pools.register(context, "bastion/units/ramparts", new StructureTemplatePool(reference2, 
/*     */           
/* 133 */           ImmutableList.of(
/* 134 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_0", reference1), Integer.valueOf(1)), 
/* 135 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_1", reference1), Integer.valueOf(1)), 
/* 136 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_2", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     Pools.register(context, "bastion/units/large_ramparts", new StructureTemplatePool(reference2, 
/*     */           
/* 143 */           ImmutableList.of(
/* 144 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Pools.register(context, "bastion/units/rampart_plates", new StructureTemplatePool(reference2, 
/*     */           
/* 151 */           ImmutableList.of(
/* 152 */             Pair.of(StructurePoolElement.single("bastion/units/rampart_plates/plate_0", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\BastionHousingUnitsPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */