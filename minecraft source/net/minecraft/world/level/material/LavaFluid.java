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
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LavaFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public static final float MIN_LEVEL_CUTOFF = 0.44444445F;
/*     */   
/*  41 */   public Fluid getFlowing() { return Fluids.FLOWING_LAVA; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public Fluid getSource() { return Fluids.LAVA; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public Item getBucket() { return Items.LAVA_BUCKET; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  56 */     BlockPos above = pos.above();
/*  57 */     if (level.getBlockState(above).isAir() && !level.getBlockState(above).isSolidRender()) {
/*  58 */       if (random.nextInt(100) == 0) {
/*  59 */         double xx = pos.getX() + random.nextDouble();
/*     */         
/*  61 */         double yy = pos.getY() + 1.0D;
/*  62 */         double zz = pos.getZ() + random.nextDouble();
/*  63 */         level.addParticle(ParticleTypes.LAVA, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*  64 */         level.playLocalSound(xx, yy, zz, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       } 
/*  66 */       if (random.nextInt(200) == 0) {
/*  67 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(ServerLevel level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  74 */     if (!level.canSpreadFireAround(pos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  79 */     int passes = random.nextInt(3);
/*  80 */     if (passes > 0) {
/*  81 */       BlockPos testPos = pos;
/*     */       
/*  83 */       for (int pass = 0; pass < passes; pass++) {
/*  84 */         testPos = testPos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
/*  85 */         if (!level.isLoaded(testPos)) {
/*     */           return;
/*     */         }
/*  88 */         BlockState blockState = level.getBlockState(testPos);
/*  89 */         if (blockState.isAir()) {
/*  90 */           if (hasFlammableNeighbours(level, testPos)) {
/*  91 */             level.setBlockAndUpdate(testPos, BaseFireBlock.getState(level, testPos));
/*     */             return;
/*     */           } 
/*  94 */         } else if (blockState.blocksMotion()) {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  99 */       for (int i = 0; i < 3; i++) {
/* 100 */         BlockPos testPos = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
/* 101 */         if (!level.isLoaded(testPos)) {
/*     */           return;
/*     */         }
/* 104 */         if (level.isEmptyBlock(testPos.above()) && isFlammable(level, testPos)) {
/* 105 */           level.setBlockAndUpdate(testPos.above(), BaseFireBlock.getState(level, testPos));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
/* 113 */     effectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 114 */     effectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
/* 115 */     effectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
/*     */   }
/*     */   
/*     */   private boolean hasFlammableNeighbours(LevelReader level, BlockPos pos) {
/* 119 */     for (Direction direction : Direction.values()) {
/* 120 */       if (isFlammable(level, pos.relative(direction))) {
/* 121 */         return true;
/*     */       }
/*     */     } 
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isFlammable(LevelReader level, BlockPos pos) {
/* 128 */     if (level.isInsideBuildHeight(pos.getY()) && !level.hasChunkAt(pos)) {
/* 129 */       return false;
/*     */     }
/* 131 */     return level.getBlockState(pos).ignitedByLava();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public ParticleOptions getDripParticle() { return ParticleTypes.DRIPPING_LAVA; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) { fizz(level, pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public int getSlopeFindDistance(LevelReader level) { return isFastLava(level) ? 4 : 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public BlockState createLegacyBlock(FluidState fluidState) { return (BlockState)Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(fluidState))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public boolean isSame(Fluid other) { return (other == Fluids.LAVA || other == Fluids.FLOWING_LAVA); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public int getDropOff(LevelReader level) { return isFastLava(level) ? 1 : 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) { return (state.getHeight(level, pos) >= 0.44444445F && other.is(FluidTags.WATER)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public int getTickDelay(LevelReader level) { return isFastLava(level) ? 10 : 30; }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSpreadDelay(Level level, BlockPos pos, FluidState oldFluidState, FluidState newFluidState) {
/* 176 */     int result = getTickDelay(level);
/*     */     
/* 178 */     if (!oldFluidState.isEmpty() && !newFluidState.isEmpty() && !((Boolean)oldFluidState.getValue(FALLING)).booleanValue() && !((Boolean)newFluidState.getValue(FALLING)).booleanValue() && newFluidState.getHeight(level, pos) > oldFluidState.getHeight(level, pos) && level.getRandom().nextInt(4) != 0) {
/* 179 */       result *= 4;
/*     */     }
/* 181 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 185 */   private void fizz(LevelAccessor level, BlockPos pos) { level.levelEvent(1501, pos, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   protected boolean canConvertToSource(ServerLevel level) { return ((Boolean)level.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState target) {
/* 195 */     if (direction == Direction.DOWN) {
/* 196 */       FluidState fluidState = level.getFluidState(pos);
/* 197 */       if (is(FluidTags.LAVA) && fluidState.is(FluidTags.WATER)) {
/* 198 */         if (state.getBlock() instanceof LiquidBlock) {
/* 199 */           level.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
/*     */         }
/* 201 */         fizz(level, pos);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 206 */     super.spreadTo(level, pos, state, direction, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 211 */   protected boolean isRandomlyTicking() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   protected float getExplosionResistance() { return 100.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   public Optional<SoundEvent> getPickupSound() { return Optional.of(SoundEvents.BUCKET_FILL_LAVA); }
/*     */ 
/*     */ 
/*     */   
/* 225 */   private static boolean isFastLava(LevelReader level) { return ((Boolean)level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA)).booleanValue(); }
/*     */ 
/*     */   
/*     */   public static class Source
/*     */     extends LavaFluid
/*     */   {
/* 231 */     public int getAmount(FluidState fluidState) { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     public boolean isSource(FluidState fluidState) { return true; }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends LavaFluid
/*     */   {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
/* 243 */       super.createFluidStateDefinition(builder);
/* 244 */       builder.add(new Property[] { LEVEL });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 249 */     public int getAmount(FluidState fluidState) { return ((Integer)fluidState.getValue(LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     public boolean isSource(FluidState fluidState) { return false; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\material\LavaFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */