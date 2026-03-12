/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class WeatheringCopperGolemStatueBlock extends CopperGolemStatueBlock implements WeatheringCopper {
/* 21 */   public static final MapCodec<WeatheringCopperGolemStatueBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/* 22 */         .fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), 
/* 23 */         propertiesCodec())
/* 24 */       .apply(i, WeatheringCopperGolemStatueBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<WeatheringCopperGolemStatueBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) { super(weatherState, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected boolean isRandomlyTicking(BlockState state) { return WeatheringCopper.getNext(state.getBlock()).isPresent(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { changeOverTime(state, level, pos, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public WeatheringCopper.WeatherState getAge() { return getWeatheringState(); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 52 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CopperGolemStatueBlockEntity) { CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
/* 53 */       if (itemStack.is(ItemTags.AXES))
/* 54 */       { if (getAge().equals(WeatheringCopper.WeatherState.UNAFFECTED)) {
/* 55 */           CopperGolem copperGolem = copperGolemStatueBlockEntity.removeStatue(state);
/* 56 */           itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
/* 57 */           if (copperGolem != null) {
/* 58 */             level.addFreshEntity(copperGolem);
/* 59 */             level.removeBlock(pos, false);
/* 60 */             return InteractionResult.SUCCESS;
/*    */           } 
/*    */         }  }
/* 63 */       else { if (itemStack.is(Items.HONEYCOMB)) {
/* 64 */           return InteractionResult.PASS;
/*    */         }
/* 66 */         updatePose(level, state, pos, player);
/* 67 */         return InteractionResult.SUCCESS; }
/*    */        }
/*    */     
/* 70 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperGolemStatueBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */