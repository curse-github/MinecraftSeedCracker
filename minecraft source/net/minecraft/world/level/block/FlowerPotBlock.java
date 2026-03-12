/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FlowerPotBlock extends Block {
/*  33 */   public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/*  34 */         .byNameCodec().fieldOf("potted").forGetter(()), 
/*  35 */         propertiesCodec())
/*  36 */       .apply(i, FlowerPotBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  40 */   public MapCodec<FlowerPotBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  43 */   private static final Map<Block, Block> POTTED_BY_CONTENT = Maps.newHashMap();
/*     */   
/*  45 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 6.0D);
/*     */   
/*     */   private final Block potted;
/*     */   
/*     */   public FlowerPotBlock(Block potted, BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  51 */     this.potted = potted;
/*     */     
/*  53 */     POTTED_BY_CONTENT.put(potted, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  63 */     Item item = itemStack.getItem(); BlockItem blockItem = (BlockItem)item; BlockState newContents = ((item instanceof BlockItem) ? (Block)POTTED_BY_CONTENT.getOrDefault(blockItem.getBlock(), Blocks.AIR) : Blocks.AIR).defaultBlockState();
/*  64 */     if (newContents.isAir()) {
/*  65 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  68 */     if (!isEmpty()) {
/*  69 */       return InteractionResult.CONSUME;
/*     */     }
/*     */     
/*  72 */     level.setBlock(pos, newContents, 3);
/*  73 */     level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/*  74 */     player.awardStat(Stats.POT_FLOWER);
/*     */     
/*  76 */     itemStack.consume(1, player);
/*  77 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  82 */     if (isEmpty()) {
/*  83 */       return InteractionResult.CONSUME;
/*     */     }
/*     */     
/*  86 */     ItemStack plant = new ItemStack(this.potted);
/*     */     
/*  88 */     if (!player.addItem(plant)) {
/*  89 */       player.drop(plant, false);
/*     */     }
/*     */     
/*  92 */     level.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), 3);
/*  93 */     level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/*     */     
/*  95 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 100 */     if (isEmpty()) {
/* 101 */       return super.getCloneItemStack(level, pos, state, includeData);
/*     */     }
/* 103 */     return new ItemStack(this.potted);
/*     */   }
/*     */ 
/*     */   
/* 107 */   private boolean isEmpty() { return (this.potted == Blocks.AIR); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 112 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 113 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 116 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/* 120 */   public Block getPotted() { return this.potted; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected boolean isRandomlyTicking(BlockState state) { return (state.is(Blocks.POTTED_OPEN_EYEBLOSSOM) || state.is(Blocks.POTTED_CLOSED_EYEBLOSSOM)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 135 */     if (isRandomlyTicking(state)) {
/* 136 */       boolean isOpen = (this.potted == Blocks.OPEN_EYEBLOSSOM);
/* 137 */       boolean shouldBeOpen = ((TriState)level.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos)).toBoolean(isOpen);
/* 138 */       if (isOpen != shouldBeOpen) {
/* 139 */         level.setBlock(pos, opposite(state), 3);
/* 140 */         EyeblossomBlock.Type newType = EyeblossomBlock.Type.fromBoolean(isOpen).transform();
/* 141 */         newType.spawnTransformParticle(level, pos, random);
/* 142 */         level.playSound(null, pos, newType.longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/* 145 */     super.randomTick(state, level, pos, random);
/*     */   }
/*     */   
/*     */   public BlockState opposite(BlockState state) {
/* 149 */     if (state.is(Blocks.POTTED_OPEN_EYEBLOSSOM)) {
/* 150 */       return Blocks.POTTED_CLOSED_EYEBLOSSOM.defaultBlockState();
/*     */     }
/* 152 */     if (state.is(Blocks.POTTED_CLOSED_EYEBLOSSOM)) {
/* 153 */       return Blocks.POTTED_OPEN_EYEBLOSSOM.defaultBlockState();
/*     */     }
/* 155 */     return state;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FlowerPotBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */