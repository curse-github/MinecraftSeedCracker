/*    */ package net.minecraft.util.eventlog;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.nio.channels.Channels;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class JsonEventLog<T> extends Object implements Closeable {
/* 20 */   private static final Gson GSON = new Gson();
/*    */   
/*    */   private final Codec<T> codec;
/*    */   
/*    */   public JsonEventLog(Codec<T> codec, FileChannel channel) {
/* 25 */     this.referenceCount = new AtomicInteger(1);
/*    */ 
/*    */     
/* 28 */     this.codec = codec;
/* 29 */     this.channel = channel;
/*    */   }
/*    */   private final FileChannel channel; private final AtomicInteger referenceCount;
/*    */   public static <T> JsonEventLog<T> open(Codec<T> codec, Path path) throws IOException {
/* 33 */     FileChannel channel = FileChannel.open(path, new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE });
/* 34 */     return new JsonEventLog(codec, channel);
/*    */   }
/*    */   
/*    */   public void write(T event) throws IOException {
/* 38 */     JsonElement json = (JsonElement)this.codec.encodeStart(JsonOps.INSTANCE, event).getOrThrow(IOException::new);
/*    */     
/* 40 */     this.channel.position(this.channel.size());
/* 41 */     Writer writer = Channels.newWriter(this.channel, StandardCharsets.UTF_8);
/* 42 */     GSON.toJson(json, GSON.newJsonWriter(writer));
/*    */     
/* 44 */     writer.write(10);
/* 45 */     writer.flush();
/*    */   }
/*    */   
/*    */   public JsonEventLogReader<T> openReader() throws IOException {
/* 49 */     if (this.referenceCount.get() <= 0) {
/* 50 */       throw new IOException("Event log has already been closed");
/*    */     }
/* 52 */     this.referenceCount.incrementAndGet();
/*    */     
/* 54 */     final JsonEventLogReader<T> reader = JsonEventLogReader.create(this.codec, Channels.newReader(this.channel, StandardCharsets.UTF_8));
/* 55 */     return new JsonEventLogReader<T>()
/*    */       {
/*    */         
/*    */         public T next() throws IOException
/*    */         {
/*    */           try {
/* 61 */             JsonEventLog.this.channel.position(this.position);
/* 62 */             object = reader.next(); return (T)object;
/*    */           } finally {
/* 64 */             this.position = JsonEventLog.this.channel.position();
/*    */           } 
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 70 */         public void close() throws IOException { JsonEventLog.this.releaseReference(); }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public void close() throws IOException { releaseReference(); }
/*    */ 
/*    */   
/*    */   private void releaseReference() throws IOException {
/* 81 */     if (this.referenceCount.decrementAndGet() <= 0)
/* 82 */       this.channel.close(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\eventlog\JsonEventLog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */