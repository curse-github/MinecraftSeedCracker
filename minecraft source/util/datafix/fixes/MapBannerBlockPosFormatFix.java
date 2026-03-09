/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class MapBannerBlockPosFormatFix extends DataFix {
/* 14 */   public MapBannerBlockPosFormatFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type<?> type = getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
/* 20 */     OpticFinder<?> dataF = type.findField("data");
/* 21 */     OpticFinder<?> bannersF = dataF.type().findField("banners");
/* 22 */     OpticFinder<?> bannerF = DSL.typeFinder(((List.ListType)bannersF.type()).getElement());
/* 23 */     return fixTypeEverywhereTyped("MapBannerBlockPosFormatFix", type, input -> 
/* 24 */         input.updateTyped(dataF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\MapBannerBlockPosFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */