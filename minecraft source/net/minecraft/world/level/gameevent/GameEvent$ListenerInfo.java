/*     */ package net.minecraft.world.level.gameevent;
/*     */ 
/*     */ import net.minecraft.core.Holder;
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
/*     */ public final class ListenerInfo
/*     */   extends Object
/*     */   implements Comparable<GameEvent.ListenerInfo>
/*     */ {
/*     */   private final Holder<GameEvent> gameEvent;
/*     */   private final Vec3 source;
/*     */   private final GameEvent.Context context;
/*     */   private final GameEventListener recipient;
/*     */   private final double distanceToRecipient;
/*     */   
/*     */   public ListenerInfo(Holder<GameEvent> gameEvent, Vec3 source, GameEvent.Context context, GameEventListener recipient, Vec3 recipientPos) {
/* 128 */     this.gameEvent = gameEvent;
/* 129 */     this.source = source;
/* 130 */     this.context = context;
/* 131 */     this.recipient = recipient;
/* 132 */     this.distanceToRecipient = source.distanceToSqr(recipientPos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public int compareTo(ListenerInfo other) { return Double.compare(this.distanceToRecipient, other.distanceToRecipient); }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public Holder<GameEvent> gameEvent() { return this.gameEvent; }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public Vec3 source() { return this.source; }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public GameEvent.Context context() { return this.context; }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public GameEventListener recipient() { return this.recipient; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\GameEvent$ListenerInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */