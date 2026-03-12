/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*    */ 
/*    */ public class HeightRangePlacement
/*    */   extends PlacementModifier
/*    */ {
/* 18 */   public static final MapCodec<HeightRangePlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(HeightProvider.CODEC
/* 19 */         .fieldOf("height").forGetter(()))
/* 20 */       .apply(i, HeightRangePlacement::new));
/*    */   
/*    */   private final HeightProvider height;
/*    */ 
/*    */   
/* 25 */   private HeightRangePlacement(HeightProvider height) { this.height = height; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static HeightRangePlacement of(HeightProvider height) { return new HeightRangePlacement(height); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static HeightRangePlacement uniform(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) { return of(UniformHeight.of(minInclusive, maxInclusive)); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static HeightRangePlacement triangle(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) { return of(TrapezoidHeight.of(minInclusive, maxInclusive)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) { return Stream.of(origin.atY(this.height.sample(random, context))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public PlacementModifierType<?> type() { return PlacementModifierType.HEIGHT_RANGE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\HeightRangePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */