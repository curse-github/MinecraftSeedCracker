/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
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
/*    */ public final class Lookup
/*    */   extends Record
/*    */   implements LevelBasedValue
/*    */ {
/*    */   private final List<Float> values;
/*    */   private final LevelBasedValue fallback;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 70 */   public Lookup(List<Float> values, LevelBasedValue fallback) { this.values = values; this.fallback = fallback; } public List<Float> values() { return this.values; } public LevelBasedValue fallback() { return this.fallback; }
/* 71 */   public static final MapCodec<Lookup> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 72 */         .listOf().fieldOf("values").forGetter(Lookup::values), LevelBasedValue.CODEC
/* 73 */         .fieldOf("fallback").forGetter(Lookup::fallback))
/* 74 */       .apply(i, Lookup::new));
/*    */ 
/*    */ 
/*    */   
/* 78 */   public float calculate(int level) { return (level <= this.values.size()) ? ((Float)this.values.get(level - 1)).floatValue() : this.fallback.calculate(level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   public MapCodec<Lookup> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Lookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */