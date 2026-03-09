/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ 
/*    */ class WeavingMobEffect
/*    */   extends MobEffect
/*    */ {
/*    */   private final ToIntFunction<RandomSource> maxCobwebs;
/*    */   
/*    */   protected WeavingMobEffect(MobEffectCategory category, int color, ToIntFunction<RandomSource> maxCobwebs) {
/* 24 */     super(category, color, ParticleTypes.ITEM_COBWEB);
/* 25 */     this.maxCobwebs = maxCobwebs;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 30 */     if (reason == Entity.RemovalReason.KILLED && (
/* 31 */       mob instanceof net.minecraft.world.entity.player.Player || ((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())) {
/* 32 */       spawnCobwebsRandomlyAround(level, mob.getRandom(), mob.blockPosition());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void spawnCobwebsRandomlyAround(ServerLevel level, RandomSource random, BlockPos pos) {
/* 38 */     Set<BlockPos> positionsToTransform = Sets.newHashSet();
/* 39 */     int cobwebCount = this.maxCobwebs.applyAsInt(random);
/* 40 */     for (BlockPos blockPos : BlockPos.randomInCube(random, 15, pos, 1)) {
/* 41 */       BlockPos below = blockPos.below();
/*    */       
/* 43 */       if (positionsToTransform.contains(blockPos)) {
/*    */         continue;
/*    */       }
/*    */       
/* 47 */       if (level.getBlockState(blockPos).canBeReplaced() && level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
/* 48 */         positionsToTransform.add(blockPos.immutable());
/* 49 */         if (positionsToTransform.size() >= cobwebCount) {
/*    */           break;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     for (BlockPos blockPos : positionsToTransform) {
/* 56 */       level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
/* 57 */       level.levelEvent(3018, blockPos, 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\WeavingMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */