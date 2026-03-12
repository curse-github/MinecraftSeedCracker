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
/*    */ public class BastionPieces {
/* 14 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("bastion/starts");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/* 17 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/* 18 */     Holder.Reference reference1 = processorLists.getOrThrow(ProcessorLists.BASTION_GENERIC_DEGRADATION);
/*    */     
/* 20 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/* 21 */     Holder.Reference reference2 = pools.getOrThrow(Pools.EMPTY);
/*    */     
/* 23 */     context.register(START, new StructureTemplatePool(reference2, 
/*    */           
/* 25 */           ImmutableList.of(
/* 26 */             Pair.of(StructurePoolElement.single("bastion/units/air_base", reference1), Integer.valueOf(1)), 
/* 27 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/air_base", reference1), Integer.valueOf(1)), 
/* 28 */             Pair.of(StructurePoolElement.single("bastion/treasure/big_air_full", reference1), Integer.valueOf(1)), 
/* 29 */             Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     BastionHousingUnitsPools.bootstrap(context);
/* 35 */     BastionHoglinStablePools.bootstrap(context);
/* 36 */     BastionTreasureRoomPools.bootstrap(context);
/* 37 */     BastionBridgePools.bootstrap(context);
/* 38 */     BastionSharedPools.bootstrap(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\BastionPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */