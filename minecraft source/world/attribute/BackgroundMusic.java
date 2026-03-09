/*    */ package net.minecraft.world.attribute;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.sounds.Music;
/*    */ import net.minecraft.sounds.Musics;
/*    */ 
/*    */ public final class BackgroundMusic extends Record {
/*    */   private final Optional<Music> defaultMusic;
/*    */   private final Optional<Music> creativeMusic;
/*    */   private final Optional<Music> underwaterMusic;
/*    */   
/* 12 */   public BackgroundMusic(Optional<Music> defaultMusic, Optional<Music> creativeMusic, Optional<Music> underwaterMusic) { this.defaultMusic = defaultMusic; this.creativeMusic = creativeMusic; this.underwaterMusic = underwaterMusic; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/BackgroundMusic;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/attribute/BackgroundMusic; } public Optional<Music> defaultMusic() { return this.defaultMusic; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/BackgroundMusic;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BackgroundMusic; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/BackgroundMusic;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/BackgroundMusic;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Music> creativeMusic() { return this.creativeMusic; } public Optional<Music> underwaterMusic() { return this.underwaterMusic; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final BackgroundMusic EMPTY = new BackgroundMusic(Optional.empty(), Optional.empty(), Optional.empty());
/* 18 */   public static final BackgroundMusic OVERWORLD = new BackgroundMusic(Optional.of(Musics.GAME), Optional.of(Musics.CREATIVE), Optional.empty());
/*    */   
/* 20 */   public static final Codec<BackgroundMusic> CODEC = RecordCodecBuilder.create(i -> i.group(Music.CODEC
/* 21 */         .optionalFieldOf("default").forGetter(BackgroundMusic::defaultMusic), Music.CODEC
/* 22 */         .optionalFieldOf("creative").forGetter(BackgroundMusic::creativeMusic), Music.CODEC
/* 23 */         .optionalFieldOf("underwater").forGetter(BackgroundMusic::underwaterMusic))
/* 24 */       .apply(i, BackgroundMusic::new));
/*    */ 
/*    */   
/* 27 */   public BackgroundMusic(Music music) { this(Optional.of(music), Optional.empty(), Optional.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public BackgroundMusic(Holder<SoundEvent> sound) { this(Musics.createGameMusic(sound)); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public BackgroundMusic withUnderwater(Music underwaterMusic) { return new BackgroundMusic(this.defaultMusic, this.creativeMusic, Optional.of(underwaterMusic)); }
/*    */ 
/*    */   
/*    */   public Optional<Music> select(boolean isCreative, boolean isUnderwater) {
/* 39 */     if (isUnderwater && this.underwaterMusic.isPresent()) {
/* 40 */       return this.underwaterMusic;
/*    */     }
/* 42 */     if (isCreative && this.creativeMusic.isPresent()) {
/* 43 */       return this.creativeMusic;
/*    */     }
/* 45 */     return this.defaultMusic;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\BackgroundMusic.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */