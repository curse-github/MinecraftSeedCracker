/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class StructureBlock extends BaseEntityBlock implements GameMasterBlock {
/*  23 */   public static final MapCodec<StructureBlock> CODEC = simpleCodec(StructureBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  27 */   public MapCodec<StructureBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  30 */   public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;
/*     */   
/*     */   protected StructureBlock(BlockBehaviour.Properties properties) {
/*  33 */     super(properties);
/*     */     
/*  35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(MODE, StructureMode.LOAD));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new StructureBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  45 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  46 */     if (blockEntity instanceof StructureBlockEntity) {
/*  47 */       return ((StructureBlockEntity)blockEntity).usedBy(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*     */     }
/*     */     
/*  50 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*  55 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*  58 */     if (by != null) {
/*  59 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  60 */       if (blockEntity instanceof StructureBlockEntity) {
/*  61 */         ((StructureBlockEntity)blockEntity).createdBy(by);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { MODE }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  73 */     if (!(level instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  78 */     if (!(blockEntity instanceof StructureBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     StructureBlockEntity structureBlock = (StructureBlockEntity)blockEntity;
/*     */     
/*  84 */     boolean shouldTrigger = level.hasNeighborSignal(pos);
/*  85 */     boolean isPowered = structureBlock.isPowered();
/*     */     
/*  87 */     if (shouldTrigger && !isPowered) {
/*  88 */       structureBlock.setPowered(true);
/*  89 */       trigger((ServerLevel)level, structureBlock);
/*  90 */     } else if (!shouldTrigger && isPowered) {
/*  91 */       structureBlock.setPowered(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void trigger(ServerLevel level, StructureBlockEntity structureBlock) {
/*  96 */     switch (structureBlock.getMode()) {
/*     */       case SAVE:
/*  98 */         structureBlock.saveStructure(false);
/*     */         break;
/*     */       case LOAD:
/* 101 */         structureBlock.placeStructure(level);
/*     */         break;
/*     */       case CORNER:
/* 104 */         structureBlock.unloadStructure();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StructureBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */