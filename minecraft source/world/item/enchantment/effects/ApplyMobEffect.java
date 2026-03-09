/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class ApplyMobEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final HolderSet<MobEffect> toApply;
/*    */   private final LevelBasedValue minDuration;
/*    */   private final LevelBasedValue maxDuration;
/*    */   private final LevelBasedValue minAmplifier;
/*    */   private final LevelBasedValue maxAmplifier;
/*    */   
/* 23 */   public ApplyMobEffect(HolderSet<MobEffect> toApply, LevelBasedValue minDuration, LevelBasedValue maxDuration, LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier) { this.toApply = toApply; this.minDuration = minDuration; this.maxDuration = maxDuration; this.minAmplifier = minAmplifier; this.maxAmplifier = maxAmplifier; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect; } public HolderSet<MobEffect> toApply() { return this.toApply; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;
/* 23 */     //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue minDuration() { return this.minDuration; } public LevelBasedValue maxDuration() { return this.maxDuration; } public LevelBasedValue minAmplifier() { return this.minAmplifier; } public LevelBasedValue maxAmplifier() { return this.maxAmplifier; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final MapCodec<ApplyMobEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 32 */         RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffect::toApply), LevelBasedValue.CODEC
/* 33 */         .fieldOf("min_duration").forGetter(ApplyMobEffect::minDuration), LevelBasedValue.CODEC
/* 34 */         .fieldOf("max_duration").forGetter(ApplyMobEffect::maxDuration), LevelBasedValue.CODEC
/* 35 */         .fieldOf("min_amplifier").forGetter(ApplyMobEffect::minAmplifier), LevelBasedValue.CODEC
/* 36 */         .fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxAmplifier))
/* 37 */       .apply(i, ApplyMobEffect::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 41 */     if (entity instanceof LivingEntity) { LivingEntity living = (LivingEntity)entity;
/* 42 */       RandomSource random = living.getRandom();
/* 43 */       Optional<Holder<MobEffect>> selected = this.toApply.getRandomElement(random);
/* 44 */       if (selected.isPresent()) {
/* 45 */         int ticks = Math.round(Mth.randomBetween(random, this.minDuration.calculate(enchantmentLevel), this.maxDuration.calculate(enchantmentLevel)) * 20.0F);
/* 46 */         int amplifier = Math.max(0, Math.round(Mth.randomBetween(random, this.minAmplifier.calculate(enchantmentLevel), this.maxAmplifier.calculate(enchantmentLevel))));
/* 47 */         living.addEffect(new MobEffectInstance((Holder)selected.get(), ticks, amplifier));
/*    */       }  }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public MapCodec<ApplyMobEffect> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ApplyMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */