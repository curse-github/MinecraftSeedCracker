/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedBlockEntity extends BlockEntity {
/*    */   private final DyeColor color;
/*    */   
/* 13 */   public BedBlockEntity(BlockPos worldPosition, BlockState blockState) { this(worldPosition, blockState, ((BedBlock)blockState.getBlock()).getColor()); }
/*    */ 
/*    */   
/*    */   public BedBlockEntity(BlockPos worldPosition, BlockState blockState, DyeColor color) {
/* 17 */     super(BlockEntityType.BED, worldPosition, blockState);
/* 18 */     this.color = color;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public DyeColor getColor() { return this.color; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BedBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */