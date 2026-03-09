/*    */ package net.minecraft.core;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface ClientAsset {
/*    */   Identifier id();
/*    */   
/*    */   public static interface Texture extends ClientAsset {
/*    */     Identifier texturePath();
/*    */   }
/*    */   
/*    */   public static final class ResourceTexture extends Record implements Texture {
/*    */     private final Identifier id;
/*    */     private final Identifier texturePath;
/*    */     
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/ClientAsset$ResourceTexture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$ResourceTexture; }
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/ClientAsset$ResourceTexture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$ResourceTexture; }
/*    */     
/* 22 */     public ResourceTexture(Identifier id, Identifier texturePath) { this.id = id; this.texturePath = texturePath; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/ClientAsset$ResourceTexture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/ClientAsset$ResourceTexture;
/* 22 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } public Identifier texturePath() { return this.texturePath; }
/*    */ 
/*    */ 
/*    */     
/* 26 */     public static final Codec<ResourceTexture> CODEC = Identifier.CODEC.xmap(ResourceTexture::new, ResourceTexture::id);
/*    */     
/* 28 */     public static final MapCodec<ResourceTexture> DEFAULT_FIELD_CODEC = CODEC.fieldOf("asset_id");
/*    */     
/* 30 */     public static final StreamCodec<ByteBuf, ResourceTexture> STREAM_CODEC = Identifier.STREAM_CODEC.map(ResourceTexture::new, ResourceTexture::id);
/*    */     
/*    */     public ResourceTexture(Identifier texture) {
/* 33 */       this(texture, texture
/*    */           
/* 35 */           .withPath(path -> "textures/" + path + ".png"));
/*    */     } }
/*    */   public static final class DownloadedTexture extends Record implements Texture { private final Identifier texturePath;
/*    */     private final String url;
/*    */     
/* 40 */     public DownloadedTexture(Identifier texturePath, String url) { this.texturePath = texturePath; this.url = url; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/ClientAsset$DownloadedTexture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 40 */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$DownloadedTexture; } public Identifier texturePath() { return this.texturePath; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/ClientAsset$DownloadedTexture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$DownloadedTexture; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/ClientAsset$DownloadedTexture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/ClientAsset$DownloadedTexture;
/* 40 */       //   0	8	1	o	Ljava/lang/Object; } public String url() { return this.url; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     public Identifier id() { return this.texturePath; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\ClientAsset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */