/*    */ package net.minecraft.network.protocol.common;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ServerboundCustomClickActionPacket extends Record implements Packet<ServerCommonPacketListener> {
/*    */   private final Identifier id;
/*    */   private final Optional<Tag> payload;
/*    */   
/* 14 */   public ServerboundCustomClickActionPacket(Identifier id, Optional<Tag> payload) { this.id = id; this.payload = payload; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Tag> payload() { return this.payload; }
/*    */   
/* 16 */   private static final StreamCodec<ByteBuf, Optional<Tag>> UNTRUSTED_TAG_CODEC = ByteBufCodecs.optionalTagCodec(() -> new NbtAccounter(32768L, 16))
/* 17 */     .apply(ByteBufCodecs.lengthPrefixed(65536));
/*    */   
/* 19 */   public static final StreamCodec<ByteBuf, ServerboundCustomClickActionPacket> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, ServerboundCustomClickActionPacket::id, UNTRUSTED_TAG_CODEC, ServerboundCustomClickActionPacket::payload, ServerboundCustomClickActionPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundCustomClickActionPacket> type() { return CommonPacketTypes.SERVERBOUND_CUSTOM_CLICK_ACTION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerCommonPacketListener listener) { listener.handleCustomClickAction(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ServerboundCustomClickActionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */