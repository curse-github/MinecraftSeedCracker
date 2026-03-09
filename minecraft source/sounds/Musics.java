/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ 
/*    */ public class Musics
/*    */ {
/*    */   private static final int ONE_SECOND = 20;
/*    */   private static final int THIRTY_SECONDS = 600;
/*    */   private static final int TEN_MINUTES = 12000;
/*    */   private static final int TWENTY_MINUTES = 24000;
/*    */   private static final int FIVE_MINUTES = 6000;
/* 13 */   public static final Music MENU = new Music(SoundEvents.MUSIC_MENU, 20, 600, true);
/* 14 */   public static final Music CREATIVE = new Music(SoundEvents.MUSIC_CREATIVE, 12000, 24000, false);
/* 15 */   public static final Music CREDITS = new Music(SoundEvents.MUSIC_CREDITS, 0, 0, true);
/* 16 */   public static final Music END_BOSS = new Music(SoundEvents.MUSIC_DRAGON, 0, 0, true);
/* 17 */   public static final Music END = new Music(SoundEvents.MUSIC_END, 6000, 24000, true);
/*    */   
/* 19 */   public static final Music UNDER_WATER = createGameMusic(SoundEvents.MUSIC_UNDER_WATER);
/* 20 */   public static final Music GAME = createGameMusic(SoundEvents.MUSIC_GAME);
/*    */ 
/*    */   
/* 23 */   public static Music createGameMusic(Holder<SoundEvent> soundEvent) { return new Music(soundEvent, 12000, 24000, false); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\sounds\Musics.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */