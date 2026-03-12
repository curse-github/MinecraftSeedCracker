/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EmptyLootItem extends LootPoolSingletonContainer {
/* 14 */   public static final MapCodec<EmptyLootItem> CODEC = RecordCodecBuilder.mapCodec(i -> singletonFields(i).apply(i, EmptyLootItem::new));
/*    */ 
/*    */   
/* 17 */   private EmptyLootItem(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) { super(weight, quality, conditions, functions); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public LootPoolEntryType getType() { return LootPoolEntries.EMPTY; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {}
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static LootPoolSingletonContainer.Builder<?> emptyItem() { return simpleBuilder(EmptyLootItem::new); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\EmptyLootItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */