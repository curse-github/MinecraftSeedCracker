/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperStairBlock extends StairBlock implements WeatheringCopper {
/* 11 */   public static final MapCodec<WeatheringCopperStairBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/* 12 */         .fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), BlockState.CODEC
/* 13 */         .fieldOf("base_state").forGetter(()), 
/* 14 */         propertiesCodec())
/* 15 */       .apply(i, WeatheringCopperStairBlock::new));
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/* 19 */   public MapCodec<WeatheringCopperStairBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WeatheringCopperStairBlock(WeatheringCopper.WeatherState weatherState, BlockState baseState, BlockBehaviour.Properties properties) {
/* 25 */     super(baseState, properties);
/* 26 */     this.weatherState = weatherState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { changeOverTime(state, level, pos, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected boolean isRandomlyTicking(BlockState state) { return WeatheringCopper.getNext(state.getBlock()).isPresent(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public WeatheringCopper.WeatherState getAge() { return this.weatherState; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperStairBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */