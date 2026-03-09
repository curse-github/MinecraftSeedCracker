/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.CharPredicate;
/*    */ 
/*    */ public class ParserUtils {
/*    */   public static String readWhile(StringReader reader, CharPredicate predicate) {
/*  8 */     int start = reader.getCursor();
/*  9 */     while (reader.canRead() && predicate.test(reader.peek())) {
/* 10 */       reader.skip();
/*    */     }
/* 12 */     return reader.getString().substring(start, reader.getCursor());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\ParserUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */