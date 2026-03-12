/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleMaps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpatialAttributeInterpolator
/*    */ {
/* 16 */   private final Reference2DoubleArrayMap<EnvironmentAttributeMap> weightsBySource = new Reference2DoubleArrayMap();
/*    */ 
/*    */   
/* 19 */   public void clear() { this.weightsBySource.clear(); }
/*    */ 
/*    */   
/*    */   public SpatialAttributeInterpolator accumulate(double weight, EnvironmentAttributeMap attributes) {
/* 23 */     this.weightsBySource.mergeDouble(attributes, weight, Double::sum);
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public <Value> Value applyAttributeLayer(EnvironmentAttribute<Value> attribute, Value baseValue) {
/* 28 */     if (this.weightsBySource.isEmpty())
/* 29 */       return baseValue; 
/* 30 */     if (this.weightsBySource.size() == 1) {
/* 31 */       EnvironmentAttributeMap sourceAttributes = (EnvironmentAttributeMap)this.weightsBySource.keySet().iterator().next();
/* 32 */       return (Value)sourceAttributes.applyModifier(attribute, baseValue);
/*    */     } 
/*    */     
/* 35 */     LerpFunction<Value> lerp = attribute.type().spatialLerp();
/* 36 */     Value resultValue = null;
/*    */ 
/*    */     
/* 39 */     double accumulatedWeight = 0.0D;
/* 40 */     for (ObjectIterator objectIterator = Reference2DoubleMaps.fastIterable(this.weightsBySource).iterator(); objectIterator.hasNext(); ) { Reference2DoubleMap.Entry<EnvironmentAttributeMap> entry = (Reference2DoubleMap.Entry)objectIterator.next();
/* 41 */       EnvironmentAttributeMap sourceAttributes = (EnvironmentAttributeMap)entry.getKey();
/* 42 */       double sourceWeight = entry.getDoubleValue();
/* 43 */       Value sourceValue = (Value)sourceAttributes.applyModifier(attribute, baseValue);
/* 44 */       accumulatedWeight += sourceWeight;
/* 45 */       if (resultValue == null) {
/* 46 */         resultValue = sourceValue; continue;
/*    */       } 
/* 48 */       float relativeFraction = (float)(sourceWeight / accumulatedWeight);
/* 49 */       resultValue = (Value)lerp.apply(relativeFraction, resultValue, sourceValue); }
/*    */ 
/*    */ 
/*    */     
/* 53 */     return (Value)Objects.requireNonNull(resultValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\SpatialAttributeInterpolator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */