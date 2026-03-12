/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CopperGolemStatueBlockEntity
/*    */   extends BlockEntity
/*    */ {
/* 21 */   public CopperGolemStatueBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.COPPER_GOLEM_STATUE, worldPosition, blockState); }
/*    */ 
/*    */   
/*    */   public void createStatue(CopperGolem copperGolem) {
/* 25 */     setComponents(DataComponentMap.builder()
/* 26 */         .addAll(components())
/* 27 */         .set(DataComponents.CUSTOM_NAME, copperGolem.getCustomName())
/* 28 */         .build());
/* 29 */     setChanged();
/*    */   }
/*    */   
/*    */   public CopperGolem removeStatue(BlockState state) {
/* 33 */     CopperGolem copperGolem = (CopperGolem)EntityType.COPPER_GOLEM.create(this.level, EntitySpawnReason.TRIGGERED);
/* 34 */     if (copperGolem != null) {
/* 35 */       copperGolem.setCustomName((Component)components().get(DataComponents.CUSTOM_NAME));
/* 36 */       return initCopperGolem(state, copperGolem);
/*    */     } 
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   private CopperGolem initCopperGolem(BlockState state, CopperGolem copperGolem) {
/* 42 */     BlockPos blockPos = getBlockPos();
/* 43 */     copperGolem.snapTo((blockPos.getCenter()).x, blockPos.getY(), (blockPos.getCenter()).z, ((Direction)state.getValue(CopperGolemStatueBlock.FACING)).toYRot(), 0.0F);
/* 44 */     copperGolem.yHeadRot = copperGolem.getYRot();
/* 45 */     copperGolem.yBodyRot = copperGolem.getYRot();
/* 46 */     copperGolem.playSpawnSound();
/* 47 */     return copperGolem;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*    */ 
/*    */   
/*    */   public ItemStack getItem(ItemStack itemStack, CopperGolemStatueBlock.Pose pose) {
/* 56 */     itemStack.applyComponents(collectComponents());
/* 57 */     itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CopperGolemStatueBlock.POSE, pose));
/* 58 */     return itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\CopperGolemStatueBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */