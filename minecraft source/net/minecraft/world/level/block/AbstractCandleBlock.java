/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class AbstractCandleBlock
/*    */   extends Block {
/*    */   public static final int LIGHT_PER_CANDLE = 3;
/* 29 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected AbstractCandleBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static boolean isLit(BlockState state) { return (state.hasProperty(LIT) && (state.is(BlockTags.CANDLES) || state.is(BlockTags.CANDLE_CAKES)) && ((Boolean)state.getValue(LIT)).booleanValue()); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 46 */     if (!level.isClientSide() && projectile.isOnFire() && canBeLit(state)) {
/* 47 */       setLit(level, state, blockHit.getBlockPos(), true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 52 */   protected boolean canBeLit(BlockState state) { return !((Boolean)state.getValue(LIT)).booleanValue(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 57 */     if (!((Boolean)state.getValue(LIT)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 61 */     getParticleOffsets(state).forEach(particlePos -> addParticlesAndSound(level, particlePos.add(pos.getX(), pos.getY(), pos.getZ()), random));
/*    */   }
/*    */   
/*    */   private static void addParticlesAndSound(Level level, Vec3 pos, RandomSource random) {
/* 65 */     float chance = random.nextFloat();
/* 66 */     if (chance < 0.3F) {
/* 67 */       level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
/* 68 */       if (chance < 0.17F) {
/* 69 */         level.playLocalSound(pos.x + 0.5D, pos.y + 0.5D, pos.z + 0.5D, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
/*    */       }
/*    */     } 
/* 72 */     level.addParticle(ParticleTypes.SMALL_FLAME, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */   
/*    */   public static void extinguish(Player player, BlockState state, LevelAccessor level, BlockPos pos) {
/* 76 */     setLit(level, state, pos, false);
/* 77 */     if (state.getBlock() instanceof AbstractCandleBlock) {
/* 78 */       ((AbstractCandleBlock)state.getBlock()).getParticleOffsets(state).forEach(particlePos -> level.addParticle(ParticleTypes.SMOKE, pos.getX() + particlePos.x(), pos.getY() + particlePos.y(), pos.getZ() + particlePos.z(), 0.0D, 0.10000000149011612D, 0.0D));
/*    */     }
/* 80 */     level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 81 */     level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/*    */   }
/*    */ 
/*    */   
/* 85 */   private static void setLit(LevelAccessor level, BlockState state, BlockPos pos, boolean lit) { level.setBlock(pos, (BlockState)state.setValue(LIT, Boolean.valueOf(lit)), 11); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 90 */     if (explosion.canTriggerBlocks() && ((Boolean)state.getValue(LIT)).booleanValue()) {
/* 91 */       extinguish(null, state, level, pos);
/*    */     }
/* 93 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractCandleBlock> codec();
/*    */   
/*    */   protected abstract Iterable<Vec3> getParticleOffsets(BlockState paramBlockState);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AbstractCandleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */