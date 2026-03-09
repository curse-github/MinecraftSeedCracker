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
/*    */ public class AncientCityStructurePieces {
/* 14 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("ancient_city/city_center");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/* 17 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/* 18 */     Holder.Reference reference1 = processorLists.getOrThrow(ProcessorLists.ANCIENT_CITY_START_DEGRADATION);
/*    */     
/* 20 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/* 21 */     Holder.Reference reference2 = pools.getOrThrow(Pools.EMPTY);
/*    */     
/* 23 */     context.register(START, new StructureTemplatePool(reference2, 
/*    */           
/* 25 */           ImmutableList.of(
/* 26 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/city_center_1", reference1), Integer.valueOf(1)), 
/* 27 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/city_center_2", reference1), Integer.valueOf(1)), 
/* 28 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/city_center_3", reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     AncientCityStructurePools.bootstrap(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\AncientCityStructurePieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */