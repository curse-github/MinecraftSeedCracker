/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class BlockStateStructureTemplateFix extends DataFix {
/* 10 */   public BlockStateStructureTemplateFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("BlockStateStructureTemplateFix", getInputSchema().getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), BlockStateData::upgradeBlockStateTag)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockStateStructureTemplateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */