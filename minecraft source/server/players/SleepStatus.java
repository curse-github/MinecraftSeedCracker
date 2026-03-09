/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SleepStatus
/*    */ {
/*    */   private int activePlayers;
/*    */   private int sleepingPlayers;
/*    */   
/* 16 */   public boolean areEnoughSleeping(int sleepPercentageNeeded) { return (this.sleepingPlayers >= sleepersNeeded(sleepPercentageNeeded)); }
/*    */ 
/*    */   
/*    */   public boolean areEnoughDeepSleeping(int sleepPercentageNeeded, List<ServerPlayer> players) {
/* 20 */     int deepSleepers = (int)players.stream().filter(Player::isSleepingLongEnough).count();
/* 21 */     return (deepSleepers >= sleepersNeeded(sleepPercentageNeeded));
/*    */   }
/*    */ 
/*    */   
/* 25 */   public int sleepersNeeded(int sleepPercentageNeeded) { return Math.max(1, Mth.ceil((this.activePlayers * sleepPercentageNeeded) / 100.0F)); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void removeAllSleepers() { this.sleepingPlayers = 0; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public int amountSleeping() { return this.sleepingPlayers; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean update(List<ServerPlayer> players) {
/* 38 */     int oldActivePlayers = this.activePlayers;
/* 39 */     int oldSleepingPlayers = this.sleepingPlayers;
/* 40 */     this.activePlayers = 0;
/* 41 */     this.sleepingPlayers = 0;
/*    */     
/* 43 */     for (ServerPlayer player : players) {
/* 44 */       if (!player.isSpectator()) {
/* 45 */         this.activePlayers++;
/* 46 */         if (player.isSleeping()) {
/* 47 */           this.sleepingPlayers++;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     return ((oldSleepingPlayers > 0 || this.sleepingPlayers > 0) && (oldActivePlayers != this.activePlayers || oldSleepingPlayers != this.sleepingPlayers));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\SleepStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */