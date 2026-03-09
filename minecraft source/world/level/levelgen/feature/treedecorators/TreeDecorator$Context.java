/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Context
/*    */ {
/*    */   private final LevelSimulatedReader level;
/*    */   private final BiConsumer<BlockPos, BlockState> decorationSetter;
/*    */   private final RandomSource random;
/*    */   private final ObjectArrayList<BlockPos> logs;
/*    */   private final ObjectArrayList<BlockPos> leaves;
/*    */   private final ObjectArrayList<BlockPos> roots;
/*    */   
/*    */   public Context(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> decorationSetter, RandomSource random, Set<BlockPos> trunkSet, Set<BlockPos> foliageSet, Set<BlockPos> rootSet) {
/* 34 */     this.level = level;
/* 35 */     this.decorationSetter = decorationSetter;
/* 36 */     this.random = random;
/*    */     
/* 38 */     this.roots = new ObjectArrayList(rootSet);
/* 39 */     this.logs = new ObjectArrayList(trunkSet);
/* 40 */     this.leaves = new ObjectArrayList(foliageSet);
/*    */     
/* 42 */     this.logs.sort(Comparator.comparingInt(Vec3i::getY));
/* 43 */     this.leaves.sort(Comparator.comparingInt(Vec3i::getY));
/* 44 */     this.roots.sort(Comparator.comparingInt(Vec3i::getY));
/*    */   }
/*    */ 
/*    */   
/* 48 */   public void placeVine(BlockPos pos, BooleanProperty direction) { setBlock(pos, (BlockState)Blocks.VINE.defaultBlockState().setValue(direction, Boolean.valueOf(true))); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public void setBlock(BlockPos pos, BlockState state) { this.decorationSetter.accept(pos, state); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public boolean isAir(BlockPos pos) { return this.level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public boolean checkBlock(BlockPos pos, Predicate<BlockState> predicate) { return this.level.isStateAtPosition(pos, predicate); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public LevelSimulatedReader level() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public RandomSource random() { return this.random; }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public ObjectArrayList<BlockPos> logs() { return this.logs; }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public ObjectArrayList<BlockPos> leaves() { return this.leaves; }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public ObjectArrayList<BlockPos> roots() { return this.roots; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TreeDecorator$Context.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */