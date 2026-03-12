/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ 
/*    */ public final class ServerboundSetBeaconPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final Optional<Holder<MobEffect>> primary;
/*    */   private final Optional<Holder<MobEffect>> secondary;
/*    */   
/* 13 */   public ServerboundSetBeaconPacket(Optional<Holder<MobEffect>> primary, Optional<Holder<MobEffect>> secondary) { this.primary = primary; this.secondary = secondary; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket; } public Optional<Holder<MobEffect>> primary() { return this.primary; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Holder<MobEffect>> secondary() { return this.secondary; }
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetBeaconPacket> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC
/* 15 */       .apply(ByteBufCodecs::optional), ServerboundSetBeaconPacket::primary, MobEffect.STREAM_CODEC
/* 16 */       .apply(ByteBufCodecs::optional), ServerboundSetBeaconPacket::secondary, ServerboundSetBeaconPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ServerboundSetBeaconPacket> type() { return GamePacketTypes.SERVERBOUND_SET_BEACON; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ServerGamePacketListener listener) { listener.handleSetBeaconPacket(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetBeaconPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */