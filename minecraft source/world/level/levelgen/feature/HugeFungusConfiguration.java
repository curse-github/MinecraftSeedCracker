/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class HugeFungusConfiguration implements FeatureConfiguration {
/* 10 */   public static final Codec<HugeFungusConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockState.CODEC
/* 11 */         .fieldOf("valid_base_block").forGetter(()), BlockState.CODEC
/* 12 */         .fieldOf("stem_state").forGetter(()), BlockState.CODEC
/* 13 */         .fieldOf("hat_state").forGetter(()), BlockState.CODEC
/* 14 */         .fieldOf("decor_state").forGetter(()), BlockPredicate.CODEC
/* 15 */         .fieldOf("replaceable_blocks").forGetter(()), Codec.BOOL
/* 16 */         .fieldOf("planted").orElse(Boolean.valueOf(false)).forGetter(()))
/* 17 */       .apply(i, HugeFungusConfiguration::new));
/*    */   
/*    */   public final BlockState validBaseState;
/*    */   
/*    */   public final BlockState stemState;
/*    */   public final BlockState hatState;
/*    */   public final BlockState decorState;
/*    */   public final BlockPredicate replaceableBlocks;
/*    */   public final boolean planted;
/*    */   
/*    */   public HugeFungusConfiguration(BlockState validBaseState, BlockState stemState, BlockState hatState, BlockState decorState, BlockPredicate replaceableBlocks, boolean planted) {
/* 28 */     this.validBaseState = validBaseState;
/* 29 */     this.stemState = stemState;
/* 30 */     this.hatState = hatState;
/* 31 */     this.decorState = decorState;
/* 32 */     this.replaceableBlocks = replaceableBlocks;
/* 33 */     this.planted = planted;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */