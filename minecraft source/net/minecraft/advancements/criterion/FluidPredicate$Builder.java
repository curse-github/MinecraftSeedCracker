/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 40 */   private Optional<HolderSet<Fluid>> fluids = Optional.empty();
/* 41 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public static Builder fluid() { return new Builder(); }
/*    */ 
/*    */   
/*    */   public Builder of(Fluid fluid) {
/* 51 */     this.fluids = Optional.of(HolderSet.direct(new Holder[] { fluid.builtInRegistryHolder() }));
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public Builder of(HolderSet<Fluid> fluids) {
/* 56 */     this.fluids = Optional.of(fluids);
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setProperties(StatePropertiesPredicate properties) {
/* 61 */     this.properties = Optional.of(properties);
/* 62 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 66 */   public FluidPredicate build() { return new FluidPredicate(this.fluids, this.properties); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\FluidPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */