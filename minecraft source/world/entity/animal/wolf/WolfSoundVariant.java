/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class WolfSoundVariant extends Record {
/*    */   private final Holder<SoundEvent> ambientSound;
/*    */   private final Holder<SoundEvent> deathSound;
/*    */   private final Holder<SoundEvent> growlSound;
/*    */   
/* 13 */   public WolfSoundVariant(Holder<SoundEvent> ambientSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> growlSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> pantSound, Holder<SoundEvent> whineSound) { this.ambientSound = ambientSound; this.deathSound = deathSound; this.growlSound = growlSound; this.hurtSound = hurtSound; this.pantSound = pantSound; this.whineSound = whineSound; } private final Holder<SoundEvent> hurtSound; private final Holder<SoundEvent> pantSound; private final Holder<SoundEvent> whineSound; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<SoundEvent> ambientSound() { return this.ambientSound; } public Holder<SoundEvent> deathSound() { return this.deathSound; } public Holder<SoundEvent> growlSound() { return this.growlSound; } public Holder<SoundEvent> hurtSound() { return this.hurtSound; } public Holder<SoundEvent> pantSound() { return this.pantSound; } public Holder<SoundEvent> whineSound() { return this.whineSound; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final Codec<WolfSoundVariant> DIRECT_CODEC = getWolfSoundVariantCodec();
/* 22 */   public static final Codec<WolfSoundVariant> NETWORK_CODEC = getWolfSoundVariantCodec();
/* 23 */   public static final Codec<Holder<WolfSoundVariant>> CODEC = RegistryFixedCodec.create(Registries.WOLF_SOUND_VARIANT);
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WolfSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WOLF_SOUND_VARIANT);
/*    */ 
/*    */   
/* 27 */   private static Codec<WolfSoundVariant> getWolfSoundVariantCodec() { return RecordCodecBuilder.create(i -> i.group(SoundEvent.CODEC
/* 28 */           .fieldOf("ambient_sound").forGetter(WolfSoundVariant::ambientSound), SoundEvent.CODEC
/* 29 */           .fieldOf("death_sound").forGetter(WolfSoundVariant::deathSound), SoundEvent.CODEC
/* 30 */           .fieldOf("growl_sound").forGetter(WolfSoundVariant::growlSound), SoundEvent.CODEC
/* 31 */           .fieldOf("hurt_sound").forGetter(WolfSoundVariant::hurtSound), SoundEvent.CODEC
/* 32 */           .fieldOf("pant_sound").forGetter(WolfSoundVariant::pantSound), SoundEvent.CODEC
/* 33 */           .fieldOf("whine_sound").forGetter(WolfSoundVariant::whineSound))
/* 34 */         .apply(i, WolfSoundVariant::new)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\wolf\WolfSoundVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */