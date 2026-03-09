/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class BeehiveFieldRenameFix extends DataFix {
/* 15 */   public BeehiveFieldRenameFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   private Dynamic<?> fixBeehive(Dynamic<?> beehive) { return beehive.remove("Bees"); }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixBee(Dynamic<?> bee) {
/* 25 */     bee = bee.remove("EntityData");
/* 26 */     bee = bee.renameField("TicksInHive", "ticks_in_hive");
/* 27 */     return bee.renameField("MinOccupationTicks", "min_ticks_in_hive");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 33 */     Type<?> beehiveType = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:beehive");
/* 34 */     OpticFinder<?> beehiveF = DSL.namedChoice("minecraft:beehive", beehiveType);
/*    */     
/* 36 */     List.ListType<?> beesType = (List.ListType)beehiveType.findFieldType("Bees");
/* 37 */     Type<?> beeType = beesType.getElement();
/*    */     
/* 39 */     OpticFinder<?> beesF = DSL.fieldFinder("Bees", beesType);
/* 40 */     OpticFinder<?> beeF = DSL.typeFinder(beeType);
/*    */     
/* 42 */     Type<?> entityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 43 */     Type<?> newEntityType = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 44 */     return fixTypeEverywhereTyped("BeehiveFieldRenameFix", entityType, newEntityType, input -> ExtraDataFixUtils.cast(newEntityType, input
/*    */           
/* 46 */           .updateTyped(beehiveF, ())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BeehiveFieldRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */