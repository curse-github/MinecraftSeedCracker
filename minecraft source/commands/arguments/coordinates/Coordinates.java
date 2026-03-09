/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface Coordinates
/*    */ {
/*    */   Vec3 getPosition(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   Vec2 getRotation(CommandSourceStack paramCommandSourceStack);
/*    */   
/* 14 */   default BlockPos getBlockPos(CommandSourceStack sender) { return BlockPos.containing(getPosition(sender)); }
/*    */   
/*    */   boolean isXRelative();
/*    */   
/*    */   boolean isYRelative();
/*    */   
/*    */   boolean isZRelative();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\Coordinates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */