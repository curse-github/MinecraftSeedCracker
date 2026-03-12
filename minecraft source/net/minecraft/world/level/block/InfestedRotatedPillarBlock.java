/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ 
/*    */ public class InfestedRotatedPillarBlock extends InfestedBlock {
/* 12 */   public static final MapCodec<InfestedRotatedPillarBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/* 13 */         .byNameCodec().fieldOf("host").forGetter(InfestedBlock::getHostBlock), 
/* 14 */         propertiesCodec())
/* 15 */       .apply(i, InfestedRotatedPillarBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<InfestedRotatedPillarBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/*    */   public InfestedRotatedPillarBlock(Block hostBlock, BlockBehaviour.Properties properties) {
/* 23 */     super(hostBlock, properties);
/* 24 */     registerDefaultState((BlockState)defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected BlockState rotate(BlockState state, Rotation rotation) { return RotatedPillarBlock.rotatePillar(state, rotation); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { RotatedPillarBlock.AXIS }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(RotatedPillarBlock.AXIS, context.getClickedFace().getAxis()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\InfestedRotatedPillarBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */