/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class BlockPileConfiguration implements FeatureConfiguration {
/*  7 */   public static final Codec<BlockPileConfiguration> CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, c -> c.stateProvider).codec();
/*    */   
/*    */   public final BlockStateProvider stateProvider;
/*    */ 
/*    */   
/* 12 */   public BlockPileConfiguration(BlockStateProvider stateProvider) { this.stateProvider = stateProvider; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockPileConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */