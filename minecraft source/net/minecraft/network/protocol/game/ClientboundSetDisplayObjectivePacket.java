/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.scores.DisplaySlot;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ 
/*    */ public class ClientboundSetDisplayObjectivePacket extends Object implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetDisplayObjectivePacket> STREAM_CODEC = Packet.codec(ClientboundSetDisplayObjectivePacket::write, ClientboundSetDisplayObjectivePacket::new);
/*    */   
/*    */   private final DisplaySlot slot;
/*    */   private final String objectiveName;
/*    */   
/*    */   public ClientboundSetDisplayObjectivePacket(DisplaySlot slot, Objective objective) {
/* 20 */     this.slot = slot;
/*    */     
/* 22 */     if (objective == null) {
/* 23 */       this.objectiveName = "";
/*    */     } else {
/* 25 */       this.objectiveName = objective.getName();
/*    */     } 
/*    */   }
/*    */   
/*    */   private ClientboundSetDisplayObjectivePacket(FriendlyByteBuf input) {
/* 30 */     this.slot = (DisplaySlot)input.readById(DisplaySlot.BY_ID);
/* 31 */     this.objectiveName = input.readUtf();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 35 */     output.writeById(DisplaySlot::id, this.slot);
/* 36 */     output.writeUtf(this.objectiveName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public PacketType<ClientboundSetDisplayObjectivePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_DISPLAY_OBJECTIVE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public void handle(ClientGamePacketListener listener) { listener.handleSetDisplayObjective(this); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public DisplaySlot getSlot() { return this.slot; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public String getObjectiveName() { return Objects.equals(this.objectiveName, "") ? null : this.objectiveName; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetDisplayObjectivePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */