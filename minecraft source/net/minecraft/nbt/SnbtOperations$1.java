/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
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
/*    */ class null
/*    */   implements SnbtOperations.BuiltinOperation
/*    */ {
/*    */   public <T> T run(DynamicOps<T> ops, List<T> arguments, ParseState<StringReader> state) {
/* 34 */     Boolean result = convert(ops, arguments.getFirst());
/* 35 */     if (result == null) {
/* 36 */       state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_NUMBER_OR_BOOLEAN);
/* 37 */       return null;
/*    */     } 
/* 39 */     return (T)ops.createBoolean(result.booleanValue());
/*    */   }
/*    */   
/*    */   private static <T> Boolean convert(DynamicOps<T> ops, T arg) {
/* 43 */     Optional<Boolean> asBoolean = ops.getBooleanValue(arg).result();
/* 44 */     if (asBoolean.isPresent()) {
/* 45 */       return (Boolean)asBoolean.get();
/*    */     }
/* 47 */     Optional<Number> asNumber = ops.getNumberValue(arg).result();
/* 48 */     if (asNumber.isPresent()) {
/* 49 */       return Boolean.valueOf((((Number)asNumber.get()).doubleValue() != 0.0D));
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtOperations$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */