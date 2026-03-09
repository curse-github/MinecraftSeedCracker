/*    */ package net.minecraft.server.permissions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface PermissionCheck {
/* 10 */   public static final Codec<PermissionCheck> CODEC = BuiltInRegistries.PERMISSION_CHECK_TYPE.byNameCodec().dispatch(PermissionCheck::codec, c -> c);
/*    */ 
/*    */   
/*    */   boolean check(PermissionSet paramPermissionSet);
/*    */ 
/*    */   
/*    */   MapCodec<? extends PermissionCheck> codec();
/*    */   
/*    */   public static class AlwaysPass
/*    */     implements PermissionCheck
/*    */   {
/* 21 */     public static final AlwaysPass INSTANCE = new AlwaysPass();
/*    */     
/* 23 */     public static final MapCodec<AlwaysPass> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */     
/* 27 */     public boolean check(PermissionSet source) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     public MapCodec<AlwaysPass> codec() { return MAP_CODEC; } }
/*    */   public static final class Require extends Record implements PermissionCheck { private final Permission permission;
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/PermissionCheck$Require;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require; }
/*    */     
/* 36 */     public Require(Permission permission) { this.permission = permission; } public Permission permission() { return this.permission; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/PermissionCheck$Require;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/PermissionCheck$Require;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 39 */     public static final MapCodec<Require> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Permission.CODEC
/* 40 */           .fieldOf("permission").forGetter(Require::permission))
/* 41 */         .apply(i, Require::new));
/*    */ 
/*    */ 
/*    */     
/* 45 */     public MapCodec<Require> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     public boolean check(PermissionSet source) { return source.hasPermission(this.permission); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\PermissionCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */