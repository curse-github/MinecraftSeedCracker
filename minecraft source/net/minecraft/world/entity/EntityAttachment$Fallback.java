/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.phys.Vec3;
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
/*    */ public interface Fallback
/*    */ {
/* 30 */   public static final List<Vec3> ZERO = List.of(Vec3.ZERO);
/* 31 */   public static final Fallback AT_FEET = (width, height) -> ZERO;
/* 32 */   public static final Fallback AT_HEIGHT = (width, height) -> List.of(new Vec3(0.0D, height, 0.0D));
/* 33 */   public static final Fallback AT_CENTER = (width, height) -> List.of(new Vec3(0.0D, height / 2.0D, 0.0D));
/*    */   
/*    */   List<Vec3> create(float paramFloat1, float paramFloat2);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityAttachment$Fallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */