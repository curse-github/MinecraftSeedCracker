/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SnifferEggBlock extends Block {
/*  28 */   public static final MapCodec<SnifferEggBlock> CODEC = simpleCodec(SnifferEggBlock::new);
/*     */   
/*     */   public static final int MAX_HATCH_LEVEL = 2;
/*     */   
/*  32 */   public MapCodec<SnifferEggBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
/*     */   
/*     */   private static final int REGULAR_HATCH_TIME_TICKS = 24000;
/*     */   
/*     */   private static final int BOOSTED_HATCH_TIME_TICKS = 12000;
/*     */   private static final int RANDOM_HATCH_OFFSET_TICKS = 300;
/*  42 */   private static final VoxelShape SHAPE = Block.column(14.0D, 12.0D, 0.0D, 16.0D);
/*     */   
/*     */   public SnifferEggBlock(BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*  46 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(HATCH, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { HATCH }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public int getHatchLevel(BlockState state) { return ((Integer)state.getValue(HATCH)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   private boolean isReadyToHatch(BlockState state) { return (getHatchLevel(state) == 2); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick(BlockState state, ServerLevel level, BlockPos position, RandomSource random) {
/*  69 */     if (!isReadyToHatch(state)) {
/*  70 */       level.playSound(null, position, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
/*  71 */       level.setBlock(position, (BlockState)state.setValue(HATCH, Integer.valueOf(getHatchLevel(state) + 1)), 2);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  76 */     level.playSound(null, position, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
/*  77 */     level.destroyBlock(position, false);
/*     */     
/*  79 */     Sniffer sniffer = (Sniffer)EntityType.SNIFFER.create(level, EntitySpawnReason.BREEDING);
/*  80 */     if (sniffer != null) {
/*  81 */       Vec3 spawnAt = position.getCenter();
/*     */       
/*  83 */       sniffer.setBaby(true);
/*  84 */       sniffer.snapTo(spawnAt.x(), spawnAt.y(), spawnAt.z(), Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
/*     */       
/*  86 */       level.addFreshEntity(sniffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  92 */     boolean boosted = hatchBoost(level, pos);
/*     */     
/*  94 */     if (!level.isClientSide() && boosted) {
/*  95 */       level.levelEvent(3009, pos, 0);
/*     */     }
/*     */     
/*  98 */     int hatchTime = boosted ? 12000 : 24000;
/*  99 */     int progressionTickDelay = hatchTime / 3;
/*     */     
/* 101 */     level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(state));
/* 102 */     level.scheduleTick(pos, this, progressionTickDelay + level.random.nextInt(300));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static boolean hatchBoost(BlockGetter level, BlockPos pos) { return level.getBlockState(pos.below()).is(BlockTags.SNIFFER_EGG_HATCH_BOOST); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SnifferEggBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */