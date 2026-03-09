/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public final class WeatherCheck extends Record implements LootItemCondition {
/*    */   private final Optional<Boolean> isRaining;
/*    */   private final Optional<Boolean> isThundering;
/*    */   
/* 11 */   public WeatherCheck(Optional<Boolean> isRaining, Optional<Boolean> isThundering) { this.isRaining = isRaining; this.isThundering = isThundering; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck; } public Optional<Boolean> isRaining() { return this.isRaining; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Boolean> isThundering() { return this.isThundering; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final MapCodec<WeatherCheck> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 16 */         .optionalFieldOf("raining").forGetter(WeatherCheck::isRaining), Codec.BOOL
/* 17 */         .optionalFieldOf("thundering").forGetter(WeatherCheck::isThundering))
/* 18 */       .apply(i, WeatherCheck::new));
/*    */ 
/*    */ 
/*    */   
/* 22 */   public LootItemConditionType getType() { return LootItemConditions.WEATHER_CHECK; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 27 */     ServerLevel level = context.getLevel();
/*    */     
/* 29 */     if (this.isRaining.isPresent() && ((Boolean)this.isRaining.get()).booleanValue() != level.isRaining()) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     if (this.isThundering.isPresent() && ((Boolean)this.isThundering.get()).booleanValue() != level.isThundering()) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/* 41 */     private Optional<Boolean> isRaining = Optional.empty();
/* 42 */     private Optional<Boolean> isThundering = Optional.empty();
/*    */     
/*    */     public Builder setRaining(boolean raining) {
/* 45 */       this.isRaining = Optional.of(Boolean.valueOf(raining));
/* 46 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setThundering(boolean thundering) {
/* 50 */       this.isThundering = Optional.of(Boolean.valueOf(thundering));
/* 51 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 56 */     public WeatherCheck build() { return new WeatherCheck(this.isRaining, this.isThundering); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static Builder weather() { return new Builder(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\WeatherCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */