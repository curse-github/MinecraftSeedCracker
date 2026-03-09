/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class LongJumpToPreferredBlock<E extends Mob>
/*    */   extends LongJumpToRandomPos<E> {
/*    */   private final TagKey<Block> preferredBlockTag;
/*    */   private final float preferredBlocksChance;
/* 22 */   private final List<LongJumpToRandomPos.PossibleJump> notPrefferedJumpCandidates = new ArrayList();
/*    */   private boolean currentlyWantingPreferredOnes;
/*    */   
/*    */   public LongJumpToPreferredBlock(UniformInt timeBetweenLongJumps, int maxLongJumpHeight, int maxLongJumpWidth, float maxJumpVelocity, Function<E, SoundEvent> getJumpSound, TagKey<Block> preferredBlockTag, float preferredBlocksChance, BiPredicate<E, BlockPos> acceptableLandingSpot) {
/* 26 */     super(timeBetweenLongJumps, maxLongJumpHeight, maxLongJumpWidth, maxJumpVelocity, getJumpSound, acceptableLandingSpot);
/* 27 */     this.preferredBlockTag = preferredBlockTag;
/* 28 */     this.preferredBlocksChance = preferredBlocksChance;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, E body, long timestamp) {
/* 33 */     super.start(level, body, timestamp);
/*    */     
/* 35 */     this.notPrefferedJumpCandidates.clear();
/*    */     
/* 37 */     this.currentlyWantingPreferredOnes = (body.getRandom().nextFloat() < this.preferredBlocksChance);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Optional<LongJumpToRandomPos.PossibleJump> getJumpCandidate(ServerLevel level) {
/* 42 */     if (!this.currentlyWantingPreferredOnes) {
/* 43 */       return super.getJumpCandidate(level);
/*    */     }
/*    */     
/* 46 */     BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
/*    */     
/* 48 */     while (!this.jumpCandidates.isEmpty()) {
/* 49 */       Optional<LongJumpToRandomPos.PossibleJump> jumpCandidate = super.getJumpCandidate(level);
/*    */       
/* 51 */       if (jumpCandidate.isPresent()) {
/* 52 */         LongJumpToRandomPos.PossibleJump possibleJump = (LongJumpToRandomPos.PossibleJump)jumpCandidate.get();
/*    */         
/* 54 */         if (level.getBlockState(testPos.setWithOffset(possibleJump.targetPos(), Direction.DOWN)).is(this.preferredBlockTag)) {
/* 55 */           return jumpCandidate;
/*    */         }
/*    */         
/* 58 */         this.notPrefferedJumpCandidates.add(possibleJump);
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     if (!this.notPrefferedJumpCandidates.isEmpty()) {
/* 63 */       return Optional.of((LongJumpToRandomPos.PossibleJump)this.notPrefferedJumpCandidates.remove(0));
/*    */     }
/*    */     
/* 66 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LongJumpToPreferredBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */