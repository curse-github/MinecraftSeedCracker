/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
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
/*     */ class null
/*     */   implements ScoreAccess
/*     */ {
/*  82 */   public int get() { return score.value(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int value) {
/*  87 */     if (!canModify) {
/*  88 */       throw new IllegalStateException("Cannot modify read-only score");
/*     */     }
/*     */     
/*  91 */     boolean hasChanged = requiresSync.isTrue();
/*     */     
/*  93 */     if (objective.displayAutoUpdate()) {
/*  94 */       Component newDisplay = scoreHolder.getDisplayName();
/*  95 */       if (newDisplay != null && !newDisplay.equals(score.display())) {
/*  96 */         score.display(newDisplay);
/*  97 */         hasChanged = true;
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     if (value != score.value()) {
/* 102 */       score.value(value);
/* 103 */       hasChanged = true;
/*     */     } 
/*     */     
/* 106 */     if (hasChanged) {
/* 107 */       sendScoreToPlayers();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public Component display() { return score.display(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void display(Component display) {
/* 118 */     if (requiresSync.isTrue() || !Objects.equals(display, score.display())) {
/* 119 */       score.display(display);
/* 120 */       sendScoreToPlayers();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void numberFormatOverride(NumberFormat numberFormat) {
/* 126 */     score.numberFormat(numberFormat);
/* 127 */     sendScoreToPlayers();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public boolean locked() { return score.isLocked(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public void unlock() { setLocked(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public void lock() { setLocked(true); }
/*     */ 
/*     */   
/*     */   private void setLocked(boolean locked) {
/* 146 */     score.setLocked(locked);
/*     */     
/* 148 */     if (requiresSync.isTrue()) {
/* 149 */       sendScoreToPlayers();
/*     */     }
/*     */     
/* 152 */     Scoreboard.this.onScoreLockChanged(scoreHolder, objective);
/*     */   }
/*     */   
/*     */   private void sendScoreToPlayers() {
/* 156 */     Scoreboard.this.onScoreChanged(scoreHolder, objective, score);
/* 157 */     requiresSync.setFalse();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Scoreboard$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */