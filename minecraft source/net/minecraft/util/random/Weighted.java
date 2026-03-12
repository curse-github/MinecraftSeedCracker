/*    */ package net.minecraft.util.random;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class Weighted<T> extends Record {
/*    */   private final T value;
/*    */   private final int weight;
/*    */   
/* 17 */   public T value() { return (T)this.value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/random/Weighted;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/random/Weighted;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/random/Weighted<TT;>; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/random/Weighted;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/random/Weighted;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/random/Weighted<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/random/Weighted;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/random/Weighted;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 17 */     //   0	8	0	this	Lnet/minecraft/util/random/Weighted<TT;>; } public int weight() { return this.weight; }
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public Weighted(T value, int weight) {
/* 21 */     if (weight < 0) {
/* 22 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Weight should be >= 0"));
/*    */     }
/*    */     
/* 25 */     if (weight == 0 && SharedConstants.IS_RUNNING_IN_IDE)
/* 26 */       LOGGER.warn("Found 0 weight, make sure this is intentional!"); 
/*    */     this.value = value;
/*    */     this.weight = weight;
/*    */   }
/*    */   
/* 31 */   public static <E> Codec<Weighted<E>> codec(Codec<E> elementCodec) { return codec(elementCodec.fieldOf("data")); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static <E> Codec<Weighted<E>> codec(MapCodec<E> elementCodec) { return RecordCodecBuilder.create(i -> i.group(elementCodec
/* 36 */           .forGetter(Weighted::value), ExtraCodecs.NON_NEGATIVE_INT
/* 37 */           .fieldOf("weight").forGetter(Weighted::weight))
/* 38 */         .apply(i, Weighted::new)); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static <B extends io.netty.buffer.ByteBuf, T> StreamCodec<B, Weighted<T>> streamCodec(StreamCodec<B, T> valueCodec) { return StreamCodec.composite(valueCodec, Weighted::value, ByteBufCodecs.VAR_INT, Weighted::weight, Weighted::new); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public <U> Weighted<U> map(Function<T, U> function) { return new Weighted(function.apply(value()), this.weight); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\random\Weighted.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */