/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
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
/*    */ class null
/*    */   extends BaseSpawner
/*    */ {
/* 20 */   public void broadcastEvent(Level level, BlockPos pos, int id) { level.broadcastEntityEvent(MinecartSpawner.this, (byte)id); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartSpawner$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */