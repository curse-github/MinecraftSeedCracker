/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundSetScorePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final String owner;
/*    */   private final String objectiveName;
/*    */   
/* 15 */   public ClientboundSetScorePacket(String owner, String objectiveName, int score, Optional<Component> display, Optional<NumberFormat> numberFormat) { this.owner = owner; this.objectiveName = objectiveName; this.score = score; this.display = display; this.numberFormat = numberFormat; } private final int score; private final Optional<Component> display; private final Optional<NumberFormat> numberFormat; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public String owner() { return this.owner; } public String objectiveName() { return this.objectiveName; } public int score() { return this.score; } public Optional<Component> display() { return this.display; } public Optional<NumberFormat> numberFormat() { return this.numberFormat; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetScorePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundSetScorePacket::owner, ByteBufCodecs.STRING_UTF8, ClientboundSetScorePacket::objectiveName, ByteBufCodecs.VAR_INT, ClientboundSetScorePacket::score, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, ClientboundSetScorePacket::display, NumberFormatTypes.OPTIONAL_STREAM_CODEC, ClientboundSetScorePacket::numberFormat, ClientboundSetScorePacket::new);
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
/* 33 */   public PacketType<ClientboundSetScorePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_SCORE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void handle(ClientGamePacketListener listener) { listener.handleSetScore(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetScorePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */