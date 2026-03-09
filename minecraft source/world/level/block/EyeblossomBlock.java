/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.TrailParticleOption;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EyeblossomBlock extends FlowerBlock {
/*  31 */   public static final MapCodec<EyeblossomBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/*  32 */         .fieldOf("open").forGetter(()), 
/*  33 */         propertiesCodec())
/*  34 */       .apply(i, EyeblossomBlock::new)); private static final int EYEBLOSSOM_XZ_RANGE = 3;
/*     */   private static final int EYEBLOSSOM_Y_RANGE = 2;
/*     */   private final Type type;
/*     */   
/*  38 */   public MapCodec<? extends EyeblossomBlock> codec() { return CODEC; }
/*     */   
/*     */   public enum Type
/*     */   {
/*  42 */     OPEN(true, MobEffects.BLINDNESS, 11.0F, SoundEvents.EYEBLOSSOM_OPEN_LONG, SoundEvents.EYEBLOSSOM_OPEN, 16545810),
/*  43 */     CLOSED(false, MobEffects.NAUSEA, 7.0F, SoundEvents.EYEBLOSSOM_CLOSE_LONG, SoundEvents.EYEBLOSSOM_CLOSE, 6250335);
/*     */     
/*     */     private final boolean open;
/*     */     private final Holder<MobEffect> effect;
/*     */     private final float effectDuration;
/*     */     private final SoundEvent longSwitchSound;
/*     */     private final SoundEvent shortSwitchSound;
/*     */     private final int particleColor;
/*     */     
/*     */     Type(boolean open, Holder<MobEffect> effect, float duration, SoundEvent longSwitchSound, SoundEvent shortSwitchSound, int particleColor) {
/*  53 */       this.open = open;
/*  54 */       this.effect = effect;
/*  55 */       this.effectDuration = duration;
/*  56 */       this.longSwitchSound = longSwitchSound;
/*  57 */       this.shortSwitchSound = shortSwitchSound;
/*  58 */       this.particleColor = particleColor;
/*     */     }
/*     */ 
/*     */     
/*  62 */     public Block block() { return this.open ? Blocks.OPEN_EYEBLOSSOM : Blocks.CLOSED_EYEBLOSSOM; }
/*     */ 
/*     */ 
/*     */     
/*  66 */     public BlockState state() { return block().defaultBlockState(); }
/*     */ 
/*     */ 
/*     */     
/*  70 */     public Type transform() { return fromBoolean(!this.open); }
/*     */ 
/*     */ 
/*     */     
/*  74 */     public boolean emitSounds() { return this.open; }
/*     */ 
/*     */ 
/*     */     
/*  78 */     public static Type fromBoolean(boolean open) { return open ? OPEN : CLOSED; }
/*     */ 
/*     */     
/*     */     public void spawnTransformParticle(ServerLevel level, BlockPos pos, RandomSource random) {
/*  82 */       Vec3 start = pos.getCenter();
/*  83 */       double lifetime = 0.5D + random.nextDouble();
/*  84 */       Vec3 velocity = new Vec3(random.nextDouble() - 0.5D, random.nextDouble() + 1.0D, random.nextDouble() - 0.5D);
/*  85 */       Vec3 target = start.add(velocity.scale(lifetime));
/*  86 */       TrailParticleOption particle = new TrailParticleOption(target, this.particleColor, (int)(20.0D * lifetime));
/*  87 */       level.sendParticles(particle, start.x, start.y, start.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */ 
/*     */     
/*  91 */     public SoundEvent longSwitchSound() { return this.longSwitchSound; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EyeblossomBlock(Type type, BlockBehaviour.Properties properties) {
/* 101 */     super(type.effect, type.effectDuration, properties);
/* 102 */     this.type = type;
/*     */   }
/*     */   
/*     */   public EyeblossomBlock(boolean open, BlockBehaviour.Properties properties) {
/* 106 */     super((Type.fromBoolean(open)).effect, (Type.fromBoolean(open)).effectDuration, properties);
/* 107 */     this.type = Type.fromBoolean(open);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 112 */     if (this.type.emitSounds() && random.nextInt(700) == 0) {
/* 113 */       BlockState below = level.getBlockState(pos.below());
/* 114 */       if (below.is(Blocks.PALE_MOSS_BLOCK)) {
/* 115 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.EYEBLOSSOM_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 122 */     if (tryChangingState(state, level, pos, random)) {
/* 123 */       level.playSound(null, pos, (this.type.transform()).longSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     }
/* 125 */     super.randomTick(state, level, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 130 */     if (tryChangingState(state, level, pos, random)) {
/* 131 */       level.playSound(null, pos, (this.type.transform()).shortSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     }
/* 133 */     super.tick(state, level, pos, random);
/*     */   }
/*     */   
/*     */   private boolean tryChangingState(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 137 */     boolean shouldBeOpen = ((TriState)level.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos)).toBoolean(this.type.open);
/* 138 */     if (shouldBeOpen == this.type.open) {
/* 139 */       return false;
/*     */     }
/* 141 */     Type newType = this.type.transform();
/* 142 */     level.setBlock(pos, newType.state(), 3);
/* 143 */     level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
/* 144 */     newType.spawnTransformParticle(level, pos, random);
/* 145 */     BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach(nearby -> {
/* 146 */           BlockState nearbyState = level.getBlockState(nearby);
/* 147 */           if (nearbyState == state) {
/* 148 */             double distance = Math.sqrt(pos.distSqr(nearby));
/* 149 */             int delay = random.nextIntBetweenInclusive((int)(distance * 5.0D), (int)(distance * 10.0D));
/* 150 */             level.scheduleTick(nearby, state.getBlock(), delay);
/*     */           } 
/*     */         });
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 158 */     if (!level.isClientSide() && level
/* 159 */       .getDifficulty() != Difficulty.PEACEFUL && entity instanceof Bee) {
/* 160 */       Bee bee = (Bee)entity;
/* 161 */       if (Bee.attractsBees(state) && 
/* 162 */         !bee.hasEffect(MobEffects.POISON)) {
/* 163 */         bee.addEffect(getBeeInteractionEffect());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 169 */   public MobEffectInstance getBeeInteractionEffect() { return new MobEffectInstance(MobEffects.POISON, 25); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EyeblossomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */