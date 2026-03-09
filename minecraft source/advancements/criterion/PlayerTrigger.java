/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class PlayerTrigger
/*    */   extends SimpleCriterionTrigger<PlayerTrigger.TriggerInstance>
/*    */ {
/* 19 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void trigger(ServerPlayer player) { trigger(player, t -> true); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player;
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerTrigger$TriggerInstance; }
/* 26 */     public TriggerInstance(Optional<ContextAwarePredicate> player) { this.player = player; } public Optional<ContextAwarePredicate> player() { return this.player; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerTrigger$TriggerInstance; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 29 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 30 */           .optionalFieldOf("player").forGetter(TriggerInstance::player))
/* 31 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 34 */     public static Criterion<TriggerInstance> located(LocationPredicate.Builder location) { return CriteriaTriggers.LOCATION.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().located(location))))); }
/*    */ 
/*    */ 
/*    */     
/* 38 */     public static Criterion<TriggerInstance> located(EntityPredicate.Builder player) { return CriteriaTriggers.LOCATION.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player.build())))); }
/*    */ 
/*    */ 
/*    */     
/* 42 */     public static Criterion<TriggerInstance> located(Optional<EntityPredicate> player) { return CriteriaTriggers.LOCATION.createCriterion(new TriggerInstance(EntityPredicate.wrap(player))); }
/*    */ 
/*    */ 
/*    */     
/* 46 */     public static Criterion<TriggerInstance> sleptInBed() { return CriteriaTriggers.SLEPT_IN_BED.createCriterion(new TriggerInstance(Optional.empty())); }
/*    */ 
/*    */ 
/*    */     
/* 50 */     public static Criterion<TriggerInstance> raidWon() { return CriteriaTriggers.RAID_WIN.createCriterion(new TriggerInstance(Optional.empty())); }
/*    */ 
/*    */ 
/*    */     
/* 54 */     public static Criterion<TriggerInstance> avoidVibration() { return CriteriaTriggers.AVOID_VIBRATION.createCriterion(new TriggerInstance(Optional.empty())); }
/*    */ 
/*    */ 
/*    */     
/* 58 */     public static Criterion<TriggerInstance> tick() { return CriteriaTriggers.TICK.createCriterion(new TriggerInstance(Optional.empty())); }
/*    */ 
/*    */     
/*    */     public static Criterion<TriggerInstance> walkOnBlockWithEquipment(HolderGetter<Block> blocks, HolderGetter<Item> items, Block stepOnBlock, Item requiredEquipment) {
/* 62 */       return located(EntityPredicate.Builder.entity()
/* 63 */           .equipment(
/* 64 */             EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of(items, new ItemLike[] { requiredEquipment
/* 65 */                 }))).steppingOn(
/* 66 */             LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { stepOnBlock }))));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */