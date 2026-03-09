/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class Layer
/*    */   extends Record {
/*    */   private final IntProvider height;
/*    */   private final BlockStateProvider state;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 21 */   public Layer(IntProvider height, BlockStateProvider state) { this.height = height; this.state = state; } public IntProvider height() { return this.height; } public BlockStateProvider state() { return this.state; }
/* 22 */   public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(IntProvider.NON_NEGATIVE_CODEC
/* 23 */         .fieldOf("height").forGetter(Layer::height), BlockStateProvider.CODEC
/* 24 */         .fieldOf("provider").forGetter(Layer::state))
/* 25 */       .apply(i, Layer::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockColumnConfiguration$Layer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */