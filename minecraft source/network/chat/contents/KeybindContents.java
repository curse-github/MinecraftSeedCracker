/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentContents;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public class KeybindContents implements ComponentContents {
/* 16 */   public static final MapCodec<KeybindContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 17 */         .fieldOf("keybind").forGetter(()))
/* 18 */       .apply(i, KeybindContents::new));
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private Supplier<Component> nameResolver;
/*    */   
/* 24 */   public KeybindContents(String name) { this.name = name; }
/*    */ 
/*    */   
/*    */   private Component getNestedComponent() {
/* 28 */     if (this.nameResolver == null) {
/* 29 */       this.nameResolver = (Supplier)KeybindResolver.keyResolver.apply(this.name);
/*    */     }
/*    */     
/* 32 */     return (Component)this.nameResolver.get();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return getNestedComponent().visit(output); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) { return getNestedComponent().visit(output, currentStyle); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: if_acmpne -> 7
/*    */     //   5: iconst_1
/*    */     //   6: ireturn
/*    */     //   7: aload_1
/*    */     //   8: instanceof net/minecraft/network/chat/contents/KeybindContents
/*    */     //   11: ifeq -> 37
/*    */     //   14: aload_1
/*    */     //   15: checkcast net/minecraft/network/chat/contents/KeybindContents
/*    */     //   18: astore_2
/*    */     //   19: aload_0
/*    */     //   20: getfield name : Ljava/lang/String;
/*    */     //   23: aload_2
/*    */     //   24: getfield name : Ljava/lang/String;
/*    */     //   27: invokevirtual equals : (Ljava/lang/Object;)Z
/*    */     //   30: ifeq -> 37
/*    */     //   33: iconst_1
/*    */     //   34: goto -> 38
/*    */     //   37: iconst_0
/*    */     //   38: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     //   #48	-> 5
/*    */     //   #51	-> 7
/*    */     //   #50	-> 14
/*    */     //   #51	-> 27
/*    */     //   #50	-> 38
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   19	18	2	that	Lnet/minecraft/network/chat/contents/KeybindContents;
/*    */     //   0	39	0	this	Lnet/minecraft/network/chat/contents/KeybindContents;
/*    */     //   0	39	1	o	Ljava/lang/Object; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public int hashCode() { return this.name.hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public String toString() { return "keybind{" + this.name + "}"; }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public MapCodec<KeybindContents> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\KeybindContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */