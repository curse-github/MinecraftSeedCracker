/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ 
/*     */ public class WitherSkullBlock extends SkullBlock {
/*  26 */   public static final MapCodec<WitherSkullBlock> CODEC = simpleCodec(WitherSkullBlock::new);
/*     */   private static BlockPattern witherPatternFull;
/*     */   private static BlockPattern witherPatternBase;
/*     */   
/*  30 */   public MapCodec<WitherSkullBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   protected WitherSkullBlock(BlockBehaviour.Properties properties) { super(SkullBlock.Types.WITHER_SKELETON, properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) { checkSpawn(level, pos); }
/*     */ 
/*     */   
/*     */   public static void checkSpawn(Level level, BlockPos pos) {
/*  47 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SkullBlockEntity) { SkullBlockEntity placedSkull = (SkullBlockEntity)blockEntity;
/*  48 */       checkSpawn(level, pos, placedSkull); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void checkSpawn(Level level, BlockPos pos, SkullBlockEntity placedSkull) {
/*  53 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*  56 */     BlockState blockState = placedSkull.getBlockState();
/*  57 */     boolean correctBlock = (blockState.is(Blocks.WITHER_SKELETON_SKULL) || blockState.is(Blocks.WITHER_SKELETON_WALL_SKULL));
/*  58 */     if (!correctBlock || pos.getY() < level.getMinY() || level.getDifficulty() == Difficulty.PEACEFUL) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     BlockPattern.BlockPatternMatch match = getOrCreateWitherFull().find(level, pos);
/*  63 */     if (match == null) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     WitherBoss witherBoss = (WitherBoss)EntityType.WITHER.create(level, EntitySpawnReason.TRIGGERED);
/*  68 */     if (witherBoss != null) {
/*  69 */       CarvedPumpkinBlock.clearPatternBlocks(level, match);
/*     */       
/*  71 */       BlockPos spawnPos = match.getBlock(1, 2, 0).getPos();
/*  72 */       witherBoss.snapTo(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.55D, spawnPos.getZ() + 0.5D, (match.getForwards().getAxis() == Direction.Axis.X) ? 0.0F : 90.0F, 0.0F);
/*  73 */       witherBoss.yBodyRot = (match.getForwards().getAxis() == Direction.Axis.X) ? 0.0F : 90.0F;
/*  74 */       witherBoss.makeInvulnerable();
/*     */       
/*  76 */       for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, witherBoss.getBoundingBox().inflate(50.0D))) {
/*  77 */         CriteriaTriggers.SUMMONED_ENTITY.trigger(player, witherBoss);
/*     */       }
/*     */       
/*  80 */       level.addFreshEntity(witherBoss);
/*     */       
/*  82 */       CarvedPumpkinBlock.updatePatternBlocks(level, match);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean canSpawnMob(Level level, BlockPos pos, ItemStack itemStack) {
/*  87 */     if (itemStack.is(Items.WITHER_SKELETON_SKULL) && pos.getY() >= level.getMinY() + 2 && level.getDifficulty() != Difficulty.PEACEFUL && !level.isClientSide()) {
/*  88 */       return (getOrCreateWitherBase().find(level, pos) != null);
/*     */     }
/*     */     
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockPattern getOrCreateWitherFull() {
/*  95 */     if (witherPatternFull == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       witherPatternFull = BlockPatternBuilder.start().aisle(new String[] { "^^^", "###", "~#~" }).where('#', block -> block.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', block -> block.getState().isAir()).build();
/*     */     }
/*     */     
/* 110 */     return witherPatternFull;
/*     */   }
/*     */   
/*     */   private static BlockPattern getOrCreateWitherBase() {
/* 114 */     if (witherPatternBase == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       witherPatternBase = BlockPatternBuilder.start().aisle(new String[] { "   ", "###", "~#~" }).where('#', block -> block.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('~', block -> block.getState().isAir()).build();
/*     */     }
/*     */     
/* 126 */     return witherPatternBase;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WitherSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */