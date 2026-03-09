/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
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
/*     */   extends Object
/*     */   implements FunctionUserBuilder<LootTable.Builder>
/*     */ {
/* 219 */   private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();
/*     */   
/* 221 */   private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */   
/* 223 */   private ContextKeySet paramSet = LootTable.DEFAULT_PARAM_SET;
/* 224 */   private Optional<Identifier> randomSequence = Optional.empty();
/*     */   
/*     */   public Builder withPool(LootPool.Builder pool) {
/* 227 */     this.pools.add(pool.build());
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setParamSet(ContextKeySet paramSet) {
/* 232 */     this.paramSet = paramSet;
/* 233 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setRandomSequence(Identifier key) {
/* 237 */     this.randomSequence = Optional.of(key);
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Builder apply(LootItemFunction.Builder function) {
/* 243 */     this.functions.add(function.build());
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 249 */   public Builder unwrap() { return this; }
/*     */ 
/*     */ 
/*     */   
/* 253 */   public LootTable build() { return new LootTable(this.paramSet, this.randomSequence, this.pools.build(), this.functions.build()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootTable$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */