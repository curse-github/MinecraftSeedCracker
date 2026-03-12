/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*    */ import net.minecraft.world.level.block.SculkSpreader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
/*    */ 
/*    */ 
/*    */ public class SculkPatchFeature
/*    */   extends Feature<SculkPatchConfiguration>
/*    */ {
/* 20 */   public SculkPatchFeature(Codec<SculkPatchConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SculkPatchConfiguration> context) {
/* 25 */     WorldGenLevel level = context.level();
/* 26 */     BlockPos origin = context.origin();
/* 27 */     if (!canSpreadFrom(level, origin)) {
/* 28 */       return false;
/*    */     }
/* 30 */     SculkPatchConfiguration config = (SculkPatchConfiguration)context.config();
/* 31 */     RandomSource random = context.random();
/* 32 */     SculkSpreader spreader = SculkSpreader.createWorldGenSpreader();
/* 33 */     int totalRounds = config.spreadRounds() + config.growthRounds();
/* 34 */     for (int round = 0; round < totalRounds; round++) {
/* 35 */       for (int i = 0; i < config.chargeCount(); i++) {
/* 36 */         spreader.addCursors(origin, config.amountPerCharge());
/*    */       }
/* 38 */       boolean spreadVeins = (round < config.spreadRounds());
/* 39 */       for (int i = 0; i < config.spreadAttempts(); i++) {
/* 40 */         spreader.updateCursors(level, origin, random, spreadVeins);
/*    */       }
/* 42 */       spreader.clear();
/*    */     } 
/* 44 */     BlockPos below = origin.below();
/* 45 */     if (random.nextFloat() <= config.catalystChance() && level.getBlockState(below).isCollisionShapeFullBlock(level, below)) {
/* 46 */       level.setBlock(origin, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
/*    */     }
/* 48 */     int extraGrowths = config.extraRareGrowths().sample(random);
/* 49 */     for (int i = 0; i < extraGrowths; i++) {
/* 50 */       BlockPos candidate = origin.offset(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
/* 51 */       if (level.getBlockState(candidate).isAir() && level.getBlockState(candidate.below()).isFaceSturdy(level, candidate.below(), Direction.UP)) {
/* 52 */         level.setBlock(candidate, (BlockState)Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(true)), 3);
/*    */       }
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   private boolean canSpreadFrom(LevelAccessor level, BlockPos origin) {
/* 59 */     BlockState start = level.getBlockState(origin);
/* 60 */     if (start.getBlock() instanceof net.minecraft.world.level.block.SculkBehaviour) {
/* 61 */       return true;
/*    */     }
/* 63 */     if (start.isAir() || (start.is(Blocks.WATER) && start.getFluidState().isSource())) {
/* 64 */       Objects.requireNonNull(origin); return Direction.stream().map(origin::relative).anyMatch(pos -> level.getBlockState(pos).isCollisionShapeFullBlock(level, pos));
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SculkPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */