/*    */ package net.minecraft.core;
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
/*    */ public static final abstract enum AxisCycle
/*    */ {
/*    */   NONE, FORWARD, BACKWARD;
/*    */   public static final Direction.Axis[] AXIS_VALUES;
/*    */   public static final AxisCycle[] VALUES;
/*    */   
/*    */   static  {
/*    */     // Byte code:
/*    */     //   0: new net/minecraft/core/AxisCycle$1
/*    */     //   3: dup
/*    */     //   4: ldc 'NONE'
/*    */     //   6: iconst_0
/*    */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   10: putstatic net/minecraft/core/AxisCycle.NONE : Lnet/minecraft/core/AxisCycle;
/*    */     //   13: new net/minecraft/core/AxisCycle$2
/*    */     //   16: dup
/*    */     //   17: ldc 'FORWARD'
/*    */     //   19: iconst_1
/*    */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   23: putstatic net/minecraft/core/AxisCycle.FORWARD : Lnet/minecraft/core/AxisCycle;
/*    */     //   26: new net/minecraft/core/AxisCycle$3
/*    */     //   29: dup
/*    */     //   30: ldc 'BACKWARD'
/*    */     //   32: iconst_2
/*    */     //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   36: putstatic net/minecraft/core/AxisCycle.BACKWARD : Lnet/minecraft/core/AxisCycle;
/*    */     //   39: invokestatic $values : ()[Lnet/minecraft/core/AxisCycle;
/*    */     //   42: putstatic net/minecraft/core/AxisCycle.$VALUES : [Lnet/minecraft/core/AxisCycle;
/*    */     //   45: invokestatic values : ()[Lnet/minecraft/core/Direction$Axis;
/*    */     //   48: putstatic net/minecraft/core/AxisCycle.AXIS_VALUES : [Lnet/minecraft/core/Direction$Axis;
/*    */     //   51: invokestatic values : ()[Lnet/minecraft/core/AxisCycle;
/*    */     //   54: putstatic net/minecraft/core/AxisCycle.VALUES : [Lnet/minecraft/core/AxisCycle;
/*    */     //   57: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     //   #28	-> 13
/*    */     //   #49	-> 26
/*    */     //   #3	-> 39
/*    */     //   #72	-> 45
/*    */     //   #73	-> 51
/*    */   }
/*    */   
/* 88 */   public static AxisCycle between(Direction.Axis from, Direction.Axis to) { return VALUES[Math.floorMod(to.ordinal() - from.ordinal(), 3)]; }
/*    */   
/*    */   public abstract int cycle(int paramInt1, int paramInt2, int paramInt3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract double cycle(double paramDouble1, double paramDouble2, double paramDouble3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract Direction.Axis cycle(Direction.Axis paramAxis);
/*    */   
/*    */   public abstract AxisCycle inverse();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\AxisCycle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */