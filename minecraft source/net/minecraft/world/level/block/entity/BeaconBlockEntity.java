/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BeaconMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BeaconBeamBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class BeaconBlockEntity
/*     */   extends BlockEntity
/*     */   implements MenuProvider, Nameable, BeaconBeamOwner {
/*     */   private static final int MAX_LEVELS = 4;
/*  57 */   public static final List<List<Holder<MobEffect>>> BEACON_EFFECTS = List.of(
/*  58 */       List.of(MobEffects.SPEED, MobEffects.HASTE), 
/*  59 */       List.of(MobEffects.RESISTANCE, MobEffects.JUMP_BOOST), 
/*  60 */       List.of(MobEffects.STRENGTH), List.of(MobEffects.REGENERATION));
/*     */   
/*  62 */   private static final Set<Holder<MobEffect>> VALID_EFFECTS = (Set)BEACON_EFFECTS.stream().flatMap(Collection::stream).collect(Collectors.toSet());
/*     */   
/*     */   public static final int DATA_LEVELS = 0;
/*     */   
/*     */   public static final int DATA_PRIMARY = 1;
/*     */   public static final int DATA_SECONDARY = 2;
/*     */   public static final int NUM_DATA_VALUES = 3;
/*     */   private static final int BLOCKS_CHECK_PER_TICK = 10;
/*  70 */   private static final Component DEFAULT_NAME = Component.translatable("container.beacon");
/*     */   
/*     */   private static final String TAG_PRIMARY = "primary_effect";
/*     */   
/*     */   private static final String TAG_SECONDARY = "secondary_effect";
/*  75 */   private List<BeaconBeamOwner.Section> beamSections = new ArrayList();
/*  76 */   private List<BeaconBeamOwner.Section> checkingBeamSections = new ArrayList();
/*     */   
/*     */   private int levels;
/*     */   
/*     */   private int lastCheckY;
/*     */   
/*     */   private Holder<MobEffect> primaryPower;
/*     */   
/*     */   private Holder<MobEffect> secondaryPower;
/*     */   private Component name;
/*  86 */   private LockCode lockKey = LockCode.NO_LOCK;
/*     */   
/*  88 */   private final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int dataId) {
/*  91 */         switch (dataId) { case 0: case 1: case 2:  }  return 
/*     */ 
/*     */ 
/*     */           
/*  95 */           0;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void set(int dataId, int value) {
/* 101 */         switch (dataId) { case 0:
/* 102 */             BeaconBlockEntity.this.levels = value; break;
/*     */           case 1:
/* 104 */             if (!BeaconBlockEntity.this.level.isClientSide() && !BeaconBlockEntity.this.beamSections.isEmpty()) {
/* 105 */               BeaconBlockEntity.playSound(BeaconBlockEntity.this.level, BeaconBlockEntity.this.worldPosition, SoundEvents.BEACON_POWER_SELECT);
/*     */             }
/* 107 */             BeaconBlockEntity.this.primaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value)); break;
/*     */           case 2:
/* 109 */             BeaconBlockEntity.this.secondaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
/*     */             break; }
/*     */       
/*     */       }
/*     */ 
/*     */       
/* 115 */       public int getCount() { return 3; }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 120 */   private static Holder<MobEffect> filterEffect(Holder<MobEffect> effect) { return VALID_EFFECTS.contains(effect) ? effect : null; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public BeaconBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.BEACON, worldPosition, blockState); }
/*     */   
/*     */   public static void tick(Level level, BlockPos pos, BlockState selfState, BeaconBlockEntity entity) {
/*     */     BlockPos checkPos;
/* 128 */     int x = pos.getX();
/* 129 */     int y = pos.getY();
/* 130 */     int z = pos.getZ();
/*     */ 
/*     */     
/* 133 */     if (entity.lastCheckY < y) {
/* 134 */       checkPos = pos;
/* 135 */       entity.checkingBeamSections = Lists.newArrayList();
/* 136 */       entity.lastCheckY = checkPos.getY() - 1;
/*     */     } else {
/* 138 */       checkPos = new BlockPos(x, entity.lastCheckY + 1, z);
/*     */     } 
/*     */     
/* 141 */     BeaconBeamOwner.Section lastBeamSection = entity.checkingBeamSections.isEmpty() ? null : (BeaconBeamOwner.Section)entity.checkingBeamSections.get(entity.checkingBeamSections.size() - 1);
/*     */     
/* 143 */     int lastSetBlock = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
/*     */ 
/*     */     
/* 146 */     for (int i = 0; i < 10 && 
/* 147 */       checkPos.getY() <= lastSetBlock; i++) {
/*     */ 
/*     */       
/* 150 */       BlockState state = level.getBlockState(checkPos);
/* 151 */       Block block = state.getBlock();
/* 152 */       if (block instanceof BeaconBeamBlock) { BeaconBeamBlock beaconBeamBlock = (BeaconBeamBlock)block;
/* 153 */         int color = beaconBeamBlock.getColor().getTextureDiffuseColor();
/*     */         
/* 155 */         if (entity.checkingBeamSections.size() <= 1) {
/* 156 */           lastBeamSection = new BeaconBeamOwner.Section(color);
/* 157 */           entity.checkingBeamSections.add(lastBeamSection);
/* 158 */         } else if (lastBeamSection != null) {
/*     */           
/* 160 */           if (color == lastBeamSection.getColor()) {
/* 161 */             lastBeamSection.increaseHeight();
/*     */           } else {
/* 163 */             lastBeamSection = new BeaconBeamOwner.Section(ARGB.average(lastBeamSection.getColor(), color));
/* 164 */             entity.checkingBeamSections.add(lastBeamSection);
/*     */           } 
/*     */         }  }
/* 167 */       else if (lastBeamSection != null && (state.getLightBlock() < 15 || state.is(Blocks.BEDROCK)))
/* 168 */       { lastBeamSection.increaseHeight(); }
/*     */       else
/* 170 */       { entity.checkingBeamSections.clear();
/* 171 */         entity.lastCheckY = lastSetBlock;
/*     */         break; }
/*     */       
/* 174 */       checkPos = checkPos.above();
/* 175 */       entity.lastCheckY++;
/*     */     } 
/*     */     
/* 178 */     int previousLevels = entity.levels;
/*     */     
/* 180 */     if (level.getGameTime() % 80L == 0L) {
/* 181 */       if (!entity.beamSections.isEmpty()) {
/* 182 */         entity.levels = updateBase(level, x, y, z);
/*     */       }
/*     */       
/* 185 */       if (entity.levels > 0 && !entity.beamSections.isEmpty()) {
/* 186 */         applyEffects(level, pos, entity.levels, entity.primaryPower, entity.secondaryPower);
/* 187 */         playSound(level, pos, SoundEvents.BEACON_AMBIENT);
/*     */       } 
/*     */     } 
/*     */     
/* 191 */     if (entity.lastCheckY >= lastSetBlock) {
/* 192 */       entity.lastCheckY = level.getMinY() - 1;
/* 193 */       boolean wasActive = (previousLevels > 0);
/*     */       
/* 195 */       entity.beamSections = entity.checkingBeamSections;
/*     */       
/* 197 */       if (!level.isClientSide()) {
/* 198 */         boolean isActive = (entity.levels > 0);
/*     */         
/* 200 */         if (!wasActive && isActive) {
/* 201 */           playSound(level, pos, SoundEvents.BEACON_ACTIVATE);
/*     */           
/* 203 */           for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, (new AABB(x, y, z, x, (y - 4), z)).inflate(10.0D, 5.0D, 10.0D))) {
/* 204 */             CriteriaTriggers.CONSTRUCT_BEACON.trigger(player, entity.levels);
/*     */           }
/* 206 */         } else if (wasActive && !isActive) {
/* 207 */           playSound(level, pos, SoundEvents.BEACON_DEACTIVATE);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int updateBase(Level level, int x, int y, int z) {
/* 214 */     int levels = 0;
/* 215 */     for (int step = 1; step <= 4; ) {
/* 216 */       int ly = y - step;
/* 217 */       if (ly < level.getMinY()) {
/*     */         break;
/*     */       }
/*     */       
/* 221 */       boolean isOk = true;
/* 222 */       for (int lx = x - step; lx <= x + step && isOk; lx++) {
/* 223 */         for (int lz = z - step; lz <= z + step; lz++) {
/* 224 */           if (!level.getBlockState(new BlockPos(lx, ly, lz)).is(BlockTags.BEACON_BASE_BLOCKS)) {
/* 225 */             isOk = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 231 */       if (isOk) {
/* 232 */         levels = step;
/*     */         
/*     */         step++;
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     return levels;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemoved() {
/* 243 */     playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
/* 244 */     super.setRemoved();
/*     */   }
/*     */   
/*     */   private static void applyEffects(Level level, BlockPos worldPosition, int levels, Holder<MobEffect> primaryPower, Holder<MobEffect> secondaryPower) {
/* 248 */     if (level.isClientSide() || primaryPower == null) {
/*     */       return;
/*     */     }
/*     */     
/* 252 */     double range = (levels * 10 + 10);
/* 253 */     int baseAmp = 0;
/* 254 */     if (levels >= 4 && Objects.equals(primaryPower, secondaryPower)) {
/* 255 */       baseAmp = 1;
/*     */     }
/* 257 */     int durationTicks = (9 + levels * 2) * 20;
/*     */     
/* 259 */     AABB bb = (new AABB(worldPosition)).inflate(range).expandTowards(0.0D, level.getHeight(), 0.0D);
/* 260 */     List<Player> players = level.getEntitiesOfClass(Player.class, bb);
/* 261 */     for (Player player : players) {
/* 262 */       player.addEffect(new MobEffectInstance(primaryPower, durationTicks, baseAmp, true, true));
/*     */     }
/*     */     
/* 265 */     if (levels >= 4 && !Objects.equals(primaryPower, secondaryPower) && secondaryPower != null) {
/* 266 */       for (Player player : players) {
/* 267 */         player.addEffect(new MobEffectInstance(secondaryPower, durationTicks, 0, true, true));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 273 */   public static void playSound(Level level, BlockPos worldPosition, SoundEvent event) { level.playSound(null, worldPosition, event, SoundSource.BLOCKS, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   public List<BeaconBeamOwner.Section> getBeamSections() { return (this.levels == 0) ? ImmutableList.of() : this.beamSections; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 288 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */   
/*     */   private static void storeEffect(ValueOutput output, String field, Holder<MobEffect> effect) {
/* 292 */     if (effect != null) {
/* 293 */       effect.unwrapKey().ifPresent(key -> output.putString(field, key.identifier().toString()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 299 */   private static Holder<MobEffect> loadEffect(ValueInput input, String field) { Objects.requireNonNull(VALID_EFFECTS); return (Holder)input.read(field, BuiltInRegistries.MOB_EFFECT.holderByNameCodec()).filter(VALID_EFFECTS::contains)
/* 300 */       .orElse(null); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 305 */     super.loadAdditional(input);
/*     */     
/* 307 */     this.primaryPower = loadEffect(input, "primary_effect");
/* 308 */     this.secondaryPower = loadEffect(input, "secondary_effect");
/*     */     
/* 310 */     this.name = parseCustomNameSafe(input, "CustomName");
/*     */     
/* 312 */     this.lockKey = LockCode.fromTag(input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 317 */     super.saveAdditional(output);
/*     */     
/* 319 */     storeEffect(output, "primary_effect", this.primaryPower);
/* 320 */     storeEffect(output, "secondary_effect", this.secondaryPower);
/* 321 */     output.putInt("Levels", this.levels);
/*     */     
/* 323 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */     
/* 325 */     this.lockKey.addToTag(output);
/*     */   }
/*     */ 
/*     */   
/* 329 */   public void setCustomName(Component name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 334 */   public Component getCustomName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 339 */     if (this.lockKey.canUnlock(player)) {
/* 340 */       return new BeaconMenu(containerId, inventory, this.dataAccess, ContainerLevelAccess.create(this.level, getBlockPos()));
/*     */     }
/* 342 */     BaseContainerBlockEntity.sendChestLockedNotifications(getBlockPos().getCenter(), player, getDisplayName());
/*     */     
/* 344 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 349 */   public Component getDisplayName() { return getName(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 354 */     if (this.name != null) {
/* 355 */       return this.name;
/*     */     }
/* 357 */     return DEFAULT_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 362 */     super.applyImplicitComponents(components);
/* 363 */     this.name = (Component)components.get(DataComponents.CUSTOM_NAME);
/* 364 */     this.lockKey = (LockCode)components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 369 */     super.collectImplicitComponents(components);
/* 370 */     components.set(DataComponents.CUSTOM_NAME, this.name);
/* 371 */     if (!this.lockKey.equals(LockCode.NO_LOCK)) {
/* 372 */       components.set(DataComponents.LOCK, this.lockKey);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 378 */     output.discard("CustomName");
/* 379 */     output.discard("lock");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLevel(Level level) {
/* 384 */     super.setLevel(level);
/* 385 */     this.lastCheckY = level.getMinY() - 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BeaconBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */