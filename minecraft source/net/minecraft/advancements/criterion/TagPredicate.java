/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public final class TagPredicate<T> extends Record {
/*    */   private final TagKey<T> tag;
/*    */   private final boolean expected;
/*    */   
/* 10 */   public TagPredicate(TagKey<T> tag, boolean expected) { this.tag = tag; this.expected = expected; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/TagPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate<TT;>; } public TagKey<T> tag() { return this.tag; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/TagPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TagPredicate<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/TagPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/TagPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/TagPredicate<TT;>; } public boolean expected() { return this.expected; }
/*    */   
/* 12 */   public static <T> Codec<TagPredicate<T>> codec(ResourceKey<? extends Registry<T>> registryKey) { return RecordCodecBuilder.create(i -> i.group(
/* 13 */           TagKey.codec(registryKey).fieldOf("id").forGetter(TagPredicate::tag), Codec.BOOL
/* 14 */           .fieldOf("expected").forGetter(TagPredicate::expected))
/* 15 */         .apply(i, TagPredicate::new)); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static <T> TagPredicate<T> is(TagKey<T> tag) { return new TagPredicate(tag, true); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static <T> TagPredicate<T> isNot(TagKey<T> tag) { return new TagPredicate(tag, false); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean matches(Holder<T> holder) { return (holder.is(this.tag) == this.expected); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\TagPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */