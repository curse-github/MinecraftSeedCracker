/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ServerboundInteractPacket extends Object implements Packet<ServerGamePacketListener> {
/*  18 */   public static final StreamCodec<FriendlyByteBuf, ServerboundInteractPacket> STREAM_CODEC = Packet.codec(ServerboundInteractPacket::write, ServerboundInteractPacket::new);
/*     */   
/*     */   private final int entityId;
/*     */   private final Action action;
/*     */   private final boolean usingSecondaryAction;
/*     */   
/*     */   private ServerboundInteractPacket(int entityId, boolean usingSecondaryAction, Action action) {
/*  25 */     this.entityId = entityId;
/*  26 */     this.action = action;
/*  27 */     this.usingSecondaryAction = usingSecondaryAction;
/*     */   }
/*     */ 
/*     */   
/*  31 */   public static ServerboundInteractPacket createAttackPacket(Entity entity, boolean usingSecondaryAction) { return new ServerboundInteractPacket(entity.getId(), usingSecondaryAction, ATTACK_ACTION); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public static ServerboundInteractPacket createInteractionPacket(Entity entity, boolean usingSecondaryAction, InteractionHand hand) { return new ServerboundInteractPacket(entity.getId(), usingSecondaryAction, new InteractionAction(hand)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static ServerboundInteractPacket createInteractionPacket(Entity entity, boolean usingSecondaryAction, InteractionHand hand, Vec3 location) { return new ServerboundInteractPacket(entity.getId(), usingSecondaryAction, new InteractionAtLocationAction(hand, location)); }
/*     */ 
/*     */   
/*     */   private ServerboundInteractPacket(FriendlyByteBuf input) {
/*  43 */     this.entityId = input.readVarInt();
/*  44 */     ActionType type = (ActionType)input.readEnum(ActionType.class);
/*  45 */     this.action = (Action)type.reader.apply(input);
/*  46 */     this.usingSecondaryAction = input.readBoolean();
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/*  50 */     output.writeVarInt(this.entityId);
/*  51 */     output.writeEnum(this.action.getType());
/*  52 */     this.action.write(output);
/*  53 */     output.writeBoolean(this.usingSecondaryAction);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public PacketType<ServerboundInteractPacket> type() { return GamePacketTypes.SERVERBOUND_INTERACT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public void handle(ServerGamePacketListener listener) { listener.handleInteract(this); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public Entity getTarget(ServerLevel level) { return level.getEntityOrPart(this.entityId); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean isUsingSecondaryAction() { return this.usingSecondaryAction; }
/*     */ 
/*     */   
/*     */   public boolean isWithinRange(ServerPlayer player, AABB aabb, double buffer) {
/*  75 */     if (this.action.getType() == ActionType.ATTACK) {
/*  76 */       return player.isWithinAttackRange(aabb, buffer);
/*     */     }
/*  78 */     return player.isWithinEntityInteractionRange(aabb, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public void dispatch(Handler handler) { this.action.dispatch(handler); }
/*     */   
/*     */   private enum ActionType
/*     */   {
/*  87 */     INTERACT(InteractionAction::new),
/*  88 */     ATTACK(input -> ServerboundInteractPacket.ATTACK_ACTION),
/*  89 */     INTERACT_AT(InteractionAtLocationAction::new);
/*     */ 
/*     */     
/*     */     private final Function<FriendlyByteBuf, ServerboundInteractPacket.Action> reader;
/*     */ 
/*     */     
/*  95 */     ActionType(Function<FriendlyByteBuf, ServerboundInteractPacket.Action> reader) { this.reader = reader; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InteractionAction
/*     */     implements Action
/*     */   {
/*     */     private final InteractionHand hand;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     private InteractionAction(InteractionHand hand) { this.hand = hand; }
/*     */ 
/*     */ 
/*     */     
/* 123 */     private InteractionAction(FriendlyByteBuf input) { this.hand = (InteractionHand)input.readEnum(InteractionHand.class); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     public ServerboundInteractPacket.ActionType getType() { return ServerboundInteractPacket.ActionType.INTERACT; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     public void dispatch(ServerboundInteractPacket.Handler handler) { handler.onInteraction(this.hand); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     public void write(FriendlyByteBuf output) { output.writeEnum(this.hand); }
/*     */   }
/*     */   
/*     */   private static class InteractionAtLocationAction
/*     */     implements Action {
/*     */     private final InteractionHand hand;
/*     */     private final Vec3 location;
/*     */     
/*     */     private InteractionAtLocationAction(InteractionHand hand, Vec3 location) {
/* 147 */       this.hand = hand;
/* 148 */       this.location = location;
/*     */     }
/*     */     
/*     */     private InteractionAtLocationAction(FriendlyByteBuf input) {
/* 152 */       this.location = new Vec3(input.readFloat(), input.readFloat(), input.readFloat());
/* 153 */       this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 158 */     public ServerboundInteractPacket.ActionType getType() { return ServerboundInteractPacket.ActionType.INTERACT_AT; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     public void dispatch(ServerboundInteractPacket.Handler handler) { handler.onInteraction(this.hand, this.location); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 168 */       output.writeFloat((float)this.location.x);
/* 169 */       output.writeFloat((float)this.location.y);
/* 170 */       output.writeFloat((float)this.location.z);
/* 171 */       output.writeEnum(this.hand);
/*     */     }
/*     */   }
/*     */   
/* 175 */   private static final Action ATTACK_ACTION = new Action()
/*     */     {
/*     */       public ServerboundInteractPacket.ActionType getType() {
/* 178 */         return ServerboundInteractPacket.ActionType.ATTACK;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 183 */       public void dispatch(ServerboundInteractPacket.Handler handler) { handler.onAttack(); }
/*     */       
/*     */       public void write(FriendlyByteBuf output) {}
/*     */     };
/*     */   
/*     */   private static interface Action {
/*     */     ServerboundInteractPacket.ActionType getType();
/*     */     
/*     */     void dispatch(ServerboundInteractPacket.Handler param1Handler);
/*     */     
/*     */     void write(FriendlyByteBuf param1FriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public static interface Handler {
/*     */     void onInteraction(InteractionHand param1InteractionHand);
/*     */     
/*     */     void onInteraction(InteractionHand param1InteractionHand, Vec3 param1Vec3);
/*     */     
/*     */     void onAttack();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */