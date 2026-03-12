/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class PoolAliasBindings {
/*    */   public static MapCodec<? extends PoolAliasBinding> bootstrap(Registry<MapCodec<? extends PoolAliasBinding>> registry) {
/* 16 */     Registry.register(registry, "random", RandomPoolAlias.CODEC);
/* 17 */     Registry.register(registry, "random_group", RandomGroupPoolAlias.CODEC);
/* 18 */     return (MapCodec)Registry.register(registry, "direct", DirectPoolAlias.CODEC);
/*    */   }
/*    */   
/*    */   public static void registerTargetsAsPools(BootstrapContext<StructureTemplatePool> context, Holder<StructureTemplatePool> emptyPool, List<PoolAliasBinding> aliasBindings) {
/* 22 */     aliasBindings.stream()
/* 23 */       .flatMap(PoolAliasBinding::allTargets)
/* 24 */       .map(key -> key.identifier().getPath())
/* 25 */       .forEach(path -> Pools.register(context, path, new StructureTemplatePool(emptyPool, 
/*    */             
/* 27 */             List.of(Pair.of(StructurePoolElement.single(path), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasBindings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */