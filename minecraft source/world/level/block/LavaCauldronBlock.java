/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.cauldron.CauldronInteraction;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.entity.InsideBlockEffectType;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class LavaCauldronBlock extends AbstractCauldronBlock {
/* 18 */   public static final MapCodec<LavaCauldronBlock> CODEC = simpleCodec(LavaCauldronBlock::new);
/* 19 */   private static final VoxelShape SHAPE_INSIDE = Block.column(12.0D, 4.0D, 15.0D);
/* 20 */   private static final VoxelShape FILLED_SHAPE = Shapes.or(AbstractCauldronBlock.SHAPE, SHAPE_INSIDE);
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<LavaCauldronBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public LavaCauldronBlock(BlockBehaviour.Properties properties) { super(properties, CauldronInteraction.LAVA); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected double getContentHeight(BlockState state) { return 0.9375D; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public boolean isFull(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) { return FILLED_SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 48 */     effectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 49 */     effectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
/* 50 */     effectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return 3; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LavaCauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */