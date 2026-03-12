/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class InteractionAtLocationAction
/*     */   implements ServerboundInteractPacket.Action
/*     */ {
/*     */   private final InteractionHand hand;
/*     */   private final Vec3 location;
/*     */   
/*     */   private InteractionAtLocationAction(InteractionHand hand, Vec3 location) {
/* 147 */     this.hand = hand;
/* 148 */     this.location = location;
/*     */   }
/*     */   
/*     */   private InteractionAtLocationAction(FriendlyByteBuf input) {
/* 152 */     this.location = new Vec3(input.readFloat(), input.readFloat(), input.readFloat());
/* 153 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public ServerboundInteractPacket.ActionType getType() { return ServerboundInteractPacket.ActionType.INTERACT_AT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public void dispatch(ServerboundInteractPacket.Handler handler) { handler.onInteraction(this.hand, this.location); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/* 168 */     output.writeFloat((float)this.location.x);
/* 169 */     output.writeFloat((float)this.location.y);
/* 170 */     output.writeFloat((float)this.location.z);
/* 171 */     output.writeEnum(this.hand);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket$InteractionAtLocationAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */