/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SkullBlock extends AbstractSkullBlock {
/* 22 */   public static final MapCodec<SkullBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Type.CODEC
/* 23 */         .fieldOf("kind").forGetter(AbstractSkullBlock::getType), 
/* 24 */         propertiesCodec())
/* 25 */       .apply(i, SkullBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 29 */   public MapCodec<? extends SkullBlock> codec() { return CODEC; }
/*    */   
/*    */   public static interface Type
/*    */     extends StringRepresentable {
/* 33 */     public static final Map<String, Type> TYPES = new Object2ObjectArrayMap(); public static final Codec<Type> CODEC;
/*    */     static  {
/* 35 */       Objects.requireNonNull(TYPES); CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
/*    */     } }
/*    */   
/*    */   public enum Types implements Type {
/* 39 */     SKELETON("skeleton"),
/* 40 */     WITHER_SKELETON("wither_skeleton"),
/* 41 */     PLAYER("player"),
/* 42 */     ZOMBIE("zombie"),
/* 43 */     CREEPER("creeper"),
/* 44 */     PIGLIN("piglin"),
/* 45 */     DRAGON("dragon");
/*    */     
/*    */     private final String name;
/*    */ 
/*    */     
/*    */     Types(String name) {
/* 51 */       this.name = name;
/* 52 */       TYPES.put(name, this);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 57 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ 
/*    */   
/* 61 */   public static final int MAX = RotationSegment.getMaxSegmentIndex();
/* 62 */   private static final int ROTATIONS = MAX + 1;
/*    */   
/* 64 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 66 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 8.0D);
/* 67 */   private static final VoxelShape SHAPE_PIGLIN = Block.column(10.0D, 0.0D, 8.0D);
/*    */   
/*    */   protected SkullBlock(Type type, BlockBehaviour.Properties properties) {
/* 70 */     super(type, properties);
/* 71 */     registerDefaultState((BlockState)defaultBlockState().setValue(ROTATION, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (getType() == Types.PIGLIN) ? SHAPE_PIGLIN : SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)super.getStateForPlacement(context).setValue(ROTATION, Integer.valueOf(RotationSegment.convertToSegment(context.getRotation()))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue(ROTATION)).intValue(), ROTATIONS))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   protected BlockState mirror(BlockState state, Mirror mirror) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue(ROTATION)).intValue(), ROTATIONS))); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 96 */     super.createBlockStateDefinition(builder);
/* 97 */     builder.add(new Property[] { ROTATION });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */