/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WitherRoseBlock extends FlowerBlock {
/* 27 */   public static final MapCodec<WitherRoseBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(EFFECTS_FIELD
/* 28 */         .forGetter(FlowerBlock::getSuspiciousEffects), 
/* 29 */         propertiesCodec())
/* 30 */       .apply(i, WitherRoseBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 34 */   public MapCodec<WitherRoseBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public WitherRoseBlock(Holder<MobEffect> mobEffect, float effectSeconds, BlockBehaviour.Properties properties) { this(makeEffectList(mobEffect, effectSeconds), properties); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public WitherRoseBlock(SuspiciousStewEffects suspiciousStewEffects, BlockBehaviour.Properties properties) { super(suspiciousStewEffects, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (super.mayPlaceOn(state, level, pos) || state.is(Blocks.NETHERRACK) || state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 52 */     VoxelShape shape = getShape(state, level, pos, CollisionContext.empty());
/* 53 */     Vec3 shapeCenter = shape.bounds().getCenter();
/* 54 */     double x = pos.getX() + shapeCenter.x;
/* 55 */     double z = pos.getZ() + shapeCenter.z;
/* 56 */     for (int i = 0; i < 3; i++) {
/* 57 */       if (random.nextBoolean()) {
/* 58 */         level.addParticle(ParticleTypes.SMOKE, x + random.nextDouble() / 5.0D, pos.getY() + 0.5D - random.nextDouble(), z + random.nextDouble() / 5.0D, 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 65 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (level
/* 66 */         .getDifficulty() != Difficulty.PEACEFUL && entity instanceof LivingEntity) {
/* 67 */         LivingEntity livingEntity = (LivingEntity)entity;
/* 68 */         if (!livingEntity.isInvulnerableTo(serverLevel, level.damageSources().wither())) {
/* 69 */           livingEntity.addEffect(getBeeInteractionEffect());
/*    */         }
/*    */       }  }
/*    */   
/*    */   }
/*    */   
/* 75 */   public MobEffectInstance getBeeInteractionEffect() { return new MobEffectInstance(MobEffects.WITHER, 40); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WitherRoseBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */