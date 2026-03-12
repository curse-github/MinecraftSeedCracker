/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
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
/*    */ public static enum OperationId
/*    */   implements StringRepresentable
/*    */ {
/* 73 */   OVERRIDE("override"),
/* 74 */   ALPHA_BLEND("alpha_blend"),
/* 75 */   ADD("add"),
/* 76 */   SUBTRACT("subtract"),
/* 77 */   MULTIPLY("multiply"),
/* 78 */   BLEND_TO_GRAY("blend_to_gray"),
/* 79 */   MINIMUM("minimum"),
/* 80 */   MAXIMUM("maximum"),
/* 81 */   AND("and"),
/* 82 */   NAND("nand"),
/* 83 */   OR("or"),
/* 84 */   NOR("nor"),
/* 85 */   XOR("xor"),
/* 86 */   XNOR("xnor");
/*    */   
/*    */   static  {
/* 89 */     CODEC = StringRepresentable.fromEnum(OperationId::values);
/*    */   }
/*    */   public static final Codec<OperationId> CODEC;
/*    */   private final String name;
/*    */   
/* 94 */   OperationId(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 99 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\AttributeModifier$OperationId.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */