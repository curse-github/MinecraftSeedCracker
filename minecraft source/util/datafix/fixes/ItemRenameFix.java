/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class ItemRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public ItemRenameFix(Schema outputSchema, String name) {
/* 19 */     super(outputSchema, false);
/* 20 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     Type<Pair<String, String>> itemNameType = DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString());
/* 26 */     if (!Objects.equals(getInputSchema().getType(References.ITEM_NAME), itemNameType)) {
/* 27 */       throw new IllegalStateException("item name type is not what was expected.");
/*    */     }
/* 29 */     return fixTypeEverywhere(this.name, itemNameType, ops -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataFix create(Schema outputSchema, String name, final Function<String, String> fixItem) {
/* 35 */     return new ItemRenameFix(outputSchema, name)
/*    */       {
/*    */         protected String fixItem(String item) {
/* 38 */           return (String)fixItem.apply(item);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected abstract String fixItem(String paramString);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */