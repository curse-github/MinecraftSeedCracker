/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ 
/*    */ 
/*    */ public class SnbtOperations
/*    */ {
/* 24 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_STRING_UUID = DelayedException.create(new SimpleCommandExceptionType(Component.translatable("snbt.parser.expected_string_uuid")));
/* 25 */   private static final DelayedException<CommandSyntaxException> ERROR_EXPECTED_NUMBER_OR_BOOLEAN = DelayedException.create(new SimpleCommandExceptionType(Component.translatable("snbt.parser.expected_number_or_boolean")));
/*    */   
/*    */   public static final String BUILTIN_TRUE = "true";
/*    */   public static final String BUILTIN_FALSE = "false";
/*    */   
/* 30 */   public static final Map<BuiltinKey, BuiltinOperation> BUILTIN_OPERATIONS = Map.of(new BuiltinKey("bool", 1), new BuiltinOperation()
/*    */       {
/*    */         public <T> T run(DynamicOps<T> ops, List<T> arguments, ParseState<StringReader> state)
/*    */         {
/* 34 */           Boolean result = SnbtOperations.null.convert(ops, arguments.getFirst());
/* 35 */           if (result == null) {
/* 36 */             state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_NUMBER_OR_BOOLEAN);
/* 37 */             return null;
/*    */           } 
/* 39 */           return (T)ops.createBoolean(result.booleanValue());
/*    */         }
/*    */         
/*    */         private static <T> Boolean convert(DynamicOps<T> ops, T arg) {
/* 43 */           Optional<Boolean> asBoolean = ops.getBooleanValue(arg).result();
/* 44 */           if (asBoolean.isPresent()) {
/* 45 */             return (Boolean)asBoolean.get();
/*    */           }
/* 47 */           Optional<Number> asNumber = ops.getNumberValue(arg).result();
/* 48 */           if (asNumber.isPresent()) {
/* 49 */             return Boolean.valueOf((((Number)asNumber.get()).doubleValue() != 0.0D));
/*    */           }
/* 51 */           return null;
/*    */         }
/*    */       }new BuiltinKey("uuid", 1), new BuiltinOperation()
/*    */       {
/*    */         public <T> T run(DynamicOps<T> ops, List<T> arguments, ParseState<StringReader> state) {
/*    */           UUID uuid;
/* 57 */           Optional<String> arg = ops.getStringValue(arguments.getFirst()).result();
/* 58 */           if (arg.isEmpty()) {
/* 59 */             state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
/* 60 */             return null;
/*    */           } 
/*    */ 
/*    */           
/*    */           try {
/* 65 */             uuid = UUID.fromString((String)arg.get());
/* 66 */           } catch (IllegalArgumentException e) {
/* 67 */             state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
/* 68 */             return null;
/*    */           } 
/* 70 */           return (T)ops.createIntList(IntStream.of(UUIDUtil.uuidToIntArray(uuid)));
/*    */         }
/*    */       });
/*    */ 
/*    */   
/* 75 */   public static final SuggestionSupplier<StringReader> BUILTIN_IDS = new SuggestionSupplier<StringReader>() {
/* 76 */       private final Set<String> keys = (Set)Stream.concat(
/* 77 */           Stream.of(new String[] { "false", "true" }, ), SnbtOperations.BUILTIN_OPERATIONS
/* 78 */           .keySet().stream().map(SnbtOperations.BuiltinKey::id))
/* 79 */         .collect(Collectors.toSet());
/*    */ 
/*    */ 
/*    */       
/* 83 */       public Stream<String> possibleValues(ParseState<StringReader> state) { return this.keys.stream(); }
/*    */     };
/*    */   public static final class BuiltinKey extends Record { private final String id; private final int argCount;
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #91	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/nbt/SnbtOperations$BuiltinKey; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #91	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */     
/* 91 */     public BuiltinKey(String id, int argCount) { this.id = id; this.argCount = argCount; } public String id() { return this.id; } public int argCount() { return this.argCount; }
/*    */ 
/*    */     
/* 94 */     public String toString() { return this.id + "/" + this.id; } }
/*    */ 
/*    */   
/*    */   public static interface BuiltinOperation {
/*    */     <T> T run(DynamicOps<T> param1DynamicOps, List<T> param1List, ParseState<StringReader> param1ParseState);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtOperations.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */