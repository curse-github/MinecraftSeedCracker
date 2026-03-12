/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultState;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class VaultBlock extends BaseEntityBlock {
/* 28 */   public static final MapCodec<VaultBlock> CODEC = simpleCodec(VaultBlock::new);
/* 29 */   public static final Property<VaultState> STATE = BlockStateProperties.VAULT_STATE;
/* 30 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/* 31 */   public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;
/*    */ 
/*    */ 
/*    */   
/* 35 */   public MapCodec<VaultBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/*    */   public VaultBlock(BlockBehaviour.Properties properties) {
/* 39 */     super(properties);
/* 40 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(STATE, VaultState.INACTIVE)).setValue(OMINOUS, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 45 */     if (itemStack.isEmpty() || state.getValue(STATE) != VaultState.ACTIVE) {
/* 46 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*    */     }
/*    */     
/* 49 */     if (level instanceof ServerLevel) { VaultBlockEntity vault; ServerLevel serverLevel = (ServerLevel)level;
/* 50 */       BlockEntity blockEntity = serverLevel.getBlockEntity(pos); if (blockEntity instanceof VaultBlockEntity) { vault = (VaultBlockEntity)blockEntity; }
/* 51 */       else { return InteractionResult.TRY_WITH_EMPTY_HAND; }
/*    */       
/* 53 */       VaultBlockEntity.Server.tryInsertKey(serverLevel, pos, state, vault.getConfig(), vault.getServerData(), vault.getSharedData(), player, itemStack); }
/*    */     
/* 55 */     return InteractionResult.SUCCESS_SERVER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new VaultBlockEntity(pos, state); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, STATE, OMINOUS }); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 70 */     ServerLevel serverLevel = (ServerLevel)level; return (level instanceof ServerLevel) ? 
/* 71 */       createTickerHelper(type, BlockEntityType.VAULT, (innerLevel, pos, state, entity) -> VaultBlockEntity.Server.tick(serverLevel, pos, state, entity.getConfig(), entity.getServerData(), entity.getSharedData())) : 
/* 72 */       createTickerHelper(type, BlockEntityType.VAULT, (innerLevel, pos, state, entity) -> VaultBlockEntity.Client.tick(innerLevel, pos, state, entity.getClientData(), entity.getSharedData()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   public BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\VaultBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */