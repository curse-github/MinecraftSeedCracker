/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DragonEggBlock extends FallingBlock {
/* 20 */   public static final MapCodec<DragonEggBlock> CODEC = simpleCodec(DragonEggBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<DragonEggBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 27 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */ 
/*    */   
/* 30 */   public DragonEggBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 40 */     teleport(state, level, pos);
/* 41 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) { teleport(state, level, pos); }
/*    */ 
/*    */   
/*    */   private void teleport(BlockState state, Level level, BlockPos pos) {
/* 50 */     WorldBorder worldBorder = level.getWorldBorder();
/* 51 */     for (int i = 0; i < 1000; i++) {
/* 52 */       BlockPos testPos = pos.offset(level.random
/* 53 */           .nextInt(16) - level.random.nextInt(16), level.random
/* 54 */           .nextInt(8) - level.random.nextInt(8), level.random
/* 55 */           .nextInt(16) - level.random.nextInt(16));
/*    */       
/* 57 */       if (level.getBlockState(testPos).isAir() && worldBorder.isWithinBounds(testPos) && !level.isOutsideBuildHeight(testPos)) {
/* 58 */         if (level.isClientSide()) {
/* 59 */           for (int j = 0; j < 128; j++) {
/* 60 */             double d = level.random.nextDouble();
/* 61 */             float xa = (level.random.nextFloat() - 0.5F) * 0.2F;
/* 62 */             float ya = (level.random.nextFloat() - 0.5F) * 0.2F;
/* 63 */             float za = (level.random.nextFloat() - 0.5F) * 0.2F;
/*    */             
/* 65 */             double x = Mth.lerp(d, testPos.getX(), pos.getX()) + level.random.nextDouble() - 0.5D + 0.5D;
/* 66 */             double y = Mth.lerp(d, testPos.getY(), pos.getY()) + level.random.nextDouble() - 0.5D;
/* 67 */             double z = Mth.lerp(d, testPos.getZ(), pos.getZ()) + level.random.nextDouble() - 0.5D + 0.5D;
/* 68 */             level.addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*    */           } 
/*    */         } else {
/* 71 */           level.setBlock(testPos, state, 2);
/* 72 */           level.removeBlock(pos, false);
/*    */         } 
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 81 */   protected int getDelayAfterPlace() { return 5; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) { return -16777216; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DragonEggBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */