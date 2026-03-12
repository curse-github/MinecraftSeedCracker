/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public interface AttributeModifier<Subject, Argument>
/*    */ {
/* 11 */   public static final Map<OperationId, AttributeModifier<Boolean, ?>> BOOLEAN_LIBRARY = Map.of(OperationId.AND, BooleanModifier.AND, OperationId.NAND, BooleanModifier.NAND, OperationId.OR, BooleanModifier.OR, OperationId.NOR, BooleanModifier.NOR, OperationId.XOR, BooleanModifier.XOR, OperationId.XNOR, BooleanModifier.XNOR);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final Map<OperationId, AttributeModifier<Float, ?>> FLOAT_LIBRARY = Map.of(OperationId.ALPHA_BLEND, FloatModifier.ALPHA_BLEND, OperationId.ADD, FloatModifier.ADD, OperationId.SUBTRACT, FloatModifier.SUBTRACT, OperationId.MULTIPLY, FloatModifier.MULTIPLY, OperationId.MINIMUM, FloatModifier.MINIMUM, OperationId.MAXIMUM, FloatModifier.MAXIMUM);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final Map<OperationId, AttributeModifier<Integer, ?>> RGB_COLOR_LIBRARY = Map.of(OperationId.ALPHA_BLEND, ColorModifier.ALPHA_BLEND, OperationId.ADD, ColorModifier.ADD, OperationId.SUBTRACT, ColorModifier.SUBTRACT, OperationId.MULTIPLY, ColorModifier.MULTIPLY_RGB, OperationId.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final Map<OperationId, AttributeModifier<Integer, ?>> ARGB_COLOR_LIBRARY = Map.of(OperationId.ALPHA_BLEND, ColorModifier.ALPHA_BLEND, OperationId.ADD, ColorModifier.ADD, OperationId.SUBTRACT, ColorModifier.SUBTRACT, OperationId.MULTIPLY, ColorModifier.MULTIPLY_ARGB, OperationId.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   static <Value> AttributeModifier<Value, Value> override() { return OverrideModifier.INSTANCE; }
/*    */   
/*    */   Subject apply(Subject paramSubject, Argument paramArgument);
/*    */   
/*    */   Codec<Argument> argumentCodec(EnvironmentAttribute<Subject> paramEnvironmentAttribute);
/*    */   
/*    */   LerpFunction<Argument> argumentKeyframeLerp(EnvironmentAttribute<Subject> paramEnvironmentAttribute);
/*    */   
/*    */   public static final class OverrideModifier<Value>
/*    */     extends Record implements AttributeModifier<Value, Value> {
/* 54 */     private static final OverrideModifier<?> INSTANCE = new OverrideModifier();
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier<TValue;>; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier<TValue;>; }
/*    */     
/* 58 */     public Value apply(Value subject, Value argument) { return argument; }
/*    */ 
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier<TValue;>; }
/*    */     
/* 63 */     public Codec<Value> argumentCodec(EnvironmentAttribute<Value> attribute) { return attribute.valueCodec(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 68 */     public LerpFunction<Value> argumentKeyframeLerp(EnvironmentAttribute<Value> attribute) { return attribute.type().keyframeLerp(); }
/*    */   }
/*    */   
/*    */   public enum OperationId
/*    */     implements StringRepresentable {
/* 73 */     OVERRIDE("override"),
/* 74 */     ALPHA_BLEND("alpha_blend"),
/* 75 */     ADD("add"),
/* 76 */     SUBTRACT("subtract"),
/* 77 */     MULTIPLY("multiply"),
/* 78 */     BLEND_TO_GRAY("blend_to_gray"),
/* 79 */     MINIMUM("minimum"),
/* 80 */     MAXIMUM("maximum"),
/* 81 */     AND("and"),
/* 82 */     NAND("nand"),
/* 83 */     OR("or"),
/* 84 */     NOR("nor"),
/* 85 */     XOR("xor"),
/* 86 */     XNOR("xnor"); public static final Codec<OperationId> CODEC;
/*    */     
/*    */     static  {
/* 89 */       CODEC = StringRepresentable.fromEnum(OperationId::values);
/*    */     }
/*    */     
/*    */     private final String name;
/*    */     
/* 94 */     OperationId(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 99 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\AttributeModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */