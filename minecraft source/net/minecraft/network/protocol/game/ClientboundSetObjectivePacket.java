/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class ClientboundSetObjectivePacket extends Object implements Packet<ClientGamePacketListener> {
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetObjectivePacket> STREAM_CODEC = Packet.codec(ClientboundSetObjectivePacket::write, ClientboundSetObjectivePacket::new);
/*    */   
/*    */   public static final int METHOD_ADD = 0;
/*    */   
/*    */   public static final int METHOD_REMOVE = 1;
/*    */   public static final int METHOD_CHANGE = 2;
/*    */   private final String objectiveName;
/*    */   private final Component displayName;
/*    */   private final ObjectiveCriteria.RenderType renderType;
/*    */   private final Optional<NumberFormat> numberFormat;
/*    */   private final int method;
/*    */   
/*    */   public ClientboundSetObjectivePacket(Objective objective, int method) {
/* 31 */     this.objectiveName = objective.getName();
/* 32 */     this.displayName = objective.getDisplayName();
/* 33 */     this.renderType = objective.getRenderType();
/* 34 */     this.numberFormat = Optional.ofNullable(objective.numberFormat());
/* 35 */     this.method = method;
/*    */   }
/*    */   
/*    */   private ClientboundSetObjectivePacket(RegistryFriendlyByteBuf input) {
/* 39 */     this.objectiveName = input.readUtf();
/* 40 */     this.method = input.readByte();
/*    */     
/* 42 */     if (this.method == 0 || this.method == 2) {
/* 43 */       this.displayName = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 44 */       this.renderType = (ObjectiveCriteria.RenderType)input.readEnum(ObjectiveCriteria.RenderType.class);
/* 45 */       this.numberFormat = (Optional)NumberFormatTypes.OPTIONAL_STREAM_CODEC.decode(input);
/*    */     } else {
/* 47 */       this.displayName = CommonComponents.EMPTY;
/* 48 */       this.renderType = ObjectiveCriteria.RenderType.INTEGER;
/* 49 */       this.numberFormat = Optional.empty();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 54 */     output.writeUtf(this.objectiveName);
/* 55 */     output.writeByte(this.method);
/*    */     
/* 57 */     if (this.method == 0 || this.method == 2) {
/* 58 */       ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.displayName);
/* 59 */       output.writeEnum(this.renderType);
/* 60 */       NumberFormatTypes.OPTIONAL_STREAM_CODEC.encode(output, this.numberFormat);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public PacketType<ClientboundSetObjectivePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_OBJECTIVE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public void handle(ClientGamePacketListener listener) { listener.handleAddObjective(this); }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public String getObjectiveName() { return this.objectiveName; }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public Component getDisplayName() { return this.displayName; }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public int getMethod() { return this.method; }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public ObjectiveCriteria.RenderType getRenderType() { return this.renderType; }
/*    */ 
/*    */ 
/*    */   
/* 91 */   public Optional<NumberFormat> getNumberFormat() { return this.numberFormat; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetObjectivePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */