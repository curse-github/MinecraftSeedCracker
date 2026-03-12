/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ 
/*     */ 
/*     */ public final class GameRuleMap
/*     */ {
/*  16 */   public static final Codec<GameRuleMap> CODEC = Codec.dispatchedMap(BuiltInRegistries.GAME_RULE.byNameCodec(), GameRule::valueCodec).xmap(GameRuleMap::ofTrusted, GameRuleMap::map);
/*     */   
/*     */   private final Reference2ObjectMap<GameRule<?>, Object> map;
/*     */ 
/*     */   
/*  21 */   private GameRuleMap(Reference2ObjectMap<GameRule<?>, Object> map) { this.map = map; }
/*     */ 
/*     */ 
/*     */   
/*  25 */   private static GameRuleMap ofTrusted(Map<GameRule<?>, Object> map) { return new GameRuleMap(new Reference2ObjectOpenHashMap(map)); }
/*     */ 
/*     */ 
/*     */   
/*  29 */   public static GameRuleMap of() { return new GameRuleMap(new Reference2ObjectOpenHashMap()); }
/*     */ 
/*     */   
/*     */   public static GameRuleMap of(Stream<GameRule<?>> gameRuleTypeStream) {
/*  33 */     Reference2ObjectOpenHashMap<GameRule<?>, Object> map = new Reference2ObjectOpenHashMap<GameRule<?>, Object>();
/*  34 */     gameRuleTypeStream.forEach(gameRule -> map.put(gameRule, gameRule.defaultValue()));
/*  35 */     return new GameRuleMap(map);
/*     */   }
/*     */ 
/*     */   
/*  39 */   public static GameRuleMap copyOf(GameRuleMap gameRuleMap) { return new GameRuleMap(new Reference2ObjectOpenHashMap(gameRuleMap.map)); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public boolean has(GameRule<?> gameRule) { return this.map.containsKey(gameRule); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public <T> T get(GameRule<T> gameRule) { return (T)this.map.get(gameRule); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public <T> void set(GameRule<T> gameRule, T value) { this.map.put(gameRule, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public <T> T remove(GameRule<T> gameRule) { return (T)this.map.remove(gameRule); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public Set<GameRule<?>> keySet() { return this.map.keySet(); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public int size() { return this.map.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public String toString() { return this.map.toString(); }
/*     */ 
/*     */   
/*     */   public GameRuleMap withOther(GameRuleMap other) {
/*  74 */     GameRuleMap result = copyOf(this);
/*  75 */     result.setFromIf(other, r -> true);
/*  76 */     return result;
/*     */   }
/*     */   
/*     */   public void setFromIf(GameRuleMap other, Predicate<GameRule<?>> predicate) {
/*  80 */     for (GameRule<?> gameRule : other.keySet()) {
/*  81 */       if (predicate.test(gameRule)) {
/*  82 */         setGameRule(other, gameRule, this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  88 */   private static <T> void setGameRule(GameRuleMap other, GameRule<T> gameRule, GameRuleMap result) { result.set(gameRule, Objects.requireNonNull(other.get(gameRule))); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   private Reference2ObjectMap<GameRule<?>, Object> map() { return this.map; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  97 */     if (obj == this) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (obj == null || obj.getClass() != getClass()) {
/* 101 */       return false;
/*     */     }
/* 103 */     GameRuleMap that = (GameRuleMap)obj;
/* 104 */     return Objects.equals(this.map, that.map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public int hashCode() { return Objects.hash(new Object[] { this.map }); }
/*     */   
/*     */   public static class Builder
/*     */   {
/* 113 */     final Reference2ObjectMap<GameRule<?>, Object> map = new Reference2ObjectOpenHashMap();
/*     */     
/*     */     public <T> Builder set(GameRule<T> gameRule, T value) {
/* 116 */       this.map.put(gameRule, value);
/* 117 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 121 */     public GameRuleMap build() { return new GameRuleMap(this.map); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gamerules\GameRuleMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */