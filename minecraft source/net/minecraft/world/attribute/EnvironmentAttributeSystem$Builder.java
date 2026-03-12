/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.timeline.Timeline;
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
/*     */ public class Builder
/*     */ {
/* 136 */   private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder addDefaultLayers(Level level) {
/* 142 */     EnvironmentAttributeSystem.addDefaultLayers(this, level);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addConstantLayer(EnvironmentAttributeMap attributeMap) {
/* 147 */     for (EnvironmentAttribute<?> attribute : attributeMap.keySet()) {
/* 148 */       addConstantEntry(attribute, attributeMap);
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   private <Value> Builder addConstantEntry(EnvironmentAttribute<Value> attribute, EnvironmentAttributeMap attributeMap) {
/* 154 */     EnvironmentAttributeMap.Entry<Value, ?> entry = attributeMap.get(attribute);
/* 155 */     if (entry == null) {
/* 156 */       throw new IllegalArgumentException("Missing attribute " + String.valueOf(attribute));
/*     */     }
/* 158 */     Objects.requireNonNull(entry); return addConstantLayer(attribute, entry::applyModifier);
/*     */   }
/*     */ 
/*     */   
/* 162 */   public <Value> Builder addConstantLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Constant<Value> layer) { return addLayer(attribute, layer); }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public <Value> Builder addTimeBasedLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.TimeBased<Value> layer) { return addLayer(attribute, layer); }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public <Value> Builder addPositionalLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Positional<Value> layer) { return addLayer(attribute, layer); }
/*     */ 
/*     */   
/*     */   private <Value> Builder addLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer<Value> layer) {
/* 174 */     ((List)this.layersByAttribute.computeIfAbsent(attribute, t -> new ArrayList())).add(layer);
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addTimelineLayer(Holder<Timeline> timeline, LongSupplier dayTimeGetter) {
/* 179 */     for (EnvironmentAttribute<?> attribute : ((Timeline)timeline.value()).attributes()) {
/* 180 */       addTimelineLayerForAttribute(timeline, attribute, dayTimeGetter);
/*     */     }
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 186 */   private <Value> void addTimelineLayerForAttribute(Holder<Timeline> timeline, EnvironmentAttribute<Value> attribute, LongSupplier dayTimeGetter) { addTimeBasedLayer(attribute, ((Timeline)timeline.value()).createTrackSampler(attribute, dayTimeGetter)); }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public EnvironmentAttributeSystem build() { return new EnvironmentAttributeSystem(this.layersByAttribute); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */