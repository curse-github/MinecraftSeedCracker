/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
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
/* 124 */   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder putAll(EnvironmentAttributeMap map) {
/* 130 */     this.entries.putAll(map.entries);
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Parameter> Builder modify(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Parameter> modifier, Parameter value) {
/* 135 */     attribute.type().checkAllowedModifier(modifier);
/* 136 */     this.entries.put(attribute, new EnvironmentAttributeMap.Entry(value, modifier));
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 141 */   public <Value> Builder set(EnvironmentAttribute<Value> attribute, Value value) { return modify(attribute, AttributeModifier.override(), value); }
/*     */ 
/*     */   
/*     */   public EnvironmentAttributeMap build() {
/* 145 */     if (this.entries.isEmpty()) {
/* 146 */       return EnvironmentAttributeMap.EMPTY;
/*     */     }
/* 148 */     return new EnvironmentAttributeMap(Map.copyOf(this.entries));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeMap$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */