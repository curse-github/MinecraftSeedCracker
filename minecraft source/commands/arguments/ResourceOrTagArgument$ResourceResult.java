/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ final class ResourceResult<T>
/*    */   extends Record
/*    */   implements ResourceOrTagArgument.Result<T>
/*    */ {
/*    */   private final Holder.Reference<T> value;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*    */   
/* 47 */   private ResourceResult(Holder.Reference<T> value) { this.value = value; } public Holder.Reference<T> value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*    */   
/* 50 */   public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() { return Either.left(this.value); }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*    */ 
/*    */   
/* 56 */   public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) { return this.value.key().isFor(registryKey) ? Optional.of(this) : Optional.empty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean test(Holder<T> holder) { return holder.equals(this.value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public String asPrintable() { return this.value.key().identifier().toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument$ResourceResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */