/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PermissionCheckTypes {
/*    */   public static MapCodec<? extends PermissionCheck> bootstrap(Registry<MapCodec<? extends PermissionCheck>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("always_pass"), PermissionCheck.AlwaysPass.MAP_CODEC);
/* 10 */     return (MapCodec)Registry.register(registry, Identifier.withDefaultNamespace("require"), PermissionCheck.Require.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\PermissionCheckTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */