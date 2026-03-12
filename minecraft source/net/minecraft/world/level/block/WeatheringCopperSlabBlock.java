/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperSlabBlock extends SlabBlock implements WeatheringCopper {
/* 11 */   public static final MapCodec<WeatheringCopperSlabBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/* 12 */         .fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), 
/* 13 */         propertiesCodec())
/* 14 */       .apply(i, WeatheringCopperSlabBlock::new));
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/* 18 */   public MapCodec<WeatheringCopperSlabBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WeatheringCopperSlabBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
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


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperSlabBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */