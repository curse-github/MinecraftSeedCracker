/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ public class MobEffect
/*     */   implements FeatureElement
/*     */ {
/*  40 */   public static final Codec<Holder<MobEffect>> CODEC = BuiltInRegistries.MOB_EFFECT.holderByNameCodec();
/*  41 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MobEffect>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT); private final Map<Holder<Attribute>, AttributeTemplate> attributeModifiers; private final MobEffectCategory category; private final int color; private final Function<MobEffectInstance, ParticleOptions> particleFactory;
/*     */   private String descriptionId;
/*  43 */   private static final int AMBIENT_ALPHA = Mth.floor(38.25F); private int blendInDurationTicks; private int blendOutDurationTicks; private int blendOutAdvanceTicks; private Optional<SoundEvent> soundOnAdded; private FeatureFlagSet requiredFeatures; protected MobEffect(MobEffectCategory category, int color) {
/*  44 */     this.attributeModifiers = new Object2ObjectOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     this.soundOnAdded = Optional.empty();
/*  54 */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */ 
/*     */     
/*  57 */     this.category = category;
/*  58 */     this.color = color;
/*     */     
/*  60 */     this.particleFactory = (effectInstance -> {
/*  61 */         int alpha = effectInstance.isAmbient() ? AMBIENT_ALPHA : 255;
/*  62 */         return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(alpha, color));
/*     */       }); } protected MobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) {
/*     */     this.attributeModifiers = new Object2ObjectOpenHashMap();
/*     */     this.soundOnAdded = Optional.empty();
/*     */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*  67 */     this.category = category;
/*  68 */     this.color = color;
/*  69 */     this.particleFactory = (ignored -> particleOptions);
/*     */   }
/*     */ 
/*     */   
/*  73 */   public int getBlendInDurationTicks() { return this.blendInDurationTicks; }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public int getBlendOutDurationTicks() { return this.blendOutDurationTicks; }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public int getBlendOutAdvanceTicks() { return this.blendOutAdvanceTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) { return true; }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void applyInstantenousEffect(ServerLevel level, Entity source, Entity owner, LivingEntity mob, int amplification, double scale) { applyEffectTick(level, mob, amplification); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEffectStarted(LivingEntity mob, int amplifier) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public void onEffectAdded(LivingEntity mob, int amplifier) { this.soundOnAdded.ifPresent(soundEvent -> mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), soundEvent, mob.getSoundSource(), 1.0F, 1.0F)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {}
/*     */ 
/*     */   
/*     */   public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {}
/*     */ 
/*     */   
/* 132 */   public boolean isInstantenous() { return false; }
/*     */ 
/*     */   
/*     */   protected String getOrCreateDescriptionId() {
/* 136 */     if (this.descriptionId == null) {
/* 137 */       this.descriptionId = Util.makeDescriptionId("effect", BuiltInRegistries.MOB_EFFECT.getKey(this));
/*     */     }
/* 139 */     return this.descriptionId;
/*     */   }
/*     */ 
/*     */   
/* 143 */   public String getDescriptionId() { return getOrCreateDescriptionId(); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public Component getDisplayName() { return Component.translatable(getDescriptionId()); }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public MobEffectCategory getCategory() { return this.category; }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public int getColor() { return this.color; }
/*     */ 
/*     */   
/*     */   public MobEffect addAttributeModifier(Holder<Attribute> attribute, Identifier id, double amount, AttributeModifier.Operation operation) {
/* 159 */     this.attributeModifiers.put(attribute, new AttributeTemplate(id, amount, operation));
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 164 */   public MobEffect setBlendDuration(int ticks) { return setBlendDuration(ticks, ticks, ticks); }
/*     */ 
/*     */   
/*     */   public MobEffect setBlendDuration(int inTicks, int outTicks, int outAdvanceTicks) {
/* 168 */     this.blendInDurationTicks = inTicks;
/* 169 */     this.blendOutDurationTicks = outTicks;
/* 170 */     this.blendOutAdvanceTicks = outAdvanceTicks;
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public void createModifiers(int amplifier, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 175 */     this.attributeModifiers.forEach((attribute, template) -> 
/* 176 */         consumer.accept(attribute, template.create(amplifier)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttributeModifiers(AttributeMap attributes) {
/* 181 */     for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
/* 182 */       AttributeInstance attribute = attributes.getInstance((Holder)entry.getKey());
/*     */       
/* 184 */       if (attribute != null) {
/* 185 */         attribute.removeModifier(((AttributeTemplate)entry.getValue()).id());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAttributeModifiers(AttributeMap attributes, int amplifier) {
/* 191 */     for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
/* 192 */       AttributeInstance attribute = attributes.getInstance((Holder)entry.getKey());
/*     */       
/* 194 */       if (attribute != null) {
/* 195 */         attribute.removeModifier(((AttributeTemplate)entry.getValue()).id());
/* 196 */         attribute.addPermanentModifier(((AttributeTemplate)entry.getValue()).create(amplifier));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 202 */   public boolean isBeneficial() { return (this.category == MobEffectCategory.BENEFICIAL); }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public ParticleOptions createParticleOptions(MobEffectInstance mobEffectInstance) { return (ParticleOptions)this.particleFactory.apply(mobEffectInstance); }
/*     */ 
/*     */   
/*     */   public MobEffect withSoundOnAdded(SoundEvent soundEvent) {
/* 210 */     this.soundOnAdded = Optional.of(soundEvent);
/* 211 */     return this;
/*     */   }
/*     */   
/*     */   public MobEffect requiredFeatures(FeatureFlag... flags) {
/* 215 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public FeatureFlagSet requiredFeatures() { return this.requiredFeatures; }
/*     */   private static final class AttributeTemplate extends Record { private final Identifier id; private final double amount; private final AttributeModifier.Operation operation;
/*     */     
/* 224 */     private AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) { this.id = id; this.amount = amount; this.operation = operation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 224 */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;
/* 224 */       //   0	8	1	o	Ljava/lang/Object; } public double amount() { return this.amount; } public AttributeModifier.Operation operation() { return this.operation; }
/*     */     
/* 226 */     public AttributeModifier create(int amplifier) { return new AttributeModifier(this.id, this.amount * (amplifier + 1), this.operation); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\MobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */