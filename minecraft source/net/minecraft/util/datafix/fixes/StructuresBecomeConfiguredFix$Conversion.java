/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ final class Conversion
/*    */   extends Record
/*    */ {
/*    */   private final Map<String, String> biomeMapping;
/*    */   private final String fallback;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 32 */   private Conversion(Map<String, String> biomeMapping, String fallback) { this.biomeMapping = biomeMapping; this.fallback = fallback; } public Map<String, String> biomeMapping() { return this.biomeMapping; } public String fallback() { return this.fallback; }
/*    */   
/* 34 */   public static Conversion trivial(String result) { return new Conversion(Map.of(), result); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static Conversion biomeMapped(Map<List<String>, String> mapping, String fallback) { return new Conversion(unpack(mapping), fallback); }
/*    */ 
/*    */   
/*    */   private static Map<String, String> unpack(Map<List<String>, String> packed) {
/* 42 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/* 43 */     for (Iterator iterator = packed.entrySet().iterator(); iterator.hasNext(); ) { Map.Entry<List<String>, String> entry = (Map.Entry)iterator.next();
/* 44 */       ((List)entry.getKey()).forEach(k -> builder.put(k, (String)entry.getValue())); }
/*    */     
/* 46 */     return builder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\StructuresBecomeConfiguredFix$Conversion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */