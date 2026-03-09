/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final Enchantment.EnchantmentDefinition definition;
/*     */   private HolderSet<Enchantment> exclusiveSet;
/*     */   private final Map<DataComponentType<?>, List<?>> effectLists;
/*     */   private final DataComponentMap.Builder effectMapBuilder;
/*     */   
/*     */   public Builder(Enchantment.EnchantmentDefinition definition) {
/* 493 */     this.exclusiveSet = HolderSet.direct(new net.minecraft.core.Holder[0]);
/* 494 */     this.effectLists = new HashMap();
/* 495 */     this.effectMapBuilder = DataComponentMap.builder();
/*     */ 
/*     */     
/* 498 */     this.definition = definition;
/*     */   }
/*     */   
/*     */   public Builder exclusiveWith(HolderSet<Enchantment> set) {
/* 502 */     this.exclusiveSet = set;
/* 503 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect, LootItemCondition.Builder condition) {
/* 507 */     getEffectsList(type).add(new ConditionalEffect(effect, Optional.of(condition.build())));
/* 508 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect) {
/* 512 */     getEffectsList(type).add(new ConditionalEffect(effect, Optional.empty()));
/* 513 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect, LootItemCondition.Builder condition) {
/* 517 */     getEffectsList(type).add(new TargetedConditionalEffect(enchanted, affected, effect, Optional.of(condition.build())));
/* 518 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect) {
/* 522 */     getEffectsList(type).add(new TargetedConditionalEffect(enchanted, affected, effect, Optional.empty()));
/* 523 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEffect(DataComponentType<List<EnchantmentAttributeEffect>> type, EnchantmentAttributeEffect effect) {
/* 527 */     getEffectsList(type).add(effect);
/* 528 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <E> Builder withSpecialEffect(DataComponentType<E> type, E effect) {
/* 533 */     this.effectMapBuilder.set(type, effect);
/* 534 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEffect(DataComponentType<Unit> type) {
/* 538 */     this.effectMapBuilder.set(type, Unit.INSTANCE);
/* 539 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private <E> List<E> getEffectsList(DataComponentType<List<E>> type) {
/* 544 */     return (List)this.effectLists.computeIfAbsent(type, k -> {
/* 545 */           ArrayList<E> newList = new ArrayList<E>();
/* 546 */           this.effectMapBuilder.set(type, newList);
/* 547 */           return newList;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 552 */   public Enchantment build(Identifier descriptionKey) { return new Enchantment(Component.translatable(Util.makeDescriptionId("enchantment", descriptionKey)), this.definition, this.exclusiveSet, this.effectMapBuilder.build()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantment$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */