/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PoolAliasLookup
/*    */ {
/* 15 */   public static final PoolAliasLookup EMPTY = key -> key;
/*    */ 
/*    */ 
/*    */   
/*    */   static PoolAliasLookup create(List<PoolAliasBinding> poolAliasBindings, BlockPos pos, long seed) {
/* 20 */     if (poolAliasBindings.isEmpty()) {
/* 21 */       return EMPTY;
/*    */     }
/*    */     
/* 24 */     RandomSource random = RandomSource.create(seed).forkPositional().at(pos);
/* 25 */     ImmutableMap.Builder<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> builder = ImmutableMap.builder();
/* 26 */     poolAliasBindings.forEach(binding -> { Objects.requireNonNull(builder); binding.forEachResolved(random, builder::put);
/* 27 */         }); ImmutableMap immutableMap = builder.build();
/*    */     
/* 29 */     return resourceKey -> (ResourceKey)Objects.requireNonNull((ResourceKey)aliasMappings.getOrDefault(resourceKey, resourceKey), ());
/*    */   }
/*    */   
/*    */   ResourceKey<StructureTemplatePool> lookup(ResourceKey<StructureTemplatePool> paramResourceKey);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */