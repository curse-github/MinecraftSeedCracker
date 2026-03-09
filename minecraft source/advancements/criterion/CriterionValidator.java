/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public class CriterionValidator
/*    */ {
/*    */   private final ProblemReporter reporter;
/*    */   private final HolderGetter.Provider lootData;
/*    */   
/*    */   public CriterionValidator(ProblemReporter reporter, HolderGetter.Provider lootData) {
/* 17 */     this.reporter = reporter;
/* 18 */     this.lootData = lootData;
/*    */   }
/*    */ 
/*    */   
/* 22 */   public void validateEntity(Optional<ContextAwarePredicate> predicate, String fieldName) { predicate.ifPresent(p -> validateEntity(p, fieldName)); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void validateEntities(List<ContextAwarePredicate> predicates, String fieldName) { validate(predicates, LootContextParamSets.ADVANCEMENT_ENTITY, fieldName); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void validateEntity(ContextAwarePredicate predicate, String fieldName) { validate(predicate, LootContextParamSets.ADVANCEMENT_ENTITY, fieldName); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void validate(ContextAwarePredicate predicate, ContextKeySet params, String fieldName) { predicate.validate(new ValidationContext(this.reporter.forChild(new ProblemReporter.FieldPathElement(fieldName)), params, this.lootData)); }
/*    */ 
/*    */   
/*    */   public void validate(List<ContextAwarePredicate> predicates, ContextKeySet params, String fieldName) {
/* 38 */     for (int i = 0; i < predicates.size(); i++) {
/* 39 */       ContextAwarePredicate predicate = (ContextAwarePredicate)predicates.get(i);
/* 40 */       predicate.validate(new ValidationContext(this.reporter.forChild(new ProblemReporter.IndexedFieldPathElement(fieldName, i)), params, this.lootData));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\CriterionValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */