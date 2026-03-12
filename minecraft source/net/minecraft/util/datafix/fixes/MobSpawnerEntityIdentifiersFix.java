/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class MobSpawnerEntityIdentifiersFix
/*    */   extends DataFix
/*    */ {
/* 19 */   public MobSpawnerEntityIdentifiersFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> input) {
/* 23 */     if (!"MobSpawner".equals(input.get("id").asString(""))) {
/* 24 */       return input;
/*    */     }
/*    */     
/* 27 */     Optional<String> entityId = input.get("EntityId").asString().result();
/* 28 */     if (entityId.isPresent()) {
/* 29 */       Dynamic<?> spawnData = (Dynamic)DataFixUtils.orElse(input.get("SpawnData").result(), input.emptyMap());
/* 30 */       spawnData = spawnData.set("id", spawnData.createString(((String)entityId.get()).isEmpty() ? "Pig" : (String)entityId.get()));
/* 31 */       input = input.set("SpawnData", spawnData);
/*    */       
/* 33 */       input = input.remove("EntityId");
/*    */     } 
/*    */     
/* 36 */     Optional<? extends Stream<? extends Dynamic<?>>> spawnPotentials = input.get("SpawnPotentials").asStreamOpt().result();
/* 37 */     if (spawnPotentials.isPresent()) {
/* 38 */       input = input.set("SpawnPotentials", input.createList(((Stream)spawnPotentials.get()).map(spawnPotential -> {
/* 39 */                 Optional<String> type = spawnPotential.get("Type").asString().result();
/* 40 */                 if (type.isPresent()) {
/* 41 */                   Dynamic<?> spawnData = ((Dynamic)DataFixUtils.orElse(spawnPotential.get("Properties").result(), spawnPotential.emptyMap())).set("id", spawnPotential.createString((String)type.get()));
/* 42 */                   return spawnPotential
/* 43 */                     .set("Entity", spawnData)
/* 44 */                     .remove("Type")
/* 45 */                     .remove("Properties");
/*    */                 } 
/* 47 */                 return spawnPotential;
/*    */               })));
/*    */     }
/*    */     
/* 51 */     return input;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 56 */     Type<?> newType = getOutputSchema().getType(References.UNTAGGED_SPAWNER);
/* 57 */     return fixTypeEverywhereTyped("MobSpawnerEntityIdentifiersFix", getInputSchema().getType(References.UNTAGGED_SPAWNER), newType, input -> {
/* 58 */           Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/* 59 */           tag = tag.set("id", tag.createString("MobSpawner"));
/* 60 */           DataResult<? extends Pair<? extends Typed<?>, ?>> fixed = newType.readTyped(fix(tag));
/* 61 */           if (fixed.result().isEmpty())
/*    */           {
/* 63 */             return input;
/*    */           }
/* 65 */           return (Typed)((Pair)fixed.result().get()).getFirst();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\MobSpawnerEntityIdentifiersFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */