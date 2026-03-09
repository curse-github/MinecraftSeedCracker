/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public abstract class IntrinsicHolderTagsProvider<T>
/*    */   extends TagsProvider<T> {
/*    */   private final Function<T, ResourceKey<T>> keyExtractor;
/*    */   
/*    */   public IntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, Function<T, ResourceKey<T>> keyExtractor) {
/* 17 */     super(output, registryKey, lookupProvider);
/* 18 */     this.keyExtractor = keyExtractor;
/*    */   }
/*    */   
/*    */   public IntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<T>> parentProvider, Function<T, ResourceKey<T>> keyExtractor) {
/* 22 */     super(output, registryKey, lookupProvider, parentProvider);
/* 23 */     this.keyExtractor = keyExtractor;
/*    */   }
/*    */   
/*    */   protected TagAppender<T, T> tag(TagKey<T> tag) {
/* 27 */     TagBuilder builder = getOrCreateRawBuilder(tag);
/* 28 */     return TagAppender.forBuilder(builder).map(this.keyExtractor);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\IntrinsicHolderTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */