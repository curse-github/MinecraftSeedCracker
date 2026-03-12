/*    */ package net.minecraft.world.item;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class JukeboxSong extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   private final Component description;
/*    */   private final float lengthInSeconds;
/*    */   private final int comparatorOutput;
/*    */   
/* 22 */   public JukeboxSong(Holder<SoundEvent> soundEvent, Component description, float lengthInSeconds, int comparatorOutput) { this.soundEvent = soundEvent; this.description = description; this.lengthInSeconds = lengthInSeconds; this.comparatorOutput = comparatorOutput; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/JukeboxSong;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 22 */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxSong; } public Holder<SoundEvent> soundEvent() { return this.soundEvent; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/JukeboxSong;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxSong; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/JukeboxSong;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/JukeboxSong;
/* 22 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public float lengthInSeconds() { return this.lengthInSeconds; } public int comparatorOutput() { return this.comparatorOutput; }
/* 23 */   public static final Codec<JukeboxSong> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(SoundEvent.CODEC
/* 24 */         .fieldOf("sound_event").forGetter(JukeboxSong::soundEvent), ComponentSerialization.CODEC
/* 25 */         .fieldOf("description").forGetter(JukeboxSong::description), ExtraCodecs.POSITIVE_FLOAT
/* 26 */         .fieldOf("length_in_seconds").forGetter(JukeboxSong::lengthInSeconds), 
/* 27 */         ExtraCodecs.intRange(0, 15).fieldOf("comparator_output").forGetter(JukeboxSong::comparatorOutput))
/* 28 */       .apply(i, JukeboxSong::new));
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxSong> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, JukeboxSong::soundEvent, ComponentSerialization.STREAM_CODEC, JukeboxSong::description, ByteBufCodecs.FLOAT, JukeboxSong::lengthInSeconds, ByteBufCodecs.VAR_INT, JukeboxSong::comparatorOutput, JukeboxSong::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final Codec<Holder<JukeboxSong>> CODEC = RegistryFixedCodec.create(Registries.JUKEBOX_SONG);
/* 38 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<JukeboxSong>> STREAM_CODEC = ByteBufCodecs.holder(Registries.JUKEBOX_SONG, DIRECT_STREAM_CODEC);
/*    */   
/*    */   private static final int SONG_END_PADDING_TICKS = 20;
/*    */   
/* 42 */   public int lengthInTicks() { return Mth.ceil(this.lengthInSeconds * 20.0F); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean hasFinished(long ticksElapsed) { return (ticksElapsed >= (lengthInTicks() + 20)); }
/*    */ 
/*    */   
/*    */   public static Optional<Holder<JukeboxSong>> fromStack(HolderLookup.Provider registries, ItemStack stack) {
/* 50 */     JukeboxPlayable jukeboxPlayable = (JukeboxPlayable)stack.get(DataComponents.JUKEBOX_PLAYABLE);
/* 51 */     if (jukeboxPlayable != null) {
/* 52 */       return jukeboxPlayable.song().unwrap(registries);
/*    */     }
/* 54 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\JukeboxSong.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */