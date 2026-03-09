/*    */ package net.minecraft.world.entity.boss.enderdragon;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DragonFlightHistory {
/*    */   public static final int LENGTH = 64;
/*    */   private static final int MASK = 63;
/*    */   private final Sample[] samples;
/*    */   private int head;
/*    */   
/*    */   public DragonFlightHistory() {
/* 11 */     this.samples = new Sample[64];
/* 12 */     this.head = -1;
/*    */ 
/*    */     
/* 15 */     Arrays.fill(this.samples, new Sample(0.0D, 0.0F));
/*    */   }
/*    */   
/*    */   public void copyFrom(DragonFlightHistory history) {
/* 19 */     System.arraycopy(history.samples, 0, this.samples, 0, 64);
/* 20 */     this.head = history.head;
/*    */   }
/*    */   
/*    */   public void record(double y, float yRot) {
/* 24 */     Sample sample = new Sample(y, yRot);
/* 25 */     if (this.head < 0) {
/* 26 */       Arrays.fill(this.samples, sample);
/*    */     }
/*    */     
/* 29 */     if (++this.head == 64) {
/* 30 */       this.head = 0;
/*    */     }
/* 32 */     this.samples[this.head] = sample;
/*    */   }
/*    */ 
/*    */   
/* 36 */   public Sample get(int delay) { return this.samples[this.head - delay & 0x3F]; }
/*    */ 
/*    */   
/*    */   public Sample get(int delay, float partialTicks) {
/* 40 */     Sample sample = get(delay);
/* 41 */     Sample sampleOld = get(delay + 1);
/* 42 */     return new Sample(
/* 43 */         Mth.lerp(partialTicks, sampleOld.y, sample.y), 
/* 44 */         Mth.rotLerp(partialTicks, sampleOld.yRot, sample.yRot));
/*    */   }
/*    */   public static final class Sample extends Record { private final double y; private final float yRot;
/*    */     
/* 48 */     public Sample(double y, float yRot) { this.y = y; this.yRot = yRot; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 48 */       //   0	7	0	this	Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample; } public double y() { return this.y; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
/* 48 */       //   0	8	1	o	Ljava/lang/Object; } public float yRot() { return this.yRot; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\DragonFlightHistory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */