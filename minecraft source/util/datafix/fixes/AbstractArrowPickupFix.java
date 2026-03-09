/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AbstractArrowPickupFix
/*    */   extends DataFix
/*    */ {
/* 17 */   public AbstractArrowPickupFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Schema inputSchema = getInputSchema();
/* 23 */     return fixTypeEverywhereTyped("AbstractArrowPickupFix", inputSchema.getType(References.ENTITY), this::updateProjectiles);
/*    */   }
/*    */   
/*    */   private Typed<?> updateProjectiles(Typed<?> input) {
/* 27 */     input = updateEntity(input, "minecraft:arrow", AbstractArrowPickupFix::updatePickup);
/* 28 */     input = updateEntity(input, "minecraft:spectral_arrow", AbstractArrowPickupFix::updatePickup);
/* 29 */     return updateEntity(input, "minecraft:trident", AbstractArrowPickupFix::updatePickup);
/*    */   }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updatePickup(Dynamic<?> tag) {
/* 34 */     if (tag.get("pickup").result().isPresent()) {
/* 35 */       return tag;
/*    */     }
/*    */     
/* 38 */     boolean fromPlayer = tag.get("player").asBoolean(true);
/* 39 */     return tag.set("pickup", tag.createByte((byte)(fromPlayer ? 1 : 0))).remove("player");
/*    */   }
/*    */   
/*    */   private Typed<?> updateEntity(Typed<?> input, String name, Function<Dynamic<?>, Dynamic<?>> function) {
/* 43 */     Type<?> oldType = getInputSchema().getChoiceType(References.ENTITY, name);
/* 44 */     Type<?> newType = getOutputSchema().getChoiceType(References.ENTITY, name);
/* 45 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, entity -> entity.update(DSL.remainderFinder(), function));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AbstractArrowPickupFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */