/*    */ package net.minecraft.world.item.enchantment;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class Repairable extends Record {
/*    */   private final HolderSet<Item> items;
/*    */   
/* 14 */   public Repairable(HolderSet<Item> items) { this.items = items; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Repairable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Repairable; } public HolderSet<Item> items() { return this.items; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Repairable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Repairable; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Repairable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Repairable;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final Codec<Repairable> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 16 */         RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(Repairable::items))
/* 17 */       .apply(i, Repairable::new));
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, Repairable> STREAM_CODEC = StreamCodec.composite(
/* 20 */       ByteBufCodecs.holderSet(Registries.ITEM), Repairable::items, Repairable::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean isValidRepairItem(ItemStack repairItemStack) { return repairItemStack.is(this.items); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\Repairable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */