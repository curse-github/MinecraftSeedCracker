/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.net.URI;
/*    */ import net.minecraft.network.chat.Component;
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
/*    */ public final class Entry
/*    */   extends Record
/*    */ {
/*    */   private final Either<ServerLinks.KnownLinkType, Component> type;
/*    */   private final URI link;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/ServerLinks$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks$Entry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ServerLinks$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks$Entry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/ServerLinks$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/ServerLinks$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 49 */   public Entry(Either<ServerLinks.KnownLinkType, Component> type, URI link) { this.type = type; this.link = link; } public Either<ServerLinks.KnownLinkType, Component> type() { return this.type; } public URI link() { return this.link; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public static Entry knownType(ServerLinks.KnownLinkType type, URI link) { return new Entry(Either.left(type), link); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public static Entry custom(Component displayName, URI link) { return new Entry(Either.right(displayName), link); }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Component displayName() { return (Component)this.type.map(ServerLinks.KnownLinkType::displayName, r -> r); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerLinks$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */