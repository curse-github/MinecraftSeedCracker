/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*     */ import net.minecraft.network.chat.contents.KeybindContents;
/*     */ import net.minecraft.network.chat.contents.NbtContents;
/*     */ import net.minecraft.network.chat.contents.ObjectContents;
/*     */ import net.minecraft.network.chat.contents.PlainTextContents;
/*     */ import net.minecraft.network.chat.contents.ScoreContents;
/*     */ import net.minecraft.network.chat.contents.SelectorContents;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Component
/*     */   extends Message, FormattedText
/*     */ {
/*  36 */   default String getString() { return super.getString(); }
/*     */ 
/*     */   
/*     */   default String getString(int limit) {
/*  40 */     StringBuilder builder = new StringBuilder();
/*  41 */     visit(contents -> {
/*  42 */           int remaining = limit - builder.length();
/*  43 */           if (remaining <= 0) {
/*  44 */             return STOP_ITERATION;
/*     */           }
/*  46 */           builder.append((contents.length() <= remaining) ? contents : contents.substring(0, remaining));
/*  47 */           return Optional.empty();
/*     */         });
/*  49 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default String tryCollapseToString() {
/*  55 */     ComponentContents componentContents = getContents(); if (componentContents instanceof PlainTextContents) { PlainTextContents text = (PlainTextContents)componentContents; if (getSiblings().isEmpty() && getStyle().isEmpty())
/*  56 */         return text.text();  }
/*     */     
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   default MutableComponent plainCopy() { return MutableComponent.create(getContents()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   default MutableComponent copy() { return new MutableComponent(getContents(), new ArrayList(getSiblings()), getStyle()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/*  85 */     Style selfStyle = getStyle().applyTo(parentStyle);
/*     */     
/*  87 */     Optional<T> selfResult = getContents().visit(output, selfStyle);
/*  88 */     if (selfResult.isPresent()) {
/*  89 */       return selfResult;
/*     */     }
/*     */     
/*  92 */     for (Component sibling : getSiblings()) {
/*  93 */       Optional<T> result = sibling.visit(output, selfStyle);
/*  94 */       if (result.isPresent()) {
/*  95 */         return result;
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 104 */     Optional<T> selfResult = getContents().visit(output);
/* 105 */     if (selfResult.isPresent()) {
/* 106 */       return selfResult;
/*     */     }
/*     */     
/* 109 */     for (Component sibling : getSiblings()) {
/* 110 */       Optional<T> result = sibling.visit(output);
/* 111 */       if (result.isPresent()) {
/* 112 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 116 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 120 */   default List<Component> toFlatList() { return toFlatList(Style.EMPTY); }
/*     */ 
/*     */   
/*     */   default List<Component> toFlatList(Style rootStyle) {
/* 124 */     List<Component> result = Lists.newArrayList();
/* 125 */     visit((style, contents) -> {
/* 126 */           if (!contents.isEmpty()) {
/* 127 */             result.add(literal(contents).withStyle(style));
/*     */           }
/* 129 */           return Optional.empty();
/*     */         }rootStyle);
/* 131 */     return result;
/*     */   }
/*     */   
/*     */   default boolean contains(Component other) {
/* 135 */     if (equals(other)) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     List<Component> flat = toFlatList();
/* 140 */     List<Component> otherFlat = other.toFlatList(getStyle());
/* 141 */     return (Collections.indexOfSubList(flat, otherFlat) != -1);
/*     */   }
/*     */ 
/*     */   
/* 145 */   static Component nullToEmpty(String text) { return (text != null) ? literal(text) : CommonComponents.EMPTY; }
/*     */ 
/*     */ 
/*     */   
/* 149 */   static MutableComponent literal(String text) { return MutableComponent.create(PlainTextContents.create(text)); }
/*     */ 
/*     */ 
/*     */   
/* 153 */   static MutableComponent translatable(String key) { return MutableComponent.create(new TranslatableContents(key, null, TranslatableContents.NO_ARGS)); }
/*     */ 
/*     */ 
/*     */   
/* 157 */   static MutableComponent translatable(String key, Object... args) { return MutableComponent.create(new TranslatableContents(key, null, args)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MutableComponent translatableEscape(String key, Object... args) {
/* 164 */     for (int i = 0; i < args.length; i++) {
/* 165 */       Object arg = args[i];
/* 166 */       if (!TranslatableContents.isAllowedPrimitiveArgument(arg) && !(arg instanceof Component)) {
/* 167 */         args[i] = String.valueOf(arg);
/*     */       }
/*     */     } 
/* 170 */     return translatable(key, args);
/*     */   }
/*     */ 
/*     */   
/* 174 */   static MutableComponent translatableWithFallback(String key, String fallback) { return MutableComponent.create(new TranslatableContents(key, fallback, TranslatableContents.NO_ARGS)); }
/*     */ 
/*     */ 
/*     */   
/* 178 */   static MutableComponent translatableWithFallback(String key, String fallback, Object... args) { return MutableComponent.create(new TranslatableContents(key, fallback, args)); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   static MutableComponent empty() { return MutableComponent.create(PlainTextContents.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/* 186 */   static MutableComponent keybind(String name) { return MutableComponent.create(new KeybindContents(name)); }
/*     */ 
/*     */ 
/*     */   
/* 190 */   static MutableComponent nbt(String nbtPath, boolean interpreting, Optional<Component> separator, DataSource dataSource) { return MutableComponent.create(new NbtContents(nbtPath, interpreting, separator, dataSource)); }
/*     */ 
/*     */ 
/*     */   
/* 194 */   static MutableComponent score(SelectorPattern pattern, String objective) { return MutableComponent.create(new ScoreContents(Either.left(pattern), objective)); }
/*     */ 
/*     */ 
/*     */   
/* 198 */   static MutableComponent score(String name, String objective) { return MutableComponent.create(new ScoreContents(Either.right(name), objective)); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   static MutableComponent selector(SelectorPattern pattern, Optional<Component> separator) { return MutableComponent.create(new SelectorContents(pattern, separator)); }
/*     */ 
/*     */ 
/*     */   
/* 206 */   static MutableComponent object(ObjectInfo info) { return MutableComponent.create(new ObjectContents(info)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   static Component translationArg(Date date) { return literal(date.toString()); }
/*     */   
/*     */   static Component translationArg(Message message) {
/*     */     Component component;
/* 215 */     return (message instanceof Component) ? component : (component = (Component)message).literal(message.getString());
/*     */   }
/*     */ 
/*     */   
/* 219 */   static Component translationArg(UUID uuid) { return literal(uuid.toString()); }
/*     */ 
/*     */ 
/*     */   
/* 223 */   static Component translationArg(Identifier id) { return literal(id.toString()); }
/*     */ 
/*     */ 
/*     */   
/* 227 */   static Component translationArg(ChunkPos chunkPos) { return literal(chunkPos.toString()); }
/*     */ 
/*     */ 
/*     */   
/* 231 */   static Component translationArg(URI uri) { return literal(uri.toString()); }
/*     */   
/*     */   Style getStyle();
/*     */   
/*     */   ComponentContents getContents();
/*     */   
/*     */   List<Component> getSiblings();
/*     */   
/*     */   FormattedCharSequence getVisualOrderText();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\Component.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */