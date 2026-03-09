/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AdvancementsRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AdvancementsRenameFix(Schema outputSchema, boolean changesType, String name, Function<String, String> renamer) {
/* 15 */     super(outputSchema, changesType);
/* 16 */     this.name = name;
/* 17 */     this.renamer = renamer;
/*    */   }
/*    */   private final Function<String, String> renamer;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AdvancementsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */