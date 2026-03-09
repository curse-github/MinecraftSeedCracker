/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CatTypeFix
/*    */   extends NamedEntityFix {
/* 10 */   public CatTypeFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "CatTypeFix", References.ENTITY, "minecraft:cat"); }
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     if (input.get("CatType").asInt(0) == 9) {
/* 15 */       return input.set("CatType", input.createInt(10));
/*    */     }
/* 17 */     return input;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\CatTypeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */