/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.debug.DebugHiveInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.Bees;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BeehiveBlockEntity
/*     */   extends BlockEntity {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String TAG_FLOWER_POS = "flower_pos";
/*     */   
/*     */   private static final String BEES = "bees";
/*     */   
/*  60 */   private static final List<String> IGNORED_BEE_TAGS = Arrays.asList(new String[] { "Air", "drop_chances", "equipment", "Brain", "CanPickUpLoot", "DeathTime", "fall_distance", "FallFlying", "Fire", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "sleeping_pos", "CannotEnterHiveTicks", "TicksSincePollination", "CropsGrownSincePollination", "hive_pos", "Passengers", "leash", "UUID" });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MAX_OCCUPANTS = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MIN_TICKS_BEFORE_REENTERING_HIVE = 400;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MIN_OCCUPATION_TICKS_NECTAR = 2400;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MIN_OCCUPATION_TICKS_NECTARLESS = 600;
/*     */ 
/*     */ 
/*     */   
/*  81 */   private final List<BeeData> stored = Lists.newArrayList();
/*     */   private BlockPos savedFlowerPos;
/*     */   
/*     */   public enum BeeReleaseStatus
/*     */   {
/*  86 */     HONEY_DELIVERED,
/*  87 */     BEE_RELEASED,
/*  88 */     EMERGENCY;
/*     */   }
/*     */ 
/*     */   
/*  92 */   public BeehiveBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.BEEHIVE, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  97 */     if (isFireNearby())
/*     */     {
/*  99 */       emptyAllLivingFromHive(null, this.level.getBlockState(getBlockPos()), BeeReleaseStatus.EMERGENCY);
/*     */     }
/* 101 */     super.setChanged();
/*     */   }
/*     */   
/*     */   public boolean isFireNearby() {
/* 105 */     if (this.level == null) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     for (BlockPos pos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1))) {
/* 110 */       if (this.level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.FireBlock) {
/* 111 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 119 */   public boolean isEmpty() { return this.stored.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean isFull() { return (this.stored.size() == 3); }
/*     */ 
/*     */   
/*     */   public void emptyAllLivingFromHive(Player player, BlockState state, BeeReleaseStatus releaseReason) {
/* 127 */     List<Entity> releasedFromHive = releaseAllOccupants(state, releaseReason);
/*     */     
/* 129 */     if (player != null) {
/* 130 */       for (Entity released : releasedFromHive) {
/* 131 */         if (released instanceof Bee) { Bee bee = (Bee)released;
/* 132 */           if (player.position().distanceToSqr(released.position()) <= 16.0D) {
/* 133 */             if (!isSedated()) {
/* 134 */               bee.setTarget(player); continue;
/*     */             } 
/* 136 */             bee.setStayOutOfHiveCountdown(400);
/*     */           }  }
/*     */       
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Entity> releaseAllOccupants(BlockState state, BeeReleaseStatus releaseStatus) {
/* 145 */     List<Entity> spawned = Lists.newArrayList();
/* 146 */     this.stored.removeIf(occupantEntry -> releaseOccupant(this.level, this.worldPosition, state, occupantEntry.toOccupant(), spawned, releaseStatus, this.savedFlowerPos));
/* 147 */     if (!spawned.isEmpty()) {
/* 148 */       super.setChanged();
/*     */     }
/* 150 */     return spawned;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 155 */   public int getOccupantCount() { return this.stored.size(); }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static int getHoneyLevel(BlockState blockState) { return ((Integer)blockState.getValue(BeehiveBlock.HONEY_LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 164 */   public boolean isSedated() { return CampfireBlock.isSmokeyPos(this.level, getBlockPos()); }
/*     */ 
/*     */   
/*     */   public void addOccupant(Bee bee) {
/* 168 */     if (this.stored.size() >= 3) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 173 */     bee.stopRiding();
/* 174 */     bee.ejectPassengers();
/* 175 */     bee.dropLeash();
/* 176 */     storeBee(Occupant.of(bee));
/*     */     
/* 178 */     if (this.level != null) {
/*     */       
/* 180 */       if (bee.hasSavedFlowerPos() && (!hasSavedFlowerPos() || this.level.random.nextBoolean())) {
/* 181 */         this.savedFlowerPos = bee.getSavedFlowerPos();
/*     */       }
/*     */       
/* 184 */       BlockPos blockPos = getBlockPos();
/* 185 */       this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 186 */       this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(bee, getBlockState()));
/*     */     } 
/*     */ 
/*     */     
/* 190 */     bee.discard();
/* 191 */     super.setChanged();
/*     */   }
/*     */ 
/*     */   
/* 195 */   public void storeBee(Occupant occupant) { this.stored.add(new BeeData(occupant)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean releaseOccupant(Level level, BlockPos blockPos, BlockState state, Occupant beeData, List<Entity> spawned, BeeReleaseStatus releaseStatus, BlockPos savedFlowerPos) {
/* 202 */     if (((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, blockPos)).booleanValue() && releaseStatus != BeeReleaseStatus.EMERGENCY) {
/* 203 */       return false;
/*     */     }
/*     */     
/* 206 */     Direction facing = (Direction)state.getValue(BeehiveBlock.FACING);
/* 207 */     BlockPos facingPos = blockPos.relative(facing);
/* 208 */     boolean frontBlocked = !level.getBlockState(facingPos).getCollisionShape(level, facingPos).isEmpty();
/*     */     
/* 210 */     if (frontBlocked && releaseStatus != BeeReleaseStatus.EMERGENCY) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     Entity entity = beeData.createEntity(level, blockPos);
/* 215 */     if (entity != null) {
/* 216 */       if (entity instanceof Bee) { Bee bee = (Bee)entity;
/*     */ 
/*     */         
/* 219 */         if (savedFlowerPos != null && !bee.hasSavedFlowerPos() && level.random.nextFloat() < 0.9F) {
/* 220 */           bee.setSavedFlowerPos(savedFlowerPos);
/*     */         }
/*     */         
/* 223 */         if (releaseStatus == BeeReleaseStatus.HONEY_DELIVERED) {
/* 224 */           bee.dropOffNectar();
/*     */           
/* 226 */           if (state.is(BlockTags.BEEHIVES, s -> s.hasProperty(BeehiveBlock.HONEY_LEVEL))) {
/* 227 */             int honeyLevel = getHoneyLevel(state);
/* 228 */             if (honeyLevel < 5) {
/* 229 */               int levelIncrease = (level.random.nextInt(100) == 0) ? 2 : 1;
/* 230 */               if (honeyLevel + levelIncrease > 5) {
/* 231 */                 levelIncrease--;
/*     */               }
/* 233 */               level.setBlockAndUpdate(blockPos, (BlockState)state.setValue(BeehiveBlock.HONEY_LEVEL, Integer.valueOf(honeyLevel + levelIncrease)));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 238 */         if (spawned != null) {
/* 239 */           spawned.add(bee);
/*     */         }
/*     */         
/* 242 */         float bbWidth = entity.getBbWidth();
/* 243 */         double delta = frontBlocked ? 0.0D : (0.55D + (bbWidth / 2.0F));
/* 244 */         double spawnX = blockPos.getX() + 0.5D + delta * facing.getStepX();
/* 245 */         double spawnY = blockPos.getY() + 0.5D - (entity.getBbHeight() / 2.0F);
/* 246 */         double spawnZ = blockPos.getZ() + 0.5D + delta * facing.getStepZ();
/* 247 */         entity.snapTo(spawnX, spawnY, spawnZ, entity.getYRot(), entity.getXRot()); }
/*     */ 
/*     */       
/* 250 */       level.playSound(null, blockPos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 251 */       level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, level.getBlockState(blockPos)));
/*     */       
/* 253 */       return level.addFreshEntity(entity);
/*     */     } 
/*     */     
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 260 */   private boolean hasSavedFlowerPos() { return (this.savedFlowerPos != null); }
/*     */ 
/*     */   
/*     */   private static void tickOccupants(Level level, BlockPos pos, BlockState state, List<BeeData> stored, BlockPos savedFlowerPos) {
/* 264 */     boolean changed = false;
/* 265 */     Iterator<BeeData> iterator = stored.iterator();
/* 266 */     while (iterator.hasNext()) {
/* 267 */       BeeData data = (BeeData)iterator.next();
/* 268 */       if (data.tick()) {
/*     */         
/* 270 */         BeeReleaseStatus releaseStatus = data.hasNectar() ? BeeReleaseStatus.HONEY_DELIVERED : BeeReleaseStatus.BEE_RELEASED;
/* 271 */         if (releaseOccupant(level, pos, state, data.toOccupant(), null, releaseStatus, savedFlowerPos)) {
/* 272 */           changed = true;
/* 273 */           iterator.remove();
/*     */         } 
/*     */       } 
/*     */     } 
/* 277 */     if (changed) {
/* 278 */       setChanged(level, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void serverTick(Level level, BlockPos blockPos, BlockState state, BeehiveBlockEntity entity) {
/* 283 */     tickOccupants(level, blockPos, state, entity.stored, entity.savedFlowerPos);
/*     */     
/* 285 */     if (!entity.stored.isEmpty() && level.getRandom().nextDouble() < 0.005D) {
/* 286 */       double x = blockPos.getX() + 0.5D;
/* 287 */       double y = blockPos.getY();
/* 288 */       double z = blockPos.getZ() + 0.5D;
/* 289 */       level.playSound(null, x, y, z, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 295 */     super.loadAdditional(input);
/*     */     
/* 297 */     this.stored.clear();
/* 298 */     ((List)input.read("bees", Occupant.LIST_CODEC).orElse(List.of()))
/* 299 */       .forEach(this::storeBee);
/*     */     
/* 301 */     this.savedFlowerPos = (BlockPos)input.read("flower_pos", BlockPos.CODEC).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 306 */     super.saveAdditional(output);
/*     */     
/* 308 */     output.store("bees", Occupant.LIST_CODEC, getBees());
/* 309 */     output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 314 */     super.applyImplicitComponents(components);
/* 315 */     this.stored.clear();
/* 316 */     List<Occupant> bees = ((Bees)components.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
/* 317 */     bees.forEach(this::storeBee);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 322 */     super.collectImplicitComponents(components);
/* 323 */     components.set(DataComponents.BEES, new Bees(getBees()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 328 */     super.removeComponentsFromTag(output);
/* 329 */     output.discard("bees");
/*     */   }
/*     */ 
/*     */   
/* 333 */   private List<Occupant> getBees() { return this.stored.stream().map(BeeData::toOccupant).toList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) { registration.register(DebugSubscriptions.BEE_HIVES, () -> DebugHiveInfo.pack(this)); }
/*     */   
/*     */   private static class BeeData
/*     */   {
/*     */     private final BeehiveBlockEntity.Occupant occupant;
/*     */     private int ticksInHive;
/*     */     
/*     */     private BeeData(BeehiveBlockEntity.Occupant occupant) {
/* 346 */       this.occupant = occupant;
/* 347 */       this.ticksInHive = occupant.ticksInHive();
/*     */     }
/*     */ 
/*     */     
/* 351 */     public boolean tick() { return (this.ticksInHive++ > this.occupant.minTicksInHive); }
/*     */ 
/*     */ 
/*     */     
/* 355 */     public BeehiveBlockEntity.Occupant toOccupant() { return new BeehiveBlockEntity.Occupant(this.occupant.entityData, this.ticksInHive, this.occupant.minTicksInHive); }
/*     */ 
/*     */ 
/*     */     
/* 359 */     public boolean hasNectar() { return this.occupant.entityData.getUnsafe().getBooleanOr("HasNectar", false); } }
/*     */   public static final class Occupant extends Record { private final TypedEntityData<EntityType<?>> entityData; private final int ticksInHive;
/*     */     private final int minTicksInHive;
/*     */     
/* 363 */     public Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInHive, int minTicksInHive) { this.entityData = entityData; this.ticksInHive = ticksInHive; this.minTicksInHive = minTicksInHive; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 363 */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant; } public TypedEntityData<EntityType<?>> entityData() { return this.entityData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/* 363 */       //   0	8	1	o	Ljava/lang/Object; } public int ticksInHive() { return this.ticksInHive; } public int minTicksInHive() { return this.minTicksInHive; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 368 */     public static final Codec<Occupant> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 369 */           TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(Occupant::entityData), Codec.INT
/* 370 */           .fieldOf("ticks_in_hive").forGetter(Occupant::ticksInHive), Codec.INT
/* 371 */           .fieldOf("min_ticks_in_hive").forGetter(Occupant::minTicksInHive))
/* 372 */         .apply(i, Occupant::new));
/*     */     
/* 374 */     public static final Codec<List<Occupant>> LIST_CODEC = CODEC.listOf();
/*     */     
/* 376 */     public static final StreamCodec<RegistryFriendlyByteBuf, Occupant> STREAM_CODEC = StreamCodec.composite(
/* 377 */         TypedEntityData.streamCodec(EntityType.STREAM_CODEC), Occupant::entityData, ByteBufCodecs.VAR_INT, Occupant::ticksInHive, ByteBufCodecs.VAR_INT, Occupant::minTicksInHive, Occupant::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Occupant of(Entity entity)
/*     */     {
/* 384 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), BeehiveBlockEntity.LOGGER); 
/* 385 */       try { TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
/* 386 */         entity.save(output);
/* 387 */         Objects.requireNonNull(output); BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
/* 388 */         CompoundTag entityTag = output.buildResult();
/* 389 */         boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
/* 390 */         Occupant occupant = new Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? 2400 : 600);
/* 391 */         reporter.close(); return occupant; }
/*     */       catch (Throwable throwable) { try { reporter.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */          throw throwable; }
/* 395 */        } public static Occupant create(int ticksInHive) { return new Occupant(TypedEntityData.of(EntityType.BEE, new CompoundTag()), ticksInHive, 600); }
/*     */ 
/*     */     
/*     */     public Entity createEntity(Level level, BlockPos hivePos) {
/* 399 */       CompoundTag entityTag = this.entityData.copyTagWithoutId();
/*     */       
/* 401 */       Objects.requireNonNull(entityTag); BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
/*     */       
/* 403 */       Entity entity = EntityType.loadEntityRecursive((EntityType)this.entityData.type(), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
/* 404 */       if (entity == null || !entity.getType().is(EntityTypeTags.BEEHIVE_INHABITORS)) {
/* 405 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 409 */       entity.setNoGravity(true);
/*     */       
/* 411 */       if (entity instanceof Bee) { Bee bee = (Bee)entity;
/* 412 */         bee.setHivePos(hivePos);
/* 413 */         setBeeReleaseData(this.ticksInHive, bee); }
/*     */ 
/*     */       
/* 416 */       return entity;
/*     */     }
/*     */     
/*     */     private static void setBeeReleaseData(int ticksInHive, Bee bee) {
/* 420 */       int age = bee.getAge();
/* 421 */       if (age < 0) {
/* 422 */         bee.setAge(Math.min(0, age + ticksInHive));
/* 423 */       } else if (age > 0) {
/* 424 */         bee.setAge(Math.max(0, age - ticksInHive));
/*     */       } 
/* 426 */       bee.setInLoveTime(Math.max(0, bee.getInLoveTime() - ticksInHive));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BeehiveBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */