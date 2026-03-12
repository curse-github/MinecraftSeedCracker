/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class RandomGroupPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final WeightedList<List<PoolAliasBinding>> groups;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias; }
/*    */   
/* 22 */   public RandomGroupPoolAlias(WeightedList<List<PoolAliasBinding>> groups) { this.groups = groups; } public WeightedList<List<PoolAliasBinding>> groups() { return this.groups; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 23 */   static MapCodec<RandomGroupPoolAlias> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 24 */         WeightedList.nonEmptyCodec(Codec.list(PoolAliasBinding.CODEC)).fieldOf("groups").forGetter(RandomGroupPoolAlias::groups))
/* 25 */       .apply(i, RandomGroupPoolAlias::new));
/*    */ 
/*    */   
/*    */   public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
/* 29 */     this.groups.getRandom(random).ifPresent(combination -> 
/* 30 */         combination.forEach(()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
/* 36 */     return this.groups.unwrap().stream()
/* 37 */       .flatMap(weightedEntry -> ((List)weightedEntry.value()).stream())
/* 38 */       .flatMap(PoolAliasBinding::allTargets);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public MapCodec<RandomGroupPoolAlias> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\RandomGroupPoolAlias.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */