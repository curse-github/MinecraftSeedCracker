/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ public class Empty
/*    */   extends EntityCollisionContext
/*    */ {
/* 88 */   protected static final CollisionContext WITHOUT_FLUID_COLLISIONS = new Empty(false);
/* 89 */   protected static final CollisionContext WITH_FLUID_COLLISIONS = new Empty(true);
/*    */ 
/*    */   
/* 92 */   public Empty(boolean alwaysCollideWithFluid) { super(false, false, -1.7976931348623157E308D, ItemStack.EMPTY, alwaysCollideWithFluid, null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 97 */   public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) { return defaultValue; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\EntityCollisionContext$Empty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */