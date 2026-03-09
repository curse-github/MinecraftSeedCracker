/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.inventory.MenuType;
/*    */ 
/*    */ public class ClientboundOpenScreenPacket extends Object implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenScreenPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundOpenScreenPacket::getContainerId, 
/*    */       
/* 16 */       ByteBufCodecs.registry(Registries.MENU), ClientboundOpenScreenPacket::getType, ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundOpenScreenPacket::getTitle, ClientboundOpenScreenPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   
/*    */   private final MenuType<?> type;
/*    */   
/*    */   private final Component title;
/*    */ 
/*    */   
/*    */   public ClientboundOpenScreenPacket(int containerId, MenuType<?> type, Component title) {
/* 26 */     this.containerId = containerId;
/* 27 */     this.type = type;
/* 28 */     this.title = title;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public PacketType<ClientboundOpenScreenPacket> type() { return GamePacketTypes.CLIENTBOUND_OPEN_SCREEN; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void handle(ClientGamePacketListener listener) { listener.handleOpenScreen(this); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int getContainerId() { return this.containerId; }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public MenuType<?> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public Component getTitle() { return this.title; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundOpenScreenPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */