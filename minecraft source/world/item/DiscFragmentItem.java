/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ 
/*    */ public class DiscFragmentItem
/*    */   extends Item
/*    */ {
/* 12 */   public DiscFragmentItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) { builder.accept(getDisplayName().withStyle(ChatFormatting.GRAY)); }
/*    */ 
/*    */   
/*    */   public MutableComponent getDisplayName() {
/* 21 */     return Component.translatable(this.descriptionId + ".desc");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\DiscFragmentItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */