/*     */ package net.minecraft.world.item.component;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*     */ import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public final class Consumable extends Record {
/*     */   private final float consumeSeconds;
/*     */   private final ItemUseAnimation animation;
/*     */   private final Holder<SoundEvent> sound;
/*     */   private final boolean hasConsumeParticles;
/*     */   
/*  34 */   public Consumable(float consumeSeconds, ItemUseAnimation animation, Holder<SoundEvent> sound, boolean hasConsumeParticles, List<ConsumeEffect> onConsumeEffects) { this.consumeSeconds = consumeSeconds; this.animation = animation; this.sound = sound; this.hasConsumeParticles = hasConsumeParticles; this.onConsumeEffects = onConsumeEffects; } private final List<ConsumeEffect> onConsumeEffects; public static final float DEFAULT_CONSUME_SECONDS = 1.6F; private static final int CONSUME_EFFECTS_INTERVAL = 4; private static final float CONSUME_EFFECTS_START_FRACTION = 0.21875F; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Consumable;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/Consumable; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Consumable;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/Consumable; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Consumable;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/Consumable;
/*  34 */     //   0	8	1	o	Ljava/lang/Object; } public float consumeSeconds() { return this.consumeSeconds; } public ItemUseAnimation animation() { return this.animation; } public Holder<SoundEvent> sound() { return this.sound; } public boolean hasConsumeParticles() { return this.hasConsumeParticles; } public List<ConsumeEffect> onConsumeEffects() { return this.onConsumeEffects; }
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
/*  46 */   public static final Codec<Consumable> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_FLOAT
/*  47 */         .optionalFieldOf("consume_seconds", Float.valueOf(1.6F)).forGetter(Consumable::consumeSeconds), ItemUseAnimation.CODEC
/*  48 */         .optionalFieldOf("animation", ItemUseAnimation.EAT).forGetter(Consumable::animation), SoundEvent.CODEC
/*  49 */         .optionalFieldOf("sound", SoundEvents.GENERIC_EAT).forGetter(Consumable::sound), Codec.BOOL
/*  50 */         .optionalFieldOf("has_consume_particles", Boolean.valueOf(true)).forGetter(Consumable::hasConsumeParticles), ConsumeEffect.CODEC
/*  51 */         .listOf().optionalFieldOf("on_consume_effects", List.of()).forGetter(Consumable::onConsumeEffects))
/*  52 */       .apply(i, Consumable::new));
/*     */   
/*  54 */   public static final StreamCodec<RegistryFriendlyByteBuf, Consumable> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, Consumable::consumeSeconds, ItemUseAnimation.STREAM_CODEC, Consumable::animation, SoundEvent.STREAM_CODEC, Consumable::sound, ByteBufCodecs.BOOL, Consumable::hasConsumeParticles, ConsumeEffect.STREAM_CODEC
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  59 */       .apply(ByteBufCodecs.list()), Consumable::onConsumeEffects, Consumable::new);
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult startConsuming(LivingEntity user, ItemStack stack, InteractionHand hand) {
/*  64 */     if (!canConsume(user, stack)) {
/*  65 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  68 */     boolean consumesOverTime = (consumeTicks() > 0);
/*  69 */     if (consumesOverTime) {
/*  70 */       user.startUsingItem(hand);
/*  71 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/*  74 */     ItemStack result = onConsume(user.level(), user, stack);
/*  75 */     return InteractionResult.CONSUME.heldItemTransformedTo(result);
/*     */   }
/*     */   
/*     */   public ItemStack onConsume(Level level, LivingEntity user, ItemStack stack) {
/*  79 */     RandomSource random = user.getRandom();
/*  80 */     emitParticlesAndSounds(random, user, stack, 16);
/*     */     
/*  82 */     if (user instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)user;
/*  83 */       serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
/*  84 */       CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack); }
/*     */ 
/*     */     
/*  87 */     stack.getAllOfType(ConsumableListener.class).forEach(component -> component.onConsume(level, user, stack, this));
/*  88 */     if (!level.isClientSide()) {
/*  89 */       this.onConsumeEffects.forEach(action -> action.apply(level, stack, user));
/*     */     }
/*     */     
/*  92 */     user.gameEvent((this.animation == ItemUseAnimation.DRINK) ? GameEvent.DRINK : GameEvent.EAT);
/*  93 */     stack.consume(1, user);
/*  94 */     return stack;
/*     */   }
/*     */   
/*     */   public boolean canConsume(LivingEntity user, ItemStack stack) {
/*  98 */     FoodProperties foodProperties = (FoodProperties)stack.get(DataComponents.FOOD);
/*  99 */     if (foodProperties != null && user instanceof Player) { Player player = (Player)user;
/* 100 */       return player.canEat(foodProperties.canAlwaysEat()); }
/*     */     
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 106 */   public int consumeTicks() { return (int)(this.consumeSeconds * 20.0F); }
/*     */ 
/*     */   
/*     */   public void emitParticlesAndSounds(RandomSource random, LivingEntity user, ItemStack itemStack, int particleCount) {
/* 110 */     float eatVolume = random.nextBoolean() ? 0.5F : 1.0F;
/* 111 */     float eatPitch = random.triangle(1.0F, 0.2F);
/* 112 */     float drinkVolume = 0.5F;
/* 113 */     float drinkPitch = Mth.randomBetween(random, 0.9F, 1.0F);
/*     */     
/* 115 */     float consumableVolume = (this.animation == ItemUseAnimation.DRINK) ? 0.5F : eatVolume;
/* 116 */     float consumablePitch = (this.animation == ItemUseAnimation.DRINK) ? drinkPitch : eatPitch;
/* 117 */     if (this.hasConsumeParticles) {
/* 118 */       user.spawnItemParticles(itemStack, particleCount);
/*     */     }
/*     */     
/* 121 */     OverrideConsumeSound override = (OverrideConsumeSound)user; SoundEvent consumeSound = (user instanceof OverrideConsumeSound) ? override.getConsumeSound(itemStack) : (SoundEvent)this.sound.value();
/* 122 */     user.playSound(consumeSound, consumableVolume, consumablePitch);
/*     */   }
/*     */   
/*     */   public boolean shouldEmitParticlesAndSounds(int useItemRemainingTicks) {
/* 126 */     int itemUsedForTicks = consumeTicks() - useItemRemainingTicks;
/* 127 */     int waitTicksBeforeUseEffects = (int)(consumeTicks() * 0.21875F);
/* 128 */     boolean isValidTime = (itemUsedForTicks > waitTicksBeforeUseEffects);
/* 129 */     return (isValidTime && useItemRemainingTicks % 4 == 0);
/*     */   }
/*     */ 
/*     */   
/* 133 */   public static Builder builder() { return new Builder(); }
/*     */   public static interface OverrideConsumeSound {
/*     */     SoundEvent getConsumeSound(ItemStack param1ItemStack); }
/*     */   
/* 137 */   public static class Builder { private float consumeSeconds = 1.6F;
/* 138 */     private ItemUseAnimation animation = ItemUseAnimation.EAT;
/* 139 */     private Holder<SoundEvent> sound = SoundEvents.GENERIC_EAT;
/*     */     private boolean hasConsumeParticles = true;
/* 141 */     private final List<ConsumeEffect> onConsumeEffects = new ArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder consumeSeconds(float consumeSeconds) {
/* 146 */       this.consumeSeconds = consumeSeconds;
/* 147 */       return this;
/*     */     }
/*     */     
/*     */     public Builder animation(ItemUseAnimation animation) {
/* 151 */       this.animation = animation;
/* 152 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sound(Holder<SoundEvent> sound) {
/* 156 */       this.sound = sound;
/* 157 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 161 */     public Builder soundAfterConsume(Holder<SoundEvent> soundAfterConsume) { return onConsume(new PlaySoundConsumeEffect(soundAfterConsume)); }
/*     */ 
/*     */     
/*     */     public Builder hasConsumeParticles(boolean hasConsumeParticles) {
/* 165 */       this.hasConsumeParticles = hasConsumeParticles;
/* 166 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onConsume(ConsumeEffect effect) {
/* 170 */       this.onConsumeEffects.add(effect);
/* 171 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 175 */     public Consumable build() { return new Consumable(this.consumeSeconds, this.animation, this.sound, this.hasConsumeParticles, this.onConsumeEffects); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\Consumable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */