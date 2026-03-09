/*    */ package net.minecraft.world.entity.monster.warden;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public static enum AngerLevel
/*    */ {
/* 10 */   CALM(0, SoundEvents.WARDEN_AMBIENT, SoundEvents.WARDEN_LISTENING),
/* 11 */   AGITATED(40, SoundEvents.WARDEN_AGITATED, SoundEvents.WARDEN_LISTENING_ANGRY),
/* 12 */   ANGRY(80, SoundEvents.WARDEN_ANGRY, SoundEvents.WARDEN_LISTENING_ANGRY);
/*    */   
/*    */   static  {
/* 15 */     SORTED_LEVELS = (AngerLevel[])Util.make(values(), values -> 
/* 16 */         Arrays.sort(values, ()));
/*    */   }
/*    */   private static final AngerLevel[] SORTED_LEVELS;
/*    */   private final int minimumAnger;
/*    */   private final SoundEvent ambientSound;
/*    */   private final SoundEvent listeningSound;
/*    */   
/*    */   AngerLevel(int minimumAnger, SoundEvent ambientSound, SoundEvent listeningSound) {
/* 24 */     this.minimumAnger = minimumAnger;
/* 25 */     this.ambientSound = ambientSound;
/* 26 */     this.listeningSound = listeningSound;
/*    */   }
/*    */ 
/*    */   
/* 30 */   public int getMinimumAnger() { return this.minimumAnger; }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public SoundEvent getAmbientSound() { return this.ambientSound; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public SoundEvent getListeningSound() { return this.listeningSound; }
/*    */ 
/*    */   
/*    */   public static AngerLevel byAnger(int anger) {
/* 42 */     for (AngerLevel level : SORTED_LEVELS) {
/* 43 */       if (anger >= level.minimumAnger) {
/* 44 */         return level;
/*    */       }
/*    */     } 
/* 47 */     return CALM;
/*    */   }
/*    */ 
/*    */   
/* 51 */   public boolean isAngry() { return (this == ANGRY); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\warden\AngerLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */