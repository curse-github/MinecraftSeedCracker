/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.level.storage.loot.LootParams;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface RandomizableContainer
/*    */   extends Container {
/*    */   public static final String LOOT_TABLE_TAG = "LootTable";
/*    */   public static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
/*    */   
/*    */   ResourceKey<LootTable> getLootTable();
/*    */   
/*    */   void setLootTable(ResourceKey<LootTable> paramResourceKey);
/*    */   
/*    */   default void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
/* 31 */     setLootTable(lootTable);
/* 32 */     setLootTableSeed(seed);
/*    */   }
/*    */   
/*    */   long getLootTableSeed();
/*    */   
/*    */   void setLootTableSeed(long paramLong);
/*    */   
/*    */   BlockPos getBlockPos();
/*    */   
/*    */   Level getLevel();
/*    */   
/*    */   static void setBlockEntityLootTable(BlockGetter level, RandomSource random, BlockPos blockEntityPos, ResourceKey<LootTable> lootTable) {
/* 44 */     BlockEntity blockEntity = level.getBlockEntity(blockEntityPos);
/* 45 */     if (blockEntity instanceof RandomizableContainer) { RandomizableContainer randomizableContainer = (RandomizableContainer)blockEntity;
/* 46 */       randomizableContainer.setLootTable(lootTable, random.nextLong()); }
/*    */   
/*    */   }
/*    */   
/*    */   default boolean tryLoadLootTable(ValueInput base) {
/* 51 */     ResourceKey<LootTable> lootTable = (ResourceKey)base.read("LootTable", LootTable.KEY_CODEC).orElse(null);
/* 52 */     setLootTable(lootTable);
/* 53 */     setLootTableSeed(base.getLongOr("LootTableSeed", 0L));
/* 54 */     return (lootTable != null);
/*    */   }
/*    */   
/*    */   default boolean trySaveLootTable(ValueOutput base) {
/* 58 */     ResourceKey<LootTable> lootTable = getLootTable();
/* 59 */     if (lootTable == null) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     base.store("LootTable", LootTable.KEY_CODEC, lootTable);
/* 64 */     long lootTableSeed = getLootTableSeed();
/* 65 */     if (lootTableSeed != 0L) {
/* 66 */       base.putLong("LootTableSeed", lootTableSeed);
/*    */     }
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   default void unpackLootTable(Player player) {
/* 72 */     Level level = getLevel();
/* 73 */     BlockPos worldPosition = getBlockPos();
/* 74 */     ResourceKey<LootTable> lootTableKey = getLootTable();
/*    */     
/* 76 */     if (lootTableKey != null && level != null && level.getServer() != null) {
/* 77 */       LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableKey);
/* 78 */       if (player instanceof ServerPlayer) {
/* 79 */         CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, lootTableKey);
/*    */       }
/* 81 */       setLootTable(null);
/*    */       
/* 83 */       LootParams.Builder params = (new LootParams.Builder((ServerLevel)level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition));
/*    */       
/* 85 */       if (player != null) {
/* 86 */         params.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
/*    */       }
/*    */       
/* 89 */       lootTable.fill(this, params.create(LootContextParamSets.CHEST), getLootTableSeed());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\RandomizableContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */