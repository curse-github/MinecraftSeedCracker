/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WitherWallSkullBlock extends WallSkullBlock {
/* 12 */   public static final MapCodec<WitherWallSkullBlock> CODEC = simpleCodec(WitherWallSkullBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public MapCodec<WitherWallSkullBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected WitherWallSkullBlock(BlockBehaviour.Properties properties) { super(SkullBlock.Types.WITHER_SKELETON, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) { WitherSkullBlock.checkSpawn(level, pos); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WitherWallSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */