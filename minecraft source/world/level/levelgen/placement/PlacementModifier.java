/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class PlacementModifier
/*    */ {
/* 11 */   public static final Codec<PlacementModifier> CODEC = BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.byNameCodec().dispatch(PlacementModifier::type, PlacementModifierType::codec);
/*    */   
/*    */   public abstract Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */   
/*    */   public abstract PlacementModifierType<?> type();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\PlacementModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */