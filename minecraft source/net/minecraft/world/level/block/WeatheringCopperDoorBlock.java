/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ 
/*    */ public class WeatheringCopperDoorBlock extends DoorBlock implements WeatheringCopper {
/* 13 */   public static final MapCodec<WeatheringCopperDoorBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockSetType.CODEC
/* 14 */         .fieldOf("block_set_type").forGetter(DoorBlock::type), WeatheringCopper.WeatherState.CODEC
/* 15 */         .fieldOf("weathering_state").forGetter(WeatheringCopperDoorBlock::getAge), 
/* 16 */         propertiesCodec())
/* 17 */       .apply(i, WeatheringCopperDoorBlock::new));
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/* 21 */   public MapCodec<WeatheringCopperDoorBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperDoorBlock(BlockSetType type, WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 27 */     super(type, properties);
/* 28 */     this.weatherState = weatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 33 */     if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
/* 34 */       changeOverTime(state, level, pos, random);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected boolean isRandomlyTicking(BlockState state) { return WeatheringCopper.getNext(state.getBlock()).isPresent(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public WeatheringCopper.WeatherState getAge() { return this.weatherState; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */