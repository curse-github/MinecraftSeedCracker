/*     */ package net.minecraft.world.item.component;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class ItemContainerContents implements TooltipProvider {
/*     */   private static final int NO_SLOT = -1;
/*     */   private static final int MAX_SIZE = 256;
/*  27 */   public static final ItemContainerContents EMPTY = new ItemContainerContents(NonNullList.create());
/*     */   
/*  29 */   public static final Codec<ItemContainerContents> CODEC = Slot.CODEC.sizeLimitedListOf(256).xmap(ItemContainerContents::fromSlots, ItemContainerContents::asSlots);
/*  30 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerContents> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(256)).map(ItemContainerContents::new, c -> c.items);
/*     */   
/*     */   private final NonNullList<ItemStack> items;
/*     */   
/*     */   private final int hashCode;
/*     */ 
/*     */   
/*     */   private ItemContainerContents(NonNullList<ItemStack> items) {
/*  38 */     if (items.size() > 256) {
/*  39 */       throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 256");
/*     */     }
/*  41 */     this.items = items;
/*  42 */     this.hashCode = ItemStack.hashStackList(items);
/*     */   }
/*     */ 
/*     */   
/*  46 */   private ItemContainerContents(int size) { this(NonNullList.withSize(size, ItemStack.EMPTY)); }
/*     */ 
/*     */   
/*     */   private ItemContainerContents(List<ItemStack> items) {
/*  50 */     this(items.size());
/*  51 */     for (int i = 0; i < items.size(); i++) {
/*  52 */       this.items.set(i, (ItemStack)items.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   private static ItemContainerContents fromSlots(List<Slot> slots) {
/*  57 */     OptionalInt maxSlotIndex = slots.stream().mapToInt(Slot::index).max();
/*     */     
/*  59 */     if (maxSlotIndex.isEmpty()) {
/*  60 */       return EMPTY;
/*     */     }
/*     */     
/*  63 */     ItemContainerContents contents = new ItemContainerContents(maxSlotIndex.getAsInt() + 1);
/*  64 */     for (Slot slot : slots) {
/*  65 */       contents.items.set(slot.index(), slot.item());
/*     */     }
/*  67 */     return contents;
/*     */   }
/*     */   
/*     */   public static ItemContainerContents fromItems(List<ItemStack> itemStacks) {
/*  71 */     int lastNonEmptySlot = findLastNonEmptySlot(itemStacks);
/*  72 */     if (lastNonEmptySlot == -1) {
/*  73 */       return EMPTY;
/*     */     }
/*  75 */     ItemContainerContents contents = new ItemContainerContents(lastNonEmptySlot + 1);
/*  76 */     for (int i = 0; i <= lastNonEmptySlot; i++) {
/*  77 */       contents.items.set(i, ((ItemStack)itemStacks.get(i)).copy());
/*     */     }
/*  79 */     return contents;
/*     */   }
/*     */   
/*     */   private static int findLastNonEmptySlot(List<ItemStack> itemStacks) {
/*  83 */     for (int i = itemStacks.size() - 1; i >= 0; i--) {
/*  84 */       if (!((ItemStack)itemStacks.get(i)).isEmpty()) {
/*  85 */         return i;
/*     */       }
/*     */     } 
/*  88 */     return -1;
/*     */   }
/*     */   
/*     */   private List<Slot> asSlots() {
/*  92 */     List<Slot> slots = new ArrayList<Slot>();
/*  93 */     for (int i = 0; i < this.items.size(); i++) {
/*  94 */       ItemStack item = (ItemStack)this.items.get(i);
/*  95 */       if (!item.isEmpty()) {
/*  96 */         slots.add(new Slot(i, item));
/*     */       }
/*     */     } 
/*  99 */     return slots;
/*     */   }
/*     */   
/*     */   public void copyInto(NonNullList<ItemStack> destination) {
/* 103 */     for (int i = 0; i < destination.size(); i++) {
/* 104 */       ItemStack item = (i < this.items.size()) ? (ItemStack)this.items.get(i) : ItemStack.EMPTY;
/* 105 */       destination.set(i, item.copy());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 110 */   public ItemStack copyOne() { return this.items.isEmpty() ? ItemStack.EMPTY : ((ItemStack)this.items.get(0)).copy(); }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public Stream<ItemStack> stream() { return this.items.stream().map(ItemStack::copy); }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public Stream<ItemStack> nonEmptyStream() { return this.items.stream().filter(itemStack -> !itemStack.isEmpty()).map(ItemStack::copy); }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public Iterable<ItemStack> nonEmptyItems() { return Iterables.filter(this.items, itemStack -> !itemStack.isEmpty()); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public Iterable<ItemStack> nonEmptyItemsCopy() { return Iterables.transform(nonEmptyItems(), ItemStack::copy); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 131 */     if (this == obj) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (obj instanceof ItemContainerContents) { ItemContainerContents contents = (ItemContainerContents)obj; if (ItemStack.listMatches(this.items, contents.items)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public int hashCode() { return this.hashCode; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 144 */     int lineCount = 0;
/* 145 */     int itemCount = 0;
/* 146 */     for (ItemStack stack : nonEmptyItems()) {
/* 147 */       itemCount++;
/* 148 */       if (lineCount > 4) {
/*     */         continue;
/*     */       }
/* 151 */       lineCount++;
/* 152 */       consumer.accept(Component.translatable("item.container.item_count", new Object[] { stack.getHoverName(), Integer.valueOf(stack.getCount()) }));
/*     */     } 
/*     */     
/* 155 */     if (itemCount - lineCount > 0)
/* 156 */       consumer.accept(Component.translatable("item.container.more_items", new Object[] { Integer.valueOf(itemCount - lineCount) }).withStyle(ChatFormatting.ITALIC)); 
/*     */   }
/*     */   private static final class Slot extends Record { private final int index; private final ItemStack item;
/*     */     
/* 160 */     private Slot(int index, ItemStack item) { this.index = index; this.item = item; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 160 */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot; } public int index() { return this.index; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/* 160 */       //   0	8	1	o	Ljava/lang/Object; } public ItemStack item() { return this.item; }
/* 161 */     public static final Codec<Slot> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 162 */           Codec.intRange(0, 255).fieldOf("slot").forGetter(Slot::index), ItemStack.CODEC
/* 163 */           .fieldOf("item").forGetter(Slot::item))
/* 164 */         .apply(i, Slot::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ItemContainerContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */