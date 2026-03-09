/*    */ package net.minecraft.world.attribute;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EnvironmentAttributeProbe {
/*    */   private final Map<EnvironmentAttribute<?>, ValueProbe<?>> valueProbes;
/*    */   private final Function<EnvironmentAttribute<?>, ValueProbe<?>> valueProbeFactory;
/*    */   
/*    */   public EnvironmentAttributeProbe() {
/* 13 */     this.valueProbes = new Reference2ObjectOpenHashMap();
/*    */ 
/*    */     
/* 16 */     this.valueProbeFactory = (x$0 -> new ValueProbe(x$0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     this.biomeInterpolator = new SpatialAttributeInterpolator();
/*    */   } private Level level; private Vec3 position; private final SpatialAttributeInterpolator biomeInterpolator;
/*    */   public void reset() {
/* 24 */     this.level = null;
/* 25 */     this.position = null;
/* 26 */     this.biomeInterpolator.clear();
/* 27 */     this.valueProbes.clear();
/*    */   }
/*    */   
/*    */   public void tick(Level level, Vec3 position) {
/* 31 */     this.level = level;
/* 32 */     this.position = position;
/* 33 */     this.valueProbes.values().removeIf(ValueProbe::tick);
/*    */     
/* 35 */     this.biomeInterpolator.clear();
/*    */ 
/*    */     
/* 38 */     Objects.requireNonNull(level.getBiomeManager()); GaussianSampler.sample(position.scale(0.25D), level.getBiomeManager()::getNoiseBiomeAtQuart, (weight, biome) -> 
/* 39 */         this.biomeInterpolator.accumulate(weight, ((Biome)biome.value()).getAttributes()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <Value> Value getValue(EnvironmentAttribute<Value> attribute, float partialTicks) {
/* 45 */     ValueProbe<Value> valueProbe = (ValueProbe)this.valueProbes.computeIfAbsent(attribute, this.valueProbeFactory);
/* 46 */     return (Value)valueProbe.get(attribute, partialTicks);
/*    */   }
/*    */   
/*    */   private class ValueProbe<Value> extends Object {
/*    */     private Value lastValue;
/*    */     private Value newValue;
/*    */     
/*    */     public ValueProbe(EnvironmentAttribute<Value> attribute) {
/* 54 */       Value value = (Value)getValueFromLevel(attribute);
/* 55 */       this.lastValue = value;
/* 56 */       this.newValue = value;
/*    */     }
/*    */     
/*    */     private Value getValueFromLevel(EnvironmentAttribute<Value> attribute) {
/* 60 */       if (EnvironmentAttributeProbe.this.level == null || EnvironmentAttributeProbe.this.position == null) {
/* 61 */         return (Value)attribute.defaultValue();
/*    */       }
/* 63 */       return (Value)EnvironmentAttributeProbe.this.level.environmentAttributes().getValue(attribute, EnvironmentAttributeProbe.this.position, EnvironmentAttributeProbe.this.biomeInterpolator);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean tick() {
/* 68 */       if (this.newValue == null) {
/* 69 */         return true;
/*    */       }
/* 71 */       this.lastValue = this.newValue;
/* 72 */       this.newValue = null;
/* 73 */       return false;
/*    */     }
/*    */     
/*    */     public Value get(EnvironmentAttribute<Value> attribute, float partialTicks) {
/* 77 */       if (this.newValue == null) {
/* 78 */         this.newValue = getValueFromLevel(attribute);
/*    */       }
/* 80 */       return (Value)attribute.type().partialTickLerp().apply(partialTicks, this.lastValue, this.newValue);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeProbe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */