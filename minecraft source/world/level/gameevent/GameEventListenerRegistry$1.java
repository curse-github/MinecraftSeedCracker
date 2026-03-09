/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements GameEventListenerRegistry
/*    */ {
/* 10 */   public boolean isEmpty() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void register(GameEventListener listener) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void unregister(GameEventListener listener) {}
/*    */ 
/*    */ 
/*    */   
/* 23 */   public boolean visitInRangeListeners(Holder<GameEvent> event, Vec3 sourcePosition, GameEvent.Context context, GameEventListenerRegistry.ListenerVisitor action) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */