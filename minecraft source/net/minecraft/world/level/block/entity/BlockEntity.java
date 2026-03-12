/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.PatchedDataComponentMap;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockEntity
/*     */   implements DebugValueSource {
/*  41 */   private static final Codec<BlockEntityType<?>> TYPE_CODEC = BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec();
/*     */   
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final BlockEntityType<?> type;
/*     */   protected Level level;
/*     */   protected final BlockPos worldPosition;
/*     */   protected boolean remove;
/*     */   private BlockState blockState;
/*     */   private DataComponentMap components;
/*     */   
/*     */   public BlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
/*  52 */     this.components = DataComponentMap.EMPTY;
/*     */ 
/*     */     
/*  55 */     this.type = type;
/*  56 */     this.worldPosition = worldPosition.immutable();
/*  57 */     validateBlockState(blockState);
/*  58 */     this.blockState = blockState;
/*     */   }
/*     */   
/*     */   private void validateBlockState(BlockState blockState) {
/*  62 */     if (!isValidBlockState(blockState)) {
/*  63 */       throw new IllegalStateException("Invalid block entity " + getNameForReporting() + " state at " + String.valueOf(this.worldPosition) + ", got " + String.valueOf(blockState));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  68 */   public boolean isValidBlockState(BlockState blockState) { return this.type.isValid(blockState); }
/*     */ 
/*     */   
/*     */   public static BlockPos getPosFromTag(ChunkPos base, CompoundTag entityTag) {
/*  72 */     int x = entityTag.getIntOr("x", 0);
/*  73 */     int y = entityTag.getIntOr("y", 0);
/*  74 */     int z = entityTag.getIntOr("z", 0);
/*     */     
/*  76 */     int sectionX = SectionPos.blockToSectionCoord(x);
/*  77 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/*     */     
/*  79 */     if (sectionX != base.x || sectionZ != base.z) {
/*  80 */       LOGGER.warn("Block entity {} found in a wrong chunk, expected position from chunk {}", entityTag, base);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  88 */       x = base.getBlockX(SectionPos.sectionRelative(x));
/*  89 */       z = base.getBlockZ(SectionPos.sectionRelative(z));
/*     */     } 
/*     */     
/*  92 */     return new BlockPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*  96 */   public Level getLevel() { return this.level; }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void setLevel(Level level) { this.level = level; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean hasLevel() { return (this.level != null); }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {}
/*     */ 
/*     */   
/*     */   public final void loadWithComponents(ValueInput input) {
/* 111 */     loadAdditional(input);
/*     */     
/* 113 */     this.components = (DataComponentMap)input.read("components", DataComponentMap.CODEC).orElse(DataComponentMap.EMPTY);
/*     */   }
/*     */ 
/*     */   
/* 117 */   public final void loadCustomOnly(ValueInput input) { loadAdditional(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CompoundTag saveWithFullMetadata(HolderLookup.Provider registries) {
/* 128 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 129 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
/* 130 */       saveWithFullMetadata(output);
/* 131 */       CompoundTag compoundTag = output.buildResult();
/* 132 */       reporter.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 136 */      } public void saveWithFullMetadata(ValueOutput output) { saveWithoutMetadata(output);
/* 137 */     saveMetadata(output); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveWithId(ValueOutput output) {
/* 145 */     saveWithoutMetadata(output);
/* 146 */     saveId(output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CompoundTag saveWithoutMetadata(HolderLookup.Provider registries) {
/* 153 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 154 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
/* 155 */       saveWithoutMetadata(output);
/* 156 */       CompoundTag compoundTag = output.buildResult();
/* 157 */       reporter.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 161 */      } public void saveWithoutMetadata(ValueOutput output) { saveAdditional(output);
/* 162 */     output.store("components", DataComponentMap.CODEC, this.components); }
/*     */ 
/*     */   
/*     */   public final CompoundTag saveCustomOnly(HolderLookup.Provider registries) {
/* 166 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 167 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
/* 168 */       saveCustomOnly(output);
/* 169 */       CompoundTag compoundTag = output.buildResult();
/* 170 */       reporter.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 174 */      } public void saveCustomOnly(ValueOutput output) { saveAdditional(output); }
/*     */ 
/*     */ 
/*     */   
/* 178 */   private void saveId(ValueOutput output) { addEntityType(output, getType()); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static void addEntityType(ValueOutput output, BlockEntityType<?> type) { output.store("id", TYPE_CODEC, type); }
/*     */ 
/*     */   
/*     */   private void saveMetadata(ValueOutput output) {
/* 186 */     saveId(output);
/* 187 */     output.putInt("x", this.worldPosition.getX());
/* 188 */     output.putInt("y", this.worldPosition.getY());
/* 189 */     output.putInt("z", this.worldPosition.getZ());
/*     */   }
/*     */   public static BlockEntity loadStatic(BlockPos pos, BlockState state, CompoundTag tag, HolderLookup.Provider registries) {
/*     */     BlockEntity entity;
/* 193 */     BlockEntityType<?> type = (BlockEntityType)tag.read("id", TYPE_CODEC).orElse(null);
/* 194 */     if (type == null) {
/* 195 */       LOGGER.error("Skipping block entity with invalid type: {}", tag.get("id"));
/* 196 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 201 */       entity = type.create(pos, state);
/* 202 */     } catch (Throwable t) {
/* 203 */       LOGGER.error("Failed to create block entity {} for block {} at position {} ", new Object[] { type, pos, state, t });
/* 204 */       return null;
/*     */     } 
/*     */     
/* 207 */     try { ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); 
/* 208 */       try { entity.loadWithComponents(TagValueInput.create(reporter, registries, tag));
/* 209 */         BlockEntity blockEntity = entity;
/* 210 */         reporter.close(); return blockEntity; } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable t)
/* 211 */     { LOGGER.error("Failed to load data for block entity {} for block {} at position {}", new Object[] { type, pos, state, t });
/* 212 */       return null; }
/*     */   
/*     */   }
/*     */   
/*     */   public void setChanged() {
/* 217 */     if (this.level != null) {
/* 218 */       setChanged(this.level, this.worldPosition, this.blockState);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void setChanged(Level level, BlockPos worldPosition, BlockState blockState) {
/* 223 */     level.blockEntityChanged(worldPosition);
/*     */     
/* 225 */     if (!blockState.isAir()) {
/* 226 */       level.updateNeighbourForOutputSignal(worldPosition, blockState.getBlock());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 231 */   public BlockPos getBlockPos() { return this.worldPosition; }
/*     */ 
/*     */ 
/*     */   
/* 235 */   public BlockState getBlockState() { return this.blockState; }
/*     */ 
/*     */ 
/*     */   
/* 239 */   public Packet<ClientGamePacketListener> getUpdatePacket() { return null; }
/*     */ 
/*     */ 
/*     */   
/* 243 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return new CompoundTag(); }
/*     */ 
/*     */ 
/*     */   
/* 247 */   public boolean isRemoved() { return this.remove; }
/*     */ 
/*     */ 
/*     */   
/* 251 */   public void setRemoved() { this.remove = true; }
/*     */ 
/*     */ 
/*     */   
/* 255 */   public void clearRemoved() { this.remove = false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 264 */     BlockEntity blockEntity = this; if (blockEntity instanceof Container) { Container container = (Container)blockEntity; if (this.level != null) {
/* 265 */         Containers.dropContents(this.level, pos, container);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/* 270 */   public boolean triggerEvent(int b0, int b1) { return false; }
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 274 */     category.setDetail("Name", this::getNameForReporting);
/* 275 */     Objects.requireNonNull(getBlockState()); category.setDetail("Cached block", getBlockState()::toString);
/*     */     
/* 277 */     if (this.level == null) {
/* 278 */       category.setDetail("Block location", () -> String.valueOf(this.worldPosition) + " (world missing)");
/*     */     } else {
/* 280 */       Objects.requireNonNull(this.level.getBlockState(this.worldPosition)); category.setDetail("Actual block", this.level.getBlockState(this.worldPosition)::toString);
/* 281 */       CrashReportCategory.populateBlockLocationDetails(category, this.level, this.worldPosition);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 286 */   public String getNameForReporting() { return String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(getType())) + " // " + String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(getType())); }
/*     */ 
/*     */ 
/*     */   
/* 290 */   public BlockEntityType<?> getType() { return this.type; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setBlockState(BlockState blockState) {
/* 298 */     validateBlockState(blockState);
/* 299 */     this.blockState = blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {}
/*     */ 
/*     */   
/* 306 */   public final void applyComponentsFromItemStack(ItemStack stack) { applyComponents(stack.getPrototype(), stack.getComponentsPatch()); }
/*     */ 
/*     */   
/*     */   public final void applyComponents(DataComponentMap prototype, DataComponentPatch patch) {
/* 310 */     final Set<DataComponentType<?>> implicitComponents = new HashSet<DataComponentType<?>>();
/* 311 */     implicitComponents.add(DataComponents.BLOCK_ENTITY_DATA);
/* 312 */     implicitComponents.add(DataComponents.BLOCK_STATE);
/*     */     
/* 314 */     final PatchedDataComponentMap fullView = PatchedDataComponentMap.fromPatch(prototype, patch);
/* 315 */     applyImplicitComponents(new DataComponentGetter(this)
/*     */         {
/*     */           public <T> T get(DataComponentType<? extends T> type) {
/* 318 */             implicitComponents.add(type);
/* 319 */             return (T)fullView.get(type);
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
/* 324 */             implicitComponents.add(type);
/* 325 */             return (T)fullView.getOrDefault(type, defaultValue);
/*     */           }
/*     */         });
/* 328 */     Objects.requireNonNull(implicitComponents); DataComponentPatch newPatch = patch.forget(implicitComponents::contains);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     this.components = newPatch.split().added();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {}
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void removeComponentsFromTag(ValueOutput output) {}
/*     */ 
/*     */   
/*     */   public final DataComponentMap collectComponents() {
/* 348 */     DataComponentMap.Builder result = DataComponentMap.builder();
/* 349 */     result.addAll(this.components);
/* 350 */     collectImplicitComponents(result);
/* 351 */     return result.build();
/*     */   }
/*     */ 
/*     */   
/* 355 */   public DataComponentMap components() { return this.components; }
/*     */ 
/*     */ 
/*     */   
/* 359 */   public void setComponents(DataComponentMap components) { this.components = components; }
/*     */ 
/*     */ 
/*     */   
/* 363 */   public static Component parseCustomNameSafe(ValueInput input, String name) { return (Component)input.read(name, ComponentSerialization.CODEC).orElse(null); }
/*     */   private static final class BlockEntityPathElement extends Record implements ProblemReporter.PathElement { private final BlockEntity blockEntity;
/*     */     
/* 366 */     private BlockEntityPathElement(BlockEntity blockEntity) { this.blockEntity = blockEntity; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #366	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #366	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #366	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/* 366 */       //   0	8	1	o	Ljava/lang/Object; } public BlockEntity blockEntity() { return this.blockEntity; }
/*     */ 
/*     */     
/* 369 */     public String get() { return this.blockEntity.getNameForReporting() + "@" + this.blockEntity.getNameForReporting(); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 374 */   public ProblemReporter.PathElement problemPath() { return new BlockEntityPathElement(this); }
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */