/*    */ package net.minecraft.gizmos;public final class RectGizmo extends Record implements Gizmo { private final Vec3 a; private final Vec3 b;
/*    */   private final Vec3 c;
/*    */   private final Vec3 d;
/*    */   private final GizmoStyle style;
/*    */   
/*  6 */   public RectGizmo(Vec3 a, Vec3 b, Vec3 c, Vec3 d, GizmoStyle style) { this.a = a; this.b = b; this.c = c; this.d = d; this.style = style; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/RectGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/RectGizmo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/RectGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/RectGizmo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/RectGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/RectGizmo;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public Vec3 a() { return this.a; } public Vec3 b() { return this.b; } public Vec3 c() { return this.c; } public Vec3 d() { return this.d; } public GizmoStyle style() { return this.style; }
/*    */   public static RectGizmo fromCuboidFace(Vec3 cuboidCornerA, Vec3 cuboidCornerB, Direction face, GizmoStyle style) {
/*  8 */     switch (face) { default: throw new MatchException(null, null);case DOWN: case UP: case NORTH: case SOUTH: case WEST: case EAST: break; }  return 
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
/* 44 */       new RectGizmo(new Vec3(cuboidCornerB.x, cuboidCornerA.y, cuboidCornerA.z), new Vec3(cuboidCornerB.x, cuboidCornerB.y, cuboidCornerA.z), new Vec3(cuboidCornerB.x, cuboidCornerB.y, cuboidCornerB.z), new Vec3(cuboidCornerB.x, cuboidCornerA.y, cuboidCornerB.z), style);
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
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 56 */     if (this.style.hasFill()) {
/* 57 */       int color = this.style.multipliedFill(alphaMultiplier);
/* 58 */       primitives.addQuad(this.a, this.b, this.c, this.d, color);
/*    */     } 
/* 60 */     if (this.style.hasStroke()) {
/* 61 */       int color = this.style.multipliedStroke(alphaMultiplier);
/* 62 */       primitives.addLine(this.a, this.b, color, this.style.strokeWidth());
/* 63 */       primitives.addLine(this.b, this.c, color, this.style.strokeWidth());
/* 64 */       primitives.addLine(this.c, this.d, color, this.style.strokeWidth());
/* 65 */       primitives.addLine(this.d, this.a, color, this.style.strokeWidth());
/*    */     } 
/*    */   } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\RectGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */