/*    */ package net.minecraft.world.item.consume_effects;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ApplyStatusEffectsConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final List<MobEffectInstance> effects;
/*    */   private final float probability;
/*    */   
/* 16 */   public ApplyStatusEffectsConsumeEffect(List<MobEffectInstance> effects, float probability) { this.effects = effects; this.probability = probability; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect; } public List<MobEffectInstance> effects() { return this.effects; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/ApplyStatusEffectsConsumeEffect;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public float probability() { return this.probability; }
/* 17 */   public static final MapCodec<ApplyStatusEffectsConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(MobEffectInstance.CODEC
/* 18 */         .listOf().fieldOf("effects").forGetter(ApplyStatusEffectsConsumeEffect::effects), 
/* 19 */         Codec.floatRange(0.0F, 1.0F).optionalFieldOf("probability", Float.valueOf(1.0F)).forGetter(ApplyStatusEffectsConsumeEffect::probability))
/* 20 */       .apply(i, ApplyStatusEffectsConsumeEffect::new));
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, ApplyStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.composite(MobEffectInstance.STREAM_CODEC
/* 23 */       .apply(ByteBufCodecs.list()), ApplyStatusEffectsConsumeEffect::effects, ByteBufCodecs.FLOAT, ApplyStatusEffectsConsumeEffect::probability, ApplyStatusEffectsConsumeEffect::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public ApplyStatusEffectsConsumeEffect(MobEffectInstance effect, float probability) { this(List.of(effect), probability); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public ApplyStatusEffectsConsumeEffect(List<MobEffectInstance> effects) { this(effects, 1.0F); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public ApplyStatusEffectsConsumeEffect(MobEffectInstance effect) { this(effect, 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public ConsumeEffect.Type<ApplyStatusEffectsConsumeEffect> getType() { return ConsumeEffect.Type.APPLY_EFFECTS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean apply(Level level, ItemStack stack, LivingEntity user) {
/* 47 */     if (user.getRandom().nextFloat() >= this.probability) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     boolean anyApplied = false;
/* 52 */     for (MobEffectInstance effect : this.effects) {
/* 53 */       if (user.addEffect(new MobEffectInstance(effect))) {
/* 54 */         anyApplied = true;
/*    */       }
/*    */     } 
/*    */     
/* 58 */     return anyApplied;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\consume_effects\ApplyStatusEffectsConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */