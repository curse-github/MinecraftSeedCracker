/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class RarityFilter
/*    */   extends PlacementFilter
/*    */ {
/* 12 */   public static final MapCodec<RarityFilter> CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("chance").xmap(RarityFilter::new, c -> Integer.valueOf(c.chance));
/*    */   
/*    */   private final int chance;
/*    */ 
/*    */   
/* 17 */   private RarityFilter(int chance) { this.chance = chance; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static RarityFilter onAverageOnceEvery(int chance) { return new RarityFilter(chance); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) { return (random.nextFloat() < 1.0F / this.chance); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public PlacementModifierType<?> type() { return PlacementModifierType.RARITY_FILTER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\RarityFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */