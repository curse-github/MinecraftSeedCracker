/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
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
/*    */ final class ResourceResult<T>
/*    */   extends Record
/*    */   implements ResourceOrTagKeyArgument.Result<T>
/*    */ {
/*    */   private final ResourceKey<T> key;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*    */   
/* 44 */   private ResourceResult(ResourceKey<T> key) { this.key = key; } public ResourceKey<T> key() { return this.key; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*    */   
/* 47 */   public Either<ResourceKey<T>, TagKey<T>> unwrap() { return Either.left(this.key); }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*    */   
/* 52 */   public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) { return this.key.cast(registryKey).map(ResourceResult::new); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean test(Holder<T> holder) { return holder.is(this.key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public String asPrintable() { return this.key.identifier().toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrTagKeyArgument$ResourceResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */