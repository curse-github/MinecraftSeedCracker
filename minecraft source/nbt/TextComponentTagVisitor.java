/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextComponentTagVisitor
/*     */   implements TagVisitor {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int INLINE_LIST_THRESHOLD = 8;
/*     */   
/*     */   private static final int MAX_DEPTH = 64;
/*     */   private static final int MAX_LENGTH = 128;
/*  24 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_KEY = ChatFormatting.AQUA;
/*  25 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_STRING = ChatFormatting.GREEN;
/*  26 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER = ChatFormatting.GOLD;
/*  27 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER_TYPE = ChatFormatting.RED;
/*     */   
/*  29 */   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
/*     */   
/*     */   private static final String LIST_OPEN = "[";
/*     */   private static final String LIST_CLOSE = "]";
/*     */   private static final String LIST_TYPE_SEPARATOR = ";";
/*     */   private static final String ELEMENT_SPACING = " ";
/*     */   private static final String STRUCT_OPEN = "{";
/*     */   private static final String STRUCT_CLOSE = "}";
/*     */   private static final String NEWLINE = "\n";
/*     */   private static final String NAME_VALUE_SEPARATOR = ": ";
/*  39 */   private static final String ELEMENT_SEPARATOR = String.valueOf(',');
/*  40 */   private static final String WRAPPED_ELEMENT_SEPARATOR = ELEMENT_SEPARATOR + "\n";
/*  41 */   private static final String SPACED_ELEMENT_SEPARATOR = ELEMENT_SEPARATOR + " ";
/*  42 */   private static final Component FOLDED = Component.literal("<...>").withStyle(ChatFormatting.GRAY);
/*  43 */   private static final Component BYTE_TYPE = Component.literal("b").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  44 */   private static final Component SHORT_TYPE = Component.literal("s").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  45 */   private static final Component INT_TYPE = Component.literal("I").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  46 */   private static final Component LONG_TYPE = Component.literal("L").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  47 */   private static final Component FLOAT_TYPE = Component.literal("f").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  48 */   private static final Component DOUBLE_TYPE = Component.literal("d").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  49 */   private static final Component BYTE_ARRAY_TYPE = Component.literal("B").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*     */   
/*     */   private final String indentation;
/*     */   private int indentDepth;
/*     */   
/*     */   public TextComponentTagVisitor(String indentation) {
/*  55 */     this.result = Component.empty();
/*     */ 
/*     */     
/*  58 */     this.indentation = indentation;
/*     */   }
/*     */   private int depth; private final MutableComponent result;
/*     */   public Component visit(Tag tag) {
/*  62 */     tag.accept(this);
/*     */     
/*  64 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitString(StringTag tag) {
/*  69 */     String quoted = StringTag.quoteAndEscape(tag.value());
/*  70 */     String quote = quoted.substring(0, 1);
/*  71 */     MutableComponent mutableComponent = Component.literal(quoted.substring(1, quoted.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_STRING);
/*  72 */     this.result.append(quote).append(mutableComponent).append(quote);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public void visitByte(ByteTag tag) { this.result.append(Component.literal(String.valueOf(tag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(BYTE_TYPE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public void visitShort(ShortTag tag) { this.result.append(Component.literal(String.valueOf(tag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(SHORT_TYPE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public void visitInt(IntTag tag) { this.result.append(Component.literal(String.valueOf(tag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void visitLong(LongTag tag) { this.result.append(Component.literal(String.valueOf(tag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(LONG_TYPE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public void visitFloat(FloatTag tag) { this.result.append(Component.literal(String.valueOf(tag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(FLOAT_TYPE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public void visitDouble(DoubleTag tag) { this.result.append(Component.literal(String.valueOf(tag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(DOUBLE_TYPE); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag tag) {
/* 107 */     this.result.append("[").append(BYTE_ARRAY_TYPE).append(";");
/*     */     
/* 109 */     byte[] data = tag.getAsByteArray();
/* 110 */     for (int i = 0; i < data.length && i < 128; i++) {
/* 111 */       MutableComponent line = Component.literal(String.valueOf(data[i])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */       
/* 113 */       this.result.append(" ").append(line).append(BYTE_ARRAY_TYPE);
/*     */       
/* 115 */       if (i != data.length - 1) {
/* 116 */         this.result.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 120 */     if (data.length > 128) {
/* 121 */       this.result.append(FOLDED);
/*     */     }
/*     */     
/* 124 */     this.result.append("]");
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag tag) {
/* 129 */     this.result.append("[").append(INT_TYPE).append(";");
/*     */     
/* 131 */     int[] data = tag.getAsIntArray();
/* 132 */     for (int i = 0; i < data.length && i < 128; i++) {
/* 133 */       this.result.append(" ").append(Component.literal(String.valueOf(data[i])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
/* 134 */       if (i != data.length - 1) {
/* 135 */         this.result.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 139 */     if (data.length > 128) {
/* 140 */       this.result.append(FOLDED);
/*     */     }
/*     */     
/* 143 */     this.result.append("]");
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag tag) {
/* 148 */     this.result.append("[").append(LONG_TYPE).append(";");
/*     */     
/* 150 */     long[] data = tag.getAsLongArray();
/* 151 */     for (int i = 0; i < data.length && i < 128; i++) {
/* 152 */       MutableComponent mutableComponent = Component.literal(String.valueOf(data[i])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/* 153 */       this.result.append(" ").append(mutableComponent).append(LONG_TYPE);
/* 154 */       if (i != data.length - 1) {
/* 155 */         this.result.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 159 */     if (data.length > 128) {
/* 160 */       this.result.append(FOLDED);
/*     */     }
/*     */     
/* 163 */     this.result.append("]");
/*     */   }
/*     */   
/*     */   private static boolean shouldWrapListElements(ListTag list) {
/* 167 */     if (list.size() >= 8) {
/* 168 */       return false;
/*     */     }
/* 170 */     for (Tag element : list) {
/* 171 */       if (!(element instanceof NumericTag)) {
/* 172 */         return true;
/*     */       }
/*     */     } 
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag tag) {
/* 180 */     if (tag.isEmpty()) {
/* 181 */       this.result.append("[]"); return;
/*     */     } 
/* 183 */     if (this.depth >= 64) {
/* 184 */       this.result.append("[").append(FOLDED).append("]");
/*     */       
/*     */       return;
/*     */     } 
/* 188 */     if (!shouldWrapListElements(tag)) {
/* 189 */       this.result.append("[");
/* 190 */       for (int i = 0; i < tag.size(); i++) {
/* 191 */         if (i != 0) {
/* 192 */           this.result.append(SPACED_ELEMENT_SEPARATOR);
/*     */         }
/* 194 */         appendSubTag(tag.get(i), false);
/*     */       } 
/* 196 */       this.result.append("]");
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     this.result.append("[");
/* 201 */     if (!this.indentation.isEmpty()) {
/* 202 */       this.result.append("\n");
/*     */     }
/* 204 */     String entryIndent = Strings.repeat(this.indentation, this.indentDepth + 1);
/* 205 */     for (int i = 0; i < tag.size() && i < 128; i++) {
/* 206 */       this.result.append(entryIndent);
/* 207 */       appendSubTag(tag.get(i), true);
/* 208 */       if (i != tag.size() - 1) {
/* 209 */         this.result.append(this.indentation.isEmpty() ? SPACED_ELEMENT_SEPARATOR : WRAPPED_ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/* 212 */     if (tag.size() > 128) {
/* 213 */       this.result.append(entryIndent).append(FOLDED);
/*     */     }
/* 215 */     if (!this.indentation.isEmpty()) {
/* 216 */       this.result.append("\n" + Strings.repeat(this.indentation, this.indentDepth));
/*     */     }
/* 218 */     this.result.append("]");
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCompound(CompoundTag tag) {
/* 223 */     if (tag.isEmpty()) {
/* 224 */       this.result.append("{}"); return;
/*     */     } 
/* 226 */     if (this.depth >= 64) {
/* 227 */       this.result.append("{").append(FOLDED).append("}");
/*     */       
/*     */       return;
/*     */     } 
/* 231 */     this.result.append("{");
/*     */     
/* 233 */     Collection<String> strings = tag.keySet();
/* 234 */     if (LOGGER.isDebugEnabled()) {
/* 235 */       List<String> keys = Lists.newArrayList(tag.keySet());
/* 236 */       Collections.sort(keys);
/* 237 */       strings = keys;
/*     */     } 
/*     */     
/* 240 */     if (!this.indentation.isEmpty()) {
/* 241 */       this.result.append("\n");
/*     */     }
/*     */     
/* 244 */     String entryIndent = Strings.repeat(this.indentation, this.indentDepth + 1);
/* 245 */     for (Iterator<String> iterator = strings.iterator(); iterator.hasNext(); ) {
/* 246 */       String key = (String)iterator.next();
/* 247 */       this.result.append(entryIndent)
/* 248 */         .append(handleEscapePretty(key))
/* 249 */         .append(": ");
/* 250 */       appendSubTag(tag.get(key), true);
/*     */       
/* 252 */       if (iterator.hasNext()) {
/* 253 */         this.result.append(this.indentation.isEmpty() ? SPACED_ELEMENT_SEPARATOR : WRAPPED_ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/* 256 */     if (!this.indentation.isEmpty()) {
/* 257 */       this.result.append("\n" + Strings.repeat(this.indentation, this.indentDepth));
/*     */     }
/* 259 */     this.result.append("}");
/*     */   }
/*     */   
/*     */   private void appendSubTag(Tag tag, boolean indent) {
/* 263 */     if (indent) {
/* 264 */       this.indentDepth++;
/*     */     }
/* 266 */     this.depth++;
/*     */     try {
/* 268 */       tag.accept(this);
/*     */     } finally {
/* 270 */       if (indent) {
/* 271 */         this.indentDepth--;
/*     */       }
/* 273 */       this.depth--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Component handleEscapePretty(String input) {
/* 278 */     if (SIMPLE_VALUE.matcher(input).matches()) {
/* 279 */       return Component.literal(input).withStyle(SYNTAX_HIGHLIGHTING_KEY);
/*     */     }
/*     */     
/* 282 */     String quoted = StringTag.quoteAndEscape(input);
/* 283 */     String quote = quoted.substring(0, 1);
/* 284 */     MutableComponent mutableComponent = Component.literal(quoted.substring(1, quoted.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_KEY);
/* 285 */     return Component.literal(quote).append(mutableComponent).append(quote);
/*     */   }
/*     */   
/*     */   public void visitEnd(EndTag tag) {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\TextComponentTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */