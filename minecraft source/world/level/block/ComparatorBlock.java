/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ComparatorMode;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.ticks.TickPriority;
/*     */ 
/*     */ public class ComparatorBlock extends DiodeBlock implements EntityBlock {
/*  33 */   public static final MapCodec<ComparatorBlock> CODEC = simpleCodec(ComparatorBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  37 */   public MapCodec<ComparatorBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  40 */   public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;
/*     */   
/*     */   public ComparatorBlock(BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(POWERED, Boolean.valueOf(false))).setValue(MODE, ComparatorMode.COMPARE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  49 */   protected int getDelay(BlockState state) { return 2; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  54 */     if (directionToNeighbour == Direction.DOWN && !canSurviveOn(level, neighbourPos, neighbourState)) {
/*  55 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  57 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
/*  62 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  63 */     if (blockEntity instanceof ComparatorBlockEntity) {
/*  64 */       return ((ComparatorBlockEntity)blockEntity).getOutputSignal();
/*     */     }
/*     */     
/*  67 */     return 0;
/*     */   }
/*     */   
/*     */   private int calculateOutputSignal(Level level, BlockPos pos, BlockState state) {
/*  71 */     int inputSignal = getInputSignal(level, pos, state);
/*  72 */     if (inputSignal == 0) {
/*  73 */       return 0;
/*     */     }
/*     */     
/*  76 */     int alternateSignal = getAlternateSignal(level, pos, state);
/*  77 */     if (alternateSignal > inputSignal) {
/*  78 */       return 0;
/*     */     }
/*     */     
/*  81 */     if (state.getValue(MODE) == ComparatorMode.SUBTRACT) {
/*  82 */       return inputSignal - alternateSignal;
/*     */     }
/*     */     
/*  85 */     return inputSignal;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
/*  90 */     int input = getInputSignal(level, pos, state);
/*  91 */     if (input == 0) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     int sideInput = getAlternateSignal(level, pos, state);
/*  96 */     if (input > sideInput) {
/*  97 */       return true;
/*     */     }
/*     */     
/* 100 */     return (input == sideInput && state.getValue(MODE) == ComparatorMode.COMPARE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
/* 105 */     int resultSignal = super.getInputSignal(level, pos, state);
/*     */     
/* 107 */     Direction direction = (Direction)state.getValue(FACING);
/* 108 */     BlockPos targetPos = pos.relative(direction);
/* 109 */     BlockState targetState = level.getBlockState(targetPos);
/*     */     
/* 111 */     if (targetState.hasAnalogOutputSignal()) {
/* 112 */       resultSignal = targetState.getAnalogOutputSignal(level, targetPos, direction.getOpposite());
/* 113 */     } else if (resultSignal < 15 && targetState.isRedstoneConductor(level, targetPos)) {
/* 114 */       targetPos = targetPos.relative(direction);
/* 115 */       targetState = level.getBlockState(targetPos);
/* 116 */       ItemFrame itemFrame = getItemFrame(level, direction, targetPos);
/*     */       
/* 118 */       int itemFrameOrBlockSignal = Math.max(
/* 119 */           (itemFrame == null) ? Integer.MIN_VALUE : itemFrame.getAnalogOutput(), 
/* 120 */           targetState.hasAnalogOutputSignal() ? targetState.getAnalogOutputSignal(level, targetPos, direction.getOpposite()) : Integer.MIN_VALUE);
/*     */ 
/*     */       
/* 123 */       if (itemFrameOrBlockSignal != Integer.MIN_VALUE) {
/* 124 */         resultSignal = itemFrameOrBlockSignal;
/*     */       }
/*     */     } 
/*     */     
/* 128 */     return resultSignal;
/*     */   }
/*     */   
/*     */   private ItemFrame getItemFrame(Level level, Direction direction, BlockPos tPos) {
/* 132 */     List<ItemFrame> itemFrames = level.getEntitiesOfClass(ItemFrame.class, new AABB(tPos.getX(), tPos.getY(), tPos.getZ(), (tPos.getX() + 1), (tPos.getY() + 1), (tPos.getZ() + 1)), entity -> (entity.getDirection() == direction));
/*     */     
/* 134 */     if (itemFrames.size() == 1) {
/* 135 */       return (ItemFrame)itemFrames.get(0);
/*     */     }
/*     */     
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 143 */     if (!(player.getAbilities()).mayBuild) {
/* 144 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 147 */     state = (BlockState)state.cycle(MODE);
/* 148 */     float pitch = (state.getValue(MODE) == ComparatorMode.SUBTRACT) ? 0.55F : 0.5F;
/* 149 */     level.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, pitch);
/*     */     
/* 151 */     level.setBlock(pos, state, 2);
/* 152 */     refreshOutputState(level, pos, state);
/* 153 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
/* 158 */     if (level.getBlockTicks().willTickThisTick(pos, this)) {
/*     */       return;
/*     */     }
/*     */     
/* 162 */     int outputValue = calculateOutputSignal(level, pos, state);
/* 163 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 164 */     int oldValue = (blockEntity instanceof ComparatorBlockEntity) ? ((ComparatorBlockEntity)blockEntity).getOutputSignal() : 0;
/*     */     
/* 166 */     if (outputValue != oldValue || ((Boolean)state.getValue(POWERED)).booleanValue() != shouldTurnOn(level, pos, state)) {
/*     */       
/* 168 */       TickPriority priority = shouldPrioritize(level, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
/* 169 */       level.scheduleTick(pos, this, 2, priority);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshOutputState(Level level, BlockPos pos, BlockState state) {
/* 174 */     int outputValue = calculateOutputSignal(level, pos, state);
/*     */     
/* 176 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 177 */     int oldValue = 0;
/* 178 */     if (blockEntity instanceof ComparatorBlockEntity) { ComparatorBlockEntity comparatorBlockEntity = (ComparatorBlockEntity)blockEntity;
/* 179 */       oldValue = comparatorBlockEntity.getOutputSignal();
/* 180 */       comparatorBlockEntity.setOutputSignal(outputValue); }
/*     */ 
/*     */     
/* 183 */     if (oldValue != outputValue || state.getValue(MODE) == ComparatorMode.COMPARE) {
/* 184 */       boolean sourceOn = shouldTurnOn(level, pos, state);
/* 185 */       boolean isOn = ((Boolean)state.getValue(POWERED)).booleanValue();
/*     */       
/* 187 */       if (isOn && !sourceOn) {
/* 188 */         level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(false)), 2);
/* 189 */       } else if (!isOn && sourceOn) {
/* 190 */         level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       
/* 193 */       updateNeighborsInFront(level, pos, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 199 */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { refreshOutputState(level, pos, state); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/* 204 */     super.triggerEvent(state, level, pos, b0, b1);
/*     */     
/* 206 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 207 */     return (blockEntity != null && blockEntity.triggerEvent(b0, b1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 212 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new ComparatorBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, MODE, POWERED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ComparatorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */