/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public interface OutgoingChatMessage {
/*    */   Component content();
/*    */   
/*    */   void sendToPlayer(ServerPlayer paramServerPlayer, boolean paramBoolean, ChatType.Bound paramBound);
/*    */   
/*    */   static OutgoingChatMessage create(PlayerChatMessage message) {
/* 11 */     if (message.isSystem()) {
/* 12 */       return new Disguised(message.decoratedContent());
/*    */     }
/* 14 */     return new Player(message);
/*    */   }
/*    */   public static final class Player extends Record implements OutgoingChatMessage { private final PlayerChatMessage message;
/* 17 */     public Player(PlayerChatMessage message) { this.message = message; } public PlayerChatMessage message() { return this.message; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/OutgoingChatMessage$Player;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Player; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/OutgoingChatMessage$Player;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Player; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/OutgoingChatMessage$Player;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Player;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 20 */     public Component content() { return this.message.decoratedContent(); }
/*    */ 
/*    */ 
/*    */     
/*    */     public void sendToPlayer(ServerPlayer player, boolean filtered, ChatType.Bound chatType) {
/* 25 */       PlayerChatMessage filteredMessage = this.message.filter(filtered);
/* 26 */       if (!filteredMessage.isFullyFiltered())
/* 27 */         player.connection.sendPlayerChatMessage(filteredMessage, chatType); 
/*    */     } }
/*    */   
/*    */   public static final class Disguised extends Record implements OutgoingChatMessage { private final Component content;
/*    */     
/* 32 */     public Disguised(Component content) { this.content = content; }
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised; }
/*    */     
/* 35 */     public Component content() { return this.content; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */     
/* 40 */     public void sendToPlayer(ServerPlayer player, boolean filtered, ChatType.Bound chatType) { player.connection.sendDisguisedChatMessage(this.content, chatType); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\OutgoingChatMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */