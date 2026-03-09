/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FilteredSignsFix
/*    */   extends NamedEntityWriteReadFix {
/*  8 */   public FilteredSignsFix(Schema outputSchema) { super(outputSchema, false, "Remove filtered text from signs", References.BLOCK_ENTITY, "minecraft:sign"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   protected <T> Dynamic<T> fix(Dynamic<T> input) { return input.remove("FilteredText1").remove("FilteredText2").remove("FilteredText3").remove("FilteredText4"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FilteredSignsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */