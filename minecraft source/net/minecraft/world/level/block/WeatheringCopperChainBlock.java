/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperChainBlock extends ChainBlock implements WeatheringCopper {
/* 12 */   public static final MapCodec<WeatheringCopperChainBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/* 13 */         .fieldOf("weathering_state").forGetter(WeatheringCopperChainBlock::getAge), propertiesCodec())
/* 14 */       .apply(i, WeatheringCopperChainBlock::new));
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/* 18 */   public MapCodec<WeatheringCopperChainBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperChainBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/* 25 */     this.weatherState = weatherState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { changeOverTime(state, level, pos, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected boolean isRandomlyTicking(BlockState state) { return WeatheringCopper.getNext(state.getBlock()).isPresent(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public WeatheringCopper.WeatherState getAge() { return this.weatherState; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperChainBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */