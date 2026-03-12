/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EnchantRandomlyFunction extends LootItemConditionalFunction {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   public static final MapCodec<EnchantRandomlyFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(
/*  32 */           RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(()), Codec.BOOL
/*  33 */           .optionalFieldOf("only_compatible", Boolean.valueOf(true)).forGetter(())))
/*  34 */       .apply(i, EnchantRandomlyFunction::new));
/*     */   
/*     */   private final Optional<HolderSet<Enchantment>> options;
/*     */   private final boolean onlyCompatible;
/*     */   
/*     */   private EnchantRandomlyFunction(List<LootItemCondition> predicates, Optional<HolderSet<Enchantment>> options, boolean onlyCompatible) {
/*  40 */     super(predicates);
/*  41 */     this.options = options;
/*  42 */     this.onlyCompatible = onlyCompatible;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public LootItemFunctionType<EnchantRandomlyFunction> getType() { return LootItemFunctions.ENCHANT_RANDOMLY; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  52 */     RandomSource random = context.getRandom();
/*  53 */     boolean targetIsBook = itemStack.is(Items.BOOK);
/*  54 */     boolean shouldCheckCompatibility = (!targetIsBook && this.onlyCompatible);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     Stream<Holder<Enchantment>> compatibleEnchantmentsStream = ((Stream)this.options.map(HolderSet::stream).orElseGet(() -> context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().map(Function.identity()))).filter(candidate -> (!shouldCheckCompatibility || ((Enchantment)candidate.value()).canEnchant(itemStack)));
/*  62 */     List<Holder<Enchantment>> compatibleEnchantments = compatibleEnchantmentsStream.toList();
/*  63 */     Optional<Holder<Enchantment>> enchantment = Util.getRandomSafe(compatibleEnchantments, random);
/*  64 */     if (enchantment.isEmpty()) {
/*  65 */       LOGGER.warn("Couldn't find a compatible enchantment for {}", itemStack);
/*  66 */       return itemStack;
/*     */     } 
/*     */     
/*  69 */     return enchantItem(itemStack, (Holder)enchantment.get(), random);
/*     */   }
/*     */   
/*     */   private static ItemStack enchantItem(ItemStack itemStack, Holder<Enchantment> enchantment, RandomSource random) {
/*  73 */     int level = Mth.nextInt(random, ((Enchantment)enchantment.value()).getMinLevel(), ((Enchantment)enchantment.value()).getMaxLevel());
/*  74 */     if (itemStack.is(Items.BOOK)) {
/*  75 */       itemStack = new ItemStack(Items.ENCHANTED_BOOK);
/*     */     }
/*  77 */     itemStack.enchant(enchantment, level);
/*  78 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  82 */     private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */     
/*     */     private boolean onlyCompatible = true;
/*     */ 
/*     */     
/*  87 */     protected Builder getThis() { return this; }
/*     */ 
/*     */     
/*     */     public Builder withEnchantment(Holder<Enchantment> enchantment) {
/*  91 */       this.options = Optional.of(HolderSet.direct(new Holder[] { enchantment }));
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOneOf(HolderSet<Enchantment> enchantments) {
/*  96 */       this.options = Optional.of(enchantments);
/*  97 */       return this;
/*     */     }
/*     */     
/*     */     public Builder allowingIncompatibleEnchantments() {
/* 101 */       this.onlyCompatible = false;
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 107 */     public LootItemFunction build() { return new EnchantRandomlyFunction(getConditions(), this.options, this.onlyCompatible); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static Builder randomEnchantment() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static Builder randomApplicableEnchantment(HolderLookup.Provider registries) { return randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */