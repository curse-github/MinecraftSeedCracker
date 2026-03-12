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
/*    */ import net.minecraft.world.inventory.CraftingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CraftingTableBlock extends Block {
/* 18 */   public static final MapCodec<CraftingTableBlock> CODEC = simpleCodec(CraftingTableBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<? extends CraftingTableBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 25 */   private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");
/*    */ 
/*    */   
/* 28 */   protected CraftingTableBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 33 */     if (!level.isClientSide()) {
/* 34 */       player.openMenu(state.getMenuProvider(level, pos));
/* 35 */       player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
/*    */     } 
/* 37 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) { return new SimpleMenuProvider((containerId, inventory, player) -> new CraftingMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CraftingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */