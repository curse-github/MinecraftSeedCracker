/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
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
/*    */ final class PositionPredicate
/*    */   extends Record
/*    */ {
/*    */   private final MinMaxBounds.Doubles x;
/*    */   private final MinMaxBounds.Doubles y;
/*    */   private final MinMaxBounds.Doubles z;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 79 */   private PositionPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) { this.x = x; this.y = y; this.z = z; } public MinMaxBounds.Doubles x() { return this.x; } public MinMaxBounds.Doubles y() { return this.y; } public MinMaxBounds.Doubles z() { return this.z; }
/* 80 */   public static final Codec<PositionPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Doubles.CODEC
/* 81 */         .optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::x), MinMaxBounds.Doubles.CODEC
/* 82 */         .optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::y), MinMaxBounds.Doubles.CODEC
/* 83 */         .optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::z))
/* 84 */       .apply(i, PositionPredicate::new));
/*    */   
/*    */   private static Optional<PositionPredicate> of(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
/* 87 */     if (x.isAny() && y.isAny() && z.isAny()) {
/* 88 */       return Optional.empty();
/*    */     }
/* 90 */     return Optional.of(new PositionPredicate(x, y, z));
/*    */   }
/*    */ 
/*    */   
/* 94 */   public boolean matches(double x, double y, double z) { return (this.x.matches(x) && this.y.matches(y) && this.z.matches(z)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LocationPredicate$PositionPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */