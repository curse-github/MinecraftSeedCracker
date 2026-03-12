/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V702
/*    */   extends Schema
/*    */ {
/* 15 */   public V702(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 22 */     schema.register(map, "ZombieVillager", name -> DSL.optionalFields("Offers", 
/* 23 */           DSL.optionalFields("Recipes", 
/* 24 */             DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*    */ 
/*    */ 
/*    */     
/* 28 */     schema.registerSimple(map, "Husk");
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V702.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */