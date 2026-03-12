/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.UUIDUtil;
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
/*    */     UUID uuid;
/* 57 */     Optional<String> arg = ops.getStringValue(arguments.getFirst()).result();
/* 58 */     if (arg.isEmpty()) {
/* 59 */       state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
/* 60 */       return null;
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 65 */       uuid = UUID.fromString((String)arg.get());
/* 66 */     } catch (IllegalArgumentException e) {
/* 67 */       state.errorCollector().store(state.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
/* 68 */       return null;
/*    */     } 
/* 70 */     return (T)ops.createIntList(IntStream.of(UUIDUtil.uuidToIntArray(uuid)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtOperations$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */