/*    */ package net.minecraft.world.level.block.entity;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.TooltipProvider;
/*    */ 
/*    */ public final class PotDecorations extends Record implements TooltipProvider {
/*    */   private final Optional<Item> back;
/*    */   private final Optional<Item> left;
/*    */   private final Optional<Item> right;
/*    */   private final Optional<Item> front;
/*    */   
/* 24 */   public PotDecorations(Optional<Item> back, Optional<Item> left, Optional<Item> right, Optional<Item> front) { this.back = back; this.left = left; this.right = right; this.front = front; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/PotDecorations;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 24 */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/PotDecorations; } public Optional<Item> back() { return this.back; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/PotDecorations;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/PotDecorations; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/PotDecorations;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/PotDecorations;
/* 24 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Item> left() { return this.left; } public Optional<Item> right() { return this.right; } public Optional<Item> front() { return this.front; }
/* 25 */   public static final PotDecorations EMPTY = new PotDecorations(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
/*    */   
/* 27 */   public static final Codec<PotDecorations> CODEC = BuiltInRegistries.ITEM.byNameCodec().sizeLimitedListOf(4).xmap(PotDecorations::new, PotDecorations::ordered);
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, PotDecorations> STREAM_CODEC = ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list(4)).map(PotDecorations::new, PotDecorations::ordered);
/*    */ 
/*    */   
/* 31 */   private PotDecorations(List<Item> items) { this(getItem(items, 0), getItem(items, 1), getItem(items, 2), getItem(items, 3)); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PotDecorations(Item back, Item left, Item right, Item front) { this(List.of(back, left, right, front)); }
/*    */ 
/*    */   
/*    */   private static Optional<Item> getItem(List<Item> sherds, int i) {
/* 39 */     if (i >= sherds.size()) {
/* 40 */       return Optional.empty();
/*    */     }
/* 42 */     Item item = (Item)sherds.get(i);
/* 43 */     return (item == Items.BRICK) ? Optional.empty() : Optional.of(item);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public List<Item> ordered() { return Stream.of(new Optional[] { this.back, this.left, this.right, this.front }).map(item -> (Item)item.orElse(Items.BRICK)).toList(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 54 */     if (equals(EMPTY)) {
/*    */       return;
/*    */     }
/* 57 */     consumer.accept(CommonComponents.EMPTY);
/* 58 */     addSideDetailsToTooltip(consumer, this.front);
/* 59 */     addSideDetailsToTooltip(consumer, this.left);
/* 60 */     addSideDetailsToTooltip(consumer, this.right);
/* 61 */     addSideDetailsToTooltip(consumer, this.back);
/*    */   }
/*    */ 
/*    */   
/* 65 */   private static void addSideDetailsToTooltip(Consumer<Component> consumer, Optional<Item> side) { consumer.accept((new ItemStack((ItemLike)side.orElse(Items.BRICK), 1)).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\PotDecorations.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */