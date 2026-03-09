/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MangroveLeavesBlock extends TintedParticleLeavesBlock implements BonemealableBlock {
/* 14 */   public static final MapCodec<MangroveLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), 
/* 16 */         propertiesCodec())
/* 17 */       .apply(i, MangroveLeavesBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 21 */   public MapCodec<MangroveLeavesBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MangroveLeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) { super(leafParticleChance, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getBlockState(pos.below()).isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { level.setBlock(pos.below(), MangrovePropaguleBlock.createNewHangingPropagule(), 2); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public BlockPos getParticlePos(BlockPos blockPos) { return blockPos.below(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MangroveLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */