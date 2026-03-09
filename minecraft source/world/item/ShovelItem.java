/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ 
/*    */ public class ShovelItem
/*    */   extends Item
/*    */ {
/* 24 */   protected static final Map<Block, BlockState> FLATTENABLES = Maps.newHashMap((new ImmutableMap.Builder())
/* 25 */       .put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState())
/* 26 */       .put(Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState())
/* 27 */       .put(Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState())
/* 28 */       .put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState())
/* 29 */       .put(Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState())
/* 30 */       .put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState())
/* 31 */       .build());
/*    */ 
/*    */   
/* 34 */   public ShovelItem(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline, Item.Properties properties) { super(properties.shovel(material, attackDamageBaseline, attackSpeedBaseline)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 39 */     Level level = context.getLevel();
/* 40 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 42 */     BlockState blockState = level.getBlockState(pos);
/* 43 */     if (context.getClickedFace() != Direction.DOWN) {
/* 44 */       Player player = context.getPlayer();
/* 45 */       BlockState newState = (BlockState)FLATTENABLES.get(blockState.getBlock());
/* 46 */       BlockState updatedState = null;
/*    */       
/* 48 */       if (newState != null && level.getBlockState(pos.above()).isAir()) {
/* 49 */         level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 50 */         updatedState = newState;
/* 51 */       } else if (blockState.getBlock() instanceof CampfireBlock && ((Boolean)blockState.getValue(CampfireBlock.LIT)).booleanValue()) {
/* 52 */         if (!level.isClientSide()) {
/* 53 */           level.levelEvent(null, 1009, pos, 0);
/*    */         }
/* 55 */         CampfireBlock.dowse(context.getPlayer(), level, pos, blockState);
/* 56 */         updatedState = (BlockState)blockState.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
/*    */       } 
/*    */       
/* 59 */       if (updatedState != null) {
/* 60 */         if (!level.isClientSide()) {
/* 61 */           level.setBlock(pos, updatedState, 11);
/* 62 */           level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, updatedState));
/* 63 */           if (player != null) {
/* 64 */             context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
/*    */           }
/*    */         } 
/* 67 */         return InteractionResult.SUCCESS;
/*    */       } 
/* 69 */       return InteractionResult.PASS;
/*    */     } 
/*    */     
/* 72 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ShovelItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */