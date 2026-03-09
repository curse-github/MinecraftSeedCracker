/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class DarkOakTrunkPlacer extends TrunkPlacer {
/* 19 */   public static final MapCodec<DarkOakTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, DarkOakTrunkPlacer::new));
/*    */ 
/*    */   
/* 22 */   public DarkOakTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) { super(baseHeight, heightRandA, heightRandB); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.DARK_OAK_TRUNK_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 32 */     List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
/*    */     
/* 34 */     BlockPos below = origin.below();
/* 35 */     setDirtAt(level, trunkSetter, random, below, config);
/* 36 */     setDirtAt(level, trunkSetter, random, below.east(), config);
/* 37 */     setDirtAt(level, trunkSetter, random, below.south(), config);
/* 38 */     setDirtAt(level, trunkSetter, random, below.south().east(), config);
/*    */     
/* 40 */     Direction leanDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 41 */     int leanHeight = treeHeight - random.nextInt(4);
/* 42 */     int leanSteps = 2 - random.nextInt(3);
/*    */     
/* 44 */     int x = origin.getX();
/* 45 */     int y = origin.getY();
/* 46 */     int z = origin.getZ();
/*    */     
/* 48 */     int tx = x;
/* 49 */     int tz = z;
/* 50 */     int ey = y + treeHeight - 1;
/*    */ 
/*    */     
/* 53 */     for (int dy = 0; dy < treeHeight; dy++) {
/* 54 */       if (dy >= leanHeight && leanSteps > 0) {
/* 55 */         tx += leanDirection.getStepX();
/* 56 */         tz += leanDirection.getStepZ();
/* 57 */         leanSteps--;
/*    */       } 
/*    */       
/* 60 */       int yy = y + dy;
/* 61 */       BlockPos blockPos = new BlockPos(tx, yy, tz);
/* 62 */       if (TreeFeature.isAirOrLeaves(level, blockPos)) {
/* 63 */         placeLog(level, trunkSetter, random, blockPos, config);
/* 64 */         placeLog(level, trunkSetter, random, blockPos.east(), config);
/* 65 */         placeLog(level, trunkSetter, random, blockPos.south(), config);
/* 66 */         placeLog(level, trunkSetter, random, blockPos.east().south(), config);
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     attachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(tx, ey, tz), 0, true));
/*    */ 
/*    */     
/* 73 */     for (int ox = -1; ox <= 2; ox++) {
/* 74 */       for (int oz = -1; oz <= 2; oz++) {
/* 75 */         if (ox < 0 || ox > 1 || oz < 0 || oz > 1)
/*    */         {
/*    */           
/* 78 */           if (random.nextInt(3) <= 0) {
/*    */ 
/*    */             
/* 81 */             int length = random.nextInt(3) + 2;
/* 82 */             for (int branchY = 0; branchY < length; branchY++) {
/* 83 */               placeLog(level, trunkSetter, random, new BlockPos(x + ox, ey - branchY - 1, z + oz), config);
/*    */             }
/*    */             
/* 86 */             attachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(x + ox, ey, z + oz), 0, false));
/*    */           }  } 
/*    */       } 
/*    */     } 
/* 90 */     return attachments;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\DarkOakTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */