/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
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
/*    */ public final class LocationWrapper
/*    */   extends Record
/*    */ {
/*    */   private final Optional<LocationPredicate> located;
/*    */   private final Optional<LocationPredicate> steppingOn;
/*    */   private final Optional<LocationPredicate> affectsMovement;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 47 */   public LocationWrapper(Optional<LocationPredicate> located, Optional<LocationPredicate> steppingOn, Optional<LocationPredicate> affectsMovement) { this.located = located; this.steppingOn = steppingOn; this.affectsMovement = affectsMovement; } public Optional<LocationPredicate> located() { return this.located; } public Optional<LocationPredicate> steppingOn() { return this.steppingOn; } public Optional<LocationPredicate> affectsMovement() { return this.affectsMovement; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static final MapCodec<LocationWrapper> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LocationPredicate.CODEC
/* 53 */         .optionalFieldOf("location").forGetter(LocationWrapper::located), LocationPredicate.CODEC
/* 54 */         .optionalFieldOf("stepping_on").forGetter(LocationWrapper::steppingOn), LocationPredicate.CODEC
/* 55 */         .optionalFieldOf("movement_affected_by").forGetter(LocationWrapper::affectsMovement))
/* 56 */       .apply(i, LocationWrapper::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityPredicate$LocationWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */