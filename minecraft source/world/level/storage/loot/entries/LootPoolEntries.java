/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LootPoolEntries {
/* 10 */   public static final Codec<LootPoolEntryContainer> CODEC = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.byNameCodec()
/* 11 */     .dispatch(LootPoolEntryContainer::getType, LootPoolEntryType::codec);
/*    */   
/* 13 */   public static final LootPoolEntryType EMPTY = register("empty", EmptyLootItem.CODEC);
/* 14 */   public static final LootPoolEntryType ITEM = register("item", LootItem.CODEC);
/* 15 */   public static final LootPoolEntryType LOOT_TABLE = register("loot_table", NestedLootTable.CODEC);
/* 16 */   public static final LootPoolEntryType DYNAMIC = register("dynamic", DynamicLoot.CODEC);
/* 17 */   public static final LootPoolEntryType TAG = register("tag", TagEntry.CODEC);
/* 18 */   public static final LootPoolEntryType SLOTS = register("slots", SlotLoot.CODEC);
/*    */   
/* 20 */   public static final LootPoolEntryType ALTERNATIVES = register("alternatives", AlternativesEntry.CODEC);
/* 21 */   public static final LootPoolEntryType SEQUENCE = register("sequence", SequentialEntry.CODEC);
/* 22 */   public static final LootPoolEntryType GROUP = register("group", EntryGroup.CODEC);
/*    */ 
/*    */   
/* 25 */   private static LootPoolEntryType register(String name, MapCodec<? extends LootPoolEntryContainer> codec) { return (LootPoolEntryType)Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, Identifier.withDefaultNamespace(name), new LootPoolEntryType(codec)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */