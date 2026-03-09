/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TintedGlassBlock extends TransparentBlock {
/*  8 */   public static final MapCodec<TintedGlassBlock> CODEC = simpleCodec(TintedGlassBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 12 */   public MapCodec<TintedGlassBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 15 */   public TintedGlassBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected boolean propagatesSkylightDown(BlockState state) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected int getLightBlock(BlockState state) { return 15; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TintedGlassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */