/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class EndGatewayConfiguration implements FeatureConfiguration {
/* 10 */   public static final Codec<EndGatewayConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPos.CODEC
/* 11 */         .optionalFieldOf("exit").forGetter(()), Codec.BOOL
/* 12 */         .fieldOf("exact").forGetter(()))
/* 13 */       .apply(i, EndGatewayConfiguration::new));
/*    */   
/*    */   private final Optional<BlockPos> exit;
/*    */   private final boolean exact;
/*    */   
/*    */   private EndGatewayConfiguration(Optional<BlockPos> exit, boolean exact) {
/* 19 */     this.exit = exit;
/* 20 */     this.exact = exact;
/*    */   }
/*    */ 
/*    */   
/* 24 */   public static EndGatewayConfiguration knownExit(BlockPos exit, boolean exact) { return new EndGatewayConfiguration(Optional.of(exit), exact); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static EndGatewayConfiguration delayedExitSearch() { return new EndGatewayConfiguration(Optional.empty(), false); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public Optional<BlockPos> getExit() { return this.exit; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean isExitExact() { return this.exact; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\EndGatewayConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */