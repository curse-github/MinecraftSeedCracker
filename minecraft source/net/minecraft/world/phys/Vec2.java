/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Vec2
/*    */ {
/* 11 */   public static final Vec2 ZERO = new Vec2(0.0F, 0.0F);
/* 12 */   public static final Vec2 ONE = new Vec2(1.0F, 1.0F);
/* 13 */   public static final Vec2 UNIT_X = new Vec2(1.0F, 0.0F);
/* 14 */   public static final Vec2 NEG_UNIT_X = new Vec2(-1.0F, 0.0F);
/* 15 */   public static final Vec2 UNIT_Y = new Vec2(0.0F, 1.0F);
/* 16 */   public static final Vec2 NEG_UNIT_Y = new Vec2(0.0F, -1.0F);
/* 17 */   public static final Vec2 MAX = new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);
/* 18 */   public static final Vec2 MIN = new Vec2(Float.MIN_VALUE, Float.MIN_VALUE);
/*    */   
/* 20 */   public static final Codec<Vec2> CODEC = Codec.FLOAT.listOf().comapFlatMap(input -> 
/* 21 */       Util.fixedSize(input, 2).map(()), vec -> 
/* 22 */       List.of(Float.valueOf(vec.x), Float.valueOf(vec.y)));
/*    */   
/*    */   public final float x;
/*    */   
/*    */   public final float y;
/*    */   
/*    */   public Vec2(float x, float y) {
/* 29 */     this.x = x;
/* 30 */     this.y = y;
/*    */   }
/*    */ 
/*    */   
/* 34 */   public Vec2 scale(float s) { return new Vec2(this.x * s, this.y * s); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public float dot(Vec2 v) { return this.x * v.x + this.y * v.y; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Vec2 add(Vec2 rhs) { return new Vec2(this.x + rhs.x, this.y + rhs.y); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public Vec2 add(float v) { return new Vec2(this.x + v, this.y + v); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean equals(Vec2 rhs) { return (this.x == rhs.x && this.y == rhs.y); }
/*    */ 
/*    */   
/*    */   public Vec2 normalized() {
/* 54 */     float dist = Mth.sqrt(this.x * this.x + this.y * this.y);
/* 55 */     return (dist < 1.0E-4F) ? ZERO : new Vec2(this.x / dist, this.y / dist);
/*    */   }
/*    */ 
/*    */   
/* 59 */   public float length() { return Mth.sqrt(this.x * this.x + this.y * this.y); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public float lengthSquared() { return this.x * this.x + this.y * this.y; }
/*    */ 
/*    */   
/*    */   public float distanceToSqr(Vec2 p) {
/* 67 */     float xd = p.x - this.x;
/* 68 */     float yd = p.y - this.y;
/* 69 */     return xd * xd + yd * yd;
/*    */   }
/*    */ 
/*    */   
/* 73 */   public Vec2 negated() { return new Vec2(-this.x, -this.y); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\Vec2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */