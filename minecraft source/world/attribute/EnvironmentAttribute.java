/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnvironmentAttribute<Value>
/*     */   extends Object
/*     */ {
/*     */   private final AttributeType<Value> type;
/*     */   private final Value defaultValue;
/*     */   private final AttributeRange<Value> valueRange;
/*     */   private final boolean isSyncable;
/*     */   private final boolean isPositional;
/*     */   private final boolean isSpatiallyInterpolated;
/*     */   
/*     */   private EnvironmentAttribute(AttributeType<Value> type, Value defaultValue, AttributeRange<Value> valueRange, boolean isSyncable, boolean isPositional, boolean isSpatiallyInterpolated) {
/*  26 */     this.type = type;
/*  27 */     this.defaultValue = defaultValue;
/*  28 */     this.valueRange = valueRange;
/*  29 */     this.isSyncable = isSyncable;
/*  30 */     this.isPositional = isPositional;
/*  31 */     this.isSpatiallyInterpolated = isSpatiallyInterpolated;
/*     */   }
/*     */ 
/*     */   
/*  35 */   public static <Value> Builder<Value> builder(AttributeType<Value> type) { return new Builder(type); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public AttributeType<Value> type() { return this.type; }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public Value defaultValue() { return (Value)this.defaultValue; }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public Codec<Value> valueCodec() { Objects.requireNonNull(this.valueRange); return this.type.valueCodec().validate(this.valueRange::validate); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public Value sanitizeValue(Value value) { return (Value)this.valueRange.sanitize(value); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public boolean isSyncable() { return this.isSyncable; }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public boolean isPositional() { return this.isPositional; }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public boolean isSpatiallyInterpolated() { return this.isSpatiallyInterpolated; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public String toString() { return Util.getRegisteredName(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, this); }
/*     */   public static class Builder<Value> extends Object { private final AttributeType<Value> type;
/*     */     private Value defaultValue;
/*     */     private AttributeRange<Value> valueRange;
/*     */     
/*     */     public Builder(AttributeType<Value> type) {
/*  74 */       this.valueRange = AttributeRange.any();
/*  75 */       this.isSyncable = false;
/*  76 */       this.isPositional = true;
/*  77 */       this.isSpatiallyInterpolated = false;
/*     */ 
/*     */       
/*  80 */       this.type = type;
/*     */     }
/*     */     private boolean isSyncable; private boolean isPositional; private boolean isSpatiallyInterpolated;
/*     */     public Builder<Value> defaultValue(Value defaultValue) {
/*  84 */       this.defaultValue = defaultValue;
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> valueRange(AttributeRange<Value> valueRange) {
/*  89 */       this.valueRange = valueRange;
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> syncable() {
/*  94 */       this.isSyncable = true;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> notPositional() {
/*  99 */       this.isPositional = false;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<Value> spatiallyInterpolated() {
/* 104 */       this.isSpatiallyInterpolated = true;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public EnvironmentAttribute<Value> build() {
/* 109 */       return new EnvironmentAttribute(this.type, 
/*     */           
/* 111 */           Objects.requireNonNull(this.defaultValue, "Missing default value"), this.valueRange, this.isSyncable, this.isPositional, this.isSpatiallyInterpolated);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttribute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */