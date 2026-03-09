/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BoatSplitFix
/*    */   extends DataFix
/*    */ {
/* 18 */   public BoatSplitFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static boolean isNormalBoat(String id) { return id.equals("minecraft:boat"); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   private static boolean isChestBoat(String id) { return id.equals("minecraft:chest_boat"); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   private static boolean isAnyBoat(String id) { return (isNormalBoat(id) || isChestBoat(id)); }
/*    */ 
/*    */   
/*    */   private static String mapVariantToNormalBoat(String id) {
/* 34 */     switch (id) { default: case "spruce": case "birch": case "jungle": case "acacia": case "cherry": case "dark_oak": case "mangrove": case "bamboo": break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 43 */       "minecraft:bamboo_raft";
/*    */   }
/*    */ 
/*    */   
/*    */   private static String mapVariantToChestBoat(String id) {
/* 48 */     switch (id) { default: case "spruce": case "birch": case "jungle": case "acacia": case "cherry": case "dark_oak": case "mangrove": case "bamboo": break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       "minecraft:bamboo_chest_raft";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 63 */     OpticFinder<String> idF = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 65 */     Type<?> oldType = getInputSchema().getType(References.ENTITY);
/* 66 */     Type<?> newType = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 68 */     return fixTypeEverywhereTyped("BoatSplitFix", oldType, newType, input -> {
/*    */ 
/*    */ 
/*    */           
/* 72 */           Optional<String> id = input.getOptional(idF);
/* 73 */           if (id.isPresent() && isAnyBoat((String)id.get())) {
/* 74 */             String newId; Dynamic<?> tag = (Dynamic)input.getOrCreate(DSL.remainderFinder());
/* 75 */             Optional<String> maybeBoatId = tag.get("Type").asString().result();
/*    */             
/* 77 */             if (isChestBoat((String)id.get())) {
/* 78 */               newId = (String)maybeBoatId.map(BoatSplitFix::mapVariantToChestBoat).orElse("minecraft:oak_chest_boat");
/*    */             } else {
/* 80 */               newId = (String)maybeBoatId.map(BoatSplitFix::mapVariantToNormalBoat).orElse("minecraft:oak_boat");
/*    */             } 
/*    */             
/* 83 */             return ExtraDataFixUtils.cast(newType, input)
/* 84 */               .update(DSL.remainderFinder(), ())
/* 85 */               .set(idF, newId);
/*    */           } 
/* 87 */           return ExtraDataFixUtils.cast(newType, input);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BoatSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */