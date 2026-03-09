/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class LayerConfiguration implements FeatureConfiguration {
/*  9 */   public static final Codec<LayerConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 10 */         Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(()), BlockState.CODEC
/* 11 */         .fieldOf("state").forGetter(()))
/* 12 */       .apply(i, LayerConfiguration::new));
/*    */   
/*    */   public final int height;
/*    */   public final BlockState state;
/*    */   
/*    */   public LayerConfiguration(int height, BlockState state) {
/* 18 */     this.height = height;
/* 19 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\LayerConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */