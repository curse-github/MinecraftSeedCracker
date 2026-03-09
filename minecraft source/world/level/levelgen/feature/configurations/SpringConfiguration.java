/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class SpringConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<SpringConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(FluidState.CODEC
/* 13 */         .fieldOf("state").forGetter(()), Codec.BOOL
/* 14 */         .fieldOf("requires_block_below").orElse(Boolean.valueOf(true)).forGetter(()), Codec.INT
/* 15 */         .fieldOf("rock_count").orElse(Integer.valueOf(4)).forGetter(()), Codec.INT
/* 16 */         .fieldOf("hole_count").orElse(Integer.valueOf(1)).forGetter(()), 
/* 17 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("valid_blocks").forGetter(()))
/* 18 */       .apply(i, SpringConfiguration::new));
/*    */   
/*    */   public final FluidState state;
/*    */   public final boolean requiresBlockBelow;
/*    */   public final int rockCount;
/*    */   public final int holeCount;
/*    */   public final HolderSet<Block> validBlocks;
/*    */   
/*    */   public SpringConfiguration(FluidState state, boolean requiresBlockBelow, int rockCount, int holeCount, HolderSet<Block> validBlocks) {
/* 27 */     this.state = state;
/* 28 */     this.requiresBlockBelow = requiresBlockBelow;
/* 29 */     this.rockCount = rockCount;
/* 30 */     this.holeCount = holeCount;
/* 31 */     this.validBlocks = validBlocks;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpringConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */