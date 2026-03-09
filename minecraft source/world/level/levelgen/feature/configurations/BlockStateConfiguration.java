/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateConfiguration implements FeatureConfiguration {
/*  7 */   public static final Codec<BlockStateConfiguration> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, c -> c.state).codec();
/*    */   
/*    */   public final BlockState state;
/*    */ 
/*    */   
/* 12 */   public BlockStateConfiguration(BlockState state) { this.state = state; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockStateConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */