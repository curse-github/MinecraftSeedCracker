/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ComposterBlock extends Block implements WorldlyContainerHolder {
/*  44 */   public static final MapCodec<ComposterBlock> CODEC = simpleCodec(ComposterBlock::new); public static final int READY = 8;
/*     */   public static final int MIN_LEVEL = 0;
/*     */   public static final int MAX_LEVEL = 7;
/*     */   
/*  48 */   public MapCodec<ComposterBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;
/*     */   
/*  56 */   public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap(); private static final int HOLE_WIDTH = 12;
/*     */   
/*     */   public static void bootStrap() {
/*  59 */     COMPOSTABLES.defaultReturnValue(-1.0F);
/*     */     
/*  61 */     low = 0.3F;
/*  62 */     float lowMid = 0.5F;
/*  63 */     float mid = 0.65F;
/*  64 */     float midHigh = 0.85F;
/*  65 */     float high = 1.0F;
/*     */     
/*  67 */     add(0.3F, Items.JUNGLE_LEAVES);
/*  68 */     add(0.3F, Items.OAK_LEAVES);
/*  69 */     add(0.3F, Items.SPRUCE_LEAVES);
/*  70 */     add(0.3F, Items.DARK_OAK_LEAVES);
/*  71 */     add(0.3F, Items.PALE_OAK_LEAVES);
/*  72 */     add(0.3F, Items.ACACIA_LEAVES);
/*  73 */     add(0.3F, Items.CHERRY_LEAVES);
/*  74 */     add(0.3F, Items.BIRCH_LEAVES);
/*  75 */     add(0.3F, Items.AZALEA_LEAVES);
/*  76 */     add(0.3F, Items.MANGROVE_LEAVES);
/*  77 */     add(0.3F, Items.OAK_SAPLING);
/*  78 */     add(0.3F, Items.SPRUCE_SAPLING);
/*  79 */     add(0.3F, Items.BIRCH_SAPLING);
/*  80 */     add(0.3F, Items.JUNGLE_SAPLING);
/*  81 */     add(0.3F, Items.ACACIA_SAPLING);
/*  82 */     add(0.3F, Items.CHERRY_SAPLING);
/*  83 */     add(0.3F, Items.DARK_OAK_SAPLING);
/*  84 */     add(0.3F, Items.PALE_OAK_SAPLING);
/*  85 */     add(0.3F, Items.MANGROVE_PROPAGULE);
/*  86 */     add(0.3F, Items.BEETROOT_SEEDS);
/*  87 */     add(0.3F, Items.DRIED_KELP);
/*  88 */     add(0.3F, Items.SHORT_GRASS);
/*  89 */     add(0.3F, Items.KELP);
/*  90 */     add(0.3F, Items.MELON_SEEDS);
/*  91 */     add(0.3F, Items.PUMPKIN_SEEDS);
/*  92 */     add(0.3F, Items.SEAGRASS);
/*  93 */     add(0.3F, Items.SWEET_BERRIES);
/*  94 */     add(0.3F, Items.GLOW_BERRIES);
/*  95 */     add(0.3F, Items.WHEAT_SEEDS);
/*  96 */     add(0.3F, Items.MOSS_CARPET);
/*  97 */     add(0.3F, Items.PALE_MOSS_CARPET);
/*  98 */     add(0.3F, Items.PALE_HANGING_MOSS);
/*  99 */     add(0.3F, Items.PINK_PETALS);
/* 100 */     add(0.3F, Items.WILDFLOWERS);
/* 101 */     add(0.3F, Items.LEAF_LITTER);
/* 102 */     add(0.3F, Items.SMALL_DRIPLEAF);
/* 103 */     add(0.3F, Items.HANGING_ROOTS);
/* 104 */     add(0.3F, Items.MANGROVE_ROOTS);
/* 105 */     add(0.3F, Items.TORCHFLOWER_SEEDS);
/* 106 */     add(0.3F, Items.PITCHER_POD);
/* 107 */     add(0.3F, Items.FIREFLY_BUSH);
/* 108 */     add(0.3F, Items.BUSH);
/* 109 */     add(0.3F, Items.CACTUS_FLOWER);
/* 110 */     add(0.3F, Items.DRY_SHORT_GRASS);
/* 111 */     add(0.3F, Items.DRY_TALL_GRASS);
/*     */     
/* 113 */     add(0.5F, Items.DRIED_KELP_BLOCK);
/* 114 */     add(0.5F, Items.TALL_GRASS);
/* 115 */     add(0.5F, Items.FLOWERING_AZALEA_LEAVES);
/* 116 */     add(0.5F, Items.CACTUS);
/* 117 */     add(0.5F, Items.SUGAR_CANE);
/* 118 */     add(0.5F, Items.VINE);
/* 119 */     add(0.5F, Items.NETHER_SPROUTS);
/* 120 */     add(0.5F, Items.WEEPING_VINES);
/* 121 */     add(0.5F, Items.TWISTING_VINES);
/* 122 */     add(0.5F, Items.MELON_SLICE);
/* 123 */     add(0.5F, Items.GLOW_LICHEN);
/*     */     
/* 125 */     add(0.65F, Items.SEA_PICKLE);
/* 126 */     add(0.65F, Items.LILY_PAD);
/* 127 */     add(0.65F, Items.PUMPKIN);
/* 128 */     add(0.65F, Items.CARVED_PUMPKIN);
/* 129 */     add(0.65F, Items.MELON);
/* 130 */     add(0.65F, Items.APPLE);
/* 131 */     add(0.65F, Items.BEETROOT);
/* 132 */     add(0.65F, Items.CARROT);
/* 133 */     add(0.65F, Items.COCOA_BEANS);
/* 134 */     add(0.65F, Items.POTATO);
/* 135 */     add(0.65F, Items.WHEAT);
/* 136 */     add(0.65F, Items.BROWN_MUSHROOM);
/* 137 */     add(0.65F, Items.RED_MUSHROOM);
/* 138 */     add(0.65F, Items.MUSHROOM_STEM);
/* 139 */     add(0.65F, Items.CRIMSON_FUNGUS);
/* 140 */     add(0.65F, Items.WARPED_FUNGUS);
/* 141 */     add(0.65F, Items.NETHER_WART);
/* 142 */     add(0.65F, Items.CRIMSON_ROOTS);
/* 143 */     add(0.65F, Items.WARPED_ROOTS);
/* 144 */     add(0.65F, Items.SHROOMLIGHT);
/* 145 */     add(0.65F, Items.DANDELION);
/* 146 */     add(0.65F, Items.POPPY);
/* 147 */     add(0.65F, Items.BLUE_ORCHID);
/* 148 */     add(0.65F, Items.ALLIUM);
/* 149 */     add(0.65F, Items.AZURE_BLUET);
/* 150 */     add(0.65F, Items.RED_TULIP);
/* 151 */     add(0.65F, Items.ORANGE_TULIP);
/* 152 */     add(0.65F, Items.WHITE_TULIP);
/* 153 */     add(0.65F, Items.PINK_TULIP);
/* 154 */     add(0.65F, Items.OXEYE_DAISY);
/* 155 */     add(0.65F, Items.CORNFLOWER);
/* 156 */     add(0.65F, Items.LILY_OF_THE_VALLEY);
/* 157 */     add(0.65F, Items.WITHER_ROSE);
/* 158 */     add(0.65F, Items.OPEN_EYEBLOSSOM);
/* 159 */     add(0.65F, Items.CLOSED_EYEBLOSSOM);
/* 160 */     add(0.65F, Items.FERN);
/* 161 */     add(0.65F, Items.SUNFLOWER);
/* 162 */     add(0.65F, Items.LILAC);
/* 163 */     add(0.65F, Items.ROSE_BUSH);
/* 164 */     add(0.65F, Items.PEONY);
/* 165 */     add(0.65F, Items.LARGE_FERN);
/* 166 */     add(0.65F, Items.SPORE_BLOSSOM);
/* 167 */     add(0.65F, Items.AZALEA);
/* 168 */     add(0.65F, Items.MOSS_BLOCK);
/* 169 */     add(0.65F, Items.PALE_MOSS_BLOCK);
/* 170 */     add(0.65F, Items.BIG_DRIPLEAF);
/*     */     
/* 172 */     add(0.85F, Items.HAY_BLOCK);
/* 173 */     add(0.85F, Items.BROWN_MUSHROOM_BLOCK);
/* 174 */     add(0.85F, Items.RED_MUSHROOM_BLOCK);
/* 175 */     add(0.85F, Items.NETHER_WART_BLOCK);
/* 176 */     add(0.85F, Items.WARPED_WART_BLOCK);
/* 177 */     add(0.85F, Items.FLOWERING_AZALEA);
/* 178 */     add(0.85F, Items.BREAD);
/* 179 */     add(0.85F, Items.BAKED_POTATO);
/* 180 */     add(0.85F, Items.COOKIE);
/* 181 */     add(0.85F, Items.TORCHFLOWER);
/* 182 */     add(0.85F, Items.PITCHER_PLANT);
/*     */     
/* 184 */     add(1.0F, Items.CAKE);
/* 185 */     add(1.0F, Items.PUMPKIN_PIE);
/*     */   }
/*     */ 
/*     */   
/* 189 */   private static void add(float value, ItemLike item) { COMPOSTABLES.put(item.asItem(), value); }
/*     */ 
/*     */ 
/*     */   
/* 193 */   private static final VoxelShape[] SHAPES = (VoxelShape[])Util.make(() -> {
/* 194 */         shapes = Block.boxes(8, ());
/* 195 */         shapes[8] = shapes[7];
/* 196 */         return shapes;
/*     */       });
/*     */   
/*     */   public ComposterBlock(BlockBehaviour.Properties properties) {
/* 200 */     super(properties);
/* 201 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   public static void handleFill(Level level, BlockPos pos, boolean success) {
/* 205 */     BlockState state = level.getBlockState(pos);
/*     */     
/* 207 */     level.playLocalSound(pos, success ? SoundEvents.COMPOSTER_FILL_SUCCESS : SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */     
/* 209 */     double centerHeight = state.getShape(level, pos).max(Direction.Axis.Y, 0.5D, 0.5D) + 0.03125D;
/* 210 */     double sideOffsetPixels = 2.0D;
/* 211 */     double sideOffset = 0.1875D;
/* 212 */     double width = 0.625D;
/*     */     
/* 214 */     RandomSource random = level.getRandom();
/* 215 */     for (int i = 0; i < 10; i++) {
/* 216 */       double xa = random.nextGaussian() * 0.02D;
/* 217 */       double ya = random.nextGaussian() * 0.02D;
/* 218 */       double za = random.nextGaussian() * 0.02D;
/* 219 */       level.addParticle(ParticleTypes.COMPOSTER, pos
/*     */           
/* 221 */           .getX() + 0.1875D + 0.625D * random.nextFloat(), pos
/* 222 */           .getY() + centerHeight + random.nextFloat() * (1.0D - centerHeight), pos
/* 223 */           .getZ() + 0.1875D + 0.625D * random.nextFloat(), xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 231 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(LEVEL)).intValue()]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) { return Shapes.block(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[0]; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 247 */     if (((Integer)state.getValue(LEVEL)).intValue() == 7) {
/* 248 */       level.scheduleTick(pos, state.getBlock(), 20);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 254 */     int fillLevel = ((Integer)state.getValue(LEVEL)).intValue();
/*     */     
/* 256 */     if (fillLevel < 8 && COMPOSTABLES.containsKey(itemStack.getItem())) {
/* 257 */       if (fillLevel < 7 && !level.isClientSide()) {
/* 258 */         BlockState newState = addItem(player, state, level, pos, itemStack);
/* 259 */         level.levelEvent(1500, pos, (state != newState) ? 1 : 0);
/* 260 */         player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
/*     */         
/* 262 */         itemStack.consume(1, player);
/*     */       } 
/*     */       
/* 265 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 268 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 273 */     int fillLevel = ((Integer)state.getValue(LEVEL)).intValue();
/*     */     
/* 275 */     if (fillLevel == 8) {
/* 276 */       extractProduce(player, state, level, pos);
/* 277 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 280 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static BlockState insertItem(Entity sourceEntity, BlockState state, ServerLevel level, ItemStack itemStack, BlockPos pos) {
/* 284 */     int fillLevel = ((Integer)state.getValue(LEVEL)).intValue();
/*     */     
/* 286 */     if (fillLevel < 7 && COMPOSTABLES.containsKey(itemStack.getItem())) {
/* 287 */       BlockState newState = addItem(sourceEntity, state, level, pos, itemStack);
/* 288 */       itemStack.shrink(1);
/* 289 */       return newState;
/*     */     } 
/*     */     
/* 292 */     return state;
/*     */   }
/*     */   
/*     */   public static BlockState extractProduce(Entity sourceEntity, BlockState state, Level level, BlockPos pos) {
/* 296 */     if (!level.isClientSide()) {
/* 297 */       Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5D, 1.01D, 0.5D).offsetRandomXZ(level.random, 0.7F);
/* 298 */       ItemEntity entity = new ItemEntity(level, itemPos.x(), itemPos.y(), itemPos.z(), new ItemStack(Items.BONE_MEAL));
/* 299 */       entity.setDefaultPickUpDelay();
/* 300 */       level.addFreshEntity(entity);
/*     */     } 
/*     */     
/* 303 */     BlockState emptyState = empty(sourceEntity, state, level, pos);
/* 304 */     level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 305 */     return emptyState;
/*     */   }
/*     */   
/*     */   private static BlockState empty(Entity sourceEntity, BlockState state, LevelAccessor level, BlockPos pos) {
/* 309 */     BlockState newState = (BlockState)state.setValue(LEVEL, Integer.valueOf(0));
/* 310 */     level.setBlock(pos, newState, 3);
/* 311 */     level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
/* 312 */     return newState;
/*     */   }
/*     */   
/*     */   private static BlockState addItem(Entity sourceEntity, BlockState state, LevelAccessor level, BlockPos pos, ItemStack itemStack) {
/* 316 */     int fillLevel = ((Integer)state.getValue(LEVEL)).intValue();
/* 317 */     float chance = COMPOSTABLES.getFloat(itemStack.getItem());
/* 318 */     if ((fillLevel == 0 && chance > 0.0F) || level.getRandom().nextDouble() < chance) {
/* 319 */       int newLevel = fillLevel + 1;
/* 320 */       BlockState newState = (BlockState)state.setValue(LEVEL, Integer.valueOf(newLevel));
/* 321 */       level.setBlock(pos, newState, 3);
/* 322 */       level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
/*     */       
/* 324 */       if (newLevel == 7) {
/* 325 */         level.scheduleTick(pos, state.getBlock(), 20);
/*     */       }
/*     */       
/* 328 */       return newState;
/*     */     } 
/* 330 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 335 */     if (((Integer)state.getValue(LEVEL)).intValue() == 7) {
/* 336 */       level.setBlock(pos, (BlockState)state.cycle(LEVEL), 3);
/* 337 */       level.playSound(null, pos, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 343 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return ((Integer)state.getValue(LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LEVEL }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 358 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
/* 363 */     int contentLevel = ((Integer)state.getValue(LEVEL)).intValue();
/* 364 */     if (contentLevel == 8) {
/* 365 */       return new OutputContainer(state, level, pos, new ItemStack(Items.BONE_MEAL));
/*     */     }
/*     */     
/* 368 */     if (contentLevel < 7) {
/* 369 */       return new InputContainer(state, level, pos);
/*     */     }
/*     */     
/* 372 */     return new EmptyContainer();
/*     */   }
/*     */   
/*     */   private static class EmptyContainer
/*     */     extends SimpleContainer implements WorldlyContainer {
/* 377 */     public EmptyContainer() { super(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     public int[] getSlotsForFace(Direction direction) { return new int[0]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 387 */     public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 392 */     public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return false; }
/*     */   }
/*     */   
/*     */   private static class OutputContainer
/*     */     extends SimpleContainer implements WorldlyContainer {
/*     */     private final BlockState state;
/*     */     private final LevelAccessor level;
/*     */     private final BlockPos pos;
/*     */     private boolean changed;
/*     */     
/*     */     public OutputContainer(BlockState state, LevelAccessor level, BlockPos pos, ItemStack contents) {
/* 403 */       super(new ItemStack[] { contents });
/* 404 */       this.state = state;
/* 405 */       this.level = level;
/* 406 */       this.pos = pos;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 411 */     public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 416 */     public int[] getSlotsForFace(Direction direction) { new int[1][0] = 0; return (direction == Direction.DOWN) ? new int[1] : new int[0]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 421 */     public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return (!this.changed && direction == Direction.DOWN && itemStack.is(Items.BONE_MEAL)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setChanged() {
/* 431 */       ComposterBlock.empty(null, this.state, this.level, this.pos);
/* 432 */       this.changed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InputContainer extends SimpleContainer implements WorldlyContainer {
/*     */     private final BlockState state;
/*     */     private final LevelAccessor level;
/*     */     private final BlockPos pos;
/*     */     private boolean changed;
/*     */     
/*     */     public InputContainer(BlockState state, LevelAccessor level, BlockPos pos) {
/* 443 */       super(1);
/* 444 */       this.state = state;
/* 445 */       this.level = level;
/* 446 */       this.pos = pos;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 451 */     public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     public int[] getSlotsForFace(Direction direction) { new int[1][0] = 0; return (direction == Direction.UP) ? new int[1] : new int[0]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return (!this.changed && direction == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(itemStack.getItem())); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return false; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setChanged() {
/* 471 */       ItemStack contents = getItem(0);
/* 472 */       if (!contents.isEmpty()) {
/* 473 */         this.changed = true;
/* 474 */         BlockState newState = ComposterBlock.addItem(null, this.state, this.level, this.pos, contents);
/* 475 */         this.level.levelEvent(1500, this.pos, (newState != this.state) ? 1 : 0);
/* 476 */         removeItemNoUpdate(0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ComposterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */