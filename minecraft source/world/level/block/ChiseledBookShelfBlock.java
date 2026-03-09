/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class ChiseledBookShelfBlock extends BaseEntityBlock implements SelectableSlotContainer {
/*  35 */   public static final MapCodec<ChiseledBookShelfBlock> CODEC = simpleCodec(ChiseledBookShelfBlock::new);
/*     */   
/*  37 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  38 */   public static final BooleanProperty SLOT_0_OCCUPIED = BlockStateProperties.SLOT_0_OCCUPIED;
/*  39 */   public static final BooleanProperty SLOT_1_OCCUPIED = BlockStateProperties.SLOT_1_OCCUPIED;
/*  40 */   public static final BooleanProperty SLOT_2_OCCUPIED = BlockStateProperties.SLOT_2_OCCUPIED;
/*  41 */   public static final BooleanProperty SLOT_3_OCCUPIED = BlockStateProperties.SLOT_3_OCCUPIED;
/*  42 */   public static final BooleanProperty SLOT_4_OCCUPIED = BlockStateProperties.SLOT_4_OCCUPIED;
/*  43 */   public static final BooleanProperty SLOT_5_OCCUPIED = BlockStateProperties.SLOT_5_OCCUPIED;
/*     */   private static final int MAX_BOOKS_IN_STORAGE = 6;
/*     */   private static final int BOOKS_PER_ROW = 3;
/*     */   
/*  47 */   public MapCodec<ChiseledBookShelfBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES = List.of(SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED, SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED);
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
/*  64 */   public int getRows() { return 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public int getColumns() { return 3; }
/*     */ 
/*     */   
/*     */   public ChiseledBookShelfBlock(BlockBehaviour.Properties properties) {
/*  73 */     super(properties);
/*     */     
/*  75 */     BlockState defaultState = (BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH);
/*     */     
/*  77 */     for (BooleanProperty property : SLOT_OCCUPIED_PROPERTIES) {
/*  78 */       defaultState = (BlockState)defaultState.setValue(property, Boolean.valueOf(false));
/*     */     }
/*     */     
/*  81 */     registerDefaultState(defaultState);
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*     */     ChiseledBookShelfBlockEntity bookshelfBlock;
/*  86 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ChiseledBookShelfBlockEntity) { bookshelfBlock = (ChiseledBookShelfBlockEntity)blockEntity; }
/*  87 */     else { return InteractionResult.PASS; }
/*     */ 
/*     */     
/*  90 */     if (!itemStack.is(ItemTags.BOOKSHELF_BOOKS)) {
/*  91 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  94 */     OptionalInt hitSlot = getHitSlot(hitResult, (Direction)state.getValue(FACING));
/*  95 */     if (hitSlot.isEmpty()) {
/*  96 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  99 */     if (((Boolean)state.getValue((Property)SLOT_OCCUPIED_PROPERTIES.get(hitSlot.getAsInt()))).booleanValue()) {
/* 100 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 103 */     addBook(level, pos, player, bookshelfBlock, itemStack, hitSlot.getAsInt());
/* 104 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     ChiseledBookShelfBlockEntity bookshelfBlock;
/* 109 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ChiseledBookShelfBlockEntity) { bookshelfBlock = (ChiseledBookShelfBlockEntity)blockEntity; }
/* 110 */     else { return InteractionResult.PASS; }
/*     */ 
/*     */     
/* 113 */     OptionalInt hitSlot = getHitSlot(hitResult, (Direction)state.getValue(FACING));
/* 114 */     if (hitSlot.isEmpty()) {
/* 115 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 118 */     if (!((Boolean)state.getValue((Property)SLOT_OCCUPIED_PROPERTIES.get(hitSlot.getAsInt()))).booleanValue()) {
/* 119 */       return InteractionResult.CONSUME;
/*     */     }
/*     */     
/* 122 */     removeBook(level, pos, player, bookshelfBlock, hitSlot.getAsInt());
/* 123 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static void addBook(Level level, BlockPos pos, Player player, ChiseledBookShelfBlockEntity bookshelfBlock, ItemStack itemStack, int slot) {
/* 127 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
/*     */ 
/*     */ 
/*     */     
/* 135 */     SoundEvent soundEvent = itemStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
/*     */     
/* 137 */     bookshelfBlock.setItem(slot, itemStack.consumeAndReturn(1, player));
/* 138 */     level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static void removeBook(Level level, BlockPos pos, Player player, ChiseledBookShelfBlockEntity bookshelfBlock, int slot) {
/* 142 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 146 */     ItemStack retrievedBook = bookshelfBlock.removeItem(slot, 1);
/*     */ 
/*     */ 
/*     */     
/* 150 */     SoundEvent soundEvent = retrievedBook.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
/* 151 */     level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     
/* 153 */     if (!player.getInventory().add(retrievedBook)) {
/* 154 */       player.drop(retrievedBook, false);
/*     */     }
/*     */     
/* 157 */     level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new ChiseledBookShelfBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 167 */     builder.add(new Property[] { FACING });
/* 168 */     Objects.requireNonNull(builder); SLOT_OCCUPIED_PROPERTIES.forEach(xva$0 -> rec$.add(new Property[] { xva$0 }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 198 */     if (level.isClientSide())
/*     */     {
/* 200 */       return 0;
/*     */     }
/*     */     
/* 203 */     BlockEntity blockEntity1 = level.getBlockEntity(pos); if (blockEntity1 instanceof ChiseledBookShelfBlockEntity) { ChiseledBookShelfBlockEntity blockEntity = (ChiseledBookShelfBlockEntity)blockEntity1;
/* 204 */       return blockEntity.getLastInteractedSlot() + 1; }
/*     */     
/* 206 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ChiseledBookShelfBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */