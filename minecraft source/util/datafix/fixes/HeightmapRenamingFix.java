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
/*    */ 
/*    */ public class HeightmapRenamingFix
/*    */   extends DataFix {
/* 15 */   public HeightmapRenamingFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type<?> inputType = getInputSchema().getType(References.CHUNK);
/* 21 */     OpticFinder<?> levelF = inputType.findField("Level");
/* 22 */     return fixTypeEverywhereTyped("HeightmapRenamingFix", inputType, input -> input.updateTyped(levelF, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> tag) {
/* 28 */     Optional<? extends Dynamic<?>> heightmaps = tag.get("Heightmaps").result();
/* 29 */     if (heightmaps.isEmpty()) {
/* 30 */       return tag;
/*    */     }
/*    */     
/* 33 */     Dynamic<?> heightmapsTag = (Dynamic)heightmaps.get();
/*    */     
/* 35 */     Optional<? extends Dynamic<?>> liquid = heightmapsTag.get("LIQUID").result();
/* 36 */     if (liquid.isPresent()) {
/* 37 */       heightmapsTag = heightmapsTag.remove("LIQUID");
/* 38 */       heightmapsTag = heightmapsTag.set("WORLD_SURFACE_WG", (Dynamic)liquid.get());
/*    */     } 
/*    */     
/* 41 */     Optional<? extends Dynamic<?>> solid = heightmapsTag.get("SOLID").result();
/* 42 */     if (solid.isPresent()) {
/* 43 */       heightmapsTag = heightmapsTag.remove("SOLID");
/* 44 */       heightmapsTag = heightmapsTag.set("OCEAN_FLOOR_WG", (Dynamic)solid.get());
/* 45 */       heightmapsTag = heightmapsTag.set("OCEAN_FLOOR", (Dynamic)solid.get());
/*    */     } 
/*    */     
/* 48 */     Optional<? extends Dynamic<?>> light = heightmapsTag.get("LIGHT").result();
/* 49 */     if (light.isPresent()) {
/* 50 */       heightmapsTag = heightmapsTag.remove("LIGHT");
/* 51 */       heightmapsTag = heightmapsTag.set("LIGHT_BLOCKING", (Dynamic)light.get());
/*    */     } 
/*    */     
/* 54 */     Optional<? extends Dynamic<?>> rain = heightmapsTag.get("RAIN").result();
/* 55 */     if (rain.isPresent()) {
/* 56 */       heightmapsTag = heightmapsTag.remove("RAIN");
/* 57 */       heightmapsTag = heightmapsTag.set("MOTION_BLOCKING", (Dynamic)rain.get());
/* 58 */       heightmapsTag = heightmapsTag.set("MOTION_BLOCKING_NO_LEAVES", (Dynamic)rain.get());
/*    */     } 
/*    */     
/* 61 */     return tag.set("Heightmaps", heightmapsTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\HeightmapRenamingFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */