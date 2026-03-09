/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ interface ComposableEntryContainer
/*    */ {
/*    */   public static final ComposableEntryContainer ALWAYS_FALSE = (context, output) -> false;
/*    */   public static final ComposableEntryContainer ALWAYS_TRUE = (context, output) -> true;
/*    */   
/*    */   default ComposableEntryContainer and(ComposableEntryContainer other) {
/* 16 */     Objects.requireNonNull(other);
/* 17 */     return (context, output) -> (expand(context, output) && other.expand(context, output));
/*    */   }
/*    */   
/*    */   default ComposableEntryContainer or(ComposableEntryContainer other) {
/* 21 */     Objects.requireNonNull(other);
/* 22 */     return (context, output) -> (expand(context, output) || other.expand(context, output));
/*    */   }
/*    */   
/*    */   boolean expand(LootContext paramLootContext, Consumer<LootPoolEntry> paramConsumer);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\ComposableEntryContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */