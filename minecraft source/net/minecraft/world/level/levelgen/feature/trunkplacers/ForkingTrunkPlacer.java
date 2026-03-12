/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class ForkingTrunkPlacer extends TrunkPlacer {
/* 19 */   public static final MapCodec<ForkingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, ForkingTrunkPlacer::new));
/*    */ 
/*    */   
/* 22 */   public ForkingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) { super(baseHeight, heightRandA, heightRandB); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.FORKING_TRUNK_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 32 */     setDirtAt(level, trunkSetter, random, origin.below(), config);
/*    */     
/* 34 */     List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
/*    */     
/* 36 */     Direction leanDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 37 */     int leanHeight = treeHeight - random.nextInt(4) - 1;
/* 38 */     int leanSteps = 3 - random.nextInt(3);
/*    */     
/* 40 */     BlockPos.MutableBlockPos logPos = new BlockPos.MutableBlockPos();
/* 41 */     int tx = origin.getX();
/* 42 */     int tz = origin.getZ();
/* 43 */     OptionalInt ey = OptionalInt.empty();
/* 44 */     for (int yo = 0; yo < treeHeight; yo++) {
/* 45 */       int yy = origin.getY() + yo;
/* 46 */       if (yo >= leanHeight && leanSteps > 0) {
/* 47 */         tx += leanDirection.getStepX();
/* 48 */         tz += leanDirection.getStepZ();
/* 49 */         leanSteps--;
/*    */       } 
/* 51 */       if (placeLog(level, trunkSetter, random, logPos.set(tx, yy, tz), config)) {
/* 52 */         ey = OptionalInt.of(yy + 1);
/*    */       }
/*    */     } 
/*    */     
/* 56 */     if (ey.isPresent()) {
/* 57 */       attachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(tx, ey.getAsInt(), tz), 1, false));
/*    */     }
/*    */     
/* 60 */     tx = origin.getX();
/* 61 */     tz = origin.getZ();
/* 62 */     Direction branchDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 63 */     if (branchDirection != leanDirection) {
/* 64 */       int branchPos = leanHeight - random.nextInt(2) - 1;
/* 65 */       int branchSteps = 1 + random.nextInt(3);
/*    */       
/* 67 */       ey = OptionalInt.empty();
/* 68 */       for (int yo = branchPos; yo < treeHeight && branchSteps > 0; yo++, branchSteps--) {
/* 69 */         if (yo >= 1) {
/*    */ 
/*    */           
/* 72 */           int yy = origin.getY() + yo;
/* 73 */           tx += branchDirection.getStepX();
/* 74 */           tz += branchDirection.getStepZ();
/* 75 */           if (placeLog(level, trunkSetter, random, logPos.set(tx, yy, tz), config))
/* 76 */             ey = OptionalInt.of(yy + 1); 
/*    */         } 
/*    */       } 
/* 79 */       if (ey.isPresent()) {
/* 80 */         attachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(tx, ey.getAsInt(), tz), 0, false));
/*    */       }
/*    */     } 
/*    */     
/* 84 */     return attachments;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\ForkingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */