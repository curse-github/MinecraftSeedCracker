/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperChestBlock extends CopperChestBlock implements WeatheringCopper {
/* 15 */   public static final MapCodec<WeatheringCopperChestBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/* 16 */         .fieldOf("weathering_state").forGetter(CopperChestBlock::getState), BuiltInRegistries.SOUND_EVENT
/* 17 */         .byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), BuiltInRegistries.SOUND_EVENT
/* 18 */         .byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), 
/* 19 */         propertiesCodec())
/* 20 */       .apply(i, WeatheringCopperChestBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<WeatheringCopperChestBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public WeatheringCopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) { super(weatherState, openSound, closeSound, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected boolean isRandomlyTicking(BlockState state) { return WeatheringCopper.getNext(state.getBlock()).isPresent(); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 38 */     if (!((ChestType)state.getValue(ChestBlock.TYPE)).equals(ChestType.RIGHT)) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ChestBlockEntity) { ChestBlockEntity chestBlockEntity = (ChestBlockEntity)blockEntity; if (chestBlockEntity.getEntitiesWithContainerOpen().isEmpty()) {
/* 39 */           changeOverTime(state, level, pos, random);
/*    */         } }
/*    */        }
/*    */   
/*    */   }
/*    */   
/* 45 */   public WeatheringCopper.WeatherState getAge() { return getState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean isWaxed() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */