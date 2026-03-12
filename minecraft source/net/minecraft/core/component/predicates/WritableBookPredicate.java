/*    */ package net.minecraft.core.component.predicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ 
/*    */ public final class WritableBookPredicate extends Record implements SingleComponentItemPredicate<WritableBookContent> {
/*    */   private final Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages;
/*    */   
/* 15 */   public WritableBookPredicate(Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages) { this.pages = pages; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate; } public Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages() { return this.pages; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; } public static final class PagePredicate extends Record implements Predicate<Filterable<String>> { private final String contents;
/* 16 */     public PagePredicate(String contents) { this.contents = contents; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/* 16 */       //   0	8	1	o	Ljava/lang/Object; } public String contents() { return this.contents; }
/* 17 */     public static final Codec<PagePredicate> CODEC = Codec.STRING.xmap(PagePredicate::new, PagePredicate::contents);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 22 */     public boolean test(Filterable<String> value) { return ((String)value.raw()).equals(this.contents); } }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final Codec<WritableBookPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 27 */         CollectionPredicate.codec(PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WritableBookPredicate::pages))
/* 28 */       .apply(i, WritableBookPredicate::new));
/*    */ 
/*    */ 
/*    */   
/* 32 */   public DataComponentType<WritableBookContent> componentType() { return DataComponents.WRITABLE_BOOK_CONTENT; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(WritableBookContent value) {
/* 37 */     if (this.pages.isPresent() && !((CollectionPredicate)this.pages.get()).test(value.pages())) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\WritableBookPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */