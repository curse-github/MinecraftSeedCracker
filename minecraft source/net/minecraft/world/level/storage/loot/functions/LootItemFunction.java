/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LootItemFunction
/*    */   extends LootContextUser, BiFunction<ItemStack, LootContext, ItemStack>
/*    */ {
/*    */   LootItemFunctionType<? extends LootItemFunction> getType();
/*    */   
/* 16 */   static Consumer<ItemStack> decorate(BiFunction<ItemStack, LootContext, ItemStack> function, Consumer<ItemStack> output, LootContext context) { return drop -> output.accept((ItemStack)function.apply(drop, context)); }
/*    */   
/*    */   public static interface Builder {
/*    */     LootItemFunction build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */