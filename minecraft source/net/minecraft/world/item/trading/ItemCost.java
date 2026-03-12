/*    */ package net.minecraft.world.item.trading;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public final class ItemCost extends Record {
/*    */   private final Holder<Item> item;
/*    */   private final int count;
/*    */   private final DataComponentExactPredicate components;
/*    */   private final ItemStack itemStack;
/*    */   
/* 18 */   public ItemCost(Holder<Item> item, int count, DataComponentExactPredicate components, ItemStack itemStack) { this.item = item; this.count = count; this.components = components; this.itemStack = itemStack; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/trading/ItemCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/item/trading/ItemCost; } public Holder<Item> item() { return this.item; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/trading/ItemCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/trading/ItemCost; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/trading/ItemCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/trading/ItemCost;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public int count() { return this.count; } public DataComponentExactPredicate components() { return this.components; } public ItemStack itemStack() { return this.itemStack; }
/*    */   
/* 20 */   public static final Codec<ItemCost> CODEC = RecordCodecBuilder.create(i -> i.group(Item.CODEC
/* 21 */         .fieldOf("id").forGetter(ItemCost::item), ExtraCodecs.POSITIVE_INT
/* 22 */         .fieldOf("count").orElse(Integer.valueOf(1)).forGetter(ItemCost::count), DataComponentExactPredicate.CODEC
/* 23 */         .optionalFieldOf("components", DataComponentExactPredicate.EMPTY).forGetter(ItemCost::components))
/* 24 */       .apply(i, ItemCost::new));
/*    */   
/* 26 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemCost> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemCost::item, ByteBufCodecs.VAR_INT, ItemCost::count, DataComponentExactPredicate.STREAM_CODEC, ItemCost::components, ItemCost::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<ItemCost>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);
/*    */ 
/*    */   
/* 35 */   public ItemCost(ItemLike item) { this(item, 1); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public ItemCost(ItemLike item, int count) { this(item.asItem().builtInRegistryHolder(), count, DataComponentExactPredicate.EMPTY); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public ItemCost(Holder<Item> item, int count, DataComponentExactPredicate components) { this(item, count, components, createStack(item, count, components)); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public ItemCost withComponents(UnaryOperator<DataComponentExactPredicate.Builder> components) { return new ItemCost(this.item, this.count, ((DataComponentExactPredicate.Builder)components.apply(DataComponentExactPredicate.builder())).build()); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   private static ItemStack createStack(Holder<Item> item, int count, DataComponentExactPredicate components) { return new ItemStack(item, count, components.asPatch()); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean test(ItemStack itemStack) { return (itemStack.is(this.item) && this.components.test(itemStack)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\trading\ItemCost.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */