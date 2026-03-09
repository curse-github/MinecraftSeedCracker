/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class ScoreboardDisplayNameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public ScoreboardDisplayNameFix(Schema outputSchema, String name, DSL.TypeReference type) {
/* 17 */     super(outputSchema, false);
/* 18 */     this.name = name;
/* 19 */     this.type = type;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type<?> inputType = getInputSchema().getType(this.type);
/* 25 */     OpticFinder<?> displayNameF = inputType.findField("DisplayName");
/*    */     
/* 27 */     OpticFinder<Pair<String, String>> textComponentF = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 28 */     return fixTypeEverywhereTyped(this.name, inputType, team -> 
/* 29 */         team.updateTyped(displayNameF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ScoreboardDisplayNameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */