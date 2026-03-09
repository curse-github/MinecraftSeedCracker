/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class CompositeEntryBase extends LootPoolEntryContainer {
/* 14 */   public static final ProblemReporter.Problem NO_CHILDREN_PROBLEM = new ProblemReporter.Problem()
/*    */     {
/*    */       public String description() {
/* 17 */         return "Empty children list"; }
/*    */     };
/*    */ 
/*    */   
/*    */   protected final List<LootPoolEntryContainer> children;
/*    */ 
/*    */   
/*    */   protected CompositeEntryBase(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) {
/* 25 */     super(conditions);
/* 26 */     this.children = children;
/* 27 */     this.composedChildren = compose(children);
/*    */   } private final ComposableEntryContainer composedChildren; @FunctionalInterface
/*    */   public static interface CompositeEntryConstructor<T extends CompositeEntryBase> {
/*    */     T create(List<LootPoolEntryContainer> param1List1, List<LootItemCondition> param1List2); }
/*    */   public void validate(ValidationContext context) {
/* 32 */     super.validate(context);
/*    */     
/* 34 */     if (this.children.isEmpty()) {
/* 35 */       context.reportProblem(NO_CHILDREN_PROBLEM);
/*    */     }
/*    */     
/* 38 */     for (int i = 0; i < this.children.size(); i++) {
/* 39 */       ((LootPoolEntryContainer)this.children.get(i)).validate(context.forChild(new ProblemReporter.IndexedFieldPathElement("children", i)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract ComposableEntryContainer compose(List<? extends ComposableEntryContainer> paramList);
/*    */   
/*    */   public final boolean expand(LootContext context, Consumer<LootPoolEntry> output) {
/* 47 */     if (!canRun(context)) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     return this.composedChildren.expand(context, output);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends CompositeEntryBase> MapCodec<T> createCodec(CompositeEntryConstructor<T> constructor) {
/* 60 */     return RecordCodecBuilder.mapCodec(i -> {
/*    */           
/* 62 */           Objects.requireNonNull(constructor); return i.group(LootPoolEntries.CODEC.listOf().optionalFieldOf("children", List.of()).forGetter(())).and(commonFields(i).t1()).apply(i, constructor::create);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\CompositeEntryBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */