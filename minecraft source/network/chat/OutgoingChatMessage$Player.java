/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Player
/*    */   extends Record
/*    */   implements OutgoingChatMessage
/*    */ {
/*    */   private final PlayerChatMessage message;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/OutgoingChatMessage$Player;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Player; }
/*    */   
/* 17 */   public Player(PlayerChatMessage message) { this.message = message; } public PlayerChatMessage message() { return this.message; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/OutgoingChatMessage$Player;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Player; }
/*    */   
/* 20 */   public Component content() { return this.message.decoratedContent(); }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/OutgoingChatMessage$Player;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Player;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/*    */   public void sendToPlayer(ServerPlayer player, boolean filtered, ChatType.Bound chatType) {
/* 25 */     PlayerChatMessage filteredMessage = this.message.filter(filtered);
/* 26 */     if (!filteredMessage.isFullyFiltered())
/* 27 */       player.connection.sendPlayerChatMessage(filteredMessage, chatType); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\OutgoingChatMessage$Player.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */