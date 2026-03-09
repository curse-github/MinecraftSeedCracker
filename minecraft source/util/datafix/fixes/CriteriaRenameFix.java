/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class CriteriaRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public CriteriaRenameFix(Schema outputSchema, String name, String advancementId, UnaryOperator<String> conversions) {
/* 18 */     super(outputSchema, false);
/* 19 */     this.name = name;
/* 20 */     this.advancementId = advancementId;
/* 21 */     this.conversions = conversions;
/*    */   }
/*    */   private final String advancementId;
/*    */   private final UnaryOperator<String> conversions;
/*    */   
/* 26 */   protected TypeRewriteRule makeRule() { return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), input -> input.update(DSL.remainderFinder(), this::fixAdvancements)); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixAdvancements(Dynamic<?> tag) {
/* 30 */     return tag.update(this.advancementId, advancement -> 
/*    */         
/* 32 */         advancement.update("criteria", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\CriteriaRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */