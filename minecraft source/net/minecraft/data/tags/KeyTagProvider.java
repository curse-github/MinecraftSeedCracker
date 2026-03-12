/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ 
/*    */ public abstract class KeyTagProvider<T>
/*    */   extends TagsProvider<T>
/*    */ {
/* 15 */   protected KeyTagProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) { super(output, registryKey, lookupProvider); }
/*    */ 
/*    */   
/*    */   protected TagAppender<ResourceKey<T>, T> tag(TagKey<T> tag) {
/* 19 */     TagBuilder builder = getOrCreateRawBuilder(tag);
/* 20 */     return TagAppender.forBuilder(builder);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\KeyTagProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */