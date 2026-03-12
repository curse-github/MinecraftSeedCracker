/*    */ package net.minecraft.core.component.predicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ 
/*    */ public final class DamagePredicate extends Record implements DataComponentPredicate {
/*    */   private final MinMaxBounds.Ints durability;
/*    */   private final MinMaxBounds.Ints damage;
/*    */   
/*  9 */   public DamagePredicate(MinMaxBounds.Ints durability, MinMaxBounds.Ints damage) { this.durability = durability; this.damage = damage; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/DamagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate; } public MinMaxBounds.Ints durability() { return this.durability; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/DamagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/DamagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints damage() { return this.damage; }
/* 10 */   public static final Codec<DamagePredicate> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Ints.CODEC
/* 11 */         .optionalFieldOf("durability", MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::durability), MinMaxBounds.Ints.CODEC
/* 12 */         .optionalFieldOf("damage", MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::damage))
/* 13 */       .apply(i, DamagePredicate::new));
/*    */ 
/*    */   
/*    */   public boolean matches(DataComponentGetter components) {
/* 17 */     Integer damage = (Integer)components.get(DataComponents.DAMAGE);
/* 18 */     if (damage == null) {
/* 19 */       return false;
/*    */     }
/*    */     
/* 22 */     int maxDamage = ((Integer)components.getOrDefault(DataComponents.MAX_DAMAGE, Integer.valueOf(0))).intValue();
/*    */     
/* 24 */     if (!this.durability.matches(maxDamage - damage.intValue())) {
/* 25 */       return false;
/*    */     }
/* 27 */     if (!this.damage.matches(damage.intValue())) {
/* 28 */       return false;
/*    */     }
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 34 */   public static DamagePredicate durability(MinMaxBounds.Ints range) { return new DamagePredicate(range, MinMaxBounds.Ints.ANY); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\DamagePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */