/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UntrustedEntry
/*    */   extends Record
/*    */ {
/*    */   private final Either<ServerLinks.KnownLinkType, Component> type;
/*    */   private final String link;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/ServerLinks$UntrustedEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks$UntrustedEntry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ServerLinks$UntrustedEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks$UntrustedEntry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/ServerLinks$UntrustedEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/ServerLinks$UntrustedEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 38 */   public UntrustedEntry(Either<ServerLinks.KnownLinkType, Component> type, String link) { this.type = type; this.link = link; } public Either<ServerLinks.KnownLinkType, Component> type() { return this.type; } public String link() { return this.link; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final StreamCodec<ByteBuf, UntrustedEntry> STREAM_CODEC = StreamCodec.composite(ServerLinks.TYPE_STREAM_CODEC, UntrustedEntry::type, ByteBufCodecs.STRING_UTF8, UntrustedEntry::link, UntrustedEntry::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerLinks$UntrustedEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */