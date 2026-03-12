/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class ShotCrossbowTrigger
/*    */   extends SimpleCriterionTrigger<ShotCrossbowTrigger.TriggerInstance> {
/* 18 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void trigger(ServerPlayer player, ItemStack itemStack) { trigger(player, t -> t.matches(itemStack)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ItemPredicate> item;
/*    */     
/* 25 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) { this.player = player; this.item = item; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ShotCrossbowTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 25 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ShotCrossbowTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ShotCrossbowTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ShotCrossbowTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ShotCrossbowTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/ShotCrossbowTrigger$TriggerInstance;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ItemPredicate> item() { return this.item; }
/*    */ 
/*    */ 
/*    */     
/* 29 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 30 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 31 */           .optionalFieldOf("item").forGetter(TriggerInstance::item))
/* 32 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 35 */     public static Criterion<TriggerInstance> shotCrossbow(Optional<ItemPredicate> item) { return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new TriggerInstance(Optional.empty(), item)); }
/*    */ 
/*    */ 
/*    */     
/* 39 */     public static Criterion<TriggerInstance> shotCrossbow(HolderGetter<Item> items, ItemLike itemlike) { return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(items, new ItemLike[] { itemlike }).build()))); }
/*    */ 
/*    */ 
/*    */     
/* 43 */     public boolean matches(ItemStack itemStack) { return (this.item.isEmpty() || ((ItemPredicate)this.item.get()).test(itemStack)); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ShotCrossbowTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */