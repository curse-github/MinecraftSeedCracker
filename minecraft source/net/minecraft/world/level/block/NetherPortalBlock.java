/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NetherPortalBlock extends Block implements Portal {
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  48 */   public static final MapCodec<NetherPortalBlock> CODEC = simpleCodec(NetherPortalBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  52 */   public MapCodec<NetherPortalBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  55 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
/*     */   
/*  57 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.column(4.0D, 16.0D, 0.0D, 16.0D));
/*     */   
/*     */   public NetherPortalBlock(BlockBehaviour.Properties properties) {
/*  60 */     super(properties);
/*  61 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AXIS, Direction.Axis.X));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(AXIS)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  71 */     if (level.isSpawningMonsters() && ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, pos)).booleanValue() && random.nextInt(2000) < level.getDifficulty().getId() && level.anyPlayerCloseEnoughForSpawning(pos)) {
/*     */       
/*  73 */       while (level.getBlockState(pos).is(this)) {
/*  74 */         pos = pos.below();
/*     */       }
/*  76 */       if (level.getBlockState(pos).isValidSpawn(level, pos, EntityType.ZOMBIFIED_PIGLIN)) {
/*  77 */         Entity entity = EntityType.ZOMBIFIED_PIGLIN.spawn(level, pos.above(), EntitySpawnReason.STRUCTURE);
/*  78 */         if (entity != null) {
/*  79 */           entity.setPortalCooldown();
/*  80 */           Entity vehicle = entity.getVehicle();
/*  81 */           if (vehicle != null) {
/*  82 */             vehicle.setPortalCooldown();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  91 */     Direction.Axis updateAxis = directionToNeighbour.getAxis();
/*  92 */     Direction.Axis axis = (Direction.Axis)state.getValue(AXIS);
/*     */     
/*  94 */     boolean wrongAxis = (axis != updateAxis && updateAxis.isHorizontal());
/*  95 */     if (wrongAxis || neighbourState.is(this) || PortalShape.findAnyShape(level, pos, axis).isComplete()) {
/*  96 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */     }
/*     */     
/*  99 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 104 */     if (entity.canUsePortal(false)) {
/* 105 */       entity.setAsInsidePortal(this, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPortalTransitionTime(ServerLevel level, Entity entity) {
/* 111 */     if (entity instanceof Player) { Player player = (Player)entity;
/* 112 */       return Math.max(0, ((Integer)level.getGameRules().get(
/* 113 */             (player.getAbilities()).invulnerable ? 
/* 114 */             GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : 
/* 115 */             GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue()); }
/*     */     
/* 117 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
/* 122 */     ResourceKey<Level> newDimension = (currentLevel.dimension() == Level.NETHER) ? Level.OVERWORLD : Level.NETHER;
/* 123 */     ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
/* 124 */     if (newLevel == null) {
/* 125 */       return null;
/*     */     }
/*     */     
/* 128 */     boolean toNether = (newLevel.dimension() == Level.NETHER);
/*     */     
/* 130 */     WorldBorder newWorldBorder = newLevel.getWorldBorder();
/* 131 */     double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
/* 132 */     BlockPos approximateExitPos = newWorldBorder.clampToBounds(entity
/* 133 */         .getX() * teleportationScale, entity
/* 134 */         .getY(), entity
/* 135 */         .getZ() * teleportationScale);
/*     */ 
/*     */     
/* 138 */     return getExitPortal(newLevel, entity, portalEntryPos, approximateExitPos, toNether, newWorldBorder);
/*     */   } private TeleportTransition getExitPortal(ServerLevel newLevel, Entity entity, BlockPos portalEntryPos, BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder) {
/*     */     TeleportTransition.PostTeleportTransition post;
/*     */     BlockUtil.FoundRectangle exitPortal;
/* 142 */     Optional<BlockPos> exitPortalPos = newLevel.getPortalForcer().findClosestPortalPosition(approximateExitPos, toNether, worldBorder);
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (exitPortalPos.isPresent()) {
/* 147 */       BlockPos pos = (BlockPos)exitPortalPos.get();
/* 148 */       BlockState portalState = newLevel.getBlockState(pos);
/*     */       
/* 150 */       exitPortal = BlockUtil.getLargestRectangleAround(pos, (Direction.Axis)portalState.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, blockPos -> (newLevel.getBlockState(blockPos) == portalState));
/* 151 */       post = TeleportTransition.PLAY_PORTAL_SOUND.then(e -> e.placePortalTicket(pos));
/*     */     } else {
/* 153 */       Direction.Axis sourcePortalAxis = (Direction.Axis)entity.level().getBlockState(portalEntryPos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
/* 154 */       Optional<BlockUtil.FoundRectangle> createdExit = newLevel.getPortalForcer().createPortal(approximateExitPos, sourcePortalAxis);
/* 155 */       if (createdExit.isEmpty()) {
/* 156 */         LOGGER.error("Unable to create a portal, likely target out of worldborder");
/* 157 */         return null;
/*     */       } 
/*     */       
/* 160 */       exitPortal = (BlockUtil.FoundRectangle)createdExit.get();
/* 161 */       post = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
/*     */     } 
/*     */     
/* 164 */     return getDimensionTransitionFromExit(entity, portalEntryPos, exitPortal, newLevel, post);
/*     */   }
/*     */ 
/*     */   
/*     */   private static TeleportTransition getDimensionTransitionFromExit(Entity entity, BlockPos portalEntryPos, BlockUtil.FoundRectangle exitPortal, ServerLevel newLevel, TeleportTransition.PostTeleportTransition postTeleportTransition) {
/*     */     Vec3 offset;
/*     */     Direction.Axis axis;
/* 171 */     BlockState blockState = entity.level().getBlockState(portalEntryPos);
/* 172 */     if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
/* 173 */       axis = (Direction.Axis)blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
/*     */ 
/*     */       
/* 176 */       BlockUtil.FoundRectangle portalArea = BlockUtil.getLargestRectangleAround(portalEntryPos, axis, 21, Direction.Axis.Y, 21, pos -> (entity.level().getBlockState(pos) == blockState));
/* 177 */       offset = entity.getRelativePortalPosition(axis, portalArea);
/*     */     } else {
/*     */       
/* 180 */       axis = Direction.Axis.X;
/* 181 */       offset = new Vec3(0.5D, 0.0D, 0.0D);
/*     */     } 
/* 183 */     return createDimensionTransition(newLevel, exitPortal, axis, offset, entity, postTeleportTransition);
/*     */   }
/*     */   
/*     */   private static TeleportTransition createDimensionTransition(ServerLevel newLevel, BlockUtil.FoundRectangle foundRectangle, Direction.Axis portalAxis, Vec3 offset, Entity entity, TeleportTransition.PostTeleportTransition postTeleportTransition) {
/* 187 */     BlockPos bottomLeft = foundRectangle.minCorner;
/* 188 */     BlockState blockState = newLevel.getBlockState(bottomLeft);
/* 189 */     Direction.Axis axis = (Direction.Axis)blockState.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
/* 190 */     double width = foundRectangle.axis1Size;
/* 191 */     double height = foundRectangle.axis2Size;
/* 192 */     EntityDimensions dimensions = entity.getDimensions(entity.getPose());
/*     */     
/* 194 */     int outputRotation = (portalAxis == axis) ? 0 : 90;
/*     */     
/* 196 */     double offsetRight = dimensions.width() / 2.0D + (width - dimensions.width()) * offset.x();
/* 197 */     double offsetUp = (height - dimensions.height()) * offset.y();
/* 198 */     double offsetForward = 0.5D + offset.z();
/*     */     
/* 200 */     boolean xAligned = (axis == Direction.Axis.X);
/*     */ 
/*     */ 
/*     */     
/* 204 */     Vec3 targetPos = new Vec3(bottomLeft.getX() + (xAligned ? offsetRight : offsetForward), bottomLeft.getY() + offsetUp, bottomLeft.getZ() + (xAligned ? offsetForward : offsetRight));
/*     */ 
/*     */     
/* 207 */     Vec3 collisionFreePos = PortalShape.findCollisionFreePosition(targetPos, newLevel, entity, dimensions);
/* 208 */     return new TeleportTransition(newLevel, collisionFreePos, Vec3.ZERO, outputRotation, 0.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }, ), postTeleportTransition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   public Portal.Transition getLocalTransition() { return Portal.Transition.CONFUSION; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 225 */     if (random.nextInt(100) == 0) {
/* 226 */       level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
/*     */     }
/*     */     
/* 229 */     for (int i = 0; i < 4; i++) {
/* 230 */       double x = pos.getX() + random.nextDouble();
/* 231 */       double y = pos.getY() + random.nextDouble();
/* 232 */       double z = pos.getZ() + random.nextDouble();
/* 233 */       double xa = (random.nextFloat() - 0.5D) * 0.5D;
/* 234 */       double ya = (random.nextFloat() - 0.5D) * 0.5D;
/* 235 */       double za = (random.nextFloat() - 0.5D) * 0.5D;
/*     */       
/* 237 */       int flip = random.nextInt(2) * 2 - 1;
/* 238 */       if (level.getBlockState(pos.west()).is(this) || level.getBlockState(pos.east()).is(this)) {
/* 239 */         z = pos.getZ() + 0.5D + 0.25D * flip;
/* 240 */         za = (random.nextFloat() * 2.0F * flip);
/*     */       } else {
/* 242 */         x = pos.getX() + 0.5D + 0.25D * flip;
/* 243 */         xa = (random.nextFloat() * 2.0F * flip);
/*     */       } 
/*     */       
/* 246 */       level.addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 252 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 257 */     switch (rotation) {
/*     */       case COUNTERCLOCKWISE_90:
/*     */       case CLOCKWISE_90:
/* 260 */         switch ((Direction.Axis)state.getValue(AXIS)) {
/*     */           case COUNTERCLOCKWISE_90:
/* 262 */             return (BlockState)state.setValue(AXIS, Direction.Axis.Z);
/*     */           case CLOCKWISE_90:
/* 264 */             return (BlockState)state.setValue(AXIS, Direction.Axis.X);
/*     */         } 
/* 266 */         return state;
/*     */     } 
/*     */     
/* 269 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 275 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AXIS }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\NetherPortalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */