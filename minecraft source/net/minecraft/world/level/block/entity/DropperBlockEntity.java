/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DropperBlockEntity extends DispenserBlockEntity {
/*  8 */   private static final Component DEFAULT_NAME = Component.translatable("container.dropper");
/*    */ 
/*    */   
/* 11 */   public DropperBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.DROPPER, worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\DropperBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */