/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SporeBlossomBlock extends Block {
/* 18 */   public static final MapCodec<SporeBlossomBlock> CODEC = simpleCodec(SporeBlossomBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<SporeBlossomBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(12.0D, 13.0D, 16.0D);
/*    */   
/*    */   private static final int ADD_PARTICLE_ATTEMPTS = 14;
/*    */   
/*    */   private static final int PARTICLE_XZ_RADIUS = 10;
/*    */   private static final int PARTICLE_Y_MAX = 10;
/*    */   
/* 32 */   public SporeBlossomBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return (Block.canSupportCenter(level, pos.above(), Direction.DOWN) && !level.isWaterAt(pos)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 42 */     if (directionToNeighbour == Direction.UP && !canSurvive(state, level, pos)) {
/* 43 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 45 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 50 */     int plantX = pos.getX();
/* 51 */     int plantY = pos.getY();
/* 52 */     int plantZ = pos.getZ();
/*    */     
/* 54 */     double xFalling = plantX + random.nextDouble();
/* 55 */     double yFalling = plantY + 0.7D;
/* 56 */     double zFalling = plantZ + random.nextDouble();
/*    */     
/* 58 */     level.addParticle(ParticleTypes.FALLING_SPORE_BLOSSOM, xFalling, yFalling, zFalling, 0.0D, 0.0D, 0.0D);
/*    */     
/* 60 */     BlockPos.MutableBlockPos ambientPos = new BlockPos.MutableBlockPos();
/* 61 */     for (int i = 0; i < 14; i++) {
/* 62 */       ambientPos.set(plantX + Mth.nextInt(random, -10, 10), plantY - random.nextInt(10), plantZ + Mth.nextInt(random, -10, 10));
/* 63 */       BlockState particlePosState = level.getBlockState(ambientPos);
/* 64 */       if (!particlePosState.isCollisionShapeFullBlock(level, ambientPos)) {
/* 65 */         level.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, ambientPos.getX() + random.nextDouble(), ambientPos.getY() + random.nextDouble(), ambientPos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SporeBlossomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */