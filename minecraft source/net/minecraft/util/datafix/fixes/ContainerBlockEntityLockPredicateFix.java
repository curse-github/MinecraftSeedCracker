/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ContainerBlockEntityLockPredicateFix extends DataFix {
/* 11 */   public ContainerBlockEntityLockPredicateFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("ContainerBlockEntityLockPredicateFix", getInputSchema().findChoiceType(References.BLOCK_ENTITY), ContainerBlockEntityLockPredicateFix::fixBlockEntity); }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> fixBlockEntity(Typed<?> entity) {
/* 21 */     return entity.update(DSL.remainderFinder(), tag -> 
/* 22 */         tag.renameAndFixField("Lock", "lock", LockComponentPredicateFix::fixLock));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ContainerBlockEntityLockPredicateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */