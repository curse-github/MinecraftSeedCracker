/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Date;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
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
/*    */ final class UserBan
/*    */   extends Record
/*    */ {
/*    */   private final NameAndId player;
/*    */   private final String reason;
/*    */   private final String source;
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 49 */   private UserBan(NameAndId player, String reason, String source, Optional<Instant> expires) { this.player = player; this.reason = reason; this.source = source; this.expires = expires; } public NameAndId player() { return this.player; } public String reason() { return this.reason; } public String source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/*    */   
/* 51 */   private static UserBan from(UserBanListEntry entry) { return new UserBan((NameAndId)Objects.requireNonNull((NameAndId)entry.getUser()), entry.getReason(), entry.getSource(), Optional.ofNullable(entry.getExpires()).map(Date::toInstant)); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   private UserBanListEntry toBanEntry() { return new UserBanListEntry(new NameAndId(player().id(), player().name()), null, source(), (Date)expires().map(Date::from).orElse(null), reason()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\BanlistService$UserBan.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */