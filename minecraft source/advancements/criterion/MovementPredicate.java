/*    */ package net.minecraft.advancements.criterion;
/*    */ public final class MovementPredicate extends Record {
/*    */   private final MinMaxBounds.Doubles x;
/*    */   private final MinMaxBounds.Doubles y;
/*    */   private final MinMaxBounds.Doubles z;
/*    */   
/*  7 */   public MovementPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles speed, MinMaxBounds.Doubles horizontalSpeed, MinMaxBounds.Doubles verticalSpeed, MinMaxBounds.Doubles fallDistance) { this.x = x; this.y = y; this.z = z; this.speed = speed; this.horizontalSpeed = horizontalSpeed; this.verticalSpeed = verticalSpeed; this.fallDistance = fallDistance; } private final MinMaxBounds.Doubles speed; private final MinMaxBounds.Doubles horizontalSpeed; private final MinMaxBounds.Doubles verticalSpeed; private final MinMaxBounds.Doubles fallDistance; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MovementPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MovementPredicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MovementPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MovementPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MovementPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MovementPredicate;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Doubles x() { return this.x; } public MinMaxBounds.Doubles y() { return this.y; } public MinMaxBounds.Doubles z() { return this.z; } public MinMaxBounds.Doubles speed() { return this.speed; } public MinMaxBounds.Doubles horizontalSpeed() { return this.horizontalSpeed; } public MinMaxBounds.Doubles verticalSpeed() { return this.verticalSpeed; } public MinMaxBounds.Doubles fallDistance() { return this.fallDistance; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final Codec<MovementPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Doubles.CODEC
/* 17 */         .optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::x), MinMaxBounds.Doubles.CODEC
/* 18 */         .optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::y), MinMaxBounds.Doubles.CODEC
/* 19 */         .optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::z), MinMaxBounds.Doubles.CODEC
/* 20 */         .optionalFieldOf("speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::speed), MinMaxBounds.Doubles.CODEC
/* 21 */         .optionalFieldOf("horizontal_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::horizontalSpeed), MinMaxBounds.Doubles.CODEC
/* 22 */         .optionalFieldOf("vertical_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::verticalSpeed), MinMaxBounds.Doubles.CODEC
/* 23 */         .optionalFieldOf("fall_distance", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::fallDistance))
/* 24 */       .apply(i, MovementPredicate::new));
/*    */ 
/*    */   
/* 27 */   public static MovementPredicate speed(MinMaxBounds.Doubles bounds) { return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static MovementPredicate horizontalSpeed(MinMaxBounds.Doubles bounds) { return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static MovementPredicate verticalSpeed(MinMaxBounds.Doubles bounds) { return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static MovementPredicate fallDistance(MinMaxBounds.Doubles bounds) { return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds); }
/*    */ 
/*    */   
/*    */   public boolean matches(double x, double y, double z, double fallDistance) {
/* 43 */     if (!this.x.matches(x) || !this.y.matches(y) || !this.z.matches(z)) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     double speedSqr = Mth.lengthSquared(x, y, z);
/* 48 */     if (!this.speed.matchesSqr(speedSqr)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     double horizontalSpeedSqr = Mth.lengthSquared(x, z);
/* 53 */     if (!this.horizontalSpeed.matchesSqr(horizontalSpeedSqr)) {
/* 54 */       return false;
/*    */     }
/*    */     
/* 57 */     double verticalSpeed = Math.abs(y);
/* 58 */     if (!this.verticalSpeed.matches(verticalSpeed)) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     if (!this.fallDistance.matches(fallDistance)) {
/* 63 */       return false;
/*    */     }
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MovementPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */