/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class ColoredFallingBlock extends FallingBlock {
/* 11 */   public static final MapCodec<ColoredFallingBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ColorRGBA.CODEC
/* 12 */         .fieldOf("falling_dust_color").forGetter(()), 
/* 13 */         propertiesCodec())
/* 14 */       .apply(i, ColoredFallingBlock::new));
/*    */   
/*    */   protected final ColorRGBA dustColor;
/*    */   
/* 18 */   public MapCodec<? extends ColoredFallingBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ColoredFallingBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/* 25 */     this.dustColor = dustColor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) { return this.dustColor.rgba(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ColoredFallingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */