/*    */ package net.minecraft.sounds;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class Music extends Record {
/*    */   private final Holder<SoundEvent> sound;
/*    */   private final int minDelay;
/*    */   
/*  8 */   public Music(Holder<SoundEvent> sound, int minDelay, int maxDelay, boolean replaceCurrentMusic) { this.sound = sound; this.minDelay = minDelay; this.maxDelay = maxDelay; this.replaceCurrentMusic = replaceCurrentMusic; } private final int maxDelay; private final boolean replaceCurrentMusic; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/sounds/Music;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/Music; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/sounds/Music;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/Music; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/sounds/Music;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/sounds/Music;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<SoundEvent> sound() { return this.sound; } public int minDelay() { return this.minDelay; } public int maxDelay() { return this.maxDelay; } public boolean replaceCurrentMusic() { return this.replaceCurrentMusic; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Codec<Music> CODEC = RecordCodecBuilder.create(i -> i.group(SoundEvent.CODEC
/* 15 */         .fieldOf("sound").forGetter(Music::sound), ExtraCodecs.NON_NEGATIVE_INT
/* 16 */         .fieldOf("min_delay").forGetter(Music::minDelay), ExtraCodecs.NON_NEGATIVE_INT
/* 17 */         .fieldOf("max_delay").forGetter(Music::maxDelay), Codec.BOOL
/* 18 */         .optionalFieldOf("replace_current_music", Boolean.valueOf(false)).forGetter(Music::replaceCurrentMusic))
/* 19 */       .apply(i, Music::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\sounds\Music.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */