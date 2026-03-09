/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface NumberProvider
/*    */   extends LootContextUser {
/*    */   float getFloat(LootContext paramLootContext);
/*    */   
/* 10 */   default int getInt(LootContext context) { return Math.round(getFloat(context)); }
/*    */   
/*    */   LootNumberProviderType getType();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\NumberProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */