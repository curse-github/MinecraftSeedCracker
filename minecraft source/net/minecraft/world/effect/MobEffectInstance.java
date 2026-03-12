/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MobEffectInstance extends Object implements Comparable<MobEffectInstance> {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int INFINITE_DURATION = -1;
/*     */   
/*     */   public static final int MIN_AMPLIFIER = 0;
/*     */   public static final int MAX_AMPLIFIER = 255;
/*  34 */   public static final Codec<MobEffectInstance> CODEC = RecordCodecBuilder.create(i -> i.group(MobEffect.CODEC
/*  35 */         .fieldOf("id").forGetter(MobEffectInstance::getEffect), Details.MAP_CODEC
/*  36 */         .forGetter(MobEffectInstance::asDetails))
/*  37 */       .apply(i, MobEffectInstance::new));
/*     */   
/*  39 */   public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectInstance> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC, MobEffectInstance::getEffect, Details.STREAM_CODEC, MobEffectInstance::asDetails, MobEffectInstance::new);
/*     */   
/*     */   private final Holder<MobEffect> effect;
/*     */   
/*     */   private int duration;
/*     */   
/*     */   private int amplifier;
/*     */   
/*     */   private boolean ambient;
/*     */   
/*     */   private boolean visible;
/*     */   
/*     */   private boolean showIcon;
/*     */   
/*     */   private MobEffectInstance hiddenEffect;
/*     */   
/*     */   private final BlendState blendState;
/*     */   
/*  57 */   public MobEffectInstance(Holder<MobEffect> effect) { this(effect, 0, 0); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public MobEffectInstance(Holder<MobEffect> effect, int duration) { this(effect, duration, 0); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier) { this(effect, duration, amplifier, false, true); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible) { this(effect, duration, amplifier, ambient, visible, visible); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) { this(effect, duration, amplifier, ambient, visible, showIcon, null); }
/*     */   
/*     */   public MobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, MobEffectInstance hiddenEffect) {
/*     */     this.blendState = new BlendState();
/*  77 */     this.effect = effect;
/*  78 */     this.duration = duration;
/*  79 */     this.amplifier = Mth.clamp(amplifier, 0, 255);
/*  80 */     this.ambient = ambient;
/*  81 */     this.visible = visible;
/*  82 */     this.showIcon = showIcon;
/*  83 */     this.hiddenEffect = hiddenEffect;
/*     */   }
/*     */   public MobEffectInstance(MobEffectInstance copy) {
/*     */     this.blendState = new BlendState();
/*  87 */     this.effect = copy.effect;
/*  88 */     setDetailsFrom(copy);
/*     */   }
/*     */   
/*     */   private MobEffectInstance(Holder<MobEffect> effect, Details details) {
/*  92 */     this(effect, details
/*     */         
/*  94 */         .duration(), details
/*  95 */         .amplifier(), details
/*  96 */         .ambient(), details
/*  97 */         .showParticles(), details
/*  98 */         .showIcon(), (MobEffectInstance)details
/*  99 */         .hiddenEffect().map(hidden -> new MobEffectInstance(effect, hidden)).orElse(null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   private Details asDetails() { return new Details(getAmplifier(), getDuration(), isAmbient(), isVisible(), showIcon(), Optional.ofNullable(this.hiddenEffect).map(MobEffectInstance::asDetails)); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public float getBlendFactor(LivingEntity livingEntity, float partialTickTime) { return this.blendState.getFactor(livingEntity, partialTickTime); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public ParticleOptions getParticleOptions() { return ((MobEffect)this.effect.value()).createParticleOptions(this); }
/*     */ 
/*     */   
/*     */   void setDetailsFrom(MobEffectInstance copy) {
/* 116 */     this.duration = copy.duration;
/* 117 */     this.amplifier = copy.amplifier;
/* 118 */     this.ambient = copy.ambient;
/* 119 */     this.visible = copy.visible;
/* 120 */     this.showIcon = copy.showIcon;
/*     */   }
/*     */   
/*     */   public boolean update(MobEffectInstance takeOver) {
/* 124 */     if (!this.effect.equals(takeOver.effect)) {
/* 125 */       LOGGER.warn("This method should only be called for matching effects!");
/*     */     }
/*     */     
/* 128 */     boolean changed = false;
/* 129 */     if (takeOver.amplifier > this.amplifier) {
/* 130 */       if (takeOver.isShorterDurationThan(this)) {
/* 131 */         MobEffectInstance prevHiddenEffect = this.hiddenEffect;
/* 132 */         this.hiddenEffect = new MobEffectInstance(this);
/* 133 */         this.hiddenEffect.hiddenEffect = prevHiddenEffect;
/*     */       } 
/* 135 */       this.amplifier = takeOver.amplifier;
/* 136 */       this.duration = takeOver.duration;
/* 137 */       changed = true;
/* 138 */     } else if (isShorterDurationThan(takeOver)) {
/* 139 */       if (takeOver.amplifier == this.amplifier) {
/* 140 */         this.duration = takeOver.duration;
/* 141 */         changed = true;
/*     */       }
/* 143 */       else if (this.hiddenEffect == null) {
/* 144 */         this.hiddenEffect = new MobEffectInstance(takeOver);
/*     */       } else {
/* 146 */         this.hiddenEffect.update(takeOver);
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     if ((!takeOver.ambient && this.ambient) || changed) {
/* 151 */       this.ambient = takeOver.ambient;
/* 152 */       changed = true;
/*     */     } 
/* 154 */     if (takeOver.visible != this.visible) {
/* 155 */       this.visible = takeOver.visible;
/* 156 */       changed = true;
/*     */     } 
/* 158 */     if (takeOver.showIcon != this.showIcon) {
/* 159 */       this.showIcon = takeOver.showIcon;
/* 160 */       changed = true;
/*     */     } 
/*     */     
/* 163 */     return changed;
/*     */   }
/*     */ 
/*     */   
/* 167 */   private boolean isShorterDurationThan(MobEffectInstance other) { return (!isInfiniteDuration() && (this.duration < other.duration || other.isInfiniteDuration())); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public boolean isInfiniteDuration() { return (this.duration == -1); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public boolean endsWithin(int ticks) { return (!isInfiniteDuration() && this.duration <= ticks); }
/*     */ 
/*     */   
/*     */   public MobEffectInstance withScaledDuration(float scale) {
/* 179 */     MobEffectInstance copy = new MobEffectInstance(this);
/* 180 */     copy.duration = copy.mapDuration(duration -> Math.max(Mth.floor(duration * scale), 1));
/* 181 */     return copy;
/*     */   }
/*     */   
/*     */   public int mapDuration(Int2IntFunction mapper) {
/* 185 */     if (isInfiniteDuration() || this.duration == 0) {
/* 186 */       return this.duration;
/*     */     }
/* 188 */     return mapper.applyAsInt(this.duration);
/*     */   }
/*     */ 
/*     */   
/* 192 */   public Holder<MobEffect> getEffect() { return this.effect; }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public int getDuration() { return this.duration; }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public int getAmplifier() { return this.amplifier; }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public boolean isAmbient() { return this.ambient; }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public boolean isVisible() { return this.visible; }
/*     */ 
/*     */ 
/*     */   
/* 212 */   public boolean showIcon() { return this.showIcon; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tickServer(ServerLevel serverLevel, LivingEntity target, Runnable onEffectUpdate) {
/* 221 */     if (!hasRemainingDuration()) {
/* 222 */       return false;
/*     */     }
/* 224 */     int tickCount = isInfiniteDuration() ? target.tickCount : this.duration;
/* 225 */     if (((MobEffect)this.effect.value()).shouldApplyEffectTickThisTick(tickCount, this.amplifier) && !((MobEffect)this.effect.value()).applyEffectTick(serverLevel, target, this.amplifier)) {
/* 226 */       return false;
/*     */     }
/* 228 */     tickDownDuration();
/* 229 */     if (downgradeToHiddenEffect()) {
/* 230 */       onEffectUpdate.run();
/*     */     }
/* 232 */     return hasRemainingDuration();
/*     */   }
/*     */   
/*     */   public void tickClient() {
/* 236 */     if (hasRemainingDuration()) {
/* 237 */       tickDownDuration();
/* 238 */       downgradeToHiddenEffect();
/*     */     } 
/* 240 */     this.blendState.tick(this);
/*     */   }
/*     */ 
/*     */   
/* 244 */   private boolean hasRemainingDuration() { return (isInfiniteDuration() || this.duration > 0); }
/*     */ 
/*     */   
/*     */   private void tickDownDuration() {
/* 248 */     if (this.hiddenEffect != null) {
/* 249 */       this.hiddenEffect.tickDownDuration();
/*     */     }
/* 251 */     this.duration = mapDuration(d -> d - 1);
/*     */   }
/*     */   
/*     */   private boolean downgradeToHiddenEffect() {
/* 255 */     if (this.duration == 0 && this.hiddenEffect != null) {
/* 256 */       setDetailsFrom(this.hiddenEffect);
/* 257 */       this.hiddenEffect = this.hiddenEffect.hiddenEffect;
/* 258 */       return true;
/*     */     } 
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 264 */   public void onEffectStarted(LivingEntity mob) { ((MobEffect)this.effect.value()).onEffectStarted(mob, this.amplifier); }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public void onMobRemoved(ServerLevel level, LivingEntity mob, Entity.RemovalReason reason) { ((MobEffect)this.effect.value()).onMobRemoved(level, mob, this.amplifier, reason); }
/*     */ 
/*     */ 
/*     */   
/* 272 */   public void onMobHurt(ServerLevel level, LivingEntity mob, DamageSource source, float damage) { ((MobEffect)this.effect.value()).onMobHurt(level, mob, this.amplifier, source, damage); }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public String getDescriptionId() { return ((MobEffect)this.effect.value()).getDescriptionId(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     String result;
/* 282 */     if (this.amplifier > 0) {
/* 283 */       result = getDescriptionId() + " x " + getDescriptionId() + ", Duration: " + this.amplifier + 1;
/*     */     } else {
/* 285 */       result = getDescriptionId() + ", Duration: " + getDescriptionId();
/*     */     } 
/* 287 */     if (!this.visible) {
/* 288 */       result = result + ", Particles: false";
/*     */     }
/* 290 */     if (!this.showIcon) {
/* 291 */       result = result + ", Show Icon: false";
/*     */     }
/*     */     
/* 294 */     return result;
/*     */   }
/*     */   
/*     */   private String describeDuration() {
/* 298 */     if (isInfiniteDuration()) {
/* 299 */       return "infinite";
/*     */     }
/* 301 */     return Integer.toString(this.duration);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 306 */     if (this == o) {
/* 307 */       return true;
/*     */     }
/* 309 */     if (o instanceof MobEffectInstance) { MobEffectInstance that = (MobEffectInstance)o;
/* 310 */       return (this.duration == that.duration && this.amplifier == that.amplifier && this.ambient == that.ambient && this.visible == that.visible && this.showIcon == that.showIcon && this.effect.equals(that.effect)); }
/*     */     
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 317 */     result = this.effect.hashCode();
/* 318 */     result = 31 * result + this.duration;
/* 319 */     result = 31 * result + this.amplifier;
/* 320 */     result = 31 * result + (this.ambient ? 1 : 0);
/* 321 */     result = 31 * result + (this.visible ? 1 : 0);
/* 322 */     return 31 * result + (this.showIcon ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(MobEffectInstance o) {
/* 328 */     int updateCutOff = 32147;
/* 329 */     if ((getDuration() > 32147 && o.getDuration() > 32147) || (isAmbient() && o.isAmbient()))
/*     */     {
/* 331 */       return ComparisonChain.start()
/* 332 */         .compare(Boolean.valueOf(isAmbient()), Boolean.valueOf(o.isAmbient()))
/* 333 */         .compare(((MobEffect)getEffect().value()).getColor(), ((MobEffect)o.getEffect().value()).getColor())
/* 334 */         .result();
/*     */     }
/* 336 */     return ComparisonChain.start()
/* 337 */       .compareFalseFirst(isAmbient(), o.isAmbient())
/* 338 */       .compareFalseFirst(isInfiniteDuration(), o.isInfiniteDuration())
/* 339 */       .compare(getDuration(), o.getDuration())
/* 340 */       .compare(((MobEffect)getEffect().value()).getColor(), ((MobEffect)o.getEffect().value()).getColor())
/* 341 */       .result();
/*     */   }
/*     */ 
/*     */   
/* 345 */   public void onEffectAdded(LivingEntity livingEntity) { ((MobEffect)this.effect.value()).onEffectAdded(livingEntity, this.amplifier); }
/*     */   private static final class Details extends Record { private final int amplifier; private final int duration; private final boolean ambient; private final boolean showParticles; private final boolean showIcon; private final Optional<Details> hiddenEffect;
/*     */     
/* 348 */     private Details(int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, Optional<Details> hiddenEffect) { this.amplifier = amplifier; this.duration = duration; this.ambient = ambient; this.showParticles = showParticles; this.showIcon = showIcon; this.hiddenEffect = hiddenEffect; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/effect/MobEffectInstance$Details;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 348 */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details; } public int amplifier() { return this.amplifier; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/effect/MobEffectInstance$Details;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/effect/MobEffectInstance$Details;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/* 348 */       //   0	8	1	o	Ljava/lang/Object; } public int duration() { return this.duration; } public boolean ambient() { return this.ambient; } public boolean showParticles() { return this.showParticles; } public boolean showIcon() { return this.showIcon; } public Optional<Details> hiddenEffect() { return this.hiddenEffect; }
/* 349 */     public static final MapCodec<Details> MAP_CODEC = MapCodec.recursive("MobEffectInstance.Details", codec -> RecordCodecBuilder.mapCodec(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     public static final StreamCodec<ByteBuf, Details> STREAM_CODEC = StreamCodec.recursive(subCodec -> StreamCodec.composite(ByteBufCodecs.VAR_INT, Details::amplifier, ByteBufCodecs.VAR_INT, Details::duration, ByteBufCodecs.BOOL, Details::ambient, ByteBufCodecs.BOOL, Details::showParticles, ByteBufCodecs.BOOL, Details::showIcon, subCodec
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 365 */           .apply(ByteBufCodecs::optional), Details::hiddenEffect, Details::new));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 370 */     private static Details create(int amplifier, int duration, boolean ambient, boolean showParticles, Optional<Boolean> showIcon, Optional<Details> hiddenEffect) { return new Details(amplifier, duration, ambient, showParticles, ((Boolean)showIcon.orElse(Boolean.valueOf(showParticles))).booleanValue(), hiddenEffect); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 375 */   public boolean is(Holder<MobEffect> effect) { return this.effect.equals(effect); }
/*     */ 
/*     */ 
/*     */   
/* 379 */   public void copyBlendState(MobEffectInstance instance) { this.blendState.copyFrom(instance.blendState); }
/*     */ 
/*     */ 
/*     */   
/* 383 */   public void skipBlending() { this.blendState.setImmediate(this); }
/*     */   
/*     */   private static class BlendState
/*     */   {
/*     */     private float factor;
/*     */     private float factorPreviousFrame;
/*     */     
/*     */     public void setImmediate(MobEffectInstance instance) {
/* 391 */       this.factor = hasEffect(instance) ? 1.0F : 0.0F;
/* 392 */       this.factorPreviousFrame = this.factor;
/*     */     }
/*     */     
/*     */     public void copyFrom(BlendState other) {
/* 396 */       this.factor = other.factor;
/* 397 */       this.factorPreviousFrame = other.factorPreviousFrame;
/*     */     }
/*     */     
/*     */     public void tick(MobEffectInstance instance) {
/* 401 */       this.factorPreviousFrame = this.factor;
/*     */       
/* 403 */       boolean hasEffect = hasEffect(instance);
/* 404 */       float target = hasEffect ? 1.0F : 0.0F;
/* 405 */       if (this.factor == target) {
/*     */         return;
/*     */       }
/*     */       
/* 409 */       MobEffect effect = (MobEffect)instance.getEffect().value();
/* 410 */       int blendDuration = hasEffect ? effect.getBlendInDurationTicks() : effect.getBlendOutDurationTicks();
/* 411 */       if (blendDuration == 0) {
/* 412 */         this.factor = target;
/*     */       } else {
/* 414 */         float maxDeltaPerTick = 1.0F / blendDuration;
/* 415 */         this.factor += Mth.clamp(target - this.factor, -maxDeltaPerTick, maxDeltaPerTick);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 420 */     private static boolean hasEffect(MobEffectInstance instance) { return !instance.endsWithin(((MobEffect)instance.getEffect().value()).getBlendOutAdvanceTicks()); }
/*     */ 
/*     */     
/*     */     public float getFactor(LivingEntity livingEntity, float partialTickTime) {
/* 424 */       if (livingEntity.isRemoved())
/*     */       {
/*     */ 
/*     */         
/* 428 */         this.factorPreviousFrame = this.factor;
/*     */       }
/*     */       
/* 431 */       return Mth.lerp(partialTickTime, this.factorPreviousFrame, this.factor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\MobEffectInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */