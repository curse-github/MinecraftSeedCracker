/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class UniformGenerator extends Record implements NumberProvider {
/*    */   private final NumberProvider min;
/*    */   private final NumberProvider max;
/*    */   
/* 12 */   public UniformGenerator(NumberProvider min, NumberProvider max) { this.min = min; this.max = max; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator; } public NumberProvider min() { return this.min; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/UniformGenerator;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public NumberProvider max() { return this.max; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<UniformGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(NumberProviders.CODEC
/* 17 */         .fieldOf("min").forGetter(UniformGenerator::min), NumberProviders.CODEC
/* 18 */         .fieldOf("max").forGetter(UniformGenerator::max))
/* 19 */       .apply(i, UniformGenerator::new));
/*    */ 
/*    */ 
/*    */   
/* 23 */   public LootNumberProviderType getType() { return NumberProviders.UNIFORM; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static UniformGenerator between(float min, float max) { return new UniformGenerator(ConstantValue.exactly(min), ConstantValue.exactly(max)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public int getInt(LootContext context) { return Mth.nextInt(context.getRandom(), this.min.getInt(context), this.max.getInt(context)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public float getFloat(LootContext context) { return Mth.nextFloat(context.getRandom(), this.min.getFloat(context), this.max.getFloat(context)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Set<ContextKey<?>> getReferencedContextParams() { return Sets.union(this.min.getReferencedContextParams(), this.max.getReferencedContextParams()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\UniformGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */