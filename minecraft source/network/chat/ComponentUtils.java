/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ 
/*     */ public class ComponentUtils
/*     */ {
/*     */   public static final String DEFAULT_SEPARATOR_TEXT = ", ";
/*  22 */   public static final Component DEFAULT_SEPARATOR = Component.literal(", ").withStyle(ChatFormatting.GRAY);
/*  23 */   public static final Component DEFAULT_NO_STYLE_SEPARATOR = Component.literal(", ");
/*     */   
/*     */   @CheckReturnValue
/*     */   public static MutableComponent mergeStyles(MutableComponent component, Style style) {
/*  27 */     if (style.isEmpty()) {
/*  28 */       return component;
/*     */     }
/*     */     
/*  31 */     Style inner = component.getStyle();
/*  32 */     if (inner.isEmpty()) {
/*  33 */       return component.setStyle(style);
/*     */     }
/*     */     
/*  36 */     if (inner.equals(style)) {
/*  37 */       return component;
/*     */     }
/*     */     
/*  40 */     return component.setStyle(inner.applyTo(style));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public static Component mergeStyles(Component component, Style style) {
/*  45 */     if (style.isEmpty()) {
/*  46 */       return component;
/*     */     }
/*     */     
/*  49 */     Style inner = component.getStyle();
/*  50 */     if (inner.isEmpty()) {
/*  51 */       return component.copy().setStyle(style);
/*     */     }
/*     */     
/*  54 */     if (inner.equals(style)) {
/*  55 */       return component;
/*     */     }
/*     */     
/*  58 */     return component.copy().setStyle(inner.applyTo(style));
/*     */   }
/*     */ 
/*     */   
/*  62 */   public static Optional<MutableComponent> updateForEntity(CommandSourceStack source, Optional<Component> component, Entity entity, int recursionDepth) throws CommandSyntaxException { return component.isPresent() ? Optional.of(updateForEntity(source, (Component)component.get(), entity, recursionDepth)) : Optional.empty(); }
/*     */ 
/*     */   
/*     */   public static MutableComponent updateForEntity(CommandSourceStack source, Component component, Entity entity, int recursionDepth) throws CommandSyntaxException {
/*  66 */     if (recursionDepth > 100) {
/*  67 */       return component.copy();
/*     */     }
/*     */     
/*  70 */     MutableComponent result = component.getContents().resolve(source, entity, recursionDepth + 1);
/*     */     
/*  72 */     for (Component sibling : component.getSiblings()) {
/*  73 */       result.append(updateForEntity(source, sibling, entity, recursionDepth + 1));
/*     */     }
/*     */     
/*  76 */     return result.withStyle(resolveStyle(source, component.getStyle(), entity, recursionDepth));
/*     */   }
/*     */   
/*     */   private static Style resolveStyle(CommandSourceStack source, Style style, Entity entity, int recursionDepth) throws CommandSyntaxException {
/*  80 */     HoverEvent hoverEvent = style.getHoverEvent();
/*  81 */     if (hoverEvent instanceof HoverEvent.ShowText) { showText = (HoverEvent.ShowText)hoverEvent; try { Component component = showText.value(), text = component;
/*  82 */         HoverEvent resolved = new HoverEvent.ShowText(updateForEntity(source, text, entity, recursionDepth + 1));
/*  83 */         return style.withHoverEvent(resolved); }
/*     */       catch (Throwable showText) { throw new MatchException(showText.toString(), showText); }
/*     */        }
/*  86 */      return style;
/*     */   }
/*     */ 
/*     */   
/*  90 */   public static Component formatList(Collection<String> values) { return formatAndSortList(values, v -> Component.literal(v).withStyle(ChatFormatting.GREEN)); }
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<T>> Component formatAndSortList(Collection<T> values, Function<T, Component> formatter) {
/*  94 */     if (values.isEmpty())
/*  95 */       return CommonComponents.EMPTY; 
/*  96 */     if (values.size() == 1) {
/*  97 */       return (Component)formatter.apply((Comparable)values.iterator().next());
/*     */     }
/*     */     
/* 100 */     List<T> sorted = Lists.newArrayList(values);
/* 101 */     sorted.sort(Comparable::compareTo);
/* 102 */     return formatList(sorted, formatter);
/*     */   }
/*     */ 
/*     */   
/* 106 */   public static <T> Component formatList(Collection<? extends T> values, Function<T, Component> formatter) { return formatList(values, DEFAULT_SEPARATOR, formatter); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public static <T> MutableComponent formatList(Collection<? extends T> values, Optional<? extends Component> separator, Function<T, Component> formatter) { return formatList(values, (Component)DataFixUtils.orElse(separator, DEFAULT_SEPARATOR), formatter); }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public static Component formatList(Collection<? extends Component> values, Component separator) { return formatList(values, separator, Function.identity()); }
/*     */ 
/*     */   
/*     */   public static <T> MutableComponent formatList(Collection<? extends T> values, Component separator, Function<T, Component> formatter) {
/* 118 */     if (values.isEmpty())
/* 119 */       return Component.empty(); 
/* 120 */     if (values.size() == 1) {
/* 121 */       return ((Component)formatter.apply(values.iterator().next())).copy();
/*     */     }
/*     */     
/* 124 */     MutableComponent result = Component.empty();
/* 125 */     boolean first = true;
/* 126 */     for (T value : values) {
/* 127 */       if (!first) {
/* 128 */         result.append(separator);
/*     */       }
/* 130 */       result.append((Component)formatter.apply(value));
/* 131 */       first = false;
/*     */     } 
/*     */     
/* 134 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 138 */   public static MutableComponent wrapInSquareBrackets(Component inner) { return Component.translatable("chat.square_brackets", new Object[] { inner }); }
/*     */ 
/*     */   
/*     */   public static Component fromMessage(Message message) {
/* 142 */     if (message instanceof Component) return (Component)message;
/*     */ 
/*     */     
/* 145 */     return Component.literal(message.getString());
/*     */   }
/*     */   
/*     */   public static boolean isTranslationResolvable(Component component) {
/* 149 */     if (component != null) { ComponentContents componentContents = component.getContents(); if (componentContents instanceof TranslatableContents) { TranslatableContents translatable = (TranslatableContents)componentContents;
/* 150 */         String key = translatable.getKey();
/* 151 */         String fallback = translatable.getFallback();
/* 152 */         return (fallback != null || Language.getInstance().has(key)); }
/*     */        }
/* 154 */      return true;
/*     */   }
/*     */   
/*     */   public static MutableComponent copyOnClickText(String text) {
/* 158 */     return wrapInSquareBrackets(Component.literal(text).withStyle(s -> s
/* 159 */           .withColor(ChatFormatting.GREEN)
/* 160 */           .withClickEvent(new ClickEvent.CopyToClipboard(text))
/* 161 */           .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.copy.click")))
/* 162 */           .withInsertion(text)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ComponentUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */