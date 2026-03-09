/*    */ package net.minecraft.world.item.consume_effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class PlaySoundConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final Holder<SoundEvent> sound;
/*    */   
/* 13 */   public PlaySoundConsumeEffect(Holder<SoundEvent> sound) { this.sound = sound; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect; } public Holder<SoundEvent> sound() { return this.sound; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/PlaySoundConsumeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 14 */   public static final MapCodec<PlaySoundConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SoundEvent.CODEC
/* 15 */         .fieldOf("sound").forGetter(PlaySoundConsumeEffect::sound))
/* 16 */       .apply(i, PlaySoundConsumeEffect::new));
/* 17 */   public static final StreamCodec<RegistryFriendlyByteBuf, PlaySoundConsumeEffect> STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, PlaySoundConsumeEffect::sound, PlaySoundConsumeEffect::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public ConsumeEffect.Type<PlaySoundConsumeEffect> getType() { return ConsumeEffect.Type.PLAY_SOUND; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean apply(Level level, ItemStack stack, LivingEntity user) {
/* 29 */     level.playSound(null, user.blockPosition(), (SoundEvent)this.sound.value(), user.getSoundSource(), 1.0F, 1.0F);
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\consume_effects\PlaySoundConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */