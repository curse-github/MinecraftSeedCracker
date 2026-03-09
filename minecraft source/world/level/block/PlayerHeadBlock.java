/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class PlayerHeadBlock extends SkullBlock {
/*  6 */   public static final MapCodec<PlayerHeadBlock> CODEC = simpleCodec(PlayerHeadBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 10 */   public MapCodec<PlayerHeadBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected PlayerHeadBlock(BlockBehaviour.Properties properties) { super(SkullBlock.Types.PLAYER, properties); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PlayerHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */