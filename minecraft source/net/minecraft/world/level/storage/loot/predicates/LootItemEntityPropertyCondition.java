/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.criterion.EntityPredicate;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LootItemEntityPropertyCondition extends Record implements LootItemCondition {
/*    */   private final Optional<EntityPredicate> predicate;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/* 15 */   public LootItemEntityPropertyCondition(Optional<EntityPredicate> predicate, LootContext.EntityTarget entityTarget) { this.predicate = predicate; this.entityTarget = entityTarget; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition; } public Optional<EntityPredicate> predicate() { return this.predicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public LootContext.EntityTarget entityTarget() { return this.entityTarget; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final MapCodec<LootItemEntityPropertyCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(EntityPredicate.CODEC
/* 20 */         .optionalFieldOf("predicate").forGetter(LootItemEntityPropertyCondition::predicate), LootContext.EntityTarget.CODEC
/* 21 */         .fieldOf("entity").forGetter(LootItemEntityPropertyCondition::entityTarget))
/* 22 */       .apply(i, LootItemEntityPropertyCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 26 */   public LootItemConditionType getType() { return LootItemConditions.ENTITY_PROPERTIES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.ORIGIN, this.entityTarget.contextParam()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 36 */     Entity entity = (Entity)context.getOptionalParameter(this.entityTarget.contextParam());
/* 37 */     Vec3 pos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/* 38 */     return (this.predicate.isEmpty() || ((EntityPredicate)this.predicate.get()).matches(context.getLevel(), pos, entity));
/*    */   }
/*    */ 
/*    */   
/* 42 */   public static LootItemCondition.Builder entityPresent(LootContext.EntityTarget target) { return hasProperties(target, EntityPredicate.Builder.entity()); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget target, EntityPredicate.Builder predicate) { return () -> new LootItemEntityPropertyCondition(Optional.of(predicate.build()), target); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget target, EntityPredicate predicate) { return () -> new LootItemEntityPropertyCondition(Optional.of(predicate), target); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemEntityPropertyCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */