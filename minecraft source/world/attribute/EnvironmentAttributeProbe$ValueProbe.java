/*    */ package net.minecraft.world.attribute;
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
/*    */ class ValueProbe<Value>
/*    */   extends Object
/*    */ {
/*    */   private Value lastValue;
/*    */   private Value newValue;
/*    */   
/*    */   public ValueProbe(EnvironmentAttribute<Value> attribute) {
/* 54 */     Value value = (Value)getValueFromLevel(attribute);
/* 55 */     this.lastValue = value;
/* 56 */     this.newValue = value;
/*    */   }
/*    */   
/*    */   private Value getValueFromLevel(EnvironmentAttribute<Value> attribute) {
/* 60 */     if (EnvironmentAttributeProbe.this.level == null || EnvironmentAttributeProbe.this.position == null) {
/* 61 */       return (Value)attribute.defaultValue();
/*    */     }
/* 63 */     return (Value)EnvironmentAttributeProbe.this.level.environmentAttributes().getValue(attribute, EnvironmentAttributeProbe.this.position, EnvironmentAttributeProbe.this.biomeInterpolator);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tick() {
/* 68 */     if (this.newValue == null) {
/* 69 */       return true;
/*    */     }
/* 71 */     this.lastValue = this.newValue;
/* 72 */     this.newValue = null;
/* 73 */     return false;
/*    */   }
/*    */   
/*    */   public Value get(EnvironmentAttribute<Value> attribute, float partialTicks) {
/* 77 */     if (this.newValue == null) {
/* 78 */       this.newValue = getValueFromLevel(attribute);
/*    */     }
/* 80 */     return (Value)attribute.type().partialTickLerp().apply(partialTicks, this.lastValue, this.newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeProbe$ValueProbe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */