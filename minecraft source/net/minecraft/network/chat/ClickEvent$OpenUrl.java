/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.net.URI;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OpenUrl
/*    */   extends Record
/*    */   implements ClickEvent
/*    */ {
/*    */   private final URI uri;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public OpenUrl(URI uri) { this.uri = uri; } public URI uri() { return this.uri; }
/* 26 */   public static final MapCodec<OpenUrl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.UNTRUSTED_URI
/* 27 */         .fieldOf("url").forGetter(OpenUrl::uri))
/* 28 */       .apply(i, OpenUrl::new));
/*    */ 
/*    */ 
/*    */   
/* 32 */   public ClickEvent.Action action() { return ClickEvent.Action.OPEN_URL; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ClickEvent$OpenUrl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */