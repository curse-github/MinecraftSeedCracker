/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 102 */   private Optional<HolderSet<Block>> blocks = Optional.empty();
/* 103 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/* 104 */   private Optional<NbtPredicate> nbt = Optional.empty();
/* 105 */   private DataComponentMatchers components = DataComponentMatchers.ANY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static Builder block() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public Builder of(HolderGetter<Block> lookup, Block... blocks) { return of(lookup, Arrays.asList(blocks)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder of(HolderGetter<Block> lookup, Collection<Block> blocks) {
/* 120 */     this.blocks = Optional.of(HolderSet.direct(Block::builtInRegistryHolder, blocks));
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<Block> lookup, TagKey<Block> tag) {
/* 125 */     this.blocks = Optional.of(lookup.getOrThrow(tag));
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasNbt(CompoundTag nbt) {
/* 130 */     this.nbt = Optional.of(new NbtPredicate(nbt));
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setProperties(StatePropertiesPredicate.Builder properties) {
/* 135 */     this.properties = properties.build();
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Builder components(DataComponentMatchers components) {
/* 140 */     this.components = components;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 145 */   public BlockPredicate build() { return new BlockPredicate(this.blocks, this.properties, this.nbt, this.components); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\BlockPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */