/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ 
/*    */ public class WeatheringCopperTrapDoorBlock extends TrapDoorBlock implements WeatheringCopper {
/* 12 */   public static final MapCodec<WeatheringCopperTrapDoorBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockSetType.CODEC
/* 13 */         .fieldOf("block_set_type").forGetter(TrapDoorBlock::getType), WeatheringCopper.WeatherState.CODEC
/* 14 */         .fieldOf("weathering_state").forGetter(WeatheringCopperTrapDoorBlock::getAge), 
/* 15 */         propertiesCodec())
/* 16 */       .apply(i, WeatheringCopperTrapDoorBlock::new));
/*    */   
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/* 20 */   public MapCodec<WeatheringCopperTrapDoorBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperTrapDoorBlock(BlockSetType type, WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 26 */     super(type, properties);
/* 27 */     this.weatherState = weatherState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { changeOverTime(state, level, pos, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected boolean isRandomlyTicking(BlockState state) { return WeatheringCopper.getNext(state.getBlock()).isPresent(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public WeatheringCopper.WeatherState getAge() { return this.weatherState; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperTrapDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */