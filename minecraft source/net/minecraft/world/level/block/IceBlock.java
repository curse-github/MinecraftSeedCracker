/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class IceBlock extends HalfTransparentBlock {
/* 19 */   public static final MapCodec<IceBlock> CODEC = simpleCodec(IceBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<? extends IceBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public IceBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static BlockState meltsInto() { return Blocks.WATER.defaultBlockState(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) {
/* 36 */     super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
/*    */     
/* 38 */     if (!EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_ICE_MELTING)) {
/* 39 */       if (((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)).booleanValue()) {
/* 40 */         level.removeBlock(pos, false);
/*    */         
/*    */         return;
/*    */       } 
/* 44 */       BlockState belowState = level.getBlockState(pos.below());
/* 45 */       if (belowState.blocksMotion() || belowState.liquid()) {
/* 46 */         level.setBlockAndUpdate(pos, meltsInto());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 53 */     if (level.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock()) {
/* 54 */       melt(state, level, pos);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void melt(BlockState state, Level level, BlockPos pos) {
/* 59 */     if (((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)).booleanValue()) {
/* 60 */       level.removeBlock(pos, false);
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     level.setBlockAndUpdate(pos, meltsInto());
/* 65 */     level.neighborChanged(pos, meltsInto().getBlock(), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\IceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */