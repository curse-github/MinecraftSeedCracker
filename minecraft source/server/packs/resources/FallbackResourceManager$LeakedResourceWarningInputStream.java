/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LeakedResourceWarningInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final Supplier<String> message;
/*     */   private boolean closed;
/*     */   
/*     */   public LeakedResourceWarningInputStream(InputStream wrapped, Identifier location, String name) {
/* 105 */     super(wrapped);
/* 106 */     Exception exception = new Exception("Stacktrace");
/* 107 */     this.message = (() -> {
/* 108 */         StringWriter data = new StringWriter();
/* 109 */         exception.printStackTrace(new PrintWriter(data));
/* 110 */         return "Leaked resource: '" + String.valueOf(location) + "' loaded from pack: '" + name + "'\n" + String.valueOf(data);
/*     */       });
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     super.close();
/* 117 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws IOException {
/* 122 */     if (!this.closed) {
/* 123 */       FallbackResourceManager.LOGGER.warn("{}", this.message.get());
/*     */     }
/*     */     
/* 126 */     super.finalize();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$LeakedResourceWarningInputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */