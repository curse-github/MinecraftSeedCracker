/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class RandomPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final ResourceKey<StructureTemplatePool> alias;
/*    */   private final WeightedList<ResourceKey<StructureTemplatePool>> targets;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias; }
/*    */   
/* 21 */   public RandomPoolAlias(ResourceKey<StructureTemplatePool> alias, WeightedList<ResourceKey<StructureTemplatePool>> targets) { this.alias = alias; this.targets = targets; } public ResourceKey<StructureTemplatePool> alias() { return this.alias; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public WeightedList<ResourceKey<StructureTemplatePool>> targets() { return this.targets; }
/* 22 */   static MapCodec<RandomPoolAlias> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 23 */         ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(RandomPoolAlias::alias), 
/* 24 */         WeightedList.nonEmptyCodec(ResourceKey.codec(Registries.TEMPLATE_POOL)).fieldOf("targets").forGetter(RandomPoolAlias::targets))
/* 25 */       .apply(i, RandomPoolAlias::new));
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) { this.targets.getRandom(random).ifPresent(target -> aliasAndTargetConsumer.accept(this.alias, target)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public Stream<ResourceKey<StructureTemplatePool>> allTargets() { return this.targets.unwrap().stream().map(Weighted::value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public MapCodec<RandomPoolAlias> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\RandomPoolAlias.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */