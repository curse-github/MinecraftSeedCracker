/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerContents extends LootItemConditionalFunction {
/* 21 */   public static final MapCodec<SetContainerContents> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(ContainerComponentManipulators.CODEC
/* 22 */           .fieldOf("component").forGetter(()), LootPoolEntries.CODEC
/* 23 */           .listOf().fieldOf("entries").forGetter(())))
/* 24 */       .apply(i, SetContainerContents::new));
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   private final List<LootPoolEntryContainer> entries;
/*    */   
/*    */   private SetContainerContents(List<LootItemCondition> predicates, ContainerComponentManipulator<?> component, List<LootPoolEntryContainer> entries) {
/* 30 */     super(predicates);
/* 31 */     this.component = component;
/* 32 */     this.entries = List.copyOf(entries);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public LootItemFunctionType<SetContainerContents> getType() { return LootItemFunctions.SET_CONTENTS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 42 */     if (itemStack.isEmpty()) {
/* 43 */       return itemStack;
/*    */     }
/*    */     
/* 46 */     Stream.Builder<ItemStack> contents = Stream.builder();
/* 47 */     this.entries.forEach(e -> e.expand(context, ()));
/* 48 */     this.component.setContents(itemStack, contents.build());
/*    */     
/* 50 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 55 */     super.validate(context);
/*    */     
/* 57 */     for (int i = 0; i < this.entries.size(); i++)
/* 58 */       ((LootPoolEntryContainer)this.entries.get(i)).validate(context.forChild(new ProblemReporter.IndexedFieldPathElement("entries", i))); 
/*    */   }
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> { private final ImmutableList.Builder<LootPoolEntryContainer> entries; private final ContainerComponentManipulator<?> component;
/*    */     
/*    */     public Builder(ContainerComponentManipulator<?> component) {
/* 63 */       this.entries = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */       
/* 67 */       this.component = component;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 72 */     protected Builder getThis() { return this; }
/*    */ 
/*    */     
/*    */     public Builder withEntry(LootPoolEntryContainer.Builder<?> entry) {
/* 76 */       this.entries.add(entry.build());
/* 77 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 82 */     public LootItemFunction build() { return new SetContainerContents(getConditions(), this.component, this.entries.build()); } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   public static Builder setContents(ContainerComponentManipulator<?> component) { return new Builder(component); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */