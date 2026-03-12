/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ 
/*    */ public class ProjectileStoredWeaponFix
/*    */   extends DataFix
/*    */ {
/* 19 */   public ProjectileStoredWeaponFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type<?> inputEntityType = getInputSchema().getType(References.ENTITY);
/* 25 */     Type<?> outputEntityType = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 27 */     return fixTypeEverywhereTyped("Fix Arrow stored weapon", inputEntityType, outputEntityType, ExtraDataFixUtils.chainAllFilters(new Function[] {
/* 28 */             fixChoice("minecraft:arrow"), 
/* 29 */             fixChoice("minecraft:spectral_arrow")
/*    */           }));
/*    */   }
/*    */   
/*    */   private Function<Typed<?>, Typed<?>> fixChoice(String entityName) {
/* 34 */     Type<?> inputEntityChoiceType = getInputSchema().getChoiceType(References.ENTITY, entityName);
/* 35 */     Type<?> outputEntityChoiceType = getOutputSchema().getChoiceType(References.ENTITY, entityName);
/*    */     
/* 37 */     return fixChoiceCap(entityName, inputEntityChoiceType, outputEntityChoiceType);
/*    */   }
/*    */   
/*    */   private static <T> Function<Typed<?>, Typed<?>> fixChoiceCap(String entityName, Type<?> inputEntityChoiceType, Type<T> outputEntityChoiceType) {
/* 41 */     OpticFinder<?> entityF = DSL.namedChoice(entityName, inputEntityChoiceType);
/* 42 */     return input -> input.updateTyped(entityF, outputEntityChoiceType, ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ProjectileStoredWeaponFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */