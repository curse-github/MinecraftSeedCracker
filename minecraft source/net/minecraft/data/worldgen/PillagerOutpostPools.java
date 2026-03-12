/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ 
/*    */ public class PillagerOutpostPools {
/* 14 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("pillager_outpost/base_plates");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/* 17 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/* 18 */     Holder.Reference reference1 = processorLists.getOrThrow(ProcessorLists.OUTPOST_ROT);
/*    */     
/* 20 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/* 21 */     Holder.Reference reference2 = pools.getOrThrow(Pools.EMPTY);
/*    */     
/* 23 */     context.register(START, new StructureTemplatePool(reference2, 
/*    */           
/* 25 */           ImmutableList.of(
/* 26 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/base_plate"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     Pools.register(context, "pillager_outpost/towers", new StructureTemplatePool(reference2, 
/*    */           
/* 33 */           ImmutableList.of(
/* 34 */             Pair.of(StructurePoolElement.list(ImmutableList.of(
/* 35 */                   StructurePoolElement.legacy("pillager_outpost/watchtower"), 
/* 36 */                   StructurePoolElement.legacy("pillager_outpost/watchtower_overgrown", reference1))), 
/* 37 */               Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     Pools.register(context, "pillager_outpost/feature_plates", new StructureTemplatePool(reference2, 
/*    */           
/* 44 */           ImmutableList.of(
/* 45 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_plate"), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     Pools.register(context, "pillager_outpost/features", new StructureTemplatePool(reference2, 
/*    */           
/* 52 */           ImmutableList.of(
/* 53 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage1"), Integer.valueOf(1)), 
/* 54 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage2"), Integer.valueOf(1)), 
/* 55 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage_with_allays"), Integer.valueOf(1)), 
/* 56 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_logs"), Integer.valueOf(1)), 
/* 57 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_tent1"), Integer.valueOf(1)), 
/* 58 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_tent2"), Integer.valueOf(1)), 
/* 59 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_targets"), Integer.valueOf(1)), 
/* 60 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(6))), StructureTemplatePool.Projection.RIGID));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\PillagerOutpostPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */