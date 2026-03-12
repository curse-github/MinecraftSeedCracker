/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class LootItemKilledByPlayerCondition
/*    */   implements LootItemCondition {
/* 11 */   private static final LootItemKilledByPlayerCondition INSTANCE = new LootItemKilledByPlayerCondition();
/* 12 */   public static final MapCodec<LootItemKilledByPlayerCondition> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public LootItemConditionType getType() { return LootItemConditions.KILLED_BY_PLAYER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.LAST_DAMAGE_PLAYER); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public boolean test(LootContext context) { return context.hasParameter(LootContextParams.LAST_DAMAGE_PLAYER); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static LootItemCondition.Builder killedByPlayer() { return () -> INSTANCE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemKilledByPlayerCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */