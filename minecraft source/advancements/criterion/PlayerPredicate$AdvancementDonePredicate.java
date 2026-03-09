/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.AdvancementProgress;
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
/*    */ final class AdvancementDonePredicate
/*    */   extends Record
/*    */   implements PlayerPredicate.AdvancementPredicate
/*    */ {
/*    */   private final boolean state;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 79 */   private AdvancementDonePredicate(boolean state) { this.state = state; } public boolean state() { return this.state; }
/* 80 */   public static final Codec<AdvancementDonePredicate> CODEC = Codec.BOOL.xmap(AdvancementDonePredicate::new, AdvancementDonePredicate::state);
/*    */ 
/*    */ 
/*    */   
/* 84 */   public boolean test(AdvancementProgress progress) { return (progress.isDone() == this.state); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerPredicate$AdvancementDonePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */