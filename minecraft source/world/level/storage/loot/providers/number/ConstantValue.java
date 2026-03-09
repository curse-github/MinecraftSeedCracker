/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class ConstantValue extends Record implements NumberProvider {
/*  8 */   public ConstantValue(float value) { this.value = value; } private final float value; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/ConstantValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ConstantValue; } public float value() { return this.value; }
/*  9 */   public static final MapCodec<ConstantValue> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 10 */         .fieldOf("value").forGetter(ConstantValue::value))
/* 11 */       .apply(i, ConstantValue::new));
/*    */   
/* 13 */   public static final Codec<ConstantValue> INLINE_CODEC = Codec.FLOAT.xmap(ConstantValue::new, ConstantValue::value);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public LootNumberProviderType getType() { return NumberProviders.CONSTANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public float getFloat(LootContext random) { return this.value; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static ConstantValue exactly(float value) { return new ConstantValue(value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 32 */     if (this == o) {
/* 33 */       return true;
/*    */     }
/* 35 */     if (o == null || getClass() != o.getClass()) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     return (Float.compare(((ConstantValue)o).value, this.value) == 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int hashCode() { return (this.value != 0.0F) ? Float.floatToIntBits(this.value) : 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\ConstantValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */