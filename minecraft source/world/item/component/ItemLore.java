/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class ItemLore extends Record implements TooltipProvider {
/*    */   private final List<Component> lines;
/*    */   private final List<Component> styledLines;
/*    */   
/* 21 */   public List<Component> lines() { return this.lines; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemLore;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemLore; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemLore;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemLore; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemLore;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/ItemLore;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public List<Component> styledLines() { return this.styledLines; }
/* 22 */   public static final ItemLore EMPTY = new ItemLore(List.of());
/*    */   
/*    */   public static final int MAX_LINES = 256;
/*    */   
/* 26 */   private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(Boolean.valueOf(true));
/*    */   
/* 28 */   public static final Codec<ItemLore> CODEC = ComponentSerialization.CODEC.sizeLimitedListOf(256).xmap(ItemLore::new, ItemLore::lines);
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemLore> STREAM_CODEC = ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list(256)).map(ItemLore::new, ItemLore::lines);
/*    */ 
/*    */   
/* 32 */   public ItemLore(List<Component> lines) { this(lines, Lists.transform(lines, component -> ComponentUtils.mergeStyles(component, LORE_STYLE))); }
/*    */ 
/*    */   
/*    */   public ItemLore(List<Component> lines, List<Component> styledLines) {
/* 36 */     if (lines.size() > 256)
/* 37 */       throw new IllegalArgumentException("Got " + lines.size() + " lines, but maximum is 256"); 
/*    */     this.lines = lines;
/*    */     this.styledLines = styledLines;
/*    */   }
/*    */   
/* 42 */   public ItemLore withLineAdded(Component component) { return new ItemLore(Util.copyAndAdd(this.lines, component)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) { this.styledLines.forEach(consumer); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ItemLore.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */