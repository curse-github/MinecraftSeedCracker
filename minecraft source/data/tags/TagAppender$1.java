/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements TagAppender<ResourceKey<T>, T>
/*    */ {
/*    */   public TagAppender<ResourceKey<T>, T> add(ResourceKey<T> element) {
/* 39 */     builder.addElement(element.identifier());
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagAppender<ResourceKey<T>, T> addOptional(ResourceKey<T> element) {
/* 45 */     builder.addOptionalElement(element.identifier());
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagAppender<ResourceKey<T>, T> addTag(TagKey<T> tag) {
/* 51 */     builder.addTag(tag.location());
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagAppender<ResourceKey<T>, T> addOptionalTag(TagKey<T> tag) {
/* 57 */     builder.addOptionalTag(tag.location());
/* 58 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\TagAppender$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */