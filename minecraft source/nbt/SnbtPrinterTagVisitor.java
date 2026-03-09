/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class SnbtPrinterTagVisitor
/*     */   implements TagVisitor
/*     */ {
/*  21 */   private static final Map<String, List<String>> KEY_ORDER = (Map)Util.make(Maps.newHashMap(), map -> {
/*  22 */         map.put("{}", Lists.newArrayList(new String[] { "DataVersion", "author", "size", "data", "entities", "palette", "palettes" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  31 */         map.put("{}.data.[].{}", Lists.newArrayList(new String[] { "pos", "state", "nbt" }));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  36 */         map.put("{}.entities.[].{}", Lists.newArrayList(new String[] { "blockPos", "pos" }));
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final Set<String> NO_INDENTATION = Sets.newHashSet(new String[] { "{}.size.[]", "{}.data.[].{}", "{}.palette.[].{}", "{}.entities.[].{}" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
/*     */   
/*  51 */   private static final String NAME_VALUE_SEPARATOR = String.valueOf(':');
/*  52 */   private static final String ELEMENT_SEPARATOR = String.valueOf(',');
/*     */   
/*     */   private static final String LIST_OPEN = "[";
/*     */   
/*     */   private static final String LIST_CLOSE = "]";
/*     */   
/*     */   private static final String LIST_TYPE_SEPARATOR = ";";
/*     */   private static final String ELEMENT_SPACING = " ";
/*     */   private static final String STRUCT_OPEN = "{";
/*     */   private static final String STRUCT_CLOSE = "}";
/*     */   private static final String NEWLINE = "\n";
/*     */   private final String indentation;
/*     */   private final int depth;
/*     */   private final List<String> path;
/*     */   private String result;
/*     */   
/*  68 */   public SnbtPrinterTagVisitor() { this("    ", 0, Lists.newArrayList()); }
/*     */   
/*     */   public SnbtPrinterTagVisitor(String indentation, int depth, List<String> path) {
/*     */     this.result = "";
/*  72 */     this.indentation = indentation;
/*  73 */     this.depth = depth;
/*  74 */     this.path = path;
/*     */   }
/*     */   
/*     */   public String visit(Tag tag) {
/*  78 */     tag.accept(this);
/*     */     
/*  80 */     return this.result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void visitString(StringTag tag) { this.result = StringTag.quoteAndEscape(tag.value()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitByte(ByteTag tag) {
/*  90 */     this.result = "" + tag.value() + "b";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitShort(ShortTag tag) {
/*  95 */     this.result = "" + tag.value() + "s";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void visitInt(IntTag tag) { this.result = String.valueOf(tag.value()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitLong(LongTag tag) {
/* 105 */     this.result = "" + tag.value() + "L";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitFloat(FloatTag tag) {
/* 110 */     this.result = "" + tag.value() + "f";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitDouble(DoubleTag tag) {
/* 115 */     this.result = "" + tag.value() + "d";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag tag) {
/* 120 */     StringBuilder builder = (new StringBuilder("[")).append("B").append(";");
/*     */     
/* 122 */     byte[] data = tag.getAsByteArray();
/* 123 */     for (int i = 0; i < data.length; i++) {
/* 124 */       builder.append(" ").append(data[i]).append("B");
/*     */       
/* 126 */       if (i != data.length - 1) {
/* 127 */         builder.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     builder.append("]");
/* 132 */     this.result = builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag tag) {
/* 137 */     StringBuilder builder = (new StringBuilder("[")).append("I").append(";");
/*     */     
/* 139 */     int[] data = tag.getAsIntArray();
/* 140 */     for (int i = 0; i < data.length; i++) {
/* 141 */       builder.append(" ").append(data[i]);
/* 142 */       if (i != data.length - 1) {
/* 143 */         builder.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 147 */     builder.append("]");
/* 148 */     this.result = builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag tag) {
/* 153 */     String type = "L";
/* 154 */     StringBuilder builder = (new StringBuilder("[")).append("L").append(";");
/*     */     
/* 156 */     long[] data = tag.getAsLongArray();
/* 157 */     for (int i = 0; i < data.length; i++) {
/* 158 */       builder.append(" ").append(data[i]).append("L");
/* 159 */       if (i != data.length - 1) {
/* 160 */         builder.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 164 */     builder.append("]");
/* 165 */     this.result = builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag tag) {
/* 170 */     if (tag.isEmpty()) {
/* 171 */       this.result = "[]";
/*     */       
/*     */       return;
/*     */     } 
/* 175 */     StringBuilder builder = new StringBuilder("[");
/* 176 */     pushPath("[]");
/*     */     
/* 178 */     String indentation = NO_INDENTATION.contains(pathString()) ? "" : this.indentation;
/* 179 */     if (!indentation.isEmpty()) {
/* 180 */       builder.append("\n");
/*     */     }
/*     */     
/* 183 */     for (int i = 0; i < tag.size(); i++) {
/* 184 */       builder.append(Strings.repeat(indentation, this.depth + 1));
/* 185 */       builder.append((new SnbtPrinterTagVisitor(indentation, this.depth + 1, this.path)).visit(tag.get(i)));
/* 186 */       if (i != tag.size() - 1) {
/* 187 */         builder.append(ELEMENT_SEPARATOR).append(indentation.isEmpty() ? " " : "\n");
/*     */       }
/*     */     } 
/* 190 */     if (!indentation.isEmpty()) {
/* 191 */       builder.append("\n").append(Strings.repeat(indentation, this.depth));
/*     */     }
/* 193 */     builder.append("]");
/*     */     
/* 195 */     this.result = builder.toString();
/* 196 */     popPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCompound(CompoundTag tag) {
/* 201 */     if (tag.isEmpty()) {
/* 202 */       this.result = "{}";
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 207 */     StringBuilder builder = new StringBuilder("{");
/* 208 */     pushPath("{}");
/*     */     
/* 210 */     String indentation = NO_INDENTATION.contains(pathString()) ? "" : this.indentation;
/* 211 */     if (!indentation.isEmpty()) {
/* 212 */       builder.append("\n");
/*     */     }
/*     */     
/* 215 */     Collection<String> keys = getKeys(tag);
/* 216 */     for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
/* 217 */       String key = (String)iterator.next();
/* 218 */       Tag value = tag.get(key);
/*     */       
/* 220 */       pushPath(key);
/* 221 */       builder.append(Strings.repeat(indentation, this.depth + 1))
/* 222 */         .append(handleEscapePretty(key))
/* 223 */         .append(NAME_VALUE_SEPARATOR)
/* 224 */         .append(" ")
/* 225 */         .append((new SnbtPrinterTagVisitor(indentation, this.depth + 1, this.path)).visit(value));
/*     */       
/* 227 */       popPath();
/*     */       
/* 229 */       if (iterator.hasNext()) {
/* 230 */         builder.append(ELEMENT_SEPARATOR).append(indentation.isEmpty() ? " " : "\n");
/*     */       }
/*     */     } 
/* 233 */     if (!indentation.isEmpty()) {
/* 234 */       builder.append("\n").append(Strings.repeat(indentation, this.depth));
/*     */     }
/* 236 */     builder.append("}");
/* 237 */     this.result = builder.toString();
/* 238 */     popPath();
/*     */   }
/*     */ 
/*     */   
/* 242 */   private void popPath() { this.path.remove(this.path.size() - 1); }
/*     */ 
/*     */ 
/*     */   
/* 246 */   private void pushPath(String e) { this.path.add(e); }
/*     */ 
/*     */   
/*     */   protected List<String> getKeys(CompoundTag tag) {
/* 250 */     Set<String> keys = Sets.newHashSet(tag.keySet());
/* 251 */     List<String> strings = Lists.newArrayList();
/*     */     
/* 253 */     List<String> order = (List)KEY_ORDER.get(pathString());
/* 254 */     if (order != null) {
/* 255 */       for (String key : order) {
/* 256 */         if (keys.remove(key)) {
/* 257 */           strings.add(key);
/*     */         }
/*     */       } 
/* 260 */       if (!keys.isEmpty()) {
/* 261 */         Objects.requireNonNull(strings); keys.stream().sorted().forEach(strings::add);
/*     */       } 
/*     */     } else {
/* 264 */       strings.addAll(keys);
/* 265 */       Collections.sort(strings);
/*     */     } 
/* 267 */     return strings;
/*     */   }
/*     */ 
/*     */   
/* 271 */   public String pathString() { return String.join(".", this.path); }
/*     */ 
/*     */   
/*     */   protected static String handleEscapePretty(String input) {
/* 275 */     if (SIMPLE_VALUE.matcher(input).matches()) {
/* 276 */       return input;
/*     */     }
/*     */     
/* 279 */     return StringTag.quoteAndEscape(input);
/*     */   }
/*     */   
/*     */   public void visitEnd(EndTag tag) {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtPrinterTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */