/*    */ package net.minecraft.gizmos;public final class GizmoStyle extends Record { private final int stroke; private final float strokeWidth;
/*    */   private final int fill;
/*    */   private static final float DEFAULT_WIDTH = 2.5F;
/*    */   
/*  5 */   public GizmoStyle(int stroke, float strokeWidth, int fill) { this.stroke = stroke; this.strokeWidth = strokeWidth; this.fill = fill; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/GizmoStyle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/gizmos/GizmoStyle; } public int stroke() { return this.stroke; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/GizmoStyle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/GizmoStyle; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/GizmoStyle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/GizmoStyle;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public float strokeWidth() { return this.strokeWidth; } public int fill() { return this.fill; }
/*    */ 
/*    */ 
/*    */   
/*  9 */   public static GizmoStyle stroke(int argb) { return new GizmoStyle(argb, 2.5F, 0); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static GizmoStyle stroke(int argb, float width) { return new GizmoStyle(argb, width, 0); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static GizmoStyle fill(int argb) { return new GizmoStyle(0, 0.0F, argb); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static GizmoStyle strokeAndFill(int stroke, float strokeWidth, int fill) { return new GizmoStyle(stroke, strokeWidth, fill); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean hasFill() { return (this.fill != 0); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public boolean hasStroke() { return (this.stroke != 0 && this.strokeWidth > 0.0F); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public int multipliedStroke(float alphaMultiplier) { return ARGB.multiplyAlpha(this.stroke, alphaMultiplier); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int multipliedFill(float alphaMultiplier) { return ARGB.multiplyAlpha(this.fill, alphaMultiplier); } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\GizmoStyle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */