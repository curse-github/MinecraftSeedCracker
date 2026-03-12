/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class BlockColumnConfiguration extends Record implements FeatureConfiguration {
/*    */   private final List<Layer> layers;
/*    */   private final Direction direction;
/*    */   
/* 12 */   public BlockColumnConfiguration(List<Layer> layers, Direction direction, BlockPredicate allowedPlacement, boolean prioritizeTip) { this.layers = layers; this.direction = direction; this.allowedPlacement = allowedPlacement; this.prioritizeTip = prioritizeTip; } private final BlockPredicate allowedPlacement; private final boolean prioritizeTip; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public List<Layer> layers() { return this.layers; } public Direction direction() { return this.direction; } public BlockPredicate allowedPlacement() { return this.allowedPlacement; } public boolean prioritizeTip() { return this.prioritizeTip; }
/*    */   
/* 14 */   public static final Codec<BlockColumnConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(Layer.CODEC
/* 15 */         .listOf().fieldOf("layers").forGetter(BlockColumnConfiguration::layers), Direction.CODEC
/* 16 */         .fieldOf("direction").forGetter(BlockColumnConfiguration::direction), BlockPredicate.CODEC
/* 17 */         .fieldOf("allowed_placement").forGetter(BlockColumnConfiguration::allowedPlacement), Codec.BOOL
/* 18 */         .fieldOf("prioritize_tip").forGetter(BlockColumnConfiguration::prioritizeTip))
/* 19 */       .apply(i, BlockColumnConfiguration::new));
/*    */   public static final class Layer extends Record { private final IntProvider height; private final BlockStateProvider state;
/* 21 */     public Layer(IntProvider height, BlockStateProvider state) { this.height = height; this.state = state; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public IntProvider height() { return this.height; } public BlockStateProvider state() { return this.state; }
/* 22 */     public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(IntProvider.NON_NEGATIVE_CODEC
/* 23 */           .fieldOf("height").forGetter(Layer::height), BlockStateProvider.CODEC
/* 24 */           .fieldOf("provider").forGetter(Layer::state))
/* 25 */         .apply(i, Layer::new)); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static Layer layer(IntProvider height, BlockStateProvider state) { return new Layer(height, state); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static BlockColumnConfiguration simple(IntProvider height, BlockStateProvider state) { return new BlockColumnConfiguration(List.of(layer(height, state)), Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, false); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockColumnConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */