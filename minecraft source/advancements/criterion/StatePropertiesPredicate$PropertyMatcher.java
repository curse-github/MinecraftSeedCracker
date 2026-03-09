/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class PropertyMatcher
/*    */   extends Record
/*    */ {
/*    */   private final String name;
/*    */   private final StatePropertiesPredicate.ValueMatcher valueMatcher;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$PropertyMatcher;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 31 */   private PropertyMatcher(String name, StatePropertiesPredicate.ValueMatcher valueMatcher) { this.name = name; this.valueMatcher = valueMatcher; } public String name() { return this.name; } public StatePropertiesPredicate.ValueMatcher valueMatcher() { return this.valueMatcher; }
/* 32 */   public static final StreamCodec<ByteBuf, PropertyMatcher> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, PropertyMatcher::name, StatePropertiesPredicate.ValueMatcher.STREAM_CODEC, PropertyMatcher::valueMatcher, PropertyMatcher::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <S extends net.minecraft.world.level.block.state.StateHolder<?, S>> boolean match(StateDefinition<?, S> definition, S state) {
/* 39 */     Property<?> property = definition.getProperty(this.name);
/* 40 */     return (property != null && this.valueMatcher.match(state, property));
/*    */   }
/*    */   
/*    */   public Optional<String> checkState(StateDefinition<?, ?> states) {
/* 44 */     Property<?> property = states.getProperty(this.name);
/* 45 */     return (property != null) ? Optional.empty() : Optional.of(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\StatePropertiesPredicate$PropertyMatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */