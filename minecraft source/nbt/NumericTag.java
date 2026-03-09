/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ public interface NumericTag
/*    */   extends PrimitiveTag
/*    */ {
/*    */   byte byteValue();
/*    */   
/*    */   short shortValue();
/*    */   
/*    */   int intValue();
/*    */   
/*    */   long longValue();
/*    */   
/*    */   float floatValue();
/*    */   
/*    */   double doubleValue();
/*    */   
/*    */   Number box();
/*    */   
/* 22 */   default Optional<Number> asNumber() { return Optional.of(box()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   default Optional<Byte> asByte() { return Optional.of(Byte.valueOf(byteValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   default Optional<Short> asShort() { return Optional.of(Short.valueOf(shortValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   default Optional<Integer> asInt() { return Optional.of(Integer.valueOf(intValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   default Optional<Long> asLong() { return Optional.of(Long.valueOf(longValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   default Optional<Float> asFloat() { return Optional.of(Float.valueOf(floatValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   default Optional<Double> asDouble() { return Optional.of(Double.valueOf(doubleValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   default Optional<Boolean> asBoolean() { return Optional.of(Boolean.valueOf((byteValue() != 0))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\NumericTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */