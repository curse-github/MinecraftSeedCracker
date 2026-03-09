/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundTestInstanceBlockStatus extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component status;
/*    */   private final Optional<Vec3i> size;
/*    */   
/* 14 */   public ClientboundTestInstanceBlockStatus(Component status, Optional<Vec3i> size) { this.status = status; this.size = size; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus; } public Component status() { return this.status; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Vec3i> size() { return this.size; }
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTestInstanceBlockStatus> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, ClientboundTestInstanceBlockStatus::status, 
/*    */       
/* 17 */       ByteBufCodecs.optional(Vec3i.STREAM_CODEC), ClientboundTestInstanceBlockStatus::size, ClientboundTestInstanceBlockStatus::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public PacketType<ClientboundTestInstanceBlockStatus> type() { return GamePacketTypes.CLIENTBOUND_TEST_INSTANCE_BLOCK_STATUS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void handle(ClientGamePacketListener listener) { listener.handleTestInstanceBlockStatus(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTestInstanceBlockStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */