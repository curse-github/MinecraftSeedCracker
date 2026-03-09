/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugSubscription<T>
/*    */   extends Object
/*    */ {
/*    */   public static final int DOES_NOT_EXPIRE = 0;
/*    */   private final StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec;
/*    */   private final int expireAfterTicks;
/*    */   
/*    */   public DebugSubscription(StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec, int expireAfterTicks) {
/* 22 */     this.valueStreamCodec = valueStreamCodec;
/* 23 */     this.expireAfterTicks = expireAfterTicks;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public DebugSubscription(StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec) { this(valueStreamCodec, 0); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Update<T> packUpdate(T value) { return new Update(this, Optional.ofNullable(value)); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Update<T> emptyUpdate() { return new Update(this, Optional.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Event<T> packEvent(T value) { return new Event(this, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public String toString() { return Util.getRegisteredName(BuiltInRegistries.DEBUG_SUBSCRIPTION, this); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec() { return this.valueStreamCodec; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int expireAfterTicks() { return this.expireAfterTicks; }
/*    */   public static final class Update<T> extends Record { private final DebugSubscription<T> subscription; private final Optional<T> value;
/*    */     
/* 55 */     public Update(DebugSubscription<T> subscription, Optional<T> value) { this.subscription = subscription; this.value = value; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugSubscription$Update;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #55	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 55 */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update<TT;>; } public DebugSubscription<T> subscription() { return this.subscription; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugSubscription$Update;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #55	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugSubscription$Update;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #55	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 55 */       //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Update<TT;>; } public Optional<T> value() { return this.value; }
/*    */ 
/*    */ 
/*    */     
/* 59 */     public static final StreamCodec<RegistryFriendlyByteBuf, Update<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
/* 60 */       .dispatch(Update::subscription, Update::streamCodec);
/*    */     
/*    */     private static <T> StreamCodec<? super RegistryFriendlyByteBuf, Update<T>> streamCodec(DebugSubscription<T> subscription) {
/* 63 */       return ByteBufCodecs.optional((StreamCodec)Objects.requireNonNull(subscription.valueStreamCodec))
/* 64 */         .map(value -> new Update(subscription, value), Update::value);
/*    */     } }
/*    */   public static final class Event<T> extends Record { private final DebugSubscription<T> subscription; private final T value;
/*    */     
/* 68 */     public Event(DebugSubscription<T> subscription, T value) { this.subscription = subscription; this.value = value; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugSubscription$Event;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event<TT;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugSubscription$Event;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugSubscription$Event;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 68 */       //   0	8	0	this	Lnet/minecraft/util/debug/DebugSubscription$Event<TT;>; } public DebugSubscription<T> subscription() { return this.subscription; } public T value() { return (T)this.value; }
/*    */ 
/*    */ 
/*    */     
/* 72 */     public static final StreamCodec<RegistryFriendlyByteBuf, Event<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
/* 73 */       .dispatch(Event::subscription, Event::streamCodec);
/*    */ 
/*    */     
/* 76 */     private static <T> StreamCodec<? super RegistryFriendlyByteBuf, Event<T>> streamCodec(DebugSubscription<T> subscription) { return ((StreamCodec)Objects.requireNonNull(subscription.valueStreamCodec)).map(value -> new Event(subscription, value), Event::value); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugSubscription.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */