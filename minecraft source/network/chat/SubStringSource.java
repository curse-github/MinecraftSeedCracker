/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.StringDecomposer;
/*    */ 
/*    */ public class SubStringSource
/*    */ {
/*    */   private final String plainText;
/*    */   private final List<Style> charStyles;
/*    */   private final Int2IntFunction reverseCharModifier;
/*    */   
/*    */   private SubStringSource(String plainText, List<Style> charStyles, Int2IntFunction reverseCharModifier) {
/* 19 */     this.plainText = plainText;
/* 20 */     this.charStyles = ImmutableList.copyOf(charStyles);
/* 21 */     this.reverseCharModifier = reverseCharModifier;
/*    */   }
/*    */ 
/*    */   
/* 25 */   public String getPlainText() { return this.plainText; }
/*    */ 
/*    */   
/*    */   public List<FormattedCharSequence> substring(int start, int length, boolean reverse) {
/* 29 */     if (length == 0) {
/* 30 */       return ImmutableList.of();
/*    */     }
/*    */     
/* 33 */     List<FormattedCharSequence> parts = Lists.newArrayList();
/*    */     
/* 35 */     Style currentRunStyle = (Style)this.charStyles.get(start);
/* 36 */     int currentRunStart = start;
/* 37 */     for (int i = 1; i < length; i++) {
/* 38 */       int actualIndex = start + i;
/* 39 */       Style charStyle = (Style)this.charStyles.get(actualIndex);
/* 40 */       if (!charStyle.equals(currentRunStyle)) {
/* 41 */         String currentRunText = this.plainText.substring(currentRunStart, actualIndex);
/* 42 */         parts.add(reverse ? FormattedCharSequence.backward(currentRunText, currentRunStyle, this.reverseCharModifier) : FormattedCharSequence.forward(currentRunText, currentRunStyle));
/* 43 */         currentRunStyle = charStyle;
/* 44 */         currentRunStart = actualIndex;
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     if (currentRunStart < start + length) {
/* 49 */       String lastRunText = this.plainText.substring(currentRunStart, start + length);
/* 50 */       parts.add(reverse ? FormattedCharSequence.backward(lastRunText, currentRunStyle, this.reverseCharModifier) : FormattedCharSequence.forward(lastRunText, currentRunStyle));
/*    */     } 
/*    */     
/* 53 */     return reverse ? Lists.reverse(parts) : parts;
/*    */   }
/*    */ 
/*    */   
/* 57 */   public static SubStringSource create(FormattedText text) { return create(text, ch -> ch, s -> s); }
/*    */ 
/*    */   
/*    */   public static SubStringSource create(FormattedText text, Int2IntFunction reverseCharModifier, UnaryOperator<String> shaper) {
/* 61 */     StringBuilder plainText = new StringBuilder();
/* 62 */     List<Style> charStyles = Lists.newArrayList();
/*    */     
/* 64 */     text.visit((style, contents) -> {
/* 65 */           StringDecomposer.iterateFormatted(contents, style, ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 73 */           return Optional.empty();
/*    */         }Style.EMPTY);
/*    */     
/* 76 */     return new SubStringSource((String)shaper.apply(plainText.toString()), charStyles, reverseCharModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SubStringSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */