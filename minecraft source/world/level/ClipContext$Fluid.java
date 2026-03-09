/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Fluid
/*     */ {
/* 102 */   NONE(state -> false),
/* 103 */   SOURCE_ONLY(FluidState::isSource),
/* 104 */   ANY(state -> !state.isEmpty()),
/* 105 */   WATER(fluidState -> fluidState.is(FluidTags.WATER));
/*     */ 
/*     */   
/*     */   private final Predicate<FluidState> canPick;
/*     */ 
/*     */   
/* 111 */   Fluid(Predicate<FluidState> canPick) { this.canPick = canPick; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean canPick(FluidState fluidState) { return this.canPick.test(fluidState); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ClipContext$Fluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */