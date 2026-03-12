/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public static enum RegistryLayer
/*    */ {
/* 10 */   STATIC,
/* 11 */   WORLDGEN,
/* 12 */   DIMENSIONS,
/* 13 */   RELOADABLE; private static final List<RegistryLayer> VALUES;
/*    */   
/*    */   static  {
/* 16 */     VALUES = List.of(values());
/*    */     
/* 18 */     STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */   }
/*    */   private static final RegistryAccess.Frozen STATIC_ACCESS;
/* 21 */   public static LayeredRegistryAccess<RegistryLayer> createRegistryAccess() { return (new LayeredRegistryAccess(VALUES)).replaceFrom(STATIC, new RegistryAccess.Frozen[] { STATIC_ACCESS }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\RegistryLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */