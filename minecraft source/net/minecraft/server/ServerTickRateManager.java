/*     */ package net.minecraft.server;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTickingStepPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ public class ServerTickRateManager extends TickRateManager {
/*     */   private long remainingSprintTicks;
/*     */   private long sprintTickStartTime;
/*     */   private long sprintTimeSpend;
/*     */   
/*     */   public ServerTickRateManager(MinecraftServer server) {
/*  13 */     this.remainingSprintTicks = 0L;
/*  14 */     this.sprintTickStartTime = 0L;
/*  15 */     this.sprintTimeSpend = 0L;
/*  16 */     this.scheduledCurrentSprintTicks = 0L;
/*  17 */     this.previousIsFrozen = false;
/*     */ 
/*     */ 
/*     */     
/*  21 */     this.server = server;
/*     */   }
/*     */   private long scheduledCurrentSprintTicks; private boolean previousIsFrozen; private final MinecraftServer server;
/*     */   
/*  25 */   public boolean isSprinting() { return (this.scheduledCurrentSprintTicks > 0L); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean frozen) {
/*  30 */     super.setFrozen(frozen);
/*  31 */     updateStateToClients();
/*     */   }
/*     */ 
/*     */   
/*  35 */   private void updateStateToClients() { this.server.getPlayerList().broadcastAll(ClientboundTickingStatePacket.from(this)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   private void updateStepTicks() { this.server.getPlayerList().broadcastAll(ClientboundTickingStepPacket.from(this)); }
/*     */ 
/*     */   
/*     */   public boolean stepGameIfPaused(int ticks) {
/*  43 */     if (!isFrozen()) {
/*  44 */       return false;
/*     */     }
/*  46 */     this.frozenTicksToRun = ticks;
/*  47 */     updateStepTicks();
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public boolean stopStepping() {
/*  52 */     if (this.frozenTicksToRun > 0) {
/*  53 */       this.frozenTicksToRun = 0;
/*  54 */       updateStepTicks();
/*  55 */       return true;
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   public boolean stopSprinting() {
/*  61 */     if (this.remainingSprintTicks > 0L) {
/*  62 */       finishTickSprint();
/*  63 */       return true;
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requestGameToSprint(int time) {
/*  70 */     boolean interrupted = (this.remainingSprintTicks > 0L);
/*  71 */     this.sprintTimeSpend = 0L;
/*  72 */     this.scheduledCurrentSprintTicks = time;
/*  73 */     this.remainingSprintTicks = time;
/*  74 */     this.previousIsFrozen = isFrozen();
/*  75 */     setFrozen(false);
/*  76 */     return interrupted;
/*     */   }
/*     */   
/*     */   private void finishTickSprint() {
/*  80 */     long completedTicks = this.scheduledCurrentSprintTicks - this.remainingSprintTicks;
/*  81 */     double millisecondsToComplete = Math.max(1.0D, this.sprintTimeSpend) / TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*  82 */     int ticksPerSecond = (int)((TimeUtil.MILLISECONDS_PER_SECOND * completedTicks) / millisecondsToComplete);
/*  83 */     String millisecondsPerTick = String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf((completedTicks == 0L) ? millisecondsPerTick() : (millisecondsToComplete / completedTicks)) });
/*  84 */     this.scheduledCurrentSprintTicks = 0L;
/*  85 */     this.sprintTimeSpend = 0L;
/*  86 */     this.server.createCommandSourceStack().sendSuccess(() -> Component.translatable("commands.tick.sprint.report", new Object[] { Integer.valueOf(ticksPerSecond), millisecondsPerTick }), true);
/*  87 */     this.remainingSprintTicks = 0L;
/*  88 */     setFrozen(this.previousIsFrozen);
/*  89 */     this.server.onTickRateChanged();
/*     */   }
/*     */   
/*     */   public boolean checkShouldSprintThisTick() {
/*  93 */     if (!this.runGameElements) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (this.remainingSprintTicks > 0L) {
/*  97 */       this.sprintTickStartTime = System.nanoTime();
/*  98 */       this.remainingSprintTicks--;
/*  99 */       return true;
/*     */     } 
/* 101 */     finishTickSprint();
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public void endTickWork() { this.sprintTimeSpend += System.nanoTime() - this.sprintTickStartTime; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTickRate(float rate) {
/* 112 */     super.setTickRate(rate);
/* 113 */     this.server.onTickRateChanged();
/* 114 */     updateStateToClients();
/*     */   }
/*     */   
/*     */   public void updateJoiningPlayer(ServerPlayer player) {
/* 118 */     player.connection.send(ClientboundTickingStatePacket.from(this));
/* 119 */     player.connection.send(ClientboundTickingStepPacket.from(this));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerTickRateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */