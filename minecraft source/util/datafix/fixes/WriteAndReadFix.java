/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class WriteAndReadFix extends DataFix {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public WriteAndReadFix(Schema outputSchema, String name, DSL.TypeReference type) {
/* 13 */     super(outputSchema, true);
/* 14 */     this.name = name;
/* 15 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected TypeRewriteRule makeRule() { return writeAndRead(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WriteAndReadFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */