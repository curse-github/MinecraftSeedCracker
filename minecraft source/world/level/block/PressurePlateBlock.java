/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class PressurePlateBlock extends BasePressurePlateBlock {
/* 17 */   public static final MapCodec<PressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockSetType.CODEC
/* 18 */         .fieldOf("block_set_type").forGetter(()), 
/* 19 */         propertiesCodec())
/* 20 */       .apply(i, PressurePlateBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<PressurePlateBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 27 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   
/*    */   protected PressurePlateBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/* 30 */     super(properties, type);
/* 31 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected int getSignalForState(BlockState state) { return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected BlockState setSignalForState(BlockState state, int signal) { return (BlockState)state.setValue(POWERED, Boolean.valueOf((signal > 0))); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getSignalStrength(Level level, BlockPos pos) {
/* 46 */     switch (this.type.pressurePlateSensitivity()) { default: throw new MatchException(null, null);
/*    */       case EVERYTHING: 
/* 48 */       case MOBS: break; }  Class<? extends Entity> entityClass = net.minecraft.world.entity.LivingEntity.class;
/*    */     
/* 50 */     return (getEntityCount(level, TOUCH_AABB.move(pos), entityClass) > 0) ? 15 : 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { POWERED }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PressurePlateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */