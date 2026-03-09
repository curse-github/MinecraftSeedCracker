/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WetSpongeBlock extends Block {
/* 16 */   public static final MapCodec<WetSpongeBlock> CODEC = simpleCodec(WetSpongeBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 20 */   public MapCodec<WetSpongeBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected WetSpongeBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 29 */     if (((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)).booleanValue()) {
/* 30 */       level.setBlock(pos, Blocks.SPONGE.defaultBlockState(), 3);
/* 31 */       level.levelEvent(2009, pos, 0);
/* 32 */       level.playSound(null, pos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 38 */     Direction direction = Direction.getRandom(random);
/* 39 */     if (direction == Direction.UP) {
/*    */       return;
/*    */     }
/* 42 */     BlockPos relativePos = pos.relative(direction);
/* 43 */     BlockState blockState = level.getBlockState(relativePos);
/* 44 */     if (state.canOcclude() && blockState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     double xx = pos.getX();
/* 49 */     double yy = pos.getY();
/* 50 */     double zz = pos.getZ();
/*    */ 
/*    */ 
/*    */     
/* 54 */     if (direction == Direction.DOWN) {
/* 55 */       yy -= 0.05D;
/* 56 */       xx += random.nextDouble();
/* 57 */       zz += random.nextDouble();
/*    */     } else {
/* 59 */       yy += random.nextDouble() * 0.8D;
/* 60 */       if (direction.getAxis() == Direction.Axis.X) {
/* 61 */         zz += random.nextDouble();
/* 62 */         if (direction == Direction.EAST) {
/* 63 */           xx += 1.1D;
/*    */         } else {
/* 65 */           xx += 0.05D;
/*    */         } 
/*    */       } else {
/* 68 */         xx += random.nextDouble();
/* 69 */         if (direction == Direction.SOUTH) {
/* 70 */           zz += 1.1D;
/*    */         } else {
/* 72 */           zz += 0.05D;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 77 */     level.addParticle(ParticleTypes.DRIPPING_WATER, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WetSpongeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */