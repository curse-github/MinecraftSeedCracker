/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ public final class ClientboundShowDialogPacket extends Record implements Packet<ClientCommonPacketListener> {
/* 11 */   public ClientboundShowDialogPacket(Holder<Dialog> dialog) { this.dialog = dialog; } private final Holder<Dialog> dialog; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket; } public Holder<Dialog> dialog() { return this.dialog; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundShowDialogPacket> STREAM_CODEC = StreamCodec.composite(Dialog.STREAM_CODEC, ClientboundShowDialogPacket::dialog, ClientboundShowDialogPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final StreamCodec<ByteBuf, ClientboundShowDialogPacket> CONTEXT_FREE_STREAM_CODEC = StreamCodec.composite(Dialog.CONTEXT_FREE_STREAM_CODEC
/* 20 */       .map(Holder::direct, Holder::value), ClientboundShowDialogPacket::dialog, ClientboundShowDialogPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public PacketType<ClientboundShowDialogPacket> type() { return CommonPacketTypes.CLIENTBOUND_SHOW_DIALOG; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void handle(ClientCommonPacketListener listener) { listener.handleShowDialog(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundShowDialogPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */