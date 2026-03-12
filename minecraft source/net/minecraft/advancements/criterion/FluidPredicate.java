/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public final class FluidPredicate extends Record {
/*    */   private final Optional<HolderSet<Fluid>> fluids;
/*    */   private final Optional<StatePropertiesPredicate> properties;
/*    */   
/* 15 */   public FluidPredicate(Optional<HolderSet<Fluid>> fluids, Optional<StatePropertiesPredicate> properties) { this.fluids = fluids; this.properties = properties; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/FluidPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/FluidPredicate; } public Optional<HolderSet<Fluid>> fluids() { return this.fluids; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/FluidPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/FluidPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/FluidPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/FluidPredicate;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<StatePropertiesPredicate> properties() { return this.properties; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final Codec<FluidPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 20 */         RegistryCodecs.homogeneousList(Registries.FLUID).optionalFieldOf("fluids").forGetter(FluidPredicate::fluids), StatePropertiesPredicate.CODEC
/* 21 */         .optionalFieldOf("state").forGetter(FluidPredicate::properties))
/* 22 */       .apply(i, FluidPredicate::new));
/*    */   
/*    */   public boolean matches(ServerLevel level, BlockPos pos) {
/* 25 */     if (!level.isLoaded(pos)) {
/* 26 */       return false;
/*    */     }
/* 28 */     FluidState state = level.getFluidState(pos);
/*    */     
/* 30 */     if (this.fluids.isPresent() && !state.is((HolderSet)this.fluids.get())) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (this.properties.isPresent() && !((StatePropertiesPredicate)this.properties.get()).matches(state)) {
/* 34 */       return false;
/*    */     }
/* 36 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 40 */     private Optional<HolderSet<Fluid>> fluids = Optional.empty();
/* 41 */     private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     public static Builder fluid() { return new Builder(); }
/*    */ 
/*    */     
/*    */     public Builder of(Fluid fluid) {
/* 51 */       this.fluids = Optional.of(HolderSet.direct(new Holder[] { fluid.builtInRegistryHolder() }));
/* 52 */       return this;
/*    */     }
/*    */     
/*    */     public Builder of(HolderSet<Fluid> fluids) {
/* 56 */       this.fluids = Optional.of(fluids);
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setProperties(StatePropertiesPredicate properties) {
/* 61 */       this.properties = Optional.of(properties);
/* 62 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 66 */     public FluidPredicate build() { return new FluidPredicate(this.fluids, this.properties); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\FluidPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */