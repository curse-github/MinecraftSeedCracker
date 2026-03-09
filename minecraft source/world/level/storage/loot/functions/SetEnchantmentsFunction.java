/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetEnchantmentsFunction extends LootItemConditionalFunction {
/* 25 */   public static final MapCodec<SetEnchantmentsFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(
/* 26 */           Codec.unboundedMap(Enchantment.CODEC, NumberProviders.CODEC).optionalFieldOf("enchantments", Map.of()).forGetter(()), Codec.BOOL
/* 27 */           .fieldOf("add").orElse(Boolean.valueOf(false)).forGetter(())))
/* 28 */       .apply(i, SetEnchantmentsFunction::new));
/*    */   
/*    */   private final Map<Holder<Enchantment>, NumberProvider> enchantments;
/*    */   private final boolean add;
/*    */   
/*    */   private SetEnchantmentsFunction(List<LootItemCondition> predicates, Map<Holder<Enchantment>, NumberProvider> enchantments, boolean add) {
/* 34 */     super(predicates);
/* 35 */     this.enchantments = Map.copyOf(enchantments);
/* 36 */     this.add = add;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public LootItemFunctionType<SetEnchantmentsFunction> getType() { return LootItemFunctions.SET_ENCHANTMENTS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public Set<ContextKey<?>> getReferencedContextParams() { return (Set)this.enchantments.values().stream().flatMap(m -> m.getReferencedContextParams().stream()).collect(ImmutableSet.toImmutableSet()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 51 */     if (itemStack.is(Items.BOOK)) {
/* 52 */       itemStack = itemStack.transmuteCopy(Items.ENCHANTED_BOOK);
/*    */     }
/*    */     
/* 55 */     EnchantmentHelper.updateEnchantments(itemStack, enchantments -> {
/* 56 */           if (this.add) {
/* 57 */             this.enchantments.forEach(());
/*    */           } else {
/* 59 */             this.enchantments.forEach(());
/*    */           } 
/*    */         });
/* 62 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments;
/*    */     private final boolean add;
/*    */     
/* 70 */     public Builder() { this(false); }
/*    */     
/*    */     public Builder(boolean add) {
/*    */       this.enchantments = ImmutableMap.builder();
/* 74 */       this.add = add;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 79 */     protected Builder getThis() { return this; }
/*    */ 
/*    */     
/*    */     public Builder withEnchantment(Holder<Enchantment> enchantment, NumberProvider levelProvider) {
/* 83 */       this.enchantments.put(enchantment, levelProvider);
/* 84 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 89 */     public LootItemFunction build() { return new SetEnchantmentsFunction(getConditions(), this.enchantments.build(), this.add); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetEnchantmentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */