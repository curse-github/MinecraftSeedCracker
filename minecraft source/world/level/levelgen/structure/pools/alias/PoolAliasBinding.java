/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ 
/*    */ public interface PoolAliasBinding
/*    */ {
/* 20 */   public static final Codec<PoolAliasBinding> CODEC = BuiltInRegistries.POOL_ALIAS_BINDING_TYPE.byNameCodec().dispatch(PoolAliasBinding::codec, Function.identity());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   static DirectPoolAlias direct(String id, String target) { return direct(Pools.createKey(id), Pools.createKey(target)); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   static DirectPoolAlias direct(ResourceKey<StructureTemplatePool> alias, ResourceKey<StructureTemplatePool> target) { return new DirectPoolAlias(alias, target); }
/*    */ 
/*    */   
/*    */   static RandomPoolAlias random(String id, WeightedList<String> targets) {
/* 38 */     WeightedList.Builder<ResourceKey<StructureTemplatePool>> targetPools = WeightedList.builder();
/* 39 */     targets.unwrap().forEach(wrapper -> targetPools.add(Pools.createKey((String)wrapper.value()), wrapper.weight()));
/*    */     
/* 41 */     return random(Pools.createKey(id), targetPools.build());
/*    */   }
/*    */ 
/*    */   
/* 45 */   static RandomPoolAlias random(ResourceKey<StructureTemplatePool> id, WeightedList<ResourceKey<StructureTemplatePool>> targets) { return new RandomPoolAlias(id, targets); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   static RandomGroupPoolAlias randomGroup(WeightedList<List<PoolAliasBinding>> combinations) { return new RandomGroupPoolAlias(combinations); }
/*    */   
/*    */   void forEachResolved(RandomSource paramRandomSource, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> paramBiConsumer);
/*    */   
/*    */   Stream<ResourceKey<StructureTemplatePool>> allTargets();
/*    */   
/*    */   MapCodec<? extends PoolAliasBinding> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasBinding.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */