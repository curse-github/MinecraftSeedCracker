/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EquippableAssetRenameFix extends DataFix {
/* 12 */   public EquippableAssetRenameFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> componentsType = getInputSchema().getType(References.DATA_COMPONENTS);
/* 18 */     OpticFinder<?> equippableField = componentsType.findField("minecraft:equippable");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("equippable asset rename fix", componentsType, components -> 
/* 21 */         components.updateTyped(equippableField, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EquippableAssetRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */