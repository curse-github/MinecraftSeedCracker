/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public final class MutableComponent
/*     */   implements Component
/*     */ {
/*     */   private final ComponentContents contents;
/*     */   private final List<Component> siblings;
/*     */   
/*     */   MutableComponent(ComponentContents contents, List<Component> siblings, Style style) {
/*  17 */     this.visualOrderText = FormattedCharSequence.EMPTY;
/*     */ 
/*     */ 
/*     */     
/*  21 */     this.contents = contents;
/*  22 */     this.siblings = siblings;
/*  23 */     this.style = style;
/*     */   }
/*     */   private Style style; private FormattedCharSequence visualOrderText; private Language decomposedWith;
/*     */   
/*  27 */   public static MutableComponent create(ComponentContents contents) { return new MutableComponent(contents, Lists.newArrayList(), Style.EMPTY); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   public ComponentContents getContents() { return this.contents; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public List<Component> getSiblings() { return this.siblings; }
/*     */ 
/*     */   
/*     */   public MutableComponent setStyle(Style style) {
/*  41 */     this.style = style;
/*  42 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public Style getStyle() { return this.style; }
/*     */ 
/*     */   
/*     */   public MutableComponent append(String text) {
/*  51 */     if (text.isEmpty()) {
/*  52 */       return this;
/*     */     }
/*  54 */     return append(Component.literal(text));
/*     */   }
/*     */   
/*     */   public MutableComponent append(Component component) {
/*  58 */     this.siblings.add(component);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(UnaryOperator<Style> updater) {
/*  63 */     setStyle((Style)updater.apply(getStyle()));
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(Style patch) {
/*  68 */     setStyle(patch.applyTo(getStyle()));
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(ChatFormatting... formats) {
/*  73 */     setStyle(getStyle().applyFormats(formats));
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(ChatFormatting format) {
/*  78 */     setStyle(getStyle().applyFormat(format));
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withColor(int color) {
/*  83 */     setStyle(getStyle().withColor(color));
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withoutShadow() {
/*  88 */     setStyle(getStyle().withoutShadow());
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormattedCharSequence getVisualOrderText() {
/*  94 */     Language currentLanguage = Language.getInstance();
/*  95 */     if (this.decomposedWith != currentLanguage) {
/*  96 */       this.visualOrderText = currentLanguage.getVisualOrder(this);
/*  97 */       this.decomposedWith = currentLanguage;
/*     */     } 
/*  99 */     return this.visualOrderText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof net/minecraft/network/chat/MutableComponent
/*     */     //   11: ifeq -> 69
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/MutableComponent
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield contents : Lnet/minecraft/network/chat/ComponentContents;
/*     */     //   23: aload_2
/*     */     //   24: getfield contents : Lnet/minecraft/network/chat/ComponentContents;
/*     */     //   27: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 69
/*     */     //   35: aload_0
/*     */     //   36: getfield style : Lnet/minecraft/network/chat/Style;
/*     */     //   39: aload_2
/*     */     //   40: getfield style : Lnet/minecraft/network/chat/Style;
/*     */     //   43: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   46: ifeq -> 69
/*     */     //   49: aload_0
/*     */     //   50: getfield siblings : Ljava/util/List;
/*     */     //   53: aload_2
/*     */     //   54: getfield siblings : Ljava/util/List;
/*     */     //   57: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   62: ifeq -> 69
/*     */     //   65: iconst_1
/*     */     //   66: goto -> 70
/*     */     //   69: iconst_0
/*     */     //   70: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     //   #105	-> 5
/*     */     //   #111	-> 7
/*     */     //   #108	-> 14
/*     */     //   #109	-> 27
/*     */     //   #110	-> 43
/*     */     //   #111	-> 57
/*     */     //   #108	-> 70
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	50	2	that	Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   0	71	0	this	Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   0	71	1	o	Ljava/lang/Object; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     result = 1;
/* 117 */     result = 31 * result + this.contents.hashCode();
/* 118 */     result = 31 * result + this.style.hashCode();
/* 119 */     return 31 * result + this.siblings.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     StringBuilder result = new StringBuilder(this.contents.toString());
/* 126 */     boolean hasStyle = !this.style.isEmpty();
/* 127 */     boolean hasSiblings = !this.siblings.isEmpty();
/* 128 */     if (hasStyle || hasSiblings) {
/* 129 */       result.append('[');
/* 130 */       if (hasStyle) {
/* 131 */         result.append("style=");
/* 132 */         result.append(this.style);
/*     */       } 
/* 134 */       if (hasStyle && hasSiblings) {
/* 135 */         result.append(", ");
/*     */       }
/* 137 */       if (hasSiblings) {
/* 138 */         result.append("siblings=");
/* 139 */         result.append(this.siblings);
/*     */       } 
/* 141 */       result.append(']');
/*     */     } 
/* 143 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\MutableComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */