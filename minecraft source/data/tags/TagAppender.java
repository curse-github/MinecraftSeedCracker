/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ 
/*    */ public interface TagAppender<E, T>
/*    */ {
/*    */   TagAppender<E, T> add(E paramE);
/*    */   
/* 16 */   TagAppender<E, T> add(E... elements) { return addAll(Arrays.stream(elements)); }
/*    */ 
/*    */   
/*    */   default TagAppender<E, T> addAll(Collection<E> elements) {
/* 20 */     elements.forEach(this::add);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   default TagAppender<E, T> addAll(Stream<E> elements) {
/* 25 */     elements.forEach(this::add);
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   TagAppender<E, T> addOptional(E paramE);
/*    */   
/*    */   TagAppender<E, T> addTag(TagKey<T> paramTagKey);
/*    */   
/*    */   TagAppender<E, T> addOptionalTag(TagKey<T> paramTagKey);
/*    */   
/*    */   static <T> TagAppender<ResourceKey<T>, T> forBuilder(final TagBuilder builder) {
/* 36 */     return new TagAppender<ResourceKey<T>, T>()
/*    */       {
/*    */         public TagAppender<ResourceKey<T>, T> add(ResourceKey<T> element) {
/* 39 */           builder.addElement(element.identifier());
/* 40 */           return this;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagAppender<ResourceKey<T>, T> addOptional(ResourceKey<T> element) {
/* 45 */           builder.addOptionalElement(element.identifier());
/* 46 */           return this;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagAppender<ResourceKey<T>, T> addTag(TagKey<T> tag) {
/* 51 */           builder.addTag(tag.location());
/* 52 */           return this;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagAppender<ResourceKey<T>, T> addOptionalTag(TagKey<T> tag) {
/* 57 */           builder.addOptionalTag(tag.location());
/* 58 */           return this;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default <U> TagAppender<U, T> map(final Function<U, E> converter) {
/* 64 */     final TagAppender<E, T> original = this;
/* 65 */     return new TagAppender<U, T>(this)
/*    */       {
/*    */         public TagAppender<U, T> add(U element) {
/* 68 */           original.add(converter.apply(element));
/* 69 */           return this;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagAppender<U, T> addOptional(U element) {
/* 74 */           original.add(converter.apply(element));
/* 75 */           return this;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagAppender<U, T> addTag(TagKey<T> tag) {
/* 80 */           original.addTag(tag);
/* 81 */           return this;
/*    */         }
/*    */ 
/*    */         
/*    */         public TagAppender<U, T> addOptionalTag(TagKey<T> tag) {
/* 86 */           original.addOptionalTag(tag);
/* 87 */           return this;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\TagAppender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */