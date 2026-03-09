/*     */ package net.minecraft.stats;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRecipeBookRemovePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerRecipeBook extends RecipeBook {
/*     */   public static final String RECIPE_BOOK_TAG = "recipeBook";
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger(); private final DisplayResolver displayResolver; @VisibleForTesting
/*     */   protected final Set<ResourceKey<Recipe<?>>> known; @VisibleForTesting
/*     */   protected final Set<ResourceKey<Recipe<?>>> highlight;
/*     */   public ServerRecipeBook(DisplayResolver displayResolver) {
/*  34 */     this
/*  35 */       .known = Sets.newIdentityHashSet();
/*  36 */     this
/*  37 */       .highlight = Sets.newIdentityHashSet();
/*     */ 
/*     */     
/*  40 */     this.displayResolver = displayResolver;
/*     */   }
/*     */ 
/*     */   
/*  44 */   public void add(ResourceKey<Recipe<?>> id) { this.known.add(id); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public boolean contains(ResourceKey<Recipe<?>> id) { return this.known.contains(id); }
/*     */ 
/*     */   
/*     */   public void remove(ResourceKey<Recipe<?>> id) {
/*  52 */     this.known.remove(id);
/*  53 */     this.highlight.remove(id);
/*     */   }
/*     */ 
/*     */   
/*  57 */   public void removeHighlight(ResourceKey<Recipe<?>> id) { this.highlight.remove(id); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   private void addHighlight(ResourceKey<Recipe<?>> id) { this.highlight.add(id); }
/*     */ 
/*     */   
/*     */   public int addRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player) {
/*  65 */     List<ClientboundRecipeBookAddPacket.Entry> recipesToAdd = new ArrayList<ClientboundRecipeBookAddPacket.Entry>();
/*     */     
/*  67 */     for (RecipeHolder<?> recipe : recipes) {
/*  68 */       ResourceKey<Recipe<?>> id = recipe.id();
/*  69 */       if (!this.known.contains(id) && !recipe.value().isSpecial()) {
/*  70 */         add(id);
/*  71 */         addHighlight(id);
/*  72 */         this.displayResolver.displaysForRecipe(id, display -> recipesToAdd.add(new ClientboundRecipeBookAddPacket.Entry(display, recipe.value().showNotification(), true)));
/*  73 */         CriteriaTriggers.RECIPE_UNLOCKED.trigger(player, recipe);
/*     */       } 
/*     */     } 
/*     */     
/*  77 */     if (!recipesToAdd.isEmpty()) {
/*  78 */       player.connection.send(new ClientboundRecipeBookAddPacket(recipesToAdd, false));
/*     */     }
/*  80 */     return recipesToAdd.size();
/*     */   }
/*     */   
/*     */   public int removeRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player) {
/*  84 */     List<RecipeDisplayId> recipesToRemove = Lists.newArrayList();
/*     */     
/*  86 */     for (RecipeHolder<?> recipe : recipes) {
/*  87 */       ResourceKey<Recipe<?>> id = recipe.id();
/*  88 */       if (this.known.contains(id)) {
/*  89 */         remove(id);
/*  90 */         this.displayResolver.displaysForRecipe(id, display -> recipesToRemove.add(display.id()));
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (!recipesToRemove.isEmpty()) {
/*  95 */       player.connection.send(new ClientboundRecipeBookRemovePacket(recipesToRemove));
/*     */     }
/*  97 */     return recipesToRemove.size();
/*     */   }
/*     */   
/*     */   private void loadRecipes(List<ResourceKey<Recipe<?>>> recipes, Consumer<ResourceKey<Recipe<?>>> recipeAddingMethod, Predicate<ResourceKey<Recipe<?>>> validator) {
/* 101 */     for (ResourceKey<Recipe<?>> recipe : recipes) {
/*     */       
/* 103 */       if (!validator.test(recipe)) {
/* 104 */         LOGGER.error("Tried to load unrecognized recipe: {} removed now.", recipe);
/*     */         continue;
/*     */       } 
/* 107 */       recipeAddingMethod.accept(recipe);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendInitialRecipeBook(ServerPlayer player) {
/* 112 */     player.connection.send(new ClientboundRecipeBookSettingsPacket(getBookSettings().copy()));
/*     */     
/* 114 */     List<ClientboundRecipeBookAddPacket.Entry> recipesToSend = new ArrayList<ClientboundRecipeBookAddPacket.Entry>(this.known.size());
/* 115 */     for (Iterator iterator = this.known.iterator(); iterator.hasNext(); ) { ResourceKey<Recipe<?>> id = (ResourceKey)iterator.next();
/* 116 */       this.displayResolver.displaysForRecipe(id, r -> recipesToSend.add(new ClientboundRecipeBookAddPacket.Entry(r, false, this.highlight.contains(id)))); }
/*     */     
/* 118 */     player.connection.send(new ClientboundRecipeBookAddPacket(recipesToSend, true));
/*     */   }
/*     */ 
/*     */   
/* 122 */   public void copyOverData(ServerRecipeBook bookToCopy) { apply(bookToCopy.pack()); }
/*     */ 
/*     */   
/*     */   public Packed pack() {
/* 126 */     return new Packed(this.bookSettings
/* 127 */         .copy(), 
/* 128 */         List.copyOf(this.known), 
/* 129 */         List.copyOf(this.highlight));
/*     */   }
/*     */ 
/*     */   
/*     */   private void apply(Packed packed) {
/* 134 */     this.known.clear();
/* 135 */     this.highlight.clear();
/*     */     
/* 137 */     this.bookSettings.replaceFrom(packed.settings);
/*     */     
/* 139 */     this.known.addAll(packed.known);
/* 140 */     this.highlight.addAll(packed.highlight);
/*     */   }
/*     */   
/*     */   public void loadUntrusted(Packed packed, Predicate<ResourceKey<Recipe<?>>> validator) {
/* 144 */     this.bookSettings.replaceFrom(packed.settings);
/* 145 */     Objects.requireNonNull(this.known); loadRecipes(packed.known, this.known::add, validator);
/* 146 */     Objects.requireNonNull(this.highlight); loadRecipes(packed.highlight, this.highlight::add, validator);
/*     */   }
/*     */   @FunctionalInterface
/*     */   public static interface DisplayResolver {
/*     */     void displaysForRecipe(ResourceKey<Recipe<?>> param1ResourceKey, Consumer<RecipeDisplayEntry> param1Consumer); }
/*     */   public static final class Packed extends Record { private final RecipeBookSettings settings; private final List<ResourceKey<Recipe<?>>> known;
/*     */     private final List<ResourceKey<Recipe<?>>> highlight;
/*     */     
/* 154 */     public Packed(RecipeBookSettings settings, List<ResourceKey<Recipe<?>>> known, List<ResourceKey<Recipe<?>>> highlight) { this.settings = settings; this.known = known; this.highlight = highlight; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/stats/ServerRecipeBook$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 154 */       //   0	7	0	this	Lnet/minecraft/stats/ServerRecipeBook$Packed; } public RecipeBookSettings settings() { return this.settings; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/stats/ServerRecipeBook$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/stats/ServerRecipeBook$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/stats/ServerRecipeBook$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/stats/ServerRecipeBook$Packed;
/* 154 */       //   0	8	1	o	Ljava/lang/Object; } public List<ResourceKey<Recipe<?>>> known() { return this.known; } public List<ResourceKey<Recipe<?>>> highlight() { return this.highlight; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(RecipeBookSettings.MAP_CODEC
/* 160 */           .forGetter(Packed::settings), Recipe.KEY_CODEC
/* 161 */           .listOf().fieldOf("recipes").forGetter(Packed::known), Recipe.KEY_CODEC
/* 162 */           .listOf().fieldOf("toBeDisplayed").forGetter(Packed::highlight))
/* 163 */         .apply(i, Packed::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\ServerRecipeBook.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */