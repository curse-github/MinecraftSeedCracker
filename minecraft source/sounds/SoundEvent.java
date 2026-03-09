/*    */ package net.minecraft.sounds;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class SoundEvent extends Record {
/*    */   private final Identifier location;
/*    */   private final Optional<Float> fixedRange;
/*    */   
/* 16 */   public SoundEvent(Identifier location, Optional<Float> fixedRange) { this.location = location; this.fixedRange = fixedRange; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/sounds/SoundEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/sounds/SoundEvent; } public Identifier location() { return this.location; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/sounds/SoundEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/SoundEvent; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/sounds/SoundEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/sounds/SoundEvent;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Float> fixedRange() { return this.fixedRange; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final Codec<SoundEvent> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Identifier.CODEC
/* 25 */         .fieldOf("sound_id").forGetter(SoundEvent::location), Codec.FLOAT
/* 26 */         .lenientOptionalFieldOf("range").forGetter(SoundEvent::fixedRange))
/* 27 */       .apply(i, SoundEvent::create));
/*    */   
/* 29 */   public static final Codec<Holder<SoundEvent>> CODEC = RegistryFileCodec.create(Registries.SOUND_EVENT, DIRECT_CODEC);
/*    */   
/* 31 */   public static final StreamCodec<ByteBuf, SoundEvent> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, SoundEvent::location, ByteBufCodecs.FLOAT
/*    */       
/* 33 */       .apply(ByteBufCodecs::optional), SoundEvent::fixedRange, SoundEvent::create);
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SoundEvent>> STREAM_CODEC = ByteBufCodecs.holder(Registries.SOUND_EVENT, DIRECT_STREAM_CODEC);
/*    */ 
/*    */   
/* 40 */   private static SoundEvent create(Identifier location, Optional<Float> range) { return (SoundEvent)range.map(r -> createFixedRangeEvent(location, r.floatValue())).orElseGet(() -> createVariableRangeEvent(location)); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public static SoundEvent createVariableRangeEvent(Identifier location) { return new SoundEvent(location, Optional.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public static SoundEvent createFixedRangeEvent(Identifier location, float range) { return new SoundEvent(location, Optional.of(Float.valueOf(range))); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public float getRange(float volume) { return ((Float)this.fixedRange.orElse(Float.valueOf((volume > 1.0F) ? (16.0F * volume) : 16.0F))).floatValue(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\sounds\SoundEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */