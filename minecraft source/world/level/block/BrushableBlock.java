/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BrushableBlock extends BaseEntityBlock implements Fallable {
/*  28 */   public static final MapCodec<BrushableBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/*  29 */         .byNameCodec().fieldOf("turns_into").forGetter(BrushableBlock::getTurnsInto), BuiltInRegistries.SOUND_EVENT
/*  30 */         .byNameCodec().fieldOf("brush_sound").forGetter(BrushableBlock::getBrushSound), BuiltInRegistries.SOUND_EVENT
/*  31 */         .byNameCodec().fieldOf("brush_completed_sound").forGetter(BrushableBlock::getBrushCompletedSound), 
/*  32 */         propertiesCodec())
/*  33 */       .apply(i, BrushableBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  37 */   public MapCodec<BrushableBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  40 */   private static final IntegerProperty DUSTED = BlockStateProperties.DUSTED;
/*     */   
/*     */   public static final int TICK_DELAY = 2;
/*     */   private final Block turnsInto;
/*     */   private final SoundEvent brushSound;
/*     */   private final SoundEvent brushCompletedSound;
/*     */   
/*     */   public BrushableBlock(Block turnsInto, SoundEvent brushSound, SoundEvent brushCompletedSound, BlockBehaviour.Properties properties) {
/*  48 */     super(properties);
/*  49 */     this.turnsInto = turnsInto;
/*  50 */     this.brushSound = brushSound;
/*  51 */     this.brushCompletedSound = brushCompletedSound;
/*  52 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(DUSTED, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { DUSTED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { level.scheduleTick(pos, this, 2); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  67 */     ticks.scheduleTick(pos, this, 2);
/*     */     
/*  69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  74 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BrushableBlockEntity) { BrushableBlockEntity brushableBlockEntity = (BrushableBlockEntity)blockEntity;
/*  75 */       brushableBlockEntity.checkReset(level); }
/*     */ 
/*     */     
/*  78 */     if (!FallingBlock.isFree(level.getBlockState(pos.below())) || pos.getY() < level.getMinY()) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, state);
/*  83 */     entity.disableDrop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
/*  92 */     Vec3 centerOfEntity = entity.getBoundingBox().getCenter();
/*  93 */     level.levelEvent(2001, BlockPos.containing(centerOfEntity), Block.getId(entity.getBlockState()));
/*  94 */     level.gameEvent(entity, GameEvent.BLOCK_DESTROY, centerOfEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  99 */     if (random.nextInt(16) == 0) {
/* 100 */       BlockPos below = pos.below();
/*     */       
/* 102 */       if (FallingBlock.isFree(level.getBlockState(below))) {
/* 103 */         double xx = pos.getX() + random.nextDouble();
/* 104 */         double yy = pos.getY() - 0.05D;
/* 105 */         double zz = pos.getZ() + random.nextDouble();
/*     */         
/* 107 */         level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new BrushableBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public Block getTurnsInto() { return this.turnsInto; }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public SoundEvent getBrushSound() { return this.brushSound; }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public SoundEvent getBrushCompletedSound() { return this.brushCompletedSound; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BrushableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */