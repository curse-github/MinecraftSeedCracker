/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.Util;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class WorldGenSettingsHeightAndBiomeFix
/*    */   extends DataFix {
/*    */   private static final String NAME = "WorldGenSettingsHeightAndBiomeFix";
/*    */   public static final String WAS_PREVIOUSLY_INCREASED_KEY = "has_increased_height_already";
/*    */   
/* 22 */   public WorldGenSettingsHeightAndBiomeFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> worldGenSettingsType = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 28 */     OpticFinder<?> dimensionsFinder = worldGenSettingsType.findField("dimensions");
/*    */     
/* 30 */     Type<?> worldGenSettingsTypeNew = getOutputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 31 */     Type<?> dimensionsType = worldGenSettingsTypeNew.findFieldType("dimensions");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("WorldGenSettingsHeightAndBiomeFix", worldGenSettingsType, worldGenSettingsTypeNew, input -> {
/* 34 */           OptionalDynamic<?> wasIncreasedOpt = ((Dynamic)input.get(DSL.remainderFinder())).get("has_increased_height_already");
/*    */           
/* 36 */           boolean wasExpSnap = wasIncreasedOpt.result().isEmpty();
/* 37 */           boolean wasPreviouslyIncreased = wasIncreasedOpt.asBoolean(true);
/* 38 */           return input.update(DSL.remainderFinder(), ()).updateTyped(dimensionsFinder, dimensionsType, ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updateLayers(Dynamic<?> layers) {
/* 83 */     Dynamic<?> airLayer = layers.createMap(ImmutableMap.of(layers
/* 84 */           .createString("height"), layers
/* 85 */           .createInt(64), layers
/* 86 */           .createString("block"), layers
/* 87 */           .createString("minecraft:air")));
/*    */     
/* 89 */     return layers.createList(Stream.concat(Stream.of(airLayer), layers.asStream()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsHeightAndBiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */