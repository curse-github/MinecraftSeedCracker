/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<Holder<Block>> block;
/*    */   private final Optional<StatePropertiesPredicate> state;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EnterBlockTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EnterBlockTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EnterBlockTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EnterBlockTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EnterBlockTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EnterBlockTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 26 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Holder<Block>> block, Optional<StatePropertiesPredicate> state) { this.player = player; this.block = block; this.state = state; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<Holder<Block>> block() { return this.block; } public Optional<StatePropertiesPredicate> state() { return this.state; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 32 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), BuiltInRegistries.BLOCK
/* 33 */         .holderByNameCodec().optionalFieldOf("block").forGetter(TriggerInstance::block), StatePropertiesPredicate.CODEC
/* 34 */         .optionalFieldOf("state").forGetter(TriggerInstance::state))
/* 35 */       .apply(i, TriggerInstance::new)).validate(TriggerInstance::validate);
/*    */   
/*    */   private static DataResult<TriggerInstance> validate(TriggerInstance trigger) {
/* 38 */     return (DataResult)trigger.block.flatMap(block -> 
/* 39 */         trigger.state.flatMap(())
/* 40 */         .map(()))
/* 41 */       .orElseGet(() -> DataResult.success(trigger));
/*    */   }
/*    */ 
/*    */   
/* 45 */   public static Criterion<TriggerInstance> entersBlock(Block block) { return CriteriaTriggers.ENTER_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.empty())); }
/*    */ 
/*    */   
/*    */   public boolean matches(BlockState state) {
/* 49 */     if (this.block.isPresent() && !state.is((Holder)this.block.get())) {
/* 50 */       return false;
/*    */     }
/* 52 */     if (this.state.isPresent() && !((StatePropertiesPredicate)this.state.get()).matches(state)) {
/* 53 */       return false;
/*    */     }
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EnterBlockTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */