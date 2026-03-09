/*    */ package net.minecraft.gizmos;public final class LineGizmo extends Record implements Gizmo { private final Vec3 start; private final Vec3 end;
/*    */   private final int color;
/*    */   private final float width;
/*    */   public static final float DEFAULT_WIDTH = 3.0F;
/*    */   
/*  6 */   public LineGizmo(Vec3 start, Vec3 end, int color, float width) { this.start = start; this.end = end; this.color = color; this.width = width; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/LineGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/gizmos/LineGizmo; } public Vec3 start() { return this.start; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/LineGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/LineGizmo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/LineGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/LineGizmo;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public Vec3 end() { return this.end; } public int color() { return this.color; } public float width() { return this.width; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 11 */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) { primitives.addLine(this.start, this.end, ARGB.multiplyAlpha(this.color, alphaMultiplier), this.width); } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\LineGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */