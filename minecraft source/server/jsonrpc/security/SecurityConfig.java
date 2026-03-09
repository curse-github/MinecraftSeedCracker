/*    */ package net.minecraft.server.jsonrpc.security;
/*    */ public final class SecurityConfig extends Record {
/*    */   private final String secretKey;
/*    */   
/*  5 */   public SecurityConfig(String secretKey) { this.secretKey = secretKey; } private static final String SECRET_KEY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/security/SecurityConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/security/SecurityConfig; } public String secretKey() { return this.secretKey; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/security/SecurityConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/security/SecurityConfig; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/security/SecurityConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/security/SecurityConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/*    */   public static boolean isValid(String secretKey) {
/* 10 */     if (secretKey.isEmpty()) {
/* 11 */       return false;
/*    */     }
/*    */     
/* 14 */     return secretKey.matches("^[a-zA-Z0-9]{40}$");
/*    */   }
/*    */   
/*    */   public static String generateSecretKey() {
/* 18 */     random = new SecureRandom();
/* 19 */     StringBuilder key = new StringBuilder(40);
/*    */     
/* 21 */     for (int i = 0; i < 40; i++) {
/* 22 */       key.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length())));
/*    */     }
/*    */     
/* 25 */     return key.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\security\SecurityConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */