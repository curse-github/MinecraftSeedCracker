/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.Spawner;
/*    */ import net.minecraft.world.level.block.TrialSpawnerBlock;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class TrialSpawnerBlockEntity extends BlockEntity implements TrialSpawner.StateAccessor, Spawner {
/* 24 */   private final TrialSpawner trialSpawner = createDefaultSpawner();
/*    */ 
/*    */   
/* 27 */   public TrialSpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.TRIAL_SPAWNER, worldPosition, blockState); }
/*    */ 
/*    */   
/*    */   private TrialSpawner createDefaultSpawner() {
/* 31 */     PlayerDetector playerDetector = SharedConstants.DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : PlayerDetector.NO_CREATIVE_PLAYERS;
/* 32 */     PlayerDetector.EntitySelector entitySelector = PlayerDetector.EntitySelector.SELECT_FROM_LEVEL;
/* 33 */     return new TrialSpawner(TrialSpawner.FullConfig.DEFAULT, this, playerDetector, entitySelector);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loadAdditional(ValueInput input) {
/* 38 */     super.loadAdditional(input);
/*    */     
/* 40 */     this.trialSpawner.load(input);
/*    */     
/* 42 */     if (this.level != null) {
/* 43 */       markUpdated();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void saveAdditional(ValueOutput output) {
/* 49 */     super.saveAdditional(output);
/* 50 */     this.trialSpawner.store(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return this.trialSpawner.getStateData().getUpdateTag((TrialSpawnerState)getBlockState().getValue(TrialSpawnerBlock.STATE)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEntityId(EntityType<?> type, RandomSource random) {
/* 65 */     if (this.level == null) {
/* 66 */       Util.logAndPauseIfInIde("Expected non-null level");
/*    */       
/*    */       return;
/*    */     } 
/* 70 */     this.trialSpawner.overrideEntityToSpawn(type, this.level);
/* 71 */     setChanged();
/*    */   }
/*    */ 
/*    */   
/* 75 */   public TrialSpawner getTrialSpawner() { return this.trialSpawner; }
/*    */ 
/*    */ 
/*    */   
/*    */   public TrialSpawnerState getState() {
/* 80 */     if (!getBlockState().hasProperty(BlockStateProperties.TRIAL_SPAWNER_STATE)) {
/* 81 */       return TrialSpawnerState.INACTIVE;
/*    */     }
/* 83 */     return (TrialSpawnerState)getBlockState().getValue(BlockStateProperties.TRIAL_SPAWNER_STATE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setState(Level level, TrialSpawnerState state) {
/* 88 */     setChanged();
/* 89 */     level.setBlockAndUpdate(this.worldPosition, (BlockState)getBlockState().setValue(BlockStateProperties.TRIAL_SPAWNER_STATE, state));
/*    */   }
/*    */ 
/*    */   
/*    */   public void markUpdated() {
/* 94 */     setChanged();
/* 95 */     if (this.level != null)
/* 96 */       this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\TrialSpawnerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */