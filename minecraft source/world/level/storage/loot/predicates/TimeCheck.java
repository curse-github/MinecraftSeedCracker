/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class TimeCheck extends Record implements LootItemCondition {
/*    */   private final Optional<Long> period;
/*    */   private final IntRange value;
/*    */   
/* 14 */   public TimeCheck(Optional<Long> period, IntRange value) { this.period = period; this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck; } public Optional<Long> period() { return this.period; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public IntRange value() { return this.value; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final MapCodec<TimeCheck> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.LONG
/* 19 */         .optionalFieldOf("period").forGetter(TimeCheck::period), IntRange.CODEC
/* 20 */         .fieldOf("value").forGetter(TimeCheck::value))
/* 21 */       .apply(i, TimeCheck::new));
/*    */ 
/*    */ 
/*    */   
/* 25 */   public LootItemConditionType getType() { return LootItemConditions.TIME_CHECK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.value.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 35 */     ServerLevel level = context.getLevel();
/*    */     
/* 37 */     long time = level.getDayTime();
/*    */     
/* 39 */     if (this.period.isPresent()) {
/* 40 */       time %= ((Long)this.period.get()).longValue();
/*    */     }
/*    */     
/* 43 */     return this.value.test(context, (int)time);
/*    */   }
/*    */   public static class Builder implements LootItemCondition.Builder { private Optional<Long> period; private final IntRange value;
/*    */     public Builder(IntRange value) {
/* 47 */       this.period = Optional.empty();
/*    */ 
/*    */ 
/*    */       
/* 51 */       this.value = value;
/*    */     }
/*    */     
/*    */     public Builder setPeriod(long period) {
/* 55 */       this.period = Optional.of(Long.valueOf(period));
/* 56 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 61 */     public TimeCheck build() { return new TimeCheck(this.period, this.value); } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public static Builder time(IntRange period) { return new Builder(period); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\TimeCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */