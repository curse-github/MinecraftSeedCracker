/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class TagBuilder
/*    */ {
/*  9 */   private final List<TagEntry> entries = new ArrayList();
/*    */ 
/*    */   
/* 12 */   public static TagBuilder create() { return new TagBuilder(); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public List<TagEntry> build() { return List.copyOf(this.entries); }
/*    */ 
/*    */   
/*    */   public TagBuilder add(TagEntry entry) {
/* 20 */     this.entries.add(entry);
/* 21 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 25 */   public TagBuilder addElement(Identifier id) { return add(TagEntry.element(id)); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public TagBuilder addOptionalElement(Identifier id) { return add(TagEntry.optionalElement(id)); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public TagBuilder addTag(Identifier id) { return add(TagEntry.tag(id)); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public TagBuilder addOptionalTag(Identifier id) { return add(TagEntry.optionalTag(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\TagBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */