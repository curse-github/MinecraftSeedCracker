/*   */ package net.minecraft.world.level.block;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*   */ 
/*   */ public abstract class MultifaceSpreadeableBlock extends MultifaceBlock {
/* 7 */   public MultifaceSpreadeableBlock(BlockBehaviour.Properties properties) { super(properties); }
/*   */   
/*   */   public abstract MapCodec<? extends MultifaceSpreadeableBlock> codec();
/*   */   
/*   */   public abstract MultifaceSpreader getSpreader();
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceSpreadeableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */