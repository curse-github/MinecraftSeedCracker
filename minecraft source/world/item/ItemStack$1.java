/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Object
/*     */   implements StreamCodec<RegistryFriendlyByteBuf, ItemStack>
/*     */ {
/*     */   public ItemStack decode(RegistryFriendlyByteBuf input) {
/* 160 */     int count = input.readVarInt();
/* 161 */     if (count <= 0) {
/* 162 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 165 */     Holder<Item> item = (Holder)Item.STREAM_CODEC.decode(input);
/* 166 */     DataComponentPatch patch = (DataComponentPatch)patchCodec.decode(input);
/* 167 */     return new ItemStack(item, count, patch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) {
/* 172 */     if (itemStack.isEmpty()) {
/* 173 */       output.writeVarInt(0);
/*     */       
/*     */       return;
/*     */     } 
/* 177 */     output.writeVarInt(itemStack.getCount());
/* 178 */     Item.STREAM_CODEC.encode(output, itemStack.getItemHolder());
/* 179 */     patchCodec.encode(output, itemStack.components.asPatch());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemStack$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */