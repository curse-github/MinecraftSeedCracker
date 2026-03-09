/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PermissionTypes {
/*    */   public static MapCodec<? extends Permission> bootstrap(Registry<MapCodec<? extends Permission>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("atom"), Permission.Atom.MAP_CODEC);
/* 10 */     return (MapCodec)Registry.register(registry, Identifier.withDefaultNamespace("command_level"), Permission.HasCommandLevel.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\PermissionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */