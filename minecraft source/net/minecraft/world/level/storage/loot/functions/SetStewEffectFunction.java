/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class SetStewEffectFunction extends LootItemConditionalFunction {
/*  28 */   private static final Codec<List<EffectEntry>> EFFECTS_LIST = EffectEntry.CODEC.listOf().validate(entries -> {
/*  29 */         ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet();
/*  30 */         for (EffectEntry entry : entries) {
/*  31 */           if (!objectOpenHashSet.add(entry.effect())) {
/*  32 */             return DataResult.error(());
/*     */           }
/*     */         } 
/*  35 */         return DataResult.success(entries);
/*     */       });
/*     */   
/*  38 */   public static final MapCodec<SetStewEffectFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(EFFECTS_LIST
/*  39 */         .optionalFieldOf("effects", List.of()).forGetter(()))
/*  40 */       .apply(i, SetStewEffectFunction::new));
/*     */   
/*     */   private final List<EffectEntry> effects;
/*     */   
/*     */   private SetStewEffectFunction(List<LootItemCondition> predicates, List<EffectEntry> effects) {
/*  45 */     super(predicates);
/*  46 */     this.effects = effects;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public LootItemFunctionType<SetStewEffectFunction> getType() { return LootItemFunctions.SET_STEW_EFFECT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public Set<ContextKey<?>> getReferencedContextParams() { return (Set)this.effects.stream().flatMap(p -> p.duration().getReferencedContextParams().stream()).collect(ImmutableSet.toImmutableSet()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  61 */     if (!itemStack.is(Items.SUSPICIOUS_STEW) || this.effects.isEmpty()) {
/*  62 */       return itemStack;
/*     */     }
/*     */     
/*  65 */     EffectEntry entry = (EffectEntry)Util.getRandom(this.effects, context.getRandom());
/*     */     
/*  67 */     Holder<MobEffect> effect = entry.effect();
/*  68 */     int duration = entry.duration().getInt(context);
/*  69 */     if (!((MobEffect)effect.value()).isInstantenous()) {
/*  70 */       duration *= 20;
/*     */     }
/*     */     
/*  73 */     SuspiciousStewEffects.Entry newEntry = new SuspiciousStewEffects.Entry(effect, duration);
/*  74 */     itemStack.update(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY, newEntry, SuspiciousStewEffects::withEffectAdded);
/*     */     
/*  76 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  80 */     private final ImmutableList.Builder<SetStewEffectFunction.EffectEntry> effects = ImmutableList.builder();
/*     */ 
/*     */ 
/*     */     
/*  84 */     protected Builder getThis() { return this; }
/*     */ 
/*     */     
/*     */     public Builder withEffect(Holder<MobEffect> effect, NumberProvider duration) {
/*  88 */       this.effects.add(new SetStewEffectFunction.EffectEntry(effect, duration));
/*  89 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  94 */     public LootItemFunction build() { return new SetStewEffectFunction(getConditions(), this.effects.build()); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static Builder stewEffect() { return new Builder(); }
/*     */   private static final class EffectEntry extends Record { private final Holder<MobEffect> effect; private final NumberProvider duration;
/*     */     
/* 102 */     private EffectEntry(Holder<MobEffect> effect, NumberProvider duration) { this.effect = effect; this.duration = duration; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 102 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry; } public Holder<MobEffect> effect() { return this.effect; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/* 102 */       //   0	8	1	o	Ljava/lang/Object; } public NumberProvider duration() { return this.duration; }
/* 103 */     public static final Codec<EffectEntry> CODEC = RecordCodecBuilder.create(i -> i.group(MobEffect.CODEC
/* 104 */           .fieldOf("type").forGetter(EffectEntry::effect), NumberProviders.CODEC
/* 105 */           .fieldOf("duration").forGetter(EffectEntry::duration))
/* 106 */         .apply(i, EffectEntry::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */