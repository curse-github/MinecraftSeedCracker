/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ChunkResult<T>
/*     */ {
/*  11 */   static <T> ChunkResult<T> of(T value) { return new Success(value); }
/*     */ 
/*     */ 
/*     */   
/*  15 */   static <T> ChunkResult<T> error(String error) { return error(() -> error); }
/*     */ 
/*     */ 
/*     */   
/*  19 */   static <T> ChunkResult<T> error(Supplier<String> errorSupplier) { return new Fail(errorSupplier); }
/*     */   boolean isSuccess();
/*     */   
/*     */   T orElse(T paramT);
/*     */   
/*     */   String getError();
/*     */   
/*     */   static <R> R orElse(ChunkResult<? extends R> chunkResult, R orElse) {
/*  27 */     R result = (R)chunkResult.orElse(null);
/*  28 */     return (result != null) ? result : orElse;
/*     */   }
/*     */   ChunkResult<T> ifSuccess(Consumer<T> paramConsumer);
/*     */   <R> ChunkResult<R> map(Function<T, R> paramFunction);
/*     */   
/*     */   <E extends Throwable> T orElseThrow(Supplier<E> paramSupplier) throws E;
/*     */   
/*     */   public static final class Success<T> extends Record implements ChunkResult<T> { private final T value;
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Success;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>; }
/*     */     
/*  39 */     public Success(T value) { this.value = value; } public T value() { return (T)this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Success;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Success;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>; }
/*  42 */     public boolean isSuccess() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     public T orElse(T orElse) { return (T)this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     public String getError() { return null; }
/*     */ 
/*     */ 
/*     */     
/*     */     public ChunkResult<T> ifSuccess(Consumer<T> consumer) {
/*  57 */       consumer.accept(this.value);
/*  58 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  63 */     public <R> ChunkResult<R> map(Function<T, R> map) { return new Success(map.apply(this.value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E { return (T)this.value; } }
/*     */   
/*     */   public static final class Fail<T> extends Record implements ChunkResult<T> { private final Supplier<String> error;
/*     */     
/*  72 */     public Fail(Supplier<String> error) { this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Fail;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  72 */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; } public Supplier<String> error() { return this.error; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Fail;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Fail;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; }
/*  75 */     public boolean isSuccess() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     public T orElse(T orElse) { return orElse; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     public String getError() { return (String)this.error.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     public ChunkResult<T> ifSuccess(Consumer<T> consumer) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     public <R> ChunkResult<R> map(Function<T, R> map) { return new Fail(this.error); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E { throw (Throwable)exceptionSupplier.get(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */