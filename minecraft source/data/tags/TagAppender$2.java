/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.function.Function;
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
/*    */   implements TagAppender<U, T>
/*    */ {
/*    */   null(TagAppender this$0) {}
/*    */   
/*    */   public TagAppender<U, T> add(U element) {
/* 68 */     original.add(converter.apply(element));
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagAppender<U, T> addOptional(U element) {
/* 74 */     original.add(converter.apply(element));
/* 75 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagAppender<U, T> addTag(TagKey<T> tag) {
/* 80 */     original.addTag(tag);
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TagAppender<U, T> addOptionalTag(TagKey<T> tag) {
/* 86 */     original.addOptionalTag(tag);
/* 87 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\TagAppender$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */