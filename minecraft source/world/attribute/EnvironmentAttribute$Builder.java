/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder<Value>
/*     */   extends Object
/*     */ {
/*     */   private final AttributeType<Value> type;
/*     */   private Value defaultValue;
/*     */   private AttributeRange<Value> valueRange;
/*     */   private boolean isSyncable;
/*     */   private boolean isPositional;
/*     */   private boolean isSpatiallyInterpolated;
/*     */   
/*     */   public Builder(AttributeType<Value> type) {
/*  74 */     this.valueRange = AttributeRange.any();
/*  75 */     this.isSyncable = false;
/*  76 */     this.isPositional = true;
/*  77 */     this.isSpatiallyInterpolated = false;
/*     */ 
/*     */     
/*  80 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Builder<Value> defaultValue(Value defaultValue) {
/*  84 */     this.defaultValue = defaultValue;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> valueRange(AttributeRange<Value> valueRange) {
/*  89 */     this.valueRange = valueRange;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> syncable() {
/*  94 */     this.isSyncable = true;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> notPositional() {
/*  99 */     this.isPositional = false;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> spatiallyInterpolated() {
/* 104 */     this.isSpatiallyInterpolated = true;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public EnvironmentAttribute<Value> build() {
/* 109 */     return new EnvironmentAttribute(this.type, 
/*     */         
/* 111 */         Objects.requireNonNull(this.defaultValue, "Missing default value"), this.valueRange, this.isSyncable, this.isPositional, this.isSpatiallyInterpolated);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttribute$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */