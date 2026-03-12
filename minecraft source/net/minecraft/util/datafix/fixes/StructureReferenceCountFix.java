/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class StructureReferenceCountFix extends DataFix {
/* 12 */   public StructureReferenceCountFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> structureInfo = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 18 */     return fixTypeEverywhereTyped("Structure Reference Fix", structureInfo, input -> 
/* 19 */         input.update(DSL.remainderFinder(), StructureReferenceCountFix::setCountToAtLeastOne));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static <T> Dynamic<T> setCountToAtLeastOne(Dynamic<T> structureTag) { return structureTag.update("references", references -> references.createInt(((Integer)references.asNumber().map(Number::intValue).result().filter(()).orElse(Integer.valueOf(1))).intValue())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\StructureReferenceCountFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */