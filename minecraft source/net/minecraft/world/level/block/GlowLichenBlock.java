/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GlowLichenBlock extends MultifaceSpreadeableBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<GlowLichenBlock> CODEC = simpleCodec(GlowLichenBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<GlowLichenBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 22 */   private final MultifaceSpreader spreader = new MultifaceSpreader(this);
/*    */ 
/*    */   
/* 25 */   public GlowLichenBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static ToIntFunction<BlockState> emission(int lightEmission) { return state -> MultifaceBlock.hasAnyFace(state) ? lightEmission : 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return Direction.stream().anyMatch(face -> this.spreader.canSpreadInAnyDirection(state, level, pos, face.getOpposite())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { this.spreader.spreadFromRandomFaceTowardRandomDirection(state, level, pos, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected boolean propagatesSkylightDown(BlockState state) { return state.getFluidState().isEmpty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public MultifaceSpreader getSpreader() { return this.spreader; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GlowLichenBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */