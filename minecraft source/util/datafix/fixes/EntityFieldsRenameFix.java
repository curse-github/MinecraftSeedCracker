/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EntityFieldsRenameFix
/*    */   extends NamedEntityFix {
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public EntityFieldsRenameFix(Schema outputSchema, String name, String entityType, Map<String, String> renames) {
/* 14 */     super(outputSchema, false, name, References.ENTITY, entityType);
/* 15 */     this.renames = renames;
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> data) {
/* 19 */     for (Map.Entry<String, String> entry : this.renames.entrySet()) {
/* 20 */       data = data.renameField((String)entry.getKey(), (String)entry.getValue());
/*    */     }
/* 22 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityFieldsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */