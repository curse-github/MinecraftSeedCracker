/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RedstoneWireConnectionsFix extends DataFix {
/* 11 */   public RedstoneWireConnectionsFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     Schema inputSchema = getInputSchema();
/* 17 */     return fixTypeEverywhereTyped("RedstoneConnectionsFix", inputSchema.getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), this::updateRedstoneConnections));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> updateRedstoneConnections(Dynamic<T> state) {
/* 21 */     boolean isRedstone = state.get("Name").asString().result().filter("minecraft:redstone_wire"::equals).isPresent();
/* 22 */     if (!isRedstone) {
/* 23 */       return state;
/*    */     }
/*    */     
/* 26 */     return state.update("Properties", props -> {
/* 27 */           String east = props.get("east").asString("none");
/* 28 */           String west = props.get("west").asString("none");
/* 29 */           String north = props.get("north").asString("none");
/* 30 */           String south = props.get("south").asString("none");
/* 31 */           boolean eastwest = (isConnected(east) || isConnected(west));
/* 32 */           boolean northsouth = (isConnected(north) || isConnected(south));
/*    */           
/* 34 */           String newEast = (!isConnected(east) && !northsouth) ? "side" : east;
/* 35 */           String newWest = (!isConnected(west) && !northsouth) ? "side" : west;
/* 36 */           String newNorth = (!isConnected(north) && !eastwest) ? "side" : north;
/* 37 */           String newSouth = (!isConnected(south) && !eastwest) ? "side" : south;
/*    */           
/* 39 */           return props
/* 40 */             .update("east", ())
/* 41 */             .update("west", ())
/* 42 */             .update("north", ())
/* 43 */             .update("south", ());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 48 */   private static boolean isConnected(String connectionType) { return !"none".equals(connectionType); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\RedstoneWireConnectionsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */