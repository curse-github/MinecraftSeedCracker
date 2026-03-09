/*    */ package net.minecraft.gizmos;
/*    */ public final class CuboidGizmo extends Record implements Gizmo {
/*    */   private final AABB aabb;
/*    */   private final GizmoStyle style;
/*    */   private final boolean coloredCornerStroke;
/*    */   
/*  7 */   public CuboidGizmo(AABB aabb, GizmoStyle style, boolean coloredCornerStroke) { this.aabb = aabb; this.style = style; this.coloredCornerStroke = coloredCornerStroke; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/CuboidGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/gizmos/CuboidGizmo; } public AABB aabb() { return this.aabb; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/CuboidGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/CuboidGizmo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/CuboidGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/CuboidGizmo;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public GizmoStyle style() { return this.style; } public boolean coloredCornerStroke() { return this.coloredCornerStroke; }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 10 */     double x0 = this.aabb.minX;
/* 11 */     double y0 = this.aabb.minY;
/* 12 */     double z0 = this.aabb.minZ;
/* 13 */     double x1 = this.aabb.maxX;
/* 14 */     double y1 = this.aabb.maxY;
/* 15 */     double z1 = this.aabb.maxZ;
/*    */     
/* 17 */     if (this.style.hasFill()) {
/* 18 */       int color = this.style.multipliedFill(alphaMultiplier);
/*    */ 
/*    */       
/* 21 */       primitives.addQuad(new Vec3(x1, y0, z0), new Vec3(x1, y1, z0), new Vec3(x1, y1, z1), new Vec3(x1, y0, z1), color);
/*    */ 
/*    */       
/* 24 */       primitives.addQuad(new Vec3(x0, y0, z0), new Vec3(x0, y0, z1), new Vec3(x0, y1, z1), new Vec3(x0, y1, z0), color);
/*    */ 
/*    */       
/* 27 */       primitives.addQuad(new Vec3(x0, y0, z0), new Vec3(x0, y1, z0), new Vec3(x1, y1, z0), new Vec3(x1, y0, z0), color);
/*    */ 
/*    */       
/* 30 */       primitives.addQuad(new Vec3(x0, y0, z1), new Vec3(x1, y0, z1), new Vec3(x1, y1, z1), new Vec3(x0, y1, z1), color);
/*    */ 
/*    */       
/* 33 */       primitives.addQuad(new Vec3(x0, y1, z0), new Vec3(x0, y1, z1), new Vec3(x1, y1, z1), new Vec3(x1, y1, z0), color);
/*    */ 
/*    */       
/* 36 */       primitives.addQuad(new Vec3(x0, y0, z0), new Vec3(x1, y0, z0), new Vec3(x1, y0, z1), new Vec3(x0, y0, z1), color);
/*    */     } 
/*    */     
/* 39 */     if (this.style.hasStroke()) {
/* 40 */       int color = this.style.multipliedStroke(alphaMultiplier);
/*    */ 
/*    */       
/* 43 */       primitives.addLine(new Vec3(x0, y0, z0), new Vec3(x1, y0, z0), this.coloredCornerStroke ? ARGB.multiply(color, -34953) : color, this.style.strokeWidth());
/* 44 */       primitives.addLine(new Vec3(x0, y0, z0), new Vec3(x0, y1, z0), this.coloredCornerStroke ? ARGB.multiply(color, -8913033) : color, this.style.strokeWidth());
/* 45 */       primitives.addLine(new Vec3(x0, y0, z0), new Vec3(x0, y0, z1), this.coloredCornerStroke ? ARGB.multiply(color, -8947713) : color, this.style.strokeWidth());
/*    */ 
/*    */       
/* 48 */       primitives.addLine(new Vec3(x1, y0, z0), new Vec3(x1, y1, z0), color, this.style.strokeWidth());
/* 49 */       primitives.addLine(new Vec3(x1, y1, z0), new Vec3(x0, y1, z0), color, this.style.strokeWidth());
/* 50 */       primitives.addLine(new Vec3(x0, y1, z0), new Vec3(x0, y1, z1), color, this.style.strokeWidth());
/* 51 */       primitives.addLine(new Vec3(x0, y1, z1), new Vec3(x0, y0, z1), color, this.style.strokeWidth());
/* 52 */       primitives.addLine(new Vec3(x0, y0, z1), new Vec3(x1, y0, z1), color, this.style.strokeWidth());
/* 53 */       primitives.addLine(new Vec3(x1, y0, z1), new Vec3(x1, y0, z0), color, this.style.strokeWidth());
/*    */ 
/*    */       
/* 56 */       primitives.addLine(new Vec3(x0, y1, z1), new Vec3(x1, y1, z1), color, this.style.strokeWidth());
/* 57 */       primitives.addLine(new Vec3(x1, y0, z1), new Vec3(x1, y1, z1), color, this.style.strokeWidth());
/* 58 */       primitives.addLine(new Vec3(x1, y1, z0), new Vec3(x1, y1, z1), color, this.style.strokeWidth());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\CuboidGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */