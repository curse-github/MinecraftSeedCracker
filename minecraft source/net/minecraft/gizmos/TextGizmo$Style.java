/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import java.util.OptionalDouble;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Style
/*    */   extends Record
/*    */ {
/*    */   private final int color;
/*    */   private final float scale;
/*    */   private final OptionalDouble adjustLeft;
/*    */   public static final float DEFAULT_SCALE = 0.32F;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo$Style;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo$Style;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo$Style;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo$Style;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 21 */   public Style(int color, float scale, OptionalDouble adjustLeft) { this.color = color; this.scale = scale; this.adjustLeft = adjustLeft; } public int color() { return this.color; } public float scale() { return this.scale; } public OptionalDouble adjustLeft() { return this.adjustLeft; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static Style whiteAndCentered() { return new Style(-1, 0.32F, OptionalDouble.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static Style forColorAndCentered(int argb) { return new Style(argb, 0.32F, OptionalDouble.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static Style forColor(int argb) { return new Style(argb, 0.32F, OptionalDouble.of(0.0D)); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public Style withScale(float scale) { return new Style(this.color, scale, this.adjustLeft); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Style withLeftAlignment(float adjustLeft) { return new Style(this.color, this.scale, OptionalDouble.of(adjustLeft)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\TextGizmo$Style.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */