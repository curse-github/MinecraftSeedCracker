/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class NetherForestVegetationConfig extends BlockPileConfiguration {
/*  9 */   public static final Codec<NetherForestVegetationConfig> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/* 10 */         .fieldOf("state_provider").forGetter(()), ExtraCodecs.POSITIVE_INT
/* 11 */         .fieldOf("spread_width").forGetter(()), ExtraCodecs.POSITIVE_INT
/* 12 */         .fieldOf("spread_height").forGetter(()))
/* 13 */       .apply(i, NetherForestVegetationConfig::new));
/*    */   
/*    */   public final int spreadWidth;
/*    */   public final int spreadHeight;
/*    */   
/*    */   public NetherForestVegetationConfig(BlockStateProvider stateProvider, int spreadWidth, int spreadHeight) {
/* 19 */     super(stateProvider);
/* 20 */     this.spreadWidth = spreadWidth;
/* 21 */     this.spreadHeight = spreadHeight;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\NetherForestVegetationConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */