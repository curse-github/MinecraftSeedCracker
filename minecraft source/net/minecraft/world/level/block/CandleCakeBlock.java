/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CandleCakeBlock extends AbstractCandleBlock {
/*  34 */   public static final MapCodec<CandleCakeBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/*  35 */         .byNameCodec().fieldOf("candle").forGetter(()), 
/*  36 */         propertiesCodec())
/*  37 */       .apply(i, CandleCakeBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  41 */   public MapCodec<CandleCakeBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  44 */   public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
/*     */   
/*  46 */   private static final VoxelShape SHAPE = Shapes.or(
/*  47 */       Block.column(2.0D, 8.0D, 14.0D), 
/*  48 */       Block.column(14.0D, 0.0D, 8.0D));
/*     */ 
/*     */   
/*  51 */   private static final Map<CandleBlock, CandleCakeBlock> BY_CANDLE = Maps.newHashMap();
/*     */   
/*  53 */   private static final Iterable<Vec3> PARTICLE_OFFSETS = List.of((new Vec3(8.0D, 16.0D, 8.0D)).scale(0.0625D));
/*     */   
/*     */   private final CandleBlock candleBlock;
/*     */   
/*     */   protected CandleCakeBlock(Block block, BlockBehaviour.Properties properties) {
/*  58 */     super(properties);
/*  59 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LIT, Boolean.valueOf(false)));
/*     */     
/*  61 */     if (block instanceof CandleBlock) { matchingCandleBlock = (CandleBlock)block; }
/*  62 */     else { throw new IllegalArgumentException("Expected block to be of " + String.valueOf(CandleBlock.class) + " was " + String.valueOf(block.getClass())); }
/*     */ 
/*     */     
/*  65 */     BY_CANDLE.put(matchingCandleBlock, this);
/*  66 */     this.candleBlock = matchingCandleBlock;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected Iterable<Vec3> getParticleOffsets(BlockState state) { return PARTICLE_OFFSETS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  81 */     if (itemStack.is(Items.FLINT_AND_STEEL) || itemStack.is(Items.FIRE_CHARGE)) {
/*  82 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  85 */     if (candleHit(hitResult) && itemStack.isEmpty() && ((Boolean)state.getValue(LIT)).booleanValue()) {
/*  86 */       extinguish(player, state, level, pos);
/*  87 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  90 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  95 */     InteractionResult eatResult = CakeBlock.eat(level, pos, Blocks.CAKE.defaultBlockState(), player);
/*  96 */     if (eatResult.consumesAction()) {
/*  97 */       dropResources(state, level, pos);
/*     */     }
/*  99 */     return eatResult;
/*     */   }
/*     */ 
/*     */   
/* 103 */   private static boolean candleHit(BlockHitResult hitResult) { return ((hitResult.getLocation()).y - hitResult.getBlockPos().getY() > 0.5D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LIT }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Blocks.CAKE); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 118 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 119 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 122 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 127 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.below()).isSolid(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return CakeBlock.FULL_CAKE_SIGNAL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public static BlockState byCandle(CandleBlock block) { return ((CandleCakeBlock)BY_CANDLE.get(block)).defaultBlockState(); }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public static boolean canLight(BlockState state) { return state.is(BlockTags.CANDLE_CAKES, s -> (s.hasProperty(LIT) && !((Boolean)state.getValue(LIT)).booleanValue())); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CandleCakeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */