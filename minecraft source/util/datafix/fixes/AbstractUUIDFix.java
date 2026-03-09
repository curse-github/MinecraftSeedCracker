/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractUUIDFix
/*    */   extends DataFix
/*    */ {
/*    */   protected DSL.TypeReference typeReference;
/*    */   
/*    */   public AbstractUUIDFix(Schema outputSchema, DSL.TypeReference typeReference) {
/* 21 */     super(outputSchema, false);
/* 22 */     this.typeReference = typeReference;
/*    */   }
/*    */   
/*    */   protected Typed<?> updateNamedChoice(Typed<?> input, String name, Function<Dynamic<?>, Dynamic<?>> function) {
/* 26 */     Type<?> oldType = getInputSchema().getChoiceType(this.typeReference, name);
/* 27 */     Type<?> newType = getOutputSchema().getChoiceType(this.typeReference, name);
/* 28 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, typedTag -> typedTag.update(DSL.remainderFinder(), function));
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDString(Dynamic<?> tag, String oldKey, String newKey) {
/* 32 */     return createUUIDFromString(tag, oldKey).map(uuidTag -> 
/* 33 */         tag.remove(oldKey).set(newKey, uuidTag));
/*    */   }
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDMLTag(Dynamic<?> tag, String oldKey, String newKey) {
/* 38 */     return tag.get(oldKey).result().flatMap(AbstractUUIDFix::createUUIDFromML).map(uuidTag -> 
/* 39 */         tag.remove(oldKey).set(newKey, uuidTag));
/*    */   }
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDLeastMost(Dynamic<?> tag, String oldKey, String newKey) {
/* 44 */     String mostKey = oldKey + "Most";
/* 45 */     String leastKey = oldKey + "Least";
/* 46 */     return createUUIDFromLongs(tag, mostKey, leastKey).map(uuidTag -> 
/* 47 */         tag.remove(mostKey).remove(leastKey).set(newKey, uuidTag));
/*    */   }
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromString(Dynamic<?> tag, String oldKey) {
/* 52 */     return tag.get(oldKey).result().flatMap(uuidStringTag -> {
/* 53 */           String uuidString = uuidStringTag.asString(null);
/* 54 */           if (uuidString != null) {
/*    */             try {
/* 56 */               UUID uuid = UUID.fromString(uuidString);
/* 57 */               return createUUIDTag(tag, uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
/* 58 */             } catch (IllegalArgumentException illegalArgumentException) {}
/*    */           }
/*    */ 
/*    */           
/* 62 */           return Optional.empty();
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 67 */   protected static Optional<Dynamic<?>> createUUIDFromML(Dynamic<?> tag) { return createUUIDFromLongs(tag, "M", "L"); }
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromLongs(Dynamic<?> tag, String mostKey, String leastKey) {
/* 71 */     long mostSignificantBits = tag.get(mostKey).asLong(0L);
/* 72 */     long leastSignificantBits = tag.get(leastKey).asLong(0L);
/* 73 */     if (mostSignificantBits == 0L || leastSignificantBits == 0L) {
/* 74 */       return Optional.empty();
/*    */     }
/* 76 */     return createUUIDTag(tag, mostSignificantBits, leastSignificantBits);
/*    */   }
/*    */ 
/*    */   
/* 80 */   protected static Optional<Dynamic<?>> createUUIDTag(Dynamic<?> tag, long mostSignificantBits, long leastSignificantBits) { return Optional.of(tag.createIntList(Arrays.stream(new int[] { (int)(mostSignificantBits >> 32), (int)mostSignificantBits, (int)(leastSignificantBits >> 32), (int)leastSignificantBits }))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AbstractUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */