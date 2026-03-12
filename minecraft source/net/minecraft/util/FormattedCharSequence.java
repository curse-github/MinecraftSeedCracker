/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface FormattedCharSequence
/*    */ {
/*    */   public static final FormattedCharSequence EMPTY = output -> true;
/*    */   
/* 16 */   static FormattedCharSequence codepoint(int codepoint, Style style) { return output -> output.accept(0, style, codepoint); }
/*    */ 
/*    */   
/*    */   static FormattedCharSequence forward(String plainText, Style style) {
/* 20 */     if (plainText.isEmpty()) {
/* 21 */       return EMPTY;
/*    */     }
/* 23 */     return output -> StringDecomposer.iterate(plainText, style, output);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence forward(String plainText, Style style, Int2IntFunction modifier) {
/* 27 */     if (plainText.isEmpty()) {
/* 28 */       return EMPTY;
/*    */     }
/* 30 */     return output -> StringDecomposer.iterate(plainText, style, decorateOutput(output, modifier));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence backward(String plainText, Style style) {
/* 34 */     if (plainText.isEmpty()) {
/* 35 */       return EMPTY;
/*    */     }
/* 37 */     return output -> StringDecomposer.iterateBackwards(plainText, style, output);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence backward(String plainText, Style style, Int2IntFunction modifier) {
/* 41 */     if (plainText.isEmpty()) {
/* 42 */       return EMPTY;
/*    */     }
/* 44 */     return output -> StringDecomposer.iterateBackwards(plainText, style, decorateOutput(output, modifier));
/*    */   }
/*    */ 
/*    */   
/* 48 */   static FormattedCharSink decorateOutput(FormattedCharSink output, Int2IntFunction modifier) { return (p, s, ch) -> output.accept(p, s, ((Integer)modifier.apply(Integer.valueOf(ch))).intValue()); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   static FormattedCharSequence composite() { return EMPTY; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   static FormattedCharSequence composite(FormattedCharSequence part) { return part; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   static FormattedCharSequence composite(FormattedCharSequence first, FormattedCharSequence second) { return fromPair(first, second); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   static FormattedCharSequence composite(FormattedCharSequence... parts) { return fromList(ImmutableList.copyOf(parts)); }
/*    */ 
/*    */   
/*    */   static FormattedCharSequence composite(List<FormattedCharSequence> parts) {
/* 68 */     int size = parts.size();
/* 69 */     switch (size) {
/*    */       case 0:
/* 71 */         return EMPTY;
/*    */       case 1:
/* 73 */         return (FormattedCharSequence)parts.get(0);
/*    */       case 2:
/* 75 */         return fromPair((FormattedCharSequence)parts.get(0), (FormattedCharSequence)parts.get(1));
/*    */     } 
/* 77 */     return fromList(ImmutableList.copyOf(parts));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   static FormattedCharSequence fromPair(FormattedCharSequence first, FormattedCharSequence second) { return output -> (first.accept(output) && second.accept(output)); }
/*    */ 
/*    */   
/*    */   static FormattedCharSequence fromList(List<FormattedCharSequence> partCopy) {
/* 86 */     return output -> {
/* 87 */         for (FormattedCharSequence part : partCopy) {
/* 88 */           if (!part.accept(output)) {
/* 89 */             return false;
/*    */           }
/*    */         } 
/* 92 */         return true;
/*    */       };
/*    */   }
/*    */   
/*    */   boolean accept(FormattedCharSink paramFormattedCharSink);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\FormattedCharSequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */