/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AttachedStemBlock extends VegetationBlock {
/* 27 */   public static final MapCodec<AttachedStemBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 28 */         ResourceKey.codec(Registries.BLOCK).fieldOf("fruit").forGetter(()), 
/* 29 */         ResourceKey.codec(Registries.BLOCK).fieldOf("stem").forGetter(()), 
/* 30 */         ResourceKey.codec(Registries.ITEM).fieldOf("seed").forGetter(()), 
/* 31 */         propertiesCodec())
/* 32 */       .apply(i, AttachedStemBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 36 */   public MapCodec<AttachedStemBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 39 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 41 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(4.0D, 0.0D, 10.0D, 0.0D, 10.0D));
/*    */   
/*    */   private final ResourceKey<Block> fruit;
/*    */   
/*    */   private final ResourceKey<Block> stem;
/*    */   private final ResourceKey<Item> seed;
/*    */   
/*    */   protected AttachedStemBlock(ResourceKey<Block> stem, ResourceKey<Block> fruit, ResourceKey<Item> seed, BlockBehaviour.Properties properties) {
/* 49 */     super(properties);
/* 50 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
/* 51 */     this.stem = stem;
/* 52 */     this.fruit = fruit;
/* 53 */     this.seed = seed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 63 */     if (!neighbourState.is(this.fruit) && directionToNeighbour == state.getValue(FACING)) {
/* 64 */       Optional<Block> stem = level.registryAccess().lookupOrThrow(Registries.BLOCK).getOptional(this.stem);
/* 65 */       if (stem.isPresent()) {
/* 66 */         return (BlockState)((Block)stem.get()).defaultBlockState().trySetValue(StemBlock.AGE, Integer.valueOf(7));
/*    */       }
/*    */     } 
/* 69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 74 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return state.is(Blocks.FARMLAND); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack((ItemLike)DataFixUtils.orElse(level.registryAccess().lookupOrThrow(Registries.ITEM).getOptional(this.seed), this)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 89 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 94 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AttachedStemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */