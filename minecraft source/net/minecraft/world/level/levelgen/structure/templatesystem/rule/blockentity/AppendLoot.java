/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public class AppendLoot implements RuleBlockEntityModifier {
/* 13 */   public static final MapCodec<AppendLoot> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LootTable.KEY_CODEC
/* 14 */         .fieldOf("loot_table").forGetter(()))
/* 15 */       .apply(i, AppendLoot::new));
/*    */   
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */ 
/*    */   
/* 20 */   public AppendLoot(ResourceKey<LootTable> lootTable) { this.lootTable = lootTable; }
/*    */ 
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) {
/* 25 */     CompoundTag result = (existingTag == null) ? new CompoundTag() : existingTag.copy();
/*    */     
/* 27 */     result.store("LootTable", LootTable.KEY_CODEC, this.lootTable);
/* 28 */     result.putLong("LootTableSeed", random.nextLong());
/*    */     
/* 30 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public RuleBlockEntityModifierType<?> getType() { return RuleBlockEntityModifierType.APPEND_LOOT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\AppendLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */