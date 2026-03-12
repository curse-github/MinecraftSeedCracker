/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
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
/*     */ class BlockPredicate
/*     */   implements BlockPredicateArgument.Result
/*     */ {
/*     */   private final BlockState state;
/*     */   private final Set<Property<?>> properties;
/*     */   private final CompoundTag nbt;
/*     */   
/*     */   public BlockPredicate(BlockState state, Set<Property<?>> properties, CompoundTag nbt) {
/*  79 */     this.state = state;
/*  80 */     this.properties = properties;
/*  81 */     this.nbt = nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean test(BlockInWorld blockInWorld) {
/*  86 */     BlockState state = blockInWorld.getState();
/*     */     
/*  88 */     if (!state.is(this.state.getBlock())) {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     for (Property<?> property : this.properties) {
/*  93 */       if (state.getValue(property) != this.state.getValue(property)) {
/*  94 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  98 */     if (this.nbt != null) {
/*  99 */       BlockEntity entity = blockInWorld.getEntity();
/* 100 */       return (entity != null && NbtUtils.compareNbt(this.nbt, entity.saveWithFullMetadata(blockInWorld.getLevel().registryAccess()), true));
/*     */     } 
/*     */     
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean requiresNbt() { return (this.nbt != null); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument$BlockPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */