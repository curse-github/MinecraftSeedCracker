/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.stats.RecipeBookSettings;
/*    */ 
/*    */ public final class ClientboundRecipeBookSettingsPacket extends Record implements Packet<ClientGamePacketListener> {
/*  9 */   public ClientboundRecipeBookSettingsPacket(RecipeBookSettings bookSettings) { this.bookSettings = bookSettings; } private final RecipeBookSettings bookSettings; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookSettingsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookSettingsPacket; } public RecipeBookSettings bookSettings() { return this.bookSettings; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookSettingsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookSettingsPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookSettingsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookSettingsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundRecipeBookSettingsPacket> STREAM_CODEC = StreamCodec.composite(RecipeBookSettings.STREAM_CODEC, ClientboundRecipeBookSettingsPacket::bookSettings, ClientboundRecipeBookSettingsPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public PacketType<ClientboundRecipeBookSettingsPacket> type() { return GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_SETTINGS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void handle(ClientGamePacketListener listener) { listener.handleRecipeBookSettings(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRecipeBookSettingsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */