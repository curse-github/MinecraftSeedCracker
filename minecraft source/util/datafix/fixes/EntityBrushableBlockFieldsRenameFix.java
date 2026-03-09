/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityBrushableBlockFieldsRenameFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntityBrushableBlockFieldsRenameFix(Schema outputSchema) { super(outputSchema, false, "EntityBrushableBlockFieldsRenameFix", References.BLOCK_ENTITY, "minecraft:brushable_block"); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public Dynamic<?> fixTag(Dynamic<?> input) { return input.renameField("loot_table", "LootTable").renameField("loot_table_seed", "LootTableSeed"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityBrushableBlockFieldsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */