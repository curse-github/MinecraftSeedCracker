/*     */ package net.minecraft.world.item.component;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class BlocksAttacks extends Record {
/*     */   private final float blockDelaySeconds;
/*     */   private final float disableCooldownScale;
/*     */   private final List<DamageReduction> damageReductions;
/*     */   
/*  31 */   public BlocksAttacks(float blockDelaySeconds, float disableCooldownScale, List<DamageReduction> damageReductions, ItemDamageFunction itemDamage, Optional<TagKey<DamageType>> bypassedBy, Optional<Holder<SoundEvent>> blockSound, Optional<Holder<SoundEvent>> disableSound) { this.blockDelaySeconds = blockDelaySeconds; this.disableCooldownScale = disableCooldownScale; this.damageReductions = damageReductions; this.itemDamage = itemDamage; this.bypassedBy = bypassedBy; this.blockSound = blockSound; this.disableSound = disableSound; } private final ItemDamageFunction itemDamage; private final Optional<TagKey<DamageType>> bypassedBy; private final Optional<Holder<SoundEvent>> blockSound; private final Optional<Holder<SoundEvent>> disableSound; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks;
/*  31 */     //   0	8	1	o	Ljava/lang/Object; } public float blockDelaySeconds() { return this.blockDelaySeconds; } public float disableCooldownScale() { return this.disableCooldownScale; } public List<DamageReduction> damageReductions() { return this.damageReductions; } public ItemDamageFunction itemDamage() { return this.itemDamage; } public Optional<TagKey<DamageType>> bypassedBy() { return this.bypassedBy; } public Optional<Holder<SoundEvent>> blockSound() { return this.blockSound; } public Optional<Holder<SoundEvent>> disableSound() { return this.disableSound; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static final Codec<BlocksAttacks> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_FLOAT
/*  41 */         .optionalFieldOf("block_delay_seconds", Float.valueOf(0.0F)).forGetter(BlocksAttacks::blockDelaySeconds), ExtraCodecs.NON_NEGATIVE_FLOAT
/*  42 */         .optionalFieldOf("disable_cooldown_scale", Float.valueOf(1.0F)).forGetter(BlocksAttacks::disableCooldownScale), DamageReduction.CODEC
/*  43 */         .listOf().optionalFieldOf("damage_reductions", List.of(new DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F))).forGetter(BlocksAttacks::damageReductions), ItemDamageFunction.CODEC
/*  44 */         .optionalFieldOf("item_damage", ItemDamageFunction.DEFAULT).forGetter(BlocksAttacks::itemDamage), 
/*  45 */         TagKey.hashedCodec(Registries.DAMAGE_TYPE).optionalFieldOf("bypassed_by").forGetter(BlocksAttacks::bypassedBy), SoundEvent.CODEC
/*  46 */         .optionalFieldOf("block_sound").forGetter(BlocksAttacks::blockSound), SoundEvent.CODEC
/*  47 */         .optionalFieldOf("disabled_sound").forGetter(BlocksAttacks::disableSound))
/*  48 */       .apply(i, BlocksAttacks::new));
/*  49 */   public static final StreamCodec<RegistryFriendlyByteBuf, BlocksAttacks> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, BlocksAttacks::blockDelaySeconds, ByteBufCodecs.FLOAT, BlocksAttacks::disableCooldownScale, DamageReduction.STREAM_CODEC
/*     */ 
/*     */       
/*  52 */       .apply(ByteBufCodecs.list()), BlocksAttacks::damageReductions, ItemDamageFunction.STREAM_CODEC, BlocksAttacks::itemDamage, 
/*     */       
/*  54 */       TagKey.streamCodec(Registries.DAMAGE_TYPE).apply(ByteBufCodecs::optional), BlocksAttacks::bypassedBy, SoundEvent.STREAM_CODEC
/*  55 */       .apply(ByteBufCodecs::optional), BlocksAttacks::blockSound, SoundEvent.STREAM_CODEC
/*  56 */       .apply(ByteBufCodecs::optional), BlocksAttacks::disableSound, BlocksAttacks::new);
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlocked(ServerLevel level, LivingEntity user) {
/*  61 */     this.blockSound.ifPresent(sound -> 
/*  62 */         level.playSound(null, user.getX(), user.getY(), user.getZ(), sound, user.getSoundSource(), 1.0F, 0.8F + level.random.nextFloat() * 0.4F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void disable(ServerLevel level, LivingEntity user, float baseSeconds, ItemStack blockingWith) {
/*  67 */     int cooldownTicks = disableBlockingForTicks(baseSeconds);
/*  68 */     if (cooldownTicks > 0) {
/*  69 */       if (user instanceof Player) { Player player = (Player)user;
/*  70 */         player.getCooldowns().addCooldown(blockingWith, cooldownTicks); }
/*     */       
/*  72 */       user.stopUsingItem();
/*  73 */       this.disableSound.ifPresent(sound -> 
/*  74 */           level.playSound(null, user.getX(), user.getY(), user.getZ(), sound, user.getSoundSource(), 0.8F, 0.8F + level.random.nextFloat() * 0.4F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void hurtBlockingItem(Level level, ItemStack item, LivingEntity user, InteractionHand hand, float damage) {
/*     */     Player player;
/*  81 */     if (user instanceof Player) { player = (Player)user; }
/*     */     else
/*     */     { return; }
/*     */     
/*  85 */     if (!level.isClientSide()) {
/*  86 */       player.awardStat(Stats.ITEM_USED.get(item.getItem()));
/*     */     }
/*     */     
/*  89 */     int itemDamage = this.itemDamage.apply(damage);
/*  90 */     if (itemDamage > 0) {
/*  91 */       item.hurtAndBreak(itemDamage, user, hand.asEquipmentSlot());
/*     */     }
/*     */   }
/*     */   
/*     */   private int disableBlockingForTicks(float baseSeconds) {
/*  96 */     float seconds = baseSeconds * this.disableCooldownScale;
/*  97 */     if (seconds > 0.0F) {
/*  98 */       return Math.round(seconds * 20.0F);
/*     */     }
/* 100 */     return 0;
/*     */   }
/*     */ 
/*     */   
/* 104 */   public int blockDelayTicks() { return Math.round(this.blockDelaySeconds * 20.0F); }
/*     */ 
/*     */   
/*     */   public float resolveBlockedDamage(DamageSource source, float dealtDamage, double angle) {
/* 108 */     float blockedDamage = 0.0F;
/* 109 */     for (DamageReduction reduction : this.damageReductions) {
/* 110 */       blockedDamage += reduction.resolve(source, dealtDamage, angle);
/*     */     }
/* 112 */     return Mth.clamp(blockedDamage, 0.0F, dealtDamage);
/*     */   }
/*     */   public static final class DamageReduction extends Record { private final float horizontalBlockingAngle; private final Optional<HolderSet<DamageType>> type; private final float base; private final float factor;
/* 115 */     public DamageReduction(float horizontalBlockingAngle, Optional<HolderSet<DamageType>> type, float base, float factor) { this.horizontalBlockingAngle = horizontalBlockingAngle; this.type = type; this.base = base; this.factor = factor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;
/* 115 */       //   0	8	1	o	Ljava/lang/Object; } public float horizontalBlockingAngle() { return this.horizontalBlockingAngle; } public Optional<HolderSet<DamageType>> type() { return this.type; } public float base() { return this.base; } public float factor() { return this.factor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     public static final Codec<DamageReduction> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_FLOAT
/* 123 */           .optionalFieldOf("horizontal_blocking_angle", Float.valueOf(90.0F)).forGetter(DamageReduction::horizontalBlockingAngle), 
/* 124 */           RegistryCodecs.homogeneousList(Registries.DAMAGE_TYPE).optionalFieldOf("type").forGetter(DamageReduction::type), Codec.FLOAT
/* 125 */           .fieldOf("base").forGetter(DamageReduction::base), Codec.FLOAT
/* 126 */           .fieldOf("factor").forGetter(DamageReduction::factor))
/* 127 */         .apply(i, DamageReduction::new));
/* 128 */     public static final StreamCodec<RegistryFriendlyByteBuf, DamageReduction> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, DamageReduction::horizontalBlockingAngle, 
/*     */         
/* 130 */         ByteBufCodecs.holderSet(Registries.DAMAGE_TYPE).apply(ByteBufCodecs::optional), DamageReduction::type, ByteBufCodecs.FLOAT, DamageReduction::base, ByteBufCodecs.FLOAT, DamageReduction::factor, DamageReduction::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float resolve(DamageSource source, float dealtDamage, double angle) {
/* 137 */       if (angle > (0.017453292F * this.horizontalBlockingAngle)) {
/* 138 */         return 0.0F;
/*     */       }
/*     */       
/* 141 */       if (this.type.isPresent() && !((HolderSet)this.type.get()).contains(source.typeHolder())) {
/* 142 */         return 0.0F;
/*     */       }
/* 144 */       return Mth.clamp(this.base + this.factor * dealtDamage, 0.0F, dealtDamage);
/*     */     } }
/*     */   public static final class ItemDamageFunction extends Record { private final float threshold; private final float base; private final float factor;
/*     */     
/* 148 */     public ItemDamageFunction(float threshold, float base, float factor) { this.threshold = threshold; this.base = base; this.factor = factor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/* 148 */       //   0	8	1	o	Ljava/lang/Object; } public float threshold() { return this.threshold; } public float base() { return this.base; } public float factor() { return this.factor; }
/* 149 */     public static final Codec<ItemDamageFunction> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_FLOAT
/* 150 */           .fieldOf("threshold").forGetter(ItemDamageFunction::threshold), Codec.FLOAT
/* 151 */           .fieldOf("base").forGetter(ItemDamageFunction::base), Codec.FLOAT
/* 152 */           .fieldOf("factor").forGetter(ItemDamageFunction::factor))
/* 153 */         .apply(i, ItemDamageFunction::new));
/* 154 */     public static final StreamCodec<ByteBuf, ItemDamageFunction> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ItemDamageFunction::threshold, ByteBufCodecs.FLOAT, ItemDamageFunction::base, ByteBufCodecs.FLOAT, ItemDamageFunction::factor, ItemDamageFunction::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     public static final ItemDamageFunction DEFAULT = new ItemDamageFunction(1.0F, 0.0F, 1.0F);
/*     */     
/*     */     public int apply(float dealtDamage) {
/* 164 */       if (dealtDamage < this.threshold) {
/* 165 */         return 0;
/*     */       }
/* 167 */       return Mth.floor(this.base + this.factor * dealtDamage);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\BlocksAttacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */