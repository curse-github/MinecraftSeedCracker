/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ public abstract class WaterFluid
/*     */   extends FlowingFluid
/*     */ {
/*  36 */   public Fluid getFlowing() { return Fluids.FLOWING_WATER; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public Fluid getSource() { return Fluids.WATER; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public Item getBucket() { return Items.WATER_BUCKET; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  51 */     if (!fluidState.isSource() && !((Boolean)fluidState.getValue(FALLING)).booleanValue()) {
/*  52 */       if (random.nextInt(64) == 0) {
/*  53 */         level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.AMBIENT, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
/*     */       }
/*  55 */     } else if (random.nextInt(10) == 0) {
/*  56 */       level.addParticle(ParticleTypes.UNDERWATER, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public ParticleOptions getDripParticle() { return ParticleTypes.DRIPPING_WATER; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected boolean canConvertToSource(ServerLevel level) { return ((Boolean)level.getGameRules().get(GameRules.WATER_SOURCE_CONVERSION)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
/*  72 */     BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  73 */     Block.dropResources(state, level, pos, blockEntity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) { effectApplier.apply(InsideBlockEffectType.EXTINGUISH); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public int getSlopeFindDistance(LevelReader level) { return 4; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public BlockState createLegacyBlock(FluidState fluidState) { return (BlockState)Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(fluidState))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean isSame(Fluid other) { return (other == Fluids.WATER || other == Fluids.FLOWING_WATER); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public int getDropOff(LevelReader level) { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public int getTickDelay(LevelReader level) { return 5; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) { return (direction == Direction.DOWN && !other.is(FluidTags.WATER)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected float getExplosionResistance() { return 100.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public Optional<SoundEvent> getPickupSound() { return Optional.of(SoundEvents.BUCKET_FILL); }
/*     */ 
/*     */   
/*     */   public static class Source
/*     */     extends WaterFluid
/*     */   {
/* 124 */     public int getAmount(FluidState fluidState) { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     public boolean isSource(FluidState fluidState) { return true; }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends WaterFluid
/*     */   {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
/* 136 */       super.createFluidStateDefinition(builder);
/* 137 */       builder.add(new Property[] { LEVEL });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 142 */     public int getAmount(FluidState fluidState) { return ((Integer)fluidState.getValue(LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     public boolean isSource(FluidState fluidState) { return false; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\material\WaterFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */