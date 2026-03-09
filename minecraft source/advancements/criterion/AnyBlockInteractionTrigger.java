/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootParams;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class AnyBlockInteractionTrigger
/*    */   extends SimpleCriterionTrigger<AnyBlockInteractionTrigger.TriggerInstance> {
/* 20 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, BlockPos pos, ItemStack itemStack) {
/* 24 */     ServerLevel level = player.level();
/* 25 */     BlockState state = level.getBlockState(pos);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     LootParams params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, pos.getCenter()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.BLOCK_STATE, state).withParameter(LootContextParams.TOOL, itemStack).create(LootContextParamSets.ADVANCEMENT_LOCATION);
/* 32 */     LootContext context = (new LootContext.Builder(params)).create(Optional.empty());
/*    */     
/* 34 */     trigger(player, t -> t.matches(context));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> location;
/* 37 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) { this.player = player; this.location = location; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/AnyBlockInteractionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 37 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/AnyBlockInteractionTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/AnyBlockInteractionTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/AnyBlockInteractionTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/AnyBlockInteractionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/AnyBlockInteractionTrigger$TriggerInstance;
/* 37 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> location() { return this.location; }
/*    */ 
/*    */ 
/*    */     
/* 41 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 42 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), ContextAwarePredicate.CODEC
/* 43 */           .optionalFieldOf("location").forGetter(TriggerInstance::location))
/* 44 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 47 */     public boolean matches(LootContext locationContext) { return (this.location.isEmpty() || ((ContextAwarePredicate)this.location.get()).matches(locationContext)); }
/*    */ 
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 52 */       super.validate(validator);
/* 53 */       this.location.ifPresent(predicate -> validator.validate(predicate, LootContextParamSets.ADVANCEMENT_LOCATION, "location"));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\AnyBlockInteractionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */