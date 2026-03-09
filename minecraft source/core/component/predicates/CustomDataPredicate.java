/*    */ package net.minecraft.core.component.predicates;
/*    */ import net.minecraft.advancements.criterion.NbtPredicate;
/*    */ 
/*    */ public final class CustomDataPredicate extends Record implements DataComponentPredicate {
/*    */   private final NbtPredicate value;
/*    */   
/*  7 */   public CustomDataPredicate(NbtPredicate value) { this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/CustomDataPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/CustomDataPredicate; } public NbtPredicate value() { return this.value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/CustomDataPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/CustomDataPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/CustomDataPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/CustomDataPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  8 */   public static final Codec<CustomDataPredicate> CODEC = NbtPredicate.CODEC.xmap(CustomDataPredicate::new, CustomDataPredicate::value);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public boolean matches(DataComponentGetter components) { return this.value.matches(components); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static CustomDataPredicate customData(NbtPredicate value) { return new CustomDataPredicate(value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\CustomDataPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */