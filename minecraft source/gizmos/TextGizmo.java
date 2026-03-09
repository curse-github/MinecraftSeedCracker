/*    */ package net.minecraft.gizmos;
/*    */ import java.util.OptionalDouble;
/*    */ 
/*    */ public final class TextGizmo extends Record implements Gizmo {
/*    */   private final Vec3 pos;
/*    */   private final String text;
/*    */   private final Style style;
/*    */   
/*  9 */   public TextGizmo(Vec3 pos, String text, Style style) { this.pos = pos; this.text = text; this.style = style; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo; } public Vec3 pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public String text() { return this.text; } public Style style() { return this.style; }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/*    */     Style newStyle;
/* 13 */     if (alphaMultiplier < 1.0F) {
/* 14 */       newStyle = new Style(ARGB.multiplyAlpha(this.style.color, alphaMultiplier), this.style.scale, this.style.adjustLeft);
/*    */     } else {
/* 16 */       newStyle = this.style;
/*    */     } 
/* 18 */     primitives.addText(this.pos, this.text, newStyle);
/*    */   }
/*    */   public static final class Style extends Record { private final int color; private final float scale; private final OptionalDouble adjustLeft; public static final float DEFAULT_SCALE = 0.32F;
/* 21 */     public Style(int color, float scale, OptionalDouble adjustLeft) { this.color = color; this.scale = scale; this.adjustLeft = adjustLeft; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo$Style;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo$Style;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo$Style;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo$Style;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public int color() { return this.color; } public float scale() { return this.scale; } public OptionalDouble adjustLeft() { return this.adjustLeft; }
/*    */ 
/*    */ 
/*    */     
/* 25 */     public static Style whiteAndCentered() { return new Style(-1, 0.32F, OptionalDouble.empty()); }
/*    */ 
/*    */ 
/*    */     
/* 29 */     public static Style forColorAndCentered(int argb) { return new Style(argb, 0.32F, OptionalDouble.empty()); }
/*    */ 
/*    */ 
/*    */     
/* 33 */     public static Style forColor(int argb) { return new Style(argb, 0.32F, OptionalDouble.of(0.0D)); }
/*    */ 
/*    */ 
/*    */     
/* 37 */     public Style withScale(float scale) { return new Style(this.color, scale, this.adjustLeft); }
/*    */ 
/*    */ 
/*    */     
/* 41 */     public Style withLeftAlignment(float adjustLeft) { return new Style(this.color, this.scale, OptionalDouble.of(adjustLeft)); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\TextGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */