/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class TranslatableFormatException
/*    */   extends IllegalArgumentException {
/*  7 */   public TranslatableFormatException(TranslatableContents component, String message) { super(String.format(Locale.ROOT, "Error parsing: %s: %s", new Object[] { component, message })); }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public TranslatableFormatException(TranslatableContents component, int index) { super(String.format(Locale.ROOT, "Invalid index %d requested for %s", new Object[] { Integer.valueOf(index), component })); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public TranslatableFormatException(TranslatableContents component, Throwable t) { super(String.format(Locale.ROOT, "Error while parsing: %s", new Object[] { component }), t); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\TranslatableFormatException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */