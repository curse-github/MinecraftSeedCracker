/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public interface ServerLevelAccessor
/*    */   extends LevelAccessor
/*    */ {
/*    */   ServerLevel getLevel();
/*    */   
/*    */   DifficultyInstance getCurrentDifficultyAt(BlockPos paramBlockPos);
/*    */   
/* 15 */   default void addFreshEntityWithPassengers(Entity entity) { entity.getSelfAndPassengers().forEach(this::addFreshEntity); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ServerLevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */