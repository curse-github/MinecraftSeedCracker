/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StemBlock extends VegetationBlock implements BonemealableBlock {
/*  30 */   public static final MapCodec<StemBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  31 */         ResourceKey.codec(Registries.BLOCK).fieldOf("fruit").forGetter(()), 
/*  32 */         ResourceKey.codec(Registries.BLOCK).fieldOf("attached_stem").forGetter(()), 
/*  33 */         ResourceKey.codec(Registries.ITEM).fieldOf("seed").forGetter(()), 
/*  34 */         propertiesCodec())
/*  35 */       .apply(i, StemBlock::new));
/*     */   
/*     */   public static final int MAX_AGE = 7;
/*     */   
/*  39 */   public MapCodec<StemBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
/*     */   
/*  45 */   private static final VoxelShape[] SHAPES = Block.boxes(7, age -> Block.column(2.0D, 0.0D, (2 + age * 2)));
/*     */   
/*     */   private final ResourceKey<Block> fruit;
/*     */   
/*     */   private final ResourceKey<Block> attachedStem;
/*     */   private final ResourceKey<Item> seed;
/*     */   
/*     */   protected StemBlock(ResourceKey<Block> fruit, ResourceKey<Block> attachedStem, ResourceKey<Item> seed, BlockBehaviour.Properties properties) {
/*  53 */     super(properties);
/*  54 */     this.fruit = fruit;
/*  55 */     this.attachedStem = attachedStem;
/*  56 */     this.seed = seed;
/*  57 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(AGE)).intValue()]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return state.is(Blocks.FARMLAND); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  72 */     if (level.getRawBrightness(pos, 0) < 9) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     float growthSpeed = CropBlock.getGrowthSpeed(this, level, pos);
/*  77 */     if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
/*  78 */       int age = ((Integer)state.getValue(AGE)).intValue();
/*  79 */       if (age < 7) {
/*  80 */         state = (BlockState)state.setValue(AGE, Integer.valueOf(age + 1));
/*  81 */         level.setBlock(pos, state, 2);
/*     */       } else {
/*  83 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*  84 */         BlockPos relative = pos.relative(direction);
/*     */         
/*  86 */         BlockState stateBelow = level.getBlockState(relative.below());
/*  87 */         if (level.getBlockState(relative).isAir() && (stateBelow.is(Blocks.FARMLAND) || stateBelow.is(BlockTags.DIRT))) {
/*  88 */           Registry<Block> blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
/*  89 */           Optional<Block> fruit = blocks.getOptional(this.fruit);
/*  90 */           Optional<Block> stem = blocks.getOptional(this.attachedStem);
/*  91 */           if (fruit.isPresent() && stem.isPresent()) {
/*  92 */             level.setBlockAndUpdate(relative, ((Block)fruit.get()).defaultBlockState());
/*  93 */             level.setBlockAndUpdate(pos, (BlockState)((Block)stem.get()).defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack((ItemLike)DataFixUtils.orElse(level.registryAccess().lookupOrThrow(Registries.ITEM).getOptional(this.seed), this)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return (((Integer)state.getValue(AGE)).intValue() != 7); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 117 */     int age = Math.min(7, ((Integer)state.getValue(AGE)).intValue() + Mth.nextInt(level.random, 2, 5));
/* 118 */     BlockState newState = (BlockState)state.setValue(AGE, Integer.valueOf(age));
/* 119 */     level.setBlock(pos, newState, 2);
/* 120 */     if (age == 7) {
/* 121 */       newState.randomTick(level, pos, level.random);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 127 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */