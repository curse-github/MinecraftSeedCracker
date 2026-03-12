/*    */ package net.minecraft.server;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.net.URI;
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public final class ServerLinks extends Record {
/*    */   private final List<Entry> entries;
/*    */   
/* 16 */   public ServerLinks(List<Entry> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/ServerLinks;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks; } public List<Entry> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ServerLinks;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/ServerLinks; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/ServerLinks;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/ServerLinks;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 17 */   public static final ServerLinks EMPTY = new ServerLinks(List.of());
/*    */   
/* 19 */   public static final StreamCodec<ByteBuf, Either<KnownLinkType, Component>> TYPE_STREAM_CODEC = ByteBufCodecs.either(KnownLinkType.STREAM_CODEC, ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final StreamCodec<ByteBuf, List<UntrustedEntry>> UNTRUSTED_LINKS_STREAM_CODEC = UntrustedEntry.STREAM_CODEC.apply(ByteBufCodecs.list());
/*    */ 
/*    */   
/* 27 */   public boolean isEmpty() { return this.entries.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Optional<Entry> findKnownType(KnownLinkType type) { return this.entries.stream().filter(e -> ((Boolean)e.type.map((), ())).booleanValue()).findFirst(); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public List<UntrustedEntry> untrust() { return this.entries.stream().map(e -> new UntrustedEntry(e.type, e.link.toString())).toList(); }
/*    */   public static final class UntrustedEntry extends Record { private final Either<ServerLinks.KnownLinkType, Component> type; private final String link;
/*    */     
/* 38 */     public UntrustedEntry(Either<ServerLinks.KnownLinkType, Component> type, String link) { this.type = type; this.link = link; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/ServerLinks$UntrustedEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/ServerLinks$UntrustedEntry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ServerLinks$UntrustedEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/ServerLinks$UntrustedEntry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/ServerLinks$UntrustedEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/ServerLinks$UntrustedEntry;
/* 38 */       //   0	8	1	o	Ljava/lang/Object; } public Either<ServerLinks.KnownLinkType, Component> type() { return this.type; } public String link() { return this.link; }
/*    */ 
/*    */ 
/*    */     
/* 42 */     public static final StreamCodec<ByteBuf, UntrustedEntry> STREAM_CODEC = StreamCodec.composite(ServerLinks.TYPE_STREAM_CODEC, UntrustedEntry::type, ByteBufCodecs.STRING_UTF8, UntrustedEntry::link, UntrustedEntry::new); }
/*    */ 
/*    */   
/*    */   public static final class Entry extends Record {
/*    */     private final Either<ServerLinks.KnownLinkType, Component> type;
/*    */     private final URI link;
/*    */     
/* 49 */     public Entry(Either<ServerLinks.KnownLinkType, Component> type, URI link) { this.type = type; this.link = link; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/ServerLinks$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/ServerLinks$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ServerLinks$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/ServerLinks$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/ServerLinks$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/ServerLinks$Entry;
/* 49 */       //   0	8	1	o	Ljava/lang/Object; } public Either<ServerLinks.KnownLinkType, Component> type() { return this.type; } public URI link() { return this.link; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     public static Entry knownType(ServerLinks.KnownLinkType type, URI link) { return new Entry(Either.left(type), link); }
/*    */ 
/*    */ 
/*    */     
/* 58 */     public static Entry custom(Component displayName, URI link) { return new Entry(Either.right(displayName), link); }
/*    */ 
/*    */ 
/*    */     
/* 62 */     public Component displayName() { return (Component)this.type.map(ServerLinks.KnownLinkType::displayName, r -> r); }
/*    */   }
/*    */   
/*    */   public enum KnownLinkType
/*    */   {
/* 67 */     BUG_REPORT(0, "report_bug"),
/* 68 */     COMMUNITY_GUIDELINES(1, "community_guidelines"),
/* 69 */     SUPPORT(2, "support"),
/* 70 */     STATUS(3, "status"),
/* 71 */     FEEDBACK(4, "feedback"),
/* 72 */     COMMUNITY(5, "community"),
/* 73 */     WEBSITE(6, "website"),
/* 74 */     FORUMS(7, "forums"),
/* 75 */     NEWS(8, "news"),
/* 76 */     ANNOUNCEMENTS(9, "announcements"); private static final IntFunction<KnownLinkType> BY_ID; public static final StreamCodec<ByteBuf, KnownLinkType> STREAM_CODEC; private final int id; private final String name;
/*    */     
/*    */     static  {
/* 79 */       BY_ID = ByIdMap.continuous(e -> e.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */       
/* 81 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     KnownLinkType(int id, String name) {
/* 87 */       this.id = id;
/* 88 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/* 92 */     private Component displayName() { return Component.translatable("known_server_link." + this.name); }
/*    */ 
/*    */ 
/*    */     
/* 96 */     public ServerLinks.Entry create(URI link) { return ServerLinks.Entry.knownType(this, link); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerLinks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */