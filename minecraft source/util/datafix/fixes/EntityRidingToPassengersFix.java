/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class EntityRidingToPassengersFix extends DataFix {
/* 22 */   public EntityRidingToPassengersFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Schema inputSchema = getInputSchema();
/* 28 */     Schema outputSchema = getOutputSchema();
/*    */     
/* 30 */     Type<?> oldEntityTreeType = inputSchema.getTypeRaw(References.ENTITY_TREE);
/* 31 */     Type<?> newEntityTreeType = outputSchema.getTypeRaw(References.ENTITY_TREE);
/* 32 */     Type<?> entityType = inputSchema.getTypeRaw(References.ENTITY);
/*    */     
/* 34 */     return cap(inputSchema, outputSchema, oldEntityTreeType, newEntityTreeType, entityType);
/*    */   }
/*    */   
/*    */   private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule cap(Schema inputSchema, Schema outputType, Type<OldEntityTree> oldEntityTreeType, Type<NewEntityTree> newEntityTreeType, Type<Entity> entityType) {
/* 38 */     Type<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> oldType = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/* 39 */           DSL.optional(DSL.field("Riding", oldEntityTreeType)), entityType));
/*    */ 
/*    */ 
/*    */     
/* 43 */     Type<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> newType = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/* 44 */           DSL.optional(DSL.field("Passengers", DSL.list(newEntityTreeType))), entityType));
/*    */ 
/*    */ 
/*    */     
/* 48 */     Type<?> oldEntityType = inputSchema.getType(References.ENTITY_TREE);
/* 49 */     Type<?> newEntityType = outputType.getType(References.ENTITY_TREE);
/*    */     
/* 51 */     if (!Objects.equals(oldEntityType, oldType)) {
/* 52 */       throw new IllegalStateException("Old entity type is not what was expected.");
/*    */     }
/*    */     
/* 55 */     if (!newEntityType.equals(newType, true, true)) {
/* 56 */       throw new IllegalStateException("New entity type is not what was expected.");
/*    */     }
/*    */     
/* 59 */     OpticFinder<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> entityTreeFinder = DSL.typeFinder(oldType);
/* 60 */     OpticFinder<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> newEntityTreeValueFinder = DSL.typeFinder(newType);
/* 61 */     OpticFinder<NewEntityTree> newEntityTreeFinder = DSL.typeFinder(newEntityTreeType);
/*    */     
/* 63 */     Type<?> oldPlayerType = inputSchema.getType(References.PLAYER);
/* 64 */     Type<?> newPlayerType = outputType.getType(References.PLAYER);
/*    */     
/* 66 */     return TypeRewriteRule.seq(
/* 67 */         fixTypeEverywhere("EntityRidingToPassengerFix", oldType, newType, ops -> ()), 
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
/* 90 */         writeAndRead("player RootVehicle injecter", oldPlayerType, newPlayerType));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityRidingToPassengersFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */