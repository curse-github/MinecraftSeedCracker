/*    */ package net.minecraft.world.item.crafting;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class TransmuteResult extends Record {
/*    */   private final Holder<Item> item;
/*    */   private final int count;
/*    */   private final DataComponentPatch components;
/*    */   
/* 17 */   public TransmuteResult(Holder<Item> item, int count, DataComponentPatch components) { this.item = item; this.count = count; this.components = components; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/TransmuteResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/TransmuteResult; } public Holder<Item> item() { return this.item; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/TransmuteResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/TransmuteResult; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/TransmuteResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/TransmuteResult;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public int count() { return this.count; } public DataComponentPatch components() { return this.components; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final Codec<TransmuteResult> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(Item.CODEC
/* 23 */         .fieldOf("id").forGetter(TransmuteResult::item), 
/* 24 */         ExtraCodecs.intRange(1, 99).optionalFieldOf("count", Integer.valueOf(1)).forGetter(TransmuteResult::count), DataComponentPatch.CODEC
/* 25 */         .optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(TransmuteResult::components))
/* 26 */       .apply(i, TransmuteResult::new));
/*    */   
/* 28 */   public static final Codec<TransmuteResult> CODEC = Codec.withAlternative(FULL_CODEC, Item.CODEC, item -> 
/*    */       
/* 30 */       new TransmuteResult((Item)item.value()))
/* 31 */     .validate(TransmuteResult::validate);
/*    */   
/* 33 */   public static final StreamCodec<RegistryFriendlyByteBuf, TransmuteResult> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, TransmuteResult::item, ByteBufCodecs.VAR_INT, TransmuteResult::count, DataComponentPatch.STREAM_CODEC, TransmuteResult::components, TransmuteResult::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   private static DataResult<TransmuteResult> validate(TransmuteResult result) { return ItemStack.validateStrict(new ItemStack(result.item, result.count, result.components)).map(ignored -> result); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public TransmuteResult(Item item) { this(item.builtInRegistryHolder(), 1, DataComponentPatch.EMPTY); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack apply(ItemStack input) {
/* 50 */     ItemStack result = input.transmuteCopy((ItemLike)this.item.value(), this.count);
/* 51 */     result.applyComponents(this.components);
/* 52 */     return result;
/*    */   }
/*    */   
/*    */   public boolean isResultUnchanged(ItemStack input) {
/* 56 */     ItemStack result = apply(input);
/*    */     
/* 58 */     return (result.getCount() == 1 && ItemStack.isSameItemSameComponents(input, result));
/*    */   }
/*    */ 
/*    */   
/* 62 */   public SlotDisplay display() { return new SlotDisplay.ItemStackSlotDisplay(new ItemStack(this.item, this.count, this.components)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\TransmuteResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */