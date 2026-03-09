/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ public static enum SoundSource {
/*  4 */   MASTER("master"),
/*  5 */   MUSIC("music"),
/*  6 */   RECORDS("record"),
/*  7 */   WEATHER("weather"),
/*  8 */   BLOCKS("block"),
/*  9 */   HOSTILE("hostile"),
/* 10 */   NEUTRAL("neutral"),
/* 11 */   PLAYERS("player"),
/* 12 */   AMBIENT("ambient"),
/* 13 */   VOICE("voice"),
/* 14 */   UI("ui");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 20 */   SoundSource(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public String getName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\sounds\SoundSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */