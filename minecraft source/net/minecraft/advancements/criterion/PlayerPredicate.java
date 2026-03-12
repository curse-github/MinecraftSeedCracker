/*     */ package net.minecraft.advancements.criterion;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerAdvancementManager;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.ServerRecipeBook;
/*     */ import net.minecraft.stats.ServerStatsCounter;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class PlayerPredicate extends Record implements EntitySubPredicate {
/*     */   private final MinMaxBounds.Ints level;
/*     */   private final GameTypePredicate gameType;
/*     */   private final List<StatMatcher<?>> stats;
/*     */   private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes;
/*     */   
/*  44 */   public PlayerPredicate(MinMaxBounds.Ints level, GameTypePredicate gameType, List<StatMatcher<?>> stats, Object2BooleanMap<ResourceKey<Recipe<?>>> recipes, Map<Identifier, AdvancementPredicate> advancements, Optional<EntityPredicate> lookingAt, Optional<InputPredicate> input) { this.level = level; this.gameType = gameType; this.stats = stats; this.recipes = recipes; this.advancements = advancements; this.lookingAt = lookingAt; this.input = input; } private final Map<Identifier, AdvancementPredicate> advancements; private final Optional<EntityPredicate> lookingAt; private final Optional<InputPredicate> input; public static final int LOOKING_AT_RANGE = 100; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate;
/*  44 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints level() { return this.level; } public GameTypePredicate gameType() { return this.gameType; } public List<StatMatcher<?>> stats() { return this.stats; } public Object2BooleanMap<ResourceKey<Recipe<?>>> recipes() { return this.recipes; } public Map<Identifier, AdvancementPredicate> advancements() { return this.advancements; } public Optional<EntityPredicate> lookingAt() { return this.lookingAt; } public Optional<InputPredicate> input() { return this.input; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final MapCodec<PlayerPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(MinMaxBounds.Ints.CODEC
/*  56 */         .optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(PlayerPredicate::level), GameTypePredicate.CODEC
/*  57 */         .optionalFieldOf("gamemode", GameTypePredicate.ANY).forGetter(PlayerPredicate::gameType), StatMatcher.CODEC
/*  58 */         .listOf().optionalFieldOf("stats", List.of()).forGetter(PlayerPredicate::stats), 
/*  59 */         ExtraCodecs.object2BooleanMap(Recipe.KEY_CODEC).optionalFieldOf("recipes", Object2BooleanMaps.emptyMap()).forGetter(PlayerPredicate::recipes), 
/*  60 */         Codec.unboundedMap(Identifier.CODEC, AdvancementPredicate.CODEC).optionalFieldOf("advancements", Map.of()).forGetter(PlayerPredicate::advancements), EntityPredicate.CODEC
/*  61 */         .optionalFieldOf("looking_at").forGetter(PlayerPredicate::lookingAt), InputPredicate.CODEC
/*  62 */         .optionalFieldOf("input").forGetter(PlayerPredicate::input))
/*  63 */       .apply(i, PlayerPredicate::new));
/*     */   
/*     */   private static interface AdvancementPredicate extends Predicate<AdvancementProgress> {
/*  66 */     public static final Codec<AdvancementPredicate> CODEC = Codec.either(PlayerPredicate.AdvancementDonePredicate.CODEC, PlayerPredicate.AdvancementCriterionsPredicate.CODEC).xmap(Either::unwrap, predicate -> {
/*     */ 
/*     */           
/*  69 */           if (predicate instanceof PlayerPredicate.AdvancementDonePredicate) { PlayerPredicate.AdvancementDonePredicate done = (PlayerPredicate.AdvancementDonePredicate)predicate;
/*  70 */             return Either.left(done); }
/*  71 */            if (predicate instanceof PlayerPredicate.AdvancementCriterionsPredicate) { PlayerPredicate.AdvancementCriterionsPredicate criterions = (PlayerPredicate.AdvancementCriterionsPredicate)predicate;
/*  72 */             return Either.right(criterions); }
/*     */           
/*  74 */           throw new UnsupportedOperationException();
/*     */         }); }
/*     */   
/*     */   private static final class AdvancementDonePredicate extends Record implements AdvancementPredicate { private final boolean state;
/*     */     
/*  79 */     private AdvancementDonePredicate(boolean state) { this.state = state; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementDonePredicate;
/*  79 */       //   0	8	1	o	Ljava/lang/Object; } public boolean state() { return this.state; }
/*  80 */     public static final Codec<AdvancementDonePredicate> CODEC = Codec.BOOL.xmap(AdvancementDonePredicate::new, AdvancementDonePredicate::state);
/*     */ 
/*     */ 
/*     */     
/*  84 */     public boolean test(AdvancementProgress progress) { return (progress.isDone() == this.state); } }
/*     */   
/*     */   private static final class AdvancementCriterionsPredicate extends Record implements AdvancementPredicate { private final Object2BooleanMap<String> criterions;
/*     */     
/*  88 */     private AdvancementCriterionsPredicate(Object2BooleanMap<String> criterions) { this.criterions = criterions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementCriterionsPredicate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementCriterionsPredicate; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementCriterionsPredicate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementCriterionsPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementCriterionsPredicate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$AdvancementCriterionsPredicate;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public Object2BooleanMap<String> criterions() { return this.criterions; }
/*  89 */     public static final Codec<AdvancementCriterionsPredicate> CODEC = ExtraCodecs.object2BooleanMap(Codec.STRING).xmap(AdvancementCriterionsPredicate::new, AdvancementCriterionsPredicate::criterions);
/*     */ 
/*     */     
/*     */     public boolean test(AdvancementProgress progress) {
/*  93 */       for (ObjectIterator objectIterator = this.criterions.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) { Object2BooleanMap.Entry<String> e = (Object2BooleanMap.Entry)objectIterator.next();
/*  94 */         CriterionProgress criterion = progress.getCriterion((String)e.getKey());
/*  95 */         if (criterion == null || criterion.isDone() != e.getBooleanValue()) {
/*  96 */           return false;
/*     */         } }
/*     */       
/*  99 */       return true;
/*     */     } }
/*     */ 
/*     */   
/*     */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/*     */     ServerPlayer player;
/* 105 */     if (entity instanceof ServerPlayer) { player = (ServerPlayer)entity; }
/* 106 */     else { return false; }
/*     */ 
/*     */     
/* 109 */     if (!this.level.matches(player.experienceLevel)) {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     if (!this.gameType.matches(player.gameMode())) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     ServerStatsCounter serverStatsCounter = player.getStats();
/* 118 */     for (StatMatcher<?> stat : this.stats) {
/* 119 */       if (!stat.matches(serverStatsCounter)) {
/* 120 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 124 */     ServerRecipeBook recipes = player.getRecipeBook();
/* 125 */     for (ObjectIterator objectIterator = this.recipes.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) { Object2BooleanMap.Entry<ResourceKey<Recipe<?>>> e = (Object2BooleanMap.Entry)objectIterator.next();
/* 126 */       if (recipes.contains((ResourceKey)e.getKey()) != e.getBooleanValue()) {
/* 127 */         return false;
/*     */       } }
/*     */ 
/*     */     
/* 131 */     if (!this.advancements.isEmpty()) {
/* 132 */       PlayerAdvancements advancements = player.getAdvancements();
/* 133 */       ServerAdvancementManager serverAdvancements = player.level().getServer().getAdvancements();
/*     */       
/* 135 */       for (Map.Entry<Identifier, AdvancementPredicate> entry : this.advancements.entrySet()) {
/* 136 */         AdvancementHolder advancement = serverAdvancements.get((Identifier)entry.getKey());
/* 137 */         if (advancement == null || !((AdvancementPredicate)entry.getValue()).test(advancements.getOrStartProgress(advancement))) {
/* 138 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     if (this.lookingAt.isPresent()) {
/* 144 */       Vec3 from = player.getEyePosition();
/* 145 */       Vec3 viewVec = player.getViewVector(1.0F);
/* 146 */       Vec3 to = from.add(viewVec.x * 100.0D, viewVec.y * 100.0D, viewVec.z * 100.0D);
/* 147 */       EntityHitResult lookingAtResult = ProjectileUtil.getEntityHitResult(player.level(), player, from, to, (new AABB(from, to)).inflate(1.0D), e -> !e.isSpectator(), 0.0F);
/* 148 */       if (lookingAtResult == null || lookingAtResult.getType() != HitResult.Type.ENTITY) {
/* 149 */         return false;
/*     */       }
/* 151 */       Entity lookingAtEntity = lookingAtResult.getEntity();
/* 152 */       if (!((EntityPredicate)this.lookingAt.get()).matches(player, lookingAtEntity) || !player.hasLineOfSight(lookingAtEntity)) {
/* 153 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 158 */     if (this.input.isPresent() && !((InputPredicate)this.input.get()).matches(player.getLastClientInput())) {
/* 159 */       return false;
/*     */     }
/*     */     
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public MapCodec<PlayerPredicate> codec() { return EntitySubPredicates.PLAYER; }
/*     */   private static final class StatMatcher<T> extends Record { private final StatType<T> type; private final Holder<T> value; private final MinMaxBounds.Ints range; private final Supplier<Stat<T>> stat;
/*     */     
/* 170 */     private StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range, Supplier<Stat<T>> stat) { this.type = type; this.value = value; this.range = range; this.stat = stat; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #170	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #170	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #170	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 170 */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerPredicate$StatMatcher<TT;>; } public StatType<T> type() { return this.type; } public Holder<T> value() { return this.value; } public MinMaxBounds.Ints range() { return this.range; } public Supplier<Stat<T>> stat() { return this.stat; }
/* 171 */     public static final Codec<StatMatcher<?>> CODEC = BuiltInRegistries.STAT_TYPE.byNameCodec().dispatch(StatMatcher::type, StatMatcher::createTypedCodec);
/*     */     
/*     */     private static <T> MapCodec<StatMatcher<T>> createTypedCodec(StatType<T> type) {
/* 174 */       return RecordCodecBuilder.mapCodec(i -> i.group(type
/* 175 */             .getRegistry().holderByNameCodec().fieldOf("stat").forGetter(StatMatcher::value), MinMaxBounds.Ints.CODEC
/* 176 */             .optionalFieldOf("value", MinMaxBounds.Ints.ANY).forGetter(StatMatcher::range))
/* 177 */           .apply(i, ()));
/*     */     }
/*     */ 
/*     */     
/* 181 */     public StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range) { this(type, value, range, Suppliers.memoize(() -> type.get(value.value()))); }
/*     */ 
/*     */ 
/*     */     
/* 185 */     public boolean matches(StatsCounter counter) { return this.range.matches(counter.getValue((Stat)this.stat.get())); } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 190 */     private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/* 191 */     private GameTypePredicate gameType = GameTypePredicate.ANY;
/* 192 */     private final ImmutableList.Builder<PlayerPredicate.StatMatcher<?>> stats = ImmutableList.builder();
/* 193 */     private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes = new Object2BooleanOpenHashMap();
/* 194 */     private final Map<Identifier, PlayerPredicate.AdvancementPredicate> advancements = Maps.newHashMap();
/* 195 */     private Optional<EntityPredicate> lookingAt = Optional.empty();
/* 196 */     private Optional<InputPredicate> input = Optional.empty();
/*     */ 
/*     */     
/* 199 */     public static Builder player() { return new Builder(); }
/*     */ 
/*     */     
/*     */     public Builder setLevel(MinMaxBounds.Ints level) {
/* 203 */       this.level = level;
/* 204 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder addStat(StatType<T> type, Holder.Reference<T> value, MinMaxBounds.Ints range) {
/* 208 */       this.stats.add(new PlayerPredicate.StatMatcher(type, value, range));
/* 209 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addRecipe(ResourceKey<Recipe<?>> recipe, boolean present) {
/* 213 */       this.recipes.put(recipe, present);
/* 214 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setGameType(GameTypePredicate gameType) {
/* 218 */       this.gameType = gameType;
/* 219 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLookingAt(EntityPredicate.Builder lookingAt) {
/* 223 */       this.lookingAt = Optional.of(lookingAt.build());
/* 224 */       return this;
/*     */     }
/*     */     
/*     */     public Builder checkAdvancementDone(Identifier advancement, boolean isDone) {
/* 228 */       this.advancements.put(advancement, new PlayerPredicate.AdvancementDonePredicate(isDone));
/* 229 */       return this;
/*     */     }
/*     */     
/*     */     public Builder checkAdvancementCriterions(Identifier advancement, Map<String, Boolean> criterions) {
/* 233 */       this.advancements.put(advancement, new PlayerPredicate.AdvancementCriterionsPredicate(new Object2BooleanOpenHashMap(criterions)));
/* 234 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hasInput(InputPredicate input) {
/* 238 */       this.input = Optional.of(input);
/* 239 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 243 */     public PlayerPredicate build() { return new PlayerPredicate(this.level, this.gameType, this.stats.build(), this.recipes, this.advancements, this.lookingAt, this.input); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */