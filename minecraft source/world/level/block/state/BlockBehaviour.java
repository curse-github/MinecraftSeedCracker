/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.resources.DependantName;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.flag.FeatureElement;
/*      */ import net.minecraft.world.flag.FeatureFlag;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ScheduledTickAccess;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.EntityBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.material.MapColor;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ 
/*      */ public abstract class BlockBehaviour
/*      */   implements FeatureElement
/*      */ {
/*   92 */   protected static final Direction[] UPDATE_SHAPE_ORDER = { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP };
/*      */   
/*      */   protected final boolean hasCollision;
/*      */   
/*      */   protected final float explosionResistance;
/*      */   protected final boolean isRandomlyTicking;
/*      */   protected final SoundType soundType;
/*      */   protected final float friction;
/*      */   protected final float speedFactor;
/*      */   protected final float jumpFactor;
/*      */   protected final boolean dynamicShape;
/*      */   protected final FeatureFlagSet requiredFeatures;
/*      */   protected final Properties properties;
/*      */   protected final Optional<ResourceKey<LootTable>> drops;
/*      */   protected final String descriptionId;
/*      */   
/*      */   public BlockBehaviour(Properties properties) {
/*  109 */     this.hasCollision = properties.hasCollision;
/*  110 */     this.drops = properties.effectiveDrops();
/*  111 */     this.descriptionId = properties.effectiveDescriptionId();
/*  112 */     this.explosionResistance = properties.explosionResistance;
/*  113 */     this.isRandomlyTicking = properties.isRandomlyTicking;
/*  114 */     this.soundType = properties.soundType;
/*  115 */     this.friction = properties.friction;
/*  116 */     this.speedFactor = properties.speedFactor;
/*  117 */     this.jumpFactor = properties.jumpFactor;
/*  118 */     this.dynamicShape = properties.dynamicShape;
/*  119 */     this.requiredFeatures = properties.requiredFeatures;
/*  120 */     this.properties = properties;
/*      */   }
/*      */ 
/*      */   
/*  124 */   public Properties properties() { return this.properties; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  136 */   protected static <B extends Block> RecordCodecBuilder<B, Properties> propertiesCodec() { return Properties.CODEC.fieldOf("properties").forGetter(BlockBehaviour::properties); }
/*      */ 
/*      */ 
/*      */   
/*  140 */   public static <B extends Block> MapCodec<B> simpleCodec(Function<Properties, B> constructor) { return RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec()).apply(i, constructor)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags, int updateLimit) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  150 */     switch (type) {
/*      */       case LAND:
/*  152 */         return !state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */       case WATER:
/*  154 */         return state.getFluidState().is(FluidTags.WATER);
/*      */       case AIR:
/*  156 */         return !state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */     } 
/*  158 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  172 */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) { return state; }
/*      */ 
/*      */ 
/*      */   
/*  176 */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/*  196 */     if (state.isAir() || explosion.getBlockInteraction() == Explosion.BlockInteraction.TRIGGER_BLOCK) {
/*      */       return;
/*      */     }
/*      */     
/*  200 */     Block block = state.getBlock();
/*  201 */     boolean doDropExperienceHack = explosion.getIndirectSourceEntity() instanceof Player;
/*      */     
/*  203 */     if (block.dropFromExplosion(explosion)) {
/*  204 */       BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  209 */       LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getDirectSourceEntity());
/*      */       
/*  211 */       if (explosion.getBlockInteraction() == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
/*  212 */         params.withParameter(LootContextParams.EXPLOSION_RADIUS, Float.valueOf(explosion.radius()));
/*      */       }
/*      */       
/*  215 */       state.spawnAfterBreak(level, pos, ItemStack.EMPTY, doDropExperienceHack);
/*  216 */       state.getDrops(params).forEach(stack -> onHit.accept(stack, pos));
/*      */     } 
/*      */     
/*  219 */     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/*  220 */     block.wasExploded(level, pos, explosion);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  228 */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) { return InteractionResult.PASS; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  235 */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) { return InteractionResult.TRY_WITH_EMPTY_HAND; }
/*      */ 
/*      */ 
/*      */   
/*  239 */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  245 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
/*      */ 
/*      */ 
/*      */   
/*  249 */   protected boolean useShapeForLightOcclusion(BlockState state) { return false; }
/*      */ 
/*      */ 
/*      */   
/*  253 */   protected boolean isSignalSource(BlockState state) { return false; }
/*      */ 
/*      */ 
/*      */   
/*  257 */   protected FluidState getFluidState(BlockState state) { return Fluids.EMPTY.defaultFluidState(); }
/*      */ 
/*      */ 
/*      */   
/*  261 */   protected boolean hasAnalogOutputSignal(BlockState state) { return false; }
/*      */ 
/*      */ 
/*      */   
/*  265 */   protected float getMaxHorizontalOffset() { return 0.25F; }
/*      */ 
/*      */ 
/*      */   
/*  269 */   protected float getMaxVerticalOffset() { return 0.2F; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  274 */   public FeatureFlagSet requiredFeatures() { return this.requiredFeatures; }
/*      */ 
/*      */ 
/*      */   
/*  278 */   protected boolean shouldChangedStateKeepBlockEntity(BlockState oldState) { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  284 */   protected BlockState rotate(BlockState state, Rotation rotation) { return state; }
/*      */ 
/*      */ 
/*      */   
/*  288 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  300 */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) { return (state.canBeReplaced() && (context.getItemInHand().isEmpty() || !context.getItemInHand().is(asItem()))); }
/*      */ 
/*      */ 
/*      */   
/*  304 */   protected boolean canBeReplaced(BlockState state, Fluid fluid) { return (state.canBeReplaced() || !state.isSolid()); }
/*      */ 
/*      */   
/*      */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/*  308 */     if (this.drops.isEmpty()) {
/*  309 */       return Collections.emptyList();
/*      */     }
/*  311 */     LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
/*  312 */     ServerLevel level = lootParams.getLevel();
/*  313 */     LootTable table = level.getServer().reloadableRegistries().getLootTable((ResourceKey)this.drops.get());
/*  314 */     return table.getRandomItems(lootParams);
/*      */   }
/*      */ 
/*      */   
/*  318 */   protected long getSeed(BlockState state, BlockPos pos) { return Mth.getSeed(pos); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  324 */   protected VoxelShape getOcclusionShape(BlockState state) { return state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO); }
/*      */ 
/*      */ 
/*      */   
/*  328 */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return getCollisionShape(state, level, pos, CollisionContext.empty()); }
/*      */ 
/*      */ 
/*      */   
/*  332 */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) { return Shapes.empty(); }
/*      */ 
/*      */   
/*      */   protected int getLightBlock(BlockState state) {
/*  336 */     if (state.isSolidRender()) {
/*  337 */       return 15;
/*      */     }
/*  339 */     return state.propagatesSkylightDown() ? 0 : 1;
/*      */   }
/*      */ 
/*      */   
/*  343 */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) { return null; }
/*      */ 
/*      */ 
/*      */   
/*  347 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return true; }
/*      */ 
/*      */ 
/*      */   
/*  351 */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return state.isCollisionShapeFullBlock(level, pos) ? 0.2F : 1.0F; }
/*      */ 
/*      */ 
/*      */   
/*  355 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return 0; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  361 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.block(); }
/*      */ 
/*      */ 
/*      */   
/*  365 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return this.hasCollision ? state.getShape(level, pos) : Shapes.empty(); }
/*      */ 
/*      */ 
/*      */   
/*  369 */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) { return Shapes.block(); }
/*      */ 
/*      */ 
/*      */   
/*  373 */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) { return Block.isShapeFullBlock(state.getCollisionShape(level, pos)); }
/*      */ 
/*      */ 
/*      */   
/*  377 */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return getCollisionShape(state, level, pos, context); }
/*      */ 
/*      */   
/*      */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {}
/*      */ 
/*      */   
/*      */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {}
/*      */ 
/*      */   
/*      */   protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
/*  387 */     float destroySpeed = state.getDestroySpeed(level, pos);
/*  388 */     if (destroySpeed == -1.0F) {
/*  389 */       return 0.0F;
/*      */     }
/*  391 */     int modifier = player.hasCorrectToolForDrops(state) ? 30 : 100;
/*  392 */     return player.getDestroySpeed(state) / destroySpeed / modifier;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {}
/*      */ 
/*      */   
/*      */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) {}
/*      */ 
/*      */   
/*  402 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 0; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  413 */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 0; }
/*      */ 
/*      */ 
/*      */   
/*  417 */   public final Optional<ResourceKey<LootTable>> getLootTable() { return this.drops; }
/*      */ 
/*      */ 
/*      */   
/*  421 */   public final String getDescriptionId() { return this.descriptionId; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {}
/*      */ 
/*      */   
/*  428 */   protected boolean propagatesSkylightDown(BlockState state) { return (!Block.isShapeFullBlock(state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) && state.getFluidState().isEmpty()); }
/*      */ 
/*      */ 
/*      */   
/*  432 */   protected boolean isRandomlyTicking(BlockState state) { return this.isRandomlyTicking; }
/*      */ 
/*      */ 
/*      */   
/*  436 */   protected SoundType getSoundType(BlockState state) { return this.soundType; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  441 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(asItem()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum OffsetType
/*      */   {
/*  449 */     NONE,
/*  450 */     XZ,
/*  451 */     XYZ;
/*      */   }
/*      */ 
/*      */   
/*  455 */   public MapColor defaultMapColor() { return (MapColor)this.properties.mapColor.apply(asBlock().defaultBlockState()); }
/*      */ 
/*      */ 
/*      */   
/*  459 */   public float defaultDestroyTime() { return this.properties.destroyTime; }
/*      */   protected abstract MapCodec<? extends Block> codec();
/*      */   public abstract Item asItem();
/*      */   protected abstract Block asBlock();
/*      */   public static class Properties { private Function<BlockState, MapColor> mapColor; private boolean hasCollision; private SoundType soundType; private ToIntFunction<BlockState> lightEmission; private float explosionResistance; private float destroyTime; private boolean requiresCorrectToolForDrops; private boolean isRandomlyTicking;
/*  464 */     public static final Codec<Properties> CODEC = MapCodec.unitCodec(() -> of()); private float friction; private float speedFactor; private float jumpFactor; private ResourceKey<Block> id; private DependantName<Block, Optional<ResourceKey<LootTable>>> drops; private DependantName<Block, String> descriptionId; private boolean canOcclude; private boolean isAir;
/*      */     private Properties() {
/*  466 */       this.mapColor = (state -> MapColor.NONE);
/*  467 */       this.hasCollision = true;
/*      */       
/*  469 */       this.soundType = SoundType.STONE;
/*  470 */       this.lightEmission = (state -> 0);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  475 */       this.friction = 0.6F;
/*  476 */       this.speedFactor = 1.0F;
/*  477 */       this.jumpFactor = 1.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  482 */       this.drops = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("blocks/"))));
/*  483 */       this.descriptionId = (id -> Util.makeDescriptionId("block", id.identifier()));
/*  484 */       this.canOcclude = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  492 */       this.pushReaction = PushReaction.NORMAL;
/*  493 */       this.spawnTerrainParticles = true;
/*  494 */       this.instrument = NoteBlockInstrument.HARP;
/*      */ 
/*      */       
/*  497 */       this.isValidSpawn = ((state, level, pos, entityType) -> 
/*  498 */         (state.isFaceSturdy(level, pos, Direction.UP) && state.getLightEmission() < 14));
/*      */       
/*  500 */       this.isRedstoneConductor = ((state, level, pos) -> 
/*  501 */         state.isCollisionShapeFullBlock(level, pos));
/*      */       
/*  503 */       this.isSuffocating = ((state, level, pos) -> 
/*  504 */         (state.blocksMotion() && state.isCollisionShapeFullBlock(level, pos)));
/*      */       
/*  506 */       this.isViewBlocking = this.isSuffocating;
/*  507 */       this.hasPostProcess = ((state, level, pos) -> false);
/*  508 */       this.emissiveRendering = ((state, level, pos) -> false);
/*      */ 
/*      */       
/*  511 */       this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*      */     }
/*      */     private boolean ignitedByLava; @Deprecated
/*      */     private boolean liquid; @Deprecated
/*      */     private boolean forceSolidOff; private boolean forceSolidOn; private PushReaction pushReaction; private boolean spawnTerrainParticles; private NoteBlockInstrument instrument; private boolean replaceable; private BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn; private BlockBehaviour.StatePredicate isRedstoneConductor; private BlockBehaviour.StatePredicate isSuffocating; private BlockBehaviour.StatePredicate isViewBlocking; private BlockBehaviour.StatePredicate hasPostProcess; private BlockBehaviour.StatePredicate emissiveRendering; private boolean dynamicShape;
/*      */     private FeatureFlagSet requiredFeatures;
/*      */     private BlockBehaviour.OffsetFunction offsetFunction;
/*      */     
/*  519 */     public static Properties of() { return new Properties(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Properties ofFullCopy(BlockBehaviour block) {
/*  531 */       Properties copyTo = ofLegacyCopy(block);
/*  532 */       Properties copyFrom = block.properties;
/*      */       
/*  534 */       copyTo.jumpFactor = copyFrom.jumpFactor;
/*  535 */       copyTo.isRedstoneConductor = copyFrom.isRedstoneConductor;
/*  536 */       copyTo.isValidSpawn = copyFrom.isValidSpawn;
/*  537 */       copyTo.hasPostProcess = copyFrom.hasPostProcess;
/*  538 */       copyTo.isSuffocating = copyFrom.isSuffocating;
/*  539 */       copyTo.isViewBlocking = copyFrom.isViewBlocking;
/*  540 */       copyTo.drops = copyFrom.drops;
/*  541 */       copyTo.descriptionId = copyFrom.descriptionId;
/*      */       
/*  543 */       return copyTo;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public static Properties ofLegacyCopy(BlockBehaviour block) {
/*  552 */       Properties copyTo = new Properties();
/*  553 */       Properties copyFrom = block.properties;
/*      */       
/*  555 */       copyTo.destroyTime = copyFrom.destroyTime;
/*  556 */       copyTo.explosionResistance = copyFrom.explosionResistance;
/*  557 */       copyTo.hasCollision = copyFrom.hasCollision;
/*  558 */       copyTo.isRandomlyTicking = copyFrom.isRandomlyTicking;
/*  559 */       copyTo.lightEmission = copyFrom.lightEmission;
/*  560 */       copyTo.mapColor = copyFrom.mapColor;
/*  561 */       copyTo.soundType = copyFrom.soundType;
/*  562 */       copyTo.friction = copyFrom.friction;
/*  563 */       copyTo.speedFactor = copyFrom.speedFactor;
/*  564 */       copyTo.dynamicShape = copyFrom.dynamicShape;
/*  565 */       copyTo.canOcclude = copyFrom.canOcclude;
/*  566 */       copyTo.isAir = copyFrom.isAir;
/*  567 */       copyTo.ignitedByLava = copyFrom.ignitedByLava;
/*  568 */       copyTo.liquid = copyFrom.liquid;
/*  569 */       copyTo.forceSolidOff = copyFrom.forceSolidOff;
/*  570 */       copyTo.forceSolidOn = copyFrom.forceSolidOn;
/*  571 */       copyTo.pushReaction = copyFrom.pushReaction;
/*  572 */       copyTo.requiresCorrectToolForDrops = copyFrom.requiresCorrectToolForDrops;
/*  573 */       copyTo.offsetFunction = copyFrom.offsetFunction;
/*  574 */       copyTo.spawnTerrainParticles = copyFrom.spawnTerrainParticles;
/*  575 */       copyTo.requiredFeatures = copyFrom.requiredFeatures;
/*  576 */       copyTo.emissiveRendering = copyFrom.emissiveRendering;
/*  577 */       copyTo.instrument = copyFrom.instrument;
/*  578 */       copyTo.replaceable = copyFrom.replaceable;
/*      */       
/*  580 */       return copyTo;
/*      */     }
/*      */     
/*      */     public Properties mapColor(DyeColor dyeColor) {
/*  584 */       this.mapColor = (state -> dyeColor.getMapColor());
/*  585 */       return this;
/*      */     }
/*      */     
/*      */     public Properties mapColor(MapColor mapColor) {
/*  589 */       this.mapColor = (state -> mapColor);
/*  590 */       return this;
/*      */     }
/*      */     
/*      */     public Properties mapColor(Function<BlockState, MapColor> mapColor) {
/*  594 */       this.mapColor = mapColor;
/*  595 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noCollision() {
/*  599 */       this.hasCollision = false;
/*  600 */       this.canOcclude = false;
/*  601 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noOcclusion() {
/*  605 */       this.canOcclude = false;
/*  606 */       return this;
/*      */     }
/*      */     
/*      */     public Properties friction(float friction) {
/*  610 */       this.friction = friction;
/*  611 */       return this;
/*      */     }
/*      */     
/*      */     public Properties speedFactor(float speedFactor) {
/*  615 */       this.speedFactor = speedFactor;
/*  616 */       return this;
/*      */     }
/*      */     
/*      */     public Properties jumpFactor(float jumpFactor) {
/*  620 */       this.jumpFactor = jumpFactor;
/*  621 */       return this;
/*      */     }
/*      */     
/*      */     public Properties sound(SoundType soundType) {
/*  625 */       this.soundType = soundType;
/*  626 */       return this;
/*      */     }
/*      */     
/*      */     public Properties lightLevel(ToIntFunction<BlockState> lightEmission) {
/*  630 */       this.lightEmission = lightEmission;
/*  631 */       return this;
/*      */     }
/*      */ 
/*      */     
/*  635 */     public Properties strength(float destroyTime, float explosionResistance) { return destroyTime(destroyTime).explosionResistance(explosionResistance); }
/*      */ 
/*      */ 
/*      */     
/*  639 */     public Properties instabreak() { return strength(0.0F); }
/*      */ 
/*      */     
/*      */     public Properties strength(float destroyTime) {
/*  643 */       strength(destroyTime, destroyTime);
/*  644 */       return this;
/*      */     }
/*      */     
/*      */     public Properties randomTicks() {
/*  648 */       this.isRandomlyTicking = true;
/*  649 */       return this;
/*      */     }
/*      */     
/*      */     public Properties dynamicShape() {
/*  653 */       this.dynamicShape = true;
/*  654 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Properties noLootTable() {
/*  662 */       this.drops = DependantName.fixed(Optional.empty());
/*  663 */       return this;
/*      */     }
/*      */     
/*      */     public Properties overrideLootTable(Optional<ResourceKey<LootTable>> table) {
/*  667 */       this.drops = DependantName.fixed(table);
/*  668 */       return this;
/*      */     }
/*      */ 
/*      */     
/*  672 */     protected Optional<ResourceKey<LootTable>> effectiveDrops() { return (Optional)this.drops.get((ResourceKey)Objects.requireNonNull(this.id, "Block id not set")); }
/*      */ 
/*      */     
/*      */     public Properties ignitedByLava() {
/*  676 */       this.ignitedByLava = true;
/*  677 */       return this;
/*      */     }
/*      */     
/*      */     public Properties liquid() {
/*  681 */       this.liquid = true;
/*  682 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Properties forceSolidOn() {
/*  689 */       this.forceSolidOn = true;
/*  690 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Properties forceSolidOff() {
/*  699 */       this.forceSolidOff = true;
/*  700 */       return this;
/*      */     }
/*      */     
/*      */     public Properties pushReaction(PushReaction pushReaction) {
/*  704 */       this.pushReaction = pushReaction;
/*  705 */       return this;
/*      */     }
/*      */     
/*      */     public Properties air() {
/*  709 */       this.isAir = true;
/*  710 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) {
/*  714 */       this.isValidSpawn = isValidSpawn;
/*  715 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isRedstoneConductor(BlockBehaviour.StatePredicate isRedstoneConductor) {
/*  719 */       this.isRedstoneConductor = isRedstoneConductor;
/*  720 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isSuffocating(BlockBehaviour.StatePredicate isSuffocating) {
/*  724 */       this.isSuffocating = isSuffocating;
/*  725 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking) {
/*  729 */       this.isViewBlocking = isViewBlocking;
/*  730 */       return this;
/*      */     }
/*      */     
/*      */     public Properties hasPostProcess(BlockBehaviour.StatePredicate hasPostProcess) {
/*  734 */       this.hasPostProcess = hasPostProcess;
/*  735 */       return this;
/*      */     }
/*      */     
/*      */     public Properties emissiveRendering(BlockBehaviour.StatePredicate emissiveRendering) {
/*  739 */       this.emissiveRendering = emissiveRendering;
/*  740 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiresCorrectToolForDrops() {
/*  744 */       this.requiresCorrectToolForDrops = true;
/*  745 */       return this;
/*      */     }
/*      */     
/*      */     public Properties destroyTime(float destroyTime) {
/*  749 */       this.destroyTime = destroyTime;
/*  750 */       return this;
/*      */     }
/*      */     
/*      */     public Properties explosionResistance(float explosionResistance) {
/*  754 */       this.explosionResistance = Math.max(0.0F, explosionResistance);
/*  755 */       return this;
/*      */     }
/*      */     
/*      */     public Properties offsetType(BlockBehaviour.OffsetType offsetType) {
/*  759 */       switch (offsetType.ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  771 */         .offsetFunction = ((state, pos) -> {
/*  772 */           Block block = state.getBlock();
/*  773 */           long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
/*      */           
/*  775 */           float maxHorizontalOffset = block.getMaxHorizontalOffset();
/*  776 */           double x = Mth.clamp((((float)(seed & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*  777 */           double z = Mth.clamp((((float)(seed >> 8 & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*  778 */           return new Vec3(x, 0.0D, z);
/*      */         });
/*      */       
/*  781 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noTerrainParticles() {
/*  785 */       this.spawnTerrainParticles = false;
/*  786 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiredFeatures(FeatureFlag... flags) {
/*  790 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/*  791 */       return this;
/*      */     }
/*      */     
/*      */     public Properties instrument(NoteBlockInstrument instrument) {
/*  795 */       this.instrument = instrument;
/*  796 */       return this;
/*      */     }
/*      */     
/*      */     public Properties replaceable() {
/*  800 */       this.replaceable = true;
/*  801 */       return this;
/*      */     }
/*      */     
/*      */     public Properties setId(ResourceKey<Block> id) {
/*  805 */       this.id = id;
/*  806 */       return this;
/*      */     }
/*      */     
/*      */     public Properties overrideDescription(String descriptionId) {
/*  810 */       this.descriptionId = DependantName.fixed(descriptionId);
/*  811 */       return this;
/*      */     }
/*      */ 
/*      */     
/*  815 */     protected String effectiveDescriptionId() { return (String)this.descriptionId.get((ResourceKey)Objects.requireNonNull(this.id, "Block id not set")); } }
/*      */ 
/*      */   
/*      */   public static abstract class BlockStateBase
/*      */     extends StateHolder<Block, BlockState> {
/*  820 */     private static final Direction[] DIRECTIONS = Direction.values();
/*  821 */     private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill(s, Shapes.empty()));
/*  822 */     private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill(s, Shapes.block()));
/*      */     
/*      */     private final int lightEmission;
/*      */     
/*      */     private final boolean useShapeForLightOcclusion;
/*      */     private final boolean isAir;
/*      */     private final boolean ignitedByLava;
/*      */     @Deprecated
/*      */     private final boolean liquid;
/*      */     @Deprecated
/*      */     private boolean legacySolid;
/*      */     private final PushReaction pushReaction;
/*      */     private final MapColor mapColor;
/*      */     private final float destroySpeed;
/*      */     private final boolean requiresCorrectToolForDrops;
/*      */     private final boolean canOcclude;
/*      */     private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */     private final BlockBehaviour.StatePredicate isSuffocating;
/*      */     private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */     private final BlockBehaviour.StatePredicate hasPostProcess;
/*      */     private final BlockBehaviour.StatePredicate emissiveRendering;
/*      */     private final BlockBehaviour.OffsetFunction offsetFunction;
/*      */     private final boolean spawnTerrainParticles;
/*      */     private final NoteBlockInstrument instrument;
/*      */     private final boolean replaceable;
/*      */     private Cache cache;
/*  848 */     private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
/*      */     private boolean isRandomlyTicking;
/*      */     private boolean solidRender;
/*      */     private VoxelShape occlusionShape;
/*      */     private VoxelShape[] occlusionShapesByFace;
/*      */     private boolean propagatesSkylightDown;
/*      */     private int lightBlock;
/*      */     
/*      */     protected BlockStateBase(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
/*  857 */       super(owner, values, propertiesCodec);
/*  858 */       BlockBehaviour.Properties properties = owner.properties;
/*      */       
/*  860 */       this.lightEmission = properties.lightEmission.applyAsInt(asState());
/*  861 */       this.useShapeForLightOcclusion = owner.useShapeForLightOcclusion(asState());
/*  862 */       this.isAir = properties.isAir;
/*  863 */       this.ignitedByLava = properties.ignitedByLava;
/*  864 */       this.liquid = properties.liquid;
/*  865 */       this.pushReaction = properties.pushReaction;
/*  866 */       this.mapColor = (MapColor)properties.mapColor.apply(asState());
/*  867 */       this.destroySpeed = properties.destroyTime;
/*  868 */       this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
/*  869 */       this.canOcclude = properties.canOcclude;
/*  870 */       this.isRedstoneConductor = properties.isRedstoneConductor;
/*  871 */       this.isSuffocating = properties.isSuffocating;
/*  872 */       this.isViewBlocking = properties.isViewBlocking;
/*  873 */       this.hasPostProcess = properties.hasPostProcess;
/*  874 */       this.emissiveRendering = properties.emissiveRendering;
/*  875 */       this.offsetFunction = properties.offsetFunction;
/*  876 */       this.spawnTerrainParticles = properties.spawnTerrainParticles;
/*  877 */       this.instrument = properties.instrument;
/*  878 */       this.replaceable = properties.replaceable;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean calculateSolid() {
/*  887 */       if (((Block)this.owner).properties.forceSolidOn) {
/*  888 */         return true;
/*      */       }
/*  890 */       if (((Block)this.owner).properties.forceSolidOff) {
/*  891 */         return false;
/*      */       }
/*  893 */       if (this.cache == null) {
/*  894 */         return false;
/*      */       }
/*  896 */       VoxelShape shape = this.cache.collisionShape;
/*  897 */       if (shape.isEmpty()) {
/*  898 */         return false;
/*      */       }
/*  900 */       AABB bounds = shape.bounds();
/*  901 */       if (bounds.getSize() >= 0.7291666666666666D) {
/*  902 */         return true;
/*      */       }
/*  904 */       if (bounds.getYsize() >= 1.0D) {
/*  905 */         return true;
/*      */       }
/*  907 */       return false;
/*      */     }
/*      */     
/*      */     public void initCache() {
/*  911 */       this.fluidState = ((Block)this.owner).getFluidState(asState());
/*  912 */       this.isRandomlyTicking = ((Block)this.owner).isRandomlyTicking(asState());
/*  913 */       if (!getBlock().hasDynamicShape()) {
/*  914 */         this.cache = new Cache(asState());
/*      */       }
/*  916 */       this.legacySolid = calculateSolid();
/*      */       
/*  918 */       this.occlusionShape = this.canOcclude ? ((Block)this.owner).getOcclusionShape(asState()) : Shapes.empty();
/*  919 */       this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
/*      */       
/*  921 */       if (this.occlusionShape.isEmpty()) {
/*  922 */         this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
/*  923 */       } else if (this.solidRender) {
/*  924 */         this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
/*      */       } else {
/*  926 */         this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];
/*  927 */         for (Direction direction : DIRECTIONS) {
/*  928 */           this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction);
/*      */         }
/*      */       } 
/*      */       
/*  932 */       this.propagatesSkylightDown = ((Block)this.owner).propagatesSkylightDown(asState());
/*  933 */       this.lightBlock = ((Block)this.owner).getLightBlock(asState());
/*      */     }
/*      */ 
/*      */     
/*  937 */     public Block getBlock() { return (Block)this.owner; }
/*      */ 
/*      */ 
/*      */     
/*  941 */     public Holder<Block> getBlockHolder() { return ((Block)this.owner).builtInRegistryHolder(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public boolean blocksMotion() {
/*  957 */       Block block = getBlock();
/*  958 */       return (block != Blocks.COBWEB && block != Blocks.BAMBOO_SAPLING && isSolid());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*  976 */     public boolean isSolid() { return this.legacySolid; }
/*      */ 
/*      */ 
/*      */     
/*  980 */     public boolean isValidSpawn(BlockGetter level, BlockPos pos, EntityType<?> type) { return (getBlock()).properties.isValidSpawn.test(asState(), level, pos, type); }
/*      */ 
/*      */ 
/*      */     
/*  984 */     public boolean propagatesSkylightDown() { return this.propagatesSkylightDown; }
/*      */ 
/*      */ 
/*      */     
/*  988 */     public int getLightBlock() { return this.lightBlock; }
/*      */ 
/*      */ 
/*      */     
/*  992 */     public VoxelShape getFaceOcclusionShape(Direction direction) { return this.occlusionShapesByFace[direction.ordinal()]; }
/*      */ 
/*      */ 
/*      */     
/*  996 */     public VoxelShape getOcclusionShape() { return this.occlusionShape; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1001 */     public boolean hasLargeCollisionShape() { return (this.cache == null || this.cache.largeCollisionShape); }
/*      */ 
/*      */ 
/*      */     
/* 1005 */     public boolean useShapeForLightOcclusion() { return this.useShapeForLightOcclusion; }
/*      */ 
/*      */ 
/*      */     
/* 1009 */     public int getLightEmission() { return this.lightEmission; }
/*      */ 
/*      */ 
/*      */     
/* 1013 */     public boolean isAir() { return this.isAir; }
/*      */ 
/*      */ 
/*      */     
/* 1017 */     public boolean ignitedByLava() { return this.ignitedByLava; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/* 1023 */     public boolean liquid() { return this.liquid; }
/*      */ 
/*      */ 
/*      */     
/* 1027 */     public MapColor getMapColor(BlockGetter level, BlockPos pos) { return this.mapColor; }
/*      */ 
/*      */ 
/*      */     
/* 1031 */     public BlockState rotate(Rotation rotation) { return getBlock().rotate(asState(), rotation); }
/*      */ 
/*      */ 
/*      */     
/* 1035 */     public BlockState mirror(Mirror mirror) { return getBlock().mirror(asState(), mirror); }
/*      */ 
/*      */ 
/*      */     
/* 1039 */     public RenderShape getRenderShape() { return getBlock().getRenderShape(asState()); }
/*      */ 
/*      */ 
/*      */     
/* 1043 */     public boolean emissiveRendering(BlockGetter level, BlockPos pos) { return this.emissiveRendering.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1047 */     public float getShadeBrightness(BlockGetter level, BlockPos pos) { return getBlock().getShadeBrightness(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1051 */     public boolean isRedstoneConductor(BlockGetter level, BlockPos pos) { return this.isRedstoneConductor.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1055 */     public boolean isSignalSource() { return getBlock().isSignalSource(asState()); }
/*      */ 
/*      */ 
/*      */     
/* 1059 */     public int getSignal(BlockGetter level, BlockPos pos, Direction direction) { return getBlock().getSignal(asState(), level, pos, direction); }
/*      */ 
/*      */ 
/*      */     
/* 1063 */     public boolean hasAnalogOutputSignal() { return getBlock().hasAnalogOutputSignal(asState()); }
/*      */ 
/*      */ 
/*      */     
/* 1067 */     public int getAnalogOutputSignal(Level level, BlockPos pos, Direction direction) { return getBlock().getAnalogOutputSignal(asState(), level, pos, direction); }
/*      */ 
/*      */ 
/*      */     
/* 1071 */     public float getDestroySpeed(BlockGetter level, BlockPos pos) { return this.destroySpeed; }
/*      */ 
/*      */ 
/*      */     
/* 1075 */     public float getDestroyProgress(Player player, BlockGetter level, BlockPos pos) { return getBlock().getDestroyProgress(asState(), player, level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1079 */     public int getDirectSignal(BlockGetter level, BlockPos pos, Direction direction) { return getBlock().getDirectSignal(asState(), level, pos, direction); }
/*      */ 
/*      */ 
/*      */     
/* 1083 */     public PushReaction getPistonPushReaction() { return this.pushReaction; }
/*      */ 
/*      */ 
/*      */     
/* 1087 */     public boolean isSolidRender() { return this.solidRender; }
/*      */ 
/*      */ 
/*      */     
/* 1091 */     public boolean canOcclude() { return this.canOcclude; }
/*      */ 
/*      */ 
/*      */     
/* 1095 */     public boolean skipRendering(BlockState neighborState, Direction direction) { return getBlock().skipRendering(asState(), neighborState, direction); }
/*      */ 
/*      */ 
/*      */     
/* 1099 */     public VoxelShape getShape(BlockGetter level, BlockPos pos) { return getShape(level, pos, CollisionContext.empty()); }
/*      */ 
/*      */ 
/*      */     
/* 1103 */     public VoxelShape getShape(BlockGetter level, BlockPos pos, CollisionContext context) { return getBlock().getShape(asState(), level, pos, context); }
/*      */ 
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/* 1107 */       if (this.cache != null) {
/* 1108 */         return this.cache.collisionShape;
/*      */       }
/* 1110 */       return getCollisionShape(level, pos, CollisionContext.empty());
/*      */     }
/*      */ 
/*      */     
/* 1114 */     public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context) { return getBlock().getCollisionShape(asState(), level, pos, context); }
/*      */ 
/*      */ 
/*      */     
/* 1118 */     public VoxelShape getEntityInsideCollisionShape(BlockGetter level, BlockPos pos, Entity entity) { return getBlock().getEntityInsideCollisionShape(asState(), level, pos, entity); }
/*      */ 
/*      */ 
/*      */     
/* 1122 */     public VoxelShape getBlockSupportShape(BlockGetter level, BlockPos pos) { return getBlock().getBlockSupportShape(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1126 */     public VoxelShape getVisualShape(BlockGetter level, BlockPos pos, CollisionContext context) { return getBlock().getVisualShape(asState(), level, pos, context); }
/*      */ 
/*      */ 
/*      */     
/* 1130 */     public VoxelShape getInteractionShape(BlockGetter level, BlockPos pos) { return getBlock().getInteractionShape(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1134 */     public final boolean entityCanStandOn(BlockGetter level, BlockPos pos, Entity entity) { return entityCanStandOnFace(level, pos, entity, Direction.UP); }
/*      */ 
/*      */ 
/*      */     
/* 1138 */     public final boolean entityCanStandOnFace(BlockGetter level, BlockPos pos, Entity entity, Direction faceDirection) { return Block.isFaceFull(getCollisionShape(level, pos, CollisionContext.of(entity)), faceDirection); }
/*      */ 
/*      */     
/*      */     public Vec3 getOffset(BlockPos pos) {
/* 1142 */       BlockBehaviour.OffsetFunction function = this.offsetFunction;
/* 1143 */       if (function != null) {
/* 1144 */         return function.evaluate(asState(), pos);
/*      */       }
/* 1146 */       return Vec3.ZERO;
/*      */     }
/*      */ 
/*      */     
/* 1150 */     public boolean hasOffsetFunction() { return (this.offsetFunction != null); }
/*      */ 
/*      */ 
/*      */     
/* 1154 */     public boolean triggerEvent(Level level, BlockPos pos, int b0, int b1) { return getBlock().triggerEvent(asState(), level, pos, b0, b1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1162 */     public void handleNeighborChanged(Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) { getBlock().neighborChanged(asState(), level, pos, block, orientation, movedByPiston); }
/*      */ 
/*      */ 
/*      */     
/* 1166 */     public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags) { updateNeighbourShapes(level, pos, updateFlags, 512); }
/*      */ 
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags, int updateLimit) {
/* 1170 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 1171 */       for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/* 1172 */         blockPos.setWithOffset(pos, direction);
/* 1173 */         level.neighborShapeChanged(direction.getOpposite(), blockPos, pos, asState(), updateFlags, updateLimit);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1178 */     public final void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags) { updateIndirectNeighbourShapes(level, pos, updateFlags, 512); }
/*      */ 
/*      */ 
/*      */     
/* 1182 */     public void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags, int updateLimit) { getBlock().updateIndirectNeighbourShapes(asState(), level, pos, updateFlags, updateLimit); }
/*      */ 
/*      */ 
/*      */     
/* 1186 */     public void onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { getBlock().onPlace(asState(), level, pos, oldState, movedByPiston); }
/*      */ 
/*      */ 
/*      */     
/* 1190 */     public void affectNeighborsAfterRemoval(ServerLevel level, BlockPos pos, boolean movedByPiston) { getBlock().affectNeighborsAfterRemoval(asState(), level, pos, movedByPiston); }
/*      */ 
/*      */ 
/*      */     
/* 1194 */     public void onExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) { getBlock().onExplosionHit(asState(), level, pos, explosion, onHit); }
/*      */ 
/*      */ 
/*      */     
/* 1198 */     public void tick(ServerLevel level, BlockPos pos, RandomSource random) { getBlock().tick(asState(), level, pos, random); }
/*      */ 
/*      */ 
/*      */     
/* 1202 */     public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) { getBlock().randomTick(asState(), level, pos, random); }
/*      */ 
/*      */ 
/*      */     
/* 1206 */     public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) { getBlock().entityInside(asState(), level, pos, entity, effectApplier, isPrecise); }
/*      */ 
/*      */ 
/*      */     
/* 1210 */     public void spawnAfterBreak(ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) { getBlock().spawnAfterBreak(asState(), level, pos, tool, dropExperience); }
/*      */ 
/*      */ 
/*      */     
/* 1214 */     public List<ItemStack> getDrops(LootParams.Builder params) { return getBlock().getDrops(asState(), params); }
/*      */ 
/*      */ 
/*      */     
/* 1218 */     public InteractionResult useItemOn(ItemStack itemStack, Level level, Player player, InteractionHand hand, BlockHitResult hitResult) { return getBlock().useItemOn(itemStack, asState(), level, hitResult.getBlockPos(), player, hand, hitResult); }
/*      */ 
/*      */ 
/*      */     
/* 1222 */     public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hitResult) { return getBlock().useWithoutItem(asState(), level, hitResult.getBlockPos(), player, hitResult); }
/*      */ 
/*      */ 
/*      */     
/* 1226 */     public void attack(Level level, BlockPos pos, Player player) { getBlock().attack(asState(), level, pos, player); }
/*      */ 
/*      */ 
/*      */     
/* 1230 */     public boolean isSuffocating(BlockGetter level, BlockPos pos) { return this.isSuffocating.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1234 */     public boolean isViewBlocking(BlockGetter level, BlockPos pos) { return this.isViewBlocking.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1238 */     public BlockState updateShape(LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) { return getBlock().updateShape(asState(), level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random); }
/*      */ 
/*      */ 
/*      */     
/* 1242 */     public boolean isPathfindable(PathComputationType type) { return getBlock().isPathfindable(asState(), type); }
/*      */ 
/*      */ 
/*      */     
/* 1246 */     public boolean canBeReplaced(BlockPlaceContext context) { return getBlock().canBeReplaced(asState(), context); }
/*      */ 
/*      */ 
/*      */     
/* 1250 */     public boolean canBeReplaced(Fluid fluid) { return getBlock().canBeReplaced(asState(), fluid); }
/*      */ 
/*      */ 
/*      */     
/* 1254 */     public boolean canBeReplaced() { return this.replaceable; }
/*      */ 
/*      */ 
/*      */     
/* 1258 */     public boolean canSurvive(LevelReader level, BlockPos pos) { return getBlock().canSurvive(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1262 */     public boolean hasPostProcess(BlockGetter level, BlockPos pos) { return this.hasPostProcess.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1266 */     public MenuProvider getMenuProvider(Level level, BlockPos pos) { return getBlock().getMenuProvider(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */     
/* 1270 */     public boolean is(TagKey<Block> tag) { return getBlock().builtInRegistryHolder().is(tag); }
/*      */ 
/*      */ 
/*      */     
/* 1274 */     public boolean is(TagKey<Block> tag, Predicate<BlockStateBase> predicate) { return (is(tag) && predicate.test(this)); }
/*      */ 
/*      */ 
/*      */     
/* 1278 */     public boolean is(HolderSet<Block> set) { return set.contains(getBlock().builtInRegistryHolder()); }
/*      */ 
/*      */ 
/*      */     
/* 1282 */     public boolean is(Holder<Block> holder) { return is((Block)holder.value()); }
/*      */ 
/*      */ 
/*      */     
/* 1286 */     public Stream<TagKey<Block>> getTags() { return getBlock().builtInRegistryHolder().tags(); }
/*      */ 
/*      */ 
/*      */     
/* 1290 */     public boolean hasBlockEntity() { return getBlock() instanceof EntityBlock; }
/*      */ 
/*      */ 
/*      */     
/* 1294 */     public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) { return getBlock().shouldChangedStateKeepBlockEntity(oldState); }
/*      */ 
/*      */     
/*      */     public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> type) {
/* 1298 */       if (getBlock() instanceof EntityBlock) {
/* 1299 */         return ((EntityBlock)getBlock()).getTicker(level, asState(), type);
/*      */       }
/* 1301 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1305 */     public boolean is(Block block) { return (getBlock() == block); }
/*      */ 
/*      */ 
/*      */     
/* 1309 */     public boolean is(ResourceKey<Block> block) { return getBlock().builtInRegistryHolder().is(block); }
/*      */ 
/*      */ 
/*      */     
/* 1313 */     public FluidState getFluidState() { return this.fluidState; }
/*      */ 
/*      */ 
/*      */     
/* 1317 */     public boolean isRandomlyTicking() { return this.isRandomlyTicking; }
/*      */ 
/*      */ 
/*      */     
/* 1321 */     public long getSeed(BlockPos pos) { return getBlock().getSeed(asState(), pos); }
/*      */ 
/*      */ 
/*      */     
/* 1325 */     public SoundType getSoundType() { return getBlock().getSoundType(asState()); }
/*      */ 
/*      */ 
/*      */     
/* 1329 */     public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile entity) { getBlock().onProjectileHit(level, state, blockHit, entity); }
/*      */ 
/*      */ 
/*      */     
/* 1333 */     public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction) { return isFaceSturdy(level, pos, direction, SupportType.FULL); }
/*      */ 
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction, SupportType supportType) {
/* 1337 */       if (this.cache != null) {
/* 1338 */         return this.cache.isFaceSturdy(direction, supportType);
/*      */       }
/* 1340 */       return supportType.isSupporting(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public boolean isCollisionShapeFullBlock(BlockGetter level, BlockPos pos) {
/* 1344 */       if (this.cache != null) {
/* 1345 */         return this.cache.isCollisionShapeFullBlock;
/*      */       }
/* 1347 */       return getBlock().isCollisionShapeFullBlock(asState(), level, pos);
/*      */     }
/*      */ 
/*      */     
/* 1351 */     public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, boolean includeData) { return getBlock().getCloneItemStack(level, pos, asState(), includeData); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1357 */     public boolean requiresCorrectToolForDrops() { return this.requiresCorrectToolForDrops; }
/*      */ 
/*      */ 
/*      */     
/* 1361 */     public boolean shouldSpawnTerrainParticles() { return this.spawnTerrainParticles; }
/*      */ 
/*      */ 
/*      */     
/* 1365 */     public NoteBlockInstrument instrument() { return this.instrument; }
/*      */     
/*      */     protected abstract BlockState asState();
/*      */     
/* 1369 */     private static final class Cache { private static final Direction[] DIRECTIONS = Direction.values();
/* 1370 */       private static final int SUPPORT_TYPE_COUNT = SupportType.values().length;
/*      */       protected final VoxelShape collisionShape;
/*      */       protected final boolean largeCollisionShape;
/*      */       private final boolean[] faceSturdy;
/*      */       protected final boolean isCollisionShapeFullBlock;
/*      */       
/*      */       private Cache(BlockState state) {
/* 1377 */         Block block = state.getBlock();
/*      */         
/* 1379 */         this.collisionShape = block.getCollisionShape(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1380 */         if (!this.collisionShape.isEmpty() && state.hasOffsetFunction()) {
/* 1381 */           throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) }));
/*      */         }
/* 1383 */         this.largeCollisionShape = Arrays.stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/* 1384 */         this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1385 */         for (Direction direction : DIRECTIONS) {
/* 1386 */           for (SupportType type : SupportType.values()) {
/* 1387 */             this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction);
/*      */           }
/*      */         } 
/* 1390 */         this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */       }
/*      */ 
/*      */       
/* 1394 */       public boolean isFaceSturdy(Direction direction, SupportType supportType) { return this.faceSturdy[getFaceSupportIndex(direction, supportType)]; }
/*      */ 
/*      */ 
/*      */       
/* 1398 */       private static int getFaceSupportIndex(Direction direction, SupportType supportType) { return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal(); } } } private static final class Cache { private static final Direction[] DIRECTIONS = Direction.values(); private static final int SUPPORT_TYPE_COUNT = SupportType.values().length; protected final VoxelShape collisionShape; protected final boolean largeCollisionShape; private static int getFaceSupportIndex(Direction direction, SupportType supportType) { return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal(); }
/*      */     
/*      */     private final boolean[] faceSturdy;
/*      */     protected final boolean isCollisionShapeFullBlock;
/*      */     
/*      */     private Cache(BlockState state) {
/*      */       Block block = state.getBlock();
/*      */       this.collisionShape = block.getCollisionShape(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/*      */       if (!this.collisionShape.isEmpty() && state.hasOffsetFunction())
/*      */         throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) })); 
/*      */       this.largeCollisionShape = Arrays.stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/*      */       this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/*      */       for (Direction direction : DIRECTIONS) {
/*      */         for (SupportType type : SupportType.values())
/*      */           this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction); 
/*      */       } 
/*      */       this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(Direction direction, SupportType supportType) { return this.faceSturdy[getFaceSupportIndex(direction, supportType)]; } }
/*      */ 
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface StateArgumentPredicate<A> {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, A param1A);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface OffsetFunction {
/*      */     Vec3 evaluate(BlockState param1BlockState, BlockPos param1BlockPos);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface StatePredicate {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos);
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\BlockBehaviour.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */