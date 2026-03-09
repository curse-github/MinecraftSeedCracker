/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public static enum EntityAttachment
/*    */ {
/*  8 */   PASSENGER(Fallback.AT_HEIGHT),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   VEHICLE(Fallback.AT_FEET),
/* 15 */   NAME_TAG(Fallback.AT_HEIGHT),
/* 16 */   WARDEN_CHEST(Fallback.AT_CENTER);
/*    */ 
/*    */   
/*    */   private final Fallback fallback;
/*    */ 
/*    */   
/* 22 */   EntityAttachment(Fallback fallback) { this.fallback = fallback; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public List<Vec3> createFallbackPoints(float width, float height) { return this.fallback.create(width, height); }
/*    */   
/*    */   public static interface Fallback
/*    */   {
/* 30 */     public static final List<Vec3> ZERO = List.of(Vec3.ZERO);
/* 31 */     public static final Fallback AT_FEET = (width, height) -> ZERO;
/* 32 */     public static final Fallback AT_HEIGHT = (width, height) -> List.of(new Vec3(0.0D, height, 0.0D));
/* 33 */     public static final Fallback AT_CENTER = (width, height) -> List.of(new Vec3(0.0D, height / 2.0D, 0.0D));
/*    */     
/*    */     List<Vec3> create(float param1Float1, float param1Float2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityAttachment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */