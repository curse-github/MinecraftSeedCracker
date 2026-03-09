/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityPaintingItemFrameDirectionFix extends DataFix {
/* 12 */   private static final int[][] DIRECTIONS = { { 0, 0, 1 }, { -1, 0, 0 }, { 0, 0, -1 }, { 1, 0, 0 } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public EntityPaintingItemFrameDirectionFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> doFix(Dynamic<?> input, boolean isPainting, boolean isItemFrame) {
/* 24 */     if ((isPainting || isItemFrame) && input.get("Facing").asNumber().result().isEmpty()) {
/*    */       int direction;
/* 26 */       if (input.get("Direction").asNumber().result().isPresent()) {
/* 27 */         direction = input.get("Direction").asByte((byte)0) % DIRECTIONS.length;
/* 28 */         int[] steps = DIRECTIONS[direction];
/*    */         
/* 30 */         input = input.set("TileX", input.createInt(input.get("TileX").asInt(0) + steps[0]));
/* 31 */         input = input.set("TileY", input.createInt(input.get("TileY").asInt(0) + steps[1]));
/* 32 */         input = input.set("TileZ", input.createInt(input.get("TileZ").asInt(0) + steps[2]));
/*    */         
/* 34 */         input = input.remove("Direction");
/*    */         
/* 36 */         if (isItemFrame && input.get("ItemRotation").asNumber().result().isPresent()) {
/* 37 */           input = input.set("ItemRotation", input.createByte((byte)(input.get("ItemRotation").asByte((byte)0) * 2)));
/*    */         }
/*    */       } else {
/* 40 */         direction = input.get("Dir").asByte((byte)0) % DIRECTIONS.length;
/* 41 */         input = input.remove("Dir");
/*    */       } 
/* 43 */       input = input.set("Facing", input.createByte((byte)direction));
/*    */     } 
/*    */     
/* 46 */     return input;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 51 */     Type<?> paintingType = getInputSchema().getChoiceType(References.ENTITY, "Painting");
/* 52 */     OpticFinder<?> paintingF = DSL.namedChoice("Painting", paintingType);
/*    */     
/* 54 */     Type<?> itemFrameType = getInputSchema().getChoiceType(References.ENTITY, "ItemFrame");
/* 55 */     OpticFinder<?> itemFrameF = DSL.namedChoice("ItemFrame", itemFrameType);
/*    */     
/* 57 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/*    */     
/* 59 */     TypeRewriteRule paintingRule = fixTypeEverywhereTyped("EntityPaintingFix", entityType, input -> 
/* 60 */         input.updateTyped(paintingF, paintingType, ()));
/*    */     
/* 62 */     TypeRewriteRule itemFrameRule = fixTypeEverywhereTyped("EntityItemFrameFix", entityType, input -> 
/* 63 */         input.updateTyped(itemFrameF, itemFrameType, ()));
/*    */ 
/*    */     
/* 66 */     return TypeRewriteRule.seq(paintingRule, itemFrameRule);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityPaintingItemFrameDirectionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */