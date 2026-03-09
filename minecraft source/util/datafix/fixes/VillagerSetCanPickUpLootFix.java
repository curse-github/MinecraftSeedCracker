/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerSetCanPickUpLootFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   private static final String CAN_PICK_UP_LOOT = "CanPickUpLoot";
/*    */   
/* 16 */   public VillagerSetCanPickUpLootFix(Schema outputSchema) { super(outputSchema, true, "Villager CanPickUpLoot default value", References.ENTITY, "Villager"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), VillagerSetCanPickUpLootFix::fixValue); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   private static Dynamic<?> fixValue(Dynamic<?> tag) { return tag.set("CanPickUpLoot", tag.createBoolean(true)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerSetCanPickUpLootFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */