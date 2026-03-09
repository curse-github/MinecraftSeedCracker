/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class FlatLayerInfo {
/* 12 */   public static final Codec<FlatLayerInfo> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 13 */         Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(FlatLayerInfo::getHeight), BuiltInRegistries.BLOCK
/* 14 */         .byNameCodec().fieldOf("block").orElse(Blocks.AIR).forGetter(()))
/* 15 */       .apply(i, FlatLayerInfo::new));
/*    */   
/*    */   private final Block block;
/*    */   private final int height;
/*    */   
/*    */   public FlatLayerInfo(int height, Block block) {
/* 21 */     this.height = height;
/* 22 */     this.block = block;
/*    */   }
/*    */ 
/*    */   
/* 26 */   public int getHeight() { return this.height; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public BlockState getBlockState() { return this.block.defaultBlockState(); }
/*    */ 
/*    */   
/*    */   public FlatLayerInfo heightLimited(int maxHeight) {
/* 34 */     if (this.height > maxHeight) {
/* 35 */       return new FlatLayerInfo(maxHeight, this.block);
/*    */     }
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public String toString() { return ((this.height != 1) ? ("" + this.height + "*") : "") + ((this.height != 1) ? ("" + this.height + "*") : ""); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\flat\FlatLayerInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */