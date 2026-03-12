/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.trading.MerchantOffers;
/*    */ 
/*    */ public class ClientboundMerchantOffersPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMerchantOffersPacket> STREAM_CODEC = Packet.codec(ClientboundMerchantOffersPacket::write, ClientboundMerchantOffersPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   private final MerchantOffers offers;
/*    */   private final int villagerLevel;
/*    */   private final int villagerXp;
/*    */   private final boolean showProgress;
/*    */   private final boolean canRestock;
/*    */   
/*    */   public ClientboundMerchantOffersPacket(int containerId, MerchantOffers offers, int merchantLevel, int merchantXp, boolean showProgress, boolean canRestock) {
/* 20 */     this.containerId = containerId;
/* 21 */     this.offers = offers.copy();
/* 22 */     this.villagerLevel = merchantLevel;
/* 23 */     this.villagerXp = merchantXp;
/* 24 */     this.showProgress = showProgress;
/* 25 */     this.canRestock = canRestock;
/*    */   }
/*    */   
/*    */   private ClientboundMerchantOffersPacket(RegistryFriendlyByteBuf input) {
/* 29 */     this.containerId = input.readContainerId();
/* 30 */     this.offers = (MerchantOffers)MerchantOffers.STREAM_CODEC.decode(input);
/* 31 */     this.villagerLevel = input.readVarInt();
/* 32 */     this.villagerXp = input.readVarInt();
/* 33 */     this.showProgress = input.readBoolean();
/* 34 */     this.canRestock = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 38 */     output.writeContainerId(this.containerId);
/* 39 */     MerchantOffers.STREAM_CODEC.encode(output, this.offers);
/* 40 */     output.writeVarInt(this.villagerLevel);
/* 41 */     output.writeVarInt(this.villagerXp);
/* 42 */     output.writeBoolean(this.showProgress);
/* 43 */     output.writeBoolean(this.canRestock);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public PacketType<ClientboundMerchantOffersPacket> type() { return GamePacketTypes.CLIENTBOUND_MERCHANT_OFFERS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void handle(ClientGamePacketListener listener) { listener.handleMerchantOffers(this); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int getContainerId() { return this.containerId; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public MerchantOffers getOffers() { return this.offers; }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public int getVillagerLevel() { return this.villagerLevel; }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public int getVillagerXp() { return this.villagerXp; }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public boolean showProgress() { return this.showProgress; }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public boolean canRestock() { return this.canRestock; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMerchantOffersPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */