/*     */ package net.minecraft.gizmos;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Gizmos
/*     */ {
/*  11 */   private static final ThreadLocal<GizmoCollector> collector = new ThreadLocal();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TemporaryCollection withCollector(GizmoCollector collector) {
/*  17 */     TemporaryCollection result = new TemporaryCollection();
/*  18 */     Gizmos.collector.set(collector);
/*  19 */     return result;
/*     */   }
/*     */   
/*     */   public static GizmoProperties addGizmo(Gizmo gizmo) {
/*  23 */     GizmoCollector collector = (GizmoCollector)Gizmos.collector.get();
/*  24 */     if (collector == null) {
/*  25 */       throw new IllegalStateException("Gizmos cannot be created here! No GizmoCollector has been registered.");
/*     */     }
/*  27 */     return collector.add(gizmo);
/*     */   }
/*     */ 
/*     */   
/*  31 */   public static GizmoProperties cuboid(AABB aabb, GizmoStyle style) { return cuboid(aabb, style, false); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public static GizmoProperties cuboid(AABB aabb, GizmoStyle style, boolean coloredCorner) { return addGizmo(new CuboidGizmo(aabb, style, coloredCorner)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static GizmoProperties cuboid(BlockPos blockPos, GizmoStyle style) { return cuboid(new AABB(blockPos), style); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static GizmoProperties cuboid(BlockPos blockPos, float padding, GizmoStyle style) { return cuboid((new AABB(blockPos)).inflate(padding), style); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static GizmoProperties circle(Vec3 pos, float radius, GizmoStyle style) { return addGizmo(new CircleGizmo(pos, radius, style)); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static GizmoProperties line(Vec3 start, Vec3 end, int argb) { return addGizmo(new LineGizmo(start, end, argb, 3.0F)); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static GizmoProperties line(Vec3 start, Vec3 end, int argb, float width) { return addGizmo(new LineGizmo(start, end, argb, width)); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static GizmoProperties arrow(Vec3 start, Vec3 end, int argb) { return addGizmo(new ArrowGizmo(start, end, argb, 2.5F)); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static GizmoProperties arrow(Vec3 start, Vec3 end, int argb, float width) { return addGizmo(new ArrowGizmo(start, end, argb, width)); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static GizmoProperties rect(Vec3 cuboidCornerA, Vec3 cuboidCornerB, Direction face, GizmoStyle style) { return addGizmo(RectGizmo.fromCuboidFace(cuboidCornerA, cuboidCornerB, face, style)); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static GizmoProperties rect(Vec3 cornerA, Vec3 cornerB, Vec3 cornerC, Vec3 cornerD, GizmoStyle style) { return addGizmo(new RectGizmo(cornerA, cornerB, cornerC, cornerD, style)); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static GizmoProperties point(Vec3 position, int argb, float size) { return addGizmo(new PointGizmo(position, argb, size)); }
/*     */ 
/*     */   
/*     */   public static GizmoProperties billboardTextOverBlock(String text, BlockPos pos, int row, int color, float scale) {
/*  79 */     double firstRowStartPosition = 1.3D;
/*  80 */     double rowHeight = 0.2D;
/*     */     
/*  82 */     GizmoProperties properties = billboardText(text, Vec3.atLowerCornerWithOffset(pos, 0.5D, 1.3D + row * 0.2D, 0.5D), TextGizmo.Style.forColorAndCentered(color).withScale(scale));
/*  83 */     properties.setAlwaysOnTop();
/*  84 */     return properties;
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardTextOverMob(Entity entity, int row, String text, int color, float scale) {
/*  88 */     double firstRowStartPosition = 2.4D;
/*  89 */     double rowHeight = 0.25D;
/*     */ 
/*     */ 
/*     */     
/*  93 */     double x = entity.getBlockX() + 0.5D;
/*  94 */     double y = entity.getY() + 2.4D + row * 0.25D;
/*  95 */     double z = entity.getBlockZ() + 0.5D;
/*     */     
/*  97 */     float textAdjustLeft = 0.5F;
/*  98 */     GizmoProperties properties = billboardText(text, new Vec3(x, y, z), TextGizmo.Style.forColor(color).withScale(scale).withLeftAlignment(0.5F));
/*  99 */     properties.setAlwaysOnTop();
/* 100 */     return properties;
/*     */   }
/*     */ 
/*     */   
/* 104 */   public static GizmoProperties billboardText(String name, Vec3 pos, TextGizmo.Style style) { return addGizmo(new TextGizmo(pos, name, style)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TemporaryCollection
/*     */     implements AutoCloseable
/*     */   {
/* 112 */     private final GizmoCollector old = (GizmoCollector)Gizmos.collector.get();
/*     */     
/*     */     private boolean closed;
/*     */     
/*     */     public void close() {
/* 117 */       if (!this.closed) {
/* 118 */         this.closed = true;
/* 119 */         Gizmos.collector.set(this.old);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gizmos\Gizmos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */