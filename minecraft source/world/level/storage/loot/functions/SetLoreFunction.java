/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ItemLore;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetLoreFunction extends LootItemConditionalFunction {
/* 22 */   public static final MapCodec<SetLoreFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(ComponentSerialization.CODEC
/* 23 */           .sizeLimitedListOf(256).fieldOf("lore").forGetter(()), 
/* 24 */           ListOperation.codec(256).forGetter(()), LootContext.EntityTarget.CODEC
/* 25 */           .optionalFieldOf("entity").forGetter(())))
/* 26 */       .apply(i, SetLoreFunction::new));
/*    */   
/*    */   private final List<Component> lore;
/*    */   private final ListOperation mode;
/*    */   private final Optional<LootContext.EntityTarget> resolutionContext;
/*    */   
/*    */   public SetLoreFunction(List<LootItemCondition> predicates, List<Component> lore, ListOperation mode, Optional<LootContext.EntityTarget> resolutionContext) {
/* 33 */     super(predicates);
/* 34 */     this.lore = List.copyOf(lore);
/* 35 */     this.mode = mode;
/* 36 */     this.resolutionContext = resolutionContext;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public LootItemFunctionType<SetLoreFunction> getType() { return LootItemFunctions.SET_LORE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public Set<ContextKey<?>> getReferencedContextParams() { return (Set)this.resolutionContext.map(target -> Set.of(target.contextParam())).orElseGet(Set::of); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 51 */     itemStack.update(DataComponents.LORE, ItemLore.EMPTY, oldLore -> new ItemLore(updateLore(oldLore, context)));
/* 52 */     return itemStack;
/*    */   }
/*    */   
/*    */   private List<Component> updateLore(ItemLore itemLore, LootContext context) {
/* 56 */     if (itemLore == null && this.lore.isEmpty()) {
/* 57 */       return List.of();
/*    */     }
/* 59 */     UnaryOperator<Component> resolver = SetNameFunction.createResolver(context, (LootContext.EntityTarget)this.resolutionContext.orElse(null));
/* 60 */     List<Component> resolvedLines = this.lore.stream().map(resolver).toList();
/* 61 */     return this.mode.apply(itemLore.lines(), resolvedLines, 256);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 65 */     private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
/* 66 */     private final ImmutableList.Builder<Component> lore = ImmutableList.builder();
/* 67 */     private ListOperation mode = ListOperation.Append.INSTANCE;
/*    */     
/*    */     public Builder setMode(ListOperation mode) {
/* 70 */       this.mode = mode;
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setResolutionContext(LootContext.EntityTarget resolutionContext) {
/* 75 */       this.resolutionContext = Optional.of(resolutionContext);
/* 76 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addLine(Component line) {
/* 80 */       this.lore.add(line);
/* 81 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 86 */     protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 91 */     public LootItemFunction build() { return new SetLoreFunction(getConditions(), this.lore.build(), this.mode, this.resolutionContext); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 96 */   public static Builder setLore() { return new Builder(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */