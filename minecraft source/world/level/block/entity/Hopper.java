/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public interface Hopper extends Container {
/*  8 */   public static final AABB SUCK_AABB = (AABB)Block.column(16.0D, 11.0D, 32.0D).toAabbs().get(0);
/*    */ 
/*    */   
/* 11 */   default AABB getSuckAabb() { return SUCK_AABB; }
/*    */   
/*    */   double getLevelX();
/*    */   
/*    */   double getLevelY();
/*    */   
/*    */   double getLevelZ();
/*    */   
/*    */   boolean isGridAligned();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\Hopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */