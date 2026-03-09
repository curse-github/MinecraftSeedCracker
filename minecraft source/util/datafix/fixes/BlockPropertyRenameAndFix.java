/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class BlockPropertyRenameAndFix
/*    */   extends AbstractBlockPropertyFix {
/*    */   private final String blockId;
/*    */   private final String oldPropertyName;
/*    */   private final String newPropertyName;
/*    */   private final UnaryOperator<String> valueFixer;
/*    */   
/*    */   public BlockPropertyRenameAndFix(Schema outputSchema, String name, String blockId, String oldPropertyName, String newPropertyName, UnaryOperator<String> valueFixer) {
/* 15 */     super(outputSchema, name);
/* 16 */     this.blockId = blockId;
/* 17 */     this.oldPropertyName = oldPropertyName;
/* 18 */     this.newPropertyName = newPropertyName;
/* 19 */     this.valueFixer = valueFixer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected boolean shouldFix(String blockId) { return blockId.equals(this.blockId); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected <T> Dynamic<T> fixProperties(String blockId, Dynamic<T> properties) { return properties.renameAndFixField(this.oldPropertyName, this.newPropertyName, dynamic -> dynamic.createString((String)this.valueFixer.apply(dynamic.asString("")))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockPropertyRenameAndFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */