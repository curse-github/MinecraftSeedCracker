/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class State
/*     */ {
/*     */   private final StringReader reader;
/*     */   private final ItemParser.Visitor visitor;
/*     */   
/*     */   private State(StringReader reader, ItemParser.Visitor visitor) {
/* 125 */     this.reader = reader;
/* 126 */     this.visitor = visitor;
/*     */   }
/*     */   
/*     */   public void parse() throws CommandSyntaxException {
/* 130 */     this.visitor.visitSuggestions(this::suggestItem);
/* 131 */     readItem();
/* 132 */     this.visitor.visitSuggestions(this::suggestStartComponents);
/* 133 */     if (this.reader.canRead() && this.reader.peek() == '[') {
/* 134 */       this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 135 */       readComponents();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readItem() throws CommandSyntaxException {
/* 140 */     int cursor = this.reader.getCursor();
/* 141 */     Identifier id = Identifier.read(this.reader);
/* 142 */     this.visitor.visitItem((Holder)ItemParser.this.items.get(ResourceKey.create(Registries.ITEM, id)).orElseThrow(() -> {
/* 143 */             this.reader.setCursor(cursor);
/* 144 */             return ItemParser.ERROR_UNKNOWN_ITEM.createWithContext(this.reader, id);
/*     */           }));
/*     */   }
/*     */   
/*     */   private void readComponents() throws CommandSyntaxException {
/* 149 */     this.reader.expect('[');
/*     */     
/* 151 */     this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
/*     */     
/* 153 */     ReferenceArraySet referenceArraySet = new ReferenceArraySet();
/*     */     
/* 155 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 156 */       this.reader.skipWhitespace();
/*     */       
/* 158 */       if (this.reader.canRead() && this.reader.peek() == '!') {
/* 159 */         this.reader.skip();
/* 160 */         this.visitor.visitSuggestions(this::suggestComponent);
/* 161 */         DataComponentType<?> componentType = readComponentType(this.reader);
/* 162 */         if (!referenceArraySet.add(componentType)) {
/* 163 */           throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType);
/*     */         }
/* 165 */         this.visitor.visitRemovedComponent(componentType);
/* 166 */         this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 167 */         this.reader.skipWhitespace();
/*     */       } else {
/* 169 */         DataComponentType<?> componentType = readComponentType(this.reader);
/* 170 */         if (!referenceArraySet.add(componentType)) {
/* 171 */           throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType);
/*     */         }
/*     */         
/* 174 */         this.visitor.visitSuggestions(this::suggestAssignment);
/*     */         
/* 176 */         this.reader.skipWhitespace();
/* 177 */         this.reader.expect('=');
/* 178 */         this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/*     */         
/* 180 */         this.reader.skipWhitespace();
/* 181 */         readComponent(ItemParser.this.tagParser, ItemParser.this.registryOps, componentType);
/* 182 */         this.reader.skipWhitespace();
/*     */       } 
/*     */       
/* 185 */       this.visitor.visitSuggestions(this::suggestNextOrEndComponents);
/*     */       
/* 187 */       if (!this.reader.canRead() || this.reader.peek() != ',') {
/*     */         break;
/*     */       }
/*     */       
/* 191 */       this.reader.skip();
/* 192 */       this.reader.skipWhitespace();
/* 193 */       this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
/*     */       
/* 195 */       if (!this.reader.canRead()) {
/* 196 */         throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(this.reader);
/*     */       }
/*     */     } 
/*     */     
/* 200 */     this.reader.expect(']');
/* 201 */     this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/*     */   }
/*     */   
/*     */   public static DataComponentType<?> readComponentType(StringReader reader) throws CommandSyntaxException {
/* 205 */     if (!reader.canRead()) {
/* 206 */       throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(reader);
/*     */     }
/*     */     
/* 209 */     int cursor = reader.getCursor();
/* 210 */     Identifier id = Identifier.read(reader);
/* 211 */     DataComponentType<?> component = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
/* 212 */     if (component == null || component.isTransient()) {
/* 213 */       reader.setCursor(cursor);
/* 214 */       throw ItemParser.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
/*     */     } 
/*     */     
/* 217 */     return component;
/*     */   }
/*     */   
/*     */   private <T, O> void readComponent(TagParser<O> tagParser, RegistryOps<O> registryOps, DataComponentType<T> componentType) throws CommandSyntaxException {
/* 221 */     int cursor = this.reader.getCursor();
/* 222 */     O tag = (O)tagParser.parseAsArgument(this.reader);
/* 223 */     DataResult<T> result = componentType.codecOrThrow().parse(registryOps, tag);
/* 224 */     this.visitor.visitComponent(componentType, result.getOrThrow(message -> {
/* 225 */             this.reader.setCursor(cursor);
/* 226 */             return ItemParser.ERROR_MALFORMED_COMPONENT.createWithContext(this.reader, componentType.toString(), message);
/*     */           }));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestStartComponents(SuggestionsBuilder builder) {
/* 231 */     if (builder.getRemaining().isEmpty()) {
/* 232 */       builder.suggest(String.valueOf('['));
/*     */     }
/* 234 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNextOrEndComponents(SuggestionsBuilder builder) {
/* 238 */     if (builder.getRemaining().isEmpty()) {
/* 239 */       builder.suggest(String.valueOf(','));
/* 240 */       builder.suggest(String.valueOf(']'));
/*     */     } 
/* 242 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestAssignment(SuggestionsBuilder builder) {
/* 246 */     if (builder.getRemaining().isEmpty()) {
/* 247 */       builder.suggest(String.valueOf('='));
/*     */     }
/* 249 */     return builder.buildFuture();
/*     */   }
/*     */ 
/*     */   
/* 253 */   private CompletableFuture<Suggestions> suggestItem(SuggestionsBuilder builder) { return SharedSuggestionProvider.suggestResource(ItemParser.this.items.listElementIds().map(ResourceKey::identifier), builder); }
/*     */ 
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestComponentAssignmentOrRemoval(SuggestionsBuilder builder) {
/* 257 */     builder.suggest(String.valueOf('!'));
/* 258 */     return suggestComponent(builder, String.valueOf('='));
/*     */   }
/*     */ 
/*     */   
/* 262 */   private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder) { return suggestComponent(builder, ""); }
/*     */ 
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder, String suffix) {
/* 266 */     String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 267 */     SharedSuggestionProvider.filterResources(BuiltInRegistries.DATA_COMPONENT_TYPE.entrySet(), contents, entry -> ((ResourceKey)entry.getKey()).identifier(), entry -> {
/* 268 */           DataComponentType<?> type = (DataComponentType)entry.getValue();
/* 269 */           if (type.codec() != null) {
/* 270 */             Identifier id = ((ResourceKey)entry.getKey()).identifier();
/* 271 */             builder.suggest(String.valueOf(id) + String.valueOf(id));
/*     */           } 
/*     */         });
/* 274 */     return builder.buildFuture();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemParser$State.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */