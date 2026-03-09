/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class EmptyFluid
/*    */   extends Fluid
/*    */ {
/* 18 */   public Item getBucket() { return Items.AIR; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Vec3 getFlow(BlockGetter level, BlockPos pos, FluidState fluidState) { return Vec3.ZERO; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public int getTickDelay(LevelReader level) { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected boolean isEmpty() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected float getExplosionResistance() { return 0.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public float getHeight(FluidState fluidState, BlockGetter level, BlockPos pos) { return 0.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public float getOwnHeight(FluidState fluidState) { return 0.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   protected BlockState createLegacyBlock(FluidState fluidState) { return Blocks.AIR.defaultBlockState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public boolean isSource(FluidState fluidState) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public int getAmount(FluidState fluidState) { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 73 */   public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) { return Shapes.empty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\material\EmptyFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */