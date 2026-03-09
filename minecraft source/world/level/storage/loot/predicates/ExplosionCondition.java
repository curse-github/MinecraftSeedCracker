/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class ExplosionCondition
/*    */   implements LootItemCondition {
/* 12 */   private static final ExplosionCondition INSTANCE = new ExplosionCondition();
/* 13 */   public static final MapCodec<ExplosionCondition> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public LootItemConditionType getType() { return LootItemConditions.SURVIVES_EXPLOSION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.EXPLOSION_RADIUS); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 30 */     Float explosionRadius = (Float)context.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
/* 31 */     if (explosionRadius != null) {
/* 32 */       RandomSource random = context.getRandom();
/* 33 */       float probability = 1.0F / explosionRadius.floatValue();
/* 34 */       return (random.nextFloat() <= probability);
/*    */     } 
/*    */     
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 41 */   public static LootItemCondition.Builder survivesExplosion() { return () -> INSTANCE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\ExplosionCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */