/*     */ package net.minecraft.world.level.storage.loot;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class LootContext {
/*     */   private final LootParams params;
/*     */   private final RandomSource random;
/*     */   private final HolderGetter.Provider lootDataResolver;
/*     */   private final Set<VisitedEntry<?>> visitedElements;
/*     */   
/*     */   private LootContext(LootParams params, RandomSource random, HolderGetter.Provider lootDataResolver) {
/*  28 */     this.visitedElements = Sets.newLinkedHashSet();
/*     */ 
/*     */     
/*  31 */     this.params = params;
/*  32 */     this.random = random;
/*  33 */     this.lootDataResolver = lootDataResolver;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public boolean hasParameter(ContextKey<?> key) { return this.params.contextMap().has(key); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public <T> T getParameter(ContextKey<T> key) { return (T)this.params.contextMap().getOrThrow(key); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public <T> T getOptionalParameter(ContextKey<T> key) { return (T)this.params.contextMap().getOptional(key); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public void addDynamicDrops(Identifier location, Consumer<ItemStack> output) { this.params.addDynamicDrops(location, output); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public boolean hasVisitedElement(VisitedEntry<?> element) { return this.visitedElements.contains(element); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public boolean pushVisitedElement(VisitedEntry<?> element) { return this.visitedElements.add(element); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public void popVisitedElement(VisitedEntry<?> element) { this.visitedElements.remove(element); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public HolderGetter.Provider getResolver() { return this.lootDataResolver; }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public RandomSource getRandom() { return this.random; }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public float getLuck() { return this.params.getLuck(); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public ServerLevel getLevel() { return this.params.getLevel(); }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final LootParams params;
/*     */     private RandomSource random;
/*     */     
/*  85 */     public Builder(LootParams params) { this.params = params; }
/*     */ 
/*     */     
/*     */     public Builder withOptionalRandomSeed(long seed) {
/*  89 */       if (seed != 0L) {
/*  90 */         this.random = RandomSource.create(seed);
/*     */       }
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptionalRandomSource(RandomSource randomSource) {
/*  96 */       this.random = randomSource;
/*  97 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 101 */     public ServerLevel getLevel() { return this.params.getLevel(); }
/*     */ 
/*     */     
/*     */     public LootContext create(Optional<Identifier> randomSequenceKey) {
/* 105 */       ServerLevel level = getLevel();
/* 106 */       MinecraftServer server = level.getServer();
/*     */ 
/*     */       
/* 109 */       Objects.requireNonNull(level); RandomSource random = (RandomSource)Optional.ofNullable(this.random).or(() -> { Objects.requireNonNull(level); return randomSequenceKey.map(level::getRandomSequence); }).orElseGet(level::getRandom);
/* 110 */       return new LootContext(this.params, random, server.reloadableRegistries().lookup());
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EntityTarget implements StringRepresentable, LootContextArg.SimpleGetter<Entity> {
/* 115 */     THIS("this", LootContextParams.THIS_ENTITY),
/* 116 */     ATTACKER("attacker", LootContextParams.ATTACKING_ENTITY),
/* 117 */     DIRECT_ATTACKER("direct_attacker", LootContextParams.DIRECT_ATTACKING_ENTITY),
/* 118 */     ATTACKING_PLAYER("attacking_player", LootContextParams.LAST_DAMAGE_PLAYER),
/* 119 */     TARGET_ENTITY("target_entity", LootContextParams.TARGET_ENTITY),
/* 120 */     INTERACTING_ENTITY("interacting_entity", LootContextParams.INTERACTING_ENTITY); public static final StringRepresentable.EnumCodec<EntityTarget> CODEC; private final String name; private final ContextKey<? extends Entity> param;
/*     */     
/*     */     static  {
/* 123 */       CODEC = StringRepresentable.fromEnum(EntityTarget::values);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     EntityTarget(String name, ContextKey<? extends Entity> param) {
/* 130 */       this.name = name;
/* 131 */       this.param = param;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 136 */     public ContextKey<? extends Entity> contextParam() { return this.param; }
/*     */ 
/*     */     
/*     */     public static EntityTarget getByName(String name) {
/* 140 */       EntityTarget target = (EntityTarget)CODEC.byName(name);
/* 141 */       if (target != null) {
/* 142 */         return target;
/*     */       }
/* 144 */       throw new IllegalArgumentException("Invalid entity target " + name);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 149 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */   
/*     */   public enum BlockEntityTarget
/*     */     implements StringRepresentable, LootContextArg.SimpleGetter<BlockEntity> {
/* 154 */     BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);
/*     */     
/*     */     private final ContextKey<? extends BlockEntity> param;
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     BlockEntityTarget(String name, ContextKey<? extends BlockEntity> param) {
/* 162 */       this.name = name;
/* 163 */       this.param = param;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 168 */     public ContextKey<? extends BlockEntity> contextParam() { return this.param; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */   
/*     */   public enum ItemStackTarget
/*     */     implements StringRepresentable, LootContextArg.SimpleGetter<ItemStack> {
/* 178 */     TOOL("tool", LootContextParams.TOOL);
/*     */     
/*     */     private final ContextKey<? extends ItemStack> param;
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     ItemStackTarget(String name, ContextKey<? extends ItemStack> param) {
/* 186 */       this.name = name;
/* 187 */       this.param = param;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 192 */     public ContextKey<? extends ItemStack> contextParam() { return this.param; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 202 */   public static VisitedEntry<LootTable> createVisitedEntry(LootTable table) { return new VisitedEntry(LootDataType.TABLE, table); }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public static VisitedEntry<LootItemCondition> createVisitedEntry(LootItemCondition table) { return new VisitedEntry(LootDataType.PREDICATE, table); }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public static VisitedEntry<LootItemFunction> createVisitedEntry(LootItemFunction table) { return new VisitedEntry(LootDataType.MODIFIER, table); }
/*     */   public static final class VisitedEntry<T> extends Record { private final LootDataType<T> type; private final T value;
/*     */     
/* 213 */     public VisitedEntry(LootDataType<T> type, T value) { this.type = type; this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #213	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 213 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry<TT;>; } public LootDataType<T> type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #213	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #213	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 213 */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry<TT;>; } public T value() { return (T)this.value; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */