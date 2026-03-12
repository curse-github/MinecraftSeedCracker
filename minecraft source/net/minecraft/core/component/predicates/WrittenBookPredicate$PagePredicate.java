/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PagePredicate
/*    */   extends Record
/*    */   implements Predicate<Filterable<Component>>
/*    */ {
/*    */   private final Component contents;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public PagePredicate(Component contents) { this.contents = contents; } public Component contents() { return this.contents; }
/* 26 */   public static final Codec<PagePredicate> CODEC = ComponentSerialization.CODEC.xmap(PagePredicate::new, PagePredicate::contents);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean test(Filterable<Component> value) { return ((Component)value.raw()).equals(this.contents); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\WrittenBookPredicate$PagePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */