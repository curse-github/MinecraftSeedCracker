/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
/* 26 */   public static final MapCodec<EnchantWithLevelsFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(NumberProviders.CODEC
/* 27 */           .fieldOf("levels").forGetter(()), 
/* 28 */           RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(())))
/* 29 */       .apply(i, EnchantWithLevelsFunction::new));
/*    */   
/*    */   private final NumberProvider levels;
/*    */   private final Optional<HolderSet<Enchantment>> options;
/*    */   
/*    */   private EnchantWithLevelsFunction(List<LootItemCondition> predicates, NumberProvider levels, Optional<HolderSet<Enchantment>> options) {
/* 35 */     super(predicates);
/* 36 */     this.levels = levels;
/* 37 */     this.options = options;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public LootItemFunctionType<EnchantWithLevelsFunction> getType() { return LootItemFunctions.ENCHANT_WITH_LEVELS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.levels.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 52 */     RandomSource random = context.getRandom();
/* 53 */     RegistryAccess registryAccess = context.getLevel().registryAccess();
/* 54 */     return EnchantmentHelper.enchantItem(random, itemStack, this.levels.getInt(context), registryAccess, this.options);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     public Builder(NumberProvider levels) {
/* 59 */       this.options = Optional.empty();
/*    */ 
/*    */       
/* 62 */       this.levels = levels;
/*    */     }
/*    */     private final NumberProvider levels;
/*    */     private Optional<HolderSet<Enchantment>> options;
/*    */     
/* 67 */     protected Builder getThis() { return this; }
/*    */ 
/*    */     
/*    */     public Builder fromOptions(HolderSet<Enchantment> tag) {
/* 71 */       this.options = Optional.of(tag);
/* 72 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 77 */     public LootItemFunction build() { return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public static Builder enchantWithLevels(HolderLookup.Provider registries, NumberProvider levels) { return (new Builder(levels)).fromOptions(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */