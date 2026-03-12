/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ 
/*    */ final class ProviderCacheBuilder
/*    */   extends Record
/*    */ {
/*    */   private final String version;
/*    */   private final ConcurrentMap<Path, HashCode> data;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 83 */   private ProviderCacheBuilder(String version, ConcurrentMap<Path, HashCode> data) { this.version = version; this.data = data; } public String version() { return this.version; } public ConcurrentMap<Path, HashCode> data() { return this.data; }
/*    */   
/* 85 */   ProviderCacheBuilder(String version) { this(version, new ConcurrentHashMap()); }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public void put(Path path, HashCode hash) { this.data.put(path, hash); }
/*    */ 
/*    */ 
/*    */   
/* 93 */   public HashCache.ProviderCache build() { return new HashCache.ProviderCache(this.version, ImmutableMap.copyOf(this.data)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\HashCache$ProviderCacheBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */