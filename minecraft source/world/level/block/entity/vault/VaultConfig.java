/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class VaultConfig extends Record {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   private final double activationRange;
/*    */   private final double deactivationRange;
/*    */   private final ItemStack keyItem;
/*    */   
/* 16 */   public VaultConfig(ResourceKey<LootTable> lootTable, double activationRange, double deactivationRange, ItemStack keyItem, Optional<ResourceKey<LootTable>> overrideLootTableToDisplay, PlayerDetector playerDetector, PlayerDetector.EntitySelector entitySelector) { this.lootTable = lootTable; this.activationRange = activationRange; this.deactivationRange = deactivationRange; this.keyItem = keyItem; this.overrideLootTableToDisplay = overrideLootTableToDisplay; this.playerDetector = playerDetector; this.entitySelector = entitySelector; } private final Optional<ResourceKey<LootTable>> overrideLootTableToDisplay; private final PlayerDetector playerDetector; private final PlayerDetector.EntitySelector entitySelector; static final String TAG_NAME = "config"; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<LootTable> lootTable() { return this.lootTable; } public double activationRange() { return this.activationRange; } public double deactivationRange() { return this.deactivationRange; } public ItemStack keyItem() { return this.keyItem; } public Optional<ResourceKey<LootTable>> overrideLootTableToDisplay() { return this.overrideLootTableToDisplay; } public PlayerDetector.EntitySelector entitySelector() { return this.entitySelector; }
/*    */   
/* 18 */   static VaultConfig DEFAULT = new VaultConfig();
/* 19 */   static Codec<VaultConfig> CODEC = RecordCodecBuilder.create(i -> i.group(LootTable.KEY_CODEC
/* 20 */         .lenientOptionalFieldOf("loot_table", DEFAULT.lootTable()).forGetter(VaultConfig::lootTable), Codec.DOUBLE
/* 21 */         .lenientOptionalFieldOf("activation_range", Double.valueOf(DEFAULT.activationRange())).forGetter(VaultConfig::activationRange), Codec.DOUBLE
/* 22 */         .lenientOptionalFieldOf("deactivation_range", Double.valueOf(DEFAULT.deactivationRange())).forGetter(VaultConfig::deactivationRange), 
/* 23 */         ItemStack.lenientOptionalFieldOf("key_item").forGetter(VaultConfig::keyItem), LootTable.KEY_CODEC
/* 24 */         .lenientOptionalFieldOf("override_loot_table_to_display").forGetter(VaultConfig::overrideLootTableToDisplay))
/* 25 */       .apply(i, VaultConfig::new)).validate(VaultConfig::validate);
/*    */   
/*    */   private VaultConfig() {
/* 28 */     this(BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 4.0D, 4.5D, new ItemStack(Items.TRIAL_KEY), 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 33 */         Optional.empty(), PlayerDetector.INCLUDING_CREATIVE_PLAYERS, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public VaultConfig(ResourceKey<LootTable> lootTable, double activationRange, double deactivationRange, ItemStack keyItem, Optional<ResourceKey<LootTable>> overrideDisplayItems) {
/* 39 */     this(lootTable, activationRange, deactivationRange, keyItem, overrideDisplayItems, DEFAULT
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 45 */         .playerDetector(), DEFAULT
/* 46 */         .entitySelector());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public PlayerDetector playerDetector() { return SharedConstants.DEBUG_VAULT_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : this.playerDetector; }
/*    */ 
/*    */   
/*    */   private DataResult<VaultConfig> validate() {
/* 55 */     if (this.activationRange > this.deactivationRange) {
/* 56 */       return DataResult.error(() -> "Activation range must (" + this.activationRange + ") be less or equal to deactivation range (" + this.deactivationRange + ")");
/*    */     }
/* 58 */     return DataResult.success(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */