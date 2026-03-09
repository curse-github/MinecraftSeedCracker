/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class WeightedPressurePlateBlock extends BasePressurePlateBlock {
/* 19 */   public static final MapCodec<WeightedPressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 20 */         Codec.intRange(1, 1024).fieldOf("max_weight").forGetter(()), BlockSetType.CODEC
/* 21 */         .fieldOf("block_set_type").forGetter(()), 
/* 22 */         propertiesCodec())
/* 23 */       .apply(i, WeightedPressurePlateBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 27 */   public MapCodec<WeightedPressurePlateBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 30 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*    */   
/*    */   private final int maxWeight;
/*    */   
/*    */   protected WeightedPressurePlateBlock(int maxWeight, BlockSetType type, BlockBehaviour.Properties properties) {
/* 35 */     super(properties, type);
/* 36 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWER, Integer.valueOf(0)));
/* 37 */     this.maxWeight = maxWeight;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getSignalStrength(Level level, BlockPos pos) {
/* 43 */     int count = Math.min(getEntityCount(level, TOUCH_AABB.move(pos), net.minecraft.world.entity.Entity.class), this.maxWeight);
/* 44 */     if (count > 0) {
/* 45 */       float percent = Math.min(this.maxWeight, count) / this.maxWeight;
/* 46 */       return Mth.ceil(percent * 15.0F);
/*    */     } 
/*    */     
/* 49 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected int getSignalForState(BlockState state) { return ((Integer)state.getValue(POWER)).intValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected BlockState setSignalForState(BlockState state, int signal) { return (BlockState)state.setValue(POWER, Integer.valueOf(signal)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   protected int getPressedTime() { return 10; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { POWER }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeightedPressurePlateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */