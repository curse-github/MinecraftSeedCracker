/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ExplodeEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final boolean attributeToUser;
/*    */   private final Optional<Holder<DamageType>> damageType;
/*    */   private final Optional<LevelBasedValue> knockbackMultiplier;
/*    */   private final Optional<HolderSet<Block>> immuneBlocks;
/*    */   private final Vec3 offset;
/*    */   private final LevelBasedValue radius;
/*    */   
/* 29 */   public ExplodeEffect(boolean attributeToUser, Optional<Holder<DamageType>> damageType, Optional<LevelBasedValue> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks, Vec3 offset, LevelBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> sound) { this.attributeToUser = attributeToUser; this.damageType = damageType; this.knockbackMultiplier = knockbackMultiplier; this.immuneBlocks = immuneBlocks; this.offset = offset; this.radius = radius; this.createFire = createFire; this.blockInteraction = blockInteraction; this.smallParticle = smallParticle; this.largeParticle = largeParticle; this.blockParticles = blockParticles; this.sound = sound; } private final boolean createFire; private final Level.ExplosionInteraction blockInteraction; private final ParticleOptions smallParticle; private final ParticleOptions largeParticle; private final WeightedList<ExplosionParticleInfo> blockParticles; private final Holder<SoundEvent> sound; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;
/* 29 */     //   0	8	1	o	Ljava/lang/Object; } public boolean attributeToUser() { return this.attributeToUser; } public Optional<Holder<DamageType>> damageType() { return this.damageType; } public Optional<LevelBasedValue> knockbackMultiplier() { return this.knockbackMultiplier; } public Optional<HolderSet<Block>> immuneBlocks() { return this.immuneBlocks; } public Vec3 offset() { return this.offset; } public LevelBasedValue radius() { return this.radius; } public boolean createFire() { return this.createFire; } public Level.ExplosionInteraction blockInteraction() { return this.blockInteraction; } public ParticleOptions smallParticle() { return this.smallParticle; } public ParticleOptions largeParticle() { return this.largeParticle; } public WeightedList<ExplosionParticleInfo> blockParticles() { return this.blockParticles; } public Holder<SoundEvent> sound() { return this.sound; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public static final MapCodec<ExplodeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 44 */         .optionalFieldOf("attribute_to_user", Boolean.valueOf(false)).forGetter(ExplodeEffect::attributeToUser), DamageType.CODEC
/* 45 */         .optionalFieldOf("damage_type").forGetter(ExplodeEffect::damageType), LevelBasedValue.CODEC
/* 46 */         .optionalFieldOf("knockback_multiplier").forGetter(ExplodeEffect::knockbackMultiplier), 
/* 47 */         RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeEffect::immuneBlocks), Vec3.CODEC
/* 48 */         .optionalFieldOf("offset", Vec3.ZERO).forGetter(ExplodeEffect::offset), LevelBasedValue.CODEC
/* 49 */         .fieldOf("radius").forGetter(ExplodeEffect::radius), Codec.BOOL
/* 50 */         .optionalFieldOf("create_fire", Boolean.valueOf(false)).forGetter(ExplodeEffect::createFire), Level.ExplosionInteraction.CODEC
/* 51 */         .fieldOf("block_interaction").forGetter(ExplodeEffect::blockInteraction), ParticleTypes.CODEC
/* 52 */         .fieldOf("small_particle").forGetter(ExplodeEffect::smallParticle), ParticleTypes.CODEC
/* 53 */         .fieldOf("large_particle").forGetter(ExplodeEffect::largeParticle), 
/* 54 */         WeightedList.codec(ExplosionParticleInfo.CODEC).optionalFieldOf("block_particles", WeightedList.of()).forGetter(ExplodeEffect::blockParticles), SoundEvent.CODEC
/* 55 */         .fieldOf("sound").forGetter(ExplodeEffect::sound))
/* 56 */       .apply(i, ExplodeEffect::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 60 */     Vec3 pos = position.add(this.offset);
/* 61 */     serverLevel.explode(
/* 62 */         this.attributeToUser ? entity : null, 
/* 63 */         getDamageSource(entity, pos), new SimpleExplosionDamageCalculator((this.blockInteraction != Level.ExplosionInteraction.NONE), this.damageType
/*    */ 
/*    */           
/* 66 */           .isPresent(), this.knockbackMultiplier
/* 67 */           .map(value -> Float.valueOf(value.calculate(enchantmentLevel))), this.immuneBlocks), pos
/*    */ 
/*    */         
/* 70 */         .x(), pos
/* 71 */         .y(), pos
/* 72 */         .z(), 
/* 73 */         Math.max(this.radius.calculate(enchantmentLevel), 0.0F), this.createFire, this.blockInteraction, this.smallParticle, this.largeParticle, this.blockParticles, this.sound);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private DamageSource getDamageSource(Entity entity, Vec3 position) {
/* 83 */     if (this.damageType.isEmpty()) {
/* 84 */       return null;
/*    */     }
/* 86 */     if (this.attributeToUser) {
/* 87 */       return new DamageSource((Holder)this.damageType.get(), entity);
/*    */     }
/* 89 */     return new DamageSource((Holder)this.damageType.get(), position);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 94 */   public MapCodec<ExplodeEffect> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ExplodeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */