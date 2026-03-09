/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class EnchantedCountIncreaseFunction extends LootItemConditionalFunction {
/*     */   public static final int NO_LIMIT = 0;
/*  30 */   public static final MapCodec<EnchantedCountIncreaseFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(Enchantment.CODEC
/*  31 */           .fieldOf("enchantment").forGetter(()), NumberProviders.CODEC
/*  32 */           .fieldOf("count").forGetter(()), Codec.INT
/*  33 */           .optionalFieldOf("limit", Integer.valueOf(0)).forGetter(())))
/*  34 */       .apply(i, EnchantedCountIncreaseFunction::new));
/*     */   
/*     */   private final Holder<Enchantment> enchantment;
/*     */   private final NumberProvider value;
/*     */   private final int limit;
/*     */   
/*     */   private EnchantedCountIncreaseFunction(List<LootItemCondition> predicates, Holder<Enchantment> enchantment, NumberProvider value, int limit) {
/*  41 */     super(predicates);
/*  42 */     this.enchantment = enchantment;
/*  43 */     this.value = value;
/*  44 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public LootItemFunctionType<EnchantedCountIncreaseFunction> getType() { return LootItemFunctions.ENCHANTED_COUNT_INCREASE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public Set<ContextKey<?>> getReferencedContextParams() { return Sets.union(ImmutableSet.of(LootContextParams.ATTACKING_ENTITY), this.value.getReferencedContextParams()); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   private boolean hasLimit() { return (this.limit > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  63 */     Entity killer = (Entity)context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
/*     */     
/*  65 */     if (killer instanceof LivingEntity) { LivingEntity entity = (LivingEntity)killer;
/*  66 */       int level = EnchantmentHelper.getEnchantmentLevel(this.enchantment, entity);
/*  67 */       if (level == 0) {
/*  68 */         return itemStack;
/*     */       }
/*  70 */       float addition = level * this.value.getFloat(context);
/*  71 */       itemStack.grow(Math.round(addition));
/*     */       
/*  73 */       if (hasLimit()) {
/*  74 */         itemStack.limitSize(this.limit);
/*     */       } }
/*     */ 
/*     */     
/*  78 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> { private final Holder<Enchantment> enchantment;
/*     */     
/*     */     public Builder(Holder<Enchantment> enchantment, NumberProvider count) {
/*  84 */       this.limit = 0;
/*     */ 
/*     */       
/*  87 */       this.enchantment = enchantment;
/*  88 */       this.count = count;
/*     */     }
/*     */     private final NumberProvider count;
/*     */     private int limit;
/*     */     
/*  93 */     protected Builder getThis() { return this; }
/*     */ 
/*     */     
/*     */     public Builder setLimit(int limit) {
/*  97 */       this.limit = limit;
/*  98 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     public LootItemFunction build() { return new EnchantedCountIncreaseFunction(getConditions(), this.enchantment, this.count, this.limit); } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder lootingMultiplier(HolderLookup.Provider registries, NumberProvider count) {
/* 108 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 109 */     return new Builder(enchantments.getOrThrow(Enchantments.LOOTING), count);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantedCountIncreaseFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */