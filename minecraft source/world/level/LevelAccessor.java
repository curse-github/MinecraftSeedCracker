/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkSource;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.redstone.NeighborUpdater;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.ticks.ScheduledTick;
/*    */ import net.minecraft.world.ticks.TickPriority;
/*    */ 
/*    */ public interface LevelAccessor
/*    */   extends CommonLevelAccessor, LevelReader, ScheduledTickAccess
/*    */ {
/*    */   long nextSubTickCount();
/*    */   
/* 31 */   default <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay, TickPriority priority) { return new ScheduledTick(type, pos, getGameTime() + tickDelay, priority, nextSubTickCount()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   default <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay) { return new ScheduledTick(type, pos, getGameTime() + tickDelay, nextSubTickCount()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   LevelData getLevelData();
/*    */ 
/*    */ 
/*    */   
/* 45 */   default long getGameTime() { return getLevelData().getGameTime(); }
/*    */ 
/*    */   
/*    */   MinecraftServer getServer();
/*    */ 
/*    */   
/* 51 */   default Difficulty getDifficulty() { return getLevelData().getDifficulty(); }
/*    */ 
/*    */ 
/*    */   
/*    */   ChunkSource getChunkSource();
/*    */ 
/*    */   
/* 58 */   default boolean hasChunk(int chunkX, int chunkZ) { return getChunkSource().hasChunk(chunkX, chunkZ); }
/*    */ 
/*    */   
/*    */   RandomSource getRandom();
/*    */ 
/*    */   
/*    */   default void updateNeighborsAt(BlockPos pos, Block sourceBlock) {}
/*    */ 
/*    */   
/* 67 */   default void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, @UpdateFlags int updateFlags, int updateLimit) { NeighborUpdater.executeShapeUpdate(this, direction, pos, neighborPos, neighborState, updateFlags, updateLimit - 1); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   default void playSound(Entity except, BlockPos pos, SoundEvent soundEvent, SoundSource source) { playSound(except, pos, soundEvent, source, 1.0F, 1.0F); }
/*    */ 
/*    */   
/*    */   void playSound(Entity paramEntity, BlockPos paramBlockPos, SoundEvent paramSoundEvent, SoundSource paramSoundSource, float paramFloat1, float paramFloat2);
/*    */ 
/*    */   
/*    */   void addParticle(ParticleOptions paramParticleOptions, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */   
/*    */   void levelEvent(Entity paramEntity, int paramInt1, BlockPos paramBlockPos, int paramInt2);
/*    */   
/* 81 */   default void levelEvent(int type, BlockPos pos, int data) { levelEvent(null, type, pos, data); }
/*    */ 
/*    */   
/*    */   void gameEvent(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext);
/*    */ 
/*    */   
/* 87 */   default void gameEvent(Entity sourceEntity, Holder<GameEvent> gameEvent, Vec3 pos) { gameEvent(gameEvent, pos, new GameEvent.Context(sourceEntity, null)); }
/*    */ 
/*    */ 
/*    */   
/* 91 */   default void gameEvent(Entity sourceEntity, Holder<GameEvent> gameEvent, BlockPos pos) { gameEvent(gameEvent, pos, new GameEvent.Context(sourceEntity, null)); }
/*    */ 
/*    */ 
/*    */   
/* 95 */   default void gameEvent(Holder<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) { gameEvent(gameEvent, Vec3.atCenterOf(pos), context); }
/*    */ 
/*    */ 
/*    */   
/* 99 */   default void gameEvent(ResourceKey<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) { gameEvent(registryAccess().lookupOrThrow(Registries.GAME_EVENT).getOrThrow(gameEvent), pos, context); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\LevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */