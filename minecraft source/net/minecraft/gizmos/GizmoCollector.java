/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ 
/*    */ public interface GizmoCollector
/*    */ {
/*  6 */   public static final GizmoProperties IGNORED = new GizmoProperties()
/*    */     {
/*    */       public GizmoProperties setAlwaysOnTop()
/*    */       {
/* 10 */         return this;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 15 */       public GizmoProperties persistForMillis(int milliseconds) { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 20 */       public GizmoProperties fadeOut() { return this; }
/*    */     };
/*    */ 
/*    */   
/* 24 */   public static final GizmoCollector NOOP = gizmo -> IGNORED;
/*    */   
/*    */   GizmoProperties add(Gizmo paramGizmo);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\GizmoCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */