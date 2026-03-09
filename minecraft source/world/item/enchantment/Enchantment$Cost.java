/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
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
/*    */ 
/*    */ 
/*    */ public final class Cost
/*    */   extends Record
/*    */ {
/*    */   private final int base;
/*    */   private final int perLevelAboveFirst;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 69 */   public Cost(int base, int perLevelAboveFirst) { this.base = base; this.perLevelAboveFirst = perLevelAboveFirst; } public int base() { return this.base; } public int perLevelAboveFirst() { return this.perLevelAboveFirst; }
/* 70 */   public static final Codec<Cost> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 71 */         .fieldOf("base").forGetter(Cost::base), Codec.INT
/* 72 */         .fieldOf("per_level_above_first").forGetter(Cost::perLevelAboveFirst))
/* 73 */       .apply(i, Cost::new));
/*    */ 
/*    */   
/* 76 */   public int calculate(int level) { return this.base + this.perLevelAboveFirst * (level - 1); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantment$Cost.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */