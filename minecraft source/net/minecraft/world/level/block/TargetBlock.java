/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TargetBlock extends Block {
/*  28 */   public static final MapCodec<TargetBlock> CODEC = simpleCodec(TargetBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  32 */   public MapCodec<TargetBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  35 */   private static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;
/*     */   
/*     */   private static final int ACTIVATION_TICKS_ARROWS = 20;
/*     */   private static final int ACTIVATION_TICKS_OTHER = 8;
/*     */   
/*     */   public TargetBlock(BlockBehaviour.Properties properties) {
/*  41 */     super(properties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(OUTPUT_POWER, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
/*  47 */     int outputStrength = updateRedstoneOutput(level, state, hitResult, projectile);
/*     */     
/*  49 */     Entity owner = projectile.getOwner();
/*  50 */     if (owner instanceof ServerPlayer) { ServerPlayer playerOwner = (ServerPlayer)owner;
/*  51 */       playerOwner.awardStat(Stats.TARGET_HIT);
/*  52 */       CriteriaTriggers.TARGET_BLOCK_HIT.trigger(playerOwner, projectile, hitResult.getLocation(), outputStrength); }
/*     */   
/*     */   }
/*     */   
/*     */   private static int updateRedstoneOutput(LevelAccessor level, BlockState state, BlockHitResult hitResult, Entity entity) {
/*  57 */     int redstoneStrength = getRedstoneStrength(hitResult, hitResult.getLocation());
/*  58 */     int duration = (entity instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow) ? 20 : 8;
/*     */     
/*  60 */     if (!level.getBlockTicks().hasScheduledTick(hitResult.getBlockPos(), state.getBlock())) {
/*  61 */       setOutputPower(level, state, redstoneStrength, hitResult.getBlockPos(), duration);
/*     */     }
/*     */     
/*  64 */     return redstoneStrength;
/*     */   }
/*     */   private static int getRedstoneStrength(BlockHitResult hitResult, Vec3 hitLocation) {
/*     */     double distance;
/*  68 */     Direction hitDirection = hitResult.getDirection();
/*  69 */     double distX = Math.abs(Mth.frac(hitLocation.x) - 0.5D);
/*  70 */     double distY = Math.abs(Mth.frac(hitLocation.y) - 0.5D);
/*  71 */     double distZ = Math.abs(Mth.frac(hitLocation.z) - 0.5D);
/*     */ 
/*     */     
/*  74 */     Direction.Axis axis = hitDirection.getAxis();
/*  75 */     if (axis == Direction.Axis.Y) {
/*  76 */       distance = Math.max(distX, distZ);
/*  77 */     } else if (axis == Direction.Axis.Z) {
/*  78 */       distance = Math.max(distX, distY);
/*     */     } else {
/*  80 */       distance = Math.max(distY, distZ);
/*     */     } 
/*     */     
/*  83 */     return Math.max(1, Mth.ceil(15.0D * Mth.clamp((0.5D - distance) / 0.5D, 0.0D, 1.0D)));
/*     */   }
/*     */   
/*     */   private static void setOutputPower(LevelAccessor level, BlockState state, int outputStrength, BlockPos pos, int duration) {
/*  87 */     level.setBlock(pos, (BlockState)state.setValue(OUTPUT_POWER, Integer.valueOf(outputStrength)), 3);
/*  88 */     level.scheduleTick(pos, state.getBlock(), duration);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  93 */     if (((Integer)state.getValue(OUTPUT_POWER)).intValue() != 0) {
/*  94 */       level.setBlock(pos, (BlockState)state.setValue(OUTPUT_POWER, Integer.valueOf(0)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Integer)state.getValue(OUTPUT_POWER)).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { OUTPUT_POWER }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 115 */     if (level.isClientSide() || state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 119 */     if (((Integer)state.getValue(OUTPUT_POWER)).intValue() > 0 && !level.getBlockTicks().hasScheduledTick(pos, this))
/* 120 */       level.setBlock(pos, (BlockState)state.setValue(OUTPUT_POWER, Integer.valueOf(0)), 18); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TargetBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */