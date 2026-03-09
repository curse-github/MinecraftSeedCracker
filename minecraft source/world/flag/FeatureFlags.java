/*    */ package net.minecraft.world.flag;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class FeatureFlags {
/*    */   public static final FeatureFlag VANILLA;
/*    */   public static final FeatureFlag TRADE_REBALANCE;
/*    */   public static final FeatureFlag REDSTONE_EXPERIMENTS;
/*    */   public static final FeatureFlag MINECART_IMPROVEMENTS;
/*    */   public static final FeatureFlagRegistry REGISTRY;
/*    */   public static final Codec<FeatureFlagSet> CODEC;
/*    */   public static final FeatureFlagSet VANILLA_SET;
/*    */   public static final FeatureFlagSet DEFAULT_FLAGS;
/*    */   
/*    */   static  {
/* 19 */     builder = new FeatureFlagRegistry.Builder("main");
/* 20 */     VANILLA = builder.createVanilla("vanilla");
/* 21 */     TRADE_REBALANCE = builder.createVanilla("trade_rebalance");
/* 22 */     REDSTONE_EXPERIMENTS = builder.createVanilla("redstone_experiments");
/* 23 */     MINECART_IMPROVEMENTS = builder.createVanilla("minecart_improvements");
/*    */ 
/*    */     
/* 26 */     REGISTRY = builder.build();
/*    */ 
/*    */     
/* 29 */     CODEC = REGISTRY.codec();
/*    */     
/* 31 */     VANILLA_SET = FeatureFlagSet.of(VANILLA);
/* 32 */     DEFAULT_FLAGS = VANILLA_SET;
/*    */   }
/*    */   
/* 35 */   public static String printMissingFlags(FeatureFlagSet allowedFlags, FeatureFlagSet requestedFlags) { return printMissingFlags(REGISTRY, allowedFlags, requestedFlags); }
/*    */ 
/*    */   
/*    */   public static String printMissingFlags(FeatureFlagRegistry registry, FeatureFlagSet allowedFlags, FeatureFlagSet requestedFlags) {
/* 39 */     Set<Identifier> requestedFlagIds = registry.toNames(requestedFlags);
/* 40 */     Set<Identifier> allowedFlagsIds = registry.toNames(allowedFlags);
/* 41 */     return (String)requestedFlagIds.stream().filter(f -> !allowedFlagsIds.contains(f)).map(Identifier::toString).collect(Collectors.joining(", "));
/*    */   }
/*    */ 
/*    */   
/* 45 */   public static boolean isExperimental(FeatureFlagSet features) { return !features.isSubsetOf(VANILLA_SET); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\flag\FeatureFlags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */