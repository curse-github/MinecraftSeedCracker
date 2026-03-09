/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FurnaceBlock extends AbstractFurnaceBlock {
/* 22 */   public static final MapCodec<FurnaceBlock> CODEC = simpleCodec(FurnaceBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MapCodec<FurnaceBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected FurnaceBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new FurnaceBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createFurnaceTicker(level, type, BlockEntityType.FURNACE); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void openContainer(Level level, BlockPos pos, Player player) {
/* 45 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 46 */     if (blockEntity instanceof FurnaceBlockEntity) {
/* 47 */       player.openMenu((MenuProvider)blockEntity);
/* 48 */       player.awardStat(Stats.INTERACT_WITH_FURNACE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 54 */     if (!((Boolean)state.getValue(LIT)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 58 */     double x = pos.getX() + 0.5D;
/* 59 */     double y = pos.getY();
/* 60 */     double z = pos.getZ() + 0.5D;
/*    */     
/* 62 */     if (random.nextDouble() < 0.1D) {
/* 63 */       level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*    */     }
/*    */     
/* 66 */     Direction direction = (Direction)state.getValue(FACING);
/* 67 */     Direction.Axis axis = direction.getAxis();
/*    */     
/* 69 */     double r = 0.52D;
/* 70 */     double ss = random.nextDouble() * 0.6D - 0.3D;
/*    */     
/* 72 */     double dx = (axis == Direction.Axis.X) ? (direction.getStepX() * 0.52D) : ss;
/* 73 */     double dy = random.nextDouble() * 6.0D / 16.0D;
/* 74 */     double dz = (axis == Direction.Axis.Z) ? (direction.getStepZ() * 0.52D) : ss;
/*    */     
/* 76 */     level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
/* 77 */     level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FurnaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */