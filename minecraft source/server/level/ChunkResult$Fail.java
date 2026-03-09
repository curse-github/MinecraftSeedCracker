/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Fail<T>
/*     */   extends Record
/*     */   implements ChunkResult<T>
/*     */ {
/*     */   private final Supplier<String> error;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Fail;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #72	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; }
/*     */   
/*  72 */   public Fail(Supplier<String> error) { this.error = error; } public Supplier<String> error() { return this.error; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Fail;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #72	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; }
/*     */   
/*  75 */   public boolean isSuccess() { return false; }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Fail;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #72	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; }
/*     */   
/*  80 */   public T orElse(T orElse) { return orElse; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public String getError() { return (String)this.error.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public ChunkResult<T> ifSuccess(Consumer<T> consumer) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public <R> ChunkResult<R> map(Function<T, R> map) { return new Fail(this.error); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E { throw (Throwable)exceptionSupplier.get(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkResult$Fail.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */