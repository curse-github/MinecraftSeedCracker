/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementProgress;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ClientboundUpdateAdvancementsPacket extends Object implements Packet<ClientGamePacketListener> {
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAdvancementsPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateAdvancementsPacket::write, ClientboundUpdateAdvancementsPacket::new);
/*    */   
/*    */   private final boolean reset;
/*    */   private final List<AdvancementHolder> added;
/*    */   private final Set<Identifier> removed;
/*    */   private final Map<Identifier, AdvancementProgress> progress;
/*    */   private final boolean showAdvancements;
/*    */   
/*    */   public ClientboundUpdateAdvancementsPacket(boolean reset, Collection<AdvancementHolder> newAdvancements, Set<Identifier> removedAdvancements, Map<Identifier, AdvancementProgress> progress, boolean showAdvancements) {
/* 28 */     this.reset = reset;
/* 29 */     this.added = List.copyOf(newAdvancements);
/* 30 */     this.removed = Set.copyOf(removedAdvancements);
/* 31 */     this.progress = Map.copyOf(progress);
/* 32 */     this.showAdvancements = showAdvancements;
/*    */   }
/*    */   
/*    */   private ClientboundUpdateAdvancementsPacket(RegistryFriendlyByteBuf input) {
/* 36 */     this.reset = input.readBoolean();
/* 37 */     this.added = (List)AdvancementHolder.LIST_STREAM_CODEC.decode(input);
/* 38 */     this.removed = (Set)input.readCollection(Sets::newLinkedHashSetWithExpectedSize, FriendlyByteBuf::readIdentifier);
/* 39 */     this.progress = input.readMap(FriendlyByteBuf::readIdentifier, AdvancementProgress::fromNetwork);
/* 40 */     this.showAdvancements = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 44 */     output.writeBoolean(this.reset);
/*    */     
/* 46 */     AdvancementHolder.LIST_STREAM_CODEC.encode(output, this.added);
/* 47 */     output.writeCollection(this.removed, FriendlyByteBuf::writeIdentifier);
/* 48 */     output.writeMap(this.progress, FriendlyByteBuf::writeIdentifier, (buffer, value) -> value.serializeToNetwork(buffer));
/* 49 */     output.writeBoolean(this.showAdvancements);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public PacketType<ClientboundUpdateAdvancementsPacket> type() { return GamePacketTypes.CLIENTBOUND_UPDATE_ADVANCEMENTS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public void handle(ClientGamePacketListener listener) { listener.handleUpdateAdvancementsPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public List<AdvancementHolder> getAdded() { return this.added; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Set<Identifier> getRemoved() { return this.removed; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public Map<Identifier, AdvancementProgress> getProgress() { return this.progress; }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public boolean shouldReset() { return this.reset; }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public boolean shouldShowAdvancements() { return this.showAdvancements; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateAdvancementsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */