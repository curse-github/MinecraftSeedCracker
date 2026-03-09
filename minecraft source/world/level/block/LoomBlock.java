/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.LoomMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class LoomBlock extends HorizontalDirectionalBlock {
/* 20 */   public static final MapCodec<LoomBlock> CODEC = simpleCodec(LoomBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<LoomBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 27 */   private static final Component CONTAINER_TITLE = Component.translatable("container.loom");
/*    */ 
/*    */   
/* 30 */   protected LoomBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 35 */     if (!level.isClientSide()) {
/* 36 */       player.openMenu(state.getMenuProvider(level, pos));
/* 37 */       player.awardStat(Stats.INTERACT_WITH_LOOM);
/*    */     } 
/* 39 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) { return new SimpleMenuProvider((containerId, inventory, player) -> new LoomMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LoomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */