/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.VarInt;
/*    */ 
/*    */ public class IdDispatchCodec<B extends ByteBuf, V, T>
/*    */   extends Object
/*    */   implements StreamCodec<B, V>
/*    */ {
/*    */   private static final int UNKNOWN_TYPE = -1;
/*    */   private final Function<V, ? extends T> typeGetter;
/*    */   private final List<Entry<B, V, T>> byId;
/*    */   private final Object2IntMap<T> toId;
/*    */   
/*    */   private IdDispatchCodec(Function<V, ? extends T> typeGetter, List<Entry<B, V, T>> byId, Object2IntMap<T> toId) {
/* 23 */     this.typeGetter = typeGetter;
/* 24 */     this.byId = byId;
/* 25 */     this.toId = toId;
/*    */   }
/*    */ 
/*    */   
/*    */   public V decode(B input) {
/* 30 */     int id = VarInt.read(input);
/* 31 */     if (id < 0 || id >= this.byId.size()) {
/* 32 */       throw new DecoderException("Received unknown packet id " + id);
/*    */     }
/* 34 */     Entry<B, V, T> entry = (Entry)this.byId.get(id);
/*    */     try {
/* 36 */       return (V)entry.serializer.decode(input);
/* 37 */     } catch (Exception e) {
/* 38 */       if (e instanceof DontDecorateException) {
/* 39 */         throw e;
/*    */       }
/* 41 */       throw new DecoderException("Failed to decode packet '" + String.valueOf(entry.type) + "'", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(B output, V value) {
/* 47 */     T type = (T)this.typeGetter.apply(value);
/* 48 */     int id = this.toId.getOrDefault(type, -1);
/* 49 */     if (id == -1) {
/* 50 */       throw new EncoderException("Sending unknown packet '" + String.valueOf(type) + "'");
/*    */     }
/* 52 */     VarInt.write(output, id);
/* 53 */     Entry<B, V, T> entry = (Entry)this.byId.get(id);
/*    */     try {
/* 55 */       StreamCodec<? super B, V> codec = entry.serializer;
/* 56 */       codec.encode(output, value);
/* 57 */     } catch (Exception e) {
/* 58 */       if (e instanceof DontDecorateException) {
/* 59 */         throw e;
/*    */       }
/* 61 */       throw new EncoderException("Failed to encode packet '" + String.valueOf(type) + "'", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 66 */   public static <B extends ByteBuf, V, T> Builder<B, V, T> builder(Function<V, ? extends T> typeGetter) { return new Builder(typeGetter); }
/*    */   public static class Builder<B extends ByteBuf, V, T> extends Object { private final List<IdDispatchCodec.Entry<B, V, T>> entries; private final Function<V, ? extends T> typeGetter;
/*    */     
/*    */     private Builder(Function<V, ? extends T> typeGetter) {
/* 70 */       this.entries = new ArrayList();
/*    */ 
/*    */ 
/*    */       
/* 74 */       this.typeGetter = typeGetter;
/*    */     }
/*    */     
/*    */     public Builder<B, V, T> add(T type, StreamCodec<? super B, ? extends V> serializer) {
/* 78 */       this.entries.add(new IdDispatchCodec.Entry(serializer, type));
/* 79 */       return this;
/*    */     }
/*    */     
/*    */     public IdDispatchCodec<B, V, T> build() {
/* 83 */       Object2IntOpenHashMap<T> toId = new Object2IntOpenHashMap<T>();
/* 84 */       toId.defaultReturnValue(-2);
/*    */       
/* 86 */       for (IdDispatchCodec.Entry<B, V, T> entry : this.entries) {
/* 87 */         int id = toId.size();
/* 88 */         int previous = toId.putIfAbsent(entry.type, id);
/* 89 */         if (previous != -2) {
/* 90 */           throw new IllegalStateException("Duplicate registration for type " + String.valueOf(entry.type));
/*    */         }
/*    */       } 
/*    */       
/* 94 */       return new IdDispatchCodec(this.typeGetter, List.copyOf(this.entries), toId);
/*    */     } }
/*    */   public static interface DontDecorateException {}
/*    */   private static final class Entry<B, V, T> extends Record { private final StreamCodec<? super B, ? extends V> serializer; private final T type;
/* 98 */     private Entry(StreamCodec<? super B, ? extends V> serializer, T type) { this.serializer = serializer; this.type = type; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/codec/IdDispatchCodec$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #98	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 98 */       //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry<TB;TV;TT;>; } public StreamCodec<? super B, ? extends V> serializer() { return this.serializer; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/codec/IdDispatchCodec$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #98	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry<TB;TV;TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/codec/IdDispatchCodec$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #98	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 98 */       //   0	8	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry<TB;TV;TT;>; } public T type() { return (T)this.type; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\codec\IdDispatchCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */