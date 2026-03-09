/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ public class KelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
/* 17 */   public static final MapCodec<KelpPlantBlock> CODEC = simpleCodec(KelpPlantBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 21 */   public MapCodec<KelpPlantBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected KelpPlantBlock(BlockBehaviour.Properties properties) { super(properties, Direction.UP, Shapes.block(), true); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected GrowingPlantHeadBlock getHeadBlock() { return (GrowingPlantHeadBlock)Blocks.KELP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected FluidState getFluidState(BlockState state) { return Fluids.WATER.getSource(false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected boolean canAttachTo(BlockState state) { return getHeadBlock().canAttachTo(state); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\KelpPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */