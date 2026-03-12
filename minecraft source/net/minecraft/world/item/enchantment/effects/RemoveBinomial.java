/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class RemoveBinomial extends Record implements EnchantmentValueEffect {
/*    */   private final LevelBasedValue chance;
/*    */   
/*  8 */   public RemoveBinomial(LevelBasedValue chance) { this.chance = chance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/RemoveBinomial;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/RemoveBinomial; } public LevelBasedValue chance() { return this.chance; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/RemoveBinomial;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/RemoveBinomial; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/RemoveBinomial;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/RemoveBinomial;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final MapCodec<RemoveBinomial> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 10 */         .fieldOf("chance").forGetter(RemoveBinomial::chance))
/* 11 */       .apply(i, RemoveBinomial::new));
/*    */ 
/*    */   
/*    */   public float process(int level, RandomSource random, float n) {
/* 15 */     float p = this.chance.calculate(level);
/* 16 */     int drop = 0;
/* 17 */     if (n <= 128.0F || n * p < 20.0F || n * (1.0F - p) < 20.0F) {
/*    */       
/* 19 */       for (int y = 0; y < n; y++) {
/* 20 */         if (random.nextFloat() < p) {
/* 21 */           drop++;
/*    */         }
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 27 */       double miu = Math.floor((n * p));
/* 28 */       double sigma = Math.sqrt((n * p * (1.0F - p)));
/* 29 */       drop = (int)Math.round(miu + random.nextGaussian() * sigma);
/* 30 */       drop = Math.clamp(drop, 0, (int)n);
/*    */     } 
/* 32 */     return n - drop;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public MapCodec<RemoveBinomial> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\RemoveBinomial.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */