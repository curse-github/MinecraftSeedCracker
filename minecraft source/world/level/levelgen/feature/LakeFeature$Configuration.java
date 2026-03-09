/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class Configuration extends Record implements FeatureConfiguration {
/*    */   private final BlockStateProvider fluid;
/*    */   private final BlockStateProvider barrier;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 19 */   public Configuration(BlockStateProvider fluid, BlockStateProvider barrier) { this.fluid = fluid; this.barrier = barrier; } public BlockStateProvider fluid() { return this.fluid; } public BlockStateProvider barrier() { return this.barrier; }
/* 20 */   public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/* 21 */         .fieldOf("fluid").forGetter(Configuration::fluid), BlockStateProvider.CODEC
/* 22 */         .fieldOf("barrier").forGetter(Configuration::barrier))
/* 23 */       .apply(i, Configuration::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\LakeFeature$Configuration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */