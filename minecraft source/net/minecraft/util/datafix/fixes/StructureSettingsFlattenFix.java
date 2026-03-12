/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class StructureSettingsFlattenFix extends DataFix {
/* 15 */   public StructureSettingsFlattenFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type<?> worldGenSettingsType = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/*    */     
/* 22 */     OpticFinder<?> dimensions = worldGenSettingsType.findField("dimensions");
/*    */     
/* 24 */     return fixTypeEverywhereTyped("StructureSettingsFlatten", worldGenSettingsType, input -> 
/* 25 */         input.updateTyped(dimensions, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Pair<Dynamic<?>, Dynamic<?>> fixDimension(Pair<Dynamic<?>, Dynamic<?>> entry) {
/* 32 */     Dynamic<?> dimension = (Dynamic)entry.getSecond();
/* 33 */     return Pair.of((Dynamic)entry.getFirst(), dimension
/* 34 */         .update("generator", g -> 
/* 35 */           g.update("settings", ())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixStructures(Dynamic<?> input) {
/* 42 */     Dynamic<?> structures = input.get("structures").orElseEmptyMap().updateMapValues(p -> 
/* 43 */         p.mapSecond(()));
/*    */     
/* 45 */     return (Dynamic)DataFixUtils.orElse(input
/* 46 */         .get("stronghold").result().map(stronghold -> 
/* 47 */           structures.set("minecraft:stronghold", stronghold.set("type", input.createString("minecraft:concentric_rings")))), structures);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\StructureSettingsFlattenFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */