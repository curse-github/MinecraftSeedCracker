/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final LootParams params;
/*     */   private RandomSource random;
/*     */   
/*  85 */   public Builder(LootParams params) { this.params = params; }
/*     */ 
/*     */   
/*     */   public Builder withOptionalRandomSeed(long seed) {
/*  89 */     if (seed != 0L) {
/*  90 */       this.random = RandomSource.create(seed);
/*     */     }
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOptionalRandomSource(RandomSource randomSource) {
/*  96 */     this.random = randomSource;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 101 */   public ServerLevel getLevel() { return this.params.getLevel(); }
/*     */ 
/*     */   
/*     */   public LootContext create(Optional<Identifier> randomSequenceKey) {
/* 105 */     ServerLevel level = getLevel();
/* 106 */     MinecraftServer server = level.getServer();
/*     */ 
/*     */     
/* 109 */     Objects.requireNonNull(level); RandomSource random = (RandomSource)Optional.ofNullable(this.random).or(() -> { Objects.requireNonNull(level); return randomSequenceKey.map(level::getRandomSequence); }).orElseGet(level::getRandom);
/* 110 */     return new LootContext(this.params, random, server.reloadableRegistries().lookup());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContext$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */