/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WeaponSmithChestLootTableFix extends NamedEntityFix {
/*  9 */   public WeaponSmithChestLootTableFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "WeaponSmithChestLootTableFix", References.BLOCK_ENTITY, "minecraft:chest"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 14 */     return entity.update(DSL.remainderFinder(), tag -> {
/* 15 */           String lootTable = tag.get("LootTable").asString("");
/* 16 */           return lootTable.equals("minecraft:chests/village_blacksmith") ? 
/* 17 */             tag.set("LootTable", tag.createString("minecraft:chests/village/village_weaponsmith")) : 
/* 18 */             tag;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WeaponSmithChestLootTableFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */