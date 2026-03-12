/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
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
/*    */ public final class Entry
/*    */   extends Record
/*    */ {
/*    */   private final String id;
/*    */   private final Optional<Component> display;
/*    */   private final boolean initial;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 45 */   public Entry(String id, Optional<Component> display, boolean initial) { this.id = id; this.display = display; this.initial = initial; } public String id() { return this.id; } public Optional<Component> display() { return this.display; } public boolean initial() { return this.initial; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static final Codec<Entry> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 51 */         .fieldOf("id").forGetter(Entry::id), ComponentSerialization.CODEC
/* 52 */         .optionalFieldOf("display").forGetter(Entry::display), Codec.BOOL
/* 53 */         .optionalFieldOf("initial", Boolean.valueOf(false)).forGetter(Entry::initial))
/* 54 */       .apply(i, Entry::new));
/*    */   
/* 56 */   public static final Codec<Entry> CODEC = Codec.withAlternative(FULL_CODEC, Codec.STRING, id -> 
/*    */       
/* 58 */       new Entry(id, Optional.empty(), false));
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Component displayOrDefault() { return (Component)this.display.orElseGet(() -> Component.literal(this.id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\SingleOptionInput$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */