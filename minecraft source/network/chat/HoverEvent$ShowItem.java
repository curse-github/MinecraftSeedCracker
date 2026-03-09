/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ public final class ShowItem
/*    */   extends Record
/*    */   implements HoverEvent
/*    */ {
/*    */   private final ItemStack item;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem; }
/*    */   
/* 36 */   public ItemStack item() { return this.item; }
/* 37 */   public static final MapCodec<ShowItem> CODEC = ItemStack.MAP_CODEC.xmap(ShowItem::new, ShowItem::item);
/*    */   
/*    */   public ShowItem(ItemStack item) {
/* 40 */     item = item.copy();
/*    */     this.item = item;
/*    */   }
/*    */ 
/*    */   
/* 45 */   public HoverEvent.Action action() { return HoverEvent.Action.SHOW_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean equals(Object obj) { if (obj instanceof ShowItem) { ShowItem showItem = (ShowItem)obj; if (ItemStack.matches(this.item, showItem.item)); }  return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public int hashCode() { return ItemStack.hashItemAndComponents(this.item); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\HoverEvent$ShowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */