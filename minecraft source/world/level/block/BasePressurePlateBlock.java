/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BasePressurePlateBlock
/*     */   extends Block
/*     */ {
/*  27 */   private static final VoxelShape SHAPE_PRESSED = Block.column(14.0D, 0.0D, 0.5D);
/*  28 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 1.0D);
/*  29 */   protected static final AABB TOUCH_AABB = (AABB)Block.column(14.0D, 0.0D, 4.0D).toAabbs().getFirst();
/*     */   
/*     */   protected final BlockSetType type;
/*     */   
/*     */   protected BasePressurePlateBlock(BlockBehaviour.Properties properties, BlockSetType type) {
/*  34 */     super(properties.sound(type.soundType()));
/*  35 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends BasePressurePlateBlock> codec();
/*     */ 
/*     */   
/*  43 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (getSignalForState(state) > 0) ? SHAPE_PRESSED : SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected int getPressedTime() { return 20; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public boolean isPossibleToRespawnInThis(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  57 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/*  58 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  60 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  65 */     BlockPos below = pos.below();
/*  66 */     return (canSupportRigidBlock(level, below) || canSupportCenter(level, below, Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  71 */     int signal = getSignalForState(state);
/*  72 */     if (signal > 0) {
/*  73 */       checkPressed(null, level, pos, state, signal);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  79 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     int signal = getSignalForState(state);
/*  84 */     if (signal == 0) {
/*  85 */       checkPressed(entity, level, pos, state, signal);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkPressed(Entity sourceEntity, Level level, BlockPos pos, BlockState state, int oldSignal) {
/*  90 */     int signal = getSignalStrength(level, pos);
/*  91 */     boolean wasPressed = (oldSignal > 0);
/*  92 */     boolean isPressed = (signal > 0);
/*     */     
/*  94 */     if (oldSignal != signal) {
/*  95 */       BlockState newState = setSignalForState(state, signal);
/*  96 */       level.setBlock(pos, newState, 2);
/*  97 */       updateNeighbours(level, pos);
/*  98 */       level.setBlocksDirty(pos, state, newState);
/*     */     } 
/*     */     
/* 101 */     if (!isPressed && wasPressed) {
/* 102 */       level.playSound(null, pos, this.type.pressurePlateClickOff(), SoundSource.BLOCKS);
/* 103 */       level.gameEvent(sourceEntity, GameEvent.BLOCK_DEACTIVATE, pos);
/* 104 */     } else if (isPressed && !wasPressed) {
/* 105 */       level.playSound(null, pos, this.type.pressurePlateClickOn(), SoundSource.BLOCKS);
/* 106 */       level.gameEvent(sourceEntity, GameEvent.BLOCK_ACTIVATE, pos);
/*     */     } 
/*     */     
/* 109 */     if (isPressed) {
/* 110 */       level.scheduleTick(new BlockPos(pos), this, getPressedTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 116 */     if (!movedByPiston && getSignalForState(state) > 0) {
/* 117 */       updateNeighbours(level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighbours(Level level, BlockPos pos) {
/* 122 */     level.updateNeighborsAt(pos, this);
/* 123 */     level.updateNeighborsAt(pos.below(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return getSignalForState(state); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 133 */     if (direction == Direction.UP) {
/* 134 */       return getSignalForState(state);
/*     */     }
/*     */     
/* 137 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected static int getEntityCount(Level level, AABB entityDetectionBox, Class<? extends Entity> entityClass) { return level.getEntitiesOfClass(entityClass, entityDetectionBox, EntitySelector.NO_SPECTATORS.and(e -> !e.isIgnoringBlockTriggers())).size(); }
/*     */   
/*     */   protected abstract int getSignalStrength(Level paramLevel, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract int getSignalForState(BlockState paramBlockState);
/*     */   
/*     */   protected abstract BlockState setSignalForState(BlockState paramBlockState, int paramInt);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BasePressurePlateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */