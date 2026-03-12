/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CommandBlock extends BaseEntityBlock implements GameMasterBlock {
/*  35 */   public static final MapCodec<CommandBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/*  36 */         .fieldOf("automatic").forGetter(()), 
/*  37 */         propertiesCodec())
/*  38 */       .apply(i, CommandBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  42 */   public MapCodec<CommandBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  46 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*  47 */   public static final BooleanProperty CONDITIONAL = BlockStateProperties.CONDITIONAL;
/*     */   private final boolean automatic;
/*     */   
/*     */   public CommandBlock(boolean automatic, BlockBehaviour.Properties properties) {
/*  51 */     super(properties);
/*  52 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(CONDITIONAL, Boolean.valueOf(false)));
/*  53 */     this.automatic = automatic;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  58 */     CommandBlockEntity blockEntity = new CommandBlockEntity(worldPosition, blockState);
/*  59 */     blockEntity.setAutomatic(this.automatic);
/*  60 */     return blockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  65 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  70 */     if (blockEntity instanceof CommandBlockEntity) { CommandBlockEntity commandBlock = (CommandBlockEntity)blockEntity;
/*  71 */       setPoweredAndUpdate(level, pos, commandBlock, level.hasNeighborSignal(pos)); }
/*     */   
/*     */   }
/*     */   
/*     */   private void setPoweredAndUpdate(Level level, BlockPos pos, CommandBlockEntity commandBlock, boolean isPowered) {
/*  76 */     boolean wasPowered = commandBlock.isPowered();
/*  77 */     if (isPowered == wasPowered) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     commandBlock.setPowered(isPowered);
/*     */     
/*  83 */     if (isPowered) {
/*  84 */       if (commandBlock.isAutomatic() || commandBlock.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
/*     */         return;
/*     */       }
/*     */       
/*  88 */       commandBlock.markConditionMet();
/*     */       
/*  90 */       level.scheduleTick(pos, this, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  96 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  97 */     if (blockEntity instanceof CommandBlockEntity) { CommandBlockEntity commandBlock = (CommandBlockEntity)blockEntity;
/*  98 */       BaseCommandBlock baseCommandBlock = commandBlock.getCommandBlock();
/*  99 */       boolean commandSet = !StringUtil.isNullOrEmpty(baseCommandBlock.getCommand());
/* 100 */       CommandBlockEntity.Mode mode = commandBlock.getMode();
/*     */       
/* 102 */       boolean wasConditionMet = commandBlock.wasConditionMet();
/* 103 */       if (mode == CommandBlockEntity.Mode.AUTO) {
/* 104 */         commandBlock.markConditionMet();
/*     */         
/* 106 */         if (wasConditionMet) {
/* 107 */           execute(state, level, pos, baseCommandBlock, commandSet);
/* 108 */         } else if (commandBlock.isConditional()) {
/* 109 */           baseCommandBlock.setSuccessCount(0);
/*     */         } 
/*     */         
/* 112 */         if (commandBlock.isPowered() || commandBlock.isAutomatic()) {
/* 113 */           level.scheduleTick(pos, this, 1);
/*     */         }
/* 115 */       } else if (mode == CommandBlockEntity.Mode.REDSTONE) {
/* 116 */         if (wasConditionMet) {
/* 117 */           execute(state, level, pos, baseCommandBlock, commandSet);
/* 118 */         } else if (commandBlock.isConditional()) {
/* 119 */           baseCommandBlock.setSuccessCount(0);
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       level.updateNeighbourForOutputSignal(pos, this); }
/*     */   
/*     */   }
/*     */   
/*     */   private void execute(BlockState state, ServerLevel level, BlockPos pos, BaseCommandBlock baseCommandBlock, boolean commandSet) {
/* 128 */     if (commandSet) {
/* 129 */       baseCommandBlock.performCommand(level);
/*     */     } else {
/* 131 */       baseCommandBlock.setSuccessCount(0);
/*     */     } 
/*     */     
/* 134 */     executeChain(level, pos, (Direction)state.getValue(FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 139 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 140 */     if (blockEntity instanceof CommandBlockEntity && player.canUseGameMasterBlocks()) {
/* 141 */       player.openCommandBlock((CommandBlockEntity)blockEntity);
/* 142 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 145 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 150 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 155 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 156 */     if (blockEntity instanceof CommandBlockEntity) {
/* 157 */       return ((CommandBlockEntity)blockEntity).getCommandBlock().getSuccessCount();
/*     */     }
/* 159 */     return 0;
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*     */     CommandBlockEntity commandBlockEntity;
/* 164 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 165 */     if (blockEntity instanceof CommandBlockEntity) { commandBlockEntity = (CommandBlockEntity)blockEntity; }
/*     */     else
/*     */     { return; }
/*     */     
/* 169 */     BaseCommandBlock commandBlock = commandBlockEntity.getCommandBlock();
/*     */     
/* 171 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 172 */       if (!itemStack.has(DataComponents.BLOCK_ENTITY_DATA)) {
/* 173 */         commandBlock.setTrackOutput(((Boolean)serverLevel.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue());
/* 174 */         commandBlockEntity.setAutomatic(this.automatic);
/*     */       } 
/*     */       
/* 177 */       boolean hasNeighborSignal = level.hasNeighborSignal(pos);
/* 178 */       setPoweredAndUpdate(level, pos, commandBlockEntity, hasNeighborSignal); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 184 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, CONDITIONAL }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()); }
/*     */ 
/*     */   
/*     */   private static void executeChain(ServerLevel level, BlockPos blockPos, Direction direction) {
/* 203 */     BlockPos.MutableBlockPos pos = blockPos.mutable();
/*     */     
/* 205 */     GameRules gameRules = level.getGameRules();
/* 206 */     int maxIterations = ((Integer)gameRules.get(GameRules.MAX_COMMAND_SEQUENCE_LENGTH)).intValue();
/* 207 */     while (maxIterations-- > 0) {
/* 208 */       pos.move(direction);
/*     */       
/* 210 */       BlockState state = level.getBlockState(pos);
/* 211 */       Block block = state.getBlock();
/* 212 */       if (!state.is(Blocks.CHAIN_COMMAND_BLOCK)) {
/*     */         break;
/*     */       }
/*     */       
/* 216 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 217 */       if (!(blockEntity instanceof CommandBlockEntity)) {
/*     */         break;
/*     */       }
/*     */       
/* 221 */       CommandBlockEntity commandBlock = (CommandBlockEntity)blockEntity;
/* 222 */       if (commandBlock.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
/*     */         break;
/*     */       }
/*     */       
/* 226 */       if (commandBlock.isPowered() || commandBlock.isAutomatic()) {
/* 227 */         BaseCommandBlock baseCommandBlock = commandBlock.getCommandBlock();
/* 228 */         if (commandBlock.markConditionMet()) {
/* 229 */           if (baseCommandBlock.performCommand(level)) {
/* 230 */             level.updateNeighbourForOutputSignal(pos, block);
/*     */           } else {
/*     */             break;
/*     */           } 
/* 234 */         } else if (commandBlock.isConditional()) {
/* 235 */           baseCommandBlock.setSuccessCount(0);
/*     */         } 
/*     */       } 
/*     */       
/* 239 */       direction = (Direction)state.getValue(FACING);
/*     */     } 
/* 241 */     if (maxIterations <= 0) {
/* 242 */       int limit = Math.max(((Integer)gameRules.get(GameRules.MAX_COMMAND_SEQUENCE_LENGTH)).intValue(), 0);
/* 243 */       LOGGER.warn("Command Block chain tried to execute more than {} steps!", Integer.valueOf(limit));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CommandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */