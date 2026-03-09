/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.monster.Slime;
/*    */ 
/*    */ public final class SlimePredicate extends Record implements EntitySubPredicate {
/*    */   private final MinMaxBounds.Ints size;
/*    */   
/* 11 */   public SlimePredicate(MinMaxBounds.Ints size) { this.size = size; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SlimePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SlimePredicate; } public MinMaxBounds.Ints size() { return this.size; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SlimePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SlimePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SlimePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/SlimePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final MapCodec<SlimePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(MinMaxBounds.Ints.CODEC
/* 13 */         .optionalFieldOf("size", MinMaxBounds.Ints.ANY).forGetter(SlimePredicate::size))
/* 14 */       .apply(i, SlimePredicate::new));
/*    */ 
/*    */   
/* 17 */   public static SlimePredicate sized(MinMaxBounds.Ints size) { return new SlimePredicate(size); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 22 */     if (entity instanceof Slime) { Slime slime = (Slime)entity;
/* 23 */       return this.size.matches(slime.getSize()); }
/*    */     
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<SlimePredicate> codec() { return EntitySubPredicates.SLIME; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SlimePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */