/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SignApplicator;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class SignBlock
/*     */   extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  44 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  46 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*     */   
/*     */   private final WoodType type;
/*     */   
/*     */   protected SignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  51 */     super(properties);
/*  52 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  60 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  61 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  64 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public boolean isPossibleToRespawnInThis(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new SignBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*     */     SignBlockEntity sign;
/*  84 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { sign = (SignBlockEntity)blockEntity; }
/*  85 */     else { return InteractionResult.PASS; }
/*     */ 
/*     */     
/*  88 */     ServerLevel serverLevel = itemStack.getItem(); SignApplicator applicator = (SignApplicator)serverLevel, signApplicator = (serverLevel instanceof SignApplicator) ? applicator : null;
/*  89 */     boolean hasApplicatorToUse = (signApplicator != null && player.mayBuild());
/*     */     
/*  91 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  92 */     else { return (hasApplicatorToUse || sign.isWaxed()) ? InteractionResult.SUCCESS : InteractionResult.CONSUME; }
/*     */ 
/*     */     
/*  95 */     if (!hasApplicatorToUse || sign.isWaxed() || otherPlayerIsEditingSign(player, sign)) {
/*  96 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  99 */     boolean isFrontText = sign.isFacingFrontText(player);
/* 100 */     if (signApplicator.canApplyToSign(sign.getText(isFrontText), player) && signApplicator.tryApplyToSign(serverLevel, sign, isFrontText, player)) {
/* 101 */       sign.executeClickCommandsIfPresent(serverLevel, player, pos, isFrontText);
/* 102 */       player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
/* 103 */       serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, sign.getBlockPos(), GameEvent.Context.of(player, sign.getBlockState()));
/* 104 */       itemStack.consume(1, player);
/* 105 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 108 */     return InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     SignBlockEntity sign;
/* 113 */     ServerLevel serverLevel = level.getBlockEntity(pos); if (serverLevel instanceof SignBlockEntity) { sign = (SignBlockEntity)serverLevel; }
/* 114 */     else { return InteractionResult.PASS; }
/*     */ 
/*     */     
/* 117 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 118 */     else { Util.pauseInIde(new IllegalStateException("Expected to only call this on server"));
/* 119 */       return InteractionResult.CONSUME; }
/*     */ 
/*     */     
/* 122 */     boolean isFrontText = sign.isFacingFrontText(player);
/*     */     
/* 124 */     boolean executedClickCommand = sign.executeClickCommandsIfPresent(serverLevel, player, pos, isFrontText);
/*     */     
/* 126 */     if (sign.isWaxed()) {
/* 127 */       serverLevel.playSound(null, sign.getBlockPos(), sign.getSignInteractionFailedSoundEvent(), SoundSource.BLOCKS);
/* 128 */       return InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 131 */     if (executedClickCommand) {
/* 132 */       return InteractionResult.SUCCESS_SERVER;
/*     */     }
/*     */     
/* 135 */     if (!otherPlayerIsEditingSign(player, sign) && player.mayBuild() && hasEditableText(player, sign, isFrontText)) {
/* 136 */       openTextEdit(player, sign, isFrontText);
/* 137 */       return InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 140 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private boolean hasEditableText(Player player, SignBlockEntity sign, boolean isFrontText) {
/* 144 */     SignText text = sign.getText(isFrontText);
/* 145 */     return Arrays.stream(text.getMessages(player.isTextFilteringEnabled()))
/* 146 */       .allMatch(message -> (message.equals(CommonComponents.EMPTY) || message.getContents() instanceof net.minecraft.network.chat.contents.PlainTextContents));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public Vec3 getSignHitboxCenterPosition(BlockState state) { return new Vec3(0.5D, 0.5D, 0.5D); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 157 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 158 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 160 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/* 164 */   public WoodType type() { return this.type; }
/*     */ 
/*     */   
/*     */   public static WoodType getWoodType(Block block) {
/*     */     WoodType type;
/* 169 */     if (block instanceof SignBlock) {
/* 170 */       type = ((SignBlock)block).type();
/*     */     } else {
/* 172 */       type = WoodType.OAK;
/*     */     } 
/* 174 */     return type;
/*     */   }
/*     */   
/*     */   public void openTextEdit(Player player, SignBlockEntity sign, boolean isFrontText) {
/* 178 */     sign.setAllowedPlayerEditor(player.getUUID());
/* 179 */     player.openTextEdit(sign, isFrontText);
/*     */   }
/*     */   
/*     */   private boolean otherPlayerIsEditingSign(Player player, SignBlockEntity sign) {
/* 183 */     UUID playerWhoMayEdit = sign.getPlayerWhoMayEdit();
/* 184 */     return (playerWhoMayEdit != null && !playerWhoMayEdit.equals(player.getUUID()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createTickerHelper(type, BlockEntityType.SIGN, SignBlockEntity::tick); }
/*     */   
/*     */   protected abstract MapCodec<? extends SignBlock> codec();
/*     */   
/*     */   public abstract float getYRotationDegrees(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */