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
/*    */ import net.minecraft.world.inventory.SmithingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SmithingTableBlock extends CraftingTableBlock {
/* 18 */   public static final MapCodec<SmithingTableBlock> CODEC = simpleCodec(SmithingTableBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<SmithingTableBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected SmithingTableBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */   
/* 29 */   private static final Component CONTAINER_TITLE = Component.translatable("container.upgrade");
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) { return new SimpleMenuProvider((containerId, inventory, player) -> new SmithingMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 38 */     if (!level.isClientSide()) {
/* 39 */       player.openMenu(state.getMenuProvider(level, pos));
/* 40 */       player.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
/*    */     } 
/* 42 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SmithingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */