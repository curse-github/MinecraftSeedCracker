/*    */ package net.minecraft;
/*    */ 
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ public class IdentifierException
/*    */   extends RuntimeException {
/*  7 */   public IdentifierException(String message) { super(StringEscapeUtils.escapeJava(message)); }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public IdentifierException(String message, Throwable cause) { super(StringEscapeUtils.escapeJava(message), cause); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\IdentifierException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */