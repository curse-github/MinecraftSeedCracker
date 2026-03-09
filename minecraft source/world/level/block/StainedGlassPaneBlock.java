/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class StainedGlassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {
/*  8 */   public static final MapCodec<StainedGlassPaneBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DyeColor.CODEC
/*  9 */         .fieldOf("color").forGetter(StainedGlassPaneBlock::getColor), 
/* 10 */         propertiesCodec())
/* 11 */       .apply(i, StainedGlassPaneBlock::new));
/*    */   
/*    */   private final DyeColor color;
/*    */   
/* 15 */   public MapCodec<StainedGlassPaneBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StainedGlassPaneBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/* 22 */     this.color = color;
/* 23 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(NORTH, Boolean.valueOf(false))).setValue(EAST, Boolean.valueOf(false))).setValue(SOUTH, Boolean.valueOf(false))).setValue(WEST, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public DyeColor getColor() { return this.color; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StainedGlassPaneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */