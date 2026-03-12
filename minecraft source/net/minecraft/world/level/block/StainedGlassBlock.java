/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class StainedGlassBlock extends TransparentBlock implements BeaconBeamBlock {
/*  8 */   public static final MapCodec<StainedGlassBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DyeColor.CODEC
/*  9 */         .fieldOf("color").forGetter(StainedGlassBlock::getColor), 
/* 10 */         propertiesCodec())
/* 11 */       .apply(i, StainedGlassBlock::new));
/*    */   
/*    */   private final DyeColor color;
/*    */   
/* 15 */   public MapCodec<StainedGlassBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public StainedGlassBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 20 */     super(properties);
/* 21 */     this.color = color;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public DyeColor getColor() { return this.color; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StainedGlassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */