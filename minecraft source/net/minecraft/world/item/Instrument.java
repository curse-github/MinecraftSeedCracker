/*    */ package net.minecraft.world.item;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Instrument extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   private final float useDuration;
/*    */   
/* 16 */   public Instrument(Holder<SoundEvent> soundEvent, float useDuration, float range, Component description) { this.soundEvent = soundEvent; this.useDuration = useDuration; this.range = range; this.description = description; } private final float range; private final Component description; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/Instrument;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/Instrument; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/Instrument;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/Instrument; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/Instrument;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/Instrument;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<SoundEvent> soundEvent() { return this.soundEvent; } public float useDuration() { return this.useDuration; } public float range() { return this.range; } public Component description() { return this.description; }
/* 17 */   public static final Codec<Instrument> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(SoundEvent.CODEC
/* 18 */         .fieldOf("sound_event").forGetter(Instrument::soundEvent), ExtraCodecs.POSITIVE_FLOAT
/* 19 */         .fieldOf("use_duration").forGetter(Instrument::useDuration), ExtraCodecs.POSITIVE_FLOAT
/* 20 */         .fieldOf("range").forGetter(Instrument::range), ComponentSerialization.CODEC
/* 21 */         .fieldOf("description").forGetter(Instrument::description))
/* 22 */       .apply(i, Instrument::new));
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, Instrument> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, Instrument::soundEvent, ByteBufCodecs.FLOAT, Instrument::useDuration, ByteBufCodecs.FLOAT, Instrument::range, ComponentSerialization.STREAM_CODEC, Instrument::description, Instrument::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final Codec<Holder<Instrument>> CODEC = RegistryFileCodec.create(Registries.INSTRUMENT, DIRECT_CODEC);
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Instrument>> STREAM_CODEC = ByteBufCodecs.holder(Registries.INSTRUMENT, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\Instrument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */