/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TagPredicate
/*     */   implements BlockPredicateArgument.Result
/*     */ {
/*     */   private final HolderSet<Block> tag;
/*     */   private final CompoundTag nbt;
/*     */   private final Map<String, String> vagueProperties;
/*     */   
/*     */   private TagPredicate(HolderSet<Block> tag, Map<String, String> vagueProperties, CompoundTag nbt) {
/* 118 */     this.tag = tag;
/* 119 */     this.vagueProperties = vagueProperties;
/* 120 */     this.nbt = nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean test(BlockInWorld blockInWorld) {
/* 125 */     BlockState state = blockInWorld.getState();
/*     */     
/* 127 */     if (!state.is(this.tag)) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     for (Map.Entry<String, String> entry : this.vagueProperties.entrySet()) {
/* 132 */       Property<?> property = state.getBlock().getStateDefinition().getProperty((String)entry.getKey());
/* 133 */       if (property == null) {
/* 134 */         return false;
/*     */       }
/* 136 */       Comparable<?> value = (Comparable)property.getValue((String)entry.getValue()).orElse(null);
/* 137 */       if (value == null) {
/* 138 */         return false;
/*     */       }
/* 140 */       if (state.getValue(property) != value) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     if (this.nbt != null) {
/* 146 */       BlockEntity entity = blockInWorld.getEntity();
/* 147 */       return (entity != null && NbtUtils.compareNbt(this.nbt, entity.saveWithFullMetadata(blockInWorld.getLevel().registryAccess()), true));
/*     */     } 
/*     */     
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public boolean requiresNbt() { return (this.nbt != null); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument$TagPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */