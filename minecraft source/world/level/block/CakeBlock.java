/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CakeBlock extends Block {
/*  33 */   public static final MapCodec<CakeBlock> CODEC = simpleCodec(CakeBlock::new);
/*     */   
/*     */   public static final int MAX_BITES = 6;
/*     */   
/*  37 */   public MapCodec<CakeBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final IntegerProperty BITES = BlockStateProperties.BITES;
/*     */   
/*  43 */   public static final int FULL_CAKE_SIGNAL = getOutputSignal(0);
/*     */   
/*  45 */   private static final VoxelShape[] SHAPES = Block.boxes(6, bite -> Block.box((1 + bite * 2), 0.0D, 1.0D, 15.0D, 8.0D, 15.0D));
/*     */   
/*     */   protected CakeBlock(BlockBehaviour.Properties properties) {
/*  48 */     super(properties);
/*  49 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(BITES, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  54 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(BITES)).intValue()]; }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*     */     CandleBlock candleBlock;
/*  59 */     Item item = itemStack.getItem();
/*  60 */     if (itemStack.is(ItemTags.CANDLES) && ((Integer)state.getValue(BITES)).intValue() == 0) { Block block = Block.byItem(item); if (block instanceof CandleBlock) { candleBlock = (CandleBlock)block; }
/*  61 */       else { return InteractionResult.TRY_WITH_EMPTY_HAND; }  } else { return InteractionResult.TRY_WITH_EMPTY_HAND; }
/*     */ 
/*     */     
/*  64 */     itemStack.consume(1, player);
/*  65 */     level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  66 */     level.setBlockAndUpdate(pos, CandleCakeBlock.byCandle(candleBlock));
/*  67 */     level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/*  68 */     player.awardStat(Stats.ITEM_USED.get(item));
/*  69 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  74 */     if (level.isClientSide()) {
/*  75 */       if (eat(level, pos, state, player).consumesAction())
/*  76 */         return InteractionResult.SUCCESS; 
/*  77 */       if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
/*  78 */         return InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/*  82 */     return eat(level, pos, state, player);
/*     */   }
/*     */   
/*     */   protected static InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
/*  86 */     if (!player.canEat(false)) {
/*  87 */       return InteractionResult.PASS;
/*     */     }
/*  89 */     player.awardStat(Stats.EAT_CAKE_SLICE);
/*     */     
/*  91 */     player.getFoodData().eat(2, 0.1F);
/*  92 */     int bites = ((Integer)state.getValue(BITES)).intValue();
/*     */     
/*  94 */     level.gameEvent(player, GameEvent.EAT, pos);
/*     */     
/*  96 */     if (bites < 6) {
/*  97 */       level.setBlock(pos, (BlockState)state.setValue(BITES, Integer.valueOf(bites + 1)), 3);
/*     */     } else {
/*  99 */       level.removeBlock(pos, false);
/* 100 */       level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
/*     */     } 
/*     */     
/* 103 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 108 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 109 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 112 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 117 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.below()).isSolid(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { BITES }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return getOutputSignal(((Integer)state.getValue(BITES)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static int getOutputSignal(int bitesTaken) { return (7 - bitesTaken) * 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CakeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */