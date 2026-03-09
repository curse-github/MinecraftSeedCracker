/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public class BlockInWorld
/*    */ {
/*    */   private final LevelReader level;
/*    */   private final BlockPos pos;
/*    */   private final boolean loadChunks;
/*    */   private BlockState state;
/*    */   private BlockEntity entity;
/*    */   private boolean cachedEntity;
/*    */   
/*    */   public BlockInWorld(LevelReader level, BlockPos pos, boolean loadChunks) {
/* 20 */     this.level = level;
/* 21 */     this.pos = pos.immutable();
/* 22 */     this.loadChunks = loadChunks;
/*    */   }
/*    */   
/*    */   public BlockState getState() {
/* 26 */     if (this.state == null && (this.loadChunks || this.level.hasChunkAt(this.pos))) {
/* 27 */       this.state = this.level.getBlockState(this.pos);
/*    */     }
/*    */     
/* 30 */     return this.state;
/*    */   }
/*    */   
/*    */   public BlockEntity getEntity() {
/* 34 */     if (this.entity == null && !this.cachedEntity) {
/* 35 */       this.entity = this.level.getBlockEntity(this.pos);
/* 36 */       this.cachedEntity = true;
/*    */     } 
/*    */     
/* 39 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/* 43 */   public LevelReader getLevel() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static Predicate<BlockInWorld> hasState(Predicate<BlockState> predicate) { return input -> (input != null && predicate.test(input.getState())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockInWorld.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */