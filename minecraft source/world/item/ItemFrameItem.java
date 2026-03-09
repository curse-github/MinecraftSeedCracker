/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ItemFrameItem
/*    */   extends HangingEntityItem {
/* 11 */   public ItemFrameItem(EntityType<? extends HangingEntity> entityType, Item.Properties properties) { super(entityType, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) { return (!player.level().isOutsideBuildHeight(blockPos) && player.mayUseItemAt(blockPos, direction, itemStack)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemFrameItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */