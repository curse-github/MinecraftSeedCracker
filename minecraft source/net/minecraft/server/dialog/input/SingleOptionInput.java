/*    */ package net.minecraft.server.dialog.input;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ 
/*    */ public final class SingleOptionInput extends Record implements InputControl {
/*    */   private final int width;
/*    */   private final List<Entry> entries;
/*    */   
/* 15 */   public SingleOptionInput(int width, List<Entry> entries, Component label, boolean labelVisible) { this.width = width; this.entries = entries; this.label = label; this.labelVisible = labelVisible; } private final Component label; private final boolean labelVisible; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/SingleOptionInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/SingleOptionInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/SingleOptionInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public List<Entry> entries() { return this.entries; } public Component label() { return this.label; } public boolean labelVisible() { return this.labelVisible; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final MapCodec<SingleOptionInput> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Dialog.WIDTH_CODEC
/* 23 */         .optionalFieldOf("width", Integer.valueOf(200)).forGetter(SingleOptionInput::width), 
/* 24 */         ExtraCodecs.nonEmptyList(Entry.CODEC.listOf()).fieldOf("options").forGetter(SingleOptionInput::entries), ComponentSerialization.CODEC
/* 25 */         .fieldOf("label").forGetter(SingleOptionInput::label), Codec.BOOL
/* 26 */         .optionalFieldOf("label_visible", Boolean.valueOf(true)).forGetter(SingleOptionInput::labelVisible))
/* 27 */       .apply(i, SingleOptionInput::new))
/* 28 */     .validate(o -> {
/* 29 */         long initialCount = o.entries.stream().filter(Entry::initial).count();
/* 30 */         if (initialCount > 1L) {
/* 31 */           return DataResult.error(());
/*    */         }
/* 33 */         return DataResult.success(o);
/*    */       });
/*    */ 
/*    */ 
/*    */   
/* 38 */   public MapCodec<SingleOptionInput> mapCodec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Optional<Entry> initial() { return this.entries.stream().filter(Entry::initial).findFirst(); }
/*    */   public static final class Entry extends Record { private final String id; private final Optional<Component> display; private final boolean initial;
/*    */     
/* 45 */     public Entry(String id, Optional<Component> display, boolean initial) { this.id = id; this.display = display; this.initial = initial; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;
/* 45 */       //   0	8	1	o	Ljava/lang/Object; } public String id() { return this.id; } public Optional<Component> display() { return this.display; } public boolean initial() { return this.initial; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     public static final Codec<Entry> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 51 */           .fieldOf("id").forGetter(Entry::id), ComponentSerialization.CODEC
/* 52 */           .optionalFieldOf("display").forGetter(Entry::display), Codec.BOOL
/* 53 */           .optionalFieldOf("initial", Boolean.valueOf(false)).forGetter(Entry::initial))
/* 54 */         .apply(i, Entry::new));
/*    */     
/* 56 */     public static final Codec<Entry> CODEC = Codec.withAlternative(FULL_CODEC, Codec.STRING, id -> 
/*    */         
/* 58 */         new Entry(id, Optional.empty(), false));
/*    */ 
/*    */ 
/*    */     
/* 62 */     public Component displayOrDefault() { return (Component)this.display.orElseGet(() -> Component.literal(this.id)); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\SingleOptionInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */