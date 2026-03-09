/*    */ package net.minecraft.network.protocol.status;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Base64;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.WorldVersion;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public final class ServerStatus extends Record {
/*    */   private final Component description;
/*    */   private final Optional<Players> players;
/*    */   
/* 18 */   public ServerStatus(Component description, Optional<Players> players, Optional<Version> version, Optional<Favicon> favicon, boolean enforcesSecureChat) { this.description = description; this.players = players; this.version = version; this.favicon = favicon; this.enforcesSecureChat = enforcesSecureChat; } private final Optional<Version> version; private final Optional<Favicon> favicon; private final boolean enforcesSecureChat; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public Optional<Players> players() { return this.players; } public Optional<Version> version() { return this.version; } public Optional<Favicon> favicon() { return this.favicon; } public boolean enforcesSecureChat() { return this.enforcesSecureChat; }
/* 19 */   public static final Codec<ServerStatus> CODEC = RecordCodecBuilder.create(i -> i.group(ComponentSerialization.CODEC
/* 20 */         .lenientOptionalFieldOf("description", CommonComponents.EMPTY).forGetter(ServerStatus::description), Players.CODEC
/* 21 */         .lenientOptionalFieldOf("players").forGetter(ServerStatus::players), Version.CODEC
/* 22 */         .lenientOptionalFieldOf("version").forGetter(ServerStatus::version), Favicon.CODEC
/* 23 */         .lenientOptionalFieldOf("favicon").forGetter(ServerStatus::favicon), Codec.BOOL
/*    */         
/* 25 */         .lenientOptionalFieldOf("enforcesSecureChat", Boolean.valueOf(false)).forGetter(ServerStatus::enforcesSecureChat))
/* 26 */       .apply(i, ServerStatus::new));
/*    */   public static final class Players extends Record { private final int max; private final int online; private final List<NameAndId> sample;
/* 28 */     public Players(int max, int online, List<NameAndId> sample) { this.max = max; this.online = online; this.sample = sample; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public int max() { return this.max; } public int online() { return this.online; } public List<NameAndId> sample() { return this.sample; }
/* 29 */     public static final Codec<Players> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 30 */           .fieldOf("max").forGetter(Players::max), Codec.INT
/* 31 */           .fieldOf("online").forGetter(Players::online), NameAndId.CODEC
/* 32 */           .listOf().lenientOptionalFieldOf("sample", List.of()).forGetter(Players::sample))
/* 33 */         .apply(i, Players::new)); }
/*    */   public static final class Version extends Record { private final String name; private final int protocol;
/*    */     
/* 36 */     public Version(String name, int protocol) { this.name = name; this.protocol = protocol; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/* 36 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public int protocol() { return this.protocol; }
/* 37 */     public static final Codec<Version> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 38 */           .fieldOf("name").forGetter(Version::name), Codec.INT
/* 39 */           .fieldOf("protocol").forGetter(Version::protocol))
/* 40 */         .apply(i, Version::new));
/*    */     
/*    */     public static Version current() {
/* 43 */       version = SharedConstants.getCurrentVersion();
/* 44 */       return new Version(version.name(), version.protocolVersion());
/*    */     } }
/*    */   public static final class Favicon extends Record { private final byte[] iconBytes; private static final String PREFIX = "data:image/png;base64,";
/*    */     
/* 48 */     public Favicon(byte[] iconBytes) { this.iconBytes = iconBytes; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Favicon;
/* 48 */       //   0	8	1	o	Ljava/lang/Object; } public byte[] iconBytes() { return this.iconBytes; }
/*    */ 
/*    */     
/* 51 */     public static final Codec<Favicon> CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */           
/* 53 */           if (!string.startsWith("data:image/png;base64,")) {
/* 54 */             return DataResult.error(());
/*    */           }
/*    */           
/*    */           try {
/* 58 */             String base64 = string.substring("data:image/png;base64,".length()).replaceAll("\n", "");
/* 59 */             byte[] iconBytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
/* 60 */             return DataResult.success(new Favicon(iconBytes));
/* 61 */           } catch (IllegalArgumentException e) {
/* 62 */             return DataResult.error(());
/*    */           }
/*    */         
/* 65 */         }favicon -> "data:image/png;base64," + new String(Base64.getEncoder().encode(favicon.iconBytes), StandardCharsets.UTF_8)); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\status\ServerStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */