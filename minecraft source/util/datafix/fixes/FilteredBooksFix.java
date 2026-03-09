/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class FilteredBooksFix extends ItemStackTagFix {
/*  9 */   public FilteredBooksFix(Schema outputSchema) { super(outputSchema, "Remove filtered text from books", id -> (id.equals("minecraft:writable_book") || id.equals("minecraft:written_book"))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Typed<?> fixItemStackTag(Typed<?> tag) { return Util.writeAndReadTypedOrThrow(tag, tag.getType(), dynamic -> dynamic.remove("filtered_title").remove("filtered_pages")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FilteredBooksFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */