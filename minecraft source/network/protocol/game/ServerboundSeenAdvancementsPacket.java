/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ServerboundSeenAdvancementsPacket extends Object implements Packet<ServerGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSeenAdvancementsPacket> STREAM_CODEC = Packet.codec(ServerboundSeenAdvancementsPacket::write, ServerboundSeenAdvancementsPacket::new);
/*    */   
/*    */   private final Action action;
/*    */   private final Identifier tab;
/*    */   
/*    */   public ServerboundSeenAdvancementsPacket(Action action, Identifier tab) {
/* 18 */     this.action = action;
/* 19 */     this.tab = tab;
/*    */   }
/*    */ 
/*    */   
/* 23 */   public static ServerboundSeenAdvancementsPacket openedTab(AdvancementHolder tab) { return new ServerboundSeenAdvancementsPacket(Action.OPENED_TAB, tab.id()); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static ServerboundSeenAdvancementsPacket closedScreen() { return new ServerboundSeenAdvancementsPacket(Action.CLOSED_SCREEN, null); }
/*    */ 
/*    */   
/*    */   private ServerboundSeenAdvancementsPacket(FriendlyByteBuf input) {
/* 31 */     this.action = (Action)input.readEnum(Action.class);
/* 32 */     if (this.action == Action.OPENED_TAB) {
/* 33 */       this.tab = input.readIdentifier();
/*    */     } else {
/* 35 */       this.tab = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 40 */     output.writeEnum(this.action);
/* 41 */     if (this.action == Action.OPENED_TAB) {
/* 42 */       output.writeIdentifier(this.tab);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public PacketType<ServerboundSeenAdvancementsPacket> type() { return GamePacketTypes.SERVERBOUND_SEEN_ADVANCEMENTS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void handle(ServerGamePacketListener listener) { listener.handleSeenAdvancements(this); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public Action getAction() { return this.action; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public Identifier getTab() { return this.tab; }
/*    */   
/*    */   public enum Action
/*    */   {
/* 65 */     OPENED_TAB,
/* 66 */     CLOSED_SCREEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSeenAdvancementsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */