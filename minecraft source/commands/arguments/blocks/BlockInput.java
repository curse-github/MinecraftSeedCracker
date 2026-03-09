/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BlockInput
/*     */   extends Object implements Predicate<BlockInWorld> {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final BlockState state;
/*     */   private final Set<Property<?>> properties;
/*     */   private final CompoundTag tag;
/*     */   
/*     */   public BlockInput(BlockState state, Set<Property<?>> properties, CompoundTag tag) {
/*  31 */     this.state = state;
/*  32 */     this.properties = properties;
/*  33 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public BlockState getState() { return this.state; }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public Set<Property<?>> getDefinedProperties() { return this.properties; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test(BlockInWorld blockInWorld) {
/*  46 */     BlockState state = blockInWorld.getState();
/*     */     
/*  48 */     if (!state.is(this.state.getBlock())) {
/*  49 */       return false;
/*     */     }
/*     */     
/*  52 */     for (Property<?> property : this.properties) {
/*  53 */       if (state.getValue(property) != this.state.getValue(property)) {
/*  54 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  58 */     if (this.tag != null) {
/*  59 */       BlockEntity entity = blockInWorld.getEntity();
/*  60 */       return (entity != null && NbtUtils.compareNbt(this.tag, entity.saveWithFullMetadata(blockInWorld.getLevel().registryAccess()), true));
/*     */     } 
/*     */     
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  67 */   public boolean test(ServerLevel level, BlockPos pos) { return test(new BlockInWorld(level, pos, false)); }
/*     */ 
/*     */   
/*     */   public boolean place(ServerLevel level, BlockPos pos, @UpdateFlags int update) {
/*  71 */     BlockState state = ((update & 0x10) != 0) ? this.state : Block.updateFromNeighbourShapes(this.state, level, pos);
/*  72 */     if (state.isAir()) {
/*  73 */       state = this.state;
/*     */     }
/*     */ 
/*     */     
/*  77 */     state = overwriteWithDefinedProperties(state);
/*     */     
/*  79 */     boolean affected = false;
/*  80 */     if (level.setBlock(pos, state, update)) {
/*  81 */       affected = true;
/*     */     }
/*     */     
/*  84 */     if (this.tag != null) {
/*  85 */       BlockEntity entity = level.getBlockEntity(pos);
/*  86 */       if (entity != null) {
/*  87 */         ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/*  88 */         try { RegistryAccess registryAccess = level.registryAccess();
/*  89 */           ProblemReporter blockEntityReporter = reporter.forChild(entity.problemPath());
/*  90 */           TagValueOutput initialOutput = TagValueOutput.createWithContext(blockEntityReporter.forChild(() -> "(before)"), registryAccess);
/*  91 */           entity.saveWithoutMetadata(initialOutput);
/*  92 */           CompoundTag before = initialOutput.buildResult();
/*     */           
/*  94 */           entity.loadWithComponents(TagValueInput.create(reporter, registryAccess, this.tag));
/*     */           
/*  96 */           TagValueOutput updatedOutput = TagValueOutput.createWithContext(blockEntityReporter.forChild(() -> "(after)"), registryAccess);
/*  97 */           entity.saveWithoutMetadata(updatedOutput);
/*  98 */           CompoundTag after = updatedOutput.buildResult();
/*     */           
/* 100 */           if (!after.equals(before)) {
/* 101 */             affected = true;
/* 102 */             entity.setChanged();
/*     */             
/* 104 */             level.getChunkSource().blockChanged(pos);
/*     */           } 
/* 106 */           reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*     */       
/*     */       } 
/* 109 */     }  return affected;
/*     */   }
/*     */   
/*     */   private BlockState overwriteWithDefinedProperties(BlockState state) {
/* 113 */     if (state == this.state) {
/* 114 */       return state;
/*     */     }
/* 116 */     for (Property<?> property : this.properties) {
/* 117 */       state = copyProperty(state, this.state, property);
/*     */     }
/* 119 */     return state;
/*     */   }
/*     */ 
/*     */   
/* 123 */   private static <T extends Comparable<T>> BlockState copyProperty(BlockState target, BlockState source, Property<T> property) { return (BlockState)target.trySetValue(property, source.getValue(property)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */