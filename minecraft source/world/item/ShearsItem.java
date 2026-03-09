/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.Tool;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class ShearsItem
/*    */   extends Item {
/* 30 */   public ShearsItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Tool createToolProperties() {
/* 35 */     registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
/*    */     
/* 37 */     return new Tool(
/* 38 */         List.of(
/* 39 */           Tool.Rule.minesAndDrops(HolderSet.direct(new Holder[] { Blocks.COBWEB.builtInRegistryHolder() }, ), 15.0F), 
/* 40 */           Tool.Rule.overrideSpeed(registrationLookup.getOrThrow(BlockTags.LEAVES), 15.0F), 
/* 41 */           Tool.Rule.overrideSpeed(registrationLookup.getOrThrow(BlockTags.WOOL), 5.0F), 
/* 42 */           Tool.Rule.overrideSpeed(HolderSet.direct(new Holder[] { Blocks.VINE.builtInRegistryHolder(), Blocks.GLOW_LICHEN.builtInRegistryHolder() }, ), 2.0F)), 1.0F, 1, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
/* 52 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 53 */     if (tool == null) {
/* 54 */       return false;
/*    */     }
/*    */     
/* 57 */     if (!level.isClientSide() && !state.is(BlockTags.FIRE) && tool.damagePerBlock() > 0) {
/* 58 */       itemStack.hurtAndBreak(tool.damagePerBlock(), miner, EquipmentSlot.MAINHAND);
/*    */     }
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 65 */     Level level = context.getLevel();
/* 66 */     BlockPos pos = context.getClickedPos();
/* 67 */     BlockState state = level.getBlockState(pos);
/* 68 */     Block block = state.getBlock();
/* 69 */     if (block instanceof GrowingPlantHeadBlock) { GrowingPlantHeadBlock plantBlock = (GrowingPlantHeadBlock)block;
/* 70 */       if (!plantBlock.isMaxAge(state)) {
/* 71 */         Player player = context.getPlayer();
/* 72 */         ItemStack itemInHand = context.getItemInHand();
/* 73 */         if (player instanceof ServerPlayer) {
/* 74 */           CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemInHand);
/*    */         }
/* 76 */         level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 77 */         BlockState newState = plantBlock.getMaxAgeState(state);
/* 78 */         level.setBlockAndUpdate(pos, newState);
/* 79 */         level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(context.getPlayer(), newState));
/* 80 */         if (player != null) {
/* 81 */           itemInHand.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
/*    */         }
/*    */         
/* 84 */         return InteractionResult.SUCCESS;
/*    */       }  }
/*    */     
/* 87 */     return super.useOn(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ShearsItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */