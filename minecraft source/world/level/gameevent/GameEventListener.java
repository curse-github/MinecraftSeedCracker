/*    */ package net.minecraft.world.level.gameevent;public interface GameEventListener {
/*    */   PositionSource getListenerSource();
/*    */   
/*    */   int getListenerRadius();
/*    */   
/*    */   boolean handleGameEvent(ServerLevel paramServerLevel, Holder<GameEvent> paramHolder, GameEvent.Context paramContext, Vec3 paramVec3);
/*    */   
/*    */   public enum DeliveryMode {
/*  9 */     UNSPECIFIED,
/* 10 */     BY_DISTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   default DeliveryMode getDeliveryMode() { return DeliveryMode.UNSPECIFIED; }
/*    */   
/*    */   public static interface Provider<T extends GameEventListener> {
/*    */     T getListener();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\GameEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */