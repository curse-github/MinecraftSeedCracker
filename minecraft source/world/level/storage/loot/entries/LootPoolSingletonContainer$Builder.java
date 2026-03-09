/*     */ package net.minecraft.world.level.storage.loot.entries;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
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
/*     */ public abstract class Builder<T extends LootPoolSingletonContainer.Builder<T>>
/*     */   extends LootPoolEntryContainer.Builder<T>
/*     */   implements FunctionUserBuilder<T>
/*     */ {
/*  84 */   protected int weight = 1;
/*  85 */   protected int quality = 0;
/*     */   
/*  87 */   private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */ 
/*     */   
/*     */   public T apply(LootItemFunction.Builder function) {
/*  91 */     this.functions.add(function.build());
/*  92 */     return (T)(Builder)getThis();
/*     */   }
/*     */ 
/*     */   
/*  96 */   protected List<LootItemFunction> getFunctions() { return this.functions.build(); }
/*     */ 
/*     */   
/*     */   public T setWeight(int weight) {
/* 100 */     this.weight = weight;
/* 101 */     return (T)(Builder)getThis();
/*     */   }
/*     */   
/*     */   public T setQuality(int quality) {
/* 105 */     this.quality = quality;
/* 106 */     return (T)(Builder)getThis();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */