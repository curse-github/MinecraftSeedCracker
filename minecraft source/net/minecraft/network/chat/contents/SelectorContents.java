/*    */ package net.minecraft.network.chat.contents;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class SelectorContents extends Record implements ComponentContents {
/*    */   private final SelectorPattern selector;
/*    */   private final Optional<Component> separator;
/*    */   
/* 20 */   public SelectorContents(SelectorPattern selector, Optional<Component> separator) { this.selector = selector; this.separator = separator; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/SelectorContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/SelectorContents; } public SelectorPattern selector() { return this.selector; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/SelectorContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/SelectorContents;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Component> separator() { return this.separator; }
/* 21 */   public static final MapCodec<SelectorContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SelectorPattern.CODEC
/* 22 */         .fieldOf("selector").forGetter(SelectorContents::selector), ComponentSerialization.CODEC
/* 23 */         .optionalFieldOf("separator").forGetter(SelectorContents::separator))
/* 24 */       .apply(i, SelectorContents::new));
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<SelectorContents> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException {
/* 33 */     if (source == null) {
/* 34 */       return Component.empty();
/*    */     }
/* 36 */     Optional<? extends Component> resolvedSeparator = ComponentUtils.updateForEntity(source, this.separator, entity, recursionDepth);
/* 37 */     return ComponentUtils.formatList(this.selector.resolved().findEntities(source), resolvedSeparator, Entity::getDisplayName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) { return output.accept(currentStyle, this.selector.pattern()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return output.accept(this.selector.pattern()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public String toString() { return "pattern{" + String.valueOf(this.selector) + "}"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\SelectorContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */