/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CarvedPumpkinBlock extends HorizontalDirectionalBlock {
/*  32 */   public static final MapCodec<CarvedPumpkinBlock> CODEC = simpleCodec(CarvedPumpkinBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  36 */   public MapCodec<? extends CarvedPumpkinBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  39 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*     */   private BlockPattern snowGolemBase;
/*     */   
/*     */   private BlockPattern snowGolemFull;
/*     */   
/*     */   private BlockPattern ironGolemBase;
/*     */   
/*     */   private BlockPattern ironGolemFull;
/*     */   
/*     */   private BlockPattern copperGolemBase;
/*     */   
/*     */   private BlockPattern copperGolemFull;
/*     */   
/*     */   protected CarvedPumpkinBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  60 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/*  63 */     trySpawnGolem(level, pos);
/*     */   }
/*     */ 
/*     */   
/*  67 */   public boolean canSpawnGolem(LevelReader level, BlockPos topPos) { return (getOrCreateSnowGolemBase().find(level, topPos) != null || getOrCreateIronGolemBase().find(level, topPos) != null || getOrCreateCopperGolemBase().find(level, topPos) != null); }
/*     */ 
/*     */   
/*     */   private void trySpawnGolem(Level level, BlockPos topPos) {
/*  71 */     BlockPattern.BlockPatternMatch snowGolemMatch = getOrCreateSnowGolemFull().find(level, topPos);
/*  72 */     if (snowGolemMatch != null) {
/*  73 */       SnowGolem snowGolem = (SnowGolem)EntityType.SNOW_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
/*  74 */       if (snowGolem != null) {
/*  75 */         spawnGolemInWorld(level, snowGolemMatch, snowGolem, snowGolemMatch.getBlock(0, 2, 0).getPos());
/*     */         return;
/*     */       } 
/*     */     } 
/*  79 */     BlockPattern.BlockPatternMatch ironGolemMatch = getOrCreateIronGolemFull().find(level, topPos);
/*  80 */     if (ironGolemMatch != null) {
/*  81 */       IronGolem ironGolem = (IronGolem)EntityType.IRON_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
/*  82 */       if (ironGolem != null) {
/*  83 */         ironGolem.setPlayerCreated(true);
/*  84 */         spawnGolemInWorld(level, ironGolemMatch, ironGolem, ironGolemMatch.getBlock(1, 2, 0).getPos());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  89 */     BlockPattern.BlockPatternMatch copperGolemMatch = getOrCreateCopperGolemFull().find(level, topPos);
/*  90 */     if (copperGolemMatch != null) {
/*  91 */       CopperGolem copperGolem = (CopperGolem)EntityType.COPPER_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
/*  92 */       if (copperGolem != null) {
/*  93 */         spawnGolemInWorld(level, copperGolemMatch, copperGolem, copperGolemMatch.getBlock(0, 0, 0).getPos());
/*  94 */         replaceCopperBlockWithChest(level, copperGolemMatch);
/*  95 */         copperGolem.spawn(getWeatherStateFromPattern(copperGolemMatch));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private WeatheringCopper.WeatherState getWeatherStateFromPattern(BlockPattern.BlockPatternMatch copperGolemMatch) {
/* 101 */     BlockState state = copperGolemMatch.getBlock(0, 1, 0).getState();
/* 102 */     Block block = state.getBlock();
/* 103 */     if (block instanceof WeatheringCopper) { WeatheringCopper copper = (WeatheringCopper)block;
/* 104 */       return (WeatheringCopper.WeatherState)copper.getAge(); }
/*     */     
/* 106 */     return (WeatheringCopper.WeatherState)((WeatheringCopper)Optional.ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(state.getBlock()))
/* 107 */       .filter(weatheringCopper -> weatheringCopper instanceof WeatheringCopper)
/* 108 */       .map(weatheringCopper -> (WeatheringCopper)weatheringCopper)
/* 109 */       .orElse((WeatheringCopper)Blocks.COPPER_BLOCK)).getAge();
/*     */   }
/*     */   
/*     */   private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch match, Entity golem, BlockPos spawnPos) {
/* 113 */     clearPatternBlocks(level, match);
/*     */     
/* 115 */     golem.snapTo(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.05D, spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 116 */     level.addFreshEntity(golem);
/*     */     
/* 118 */     for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0D))) {
/* 119 */       CriteriaTriggers.SUMMONED_ENTITY.trigger(player, golem);
/*     */     }
/*     */     
/* 122 */     updatePatternBlocks(level, match);
/*     */   }
/*     */   
/*     */   public static void clearPatternBlocks(Level level, BlockPattern.BlockPatternMatch match) {
/* 126 */     for (int x = 0; x < match.getWidth(); x++) {
/* 127 */       for (int y = 0; y < match.getHeight(); y++) {
/* 128 */         BlockInWorld block = match.getBlock(x, y, 0);
/* 129 */         level.setBlock(block.getPos(), Blocks.AIR.defaultBlockState(), 2);
/* 130 */         level.levelEvent(2001, block.getPos(), Block.getId(block.getState()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void updatePatternBlocks(Level level, BlockPattern.BlockPatternMatch match) {
/* 136 */     for (int x = 0; x < match.getWidth(); x++) {
/* 137 */       for (int y = 0; y < match.getHeight(); y++) {
/* 138 */         BlockInWorld block = match.getBlock(x, y, 0);
/* 139 */         level.updateNeighborsAt(block.getPos(), Blocks.AIR);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*     */ 
/*     */   
/* 154 */   private static final Predicate<BlockState> PUMPKINS_PREDICATE = input -> (input.is(Blocks.CARVED_PUMPKIN) || input.is(Blocks.JACK_O_LANTERN));
/*     */   
/*     */   private BlockPattern getOrCreateSnowGolemBase() {
/* 157 */     if (this.snowGolemBase == null) {
/* 158 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 165 */         .snowGolemBase = BlockPatternBuilder.start().aisle(new String[] { " ", "#", "#" }).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
/*     */     }
/*     */     
/* 168 */     return this.snowGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateSnowGolemFull() {
/* 172 */     if (this.snowGolemFull == null) {
/* 173 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         .snowGolemFull = BlockPatternBuilder.start().aisle(new String[] { "^", "#", "#" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
/*     */     }
/*     */     
/* 184 */     return this.snowGolemFull;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateIronGolemBase() {
/* 188 */     if (this.ironGolemBase == null) {
/* 189 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 197 */         .ironGolemBase = BlockPatternBuilder.start().aisle(new String[] { "~ ~", "###", "~#~" }).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir)).build();
/*     */     }
/*     */     
/* 200 */     return this.ironGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateIronGolemFull() {
/* 204 */     if (this.ironGolemFull == null) {
/* 205 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         .ironGolemFull = BlockPatternBuilder.start().aisle(new String[] { "~^~", "###", "~#~" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir)).build();
/*     */     }
/*     */     
/* 217 */     return this.ironGolemFull;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateCopperGolemBase() {
/* 221 */     if (this.copperGolemBase == null) {
/* 222 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 228 */         .copperGolemBase = BlockPatternBuilder.start().aisle(new String[] { " ", "#" }).where('#', BlockInWorld.hasState(block -> block.is(BlockTags.COPPER))).build();
/*     */     }
/*     */     
/* 231 */     return this.copperGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateCopperGolemFull() {
/* 235 */     if (this.copperGolemFull == null) {
/* 236 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 243 */         .copperGolemFull = BlockPatternBuilder.start().aisle(new String[] { "^", "#" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(block -> block.is(BlockTags.COPPER))).build();
/*     */     }
/*     */     
/* 246 */     return this.copperGolemFull;
/*     */   }
/*     */   
/*     */   public void replaceCopperBlockWithChest(Level level, BlockPattern.BlockPatternMatch match) {
/* 250 */     BlockInWorld copperBlock = match.getBlock(0, 1, 0);
/* 251 */     BlockInWorld pumpkinBlock = match.getBlock(0, 0, 0);
/* 252 */     Direction facing = (Direction)pumpkinBlock.getState().getValue(FACING);
/* 253 */     BlockState blockState = CopperChestBlock.getFromCopperBlock(copperBlock.getState().getBlock(), facing, level, copperBlock.getPos());
/* 254 */     level.setBlock(copperBlock.getPos(), blockState, 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CarvedPumpkinBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */