/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SandBlock extends ColoredFallingBlock {
/* 13 */   public static final MapCodec<SandBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ColorRGBA.CODEC
/* 14 */         .fieldOf("falling_dust_color").forGetter(()), 
/* 15 */         propertiesCodec())
/* 16 */       .apply(i, SandBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 20 */   public MapCodec<SandBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public SandBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) { super(dustColor, properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 29 */     super.animateTick(state, level, pos, random);
/* 30 */     AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */