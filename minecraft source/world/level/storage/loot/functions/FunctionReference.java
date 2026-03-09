/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FunctionReference extends LootItemConditionalFunction {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 21 */   public static final MapCodec<FunctionReference> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(
/* 22 */         ResourceKey.codec(Registries.ITEM_MODIFIER).fieldOf("name").forGetter(()))
/* 23 */       .apply(i, FunctionReference::new));
/*    */   
/*    */   private final ResourceKey<LootItemFunction> name;
/*    */   
/*    */   private FunctionReference(List<LootItemCondition> predicates, ResourceKey<LootItemFunction> name) {
/* 28 */     super(predicates);
/* 29 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public LootItemFunctionType<FunctionReference> getType() { return LootItemFunctions.REFERENCE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 39 */     if (!context.allowsReferences()) {
/* 40 */       context.reportProblem(new ValidationContext.ReferenceNotAllowedProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     if (context.hasVisitedElement(this.name)) {
/* 45 */       context.reportProblem(new ValidationContext.RecursiveReferenceProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     super.validate(context);
/*    */     
/* 51 */     context.resolver().get(this.name).ifPresentOrElse(function -> (
/* 52 */         (LootItemFunction)function.value()).validate(context.enterElement(new ProblemReporter.ElementReferencePathElement(this.name), this.name)), () -> 
/* 53 */         context.reportProblem(new ValidationContext.MissingReferenceProblem(this.name)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 59 */     LootItemFunction function = (LootItemFunction)context.getResolver().get(this.name).map(Holder::value).orElse(null);
/* 60 */     if (function == null) {
/* 61 */       LOGGER.warn("Unknown function: {}", this.name.identifier());
/* 62 */       return itemStack;
/*    */     } 
/* 64 */     breadcrumb = LootContext.createVisitedEntry(function);
/* 65 */     if (context.pushVisitedElement(breadcrumb)) {
/*    */       try {
/* 67 */         return (ItemStack)function.apply(itemStack, context);
/*    */       } finally {
/* 69 */         context.popVisitedElement(breadcrumb);
/*    */       } 
/*    */     }
/* 72 */     LOGGER.warn("Detected infinite loop in loot tables");
/* 73 */     return itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 78 */   public static LootItemConditionalFunction.Builder<?> functionReference(ResourceKey<LootItemFunction> name) { return simpleBuilder(conditions -> new FunctionReference(conditions, name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FunctionReference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */