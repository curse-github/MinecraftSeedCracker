/*    */ package net.minecraft.network.chat;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public interface FontDescription {
/*  9 */   public static final Codec<FontDescription> CODEC = Identifier.CODEC.flatComapMap(Resource::new, fontDescription -> {
/*    */ 
/*    */         
/* 12 */         if (fontDescription instanceof Resource) { Resource resource = (Resource)fontDescription;
/* 13 */           return DataResult.success(resource.id()); }
/*    */ 
/*    */         
/* 16 */         return DataResult.error(());
/*    */       });
/*    */ 
/*    */   
/* 20 */   public static final Resource DEFAULT = new Resource(Identifier.withDefaultNamespace("default")); public static final class Resource extends Record implements FontDescription { private final Identifier id; public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$Resource;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$Resource; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$Resource;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$Resource; }
/* 22 */     public Resource(Identifier id) { this.id = id; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$Resource;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$Resource;
/* 22 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } }
/*    */   public static final class AtlasSprite extends Record implements FontDescription { private final Identifier atlasId; private final Identifier spriteId;
/*    */     
/* 25 */     public AtlasSprite(Identifier atlasId, Identifier spriteId) { this.atlasId = atlasId; this.spriteId = spriteId; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$AtlasSprite; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$AtlasSprite; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$AtlasSprite;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier atlasId() { return this.atlasId; } public Identifier spriteId() { return this.spriteId; } }
/*    */   public static final class PlayerSprite extends Record implements FontDescription { private final ResolvableProfile profile; private final boolean hat;
/*    */     
/* 28 */     public PlayerSprite(ResolvableProfile profile, boolean hat) { this.profile = profile; this.hat = hat; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public ResolvableProfile profile() { return this.profile; } public boolean hat() { return this.hat; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FontDescription.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */