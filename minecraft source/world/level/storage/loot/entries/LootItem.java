/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LootItem extends LootPoolSingletonContainer {
/* 17 */   public static final MapCodec<LootItem> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Item.CODEC
/* 18 */         .fieldOf("name").forGetter(()))
/* 19 */       .and(singletonFields(i)).apply(i, LootItem::new));
/*    */   
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private LootItem(Holder<Item> item, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 24 */     super(weight, quality, conditions, functions);
/* 25 */     this.item = item;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public LootPoolEntryType getType() { return LootPoolEntries.ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public void createItemStack(Consumer<ItemStack> output, LootContext context) { output.accept(new ItemStack(this.item)); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike item) { return simpleBuilder((weight, quality, conditions, functions) -> new LootItem(item.asItem().builtInRegistryHolder(), weight, quality, conditions, functions)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */