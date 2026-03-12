/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ final class ComponentWrapper
/*    */   extends Record
/*    */ {
/*    */   private final Identifier id;
/*    */   private final Predicate<ItemStack> presenceChecker;
/*    */   private final Decoder<? extends Predicate<ItemStack>> valueChecker;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 51 */   private ComponentWrapper(Identifier id, Predicate<ItemStack> presenceChecker, Decoder<? extends Predicate<ItemStack>> valueChecker) { this.id = id; this.presenceChecker = presenceChecker; this.valueChecker = valueChecker; } public Identifier id() { return this.id; } public Predicate<ItemStack> presenceChecker() { return this.presenceChecker; } public Decoder<? extends Predicate<ItemStack>> valueChecker() { return this.valueChecker; }
/*    */   public static <T> ComponentWrapper create(ImmutableStringReader reader, Identifier id, DataComponentType<T> type) throws CommandSyntaxException {
/* 53 */     Codec<T> codec = type.codec();
/* 54 */     if (codec == null) {
/* 55 */       throw ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
/*    */     }
/*    */     
/* 58 */     return new ComponentWrapper(id, itemStack -> 
/*    */         
/* 60 */         itemStack.has(type), codec
/* 61 */         .map(expected -> ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Predicate<ItemStack> decode(ImmutableStringReader reader, Dynamic<?> value) throws CommandSyntaxException {
/* 69 */     DataResult<? extends Predicate<ItemStack>> result = this.valueChecker.parse(value);
/* 70 */     return (Predicate)result.getOrThrow(message -> ItemPredicateArgument.ERROR_MALFORMED_COMPONENT.createWithContext(reader, this.id.toString(), message));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemPredicateArgument$ComponentWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */