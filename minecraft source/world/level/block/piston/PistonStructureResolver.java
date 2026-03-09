/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ 
/*     */ 
/*     */ public class PistonStructureResolver
/*     */ {
/*     */   public static final int MAX_PUSH_DEPTH = 12;
/*     */   private final Level level;
/*     */   private final BlockPos pistonPos;
/*     */   private final boolean extending;
/*     */   
/*     */   public PistonStructureResolver(Level level, BlockPos pistonPos, Direction direction, boolean extending) {
/*  21 */     this.toPush = Lists.newArrayList();
/*  22 */     this.toDestroy = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*  26 */     this.level = level;
/*  27 */     this.pistonPos = pistonPos;
/*  28 */     this.pistonDirection = direction;
/*  29 */     this.extending = extending;
/*     */     
/*  31 */     if (extending) {
/*  32 */       this.pushDirection = direction;
/*  33 */       this.startPos = pistonPos.relative(direction);
/*     */     } else {
/*  35 */       this.pushDirection = direction.getOpposite();
/*  36 */       this.startPos = pistonPos.relative(direction, 2);
/*     */     } 
/*     */   }
/*     */   private final BlockPos startPos; private final Direction pushDirection; private final List<BlockPos> toPush; private final List<BlockPos> toDestroy; private final Direction pistonDirection;
/*     */   public boolean resolve() {
/*  41 */     this.toPush.clear();
/*  42 */     this.toDestroy.clear();
/*     */     
/*  44 */     BlockState nextState = this.level.getBlockState(this.startPos);
/*     */     
/*  46 */     if (!PistonBaseBlock.isPushable(nextState, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
/*  47 */       if (this.extending && nextState.getPistonPushReaction() == PushReaction.DESTROY) {
/*  48 */         this.toDestroy.add(this.startPos);
/*  49 */         return true;
/*     */       } 
/*     */       
/*  52 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  56 */     if (!addBlockLine(this.startPos, this.pushDirection))
/*     */     {
/*  58 */       return false;
/*     */     }
/*     */     
/*  61 */     for (int i = 0; i < this.toPush.size(); i++) {
/*  62 */       BlockPos pos = (BlockPos)this.toPush.get(i);
/*     */ 
/*     */       
/*  65 */       if (isSticky(this.level.getBlockState(pos)) && 
/*  66 */         !addBranchingBlocks(pos))
/*     */       {
/*  68 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  77 */   private static boolean isSticky(BlockState state) { return (state.is(Blocks.SLIME_BLOCK) || state.is(Blocks.HONEY_BLOCK)); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canStickToEachOther(BlockState state1, BlockState state2) {
/*  82 */     if (state1.is(Blocks.HONEY_BLOCK) && state2.is(Blocks.SLIME_BLOCK)) {
/*  83 */       return false;
/*     */     }
/*  85 */     if (state1.is(Blocks.SLIME_BLOCK) && state2.is(Blocks.HONEY_BLOCK)) {
/*  86 */       return false;
/*     */     }
/*  88 */     return (isSticky(state1) || isSticky(state2));
/*     */   }
/*     */   
/*     */   private boolean addBlockLine(BlockPos start, Direction direction) {
/*  92 */     BlockState nextState = this.level.getBlockState(start);
/*  93 */     if (nextState.isAir())
/*     */     {
/*  95 */       return true; } 
/*  96 */     if (!PistonBaseBlock.isPushable(nextState, this.level, start, this.pushDirection, false, direction))
/*     */     {
/*  98 */       return true; } 
/*  99 */     if (start.equals(this.pistonPos))
/*     */     {
/* 101 */       return true; } 
/* 102 */     if (this.toPush.contains(start))
/*     */     {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     int blockCount = 1;
/* 108 */     if (blockCount + this.toPush.size() > 12)
/*     */     {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     while (isSticky(nextState)) {
/* 114 */       BlockPos pos = start.relative(this.pushDirection.getOpposite(), blockCount);
/* 115 */       BlockState previousState = nextState;
/* 116 */       nextState = this.level.getBlockState(pos);
/*     */       
/* 118 */       if (nextState.isAir() || !canStickToEachOther(previousState, nextState) || !PistonBaseBlock.isPushable(nextState, this.level, pos, this.pushDirection, false, this.pushDirection.getOpposite()) || pos.equals(this.pistonPos)) {
/*     */         break;
/*     */       }
/* 121 */       blockCount++;
/* 122 */       if (blockCount + this.toPush.size() > 12) {
/* 123 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 128 */     int blocksAdded = 0;
/*     */ 
/*     */     
/* 131 */     for (int i = blockCount - 1; i >= 0; i--) {
/* 132 */       this.toPush.add(start.relative(this.pushDirection.getOpposite(), i));
/* 133 */       blocksAdded++;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     for (int i = 1;; i++) {
/* 138 */       BlockPos pos = start.relative(this.pushDirection, i);
/*     */       
/* 140 */       int collisionPos = this.toPush.indexOf(pos);
/* 141 */       if (collisionPos > -1) {
/*     */         
/* 143 */         reorderListAtCollision(blocksAdded, collisionPos);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 148 */         for (int j = 0; j <= collisionPos + blocksAdded; j++) {
/* 149 */           BlockPos blockPos = (BlockPos)this.toPush.get(j);
/* 150 */           if (isSticky(this.level.getBlockState(blockPos)) && 
/* 151 */             !addBranchingBlocks(blockPos)) {
/* 152 */             return false;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 157 */         return true;
/*     */       } 
/*     */       
/* 160 */       nextState = this.level.getBlockState(pos);
/*     */       
/* 162 */       if (nextState.isAir())
/*     */       {
/* 164 */         return true;
/*     */       }
/*     */       
/* 167 */       if (!PistonBaseBlock.isPushable(nextState, this.level, pos, this.pushDirection, true, this.pushDirection) || pos.equals(this.pistonPos))
/*     */       {
/* 169 */         return false;
/*     */       }
/*     */       
/* 172 */       if (nextState.getPistonPushReaction() == PushReaction.DESTROY) {
/* 173 */         this.toDestroy.add(pos);
/* 174 */         return true;
/*     */       } 
/*     */       
/* 177 */       if (this.toPush.size() >= 12) {
/* 178 */         return false;
/*     */       }
/*     */       
/* 181 */       this.toPush.add(pos);
/* 182 */       blocksAdded++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reorderListAtCollision(int blocksAdded, int collisionPos) {
/* 187 */     List<BlockPos> head = Lists.newArrayList();
/* 188 */     List<BlockPos> lastLineAdded = Lists.newArrayList();
/* 189 */     List<BlockPos> collisionToLine = Lists.newArrayList();
/*     */     
/* 191 */     head.addAll(this.toPush.subList(0, collisionPos));
/* 192 */     lastLineAdded.addAll(this.toPush.subList(this.toPush.size() - blocksAdded, this.toPush.size()));
/* 193 */     collisionToLine.addAll(this.toPush.subList(collisionPos, this.toPush.size() - blocksAdded));
/*     */     
/* 195 */     this.toPush.clear();
/* 196 */     this.toPush.addAll(head);
/* 197 */     this.toPush.addAll(lastLineAdded);
/* 198 */     this.toPush.addAll(collisionToLine);
/*     */   }
/*     */   
/*     */   private boolean addBranchingBlocks(BlockPos fromPos) {
/* 202 */     BlockState fromState = this.level.getBlockState(fromPos);
/* 203 */     for (Direction direction : Direction.values()) {
/* 204 */       if (direction.getAxis() != this.pushDirection.getAxis()) {
/* 205 */         BlockPos neighbourPos = fromPos.relative(direction);
/* 206 */         BlockState neighbourState = this.level.getBlockState(neighbourPos);
/* 207 */         if (canStickToEachOther(neighbourState, fromState))
/*     */         {
/*     */           
/* 210 */           if (!addBlockLine(neighbourPos, direction)) {
/* 211 */             return false;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/* 216 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 220 */   public Direction getPushDirection() { return this.pushDirection; }
/*     */ 
/*     */ 
/*     */   
/* 224 */   public List<BlockPos> getToPush() { return this.toPush; }
/*     */ 
/*     */ 
/*     */   
/* 228 */   public List<BlockPos> getToDestroy() { return this.toDestroy; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\piston\PistonStructureResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */