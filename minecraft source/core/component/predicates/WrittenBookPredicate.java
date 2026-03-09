/*    */ package net.minecraft.core.component.predicates;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ 
/*    */ public final class WrittenBookPredicate extends Record implements SingleComponentItemPredicate<WrittenBookContent> {
/*    */   private final Optional<CollectionPredicate<Filterable<Component>, PagePredicate>> pages;
/*    */   private final Optional<String> author;
/*    */   
/* 18 */   public WrittenBookPredicate(Optional<CollectionPredicate<Filterable<Component>, PagePredicate>> pages, Optional<String> author, Optional<String> title, MinMaxBounds.Ints generation, Optional<Boolean> resolved) { this.pages = pages; this.author = author; this.title = title; this.generation = generation; this.resolved = resolved; } private final Optional<String> title; private final MinMaxBounds.Ints generation; private final Optional<Boolean> resolved; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<CollectionPredicate<Filterable<Component>, PagePredicate>> pages() { return this.pages; } public Optional<String> author() { return this.author; } public Optional<String> title() { return this.title; } public MinMaxBounds.Ints generation() { return this.generation; } public Optional<Boolean> resolved() { return this.resolved; }
/*    */   
/*    */   public static final class PagePredicate
/*    */     extends Record
/*    */     implements Predicate<Filterable<Component>> {
/*    */     private final Component contents;
/*    */     
/* 25 */     public PagePredicate(Component contents) { this.contents = contents; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public Component contents() { return this.contents; }
/* 26 */     public static final Codec<PagePredicate> CODEC = ComponentSerialization.CODEC.xmap(PagePredicate::new, PagePredicate::contents);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     public boolean test(Filterable<Component> value) { return ((Component)value.raw()).equals(this.contents); }
/*    */   }
/*    */ 
/*    */   
/* 35 */   public static final Codec<WrittenBookPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 36 */         CollectionPredicate.codec(PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WrittenBookPredicate::pages), Codec.STRING
/* 37 */         .optionalFieldOf("author").forGetter(WrittenBookPredicate::author), Codec.STRING
/* 38 */         .optionalFieldOf("title").forGetter(WrittenBookPredicate::title), MinMaxBounds.Ints.CODEC
/* 39 */         .optionalFieldOf("generation", MinMaxBounds.Ints.ANY).forGetter(WrittenBookPredicate::generation), Codec.BOOL
/* 40 */         .optionalFieldOf("resolved").forGetter(WrittenBookPredicate::resolved))
/* 41 */       .apply(i, WrittenBookPredicate::new));
/*    */ 
/*    */ 
/*    */   
/* 45 */   public DataComponentType<WrittenBookContent> componentType() { return DataComponents.WRITTEN_BOOK_CONTENT; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(WrittenBookContent value) {
/* 50 */     if (this.author.isPresent() && !((String)this.author.get()).equals(value.author())) {
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     if (this.title.isPresent() && !((String)this.title.get()).equals(value.title().raw())) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     if (!this.generation.matches(value.generation())) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     if (this.resolved.isPresent() && ((Boolean)this.resolved.get()).booleanValue() != value.resolved()) {
/* 63 */       return false;
/*    */     }
/*    */     
/* 66 */     if (this.pages.isPresent() && !((CollectionPredicate)this.pages.get()).test(value.pages())) {
/* 67 */       return false;
/*    */     }
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\WrittenBookPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */