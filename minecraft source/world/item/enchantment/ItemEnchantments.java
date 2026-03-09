/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.component.TooltipProvider;
/*     */ 
/*     */ public class ItemEnchantments implements TooltipProvider {
/*  31 */   public static final ItemEnchantments EMPTY = new ItemEnchantments(new Object2IntOpenHashMap());
/*     */   
/*  33 */   private static final Codec<Integer> LEVEL_CODEC = Codec.intRange(1, 255);
/*     */   
/*  35 */   public static final Codec<ItemEnchantments> CODEC = Codec.unboundedMap(Enchantment.CODEC, LEVEL_CODEC)
/*     */ 
/*     */     
/*  38 */     .xmap(map -> new ItemEnchantments(new Object2IntOpenHashMap(map)), enchantments -> enchantments.enchantments);
/*     */   
/*  40 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemEnchantments> STREAM_CODEC = StreamCodec.composite(
/*  41 */       ByteBufCodecs.map(Object2IntOpenHashMap::new, Enchantment.STREAM_CODEC, ByteBufCodecs.VAR_INT), c -> c.enchantments, ItemEnchantments::new);
/*     */ 
/*     */   
/*     */   private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;
/*     */ 
/*     */   
/*     */   private ItemEnchantments(Object2IntOpenHashMap<Holder<Enchantment>> enchantments) {
/*  48 */     this.enchantments = enchantments;
/*     */     
/*  50 */     for (ObjectIterator objectIterator = enchantments.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Holder<Enchantment>> entry = (Object2IntMap.Entry)objectIterator.next();
/*  51 */       int level = entry.getIntValue();
/*  52 */       if (level < 0 || level > 255) {
/*  53 */         throw new IllegalArgumentException("Enchantment " + String.valueOf(entry.getKey()) + " has invalid level " + level);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*  59 */   public int getLevel(Holder<Enchantment> enchantment) { return this.enchantments.getInt(enchantment); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/*  64 */     HolderLookup.Provider registries = context.registries();
/*  65 */     HolderSet<Enchantment> order = getTagOrEmpty(registries, Registries.ENCHANTMENT, EnchantmentTags.TOOLTIP_ORDER);
/*  66 */     for (Holder<Enchantment> enchantment : order) {
/*  67 */       int level = this.enchantments.getInt(enchantment);
/*  68 */       if (level > 0) {
/*  69 */         consumer.accept(Enchantment.getFullname(enchantment, level));
/*     */       }
/*     */     } 
/*  72 */     for (ObjectIterator objectIterator = this.enchantments.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Holder<Enchantment>> entry = (Object2IntMap.Entry)objectIterator.next();
/*  73 */       Holder<Enchantment> enchantment = (Holder)entry.getKey();
/*  74 */       if (!order.contains(enchantment)) {
/*  75 */         consumer.accept(Enchantment.getFullname((Holder)entry.getKey(), entry.getIntValue()));
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private static <T> HolderSet<T> getTagOrEmpty(HolderLookup.Provider registries, ResourceKey<Registry<T>> registry, TagKey<T> tag) {
/*  81 */     if (registries != null) {
/*  82 */       Optional<HolderSet.Named<T>> maybeOrder = registries.lookupOrThrow(registry).get(tag);
/*  83 */       if (maybeOrder.isPresent()) {
/*  84 */         return (HolderSet)maybeOrder.get();
/*     */       }
/*     */     } 
/*  87 */     return HolderSet.direct(new Holder[0]);
/*     */   }
/*     */ 
/*     */   
/*  91 */   public Set<Holder<Enchantment>> keySet() { return Collections.unmodifiableSet(this.enchantments.keySet()); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public Set<Object2IntMap.Entry<Holder<Enchantment>>> entrySet() { return Collections.unmodifiableSet(this.enchantments.object2IntEntrySet()); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public int size() { return this.enchantments.size(); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean isEmpty() { return this.enchantments.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 108 */     if (this == obj) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (obj instanceof ItemEnchantments) { ItemEnchantments that = (ItemEnchantments)obj;
/* 112 */       return this.enchantments.equals(that.enchantments); }
/*     */     
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public int hashCode() { return this.enchantments.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public String toString() { return "ItemEnchantments{enchantments=" + String.valueOf(this.enchantments) + "}"; }
/*     */   public static class Mutable { private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;
/*     */     
/*     */     public Mutable(ItemEnchantments enchantments) {
/* 128 */       this.enchantments = new Object2IntOpenHashMap();
/*     */ 
/*     */       
/* 131 */       this.enchantments.putAll(enchantments.enchantments);
/*     */     }
/*     */     
/*     */     public void set(Holder<Enchantment> enchantment, int level) {
/* 135 */       if (level <= 0) {
/* 136 */         this.enchantments.removeInt(enchantment);
/*     */       } else {
/* 138 */         this.enchantments.put(enchantment, Math.min(level, 255));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void upgrade(Holder<Enchantment> enchantment, int level) {
/* 143 */       if (level > 0) {
/* 144 */         this.enchantments.merge(enchantment, Math.min(level, 255), Integer::max);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 149 */     public void removeIf(Predicate<Holder<Enchantment>> predicate) { this.enchantments.keySet().removeIf(predicate); }
/*     */ 
/*     */ 
/*     */     
/* 153 */     public int getLevel(Holder<Enchantment> enchantment) { return this.enchantments.getOrDefault(enchantment, 0); }
/*     */ 
/*     */ 
/*     */     
/* 157 */     public Set<Holder<Enchantment>> keySet() { return this.enchantments.keySet(); }
/*     */ 
/*     */ 
/*     */     
/* 161 */     public ItemEnchantments toImmutable() { return new ItemEnchantments(this.enchantments); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\ItemEnchantments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */