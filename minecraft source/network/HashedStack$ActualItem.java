/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ActualItem
/*    */   extends Record
/*    */   implements HashedStack
/*    */ {
/*    */   private final Holder<Item> item;
/*    */   private final int count;
/*    */   private final HashedPatchMap components;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/HashedStack$ActualItem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HashedStack$ActualItem; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/HashedStack$ActualItem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HashedStack$ActualItem; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/HashedStack$ActualItem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/HashedStack$ActualItem;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 28 */   public ActualItem(Holder<Item> item, int count, HashedPatchMap components) { this.item = item; this.count = count; this.components = components; } public Holder<Item> item() { return this.item; } public int count() { return this.count; } public HashedPatchMap components() { return this.components; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, ActualItem> STREAM_CODEC = StreamCodec.composite(
/* 35 */       ByteBufCodecs.holderRegistry(Registries.ITEM), ActualItem::item, ByteBufCodecs.VAR_INT, ActualItem::count, HashedPatchMap.STREAM_CODEC, ActualItem::components, ActualItem::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack itemStack, HashedPatchMap.HashGenerator hasher) {
/* 43 */     if (this.count != itemStack.getCount()) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     if (!this.item.equals(itemStack.getItemHolder())) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     return this.components.matches(itemStack.getComponentsPatch(), hasher);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\HashedStack$ActualItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */