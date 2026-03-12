/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class ParticleUtils
/*     */ {
/*     */   public static void spawnParticlesOnBlockFaces(Level level, BlockPos pos, ParticleOptions particle, IntProvider particlesPerFaceRange) {
/*  20 */     for (Direction direction : Direction.values()) {
/*  21 */       spawnParticlesOnBlockFace(level, pos, particle, particlesPerFaceRange, direction, () -> getRandomSpeedRanges(level.random), 0.55D);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void spawnParticlesOnBlockFace(Level level, BlockPos pos, ParticleOptions particle, IntProvider particlesPerFaceRange, Direction face, Supplier<Vec3> speedSupplier, double stepFactor) {
/*  26 */     int particleCount = particlesPerFaceRange.sample(level.random);
/*  27 */     for (int i = 0; i < particleCount; i++) {
/*  28 */       spawnParticleOnFace(level, pos, face, particle, (Vec3)speedSupplier.get(), stepFactor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  33 */   private static Vec3 getRandomSpeedRanges(RandomSource random) { return new Vec3(Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D)); }
/*     */ 
/*     */   
/*     */   public static void spawnParticlesAlongAxis(Direction.Axis attachedAxis, Level level, BlockPos pos, double radius, ParticleOptions particle, UniformInt sparkCount) {
/*  37 */     Vec3 centerOfBlock = Vec3.atCenterOf(pos);
/*     */     
/*  39 */     boolean stepX = (attachedAxis == Direction.Axis.X);
/*  40 */     boolean stepY = (attachedAxis == Direction.Axis.Y);
/*  41 */     boolean stepZ = (attachedAxis == Direction.Axis.Z);
/*     */     
/*  43 */     int particleCount = sparkCount.sample(level.random);
/*  44 */     for (int i = 0; i < particleCount; i++) {
/*  45 */       double x = centerOfBlock.x + Mth.nextDouble(level.random, -1.0D, 1.0D) * (stepX ? 0.5D : radius);
/*  46 */       double y = centerOfBlock.y + Mth.nextDouble(level.random, -1.0D, 1.0D) * (stepY ? 0.5D : radius);
/*  47 */       double z = centerOfBlock.z + Mth.nextDouble(level.random, -1.0D, 1.0D) * (stepZ ? 0.5D : radius);
/*  48 */       double xBaseSpeed = stepX ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
/*  49 */       double yBaseSpeed = stepY ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
/*  50 */       double zBaseSpeed = stepZ ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
/*     */       
/*  52 */       level.addParticle(particle, x, y, z, xBaseSpeed, yBaseSpeed, zBaseSpeed);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void spawnParticleOnFace(Level level, BlockPos pos, Direction face, ParticleOptions particle, Vec3 speed, double stepFactor) {
/*  57 */     Vec3 centerOfBlock = Vec3.atCenterOf(pos);
/*  58 */     int stepX = face.getStepX();
/*  59 */     int stepY = face.getStepY();
/*  60 */     int stepZ = face.getStepZ();
/*  61 */     double x = centerOfBlock.x + ((stepX == 0) ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (stepX * stepFactor));
/*  62 */     double y = centerOfBlock.y + ((stepY == 0) ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (stepY * stepFactor));
/*  63 */     double z = centerOfBlock.z + ((stepZ == 0) ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (stepZ * stepFactor));
/*  64 */     double xBaseSpeed = (stepX == 0) ? speed.x() : 0.0D;
/*  65 */     double yBaseSpeed = (stepY == 0) ? speed.y() : 0.0D;
/*  66 */     double zBaseSpeed = (stepZ == 0) ? speed.z() : 0.0D;
/*     */     
/*  68 */     level.addParticle(particle, x, y, z, xBaseSpeed, yBaseSpeed, zBaseSpeed);
/*     */   }
/*     */   
/*     */   public static void spawnParticleBelow(Level level, BlockPos pos, RandomSource random, ParticleOptions particle) {
/*  72 */     double x = pos.getX() + random.nextDouble();
/*  73 */     double y = pos.getY() - 0.05D;
/*  74 */     double z = pos.getZ() + random.nextDouble();
/*     */     
/*  76 */     level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public static void spawnParticleInBlock(LevelAccessor level, BlockPos pos, int count, ParticleOptions particle) {
/*  80 */     double spreadWidth = 0.5D;
/*  81 */     BlockState blockState = level.getBlockState(pos);
/*  82 */     double spreadHeight = blockState.isAir() ? 1.0D : blockState.getShape(level, pos).max(Direction.Axis.Y);
/*  83 */     spawnParticles(level, pos, count, 0.5D, spreadHeight, true, particle);
/*     */   }
/*     */   
/*     */   public static void spawnParticles(LevelAccessor level, BlockPos pos, int count, double spreadWidth, double spreadHeight, boolean allowFloatingParticles, ParticleOptions particle) {
/*  87 */     RandomSource random = level.getRandom();
/*  88 */     for (int i = 0; i < count; i++) {
/*  89 */       double xVelocity = random.nextGaussian() * 0.02D;
/*  90 */       double yVelocity = random.nextGaussian() * 0.02D;
/*  91 */       double zVelocity = random.nextGaussian() * 0.02D;
/*     */       
/*  93 */       double spreadStartOffset = 0.5D - spreadWidth;
/*  94 */       double x = pos.getX() + spreadStartOffset + random.nextDouble() * spreadWidth * 2.0D;
/*  95 */       double y = pos.getY() + random.nextDouble() * spreadHeight;
/*  96 */       double z = pos.getZ() + spreadStartOffset + random.nextDouble() * spreadWidth * 2.0D;
/*     */       
/*  98 */       if (allowFloatingParticles || !level.getBlockState(BlockPos.containing(x, y, z).below()).isAir()) {
/*  99 */         level.addParticle(particle, x, y, z, xVelocity, yVelocity, zVelocity);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void spawnSmashAttackParticles(LevelAccessor level, BlockPos pos, int count) {
/* 105 */     Vec3 center = pos.getCenter().add(0.0D, 0.5D, 0.0D);
/* 106 */     BlockParticleOption particle = new BlockParticleOption(ParticleTypes.DUST_PILLAR, level.getBlockState(pos));
/*     */     
/* 108 */     for (int i = 0; i < count / 3.0F; i++) {
/* 109 */       double x = center.x + level.getRandom().nextGaussian() / 2.0D;
/* 110 */       double y = center.y;
/* 111 */       double z = center.z + level.getRandom().nextGaussian() / 2.0D;
/*     */       
/* 113 */       double xd = level.getRandom().nextGaussian() * 0.20000000298023224D;
/* 114 */       double yd = level.getRandom().nextGaussian() * 0.20000000298023224D;
/* 115 */       double zd = level.getRandom().nextGaussian() * 0.20000000298023224D;
/*     */       
/* 117 */       level.addParticle(particle, x, y, z, xd, yd, zd);
/*     */     } 
/*     */     
/* 120 */     for (int i = 0; i < count / 1.5F; i++) {
/* 121 */       double x = center.x + 3.5D * Math.cos(i) + level.getRandom().nextGaussian() / 2.0D;
/* 122 */       double y = center.y;
/* 123 */       double z = center.z + 3.5D * Math.sin(i) + level.getRandom().nextGaussian() / 2.0D;
/*     */       
/* 125 */       double xd = level.getRandom().nextGaussian() * 0.05000000074505806D;
/* 126 */       double yd = level.getRandom().nextGaussian() * 0.05000000074505806D;
/* 127 */       double zd = level.getRandom().nextGaussian() * 0.05000000074505806D;
/*     */       
/* 129 */       level.addParticle(particle, x, y, z, xd, yd, zd);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ParticleUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */