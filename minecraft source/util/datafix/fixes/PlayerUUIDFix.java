/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class PlayerUUIDFix extends AbstractUUIDFix {
/* 11 */   public PlayerUUIDFix(Schema outputSchema) { super(outputSchema, References.PLAYER); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("PlayerUUIDFix", getInputSchema().getType(this.typeReference), input -> {
/* 17 */           OpticFinder<?> rootVehicleFinder = input.getType().findField("RootVehicle");
/* 18 */           return input.updateTyped(rootVehicleFinder, rootVehicleFinder.type(), ())
/*    */             
/* 20 */             .update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\PlayerUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */