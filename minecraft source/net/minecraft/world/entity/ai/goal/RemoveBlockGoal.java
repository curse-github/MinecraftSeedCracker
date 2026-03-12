/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RemoveBlockGoal
/*     */   extends MoveToBlockGoal
/*     */ {
/*     */   private final Block blockToRemove;
/*     */   private final Mob removerMob;
/*     */   private int ticksSinceReachedGoal;
/*     */   private static final int WAIT_AFTER_BLOCK_FOUND = 20;
/*     */   
/*     */   public RemoveBlockGoal(Block blockToRemove, PathfinderMob mob, double speedModifier, int verticalSearchRange) {
/*  32 */     super(mob, speedModifier, 24, verticalSearchRange);
/*  33 */     this.blockToRemove = blockToRemove;
/*  34 */     this.removerMob = mob;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  39 */     if (!((Boolean)getServerLevel(this.removerMob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     if (this.nextStartTick > 0) {
/*  44 */       this.nextStartTick--;
/*  45 */       return false;
/*     */     } 
/*     */     
/*  48 */     if (findNearestBlock()) {
/*     */       
/*  50 */       this.nextStartTick = reducedTickDelay(20);
/*  51 */       return true;
/*     */     } 
/*  53 */     this.nextStartTick = nextStartTick(this.mob);
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  60 */     super.stop();
/*  61 */     this.removerMob.fallDistance = 1.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  66 */     super.start();
/*  67 */     this.ticksSinceReachedGoal = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {}
/*     */ 
/*     */   
/*     */   public void playBreakSound(Level level, BlockPos pos) {}
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     super.tick();
/*  79 */     Level level = this.removerMob.level();
/*  80 */     BlockPos mobPos = this.removerMob.blockPosition();
/*     */     
/*  82 */     BlockPos eatPos = getPosWithBlock(mobPos, level);
/*     */     
/*  84 */     RandomSource random = this.removerMob.getRandom();
/*  85 */     if (isReachedTarget() && eatPos != null) {
/*  86 */       if (this.ticksSinceReachedGoal > 0) {
/*  87 */         Vec3 movement = this.removerMob.getDeltaMovement();
/*  88 */         this.removerMob.setDeltaMovement(movement.x, 0.3D, movement.z);
/*     */         
/*  90 */         if (!level.isClientSide()) {
/*  91 */           double v = 0.08D;
/*  92 */           ((ServerLevel)level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)), eatPos
/*     */               
/*  94 */               .getX() + 0.5D, eatPos
/*  95 */               .getY() + 0.7D, eatPos
/*  96 */               .getZ() + 0.5D, 3, (random
/*     */               
/*  98 */               .nextFloat() - 0.5D) * 0.08D, (random
/*  99 */               .nextFloat() - 0.5D) * 0.08D, (random
/* 100 */               .nextFloat() - 0.5D) * 0.08D, 0.15000000596046448D);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 106 */       if (this.ticksSinceReachedGoal % 2 == 0) {
/* 107 */         Vec3 movement = this.removerMob.getDeltaMovement();
/* 108 */         this.removerMob.setDeltaMovement(movement.x, -0.3D, movement.z);
/*     */         
/* 110 */         if (this.ticksSinceReachedGoal % 6 == 0) {
/* 111 */           playDestroyProgressSound(level, this.blockPos);
/*     */         }
/*     */       } 
/*     */       
/* 115 */       if (this.ticksSinceReachedGoal > 60) {
/* 116 */         level.removeBlock(eatPos, false);
/* 117 */         if (!level.isClientSide()) {
/* 118 */           for (int i = 0; i < 20; i++) {
/* 119 */             double xa = random.nextGaussian() * 0.02D;
/* 120 */             double ya = random.nextGaussian() * 0.02D;
/* 121 */             double za = random.nextGaussian() * 0.02D;
/* 122 */             ((ServerLevel)level).sendParticles(ParticleTypes.POOF, eatPos.getX() + 0.5D, eatPos.getY(), eatPos.getZ() + 0.5D, 1, xa, ya, za, 0.15000000596046448D);
/*     */           } 
/* 124 */           playBreakSound(level, eatPos);
/*     */         } 
/*     */       } 
/* 127 */       this.ticksSinceReachedGoal++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockPos getPosWithBlock(BlockPos pos, BlockGetter level) {
/* 132 */     if (level.getBlockState(pos).is(this.blockToRemove)) {
/* 133 */       return pos;
/*     */     }
/* 135 */     BlockPos[] neighbours = { pos.below(), pos.west(), pos.east(), pos.north(), pos.south(), pos.below().below() };
/* 136 */     for (BlockPos neighborPos : neighbours) {
/* 137 */       if (level.getBlockState(neighborPos).is(this.blockToRemove)) {
/* 138 */         return neighborPos;
/*     */       }
/*     */     } 
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 146 */     ChunkAccess chunk = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
/* 147 */     if (chunk != null) {
/* 148 */       return (chunk.getBlockState(pos).is(this.blockToRemove) && chunk.getBlockState(pos.above()).isAir() && chunk.getBlockState(pos.above(2)).isAir());
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RemoveBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */