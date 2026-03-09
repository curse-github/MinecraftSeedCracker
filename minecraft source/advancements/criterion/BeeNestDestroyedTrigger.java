/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BeeNestDestroyedTrigger
/*    */   extends SimpleCriterionTrigger<BeeNestDestroyedTrigger.TriggerInstance> {
/* 19 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void trigger(ServerPlayer player, BlockState state, ItemStack itemStack, int numBeesInside) { trigger(player, t -> t.matches(state, itemStack, numBeesInside)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<Holder<Block>> block; private final Optional<ItemPredicate> item; private final MinMaxBounds.Ints beesInside;
/*    */     
/* 26 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Holder<Block>> block, Optional<ItemPredicate> item, MinMaxBounds.Ints beesInside) { this.player = player; this.block = block; this.item = item; this.beesInside = beesInside; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/BeeNestDestroyedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 26 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/BeeNestDestroyedTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/BeeNestDestroyedTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/BeeNestDestroyedTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/BeeNestDestroyedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/BeeNestDestroyedTrigger$TriggerInstance;
/* 26 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Holder<Block>> block() { return this.block; } public Optional<ItemPredicate> item() { return this.item; } public MinMaxBounds.Ints beesInside() { return this.beesInside; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 33 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), BuiltInRegistries.BLOCK
/* 34 */           .holderByNameCodec().optionalFieldOf("block").forGetter(TriggerInstance::block), ItemPredicate.CODEC
/* 35 */           .optionalFieldOf("item").forGetter(TriggerInstance::item), MinMaxBounds.Ints.CODEC
/* 36 */           .optionalFieldOf("num_bees_inside", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::beesInside))
/* 37 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 40 */     public static Criterion<TriggerInstance> destroyedBeeNest(Block block, ItemPredicate.Builder itemPredicate, MinMaxBounds.Ints numBeesInside) { return CriteriaTriggers.BEE_NEST_DESTROYED.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.of(itemPredicate.build()), numBeesInside)); }
/*    */ 
/*    */     
/*    */     public boolean matches(BlockState state, ItemStack itemStack, int numBeesInside) {
/* 44 */       if (this.block.isPresent() && !state.is((Holder)this.block.get())) {
/* 45 */         return false;
/*    */       }
/* 47 */       if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(itemStack)) {
/* 48 */         return false;
/*    */       }
/* 50 */       return this.beesInside.matches(numBeesInside);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\BeeNestDestroyedTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */