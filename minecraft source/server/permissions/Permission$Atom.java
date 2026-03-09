/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Atom
/*    */   extends Record
/*    */   implements Permission
/*    */ {
/*    */   private final Identifier id;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$Atom;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$Atom;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$Atom;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$Atom;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public Atom(Identifier id) { this.id = id; } public Identifier id() { return this.id; }
/* 26 */   public static final MapCodec<Atom> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 27 */         .fieldOf("id").forGetter(Atom::id))
/* 28 */       .apply(i, Atom::new));
/*    */ 
/*    */ 
/*    */   
/* 32 */   public MapCodec<Atom> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static Atom create(String name) { return create(Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public static Atom create(Identifier id) { return new Atom(id); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\Permission$Atom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */