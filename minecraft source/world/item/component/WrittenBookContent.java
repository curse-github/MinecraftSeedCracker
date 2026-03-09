/*     */ package net.minecraft.world.item.component;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.network.Filterable;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class WrittenBookContent extends Record implements BookContent<Component, WrittenBookContent>, TooltipProvider {
/*     */   private final Filterable<String> title;
/*     */   private final String author;
/*     */   private final int generation;
/*     */   private final List<Filterable<Component>> pages;
/*     */   
/*  35 */   public Filterable<String> title() { return this.title; } private final boolean resolved; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/WrittenBookContent;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/WrittenBookContent; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/WrittenBookContent;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/WrittenBookContent; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/WrittenBookContent;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/WrittenBookContent;
/*  35 */     //   0	8	1	o	Ljava/lang/Object; } public String author() { return this.author; } public int generation() { return this.generation; } public List<Filterable<Component>> pages() { return this.pages; } public boolean resolved() { return this.resolved; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final WrittenBookContent EMPTY = new WrittenBookContent(Filterable.passThrough(""), "", 0, List.of(), true);
/*     */   
/*     */   public static final int PAGE_LENGTH = 32767;
/*     */   
/*     */   public static final int TITLE_LENGTH = 16;
/*     */   
/*     */   public static final int TITLE_MAX_LENGTH = 32;
/*     */   public static final int MAX_GENERATION = 3;
/*     */   public static final int MAX_CRAFTABLE_GENERATION = 2;
/*     */   
/*  52 */   private static Codec<Filterable<Component>> pageCodec(Codec<Component> contentCodec) { return Filterable.codec(contentCodec); }
/*     */ 
/*     */   
/*  55 */   public static final Codec<Component> CONTENT_CODEC = ComponentSerialization.flatRestrictedCodec(32767);
/*  56 */   public static final Codec<List<Filterable<Component>>> PAGES_CODEC = pagesCodec(CONTENT_CODEC);
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static Codec<List<Filterable<Component>>> pagesCodec(Codec<Component> contentCodec) { return pageCodec(contentCodec).listOf(); }
/*     */ 
/*     */   
/*  63 */   public static final Codec<WrittenBookContent> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  64 */         Filterable.codec(Codec.string(0, 32)).fieldOf("title").forGetter(WrittenBookContent::title), Codec.STRING
/*  65 */         .fieldOf("author").forGetter(WrittenBookContent::author), 
/*  66 */         ExtraCodecs.intRange(0, 3).optionalFieldOf("generation", Integer.valueOf(0)).forGetter(WrittenBookContent::generation), PAGES_CODEC
/*  67 */         .optionalFieldOf("pages", List.of()).forGetter(WrittenBookContent::pages), Codec.BOOL
/*  68 */         .optionalFieldOf("resolved", Boolean.valueOf(false)).forGetter(WrittenBookContent::resolved))
/*  69 */       .apply(i, WrittenBookContent::new));
/*     */   
/*  71 */   public static final StreamCodec<RegistryFriendlyByteBuf, WrittenBookContent> STREAM_CODEC = StreamCodec.composite(
/*  72 */       Filterable.streamCodec(ByteBufCodecs.stringUtf8(32)), WrittenBookContent::title, ByteBufCodecs.STRING_UTF8, WrittenBookContent::author, ByteBufCodecs.VAR_INT, WrittenBookContent::generation, 
/*     */ 
/*     */       
/*  75 */       Filterable.streamCodec(ComponentSerialization.STREAM_CODEC).apply(ByteBufCodecs.list()), WrittenBookContent::pages, ByteBufCodecs.BOOL, WrittenBookContent::resolved, WrittenBookContent::new);
/*     */ 
/*     */ 
/*     */   
/*     */   public WrittenBookContent(Filterable<String> title, String author, int generation, List<Filterable<Component>> pages, boolean resolved)
/*     */   {
/*  81 */     if (generation < 0 || generation > 3)
/*  82 */       throw new IllegalArgumentException("Generation was " + generation + ", but must be between 0 and 3"); 
/*     */     this.title = title;
/*     */     this.author = author;
/*     */     this.generation = generation;
/*     */     this.pages = pages;
/*  87 */     this.resolved = resolved; } public WrittenBookContent tryCraftCopy() { if (this.generation >= 2) {
/*  88 */       return null;
/*     */     }
/*  90 */     return new WrittenBookContent(this.title, this.author, this.generation + 1, this.pages, this.resolved); }
/*     */ 
/*     */   
/*     */   public static boolean resolveForItem(ItemStack itemStack, CommandSourceStack source, Player player) {
/*  94 */     WrittenBookContent content = (WrittenBookContent)itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  95 */     if (content != null && !content.resolved()) {
/*  96 */       WrittenBookContent resolvedContent = content.resolve(source, player);
/*  97 */       if (resolvedContent != null) {
/*  98 */         itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, resolvedContent);
/*  99 */         return true;
/*     */       } 
/* 101 */       itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, content.markResolved());
/*     */     } 
/*     */     
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public WrittenBookContent resolve(CommandSourceStack source, Player player) {
/* 108 */     if (this.resolved) {
/* 109 */       return null;
/*     */     }
/*     */     
/* 112 */     ImmutableList.Builder<Filterable<Component>> newPages = ImmutableList.builderWithExpectedSize(this.pages.size());
/* 113 */     for (Filterable<Component> page : this.pages) {
/* 114 */       Optional<Filterable<Component>> resolvedPage = resolvePage(source, player, page);
/* 115 */       if (resolvedPage.isEmpty()) {
/* 116 */         return null;
/*     */       }
/* 118 */       newPages.add((Filterable)resolvedPage.get());
/*     */     } 
/*     */     
/* 121 */     return new WrittenBookContent(this.title, this.author, this.generation, newPages.build(), true);
/*     */   }
/*     */ 
/*     */   
/* 125 */   public WrittenBookContent markResolved() { return new WrittenBookContent(this.title, this.author, this.generation, this.pages, true); }
/*     */ 
/*     */   
/*     */   private static Optional<Filterable<Component>> resolvePage(CommandSourceStack source, Player player, Filterable<Component> page) {
/* 129 */     return page.resolve(component -> {
/*     */           try {
/* 131 */             MutableComponent mutableComponent = ComponentUtils.updateForEntity(source, component, player, 0);
/* 132 */             if (isPageTooLarge(mutableComponent, source.registryAccess())) {
/* 133 */               return Optional.empty();
/*     */             }
/* 135 */             return Optional.of(mutableComponent);
/* 136 */           } catch (Exception ignored) {
/* 137 */             return Optional.of(component);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean isPageTooLarge(Component page, HolderLookup.Provider registries) {
/* 143 */     DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), page);
/* 144 */     return (json.isSuccess() && GsonHelper.encodesLongerThan((JsonElement)json.getOrThrow(), 32767));
/*     */   }
/*     */ 
/*     */   
/* 148 */   public List<Component> getPages(boolean filterEnabled) { return Lists.transform(this.pages, page -> (Component)page.get(filterEnabled)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public WrittenBookContent withReplacedPages(List<Filterable<Component>> newPages) { return new WrittenBookContent(this.title, this.author, this.generation, newPages, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 159 */     if (!StringUtil.isBlank(this.author)) {
/* 160 */       consumer.accept(Component.translatable("book.byAuthor", new Object[] { this.author }).withStyle(ChatFormatting.GRAY));
/*     */     }
/* 162 */     consumer.accept(Component.translatable("book.generation." + this.generation).withStyle(ChatFormatting.GRAY));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\WrittenBookContent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */