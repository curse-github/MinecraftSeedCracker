/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class GameTestTicker
/*    */ {
/* 12 */   public static final GameTestTicker SINGLETON = new GameTestTicker();
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 14 */   private final Collection<GameTestInfo> testInfos = Lists.newCopyOnWriteArrayList();
/*    */   private GameTestRunner runner;
/* 16 */   private State state = State.IDLE;
/*    */   
/*    */   private enum State {
/* 19 */     IDLE,
/* 20 */     RUNNING,
/* 21 */     HALTING;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void add(GameTestInfo testInfo) { this.testInfos.add(testInfo); }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 32 */     if (this.state != State.IDLE) {
/* 33 */       this.state = State.HALTING;
/*    */       return;
/*    */     } 
/* 36 */     this.testInfos.clear();
/* 37 */     if (this.runner != null) {
/* 38 */       this.runner.stop();
/* 39 */       this.runner = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setRunner(GameTestRunner runner) {
/* 44 */     if (this.runner != null) {
/* 45 */       Util.logAndPauseIfInIde("The runner was already set in GameTestTicker");
/*    */     }
/* 47 */     this.runner = runner;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 51 */     if (this.runner == null) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     this.state = State.RUNNING;
/* 56 */     this.testInfos.forEach(i -> i.tick(this.runner));
/* 57 */     this.testInfos.removeIf(GameTestInfo::isDone);
/* 58 */     State finishingState = this.state;
/* 59 */     this.state = State.IDLE;
/* 60 */     if (finishingState == State.HALTING)
/* 61 */       clear(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestTicker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */