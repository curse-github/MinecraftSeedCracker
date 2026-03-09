/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class TrimPatterns {
/* 11 */   public static final ResourceKey<TrimPattern> SENTRY = registryKey("sentry");
/* 12 */   public static final ResourceKey<TrimPattern> DUNE = registryKey("dune");
/* 13 */   public static final ResourceKey<TrimPattern> COAST = registryKey("coast");
/* 14 */   public static final ResourceKey<TrimPattern> WILD = registryKey("wild");
/* 15 */   public static final ResourceKey<TrimPattern> WARD = registryKey("ward");
/* 16 */   public static final ResourceKey<TrimPattern> EYE = registryKey("eye");
/* 17 */   public static final ResourceKey<TrimPattern> VEX = registryKey("vex");
/* 18 */   public static final ResourceKey<TrimPattern> TIDE = registryKey("tide");
/* 19 */   public static final ResourceKey<TrimPattern> SNOUT = registryKey("snout");
/* 20 */   public static final ResourceKey<TrimPattern> RIB = registryKey("rib");
/* 21 */   public static final ResourceKey<TrimPattern> SPIRE = registryKey("spire");
/* 22 */   public static final ResourceKey<TrimPattern> WAYFINDER = registryKey("wayfinder");
/* 23 */   public static final ResourceKey<TrimPattern> SHAPER = registryKey("shaper");
/* 24 */   public static final ResourceKey<TrimPattern> SILENCE = registryKey("silence");
/* 25 */   public static final ResourceKey<TrimPattern> RAISER = registryKey("raiser");
/* 26 */   public static final ResourceKey<TrimPattern> HOST = registryKey("host");
/* 27 */   public static final ResourceKey<TrimPattern> FLOW = registryKey("flow");
/* 28 */   public static final ResourceKey<TrimPattern> BOLT = registryKey("bolt");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<TrimPattern> context) {
/* 31 */     register(context, SENTRY);
/* 32 */     register(context, DUNE);
/* 33 */     register(context, COAST);
/* 34 */     register(context, WILD);
/* 35 */     register(context, WARD);
/* 36 */     register(context, EYE);
/* 37 */     register(context, VEX);
/* 38 */     register(context, TIDE);
/* 39 */     register(context, SNOUT);
/* 40 */     register(context, RIB);
/* 41 */     register(context, SPIRE);
/* 42 */     register(context, WAYFINDER);
/* 43 */     register(context, SHAPER);
/* 44 */     register(context, SILENCE);
/* 45 */     register(context, RAISER);
/* 46 */     register(context, HOST);
/* 47 */     register(context, FLOW);
/* 48 */     register(context, BOLT);
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<TrimPattern> context, ResourceKey<TrimPattern> registryKey) {
/* 52 */     TrimPattern pattern = new TrimPattern(defaultAssetId(registryKey), Component.translatable(Util.makeDescriptionId("trim_pattern", registryKey.identifier())), false);
/* 53 */     context.register(registryKey, pattern);
/*    */   }
/*    */ 
/*    */   
/* 57 */   private static ResourceKey<TrimPattern> registryKey(String id) { return ResourceKey.create(Registries.TRIM_PATTERN, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static Identifier defaultAssetId(ResourceKey<TrimPattern> registryKey) { return registryKey.identifier(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\trim\TrimPatterns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */