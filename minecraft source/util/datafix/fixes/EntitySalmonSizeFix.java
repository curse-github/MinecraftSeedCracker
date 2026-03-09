/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntitySalmonSizeFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntitySalmonSizeFix(Schema outputSchema) { super(outputSchema, false, "EntitySalmonSizeFix", References.ENTITY, "minecraft:salmon"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> {
/*    */           
/* 17 */           String type = tag.get("type").asString("medium");
/* 18 */           if (type.equals("large"))
/*    */           {
/* 20 */             return tag;
/*    */           }
/*    */ 
/*    */ 
/*    */           
/* 25 */           return tag.set("type", tag.createString("medium"));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntitySalmonSizeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */