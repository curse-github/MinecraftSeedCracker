/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Block
/*     */   extends BlockBehaviour
/*     */   implements ItemLike
/*     */ {
/*  89 */   public static final MapCodec<Block> CODEC = simpleCodec(Block::new);
/*     */   
/*  91 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  93 */   private final Holder.Reference<Block> builtInRegistryHolder = BuiltInRegistries.BLOCK.createIntrusiveHolder(this);
/*     */   
/*  95 */   public static final IdMapper<BlockState> BLOCK_STATE_REGISTRY = new IdMapper();
/*     */   
/*  97 */   private static final LoadingCache<VoxelShape, Boolean> SHAPE_FULL_BLOCK_CACHE = CacheBuilder.newBuilder()
/*  98 */     .maximumSize(512L)
/*  99 */     .weakKeys()
/* 100 */     .build(new CacheLoader<VoxelShape, Boolean>()
/*     */       {
/*     */         public Boolean load(VoxelShape shape) {
/* 103 */           return Boolean.valueOf(!Shapes.joinIsNotEmpty(Shapes.block(), shape, BooleanOp.NOT_SAME));
/*     */         }
/*     */       });
/*     */ 
/*     */   
/*     */   public static final int UPDATE_NEIGHBORS = 1;
/*     */   
/*     */   public static final int UPDATE_CLIENTS = 2;
/*     */   
/*     */   public static final int UPDATE_INVISIBLE = 4;
/*     */   
/*     */   public static final int UPDATE_IMMEDIATE = 8;
/*     */   
/*     */   public static final int UPDATE_KNOWN_SHAPE = 16;
/*     */   public static final int UPDATE_SUPPRESS_DROPS = 32;
/*     */   public static final int UPDATE_MOVE_BY_PISTON = 64;
/*     */   public static final int UPDATE_SKIP_SHAPE_UPDATE_ON_WIRE = 128;
/*     */   public static final int UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS = 256;
/*     */   public static final int UPDATE_SKIP_ON_PLACE = 512;
/*     */   @UpdateFlags
/*     */   public static final int UPDATE_NONE = 260;
/*     */   @UpdateFlags
/*     */   public static final int UPDATE_ALL = 3;
/*     */   @UpdateFlags
/*     */   public static final int UPDATE_ALL_IMMEDIATE = 11;
/*     */   @UpdateFlags
/*     */   public static final int UPDATE_SKIP_ALL_SIDEEFFECTS = 816;
/*     */   public static final float INDESTRUCTIBLE = -1.0F;
/*     */   public static final float INSTANT = 0.0F;
/*     */   public static final int UPDATE_LIMIT = 512;
/*     */   protected final StateDefinition<Block, BlockState> stateDefinition;
/*     */   private BlockState defaultBlockState;
/*     */   private Item item;
/*     */   private static final int CACHE_SIZE = 256;
/*     */   
/* 138 */   protected MapCodec<? extends Block> codec() { return CODEC; }
/*     */ 
/*     */   
/*     */   public static int getId(BlockState blockState) {
/* 142 */     if (blockState == null) {
/* 143 */       return 0;
/*     */     }
/* 145 */     int id = BLOCK_STATE_REGISTRY.getId(blockState);
/* 146 */     return (id == -1) ? 0 : id;
/*     */   }
/*     */   
/*     */   public static BlockState stateById(int idWithData) {
/* 150 */     BlockState state = (BlockState)BLOCK_STATE_REGISTRY.byId(idWithData);
/* 151 */     return (state == null) ? Blocks.AIR.defaultBlockState() : state;
/*     */   }
/*     */   
/*     */   public static Block byItem(Item item) {
/* 155 */     if (item instanceof BlockItem) {
/* 156 */       return ((BlockItem)item).getBlock();
/*     */     }
/*     */     
/* 159 */     return Blocks.AIR;
/*     */   }
/*     */   
/*     */   public static BlockState pushEntitiesUp(BlockState state, BlockState newState, LevelAccessor level, BlockPos pos) {
/* 163 */     VoxelShape offsetShape = Shapes.joinUnoptimized(state.getCollisionShape(level, pos), newState.getCollisionShape(level, pos), BooleanOp.ONLY_SECOND).move(pos);
/* 164 */     if (offsetShape.isEmpty()) {
/* 165 */       return newState;
/*     */     }
/* 167 */     List<Entity> collidingEntities = level.getEntities(null, offsetShape.bounds());
/* 168 */     for (Entity collidingEntity : collidingEntities) {
/*     */       
/* 170 */       double offset = Shapes.collide(Direction.Axis.Y, collidingEntity.getBoundingBox().move(0.0D, 1.0D, 0.0D), List.of(offsetShape), -1.0D);
/* 171 */       collidingEntity.teleportRelative(0.0D, 1.0D + offset, 0.0D);
/*     */     } 
/* 173 */     return newState;
/*     */   }
/*     */ 
/*     */   
/* 177 */   public static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) { return Shapes.box(minX / 16.0D, minY / 16.0D, minZ / 16.0D, maxX / 16.0D, maxY / 16.0D, maxZ / 16.0D); }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public static VoxelShape[] boxes(int endInclusive, IntFunction<VoxelShape> voxelShapeFactory) { return (VoxelShape[])IntStream.rangeClosed(0, endInclusive).mapToObj(voxelShapeFactory).toArray(x$0 -> new VoxelShape[x$0]); }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public static VoxelShape cube(double size) { return cube(size, size, size); }
/*     */ 
/*     */   
/*     */   public static VoxelShape cube(double sizeX, double sizeY, double sizeZ) {
/* 189 */     double halfY = sizeY / 2.0D;
/* 190 */     return column(sizeX, sizeZ, 8.0D - halfY, 8.0D + halfY);
/*     */   }
/*     */ 
/*     */   
/* 194 */   public static VoxelShape column(double sizeXZ, double minY, double maxY) { return column(sizeXZ, sizeXZ, minY, maxY); }
/*     */ 
/*     */   
/*     */   public static VoxelShape column(double sizeX, double sizeZ, double minY, double maxY) {
/* 198 */     double halfX = sizeX / 2.0D;
/* 199 */     double halfZ = sizeZ / 2.0D;
/* 200 */     return box(8.0D - halfX, minY, 8.0D - halfZ, 8.0D + halfX, maxY, 8.0D + halfZ);
/*     */   }
/*     */ 
/*     */   
/* 204 */   public static VoxelShape boxZ(double sizeXY, double minZ, double maxZ) { return boxZ(sizeXY, sizeXY, minZ, maxZ); }
/*     */ 
/*     */   
/*     */   public static VoxelShape boxZ(double sizeX, double sizeY, double minZ, double maxZ) {
/* 208 */     double halfY = sizeY / 2.0D;
/* 209 */     return boxZ(sizeX, 8.0D - halfY, 8.0D + halfY, minZ, maxZ);
/*     */   }
/*     */   
/*     */   public static VoxelShape boxZ(double sizeX, double minY, double maxY, double minZ, double maxZ) {
/* 213 */     double halfX = sizeX / 2.0D;
/* 214 */     return box(8.0D - halfX, minY, minZ, 8.0D + halfX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public static BlockState updateFromNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos) {
/* 218 */     BlockState newState = state;
/*     */     
/* 220 */     BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos();
/* 221 */     for (Direction direction : UPDATE_SHAPE_ORDER) {
/* 222 */       neighbourPos.setWithOffset(pos, direction);
/* 223 */       newState = newState.updateShape(level, level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/*     */     } 
/*     */     
/* 226 */     return newState;
/*     */   }
/*     */ 
/*     */   
/* 230 */   public static void updateOrDestroy(BlockState blockState, BlockState newState, LevelAccessor level, BlockPos blockPos, @UpdateFlags int updateFlags) { updateOrDestroy(blockState, newState, level, blockPos, updateFlags, 512); }
/*     */ 
/*     */   
/*     */   public static void updateOrDestroy(BlockState blockState, BlockState newState, LevelAccessor level, BlockPos blockPos, @UpdateFlags int updateFlags, int updateLimit) {
/* 234 */     if (newState != blockState) {
/* 235 */       if (newState.isAir()) {
/* 236 */         if (!level.isClientSide()) {
/* 237 */           level.destroyBlock(blockPos, ((updateFlags & 0x20) == 0), null, updateLimit);
/*     */         }
/*     */       } else {
/* 240 */         level.setBlock(blockPos, newState, updateFlags & 0xFFFFFFDF, updateLimit);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Block(BlockBehaviour.Properties properties) {
/* 251 */     super(properties);
/* 252 */     StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<Block, BlockState>(this);
/* 253 */     createBlockStateDefinition(builder);
/*     */     
/* 255 */     this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
/* 256 */     registerDefaultState((BlockState)this.stateDefinition.any());
/*     */     
/* 258 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 259 */       String className = getClass().getSimpleName();
/* 260 */       if (!className.endsWith("Block")) {
/* 261 */         LOGGER.error("Block classes should end with Block and {} doesn't.", className);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isExceptionForConnection(BlockState state) {
/* 267 */     return (state.getBlock() instanceof LeavesBlock || state
/* 268 */       .is(Blocks.BARRIER) || state
/* 269 */       .is(Blocks.CARVED_PUMPKIN) || state
/* 270 */       .is(Blocks.JACK_O_LANTERN) || state
/* 271 */       .is(Blocks.MELON) || state
/* 272 */       .is(Blocks.PUMPKIN) || state
/* 273 */       .is(BlockTags.SHULKER_BOXES));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 278 */   protected static boolean dropFromBlockInteractLootTable(ServerLevel level, ResourceKey<LootTable> key, BlockState interactedBlockState, BlockEntity interactedBlockEntity, ItemStack tool, Entity interactingEntity, BiConsumer<ServerLevel, ItemStack> consumer) { return dropFromLootTable(level, key, params -> 
/*     */ 
/*     */         
/* 281 */         params
/* 282 */         .withParameter(LootContextParams.BLOCK_STATE, interactedBlockState)
/* 283 */         .withOptionalParameter(LootContextParams.BLOCK_ENTITY, interactedBlockEntity)
/* 284 */         .withOptionalParameter(LootContextParams.INTERACTING_ENTITY, interactingEntity)
/* 285 */         .withOptionalParameter(LootContextParams.TOOL, tool)
/* 286 */         .create(LootContextParamSets.BLOCK_INTERACT), consumer); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean dropFromLootTable(ServerLevel level, ResourceKey<LootTable> key, Function<LootParams.Builder, LootParams> paramsBuilder, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 292 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(key);
/* 293 */     LootParams params = (LootParams)paramsBuilder.apply(new LootParams.Builder(level));
/* 294 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(params);
/* 295 */     if (!objectArrayList.isEmpty()) {
/* 296 */       objectArrayList.forEach(stack -> consumer.accept(level, stack));
/* 297 */       return true;
/*     */     } 
/* 299 */     return false;
/*     */   }
/*     */   private static final class ShapePairKey extends Record { private final VoxelShape first;
/* 302 */     private ShapePairKey(VoxelShape first, VoxelShape second) { this.first = first; this.second = second; } private final VoxelShape second; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/Block$ShapePairKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #302	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 302 */       //   0	7	0	this	Lnet/minecraft/world/level/block/Block$ShapePairKey; } public VoxelShape first() { return this.first; } public VoxelShape second() { return this.second; }
/*     */ 
/*     */ 
/*     */     
/* 306 */     public boolean equals(Object o) { if (o instanceof ShapePairKey) { ShapePairKey that = (ShapePairKey)o; if (this.first == that.first && this.second == that.second); }  return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     public int hashCode() { return System.identityHashCode(this.first) * 31 + System.identityHashCode(this.second); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 316 */   private static final ThreadLocal<Object2ByteLinkedOpenHashMap<ShapePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
/* 317 */         map = new Object2ByteLinkedOpenHashMap<ShapePairKey>(256, 0.25F)
/*     */           {
/*     */             protected void rehash(int newN) {}
/*     */           };
/*     */         
/* 322 */         map.defaultReturnValue(127);
/* 323 */         return map;
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean shouldRenderFace(BlockState state, BlockState neighborState, Direction direction) {
/* 330 */     VoxelShape occluder = neighborState.getFaceOcclusionShape(direction.getOpposite());
/* 331 */     if (occluder == Shapes.block()) {
/* 332 */       return false;
/*     */     }
/*     */     
/* 335 */     if (state.skipRendering(neighborState, direction)) {
/* 336 */       return false;
/*     */     }
/*     */     
/* 339 */     if (occluder == Shapes.empty()) {
/* 340 */       return true;
/*     */     }
/*     */     
/* 343 */     VoxelShape shape = state.getFaceOcclusionShape(direction);
/*     */     
/* 345 */     if (shape == Shapes.empty()) {
/* 346 */       return true;
/*     */     }
/*     */     
/* 349 */     ShapePairKey key = new ShapePairKey(shape, occluder);
/* 350 */     Object2ByteLinkedOpenHashMap<ShapePairKey> cache = (Object2ByteLinkedOpenHashMap)OCCLUSION_CACHE.get();
/* 351 */     byte cached = cache.getAndMoveToFirst(key);
/* 352 */     if (cached != Byte.MAX_VALUE) {
/* 353 */       return (cached != 0);
/*     */     }
/* 355 */     boolean result = Shapes.joinIsNotEmpty(shape, occluder, BooleanOp.ONLY_FIRST);
/* 356 */     if (cache.size() == 256) {
/* 357 */       cache.removeLastByte();
/*     */     }
/* 359 */     cache.putAndMoveToFirst(key, (byte)(result ? 1 : 0));
/* 360 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 364 */   public static boolean canSupportRigidBlock(BlockGetter level, BlockPos below) { return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP, SupportType.RIGID); }
/*     */ 
/*     */   
/*     */   public static boolean canSupportCenter(LevelReader level, BlockPos belowPos, Direction direction) {
/* 368 */     BlockState state = level.getBlockState(belowPos);
/*     */     
/* 370 */     if (direction == Direction.DOWN && state.is(BlockTags.UNSTABLE_BOTTOM_CENTER)) {
/* 371 */       return false;
/*     */     }
/*     */     
/* 374 */     return state.isFaceSturdy(level, belowPos, direction, SupportType.CENTER);
/*     */   }
/*     */   
/*     */   public static boolean isFaceFull(VoxelShape shape, Direction direction) {
/* 378 */     VoxelShape faceShape = shape.getFaceShape(direction);
/* 379 */     return isShapeFullBlock(faceShape);
/*     */   }
/*     */ 
/*     */   
/* 383 */   public static boolean isShapeFullBlock(VoxelShape shape) { return ((Boolean)SHAPE_FULL_BLOCK_CACHE.getUnchecked(shape)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ItemStack> getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity) {
/* 397 */     LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
/* 398 */     return state.getDrops(params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ItemStack> getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool) {
/* 406 */     LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.THIS_ENTITY, breaker).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
/* 407 */     return state.getDrops(params);
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState state, Level level, BlockPos pos) {
/* 411 */     if (level instanceof ServerLevel) {
/* 412 */       getDrops(state, (ServerLevel)level, pos, null).forEach(stack -> popResource(level, pos, stack));
/* 413 */       state.spawnAfterBreak((ServerLevel)level, pos, ItemStack.EMPTY, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState state, LevelAccessor level, BlockPos pos, BlockEntity blockEntity) {
/* 418 */     if (level instanceof ServerLevel) {
/* 419 */       getDrops(state, (ServerLevel)level, pos, blockEntity).forEach(stack -> popResource((ServerLevel)level, pos, stack));
/* 420 */       state.spawnAfterBreak((ServerLevel)level, pos, ItemStack.EMPTY, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool) {
/* 425 */     if (level instanceof ServerLevel) {
/* 426 */       getDrops(state, (ServerLevel)level, pos, blockEntity, breaker, tool).forEach(stack -> popResource(level, pos, stack));
/* 427 */       state.spawnAfterBreak((ServerLevel)level, pos, tool, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void popResource(Level level, BlockPos pos, ItemStack itemStack) {
/* 432 */     double halfHeight = EntityType.ITEM.getHeight() / 2.0D;
/*     */     
/* 434 */     double x = pos.getX() + 0.5D + Mth.nextDouble(level.random, -0.25D, 0.25D);
/* 435 */     double y = pos.getY() + 0.5D + Mth.nextDouble(level.random, -0.25D, 0.25D) - halfHeight;
/* 436 */     double z = pos.getZ() + 0.5D + Mth.nextDouble(level.random, -0.25D, 0.25D);
/*     */     
/* 438 */     popResource(level, () -> new ItemEntity(level, x, y, z, itemStack), itemStack);
/*     */   }
/*     */   
/*     */   public static void popResourceFromFace(Level level, BlockPos pos, Direction face, ItemStack itemStack) {
/* 442 */     int stepX = face.getStepX();
/* 443 */     int stepY = face.getStepY();
/* 444 */     int stepZ = face.getStepZ();
/*     */     
/* 446 */     double halfWidth = EntityType.ITEM.getWidth() / 2.0D;
/* 447 */     double halfHeight = EntityType.ITEM.getHeight() / 2.0D;
/*     */     
/* 449 */     double x = pos.getX() + 0.5D + ((stepX == 0) ? Mth.nextDouble(level.random, -0.25D, 0.25D) : (stepX * (0.5D + halfWidth)));
/* 450 */     double y = pos.getY() + 0.5D + ((stepY == 0) ? Mth.nextDouble(level.random, -0.25D, 0.25D) : (stepY * (0.5D + halfHeight))) - halfHeight;
/* 451 */     double z = pos.getZ() + 0.5D + ((stepZ == 0) ? Mth.nextDouble(level.random, -0.25D, 0.25D) : (stepZ * (0.5D + halfWidth)));
/*     */     
/* 453 */     double deltaX = (stepX == 0) ? Mth.nextDouble(level.random, -0.1D, 0.1D) : (stepX * 0.1D);
/* 454 */     double deltaY = (stepY == 0) ? Mth.nextDouble(level.random, 0.0D, 0.1D) : (stepY * 0.1D + 0.1D);
/* 455 */     double deltaZ = (stepZ == 0) ? Mth.nextDouble(level.random, -0.1D, 0.1D) : (stepZ * 0.1D);
/*     */     
/* 457 */     popResource(level, () -> new ItemEntity(level, x, y, z, itemStack, deltaX, deltaY, deltaZ), itemStack);
/*     */   }
/*     */   
/*     */   private static void popResource(Level level, Supplier<ItemEntity> entityFactory, ItemStack itemStack) {
/* 461 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!itemStack.isEmpty() && ((Boolean)serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue()) {
/*     */ 
/*     */ 
/*     */         
/* 465 */         ItemEntity entity = (ItemEntity)entityFactory.get();
/* 466 */         entity.setDefaultPickUpDelay();
/* 467 */         level.addFreshEntity(entity);
/*     */         return;
/*     */       }  }
/*     */      } protected void popExperience(ServerLevel level, BlockPos pos, int amount) {
/* 471 */     if (((Boolean)level.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue()) {
/* 472 */       ExperienceOrb.award(level, Vec3.atCenterOf(pos), amount);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 477 */   public float getExplosionResistance() { return this.explosionResistance; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 497 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return defaultBlockState(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) {
/* 502 */     player.awardStat(Stats.BLOCK_MINED.get(this));
/* 503 */     player.causeFoodExhaustion(0.005F);
/* 504 */     dropResources(state, level, pos, blockEntity, player, destroyedWith);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {}
/*     */ 
/*     */   
/* 512 */   public boolean isPossibleToRespawnInThis(BlockState state) { return (!state.isSolid() && !state.liquid()); }
/*     */ 
/*     */ 
/*     */   
/* 516 */   public MutableComponent getName() { return Component.translatable(getDescriptionId()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 523 */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) { entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall()); }
/*     */ 
/*     */ 
/*     */   
/* 527 */   public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) { entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D)); }
/*     */ 
/*     */ 
/*     */   
/* 531 */   public float getFriction() { return this.friction; }
/*     */ 
/*     */ 
/*     */   
/* 535 */   public float getSpeedFactor() { return this.speedFactor; }
/*     */ 
/*     */ 
/*     */   
/* 539 */   public float getJumpFactor() { return this.jumpFactor; }
/*     */ 
/*     */ 
/*     */   
/* 543 */   protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) { level.levelEvent(player, 2001, pos, getId(state)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 548 */     spawnDestroyParticles(level, player, pos, state);
/*     */     
/* 550 */     if (state.is(BlockTags.GUARDED_BY_PIGLINS) && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 551 */       PiglinAi.angerNearbyPiglins(serverLevel, player, false); }
/*     */     
/* 553 */     level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, state));
/* 554 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {}
/*     */ 
/*     */   
/* 561 */   public boolean dropFromExplosion(Explosion explosion) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {}
/*     */ 
/*     */   
/* 568 */   public StateDefinition<Block, BlockState> getStateDefinition() { return this.stateDefinition; }
/*     */ 
/*     */ 
/*     */   
/* 572 */   protected final void registerDefaultState(BlockState state) { this.defaultBlockState = state; }
/*     */ 
/*     */ 
/*     */   
/* 576 */   public final BlockState defaultBlockState() { return this.defaultBlockState; }
/*     */ 
/*     */   
/*     */   public final BlockState withPropertiesOf(BlockState source) {
/* 580 */     BlockState result = defaultBlockState();
/* 581 */     for (Property<?> property : source.getBlock().getStateDefinition().getProperties()) {
/* 582 */       if (result.hasProperty(property)) {
/* 583 */         result = copyProperty(source, result, property);
/*     */       }
/*     */     } 
/* 586 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 591 */   private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) { return (BlockState)to.setValue(property, from.getValue(property)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item asItem() {
/* 596 */     if (this.item == null) {
/* 597 */       this.item = Item.byBlock(this);
/*     */     }
/* 599 */     return this.item;
/*     */   }
/*     */ 
/*     */   
/* 603 */   public boolean hasDynamicShape() { return this.dynamicShape; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 608 */   public String toString() { return "Block{" + BuiltInRegistries.BLOCK.wrapAsHolder(this).getRegisteredName() + "}"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 613 */   protected Block asBlock() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 620 */   protected Function<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> shapeCalculator) { Objects.requireNonNull((ImmutableMap)this.stateDefinition.getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), shapeCalculator))); return (ImmutableMap)this.stateDefinition.getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), shapeCalculator))::get; }
/*     */ 
/*     */   
/*     */   protected Function<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> shapeCalculator, Property... ignoredProperties) {
/* 624 */     Map<? extends Property<?>, Object> defaults = (Map)Arrays.stream(ignoredProperties).collect(Collectors.toMap(k -> k, k -> k.getPossibleValues().getFirst()));
/*     */ 
/*     */ 
/*     */     
/* 628 */     ImmutableMap<BlockState, VoxelShape> map = (ImmutableMap)this.stateDefinition.getPossibleStates().stream().filter(state -> defaults.entrySet().stream().allMatch(())).collect(ImmutableMap.toImmutableMap(Function.identity(), shapeCalculator));
/*     */     
/* 630 */     return blockState -> {
/* 631 */         for (Map.Entry<? extends Property<?>, Object> entry : defaults.entrySet()) {
/* 632 */           blockState = (BlockState)setValueHelper(blockState, (Property)entry.getKey(), entry.getValue());
/*     */         }
/* 634 */         return (VoxelShape)map.get(blockState);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 640 */   private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S state, Property<T> property, Object value) { return (S)(StateHolder)state.setValue(property, (Comparable)value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 648 */   public Holder.Reference<Block> builtInRegistryHolder() { return this.builtInRegistryHolder; }
/*     */ 
/*     */   
/*     */   protected void tryDropExperience(ServerLevel level, BlockPos pos, ItemStack tool, IntProvider xpRange) {
/* 652 */     int experience = EnchantmentHelper.processBlockExperience(level, tool, xpRange.sample(level.getRandom()));
/* 653 */     if (experience > 0)
/* 654 */       popExperience(level, pos, experience); 
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.CLASS)
/*     */   @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
/*     */   public static @interface UpdateFlags {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\Block.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */