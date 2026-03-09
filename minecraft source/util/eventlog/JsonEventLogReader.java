/*    */ package net.minecraft.util.eventlog;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.Strictness;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.Closeable;
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface JsonEventLogReader<T>
/*    */   extends Closeable {
/*    */   static <T> JsonEventLogReader<T> create(final Codec<T> codec, Reader reader) {
/* 19 */     final JsonReader jsonReader = new JsonReader(reader);
/* 20 */     jsonReader.setStrictness(Strictness.LENIENT);
/* 21 */     return new JsonEventLogReader<T>()
/*    */       {
/*    */         public T next() throws IOException {
/*    */           try {
/* 25 */             if (!jsonReader.hasNext()) {
/* 26 */               return null;
/*    */             }
/* 28 */             JsonElement json = JsonParser.parseReader(jsonReader);
/* 29 */             return (T)codec.parse(JsonOps.INSTANCE, json).getOrThrow(IOException::new);
/* 30 */           } catch (JsonParseException e) {
/* 31 */             throw new IOException(e);
/* 32 */           } catch (EOFException e) {
/*    */             
/* 34 */             return null;
/*    */           } 
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 40 */         public void close() throws IOException { jsonReader.close(); }
/*    */       };
/*    */   }
/*    */   
/*    */   T next() throws IOException;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\eventlog\JsonEventLogReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */