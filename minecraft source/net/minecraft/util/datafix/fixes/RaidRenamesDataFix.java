/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class RaidRenamesDataFix extends DataFix {
/* 12 */   public RaidRenamesDataFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     return fixTypeEverywhereTyped("RaidRenamesDataFix", getInputSchema().getType(References.SAVED_DATA_RAIDS), input -> 
/* 18 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fix(Dynamic<?> tag) {
/* 23 */     return tag
/* 24 */       .renameAndFixField("Raids", "raids", raids -> 
/* 25 */         raids.createList(raids.asStream().map(RaidRenamesDataFix::fixRaid)))
/*    */       
/* 27 */       .renameField("Tick", "tick")
/* 28 */       .renameField("NextAvailableID", "next_id");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixRaid(Dynamic<?> raid) {
/* 32 */     return ExtraDataFixUtils.fixInlineBlockPos(raid, "CX", "CY", "CZ", "center")
/* 33 */       .renameField("Id", "id")
/* 34 */       .renameField("Started", "started")
/* 35 */       .renameField("Active", "active")
/* 36 */       .renameField("TicksActive", "ticks_active")
/* 37 */       .renameField("BadOmenLevel", "raid_omen_level")
/* 38 */       .renameField("GroupsSpawned", "groups_spawned")
/* 39 */       .renameField("PreRaidTicks", "cooldown_ticks")
/* 40 */       .renameField("PostRaidTicks", "post_raid_ticks")
/* 41 */       .renameField("TotalHealth", "total_health")
/* 42 */       .renameField("NumGroups", "group_count")
/* 43 */       .renameField("Status", "status")
/* 44 */       .renameField("HeroesOfTheVillage", "heroes_of_the_village");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\RaidRenamesDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */