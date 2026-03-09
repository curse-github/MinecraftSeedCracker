/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface NeutralMob
/*     */ {
/*     */   public static final String TAG_ANGER_END_TIME = "anger_end_time";
/*     */   public static final String TAG_ANGRY_AT = "angry_at";
/*     */   public static final long NO_ANGER_END_TIME = -1L;
/*     */   
/*     */   long getPersistentAngerEndTime();
/*     */   
/*  41 */   default void setTimeToRemainAngry(long remainingTime) { setPersistentAngerEndTime(level().getGameTime() + remainingTime); }
/*     */ 
/*     */ 
/*     */   
/*     */   void setPersistentAngerEndTime(long paramLong);
/*     */ 
/*     */ 
/*     */   
/*     */   EntityReference<LivingEntity> getPersistentAngerTarget();
/*     */ 
/*     */ 
/*     */   
/*     */   void setPersistentAngerTarget(EntityReference<LivingEntity> paramEntityReference);
/*     */ 
/*     */   
/*     */   void startPersistentAngerTimer();
/*     */ 
/*     */   
/*     */   Level level();
/*     */ 
/*     */   
/*     */   default void addPersistentAngerSaveData(ValueOutput output) {
/*  63 */     output.putLong("anger_end_time", getPersistentAngerEndTime());
/*  64 */     output.storeNullable("angry_at", EntityReference.codec(), getPersistentAngerTarget());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void readPersistentAngerSaveData(Level level, ValueInput input) {
/*  71 */     Optional<Long> endTime = input.getLong("anger_end_time");
/*  72 */     if (endTime.isPresent()) {
/*  73 */       setPersistentAngerEndTime(((Long)endTime.get()).longValue());
/*     */     }
/*     */     else {
/*     */       
/*  77 */       Optional<Integer> angerTime = input.getInt("AngerTime");
/*  78 */       if (angerTime.isPresent()) {
/*  79 */         setTimeToRemainAngry(((Integer)angerTime.get()).intValue());
/*     */       } else {
/*  81 */         setPersistentAngerEndTime(-1L);
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     if (!(level instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     setPersistentAngerTarget(EntityReference.read(input, "angry_at"));
/*  90 */     setTarget(EntityReference.getLivingEntity(getPersistentAngerTarget(), level));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void updatePersistentAnger(ServerLevel level, boolean stayAngryIfTargetPresent) {
/*  97 */     LivingEntity target = getTarget();
/*     */     
/*  99 */     EntityReference<LivingEntity> persistentAngerTarget = getPersistentAngerTarget();
/*     */     
/* 101 */     if (target != null && target.isDeadOrDying() && persistentAngerTarget != null && persistentAngerTarget.matches(target) && target instanceof Mob) {
/*     */ 
/*     */ 
/*     */       
/* 105 */       stopBeingAngry();
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     if (target != null) {
/* 110 */       if (persistentAngerTarget == null || !persistentAngerTarget.matches(target))
/*     */       {
/* 112 */         setPersistentAngerTarget(EntityReference.of(target));
/*     */       }
/*     */       
/* 115 */       startPersistentAngerTimer();
/*     */     } 
/*     */     
/* 118 */     if (persistentAngerTarget != null && !isAngry() && (
/* 119 */       target == null || !isValidPlayerTarget(target) || !stayAngryIfTargetPresent)) {
/* 120 */       stopBeingAngry();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidPlayerTarget(LivingEntity target) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: instanceof net/minecraft/world/entity/player/Player
/*     */     //   4: ifeq -> 30
/*     */     //   7: aload_0
/*     */     //   8: checkcast net/minecraft/world/entity/player/Player
/*     */     //   11: astore_1
/*     */     //   12: aload_1
/*     */     //   13: invokevirtual isCreative : ()Z
/*     */     //   16: ifne -> 30
/*     */     //   19: aload_1
/*     */     //   20: invokevirtual isSpectator : ()Z
/*     */     //   23: ifne -> 30
/*     */     //   26: iconst_1
/*     */     //   27: goto -> 31
/*     */     //   30: iconst_0
/*     */     //   31: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #128	-> 0
/*     */     //   #126	-> 7
/*     */     //   #127	-> 13
/*     */     //   #128	-> 20
/*     */     //   #126	-> 31
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   12	18	1	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	32	0	target	Lnet/minecraft/world/entity/LivingEntity; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isAngryAt(LivingEntity entity, ServerLevel level) {
/* 135 */     if (!canAttack(entity)) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     if (isValidPlayerTarget(entity) && isAngryAtAllPlayers(level)) {
/* 140 */       return true;
/*     */     }
/*     */     
/* 143 */     EntityReference<LivingEntity> persistentAngerTarget = getPersistentAngerTarget();
/* 144 */     return (persistentAngerTarget != null && persistentAngerTarget.matches(entity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   default boolean isAngryAtAllPlayers(ServerLevel level) { return (((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue() && isAngry() && getPersistentAngerTarget() == null); }
/*     */ 
/*     */   
/*     */   default boolean isAngry() {
/* 154 */     long endTime = getPersistentAngerEndTime();
/* 155 */     if (endTime > 0L) {
/* 156 */       long remaining = endTime - level().getGameTime();
/* 157 */       return (remaining > 0L);
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */   
/*     */   default void playerDied(ServerLevel level, Player player) {
/* 163 */     if (!((Boolean)level.getGameRules().get(GameRules.FORGIVE_DEAD_PLAYERS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     EntityReference<LivingEntity> persistentAngerTarget = getPersistentAngerTarget();
/* 168 */     if (persistentAngerTarget == null || !persistentAngerTarget.matches(player)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 173 */     stopBeingAngry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void forgetCurrentTargetAndRefreshUniversalAnger() {
/* 180 */     stopBeingAngry();
/* 181 */     startPersistentAngerTimer();
/*     */   }
/*     */   
/*     */   default void stopBeingAngry() {
/* 185 */     setLastHurtByMob(null);
/* 186 */     setPersistentAngerTarget(null);
/* 187 */     setTarget(null);
/* 188 */     setPersistentAngerEndTime(-1L);
/*     */   }
/*     */   
/*     */   LivingEntity getLastHurtByMob();
/*     */   
/*     */   void setLastHurtByMob(LivingEntity paramLivingEntity);
/*     */   
/*     */   void setTarget(LivingEntity paramLivingEntity);
/*     */   
/*     */   boolean canAttack(LivingEntity paramLivingEntity);
/*     */   
/*     */   LivingEntity getTarget();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\NeutralMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */