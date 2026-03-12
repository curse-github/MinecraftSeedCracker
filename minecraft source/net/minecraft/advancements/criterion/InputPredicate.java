/*    */ package net.minecraft.advancements.criterion;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class InputPredicate extends Record {
/*    */   private final Optional<Boolean> forward;
/*    */   private final Optional<Boolean> backward;
/*    */   private final Optional<Boolean> left;
/*    */   
/*  9 */   public InputPredicate(Optional<Boolean> forward, Optional<Boolean> backward, Optional<Boolean> left, Optional<Boolean> right, Optional<Boolean> jump, Optional<Boolean> sneak, Optional<Boolean> sprint) { this.forward = forward; this.backward = backward; this.left = left; this.right = right; this.jump = jump; this.sneak = sneak; this.sprint = sprint; } private final Optional<Boolean> right; private final Optional<Boolean> jump; private final Optional<Boolean> sneak; private final Optional<Boolean> sprint; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InputPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InputPredicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InputPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InputPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InputPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/InputPredicate;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Boolean> forward() { return this.forward; } public Optional<Boolean> backward() { return this.backward; } public Optional<Boolean> left() { return this.left; } public Optional<Boolean> right() { return this.right; } public Optional<Boolean> jump() { return this.jump; } public Optional<Boolean> sneak() { return this.sneak; } public Optional<Boolean> sprint() { return this.sprint; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final Codec<InputPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 19 */         .optionalFieldOf("forward").forGetter(InputPredicate::forward), Codec.BOOL
/* 20 */         .optionalFieldOf("backward").forGetter(InputPredicate::backward), Codec.BOOL
/* 21 */         .optionalFieldOf("left").forGetter(InputPredicate::left), Codec.BOOL
/* 22 */         .optionalFieldOf("right").forGetter(InputPredicate::right), Codec.BOOL
/* 23 */         .optionalFieldOf("jump").forGetter(InputPredicate::jump), Codec.BOOL
/* 24 */         .optionalFieldOf("sneak").forGetter(InputPredicate::sneak), Codec.BOOL
/* 25 */         .optionalFieldOf("sprint").forGetter(InputPredicate::sprint))
/* 26 */       .apply(i, InputPredicate::new));
/*    */   
/*    */   public boolean matches(Input input) {
/* 29 */     return (matches(this.forward, input.forward()) && 
/* 30 */       matches(this.backward, input.backward()) && 
/* 31 */       matches(this.left, input.left()) && 
/* 32 */       matches(this.right, input.right()) && 
/* 33 */       matches(this.jump, input.jump()) && 
/* 34 */       matches(this.sneak, input.shift()) && 
/* 35 */       matches(this.sprint, input.sprint()));
/*    */   }
/*    */ 
/*    */   
/* 39 */   private boolean matches(Optional<Boolean> match, boolean value) { return ((Boolean)match.map(b -> Boolean.valueOf((b.booleanValue() == value))).orElse(Boolean.valueOf(true))).booleanValue(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\InputPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */