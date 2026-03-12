/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum CaveSurface implements StringRepresentable {
/*  8 */   CEILING(Direction.UP, 1, "ceiling"),
/*  9 */   FLOOR(Direction.DOWN, -1, "floor"); public static final Codec<CaveSurface> CODEC;
/*    */   static  {
/* 11 */     CODEC = StringRepresentable.fromEnum(CaveSurface::values);
/*    */   }
/*    */   private final Direction direction;
/*    */   private final int y;
/*    */   private final String id;
/*    */   
/*    */   CaveSurface(Direction direction, int y, String id) {
/* 18 */     this.direction = direction;
/* 19 */     this.y = y;
/* 20 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/* 24 */   public Direction getDirection() { return this.direction; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public int getY() { return this.y; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CaveSurface.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */