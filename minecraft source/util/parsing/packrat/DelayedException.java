/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface DelayedException<T extends Exception>
/*    */ {
/* 12 */   static DelayedException<CommandSyntaxException> create(SimpleCommandExceptionType type) { return (contents, position) -> type.createWithContext(StringReaderTerms.createReader(contents, position)); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   static DelayedException<CommandSyntaxException> create(DynamicCommandExceptionType type, String argument) { return (contents, position) -> type.createWithContext(StringReaderTerms.createReader(contents, position), argument); }
/*    */   
/*    */   T create(String paramString, int paramInt);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\DelayedException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */