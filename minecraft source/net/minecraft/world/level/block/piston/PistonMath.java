/*    */ package net.minecraft.world.level.block.piston;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PistonMath
/*    */ {
/*    */   public static AABB getMovementArea(AABB aabb, Direction direction, double amount) {
/* 15 */     double delta = amount * direction.getAxisDirection().getStep();
/* 16 */     double min = Math.min(delta, 0.0D);
/* 17 */     double max = Math.max(delta, 0.0D);
/* 18 */     switch (direction)
/*    */     { case WEST:
/* 20 */         return new AABB(aabb.minX + min, aabb.minY, aabb.minZ, aabb.minX + max, aabb.maxY, aabb.maxZ);
/*    */       case EAST:
/* 22 */         return new AABB(aabb.maxX + min, aabb.minY, aabb.minZ, aabb.maxX + max, aabb.maxY, aabb.maxZ);
/*    */       case DOWN:
/* 24 */         return new AABB(aabb.minX, aabb.minY + min, aabb.minZ, aabb.maxX, aabb.minY + max, aabb.maxZ);
/*    */       
/*    */       default:
/* 27 */         return new AABB(aabb.minX, aabb.maxY + min, aabb.minZ, aabb.maxX, aabb.maxY + max, aabb.maxZ);
/*    */       case NORTH:
/* 29 */         return new AABB(aabb.minX, aabb.minY, aabb.minZ + min, aabb.maxX, aabb.maxY, aabb.minZ + max);
/*    */       case SOUTH:
/* 31 */         break; }  return new AABB(aabb.minX, aabb.minY, aabb.maxZ + min, aabb.maxX, aabb.maxY, aabb.maxZ + max);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\piston\PistonMath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */