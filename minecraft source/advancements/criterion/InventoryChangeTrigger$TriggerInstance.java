/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TriggerInstance
/*     */   extends Record
/*     */   implements SimpleCriterionTrigger.SimpleInstance
/*     */ {
/*     */   private final Optional<ContextAwarePredicate> player;
/*     */   private final Slots slots;
/*     */   private final List<ItemPredicate> items;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #48	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #48	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance; }
/*     */   
/*  48 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Slots slots, List<ItemPredicate> items) { this.player = player; this.slots = slots; this.items = items; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #48	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;
/*  48 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> player() { return this.player; } public Slots slots() { return this.slots; } public List<ItemPredicate> items() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/*  54 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), Slots.CODEC
/*  55 */         .optionalFieldOf("slots", Slots.ANY).forGetter(TriggerInstance::slots), ItemPredicate.CODEC
/*  56 */         .listOf().optionalFieldOf("items", List.of()).forGetter(TriggerInstance::items))
/*  57 */       .apply(i, TriggerInstance::new));
/*     */ 
/*     */   
/*  60 */   public static Criterion<TriggerInstance> hasItems(Builder... items) { return hasItems((ItemPredicate[])Stream.of(items).map(ItemPredicate.Builder::build).toArray(x$0 -> new ItemPredicate[x$0])); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static Criterion<TriggerInstance> hasItems(ItemPredicate... items) { return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), Slots.ANY, List.of(items))); }
/*     */ 
/*     */   
/*     */   public static Criterion<TriggerInstance> hasItems(ItemLike... items) {
/*  68 */     ItemPredicate[] predicates = new ItemPredicate[items.length];
/*  69 */     for (int i = 0; i < items.length; i++) {
/*  70 */       predicates[i] = new ItemPredicate(Optional.of(HolderSet.direct(new Holder[] { items[i].asItem().builtInRegistryHolder() }, )), MinMaxBounds.Ints.ANY, DataComponentMatchers.ANY);
/*     */     } 
/*  72 */     return hasItems(predicates);
/*     */   }
/*     */   
/*     */   public boolean matches(Inventory inventory, ItemStack changedItem, int slotsFull, int slotsEmpty, int slotsOccupied) {
/*  76 */     if (!this.slots.matches(slotsFull, slotsEmpty, slotsOccupied)) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (this.items.isEmpty()) {
/*  81 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (this.items.size() == 1) {
/*  87 */       return (!changedItem.isEmpty() && ((ItemPredicate)this.items.get(0)).test(changedItem));
/*     */     }
/*     */     
/*  90 */     ObjectArrayList objectArrayList = new ObjectArrayList(this.items);
/*  91 */     int count = inventory.getContainerSize();
/*  92 */     for (int slot = 0; slot < count; slot++) {
/*  93 */       if (objectArrayList.isEmpty()) {
/*  94 */         return true;
/*     */       }
/*     */       
/*  97 */       ItemStack itemStack = inventory.getItem(slot);
/*  98 */       if (!itemStack.isEmpty()) {
/*  99 */         objectArrayList.removeIf(predicate -> predicate.test(itemStack));
/*     */       }
/*     */     } 
/* 102 */     return objectArrayList.isEmpty();
/*     */   }
/*     */   public static final class Slots extends Record { private final MinMaxBounds.Ints occupied; private final MinMaxBounds.Ints full; private final MinMaxBounds.Ints empty;
/* 105 */     public Slots(MinMaxBounds.Ints occupied, MinMaxBounds.Ints full, MinMaxBounds.Ints empty) { this.occupied = occupied; this.full = full; this.empty = empty; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 105 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots; } public MinMaxBounds.Ints occupied() { return this.occupied; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;
/* 105 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints full() { return this.full; } public MinMaxBounds.Ints empty() { return this.empty; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     public static final Codec<Slots> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Ints.CODEC
/* 111 */           .optionalFieldOf("occupied", MinMaxBounds.Ints.ANY).forGetter(Slots::occupied), MinMaxBounds.Ints.CODEC
/* 112 */           .optionalFieldOf("full", MinMaxBounds.Ints.ANY).forGetter(Slots::full), MinMaxBounds.Ints.CODEC
/* 113 */           .optionalFieldOf("empty", MinMaxBounds.Ints.ANY).forGetter(Slots::empty))
/* 114 */         .apply(i, Slots::new));
/*     */     
/* 116 */     public static final Slots ANY = new Slots(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY);
/*     */     
/*     */     public boolean matches(int slotsFull, int slotsEmpty, int slotsOccupied) {
/* 119 */       if (!this.full.matches(slotsFull)) {
/* 120 */         return false;
/*     */       }
/* 122 */       if (!this.empty.matches(slotsEmpty)) {
/* 123 */         return false;
/*     */       }
/* 125 */       if (!this.occupied.matches(slotsOccupied)) {
/* 126 */         return false;
/*     */       }
/* 128 */       return true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\InventoryChangeTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */