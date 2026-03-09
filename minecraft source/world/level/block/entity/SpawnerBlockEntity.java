/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SpawnData;
/*    */ import net.minecraft.world.level.Spawner;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class SpawnerBlockEntity
/*    */   extends BlockEntity implements Spawner {
/* 22 */   private final BaseSpawner spawner = new BaseSpawner(this)
/*    */     {
/*    */       public void broadcastEvent(Level level, BlockPos pos, int id) {
/* 25 */         level.blockEvent(pos, Blocks.SPAWNER, id, 0);
/*    */       }
/*    */ 
/*    */       
/*    */       public void setNextSpawnData(Level level, BlockPos pos, SpawnData nextSpawnData) {
/* 30 */         super.setNextSpawnData(level, pos, nextSpawnData);
/* 31 */         if (level != null) {
/* 32 */           BlockState state = level.getBlockState(pos);
/* 33 */           level.sendBlockUpdated(pos, state, state, 260);
/*    */         } 
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 39 */   public SpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.MOB_SPAWNER, worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void loadAdditional(ValueInput input) {
/* 44 */     super.loadAdditional(input);
/* 45 */     this.spawner.load(this.level, this.worldPosition, input);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void saveAdditional(ValueOutput output) {
/* 50 */     super.saveAdditional(output);
/* 51 */     this.spawner.save(output);
/*    */   }
/*    */ 
/*    */   
/* 55 */   public static void clientTick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity entity) { entity.spawner.clientTick(level, pos); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public static void serverTick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity entity) { entity.spawner.serverTick((ServerLevel)level, pos); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*    */ 
/*    */ 
/*    */   
/*    */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 69 */     CompoundTag tag = saveCustomOnly(registries);
/* 70 */     tag.remove("SpawnPotentials");
/* 71 */     return tag;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean triggerEvent(int b0, int b1) {
/* 76 */     if (this.spawner.onEventTriggered(this.level, b0)) {
/* 77 */       return true;
/*    */     }
/* 79 */     return super.triggerEvent(b0, b1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEntityId(EntityType<?> type, RandomSource random) {
/* 84 */     this.spawner.setEntityId(type, this.level, random, this.worldPosition);
/* 85 */     setChanged();
/*    */   }
/*    */ 
/*    */   
/* 89 */   public BaseSpawner getSpawner() { return this.spawner; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\SpawnerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */