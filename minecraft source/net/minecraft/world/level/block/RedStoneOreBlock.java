/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class RedStoneOreBlock extends Block {
/*  24 */   public static final MapCodec<RedStoneOreBlock> CODEC = simpleCodec(RedStoneOreBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  28 */   public MapCodec<RedStoneOreBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  31 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*     */   
/*     */   public RedStoneOreBlock(BlockBehaviour.Properties properties) {
/*  34 */     super(properties);
/*  35 */     registerDefaultState((BlockState)defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
/*  40 */     interact(state, level, pos);
/*  41 */     super.attack(state, level, pos, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/*  46 */     if (!entity.isSteppingCarefully()) {
/*  47 */       interact(onState, level, pos);
/*     */     }
/*  49 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  54 */     if (level.isClientSide()) {
/*  55 */       spawnParticles(level, pos);
/*     */     } else {
/*  57 */       interact(state, level, pos);
/*     */     } 
/*     */ 
/*     */     
/*  61 */     if (itemStack.getItem() instanceof net.minecraft.world.item.BlockItem && (new BlockPlaceContext(player, hand, itemStack, hitResult)).canPlace()) {
/*  62 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  65 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static void interact(BlockState state, Level level, BlockPos pos) {
/*  69 */     spawnParticles(level, pos);
/*  70 */     if (!((Boolean)state.getValue(LIT)).booleanValue()) {
/*  71 */       level.setBlock(pos, (BlockState)state.setValue(LIT, Boolean.valueOf(true)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected boolean isRandomlyTicking(BlockState state) { return ((Boolean)state.getValue(LIT)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  82 */     if (((Boolean)state.getValue(LIT)).booleanValue()) {
/*  83 */       level.setBlock(pos, (BlockState)state.setValue(LIT, Boolean.valueOf(false)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/*  89 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/*  90 */     if (dropExperience) {
/*  91 */       tryDropExperience(level, pos, tool, UniformInt.of(1, 5));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  97 */     if (((Boolean)state.getValue(LIT)).booleanValue()) {
/*  98 */       spawnParticles(level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void spawnParticles(Level level, BlockPos pos) {
/* 103 */     double offset = 0.5625D;
/* 104 */     RandomSource random = level.random;
/* 105 */     for (Direction direction : Direction.values()) {
/* 106 */       BlockPos relative = pos.relative(direction);
/* 107 */       if (!level.getBlockState(relative).isSolidRender()) {
/*     */ 
/*     */ 
/*     */         
/* 111 */         Direction.Axis axis = direction.getAxis();
/* 112 */         double dx = (axis == Direction.Axis.X) ? (0.5D + 0.5625D * direction.getStepX()) : random.nextFloat();
/* 113 */         double dy = (axis == Direction.Axis.Y) ? (0.5D + 0.5625D * direction.getStepY()) : random.nextFloat();
/* 114 */         double dz = (axis == Direction.Axis.Z) ? (0.5D + 0.5625D * direction.getStepZ()) : random.nextFloat();
/*     */         
/* 116 */         level.addParticle(DustParticleOptions.REDSTONE, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 122 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LIT }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RedStoneOreBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */