/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class MatchingFluidsPredicate extends StateTestingPredicate {
/* 15 */   public static final MapCodec<MatchingFluidsPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and(
/* 16 */         RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(()))
/* 17 */       .apply(i, MatchingFluidsPredicate::new)); private final HolderSet<Fluid> fluids;
/*    */   
/*    */   public MatchingFluidsPredicate(Vec3i offset, HolderSet<Fluid> fluids) {
/* 20 */     super(offset);
/* 21 */     this.fluids = fluids;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected boolean test(BlockState state) { return state.getFluidState().is(this.fluids); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public BlockPredicateType<?> type() { return BlockPredicateType.MATCHING_FLUIDS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingFluidsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */