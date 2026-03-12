/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class AmethystBlock extends Block {
/* 13 */   public static final MapCodec<AmethystBlock> CODEC = simpleCodec(AmethystBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<? extends AmethystBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public AmethystBlock(BlockBehaviour.Properties props) { super(props); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
/* 26 */     if (!level.isClientSide()) {
/* 27 */       BlockPos hitPos = hitResult.getBlockPos();
/* 28 */       level.playSound(null, hitPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.5F + level.random.nextFloat() * 1.2F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AmethystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */