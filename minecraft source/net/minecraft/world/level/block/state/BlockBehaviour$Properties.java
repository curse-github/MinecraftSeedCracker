/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Properties
/*     */ {
/*     */   private Function<BlockState, MapColor> mapColor;
/*     */   private boolean hasCollision;
/*     */   private SoundType soundType;
/*     */   private ToIntFunction<BlockState> lightEmission;
/*     */   private float explosionResistance;
/*     */   private float destroyTime;
/*     */   private boolean requiresCorrectToolForDrops;
/*     */   private boolean isRandomlyTicking;
/*     */   private float friction;
/*     */   private float speedFactor;
/*     */   private float jumpFactor;
/*     */   private ResourceKey<Block> id;
/* 464 */   public static final Codec<Properties> CODEC = MapCodec.unitCodec(() -> of()); private DependantName<Block, Optional<ResourceKey<LootTable>>> drops; private DependantName<Block, String> descriptionId; private boolean canOcclude; private boolean isAir;
/*     */   private Properties() {
/* 466 */     this.mapColor = (state -> MapColor.NONE);
/* 467 */     this.hasCollision = true;
/*     */     
/* 469 */     this.soundType = SoundType.STONE;
/* 470 */     this.lightEmission = (state -> 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 475 */     this.friction = 0.6F;
/* 476 */     this.speedFactor = 1.0F;
/* 477 */     this.jumpFactor = 1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 482 */     this.drops = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("blocks/"))));
/* 483 */     this.descriptionId = (id -> Util.makeDescriptionId("block", id.identifier()));
/* 484 */     this.canOcclude = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 492 */     this.pushReaction = PushReaction.NORMAL;
/* 493 */     this.spawnTerrainParticles = true;
/* 494 */     this.instrument = NoteBlockInstrument.HARP;
/*     */ 
/*     */     
/* 497 */     this.isValidSpawn = ((state, level, pos, entityType) -> 
/* 498 */       (state.isFaceSturdy(level, pos, Direction.UP) && state.getLightEmission() < 14));
/*     */     
/* 500 */     this.isRedstoneConductor = ((state, level, pos) -> 
/* 501 */       state.isCollisionShapeFullBlock(level, pos));
/*     */     
/* 503 */     this.isSuffocating = ((state, level, pos) -> 
/* 504 */       (state.blocksMotion() && state.isCollisionShapeFullBlock(level, pos)));
/*     */     
/* 506 */     this.isViewBlocking = this.isSuffocating;
/* 507 */     this.hasPostProcess = ((state, level, pos) -> false);
/* 508 */     this.emissiveRendering = ((state, level, pos) -> false);
/*     */ 
/*     */     
/* 511 */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   }
/*     */   private boolean ignitedByLava; @Deprecated
/*     */   private boolean liquid; @Deprecated
/*     */   private boolean forceSolidOff; private boolean forceSolidOn; private PushReaction pushReaction; private boolean spawnTerrainParticles;
/*     */   private NoteBlockInstrument instrument;
/*     */   private boolean replaceable;
/*     */   
/* 519 */   public static Properties of() { return new Properties(); }
/*     */   private BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
/*     */   private BlockBehaviour.StatePredicate isRedstoneConductor;
/*     */   private BlockBehaviour.StatePredicate isSuffocating;
/*     */   private BlockBehaviour.StatePredicate isViewBlocking;
/*     */   private BlockBehaviour.StatePredicate hasPostProcess;
/*     */   private BlockBehaviour.StatePredicate emissiveRendering;
/*     */   private boolean dynamicShape;
/*     */   private FeatureFlagSet requiredFeatures;
/*     */   private BlockBehaviour.OffsetFunction offsetFunction;
/*     */   
/*     */   public static Properties ofFullCopy(BlockBehaviour block) {
/* 531 */     Properties copyTo = ofLegacyCopy(block);
/* 532 */     Properties copyFrom = block.properties;
/*     */     
/* 534 */     copyTo.jumpFactor = copyFrom.jumpFactor;
/* 535 */     copyTo.isRedstoneConductor = copyFrom.isRedstoneConductor;
/* 536 */     copyTo.isValidSpawn = copyFrom.isValidSpawn;
/* 537 */     copyTo.hasPostProcess = copyFrom.hasPostProcess;
/* 538 */     copyTo.isSuffocating = copyFrom.isSuffocating;
/* 539 */     copyTo.isViewBlocking = copyFrom.isViewBlocking;
/* 540 */     copyTo.drops = copyFrom.drops;
/* 541 */     copyTo.descriptionId = copyFrom.descriptionId;
/*     */     
/* 543 */     return copyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Properties ofLegacyCopy(BlockBehaviour block) {
/* 552 */     Properties copyTo = new Properties();
/* 553 */     Properties copyFrom = block.properties;
/*     */     
/* 555 */     copyTo.destroyTime = copyFrom.destroyTime;
/* 556 */     copyTo.explosionResistance = copyFrom.explosionResistance;
/* 557 */     copyTo.hasCollision = copyFrom.hasCollision;
/* 558 */     copyTo.isRandomlyTicking = copyFrom.isRandomlyTicking;
/* 559 */     copyTo.lightEmission = copyFrom.lightEmission;
/* 560 */     copyTo.mapColor = copyFrom.mapColor;
/* 561 */     copyTo.soundType = copyFrom.soundType;
/* 562 */     copyTo.friction = copyFrom.friction;
/* 563 */     copyTo.speedFactor = copyFrom.speedFactor;
/* 564 */     copyTo.dynamicShape = copyFrom.dynamicShape;
/* 565 */     copyTo.canOcclude = copyFrom.canOcclude;
/* 566 */     copyTo.isAir = copyFrom.isAir;
/* 567 */     copyTo.ignitedByLava = copyFrom.ignitedByLava;
/* 568 */     copyTo.liquid = copyFrom.liquid;
/* 569 */     copyTo.forceSolidOff = copyFrom.forceSolidOff;
/* 570 */     copyTo.forceSolidOn = copyFrom.forceSolidOn;
/* 571 */     copyTo.pushReaction = copyFrom.pushReaction;
/* 572 */     copyTo.requiresCorrectToolForDrops = copyFrom.requiresCorrectToolForDrops;
/* 573 */     copyTo.offsetFunction = copyFrom.offsetFunction;
/* 574 */     copyTo.spawnTerrainParticles = copyFrom.spawnTerrainParticles;
/* 575 */     copyTo.requiredFeatures = copyFrom.requiredFeatures;
/* 576 */     copyTo.emissiveRendering = copyFrom.emissiveRendering;
/* 577 */     copyTo.instrument = copyFrom.instrument;
/* 578 */     copyTo.replaceable = copyFrom.replaceable;
/*     */     
/* 580 */     return copyTo;
/*     */   }
/*     */   
/*     */   public Properties mapColor(DyeColor dyeColor) {
/* 584 */     this.mapColor = (state -> dyeColor.getMapColor());
/* 585 */     return this;
/*     */   }
/*     */   
/*     */   public Properties mapColor(MapColor mapColor) {
/* 589 */     this.mapColor = (state -> mapColor);
/* 590 */     return this;
/*     */   }
/*     */   
/*     */   public Properties mapColor(Function<BlockState, MapColor> mapColor) {
/* 594 */     this.mapColor = mapColor;
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noCollision() {
/* 599 */     this.hasCollision = false;
/* 600 */     this.canOcclude = false;
/* 601 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noOcclusion() {
/* 605 */     this.canOcclude = false;
/* 606 */     return this;
/*     */   }
/*     */   
/*     */   public Properties friction(float friction) {
/* 610 */     this.friction = friction;
/* 611 */     return this;
/*     */   }
/*     */   
/*     */   public Properties speedFactor(float speedFactor) {
/* 615 */     this.speedFactor = speedFactor;
/* 616 */     return this;
/*     */   }
/*     */   
/*     */   public Properties jumpFactor(float jumpFactor) {
/* 620 */     this.jumpFactor = jumpFactor;
/* 621 */     return this;
/*     */   }
/*     */   
/*     */   public Properties sound(SoundType soundType) {
/* 625 */     this.soundType = soundType;
/* 626 */     return this;
/*     */   }
/*     */   
/*     */   public Properties lightLevel(ToIntFunction<BlockState> lightEmission) {
/* 630 */     this.lightEmission = lightEmission;
/* 631 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 635 */   public Properties strength(float destroyTime, float explosionResistance) { return destroyTime(destroyTime).explosionResistance(explosionResistance); }
/*     */ 
/*     */ 
/*     */   
/* 639 */   public Properties instabreak() { return strength(0.0F); }
/*     */ 
/*     */   
/*     */   public Properties strength(float destroyTime) {
/* 643 */     strength(destroyTime, destroyTime);
/* 644 */     return this;
/*     */   }
/*     */   
/*     */   public Properties randomTicks() {
/* 648 */     this.isRandomlyTicking = true;
/* 649 */     return this;
/*     */   }
/*     */   
/*     */   public Properties dynamicShape() {
/* 653 */     this.dynamicShape = true;
/* 654 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties noLootTable() {
/* 662 */     this.drops = DependantName.fixed(Optional.empty());
/* 663 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideLootTable(Optional<ResourceKey<LootTable>> table) {
/* 667 */     this.drops = DependantName.fixed(table);
/* 668 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 672 */   protected Optional<ResourceKey<LootTable>> effectiveDrops() { return (Optional)this.drops.get((ResourceKey)Objects.requireNonNull(this.id, "Block id not set")); }
/*     */ 
/*     */   
/*     */   public Properties ignitedByLava() {
/* 676 */     this.ignitedByLava = true;
/* 677 */     return this;
/*     */   }
/*     */   
/*     */   public Properties liquid() {
/* 681 */     this.liquid = true;
/* 682 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties forceSolidOn() {
/* 689 */     this.forceSolidOn = true;
/* 690 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Properties forceSolidOff() {
/* 699 */     this.forceSolidOff = true;
/* 700 */     return this;
/*     */   }
/*     */   
/*     */   public Properties pushReaction(PushReaction pushReaction) {
/* 704 */     this.pushReaction = pushReaction;
/* 705 */     return this;
/*     */   }
/*     */   
/*     */   public Properties air() {
/* 709 */     this.isAir = true;
/* 710 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) {
/* 714 */     this.isValidSpawn = isValidSpawn;
/* 715 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isRedstoneConductor(BlockBehaviour.StatePredicate isRedstoneConductor) {
/* 719 */     this.isRedstoneConductor = isRedstoneConductor;
/* 720 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isSuffocating(BlockBehaviour.StatePredicate isSuffocating) {
/* 724 */     this.isSuffocating = isSuffocating;
/* 725 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking) {
/* 729 */     this.isViewBlocking = isViewBlocking;
/* 730 */     return this;
/*     */   }
/*     */   
/*     */   public Properties hasPostProcess(BlockBehaviour.StatePredicate hasPostProcess) {
/* 734 */     this.hasPostProcess = hasPostProcess;
/* 735 */     return this;
/*     */   }
/*     */   
/*     */   public Properties emissiveRendering(BlockBehaviour.StatePredicate emissiveRendering) {
/* 739 */     this.emissiveRendering = emissiveRendering;
/* 740 */     return this;
/*     */   }
/*     */   
/*     */   public Properties requiresCorrectToolForDrops() {
/* 744 */     this.requiresCorrectToolForDrops = true;
/* 745 */     return this;
/*     */   }
/*     */   
/*     */   public Properties destroyTime(float destroyTime) {
/* 749 */     this.destroyTime = destroyTime;
/* 750 */     return this;
/*     */   }
/*     */   
/*     */   public Properties explosionResistance(float explosionResistance) {
/* 754 */     this.explosionResistance = Math.max(0.0F, explosionResistance);
/* 755 */     return this;
/*     */   }
/*     */   
/*     */   public Properties offsetType(BlockBehaviour.OffsetType offsetType) {
/* 759 */     switch (offsetType.ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 771 */       .offsetFunction = ((state, pos) -> {
/* 772 */         Block block = state.getBlock();
/* 773 */         long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
/*     */         
/* 775 */         float maxHorizontalOffset = block.getMaxHorizontalOffset();
/* 776 */         double x = Mth.clamp((((float)(seed & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/* 777 */         double z = Mth.clamp((((float)(seed >> 8 & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/* 778 */         return new Vec3(x, 0.0D, z);
/*     */       });
/*     */     
/* 781 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noTerrainParticles() {
/* 785 */     this.spawnTerrainParticles = false;
/* 786 */     return this;
/*     */   }
/*     */   
/*     */   public Properties requiredFeatures(FeatureFlag... flags) {
/* 790 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 791 */     return this;
/*     */   }
/*     */   
/*     */   public Properties instrument(NoteBlockInstrument instrument) {
/* 795 */     this.instrument = instrument;
/* 796 */     return this;
/*     */   }
/*     */   
/*     */   public Properties replaceable() {
/* 800 */     this.replaceable = true;
/* 801 */     return this;
/*     */   }
/*     */   
/*     */   public Properties setId(ResourceKey<Block> id) {
/* 805 */     this.id = id;
/* 806 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideDescription(String descriptionId) {
/* 810 */     this.descriptionId = DependantName.fixed(descriptionId);
/* 811 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 815 */   protected String effectiveDescriptionId() { return (String)this.descriptionId.get((ResourceKey)Objects.requireNonNull(this.id, "Block id not set")); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\BlockBehaviour$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */