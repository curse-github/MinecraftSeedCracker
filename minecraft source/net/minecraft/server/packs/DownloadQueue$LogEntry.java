/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.util.ExtraCodecs;
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
/*    */ final class LogEntry
/*    */   extends Record
/*    */ {
/*    */   private final UUID id;
/*    */   private final String url;
/*    */   private final Instant time;
/*    */   private final Optional<String> hash;
/*    */   private final Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 48 */   private LogEntry(UUID id, String url, Instant time, Optional<String> hash, Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo) { this.id = id; this.url = url; this.time = time; this.hash = hash; this.errorOrFileInfo = errorOrFileInfo; } public UUID id() { return this.id; } public String url() { return this.url; } public Instant time() { return this.time; } public Optional<String> hash() { return this.hash; } public Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo() { return this.errorOrFileInfo; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public static final Codec<LogEntry> CODEC = RecordCodecBuilder.create(i -> i.group(UUIDUtil.STRING_CODEC
/* 56 */         .fieldOf("id").forGetter(LogEntry::id), Codec.STRING
/* 57 */         .fieldOf("url").forGetter(LogEntry::url), ExtraCodecs.INSTANT_ISO8601
/* 58 */         .fieldOf("time").forGetter(LogEntry::time), Codec.STRING
/* 59 */         .optionalFieldOf("hash").forGetter(LogEntry::hash), 
/* 60 */         Codec.mapEither(Codec.STRING.fieldOf("error"), DownloadQueue.FileInfoEntry.CODEC.fieldOf("file")).forGetter(LogEntry::errorOrFileInfo))
/* 61 */       .apply(i, LogEntry::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\DownloadQueue$LogEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */