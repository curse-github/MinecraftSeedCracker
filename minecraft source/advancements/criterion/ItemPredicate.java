/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public final class ItemPredicate extends Record implements Predicate<ItemStack> {
/*    */   private final Optional<HolderSet<Item>> items;
/*    */   private final MinMaxBounds.Ints count;
/*    */   private final DataComponentMatchers components;
/*    */   
/* 17 */   public ItemPredicate(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentMatchers components) { this.items = items; this.count = count; this.components = components; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ItemPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemPredicate; } public Optional<HolderSet<Item>> items() { return this.items; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ItemPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ItemPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/ItemPredicate;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints count() { return this.count; } public DataComponentMatchers components() { return this.components; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final Codec<ItemPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 23 */         RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("items").forGetter(ItemPredicate::items), MinMaxBounds.Ints.CODEC
/* 24 */         .optionalFieldOf("count", MinMaxBounds.Ints.ANY).forGetter(ItemPredicate::count), DataComponentMatchers.CODEC
/* 25 */         .forGetter(ItemPredicate::components))
/* 26 */       .apply(i, ItemPredicate::new));
/*    */ 
/*    */   
/*    */   public boolean test(ItemStack itemStack) {
/* 30 */     if (this.items.isPresent() && !itemStack.is((HolderSet)this.items.get())) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (!this.count.matches(itemStack.getCount())) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (!this.components.test(itemStack)) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 45 */     private Optional<HolderSet<Item>> items = Optional.empty();
/* 46 */     private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
/* 47 */     private DataComponentMatchers components = DataComponentMatchers.ANY;
/*    */ 
/*    */     
/* 50 */     public static Builder item() { return new Builder(); }
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder of(HolderGetter<Item> lookup, ItemLike... items) {
/* 55 */       this.items = Optional.of(HolderSet.direct(i -> i.asItem().builtInRegistryHolder(), items));
/* 56 */       return this;
/*    */     }
/*    */     
/*    */     public Builder of(HolderGetter<Item> lookup, TagKey<Item> tag) {
/* 60 */       this.items = Optional.of(lookup.getOrThrow(tag));
/* 61 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withCount(MinMaxBounds.Ints count) {
/* 65 */       this.count = count;
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withComponents(DataComponentMatchers components) {
/* 70 */       this.components = components;
/* 71 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 75 */     public ItemPredicate build() { return new ItemPredicate(this.items, this.count, this.components); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ItemPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */