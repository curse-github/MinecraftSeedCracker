/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
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
/*    */ public class StringRepresentableCodec<S extends StringRepresentable>
/*    */   extends Object
/*    */   implements Codec<S>
/*    */ {
/*    */   private final Codec<S> codec;
/*    */   
/*    */   public StringRepresentableCodec(S[] valueArray, Function<String, S> nameResolver, ToIntFunction<S> idResolver) {
/* 28 */     this.codec = ExtraCodecs.orCompressed(
/* 29 */         Codec.stringResolver(StringRepresentable::getSerializedName, nameResolver), 
/* 30 */         ExtraCodecs.idResolverCodec(idResolver, i -> (i >= 0 && i < valueArray.length) ? valueArray[i] : null, -1));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) { return this.codec.decode(ops, input); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) { return this.codec.encode(input, ops, prefix); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StringRepresentable$StringRepresentableCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */