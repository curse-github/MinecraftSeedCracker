/*    */ package net.minecraft.server.dialog.body;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ItemBody extends Record implements DialogBody {
/*    */   private final ItemStack item;
/*    */   private final Optional<PlainMessage> description;
/*    */   private final boolean showDecorations;
/*    */   
/* 11 */   public ItemBody(ItemStack item, Optional<PlainMessage> description, boolean showDecorations, boolean showTooltip, int width, int height) { this.item = item; this.description = description; this.showDecorations = showDecorations; this.showTooltip = showTooltip; this.width = width; this.height = height; } private final boolean showTooltip; private final int width; private final int height; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/body/ItemBody;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/ItemBody; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/body/ItemBody;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/ItemBody; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/body/ItemBody;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/body/ItemBody;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ItemStack item() { return this.item; } public Optional<PlainMessage> description() { return this.description; } public boolean showDecorations() { return this.showDecorations; } public boolean showTooltip() { return this.showTooltip; } public int width() { return this.width; } public int height() { return this.height; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final MapCodec<ItemBody> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ItemStack.STRICT_CODEC
/* 20 */         .fieldOf("item").forGetter(ItemBody::item), PlainMessage.CODEC
/* 21 */         .optionalFieldOf("description").forGetter(ItemBody::description), Codec.BOOL
/* 22 */         .optionalFieldOf("show_decorations", Boolean.valueOf(true)).forGetter(ItemBody::showDecorations), Codec.BOOL
/* 23 */         .optionalFieldOf("show_tooltip", Boolean.valueOf(true)).forGetter(ItemBody::showTooltip), 
/* 24 */         ExtraCodecs.intRange(1, 256).optionalFieldOf("width", Integer.valueOf(16)).forGetter(ItemBody::width), 
/* 25 */         ExtraCodecs.intRange(1, 256).optionalFieldOf("height", Integer.valueOf(16)).forGetter(ItemBody::height))
/* 26 */       .apply(i, ItemBody::new));
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<ItemBody> mapCodec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\body\ItemBody.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */