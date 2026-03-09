/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ 
/*    */ 
/*    */ public interface LootContextUser
/*    */ {
/*  9 */   default Set<ContextKey<?>> getReferencedContextParams() { return Set.of(); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   default void validate(ValidationContext context) { context.validateContextUsage(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContextUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */