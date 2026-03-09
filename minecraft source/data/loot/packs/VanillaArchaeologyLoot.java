/*     */ package net.minecraft.data.loot.packs;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaArchaeologyLoot extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot; }
/*     */   
/*  20 */   public VanillaArchaeologyLoot(HolderLookup.Provider registries) { this.registries = registries; } public HolderLookup.Provider registries() { return this.registries; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  24 */     output.accept(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, 
/*  25 */         LootTable.lootTable()
/*  26 */         .withPool(LootPool.lootPool()
/*  27 */           .setRolls(ConstantValue.exactly(1.0F))
/*  28 */           .add(LootItem.lootTableItem(Items.ARMS_UP_POTTERY_SHERD).setWeight(2))
/*  29 */           .add(LootItem.lootTableItem(Items.BREWER_POTTERY_SHERD).setWeight(2))
/*  30 */           .add(LootItem.lootTableItem(Items.BRICK))
/*  31 */           .add(LootItem.lootTableItem(Items.EMERALD))
/*  32 */           .add(LootItem.lootTableItem(Items.STICK))
/*  33 */           .add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).apply(SetStewEffectFunction.stewEffect()
/*  34 */               .withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7.0F, 10.0F))
/*  35 */               .withEffect(MobEffects.JUMP_BOOST, UniformGenerator.between(7.0F, 10.0F))
/*  36 */               .withEffect(MobEffects.WEAKNESS, UniformGenerator.between(6.0F, 8.0F))
/*  37 */               .withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5.0F, 7.0F))
/*  38 */               .withEffect(MobEffects.POISON, UniformGenerator.between(10.0F, 20.0F))
/*  39 */               .withEffect(MobEffects.SATURATION, UniformGenerator.between(7.0F, 10.0F))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     output.accept(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, 
/*  45 */         LootTable.lootTable()
/*  46 */         .withPool(LootPool.lootPool()
/*  47 */           .setRolls(ConstantValue.exactly(1.0F))
/*  48 */           .add(LootItem.lootTableItem(Items.ARCHER_POTTERY_SHERD))
/*  49 */           .add(LootItem.lootTableItem(Items.MINER_POTTERY_SHERD))
/*  50 */           .add(LootItem.lootTableItem(Items.PRIZE_POTTERY_SHERD))
/*  51 */           .add(LootItem.lootTableItem(Items.SKULL_POTTERY_SHERD))
/*  52 */           .add(LootItem.lootTableItem(Items.DIAMOND))
/*  53 */           .add(LootItem.lootTableItem(Items.TNT))
/*  54 */           .add(LootItem.lootTableItem(Items.GUNPOWDER))
/*  55 */           .add(LootItem.lootTableItem(Items.EMERALD))));
/*     */ 
/*     */ 
/*     */     
/*  59 */     output.accept(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 
/*  60 */         LootTable.lootTable()
/*  61 */         .withPool(LootPool.lootPool()
/*  62 */           .setRolls(ConstantValue.exactly(1.0F))
/*  63 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
/*  64 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
/*  65 */           .add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
/*  66 */           .add(LootItem.lootTableItem(Items.CLAY).setWeight(2))
/*  67 */           .add(LootItem.lootTableItem(Items.BRICK).setWeight(2))
/*  68 */           .add(LootItem.lootTableItem(Items.YELLOW_DYE).setWeight(2))
/*  69 */           .add(LootItem.lootTableItem(Items.BLUE_DYE).setWeight(2))
/*  70 */           .add(LootItem.lootTableItem(Items.LIGHT_BLUE_DYE).setWeight(2))
/*  71 */           .add(LootItem.lootTableItem(Items.WHITE_DYE).setWeight(2))
/*  72 */           .add(LootItem.lootTableItem(Items.ORANGE_DYE).setWeight(2))
/*  73 */           .add(LootItem.lootTableItem(Items.RED_CANDLE).setWeight(2))
/*  74 */           .add(LootItem.lootTableItem(Items.GREEN_CANDLE).setWeight(2))
/*  75 */           .add(LootItem.lootTableItem(Items.PURPLE_CANDLE).setWeight(2))
/*  76 */           .add(LootItem.lootTableItem(Items.BROWN_CANDLE).setWeight(2))
/*  77 */           .add(LootItem.lootTableItem(Items.MAGENTA_STAINED_GLASS_PANE))
/*  78 */           .add(LootItem.lootTableItem(Items.PINK_STAINED_GLASS_PANE))
/*  79 */           .add(LootItem.lootTableItem(Items.BLUE_STAINED_GLASS_PANE))
/*  80 */           .add(LootItem.lootTableItem(Items.LIGHT_BLUE_STAINED_GLASS_PANE))
/*  81 */           .add(LootItem.lootTableItem(Items.RED_STAINED_GLASS_PANE))
/*  82 */           .add(LootItem.lootTableItem(Items.YELLOW_STAINED_GLASS_PANE))
/*  83 */           .add(LootItem.lootTableItem(Items.PURPLE_STAINED_GLASS_PANE))
/*  84 */           .add(LootItem.lootTableItem(Items.SPRUCE_HANGING_SIGN))
/*  85 */           .add(LootItem.lootTableItem(Items.OAK_HANGING_SIGN))
/*  86 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET))
/*  87 */           .add(LootItem.lootTableItem(Items.COAL))
/*  88 */           .add(LootItem.lootTableItem(Items.WHEAT_SEEDS))
/*  89 */           .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS))
/*  90 */           .add(LootItem.lootTableItem(Items.DEAD_BUSH))
/*  91 */           .add(LootItem.lootTableItem(Items.FLOWER_POT))
/*  92 */           .add(LootItem.lootTableItem(Items.STRING))
/*  93 */           .add(LootItem.lootTableItem(Items.LEAD))));
/*     */ 
/*     */ 
/*     */     
/*  97 */     output.accept(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, 
/*  98 */         LootTable.lootTable()
/*  99 */         .withPool(LootPool.lootPool()
/* 100 */           .setRolls(ConstantValue.exactly(1.0F))
/* 101 */           .add(LootItem.lootTableItem(Items.BURN_POTTERY_SHERD))
/* 102 */           .add(LootItem.lootTableItem(Items.DANGER_POTTERY_SHERD))
/* 103 */           .add(LootItem.lootTableItem(Items.FRIEND_POTTERY_SHERD))
/* 104 */           .add(LootItem.lootTableItem(Items.HEART_POTTERY_SHERD))
/* 105 */           .add(LootItem.lootTableItem(Items.HEARTBREAK_POTTERY_SHERD))
/* 106 */           .add(LootItem.lootTableItem(Items.HOWL_POTTERY_SHERD))
/* 107 */           .add(LootItem.lootTableItem(Items.SHEAF_POTTERY_SHERD))
/* 108 */           .add(LootItem.lootTableItem(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 109 */           .add(LootItem.lootTableItem(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 110 */           .add(LootItem.lootTableItem(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 111 */           .add(LootItem.lootTableItem(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 112 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_RELIC))));
/*     */ 
/*     */ 
/*     */     
/* 116 */     output.accept(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, 
/* 117 */         LootTable.lootTable()
/* 118 */         .withPool(LootPool.lootPool()
/* 119 */           .setRolls(ConstantValue.exactly(1.0F))
/* 120 */           .add(LootItem.lootTableItem(Items.ANGLER_POTTERY_SHERD))
/* 121 */           .add(LootItem.lootTableItem(Items.SHELTER_POTTERY_SHERD))
/* 122 */           .add(LootItem.lootTableItem(Items.SNORT_POTTERY_SHERD))
/* 123 */           .add(LootItem.lootTableItem(Items.SNIFFER_EGG))
/* 124 */           .add(LootItem.lootTableItem(Items.IRON_AXE))
/* 125 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
/* 126 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
/* 127 */           .add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
/* 128 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(2))
/* 129 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))));
/*     */ 
/*     */ 
/*     */     
/* 133 */     output.accept(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, 
/* 134 */         LootTable.lootTable()
/* 135 */         .withPool(LootPool.lootPool()
/* 136 */           .setRolls(ConstantValue.exactly(1.0F))
/* 137 */           .add(LootItem.lootTableItem(Items.BLADE_POTTERY_SHERD))
/* 138 */           .add(LootItem.lootTableItem(Items.EXPLORER_POTTERY_SHERD))
/* 139 */           .add(LootItem.lootTableItem(Items.MOURNER_POTTERY_SHERD))
/* 140 */           .add(LootItem.lootTableItem(Items.PLENTY_POTTERY_SHERD))
/* 141 */           .add(LootItem.lootTableItem(Items.IRON_AXE))
/* 142 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
/* 143 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
/* 144 */           .add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
/* 145 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(2))
/* 146 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaArchaeologyLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */