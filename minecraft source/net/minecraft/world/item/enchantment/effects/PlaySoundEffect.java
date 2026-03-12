/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class PlaySoundEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final List<Holder<SoundEvent>> soundEvents;
/*    */   private final FloatProvider volume;
/*    */   private final FloatProvider pitch;
/*    */   
/* 18 */   public PlaySoundEffect(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) { this.soundEvents = soundEvents; this.volume = volume; this.pitch = pitch; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect; } public List<Holder<SoundEvent>> soundEvents() { return this.soundEvents; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public FloatProvider volume() { return this.volume; } public FloatProvider pitch() { return this.pitch; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<PlaySoundEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 24 */         ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255)).fieldOf("sound").forGetter(PlaySoundEffect::soundEvents), 
/* 25 */         FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundEffect::volume), 
/* 26 */         FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundEffect::pitch))
/* 27 */       .apply(i, PlaySoundEffect::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 31 */     if (entity.isSilent()) {
/*    */       return;
/*    */     }
/* 34 */     RandomSource random = entity.getRandom();
/* 35 */     int index = Mth.clamp(enchantmentLevel - 1, 0, this.soundEvents.size() - 1);
/* 36 */     serverLevel.playSound(null, position.x(), position.y(), position.z(), (Holder)this.soundEvents.get(index), entity.getSoundSource(), this.volume.sample(random), this.pitch.sample(random));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public MapCodec<PlaySoundEffect> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\PlaySoundEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */