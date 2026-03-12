/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseFireBlock
/*     */   extends Block
/*     */ {
/*     */   private static final int SECONDS_ON_FIRE = 8;
/*     */   private static final int MIN_FIRE_TICKS_TO_ADD = 1;
/*     */   private static final int MAX_FIRE_TICKS_TO_ADD = 3;
/*     */   private final float fireDamage;
/*  33 */   protected static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.0D);
/*     */   
/*     */   public BaseFireBlock(BlockBehaviour.Properties properties, float fireDamage) {
/*  36 */     super(properties);
/*  37 */     this.fireDamage = fireDamage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return getState(context.getLevel(), context.getClickedPos()); }
/*     */ 
/*     */   
/*     */   public static BlockState getState(BlockGetter level, BlockPos pos) {
/*  49 */     BlockPos below = pos.below();
/*  50 */     BlockState belowState = level.getBlockState(below);
/*     */     
/*  52 */     if (SoulFireBlock.canSurviveOnBlock(belowState)) {
/*  53 */       return Blocks.SOUL_FIRE.defaultBlockState();
/*     */     }
/*     */     
/*  56 */     return ((FireBlock)Blocks.FIRE).getStateForPlacement(level, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  66 */     if (random.nextInt(24) == 0) {
/*  67 */       level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
/*     */     }
/*     */     
/*  70 */     BlockPos below = pos.below();
/*  71 */     BlockState belowState = level.getBlockState(below);
/*  72 */     if (canBurn(belowState) || belowState.isFaceSturdy(level, below, Direction.UP)) {
/*  73 */       for (int i = 0; i < 3; i++) {
/*  74 */         double xx = pos.getX() + random.nextDouble();
/*  75 */         double yy = pos.getY() + random.nextDouble() * 0.5D + 0.5D;
/*  76 */         double zz = pos.getZ() + random.nextDouble();
/*  77 */         level.addParticle(ParticleTypes.LARGE_SMOKE, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } else {
/*  80 */       if (canBurn(level.getBlockState(pos.west()))) {
/*  81 */         for (int i = 0; i < 2; i++) {
/*  82 */           double xx = pos.getX() + random.nextDouble() * 0.10000000149011612D;
/*  83 */           double yy = pos.getY() + random.nextDouble();
/*  84 */           double zz = pos.getZ() + random.nextDouble();
/*  85 */           level.addParticle(ParticleTypes.LARGE_SMOKE, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*  88 */       if (canBurn(level.getBlockState(pos.east()))) {
/*  89 */         for (int i = 0; i < 2; i++) {
/*  90 */           double xx = (pos.getX() + 1) - random.nextDouble() * 0.10000000149011612D;
/*  91 */           double yy = pos.getY() + random.nextDouble();
/*  92 */           double zz = pos.getZ() + random.nextDouble();
/*  93 */           level.addParticle(ParticleTypes.LARGE_SMOKE, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*  96 */       if (canBurn(level.getBlockState(pos.north()))) {
/*  97 */         for (int i = 0; i < 2; i++) {
/*  98 */           double xx = pos.getX() + random.nextDouble();
/*  99 */           double yy = pos.getY() + random.nextDouble();
/* 100 */           double zz = pos.getZ() + random.nextDouble() * 0.10000000149011612D;
/* 101 */           level.addParticle(ParticleTypes.LARGE_SMOKE, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/* 104 */       if (canBurn(level.getBlockState(pos.south()))) {
/* 105 */         for (int i = 0; i < 2; i++) {
/* 106 */           double xx = pos.getX() + random.nextDouble();
/* 107 */           double yy = pos.getY() + random.nextDouble();
/* 108 */           double zz = (pos.getZ() + 1) - random.nextDouble() * 0.10000000149011612D;
/* 109 */           level.addParticle(ParticleTypes.LARGE_SMOKE, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/* 112 */       if (canBurn(level.getBlockState(pos.above()))) {
/* 113 */         for (int i = 0; i < 2; i++) {
/* 114 */           double xx = pos.getX() + random.nextDouble();
/* 115 */           double yy = (pos.getY() + 1) - random.nextDouble() * 0.10000000149011612D;
/* 116 */           double zz = pos.getZ() + random.nextDouble();
/* 117 */           level.addParticle(ParticleTypes.LARGE_SMOKE, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 127 */     effectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 128 */     effectApplier.apply(InsideBlockEffectType.FIRE_IGNITE);
/* 129 */     effectApplier.runAfter(InsideBlockEffectType.FIRE_IGNITE, e -> e.hurt(e.level().damageSources().inFire(), this.fireDamage));
/*     */   }
/*     */   
/*     */   public static void fireIgnite(Entity entity) {
/* 133 */     if (!entity.fireImmune()) {
/* 134 */       if (entity.getRemainingFireTicks() < 0) {
/* 135 */         entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
/* 136 */       } else if (entity instanceof net.minecraft.server.level.ServerPlayer) {
/* 137 */         int addedFireTicks = entity.level().getRandom().nextInt(1, 3);
/* 138 */         entity.setRemainingFireTicks(entity.getRemainingFireTicks() + addedFireTicks);
/*     */       } 
/*     */       
/* 141 */       if (entity.getRemainingFireTicks() >= 0) {
/* 142 */         entity.igniteForSeconds(8.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 149 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/* 152 */     if (inPortalDimension(level)) {
/* 153 */       Optional<PortalShape> optionalShape = PortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X);
/*     */       
/* 155 */       if (optionalShape.isPresent()) {
/* 156 */         ((PortalShape)optionalShape.get()).createPortalBlocks(level);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     if (!state.canSurvive(level, pos)) {
/* 163 */       level.removeBlock(pos, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 168 */   private static boolean inPortalDimension(Level level) { return (level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 178 */     if (!level.isClientSide()) {
/* 179 */       level.levelEvent(null, 1009, pos, 0);
/*     */     }
/* 181 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   public static boolean canBePlacedAt(Level level, BlockPos pos, Direction forwardDirection) {
/* 185 */     BlockState state = level.getBlockState(pos);
/*     */     
/* 187 */     if (!state.isAir()) {
/* 188 */       return false;
/*     */     }
/*     */     
/* 191 */     return (getState(level, pos).canSurvive(level, pos) || isPortal(level, pos, forwardDirection));
/*     */   }
/*     */   
/*     */   private static boolean isPortal(Level level, BlockPos pos, Direction forwardDirection) {
/* 195 */     if (!inPortalDimension(level)) {
/* 196 */       return false;
/*     */     }
/* 198 */     BlockPos.MutableBlockPos testPos = pos.mutable();
/* 199 */     boolean hasObsidian = false;
/* 200 */     for (Direction face : Direction.values()) {
/* 201 */       if (level.getBlockState(testPos.set(pos).move(face)).is(Blocks.OBSIDIAN)) {
/* 202 */         hasObsidian = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 207 */     if (!hasObsidian) {
/* 208 */       return false;
/*     */     }
/*     */     
/* 211 */     Direction.Axis preferredAxis = forwardDirection.getAxis().isHorizontal() ? forwardDirection.getCounterClockWise().getAxis() : Direction.Plane.HORIZONTAL.getRandomAxis(level.random);
/* 212 */     return PortalShape.findEmptyPortalShape(level, pos, preferredAxis).isPresent();
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends BaseFireBlock> codec();
/*     */   
/*     */   protected abstract boolean canBurn(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BaseFireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */