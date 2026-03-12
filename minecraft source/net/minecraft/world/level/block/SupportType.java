/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.BooleanOp;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public static final abstract enum SupportType {
/*    */   FULL, CENTER, RIGID;
/*    */   
/*    */   public abstract boolean isSupporting(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*    */   
/*    */   static  {
/*    */     // Byte code:
/*    */     //   0: new net/minecraft/world/level/block/SupportType$1
/*    */     //   3: dup
/*    */     //   4: ldc 'FULL'
/*    */     //   6: iconst_0
/*    */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   10: putstatic net/minecraft/world/level/block/SupportType.FULL : Lnet/minecraft/world/level/block/SupportType;
/*    */     //   13: new net/minecraft/world/level/block/SupportType$2
/*    */     //   16: dup
/*    */     //   17: ldc 'CENTER'
/*    */     //   19: iconst_1
/*    */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   23: putstatic net/minecraft/world/level/block/SupportType.CENTER : Lnet/minecraft/world/level/block/SupportType;
/*    */     //   26: new net/minecraft/world/level/block/SupportType$3
/*    */     //   29: dup
/*    */     //   30: ldc 'RIGID'
/*    */     //   32: iconst_2
/*    */     //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   36: putstatic net/minecraft/world/level/block/SupportType.RIGID : Lnet/minecraft/world/level/block/SupportType;
/*    */     //   39: invokestatic $values : ()[Lnet/minecraft/world/level/block/SupportType;
/*    */     //   42: putstatic net/minecraft/world/level/block/SupportType.$VALUES : [Lnet/minecraft/world/level/block/SupportType;
/*    */     //   45: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     //   #18	-> 13
/*    */     //   #26	-> 26
/*    */     //   #11	-> 39
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SupportType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */