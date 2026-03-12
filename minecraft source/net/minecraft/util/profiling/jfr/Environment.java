/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public static enum Environment {
/*  6 */   CLIENT("client"), SERVER("server");
/*    */   
/*    */   private final String description;
/*    */ 
/*    */   
/* 11 */   Environment(String description) { this.description = description; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static Environment from(MinecraftServer server) { return server.isDedicatedServer() ? SERVER : CLIENT; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String getDescription() { return this.description; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\Environment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */