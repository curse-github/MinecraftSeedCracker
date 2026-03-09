/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class EmptyItemInHotbarFix
/*    */   extends DataFix {
/* 17 */   public EmptyItemInHotbarFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     OpticFinder<Pair<String, Pair<Either<Pair<String, String>, Unit>, Pair<Either<?, Unit>, Dynamic<?>>>>> itemStackF = DSL.typeFinder(getInputSchema().getType(References.ITEM_STACK));
/*    */     
/* 25 */     return fixTypeEverywhereTyped("EmptyItemInHotbarFix", getInputSchema().getType(References.HOTBAR), input -> input.update(itemStackF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EmptyItemInHotbarFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */