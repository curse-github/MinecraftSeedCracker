/*    */ package net.minecraft.server.permissions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface Permission {
/* 12 */   public static final Codec<Permission> FULL_CODEC = BuiltInRegistries.PERMISSION_TYPE.byNameCodec().dispatch(Permission::codec, c -> c);
/*    */   
/* 14 */   public static final Codec<Permission> CODEC = Codec.either(FULL_CODEC, Identifier.CODEC)
/*    */ 
/*    */     
/* 17 */     .xmap(e -> 
/* 18 */       (Permission)e.map((), Atom::create), permission -> {
/* 19 */         Atom atom = (Atom)permission; return (permission instanceof Atom) ? Either.right(atom.id()) : Either.left(permission);
/*    */       });
/*    */   MapCodec<? extends Permission> codec();
/*    */   
/*    */   public static final class Atom extends Record implements Permission { private final Identifier id;
/*    */     
/* 25 */     public Atom(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$Atom;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 25 */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$Atom;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$Atom;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$Atom;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 26 */     public static final MapCodec<Atom> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 27 */           .fieldOf("id").forGetter(Atom::id))
/* 28 */         .apply(i, Atom::new));
/*    */ 
/*    */ 
/*    */     
/* 32 */     public MapCodec<Atom> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */     
/* 36 */     public static Atom create(String name) { return create(Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */ 
/*    */     
/* 40 */     public static Atom create(Identifier id) { return new Atom(id); } }
/*    */   
/*    */   public static final class HasCommandLevel extends Record implements Permission { private final PermissionLevel level;
/*    */     
/* 44 */     public HasCommandLevel(PermissionLevel level) { this.level = level; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/* 44 */       //   0	8	1	o	Ljava/lang/Object; } public PermissionLevel level() { return this.level; }
/* 45 */     public static final MapCodec<HasCommandLevel> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PermissionLevel.CODEC
/* 46 */           .fieldOf("level").forGetter(HasCommandLevel::level))
/* 47 */         .apply(i, HasCommandLevel::new));
/*    */ 
/*    */ 
/*    */     
/* 51 */     public MapCodec<HasCommandLevel> codec() { return MAP_CODEC; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\Permission.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */