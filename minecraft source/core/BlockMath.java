/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.math.MatrixUtil;
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockMath
/*    */ {
/* 16 */   private static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL = Maps.newEnumMap(Map.of(Direction.SOUTH, 
/* 17 */         Transformation.identity(), Direction.EAST, new Transformation(null, (new Quaternionf())
/* 18 */           .rotateY(1.5707964F), null, null), Direction.WEST, new Transformation(null, (new Quaternionf())
/* 19 */           .rotateY(-1.5707964F), null, null), Direction.NORTH, new Transformation(null, (new Quaternionf())
/* 20 */           .rotateY(3.1415927F), null, null), Direction.UP, new Transformation(null, (new Quaternionf())
/* 21 */           .rotateX(-1.5707964F), null, null), Direction.DOWN, new Transformation(null, (new Quaternionf())
/* 22 */           .rotateX(1.5707964F), null, null)));
/*    */   
/* 24 */   private static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL = Maps.newEnumMap(Util.mapValues(VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL, Transformation::inverse));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformation blockCenterToCorner(Transformation transform) {
/* 30 */     Matrix4f ret = (new Matrix4f()).translation(0.5F, 0.5F, 0.5F);
/* 31 */     ret.mul(transform.getMatrix());
/* 32 */     ret.translate(-0.5F, -0.5F, -0.5F);
/* 33 */     return new Transformation(ret);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformation blockCornerToCenter(Transformation transform) {
/* 40 */     Matrix4f ret = (new Matrix4f()).translation(-0.5F, -0.5F, -0.5F);
/* 41 */     ret.mul(transform.getMatrix());
/* 42 */     ret.translate(0.5F, 0.5F, 0.5F);
/* 43 */     return new Transformation(ret);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformation getFaceTransformation(Transformation transformation, Direction originalSide) {
/* 50 */     if (MatrixUtil.isIdentity(transformation.getMatrix())) {
/* 51 */       return transformation;
/*    */     }
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
/* 64 */     Transformation faceAction = (Transformation)VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL.get(originalSide);
/*    */ 
/*    */     
/* 67 */     faceAction = transformation.compose(faceAction);
/*    */ 
/*    */ 
/*    */     
/* 71 */     Vector3f transformedNormal = faceAction.getMatrix().transformDirection(new Vector3f(0.0F, 0.0F, 1.0F));
/* 72 */     Direction newSide = Direction.getApproximateNearest(transformedNormal.x, transformedNormal.y, transformedNormal.z);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     return ((Transformation)VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL.get(newSide)).compose(faceAction);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\BlockMath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */