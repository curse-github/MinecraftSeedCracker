/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicGameEventListener<T extends GameEventListener>
/*    */   extends Object
/*    */ {
/*    */   private final T listener;
/*    */   private SectionPos lastSection;
/*    */   
/* 21 */   public DynamicGameEventListener(T listener) { this.listener = listener; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void add(ServerLevel level) { move(level); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public T getListener() { return (T)this.listener; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void remove(ServerLevel level) { ifChunkExists(level, this.lastSection, dispatcher -> dispatcher.unregister(this.listener)); }
/*    */ 
/*    */   
/*    */   public void move(ServerLevel level) {
/* 37 */     this.listener.getListenerSource().getPosition(level)
/* 38 */       .map(SectionPos::of)
/* 39 */       .ifPresent(currentSection -> {
/* 40 */           if (this.lastSection == null || !this.lastSection.equals(currentSection)) {
/* 41 */             ifChunkExists(level, this.lastSection, ());
/* 42 */             this.lastSection = currentSection;
/* 43 */             ifChunkExists(level, this.lastSection, ());
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void ifChunkExists(LevelReader level, SectionPos sectionPos, Consumer<GameEventListenerRegistry> action) {
/* 49 */     if (sectionPos == null) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     ChunkAccess chunk = level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.FULL, false);
/*    */     
/* 55 */     if (chunk != null)
/* 56 */       action.accept(chunk.getListenerRegistry(sectionPos.y())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\DynamicGameEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */