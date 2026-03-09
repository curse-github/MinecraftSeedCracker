/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ public class OminousBannerBlockEntityRenameFix
/*    */   extends NamedEntityFix {
/* 11 */   public OminousBannerBlockEntityRenameFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "OminousBannerBlockEntityRenameFix", References.BLOCK_ENTITY, "minecraft:banner"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 16 */     OpticFinder<?> customNameF = entity.getType().findField("CustomName");
/*    */     
/* 18 */     OpticFinder<Pair<String, String>> textComponentF = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 19 */     return entity.updateTyped(customNameF, customName -> 
/* 20 */         customName.update(textComponentF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OminousBannerBlockEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */