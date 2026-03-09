/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class MinecartChest
/*    */   extends AbstractMinecartContainer {
/* 25 */   public MinecartChest(EntityType<? extends MinecartChest> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected Item getDropItem() { return Items.CHEST_MINECART; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public ItemStack getPickResult() { return new ItemStack(Items.CHEST_MINECART); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getContainerSize() { return 27; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public BlockState getDefaultDisplayBlockState() { return (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public int getDefaultDisplayOffset() { return 8; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return ChestMenu.threeRows(containerId, inventory, this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public void stopOpen(ContainerUser containerUser) { level().gameEvent(GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of(containerUser.getLivingEntity())); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 65 */     InteractionResult result = interactWithContainerVehicle(player);
/* 66 */     if (result.consumesAction()) { Level level = player.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 67 */         gameEvent(GameEvent.CONTAINER_OPEN, player);
/* 68 */         PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*    */        }
/* 70 */      return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartChest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */