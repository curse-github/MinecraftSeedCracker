/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RespawnAnchorBlock extends Block {
/*  46 */   public static final MapCodec<RespawnAnchorBlock> CODEC = simpleCodec(RespawnAnchorBlock::new);
/*     */   public static final int MIN_CHARGES = 0;
/*     */   public static final int MAX_CHARGES = 4;
/*     */   
/*  50 */   public MapCodec<RespawnAnchorBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final IntegerProperty CHARGE = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;
/*     */   
/*  57 */   private static final ImmutableList<Vec3i> RESPAWN_HORIZONTAL_OFFSETS = ImmutableList.of(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 0), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, 1));
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
/*  68 */   private static final ImmutableList<Vec3i> RESPAWN_OFFSETS = (new ImmutableList.Builder())
/*  69 */     .addAll(RESPAWN_HORIZONTAL_OFFSETS)
/*  70 */     .addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::below).iterator())
/*  71 */     .addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::above).iterator())
/*  72 */     .add(new Vec3i(0, 1, 0))
/*  73 */     .build();
/*     */   
/*     */   public RespawnAnchorBlock(BlockBehaviour.Properties properties) {
/*  76 */     super(properties);
/*  77 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(CHARGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  82 */     if (isRespawnFuel(itemStack) && 
/*  83 */       canBeCharged(state)) {
/*  84 */       charge(player, level, pos, state);
/*  85 */       itemStack.consume(1, player);
/*     */       
/*  87 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  90 */     if (hand == InteractionHand.MAIN_HAND && isRespawnFuel(player.getItemInHand(InteractionHand.OFF_HAND)) && canBeCharged(state))
/*     */     {
/*  92 */       return InteractionResult.PASS;
/*     */     }
/*  94 */     return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     ServerLevel serverLevel;
/*  99 */     if (((Integer)state.getValue(CHARGE)).intValue() == 0) {
/* 100 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 103 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 104 */     else { return InteractionResult.CONSUME; }
/*     */ 
/*     */     
/* 107 */     if (canSetSpawn(serverLevel, pos)) {
/* 108 */       if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 109 */         ServerPlayer.RespawnConfig respawnConfig = serverPlayer.getRespawnConfig();
/* 110 */         ServerPlayer.RespawnConfig newRespawnConfig = new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(serverLevel.dimension(), pos, 0.0F, 0.0F), false);
/* 111 */         if (respawnConfig == null || !respawnConfig.isSamePosition(newRespawnConfig)) {
/* 112 */           serverPlayer.setRespawnPosition(newRespawnConfig, true);
/* 113 */           serverLevel.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 114 */           return InteractionResult.SUCCESS_SERVER;
/*     */         }  }
/*     */       
/* 117 */       return InteractionResult.CONSUME;
/*     */     } 
/* 119 */     explode(state, serverLevel, pos);
/* 120 */     return InteractionResult.SUCCESS_SERVER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 125 */   private static boolean isRespawnFuel(ItemStack itemInHand) { return itemInHand.is(Items.GLOWSTONE); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   private static boolean canBeCharged(BlockState state) { return (((Integer)state.getValue(CHARGE)).intValue() < 4); }
/*     */ 
/*     */   
/*     */   private static boolean isWaterThatWouldFlow(BlockPos pos, Level level) {
/* 133 */     FluidState fluid = level.getFluidState(pos);
/* 134 */     if (!fluid.is(FluidTags.WATER)) {
/* 135 */       return false;
/*     */     }
/* 137 */     if (fluid.isSource()) {
/* 138 */       return true;
/*     */     }
/* 140 */     float amount = fluid.getAmount();
/* 141 */     if (amount < 2.0F) {
/* 142 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 146 */     FluidState fluidBelow = level.getFluidState(pos.below());
/* 147 */     return !fluidBelow.is(FluidTags.WATER);
/*     */   }
/*     */   
/*     */   private void explode(BlockState state, ServerLevel level, final BlockPos pos) {
/* 151 */     level.removeBlock(pos, false);
/* 152 */     Objects.requireNonNull(pos);
/* 153 */     boolean anyWaterNeighbors = Direction.Plane.HORIZONTAL.stream().map(pos::relative).anyMatch(neighborPos -> isWaterThatWouldFlow(neighborPos, level));
/* 154 */     final boolean inWater = (anyWaterNeighbors || level.getFluidState(pos.above()).is(FluidTags.WATER));
/* 155 */     ExplosionDamageCalculator damageCalculator = new ExplosionDamageCalculator(this)
/*     */       {
/*     */         public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos testPos, BlockState block, FluidState fluid) {
/* 158 */           if (testPos.equals(pos) && inWater)
/*     */           {
/* 160 */             return Optional.of(Float.valueOf(Blocks.WATER.getExplosionResistance()));
/*     */           }
/* 162 */           return super.getBlockExplosionResistance(explosion, level, testPos, block, fluid);
/*     */         }
/*     */       };
/* 165 */     Vec3 boomPos = pos.getCenter();
/* 166 */     level.explode(null, level.damageSources().badRespawnPointExplosion(boomPos), damageCalculator, boomPos, 5.0F, true, Level.ExplosionInteraction.BLOCK);
/*     */   }
/*     */ 
/*     */   
/* 170 */   public static boolean canSetSpawn(ServerLevel level, BlockPos pos) { return ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, pos)).booleanValue(); }
/*     */ 
/*     */   
/*     */   public static void charge(Entity sourceEntity, Level level, BlockPos pos, BlockState state) {
/* 174 */     BlockState newState = (BlockState)state.setValue(CHARGE, Integer.valueOf(((Integer)state.getValue(CHARGE)).intValue() + 1));
/* 175 */     level.setBlock(pos, newState, 3);
/* 176 */     level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
/* 177 */     level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 182 */     if (((Integer)state.getValue(CHARGE)).intValue() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 186 */     if (random.nextInt(100) == 0) {
/* 187 */       level.playLocalSound(pos, SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */     }
/*     */     
/* 190 */     double x = pos.getX() + 0.5D + 0.5D - random.nextDouble();
/* 191 */     double y = pos.getY() + 1.0D;
/* 192 */     double z = pos.getZ() + 0.5D + 0.5D - random.nextDouble();
/* 193 */     double ya = random.nextFloat() * 0.04D;
/*     */     
/* 195 */     level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, 0.0D, ya, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 200 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { CHARGE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/* 209 */   public static int getScaledChargeLevel(BlockState state, int maximum) { return Mth.floor((((Integer)state.getValue(CHARGE)).intValue() - 0) / 4.0F * maximum); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 214 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return getScaledChargeLevel(state, 15); }
/*     */ 
/*     */   
/*     */   public static Optional<Vec3> findStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos) {
/* 218 */     Optional<Vec3> safePosition = findStandUpPosition(type, level, pos, true);
/* 219 */     if (safePosition.isPresent()) {
/* 220 */       return safePosition;
/*     */     }
/* 222 */     return findStandUpPosition(type, level, pos, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, boolean checkDangerous) {
/* 226 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 227 */     for (UnmodifiableIterator unmodifiableIterator = RESPAWN_OFFSETS.iterator(); unmodifiableIterator.hasNext(); ) { Vec3i offset = (Vec3i)unmodifiableIterator.next();
/* 228 */       blockPos.set(pos).move(offset);
/*     */       
/* 230 */       Vec3 position = DismountHelper.findSafeDismountLocation(type, level, blockPos, checkDangerous);
/* 231 */       if (position != null) {
/* 232 */         return Optional.of(position);
/*     */       } }
/*     */     
/* 235 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 240 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RespawnAnchorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */