/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperFullBlock extends Block implements WeatheringCopper {
/* 11 */   public static final MapCodec<WeatheringCopperFullBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/* 12 */         .fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), 
/* 13 */         propertiesCodec())
/* 14 */       .apply(i, WeatheringCopperFullBlock::new));
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/* 18 */   public MapCodec<WeatheringCopperFullBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WeatheringCopperFullBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
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


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperFullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */