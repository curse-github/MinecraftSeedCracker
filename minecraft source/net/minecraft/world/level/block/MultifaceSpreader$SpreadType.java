/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public static final abstract enum SpreadType {
/*     */   SAME_POSITION, SAME_PLANE, WRAP_AROUND;
/*     */   
/*     */   public abstract MultifaceSpreader.SpreadPos getSpreadPos(BlockPos paramBlockPos, Direction paramDirection1, Direction paramDirection2);
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/level/block/MultifaceSpreader$SpreadType$1
/*     */     //   3: dup
/*     */     //   4: ldc 'SAME_POSITION'
/*     */     //   6: iconst_0
/*     */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   10: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.SAME_POSITION : Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */     //   13: new net/minecraft/world/level/block/MultifaceSpreader$SpreadType$2
/*     */     //   16: dup
/*     */     //   17: ldc 'SAME_PLANE'
/*     */     //   19: iconst_1
/*     */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   23: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.SAME_PLANE : Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */     //   26: new net/minecraft/world/level/block/MultifaceSpreader$SpreadType$3
/*     */     //   29: dup
/*     */     //   30: ldc 'WRAP_AROUND'
/*     */     //   32: iconst_2
/*     */     //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   36: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.WRAP_AROUND : Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */     //   39: invokestatic $values : ()[Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */     //   42: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.$VALUES : [Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */     //   45: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #163	-> 0
/*     */     //   #169	-> 13
/*     */     //   #175	-> 26
/*     */     //   #162	-> 39
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */