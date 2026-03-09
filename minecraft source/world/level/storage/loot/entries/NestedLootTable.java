/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class NestedLootTable extends LootPoolSingletonContainer {
/* 22 */   public static final MapCodec<NestedLootTable> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 23 */         Codec.either(LootTable.KEY_CODEC, LootTable.DIRECT_CODEC)
/*    */ 
/*    */         
/* 26 */         .fieldOf("value").forGetter(()))
/* 27 */       .and(singletonFields(i)).apply(i, NestedLootTable::new));
/*    */   
/* 29 */   public static final ProblemReporter.PathElement INLINE_LOOT_TABLE_PATH_ELEMENT = new ProblemReporter.PathElement()
/*    */     {
/*    */       public String get() {
/* 32 */         return "->{inline}";
/*    */       }
/*    */     };
/*    */   
/*    */   private final Either<ResourceKey<LootTable>, LootTable> contents;
/*    */   
/*    */   private NestedLootTable(Either<ResourceKey<LootTable>, LootTable> contents, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 39 */     super(weight, quality, conditions, functions);
/* 40 */     this.contents = contents;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public LootPoolEntryType getType() { return LootPoolEntries.LOOT_TABLE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 50 */     ((LootTable)this.contents.map(name -> 
/* 51 */         (LootTable)context.getResolver().get(name).map(Holder::value).orElse(LootTable.EMPTY), table -> 
/* 52 */         table))
/* 53 */       .getRandomItemsRaw(context, output);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 58 */     Optional<ResourceKey<LootTable>> name = this.contents.left();
/* 59 */     if (name.isPresent()) {
/* 60 */       ResourceKey<LootTable> id = (ResourceKey)name.get();
/* 61 */       if (!context.allowsReferences()) {
/* 62 */         context.reportProblem(new ValidationContext.ReferenceNotAllowedProblem(id));
/*    */         return;
/*    */       } 
/* 65 */       if (context.hasVisitedElement(id)) {
/* 66 */         context.reportProblem(new ValidationContext.RecursiveReferenceProblem(id));
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 71 */     super.validate(context);
/*    */     
/* 73 */     this.contents
/* 74 */       .ifLeft(id -> 
/* 75 */         context.resolver().get(id).ifPresentOrElse((), ()))
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 80 */       .ifRight(lootTable -> 
/* 81 */         lootTable.validate(context.forChild(INLINE_LOOT_TABLE_PATH_ELEMENT)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceKey<LootTable> name) { return simpleBuilder((weight, quality, conditions, functions) -> new NestedLootTable(Either.left(name), weight, quality, conditions, functions)); }
/*    */ 
/*    */ 
/*    */   
/* 90 */   public static LootPoolSingletonContainer.Builder<?> inlineLootTable(LootTable table) { return simpleBuilder((weight, quality, conditions, functions) -> new NestedLootTable(Either.right(table), weight, quality, conditions, functions)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\NestedLootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */