/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoneyBlock
/*     */   extends HalfTransparentBlock
/*     */ {
/*  38 */   public static final MapCodec<HoneyBlock> CODEC = simpleCodec(HoneyBlock::new);
/*     */   private static final double SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST = 0.13D;
/*     */   private static final double MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING = 0.08D;
/*     */   
/*  42 */   public MapCodec<HoneyBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final double THROTTLE_SLIDE_SPEED_TO = 0.05D;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SLIDE_ADVANCEMENT_CHECK_INTERVAL = 20;
/*     */ 
/*     */   
/*  54 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 15.0D);
/*     */ 
/*     */   
/*  57 */   public HoneyBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private static boolean doesEntityDoHoneyBlockSlideEffects(Entity entity) { return (entity instanceof net.minecraft.world.entity.LivingEntity || entity instanceof net.minecraft.world.entity.vehicle.minecart.AbstractMinecart || entity instanceof net.minecraft.world.entity.item.PrimedTnt || entity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/*  75 */     entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*     */     
/*  77 */     if (!level.isClientSide())
/*     */     {
/*     */       
/*  80 */       level.broadcastEntityEvent(entity, (byte)54);
/*     */     }
/*     */     
/*  83 */     if (entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall())) {
/*  84 */       entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  90 */     if (isSlidingDown(pos, entity)) {
/*  91 */       maybeDoSlideAchievement(entity, pos);
/*  92 */       doSlideMovement(entity);
/*  93 */       maybeDoSlideEffects(level, entity);
/*     */     } 
/*  95 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   private static double getOldDeltaY(double deltaY) { return deltaY / 0.9800000190734863D + 0.08D; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   private static double getNewDeltaY(double deltaY) { return (deltaY - 0.08D) * 0.9800000190734863D; }
/*     */ 
/*     */   
/*     */   private boolean isSlidingDown(BlockPos pos, Entity entity) {
/* 108 */     if (entity.onGround()) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (entity.getY() > pos.getY() + 0.9375D - 1.0E-7D)
/*     */     {
/* 113 */       return false;
/*     */     }
/* 115 */     if (getOldDeltaY((entity.getDeltaMovement()).y) >= -0.08D) {
/* 116 */       return false;
/*     */     }
/*     */     
/* 119 */     double dx = Math.abs(pos.getX() + 0.5D - entity.getX());
/* 120 */     double dz = Math.abs(pos.getZ() + 0.5D - entity.getZ());
/*     */     
/* 122 */     double overlapDistance = 0.4375D + (entity.getBbWidth() / 2.0F);
/*     */     
/* 124 */     return (dx + 1.0E-7D > overlapDistance || dz + 1.0E-7D > overlapDistance);
/*     */   }
/*     */   
/*     */   private void maybeDoSlideAchievement(Entity entity, BlockPos pos) {
/* 128 */     if (entity instanceof ServerPlayer && entity.level().getGameTime() % 20L == 0L)
/*     */     {
/* 130 */       CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer)entity, entity.level().getBlockState(pos));
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSlideMovement(Entity entity) {
/* 135 */     Vec3 deltaMovement = entity.getDeltaMovement();
/* 136 */     if (getOldDeltaY((entity.getDeltaMovement()).y) < -0.13D) {
/*     */       
/* 138 */       double horizontalReductionFactor = -0.05D / getOldDeltaY((entity.getDeltaMovement()).y);
/* 139 */       entity.setDeltaMovement(new Vec3(deltaMovement.x * horizontalReductionFactor, getNewDeltaY(-0.05D), deltaMovement.z * horizontalReductionFactor));
/*     */     } else {
/*     */       
/* 142 */       entity.setDeltaMovement(new Vec3(deltaMovement.x, getNewDeltaY(-0.05D), deltaMovement.z));
/*     */     } 
/* 144 */     entity.resetFallDistance();
/*     */   }
/*     */   
/*     */   private void maybeDoSlideEffects(Level level, Entity entity) {
/* 148 */     if (doesEntityDoHoneyBlockSlideEffects(entity)) {
/* 149 */       if (level.random.nextInt(5) == 0) {
/* 150 */         entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 153 */       if (!level.isClientSide() && level.random.nextInt(5) == 0) {
/* 154 */         level.broadcastEntityEvent(entity, (byte)53);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 160 */   public static void showSlideParticles(Entity entity) { showParticles(entity, 5); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static void showJumpParticles(Entity entity) { showParticles(entity, 10); }
/*     */ 
/*     */   
/*     */   private static void showParticles(Entity entity, int count) {
/* 168 */     if (!entity.level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     BlockState blockState = Blocks.HONEY_BLOCK.defaultBlockState();
/* 173 */     for (int i = 0; i < count; i++)
/*     */     {
/* 175 */       entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HoneyBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */