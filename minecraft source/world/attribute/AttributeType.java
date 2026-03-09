/*    */ package net.minecraft.world.attribute;
/*    */ import com.google.common.collect.ImmutableBiMap;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ 
/*    */ public final class AttributeType<Value> extends Record {
/*    */   private final Codec<Value> valueCodec;
/*    */   private final Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary;
/*    */   private final Codec<AttributeModifier<Value, ?>> modifierCodec;
/*    */   
/* 12 */   public AttributeType(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary, Codec<AttributeModifier<Value, ?>> modifierCodec, LerpFunction<Value> keyframeLerp, LerpFunction<Value> stateChangeLerp, LerpFunction<Value> spatialLerp, LerpFunction<Value> partialTickLerp) { this.valueCodec = valueCodec; this.modifierLibrary = modifierLibrary; this.modifierCodec = modifierCodec; this.keyframeLerp = keyframeLerp; this.stateChangeLerp = stateChangeLerp; this.spatialLerp = spatialLerp; this.partialTickLerp = partialTickLerp; } private final LerpFunction<Value> keyframeLerp; private final LerpFunction<Value> stateChangeLerp; private final LerpFunction<Value> spatialLerp; private final LerpFunction<Value> partialTickLerp; public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AttributeType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AttributeType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AttributeType<TValue;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AttributeType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AttributeType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/world/attribute/AttributeType<TValue;>; } public Codec<Value> valueCodec() { return this.valueCodec; } public Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary() { return this.modifierLibrary; } public Codec<AttributeModifier<Value, ?>> modifierCodec() { return this.modifierCodec; } public LerpFunction<Value> keyframeLerp() { return this.keyframeLerp; } public LerpFunction<Value> stateChangeLerp() { return this.stateChangeLerp; } public LerpFunction<Value> spatialLerp() { return this.spatialLerp; } public LerpFunction<Value> partialTickLerp() { return this.partialTickLerp; }
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
/* 28 */   public static <Value> AttributeType<Value> ofInterpolated(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary, LerpFunction<Value> lerp) { return ofInterpolated(valueCodec, modifierLibrary, lerp, lerp); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <Value> AttributeType<Value> ofInterpolated(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary, LerpFunction<Value> lerp, LerpFunction<Value> partialTickLerp) {
/* 37 */     return new AttributeType(valueCodec, modifierLibrary, 
/*    */ 
/*    */         
/* 40 */         createModifierCodec(modifierLibrary), lerp, lerp, lerp, partialTickLerp);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <Value> AttributeType<Value> ofNotInterpolated(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary) {
/* 52 */     return new AttributeType(valueCodec, modifierLibrary, 
/*    */ 
/*    */         
/* 55 */         createModifierCodec(modifierLibrary), 
/*    */         
/* 57 */         LerpFunction.ofStep(1.0F), 
/*    */         
/* 59 */         LerpFunction.ofStep(0.0F), 
/*    */         
/* 61 */         LerpFunction.ofStep(0.5F), 
/*    */         
/* 63 */         LerpFunction.ofStep(0.0F));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public static <Value> AttributeType<Value> ofNotInterpolated(Codec<Value> valueCodec) { return ofNotInterpolated(valueCodec, Map.of()); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <Value> Codec<AttributeModifier<Value, ?>> createModifierCodec(Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifiers) {
/* 75 */     ImmutableBiMap<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLookup = ImmutableBiMap.builder().put(AttributeModifier.OperationId.OVERRIDE, AttributeModifier.override()).putAll(modifiers).buildOrThrow();
/*    */ 
/*    */ 
/*    */     
/* 79 */     Objects.requireNonNull(modifierLookup);
/* 80 */     Objects.requireNonNull(modifierLookup.inverse()); return ExtraCodecs.idResolverCodec(AttributeModifier.OperationId.CODEC, modifierLookup::get, modifierLookup.inverse()::get);
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkAllowedModifier(AttributeModifier<Value, ?> modifier) {
/* 85 */     if (modifier != AttributeModifier.override() && !this.modifierLibrary.containsValue(modifier)) {
/* 86 */       throw new IllegalArgumentException("Modifier " + String.valueOf(modifier) + " is not valid for " + String.valueOf(this));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 92 */   public String toString() { return Util.getRegisteredName(BuiltInRegistries.ATTRIBUTE_TYPE, this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AttributeType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */