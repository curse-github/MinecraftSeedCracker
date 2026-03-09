/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddFieldFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   private final String fieldName;
/*    */   private final String[] path;
/*    */   private final Function<Dynamic<?>, Dynamic<?>> fieldGenerator;
/*    */   
/*    */   public AddFieldFix(Schema outputSchema, DSL.TypeReference type, String fieldName, Function<Dynamic<?>, Dynamic<?>> fieldGenerator, String... path) {
/* 28 */     super(outputSchema, false);
/* 29 */     this.name = "Adding field `" + fieldName + "` to type `" + type.typeName().toLowerCase(Locale.ROOT) + "`";
/* 30 */     this.type = type;
/* 31 */     this.fieldName = fieldName;
/* 32 */     this.path = path;
/* 33 */     this.fieldGenerator = fieldGenerator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 38 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type), input -> 
/* 39 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> addField(Dynamic<?> dynamic, int pathIndex) {
/* 43 */     if (pathIndex >= this.path.length) {
/* 44 */       return dynamic.set(this.fieldName, (Dynamic)this.fieldGenerator.apply(dynamic));
/*    */     }
/*    */     
/* 47 */     Optional<? extends Dynamic<?>> field = dynamic.get(this.path[pathIndex]).result();
/* 48 */     if (field.isEmpty()) {
/* 49 */       return dynamic;
/*    */     }
/*    */     
/* 52 */     return addField((Dynamic)field.get(), pathIndex + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AddFieldFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */