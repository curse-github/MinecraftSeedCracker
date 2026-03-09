/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ 
/*     */ public class KilledByArrowTrigger
/*     */   extends SimpleCriterionTrigger<KilledByArrowTrigger.TriggerInstance> {
/*  28 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*     */ 
/*     */   
/*     */   public void trigger(ServerPlayer player, Collection<Entity> victims, ItemStack firedByWeapon) {
/*  32 */     List<LootContext> victimContexts = Lists.newArrayList();
/*  33 */     Set<EntityType<?>> entityTypes = Sets.newHashSet();
/*  34 */     for (Entity victim : victims) {
/*  35 */       entityTypes.add(victim.getType());
/*  36 */       victimContexts.add(EntityPredicate.createContext(player, victim));
/*     */     } 
/*     */     
/*  39 */     trigger(player, t -> t.matches(victimContexts, entityTypes.size(), firedByWeapon));
/*     */   }
/*     */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final List<ContextAwarePredicate> victims; private final MinMaxBounds.Ints uniqueEntityTypes; private final Optional<ItemPredicate> firedFromWeapon;
/*  42 */     public TriggerInstance(Optional<ContextAwarePredicate> player, List<ContextAwarePredicate> victims, MinMaxBounds.Ints uniqueEntityTypes, Optional<ItemPredicate> firedFromWeapon) { this.player = player; this.victims = victims; this.uniqueEntityTypes = uniqueEntityTypes; this.firedFromWeapon = firedFromWeapon; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/KilledByArrowTrigger$TriggerInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  42 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/KilledByArrowTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/KilledByArrowTrigger$TriggerInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/KilledByArrowTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/KilledByArrowTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/KilledByArrowTrigger$TriggerInstance;
/*  42 */       //   0	8	1	o	Ljava/lang/Object; } public List<ContextAwarePredicate> victims() { return this.victims; } public MinMaxBounds.Ints uniqueEntityTypes() { return this.uniqueEntityTypes; } public Optional<ItemPredicate> firedFromWeapon() { return this.firedFromWeapon; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/*  49 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/*  50 */           .listOf().optionalFieldOf("victims", List.of()).forGetter(TriggerInstance::victims), MinMaxBounds.Ints.CODEC
/*  51 */           .optionalFieldOf("unique_entity_types", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::uniqueEntityTypes), ItemPredicate.CODEC
/*  52 */           .optionalFieldOf("fired_from_weapon").forGetter(TriggerInstance::firedFromWeapon))
/*  53 */         .apply(i, TriggerInstance::new));
/*     */     
/*     */     public static Criterion<TriggerInstance> crossbowKilled(HolderGetter<Item> items, Builder... victims) {
/*  56 */       return CriteriaTriggers.KILLED_BY_ARROW.createCriterion(new TriggerInstance(
/*  57 */             Optional.empty(), 
/*  58 */             EntityPredicate.wrap(victims), MinMaxBounds.Ints.ANY, 
/*     */             
/*  60 */             Optional.of(ItemPredicate.Builder.item()
/*  61 */               .of(items, new ItemLike[] { Items.CROSSBOW
/*  62 */                 }).build())));
/*     */     }
/*     */ 
/*     */     
/*     */     public static Criterion<TriggerInstance> crossbowKilled(HolderGetter<Item> items, MinMaxBounds.Ints uniqueEntityTypes) {
/*  67 */       return CriteriaTriggers.KILLED_BY_ARROW.createCriterion(new TriggerInstance(
/*  68 */             Optional.empty(), 
/*  69 */             List.of(), uniqueEntityTypes, 
/*     */             
/*  71 */             Optional.of(ItemPredicate.Builder.item()
/*  72 */               .of(items, new ItemLike[] { Items.CROSSBOW
/*  73 */                 }).build())));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Collection<LootContext> victims, int uniqueEntityTypes, ItemStack firedFromWeapon) {
/*  78 */       if (this.firedFromWeapon.isPresent() && (
/*  79 */         firedFromWeapon == null || !((ItemPredicate)this.firedFromWeapon.get()).test(firedFromWeapon))) {
/*  80 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  84 */       if (!this.victims.isEmpty()) {
/*  85 */         List<LootContext> victimsCopy = Lists.newArrayList(victims);
/*  86 */         for (ContextAwarePredicate predicate : this.victims) {
/*  87 */           boolean found = false;
/*  88 */           for (Iterator<LootContext> iterator = victimsCopy.iterator(); iterator.hasNext(); ) {
/*  89 */             LootContext entity = (LootContext)iterator.next();
/*  90 */             if (predicate.matches(entity)) {
/*  91 */               iterator.remove();
/*  92 */               found = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*  97 */           if (!found) {
/*  98 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       return this.uniqueEntityTypes.matches(uniqueEntityTypes);
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(CriterionValidator validator) {
/* 108 */       super.validate(validator);
/* 109 */       validator.validateEntities(this.victims, "victims");
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\KilledByArrowTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */