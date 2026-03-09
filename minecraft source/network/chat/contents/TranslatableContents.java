/*     */ package net.minecraft.network.chat.contents;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentContents;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TranslatableContents
/*     */   implements ComponentContents {
/*  32 */   public static final Object[] NO_ARGS = new Object[0];
/*     */   
/*  34 */   private static final Codec<Object> PRIMITIVE_ARG_CODEC = ExtraCodecs.JAVA.validate(TranslatableContents::filterAllowedArguments);
/*     */   
/*     */   private static DataResult<Object> filterAllowedArguments(Object result) {
/*  37 */     if (!isAllowedPrimitiveArgument(result)) {
/*  38 */       return DataResult.error(() -> "This value needs to be parsed as component");
/*     */     }
/*  40 */     return DataResult.success(result);
/*     */   }
/*     */ 
/*     */   
/*  44 */   public static boolean isAllowedPrimitiveArgument(Object object) { return (object instanceof Number || object instanceof Boolean || object instanceof String); }
/*     */ 
/*     */   
/*  47 */   private static final Codec<Object> ARG_CODEC = Codec.either(PRIMITIVE_ARG_CODEC, ComponentSerialization.CODEC)
/*  48 */     .xmap(e -> 
/*  49 */       e.map((), ()), o -> {
/*     */ 
/*     */         
/*  52 */         Component c = (Component)o; return (o instanceof Component) ? Either.right(c) : Either.left(o);
/*     */       });
/*     */   
/*  55 */   public static final MapCodec<TranslatableContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/*  56 */         .fieldOf("translate").forGetter(()), Codec.STRING
/*  57 */         .lenientOptionalFieldOf("fallback").forGetter(()), ARG_CODEC
/*  58 */         .listOf().optionalFieldOf("with").forGetter(()))
/*  59 */       .apply(i, TranslatableContents::create));
/*     */ 
/*     */   
/*  62 */   private static Optional<List<Object>> adjustArgs(Object[] args) { return (args.length == 0) ? Optional.empty() : Optional.of(Arrays.asList(args)); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static Object[] adjustArgs(Optional<List<Object>> args) { return (Object[])args.map(a -> a.isEmpty() ? NO_ARGS : a.toArray()).orElse(NO_ARGS); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static TranslatableContents create(String key, Optional<String> fallback, Optional<List<Object>> args) { return new TranslatableContents(key, (String)fallback.orElse(null), adjustArgs(args)); }
/*     */ 
/*     */   
/*  73 */   private static final FormattedText TEXT_PERCENT = FormattedText.of("%");
/*  74 */   private static final FormattedText TEXT_NULL = FormattedText.of("null");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final String fallback;
/*     */   
/*     */   private final Object[] args;
/*     */   private Language decomposedWith;
/*     */   private List<FormattedText> decomposedParts;
/*  83 */   private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
/*     */   public TranslatableContents(String key, String fallback, Object[] args) {
/*     */     this.decomposedParts = ImmutableList.of();
/*  86 */     this.key = key;
/*  87 */     this.fallback = fallback;
/*  88 */     this.args = args;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public MapCodec<TranslatableContents> codec() { return MAP_CODEC; }
/*     */ 
/*     */   
/*     */   private void decompose() {
/*  97 */     Language currentLanguage = Language.getInstance();
/*  98 */     if (currentLanguage == this.decomposedWith) {
/*     */       return;
/*     */     }
/* 101 */     this.decomposedWith = currentLanguage;
/*     */ 
/*     */     
/* 104 */     String format = (this.fallback != null) ? currentLanguage.getOrDefault(this.key, this.fallback) : currentLanguage.getOrDefault(this.key);
/*     */     try {
/* 106 */       ImmutableList.Builder<FormattedText> parts = ImmutableList.builder();
/* 107 */       Objects.requireNonNull(parts); decomposeTemplate(format, parts::add);
/* 108 */       this.decomposedParts = parts.build();
/* 109 */     } catch (TranslatableFormatException e) {
/* 110 */       this.decomposedParts = ImmutableList.of(FormattedText.of(format));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decomposeTemplate(String template, Consumer<FormattedText> decomposedParts) {
/* 115 */     Matcher matcher = FORMAT_PATTERN.matcher(template);
/*     */     
/*     */     try {
/* 118 */       int replacementIndex = 0;
/* 119 */       int current = 0;
/*     */       
/* 121 */       while (matcher.find(current)) {
/* 122 */         int start = matcher.start();
/* 123 */         int end = matcher.end();
/*     */         
/* 125 */         if (start > current) {
/* 126 */           String prefix = template.substring(current, start);
/* 127 */           if (prefix.indexOf('%') != -1) {
/* 128 */             throw new IllegalArgumentException();
/*     */           }
/* 130 */           decomposedParts.accept(FormattedText.of(prefix));
/*     */         } 
/*     */         
/* 133 */         String formatType = matcher.group(2);
/* 134 */         String formatString = template.substring(start, end);
/*     */ 
/*     */         
/* 137 */         if ("%".equals(formatType) && "%%".equals(formatString)) {
/* 138 */           decomposedParts.accept(TEXT_PERCENT);
/* 139 */         } else if ("s".equals(formatType)) {
/* 140 */           String possiblePositionIndex = matcher.group(1);
/* 141 */           int index = (possiblePositionIndex != null) ? (Integer.parseInt(possiblePositionIndex) - 1) : replacementIndex++;
/* 142 */           decomposedParts.accept(getArgument(index));
/*     */         } else {
/* 144 */           throw new TranslatableFormatException(this, "Unsupported format: '" + formatString + "'");
/*     */         } 
/*     */         
/* 147 */         current = end;
/*     */       } 
/*     */       
/* 150 */       if (current < template.length()) {
/* 151 */         String tail = template.substring(current);
/* 152 */         if (tail.indexOf('%') != -1) {
/* 153 */           throw new IllegalArgumentException();
/*     */         }
/* 155 */         decomposedParts.accept(FormattedText.of(tail));
/*     */       } 
/* 157 */     } catch (IllegalArgumentException e) {
/* 158 */       throw new TranslatableFormatException(this, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FormattedText getArgument(int index) {
/* 163 */     if (index < 0 || index >= this.args.length) {
/* 164 */       throw new TranslatableFormatException(this, index);
/*     */     }
/*     */     
/* 167 */     Object arg = this.args[index];
/*     */     
/* 169 */     if (arg instanceof Component) return (Component)arg;
/*     */ 
/*     */     
/* 172 */     return (arg == null) ? TEXT_NULL : FormattedText.of(arg.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 178 */     decompose();
/*     */     
/* 180 */     for (FormattedText part : this.decomposedParts) {
/* 181 */       Optional<T> result = part.visit(output, currentStyle);
/* 182 */       if (result.isPresent()) {
/* 183 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 192 */     decompose();
/*     */     
/* 194 */     for (FormattedText part : this.decomposedParts) {
/* 195 */       Optional<T> result = part.visit(output);
/* 196 */       if (result.isPresent()) {
/* 197 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException {
/* 206 */     Object[] argsCopy = new Object[this.args.length];
/*     */     
/* 208 */     for (int i = 0; i < argsCopy.length; i++) {
/* 209 */       Object param = this.args[i];
/* 210 */       if (param instanceof Component) { Component component = (Component)param;
/* 211 */         argsCopy[i] = ComponentUtils.updateForEntity(source, component, entity, recursionDepth); }
/*     */       else
/* 213 */       { argsCopy[i] = param; }
/*     */     
/*     */     } 
/* 216 */     return MutableComponent.create(new TranslatableContents(this.key, this.fallback, argsCopy));
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
/*     */     //   8: instanceof net/minecraft/network/chat/contents/TranslatableContents
/*     */     //   11: ifeq -> 65
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/contents/TranslatableContents
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield key : Ljava/lang/String;
/*     */     //   23: aload_2
/*     */     //   24: getfield key : Ljava/lang/String;
/*     */     //   27: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   30: ifeq -> 65
/*     */     //   33: aload_0
/*     */     //   34: getfield fallback : Ljava/lang/String;
/*     */     //   37: aload_2
/*     */     //   38: getfield fallback : Ljava/lang/String;
/*     */     //   41: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   44: ifeq -> 65
/*     */     //   47: aload_0
/*     */     //   48: getfield args : [Ljava/lang/Object;
/*     */     //   51: aload_2
/*     */     //   52: getfield args : [Ljava/lang/Object;
/*     */     //   55: invokestatic equals : ([Ljava/lang/Object;[Ljava/lang/Object;)Z
/*     */     //   58: ifeq -> 65
/*     */     //   61: iconst_1
/*     */     //   62: goto -> 66
/*     */     //   65: iconst_0
/*     */     //   66: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #221	-> 0
/*     */     //   #222	-> 5
/*     */     //   #228	-> 7
/*     */     //   #225	-> 14
/*     */     //   #226	-> 27
/*     */     //   #227	-> 41
/*     */     //   #228	-> 55
/*     */     //   #225	-> 66
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	46	2	that	Lnet/minecraft/network/chat/contents/TranslatableContents;
/*     */     //   0	67	0	this	Lnet/minecraft/network/chat/contents/TranslatableContents;
/*     */     //   0	67	1	o	Ljava/lang/Object; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     result = Objects.hashCode(this.key);
/* 234 */     result = 31 * result + Objects.hashCode(this.fallback);
/* 235 */     return 31 * result + Arrays.hashCode(this.args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   public String toString() { return "translation{key='" + this.key + "'" + (
/*     */       
/* 243 */       (this.fallback != null) ? (", fallback='" + this.fallback + "'") : "") + ", args=" + 
/* 244 */       Arrays.toString(this.args) + "}"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   public String getKey() { return this.key; }
/*     */ 
/*     */ 
/*     */   
/* 253 */   public String getFallback() { return this.fallback; }
/*     */ 
/*     */ 
/*     */   
/* 257 */   public Object[] getArgs() { return this.args; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\TranslatableContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */