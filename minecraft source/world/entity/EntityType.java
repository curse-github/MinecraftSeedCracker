/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.entity.ambient.Bat;
/*     */ import net.minecraft.world.entity.animal.allay.Allay;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.animal.camel.Camel;
/*     */ import net.minecraft.world.entity.animal.camel.CamelHusk;
/*     */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*     */ import net.minecraft.world.entity.animal.cow.Cow;
/*     */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*     */ import net.minecraft.world.entity.animal.equine.Donkey;
/*     */ import net.minecraft.world.entity.animal.equine.Horse;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.equine.Mule;
/*     */ import net.minecraft.world.entity.animal.equine.SkeletonHorse;
/*     */ import net.minecraft.world.entity.animal.equine.TraderLlama;
/*     */ import net.minecraft.world.entity.animal.equine.ZombieHorse;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*     */ import net.minecraft.world.entity.animal.fish.Cod;
/*     */ import net.minecraft.world.entity.animal.fish.Pufferfish;
/*     */ import net.minecraft.world.entity.animal.fish.Salmon;
/*     */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.Frog;
/*     */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*     */ import net.minecraft.world.entity.animal.goat.Goat;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*     */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*     */ import net.minecraft.world.entity.animal.nautilus.Nautilus;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*     */ import net.minecraft.world.entity.animal.panda.Panda;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.pig.Pig;
/*     */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.entity.animal.squid.GlowSquid;
/*     */ import net.minecraft.world.entity.animal.squid.Squid;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.decoration.GlowItemFrame;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*     */ import net.minecraft.world.entity.decoration.Mannequin;
/*     */ import net.minecraft.world.entity.decoration.painting.Painting;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.monster.Blaze;
/*     */ import net.minecraft.world.entity.monster.Creeper;
/*     */ import net.minecraft.world.entity.monster.ElderGuardian;
/*     */ import net.minecraft.world.entity.monster.EnderMan;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.monster.Giant;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.monster.MagmaCube;
/*     */ import net.minecraft.world.entity.monster.Phantom;
/*     */ import net.minecraft.world.entity.monster.Ravager;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.monster.Silverfish;
/*     */ import net.minecraft.world.entity.monster.Slime;
/*     */ import net.minecraft.world.entity.monster.Strider;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.illager.Evoker;
/*     */ import net.minecraft.world.entity.monster.illager.Illusioner;
/*     */ import net.minecraft.world.entity.monster.illager.Pillager;
/*     */ import net.minecraft.world.entity.monster.illager.Vindicator;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.monster.skeleton.Bogged;
/*     */ import net.minecraft.world.entity.monster.skeleton.Parched;
/*     */ import net.minecraft.world.entity.monster.skeleton.Skeleton;
/*     */ import net.minecraft.world.entity.monster.skeleton.Stray;
/*     */ import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
/*     */ import net.minecraft.world.entity.monster.spider.CaveSpider;
/*     */ import net.minecraft.world.entity.monster.spider.Spider;
/*     */ import net.minecraft.world.entity.monster.warden.Warden;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.entity.monster.zombie.Husk;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombieVillager;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.entity.projectile.EyeOfEnder;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.FishingHook;
/*     */ import net.minecraft.world.entity.projectile.LlamaSpit;
/*     */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*     */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*     */ import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
/*     */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.BreezeWindCharge;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
/*     */ import net.minecraft.world.entity.vehicle.boat.Boat;
/*     */ import net.minecraft.world.entity.vehicle.boat.ChestBoat;
/*     */ import net.minecraft.world.entity.vehicle.boat.ChestRaft;
/*     */ import net.minecraft.world.entity.vehicle.boat.Raft;
/*     */ import net.minecraft.world.entity.vehicle.minecart.Minecart;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartFurnace;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartHopper;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartSpawner;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityType<T extends Entity>
/*     */   extends Object implements EntityTypeTest<Entity, T>, FeatureElement {
/* 193 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Holder.Reference<EntityType<?>> builtInRegistryHolder;
/*     */   
/* 197 */   private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> id, Builder<T> builder) { return (EntityType)Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(id)); }
/*     */ 
/*     */ 
/*     */   
/* 201 */   private static ResourceKey<EntityType<?>> vanillaEntityId(String vanillaId) { return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace(vanillaId)); }
/*     */ 
/*     */ 
/*     */   
/* 205 */   private static <T extends Entity> EntityType<T> register(String vanillaId, Builder<T> builder) { return register(vanillaEntityId(vanillaId), builder); }
/*     */ 
/*     */   
/* 208 */   public static final Codec<EntityType<?>> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec();
/* 209 */   public static final StreamCodec<RegistryFriendlyByteBuf, EntityType<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE);
/*     */   
/*     */   private static final float MAGIC_HORSE_WIDTH = 1.3964844F;
/*     */   
/*     */   private static final int DISPLAY_TRACKING_RANGE = 10;
/*     */   
/* 215 */   public static final EntityType<Boat> ACACIA_BOAT = register("acacia_boat", Builder.of(boatFactory(() -> Items.ACACIA_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 216 */   public static final EntityType<ChestBoat> ACACIA_CHEST_BOAT = register("acacia_chest_boat", Builder.of(chestBoatFactory(() -> Items.ACACIA_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 217 */   public static final EntityType<Allay> ALLAY = register("allay", Builder.of(Allay::new, MobCategory.CREATURE).sized(0.35F, 0.6F).eyeHeight(0.36F).ridingOffset(0.04F).clientTrackingRange(8).updateInterval(2));
/* 218 */   public static final EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD = register("area_effect_cloud", Builder.of(AreaEffectCloud::new, MobCategory.MISC).noLootTable().fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(2147483647));
/* 219 */   public static final EntityType<Armadillo> ARMADILLO = register("armadillo", Builder.of(Armadillo::new, MobCategory.CREATURE).sized(0.7F, 0.65F).eyeHeight(0.26F).clientTrackingRange(10));
/* 220 */   public static final EntityType<ArmorStand> ARMOR_STAND = register("armor_stand", Builder.of(ArmorStand::new, MobCategory.MISC).sized(0.5F, 1.975F).eyeHeight(1.7775F).clientTrackingRange(10));
/* 221 */   public static final EntityType<Arrow> ARROW = register("arrow", Builder.of(Arrow::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
/* 222 */   public static final EntityType<Axolotl> AXOLOTL = register("axolotl", Builder.of(Axolotl::new, MobCategory.AXOLOTLS).sized(0.75F, 0.42F).eyeHeight(0.2751F).clientTrackingRange(10));
/* 223 */   public static final EntityType<ChestRaft> BAMBOO_CHEST_RAFT = register("bamboo_chest_raft", Builder.of(chestRaftFactory(() -> Items.BAMBOO_CHEST_RAFT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 224 */   public static final EntityType<Raft> BAMBOO_RAFT = register("bamboo_raft", Builder.of(raftFactory(() -> Items.BAMBOO_RAFT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 225 */   public static final EntityType<Bat> BAT = register("bat", Builder.of(Bat::new, MobCategory.AMBIENT).sized(0.5F, 0.9F).eyeHeight(0.45F).clientTrackingRange(5));
/* 226 */   public static final EntityType<Bee> BEE = register("bee", Builder.of(Bee::new, MobCategory.CREATURE).sized(0.7F, 0.6F).eyeHeight(0.3F).clientTrackingRange(8));
/* 227 */   public static final EntityType<Boat> BIRCH_BOAT = register("birch_boat", Builder.of(boatFactory(() -> Items.BIRCH_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 228 */   public static final EntityType<ChestBoat> BIRCH_CHEST_BOAT = register("birch_chest_boat", Builder.of(chestBoatFactory(() -> Items.BIRCH_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 229 */   public static final EntityType<Blaze> BLAZE = register("blaze", Builder.of(Blaze::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.8F).clientTrackingRange(8).notInPeaceful());
/* 230 */   public static final EntityType<Display.BlockDisplay> BLOCK_DISPLAY = register("block_display", Builder.of(BlockDisplay::new, MobCategory.MISC).noLootTable().sized(0.0F, 0.0F).clientTrackingRange(10).updateInterval(1));
/* 231 */   public static final EntityType<Bogged> BOGGED = register("bogged", Builder.of(Bogged::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 232 */   public static final EntityType<Breeze> BREEZE = register("breeze", Builder.of(Breeze::new, MobCategory.MONSTER).sized(0.6F, 1.77F).eyeHeight(1.3452F).clientTrackingRange(10).notInPeaceful());
/* 233 */   public static final EntityType<BreezeWindCharge> BREEZE_WIND_CHARGE = register("breeze_wind_charge", Builder.of(BreezeWindCharge::new, MobCategory.MISC).noLootTable().sized(0.3125F, 0.3125F).eyeHeight(0.0F).clientTrackingRange(4).updateInterval(10));
/* 234 */   public static final EntityType<Camel> CAMEL = register("camel", Builder.of(Camel::new, MobCategory.CREATURE).sized(1.7F, 2.375F).eyeHeight(2.275F).clientTrackingRange(10));
/* 235 */   public static final EntityType<CamelHusk> CAMEL_HUSK = register("camel_husk", Builder.of(CamelHusk::new, MobCategory.MONSTER).sized(1.7F, 2.375F).eyeHeight(2.275F).clientTrackingRange(10));
/* 236 */   public static final EntityType<Cat> CAT = register("cat", Builder.of(Cat::new, MobCategory.CREATURE).sized(0.6F, 0.7F).eyeHeight(0.35F).passengerAttachments(new float[] { 0.5125F }).clientTrackingRange(8));
/* 237 */   public static final EntityType<CaveSpider> CAVE_SPIDER = register("cave_spider", Builder.of(CaveSpider::new, MobCategory.MONSTER).sized(0.7F, 0.5F).eyeHeight(0.45F).clientTrackingRange(8).notInPeaceful());
/* 238 */   public static final EntityType<Boat> CHERRY_BOAT = register("cherry_boat", Builder.of(boatFactory(() -> Items.CHERRY_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 239 */   public static final EntityType<ChestBoat> CHERRY_CHEST_BOAT = register("cherry_chest_boat", Builder.of(chestBoatFactory(() -> Items.CHERRY_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 240 */   public static final EntityType<MinecartChest> CHEST_MINECART = register("chest_minecart", Builder.of(MinecartChest::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 241 */   public static final EntityType<Chicken> CHICKEN = register("chicken", Builder.of(Chicken::new, MobCategory.CREATURE).sized(0.4F, 0.7F).eyeHeight(0.644F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 0.7D, -0.1D) }).clientTrackingRange(10));
/* 242 */   public static final EntityType<Cod> COD = register("cod", Builder.of(Cod::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.3F).eyeHeight(0.195F).clientTrackingRange(4));
/* 243 */   public static final EntityType<CopperGolem> COPPER_GOLEM = register("copper_golem", Builder.of(CopperGolem::new, MobCategory.MISC).sized(0.49F, 0.98F).eyeHeight(0.8125F).clientTrackingRange(10));
/* 244 */   public static final EntityType<MinecartCommandBlock> COMMAND_BLOCK_MINECART = register("command_block_minecart", Builder.of(MinecartCommandBlock::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 245 */   public static final EntityType<Cow> COW = register("cow", Builder.of(Cow::new, MobCategory.CREATURE).sized(0.9F, 1.4F).eyeHeight(1.3F).passengerAttachments(new float[] { 1.36875F }).clientTrackingRange(10));
/* 246 */   public static final EntityType<Creaking> CREAKING = register("creaking", Builder.of(Creaking::new, MobCategory.MONSTER).sized(0.9F, 2.7F).eyeHeight(2.3F).clientTrackingRange(8).notInPeaceful());
/* 247 */   public static final EntityType<Creeper> CREEPER = register("creeper", Builder.of(Creeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).notInPeaceful());
/* 248 */   public static final EntityType<Boat> DARK_OAK_BOAT = register("dark_oak_boat", Builder.of(boatFactory(() -> Items.DARK_OAK_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 249 */   public static final EntityType<ChestBoat> DARK_OAK_CHEST_BOAT = register("dark_oak_chest_boat", Builder.of(chestBoatFactory(() -> Items.DARK_OAK_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 250 */   public static final EntityType<Dolphin> DOLPHIN = register("dolphin", Builder.of(Dolphin::new, MobCategory.WATER_CREATURE).sized(0.9F, 0.6F).eyeHeight(0.3F));
/* 251 */   public static final EntityType<Donkey> DONKEY = register("donkey", Builder.of(Donkey::new, MobCategory.CREATURE).sized(1.3964844F, 1.5F).eyeHeight(1.425F).passengerAttachments(new float[] { 1.1125F }).clientTrackingRange(10));
/* 252 */   public static final EntityType<DragonFireball> DRAGON_FIREBALL = register("dragon_fireball", Builder.of(DragonFireball::new, MobCategory.MISC).noLootTable().sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10));
/* 253 */   public static final EntityType<Drowned> DROWNED = register("drowned", Builder.of(Drowned::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(new float[] { 2.0125F }).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 254 */   public static final EntityType<ThrownEgg> EGG = register("egg", Builder.of(ThrownEgg::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 255 */   public static final EntityType<ElderGuardian> ELDER_GUARDIAN = register("elder_guardian", Builder.of(ElderGuardian::new, MobCategory.MONSTER).sized(1.9975F, 1.9975F).eyeHeight(0.99875F).passengerAttachments(new float[] { 2.350625F }).clientTrackingRange(10).notInPeaceful());
/* 256 */   public static final EntityType<EnderMan> ENDERMAN = register("enderman", Builder.of(EnderMan::new, MobCategory.MONSTER).sized(0.6F, 2.9F).eyeHeight(2.55F).passengerAttachments(new float[] { 2.80625F }).clientTrackingRange(8).notInPeaceful());
/* 257 */   public static final EntityType<Endermite> ENDERMITE = register("endermite", Builder.of(Endermite::new, MobCategory.MONSTER).sized(0.4F, 0.3F).eyeHeight(0.13F).passengerAttachments(new float[] { 0.2375F }).clientTrackingRange(8).notInPeaceful());
/* 258 */   public static final EntityType<EnderDragon> ENDER_DRAGON = register("ender_dragon", Builder.of(EnderDragon::new, MobCategory.MONSTER).fireImmune().sized(16.0F, 8.0F).passengerAttachments(new float[] { 3.0F }).clientTrackingRange(10));
/* 259 */   public static final EntityType<ThrownEnderpearl> ENDER_PEARL = register("ender_pearl", Builder.of(ThrownEnderpearl::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 260 */   public static final EntityType<EndCrystal> END_CRYSTAL = register("end_crystal", Builder.of(EndCrystal::new, MobCategory.MISC).noLootTable().fireImmune().sized(2.0F, 2.0F).clientTrackingRange(16).updateInterval(2147483647));
/* 261 */   public static final EntityType<Evoker> EVOKER = register("evoker", Builder.of(Evoker::new, MobCategory.MONSTER).sized(0.6F, 1.95F).passengerAttachments(new float[] { 2.0F }).ridingOffset(-0.6F).clientTrackingRange(8).notInPeaceful());
/* 262 */   public static final EntityType<EvokerFangs> EVOKER_FANGS = register("evoker_fangs", Builder.of(EvokerFangs::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.8F).clientTrackingRange(6).updateInterval(2));
/* 263 */   public static final EntityType<ThrownExperienceBottle> EXPERIENCE_BOTTLE = register("experience_bottle", Builder.of(ThrownExperienceBottle::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 264 */   public static final EntityType<ExperienceOrb> EXPERIENCE_ORB = register("experience_orb", Builder.of(ExperienceOrb::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20));
/* 265 */   public static final EntityType<EyeOfEnder> EYE_OF_ENDER = register("eye_of_ender", Builder.of(EyeOfEnder::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(4));
/* 266 */   public static final EntityType<FallingBlockEntity> FALLING_BLOCK = register("falling_block", Builder.of(FallingBlockEntity::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));
/* 267 */   public static final EntityType<LargeFireball> FIREBALL = register("fireball", Builder.of(LargeFireball::new, MobCategory.MISC).noLootTable().sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10));
/* 268 */   public static final EntityType<FireworkRocketEntity> FIREWORK_ROCKET = register("firework_rocket", Builder.of(FireworkRocketEntity::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 269 */   public static final EntityType<Fox> FOX = register("fox", Builder.of(Fox::new, MobCategory.CREATURE).sized(0.6F, 0.7F).eyeHeight(0.4F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 0.6375D, -0.25D) }).clientTrackingRange(8).immuneTo(new Block[] { Blocks.SWEET_BERRY_BUSH }));
/* 270 */   public static final EntityType<Frog> FROG = register("frog", Builder.of(Frog::new, MobCategory.CREATURE).sized(0.5F, 0.5F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 0.375D, -0.25D) }).clientTrackingRange(10));
/* 271 */   public static final EntityType<MinecartFurnace> FURNACE_MINECART = register("furnace_minecart", Builder.of(MinecartFurnace::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 272 */   public static final EntityType<Ghast> GHAST = register("ghast", Builder.of(Ghast::new, MobCategory.MONSTER).fireImmune().sized(4.0F, 4.0F).eyeHeight(2.6F).passengerAttachments(new float[] { 4.0625F }).ridingOffset(0.5F).clientTrackingRange(10).notInPeaceful());
/* 273 */   public static final EntityType<HappyGhast> HAPPY_GHAST = register("happy_ghast", Builder.of(HappyGhast::new, MobCategory.CREATURE).sized(4.0F, 4.0F).eyeHeight(2.6F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 4.0D, 1.7D), new Vec3(-1.7D, 4.0D, 0.0D), new Vec3(0.0D, 4.0D, -1.7D), new Vec3(1.7D, 4.0D, 0.0D) }).ridingOffset(0.5F).clientTrackingRange(10));
/* 274 */   public static final EntityType<Giant> GIANT = register("giant", Builder.of(Giant::new, MobCategory.MONSTER).sized(3.6F, 12.0F).eyeHeight(10.44F).ridingOffset(-3.75F).clientTrackingRange(10).notInPeaceful());
/* 275 */   public static final EntityType<GlowItemFrame> GLOW_ITEM_FRAME = register("glow_item_frame", Builder.of(GlowItemFrame::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.0F).clientTrackingRange(10).updateInterval(2147483647));
/* 276 */   public static final EntityType<GlowSquid> GLOW_SQUID = register("glow_squid", Builder.of(GlowSquid::new, MobCategory.UNDERGROUND_WATER_CREATURE).sized(0.8F, 0.8F).eyeHeight(0.4F).clientTrackingRange(10));
/* 277 */   public static final EntityType<Goat> GOAT = register("goat", Builder.of(Goat::new, MobCategory.CREATURE).sized(0.9F, 1.3F).passengerAttachments(new float[] { 1.1125F }).clientTrackingRange(10));
/* 278 */   public static final EntityType<Guardian> GUARDIAN = register("guardian", Builder.of(Guardian::new, MobCategory.MONSTER).sized(0.85F, 0.85F).eyeHeight(0.425F).passengerAttachments(new float[] { 0.975F }).clientTrackingRange(8).notInPeaceful());
/* 279 */   public static final EntityType<Hoglin> HOGLIN = register("hoglin", Builder.of(Hoglin::new, MobCategory.MONSTER).sized(1.3964844F, 1.4F).passengerAttachments(new float[] { 1.49375F }).clientTrackingRange(8));
/* 280 */   public static final EntityType<MinecartHopper> HOPPER_MINECART = register("hopper_minecart", Builder.of(MinecartHopper::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 281 */   public static final EntityType<Horse> HORSE = register("horse", Builder.of(Horse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).eyeHeight(1.52F).passengerAttachments(new float[] { 1.44375F }).clientTrackingRange(10));
/* 282 */   public static final EntityType<Husk> HUSK = register("husk", Builder.of(Husk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(new float[] { 2.075F }).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 283 */   public static final EntityType<Illusioner> ILLUSIONER = register("illusioner", Builder.of(Illusioner::new, MobCategory.MONSTER).sized(0.6F, 1.95F).passengerAttachments(new float[] { 2.0F }).ridingOffset(-0.6F).clientTrackingRange(8).notInPeaceful());
/* 284 */   public static final EntityType<Interaction> INTERACTION = register("interaction", Builder.of(Interaction::new, MobCategory.MISC).noLootTable().sized(0.0F, 0.0F).clientTrackingRange(10));
/* 285 */   public static final EntityType<IronGolem> IRON_GOLEM = register("iron_golem", Builder.of(IronGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10));
/* 286 */   public static final EntityType<ItemEntity> ITEM = register("item", Builder.of(ItemEntity::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).eyeHeight(0.2125F).clientTrackingRange(6).updateInterval(20));
/* 287 */   public static final EntityType<Display.ItemDisplay> ITEM_DISPLAY = register("item_display", Builder.of(ItemDisplay::new, MobCategory.MISC).noLootTable().sized(0.0F, 0.0F).clientTrackingRange(10).updateInterval(1));
/* 288 */   public static final EntityType<ItemFrame> ITEM_FRAME = register("item_frame", Builder.of(ItemFrame::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.0F).clientTrackingRange(10).updateInterval(2147483647));
/* 289 */   public static final EntityType<Boat> JUNGLE_BOAT = register("jungle_boat", Builder.of(boatFactory(() -> Items.JUNGLE_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 290 */   public static final EntityType<ChestBoat> JUNGLE_CHEST_BOAT = register("jungle_chest_boat", Builder.of(chestBoatFactory(() -> Items.JUNGLE_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 291 */   public static final EntityType<LeashFenceKnotEntity> LEASH_KNOT = register("leash_knot", Builder.of(LeashFenceKnotEntity::new, MobCategory.MISC).noLootTable().noSave().sized(0.375F, 0.5F).eyeHeight(0.0625F).clientTrackingRange(10).updateInterval(2147483647));
/* 292 */   public static final EntityType<LightningBolt> LIGHTNING_BOLT = register("lightning_bolt", Builder.of(LightningBolt::new, MobCategory.MISC).noLootTable().noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(2147483647));
/* 293 */   public static final EntityType<Llama> LLAMA = register("llama", Builder.of(Llama::new, MobCategory.CREATURE).sized(0.9F, 1.87F).eyeHeight(1.7765F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 1.37D, -0.3D) }).clientTrackingRange(10));
/* 294 */   public static final EntityType<LlamaSpit> LLAMA_SPIT = register("llama_spit", Builder.of(LlamaSpit::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 295 */   public static final EntityType<MagmaCube> MAGMA_CUBE = register("magma_cube", Builder.of(MagmaCube::new, MobCategory.MONSTER).fireImmune().sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(8).notInPeaceful());
/* 296 */   public static final EntityType<Boat> MANGROVE_BOAT = register("mangrove_boat", Builder.of(boatFactory(() -> Items.MANGROVE_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 297 */   public static final EntityType<ChestBoat> MANGROVE_CHEST_BOAT = register("mangrove_chest_boat", Builder.of(chestBoatFactory(() -> Items.MANGROVE_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 298 */   public static final EntityType<Mannequin> MANNEQUIN = register("mannequin", Builder.of(Mannequin::create, MobCategory.MISC).sized(0.6F, 1.8F).eyeHeight(1.62F).vehicleAttachment(Avatar.DEFAULT_VEHICLE_ATTACHMENT).clientTrackingRange(32).updateInterval(2));
/* 299 */   public static final EntityType<Marker> MARKER = register("marker", Builder.of(Marker::new, MobCategory.MISC).noLootTable().sized(0.0F, 0.0F).clientTrackingRange(0));
/* 300 */   public static final EntityType<Minecart> MINECART = register("minecart", Builder.of(Minecart::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 301 */   public static final EntityType<MushroomCow> MOOSHROOM = register("mooshroom", Builder.of(MushroomCow::new, MobCategory.CREATURE).sized(0.9F, 1.4F).eyeHeight(1.3F).passengerAttachments(new float[] { 1.36875F }).clientTrackingRange(10));
/* 302 */   public static final EntityType<Mule> MULE = register("mule", Builder.of(Mule::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).eyeHeight(1.52F).passengerAttachments(new float[] { 1.2125F }).clientTrackingRange(8));
/* 303 */   public static final EntityType<Nautilus> NAUTILUS = register("nautilus", Builder.of(Nautilus::new, MobCategory.WATER_CREATURE).sized(0.875F, 0.95F).passengerAttachments(new float[] { 1.1375F }).eyeHeight(0.2751F).clientTrackingRange(10));
/* 304 */   public static final EntityType<Boat> OAK_BOAT = register("oak_boat", Builder.of(boatFactory(() -> Items.OAK_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 305 */   public static final EntityType<ChestBoat> OAK_CHEST_BOAT = register("oak_chest_boat", Builder.of(chestBoatFactory(() -> Items.OAK_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 306 */   public static final EntityType<Ocelot> OCELOT = register("ocelot", Builder.of(Ocelot::new, MobCategory.CREATURE).sized(0.6F, 0.7F).passengerAttachments(new float[] { 0.6375F }).clientTrackingRange(10));
/* 307 */   public static final EntityType<OminousItemSpawner> OMINOUS_ITEM_SPAWNER = register("ominous_item_spawner", Builder.of(OminousItemSpawner::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(8));
/* 308 */   public static final EntityType<Painting> PAINTING = register("painting", Builder.of(Painting::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(2147483647));
/* 309 */   public static final EntityType<Boat> PALE_OAK_BOAT = register("pale_oak_boat", Builder.of(boatFactory(() -> Items.PALE_OAK_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 310 */   public static final EntityType<ChestBoat> PALE_OAK_CHEST_BOAT = register("pale_oak_chest_boat", Builder.of(chestBoatFactory(() -> Items.PALE_OAK_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 311 */   public static final EntityType<Panda> PANDA = register("panda", Builder.of(Panda::new, MobCategory.CREATURE).sized(1.3F, 1.25F).clientTrackingRange(10));
/* 312 */   public static final EntityType<Parched> PARCHED = register("parched", Builder.of(Parched::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 313 */   public static final EntityType<Parrot> PARROT = register("parrot", Builder.of(Parrot::new, MobCategory.CREATURE).sized(0.5F, 0.9F).eyeHeight(0.54F).passengerAttachments(new float[] { 0.4625F }).clientTrackingRange(8));
/* 314 */   public static final EntityType<Phantom> PHANTOM = register("phantom", Builder.of(Phantom::new, MobCategory.MONSTER).sized(0.9F, 0.5F).eyeHeight(0.175F).passengerAttachments(new float[] { 0.3375F }).ridingOffset(-0.125F).clientTrackingRange(8).notInPeaceful());
/* 315 */   public static final EntityType<Pig> PIG = register("pig", Builder.of(Pig::new, MobCategory.CREATURE).sized(0.9F, 0.9F).passengerAttachments(new float[] { 0.86875F }).clientTrackingRange(10));
/* 316 */   public static final EntityType<Piglin> PIGLIN = register("piglin", Builder.of(Piglin::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.79F).passengerAttachments(new float[] { 2.0125F }).ridingOffset(-0.7F).clientTrackingRange(8));
/* 317 */   public static final EntityType<PiglinBrute> PIGLIN_BRUTE = register("piglin_brute", Builder.of(PiglinBrute::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.79F).passengerAttachments(new float[] { 2.0125F }).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 318 */   public static final EntityType<Pillager> PILLAGER = register("pillager", Builder.of(Pillager::new, MobCategory.MONSTER).canSpawnFarFromPlayer().sized(0.6F, 1.95F).passengerAttachments(new float[] { 2.0F }).ridingOffset(-0.6F).clientTrackingRange(8).notInPeaceful());
/* 319 */   public static final EntityType<PolarBear> POLAR_BEAR = register("polar_bear", Builder.of(PolarBear::new, MobCategory.CREATURE).immuneTo(new Block[] { Blocks.POWDER_SNOW }).sized(1.4F, 1.4F).clientTrackingRange(10));
/* 320 */   public static final EntityType<ThrownSplashPotion> SPLASH_POTION = register("splash_potion", Builder.of(ThrownSplashPotion::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 321 */   public static final EntityType<ThrownLingeringPotion> LINGERING_POTION = register("lingering_potion", Builder.of(ThrownLingeringPotion::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 322 */   public static final EntityType<Pufferfish> PUFFERFISH = register("pufferfish", Builder.of(Pufferfish::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.7F).eyeHeight(0.455F).clientTrackingRange(4));
/* 323 */   public static final EntityType<Rabbit> RABBIT = register("rabbit", Builder.of(Rabbit::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8));
/* 324 */   public static final EntityType<Ravager> RAVAGER = register("ravager", Builder.of(Ravager::new, MobCategory.MONSTER).sized(1.95F, 2.2F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 2.2625D, -0.0625D) }).clientTrackingRange(10).notInPeaceful());
/* 325 */   public static final EntityType<Salmon> SALMON = register("salmon", Builder.of(Salmon::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).eyeHeight(0.26F).clientTrackingRange(4));
/* 326 */   public static final EntityType<Sheep> SHEEP = register("sheep", Builder.of(Sheep::new, MobCategory.CREATURE).sized(0.9F, 1.3F).eyeHeight(1.235F).passengerAttachments(new float[] { 1.2375F }).clientTrackingRange(10));
/* 327 */   public static final EntityType<Shulker> SHULKER = register("shulker", Builder.of(Shulker::new, MobCategory.MONSTER).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).eyeHeight(0.5F).clientTrackingRange(10));
/* 328 */   public static final EntityType<ShulkerBullet> SHULKER_BULLET = register("shulker_bullet", Builder.of(ShulkerBullet::new, MobCategory.MISC).noLootTable().sized(0.3125F, 0.3125F).clientTrackingRange(8));
/* 329 */   public static final EntityType<Silverfish> SILVERFISH = register("silverfish", Builder.of(Silverfish::new, MobCategory.MONSTER).sized(0.4F, 0.3F).eyeHeight(0.13F).passengerAttachments(new float[] { 0.2375F }).clientTrackingRange(8).notInPeaceful());
/* 330 */   public static final EntityType<Skeleton> SKELETON = register("skeleton", Builder.of(Skeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 331 */   public static final EntityType<SkeletonHorse> SKELETON_HORSE = register("skeleton_horse", Builder.of(SkeletonHorse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).eyeHeight(1.52F).passengerAttachments(new float[] { 1.31875F }).clientTrackingRange(10));
/* 332 */   public static final EntityType<Slime> SLIME = register("slime", Builder.of(Slime::new, MobCategory.MONSTER).sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10).notInPeaceful());
/* 333 */   public static final EntityType<SmallFireball> SMALL_FIREBALL = register("small_fireball", Builder.of(SmallFireball::new, MobCategory.MISC).noLootTable().sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
/* 334 */   public static final EntityType<Sniffer> SNIFFER = register("sniffer", Builder.of(Sniffer::new, MobCategory.CREATURE).sized(1.9F, 1.75F).eyeHeight(1.05F).passengerAttachments(new float[] { 2.09375F }).nameTagOffset(2.05F).clientTrackingRange(10));
/* 335 */   public static final EntityType<Snowball> SNOWBALL = register("snowball", Builder.of(Snowball::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 336 */   public static final EntityType<SnowGolem> SNOW_GOLEM = register("snow_golem", Builder.of(SnowGolem::new, MobCategory.MISC).immuneTo(new Block[] { Blocks.POWDER_SNOW }).sized(0.7F, 1.9F).eyeHeight(1.7F).clientTrackingRange(8));
/* 337 */   public static final EntityType<MinecartSpawner> SPAWNER_MINECART = register("spawner_minecart", Builder.of(MinecartSpawner::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 338 */   public static final EntityType<SpectralArrow> SPECTRAL_ARROW = register("spectral_arrow", Builder.of(SpectralArrow::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
/* 339 */   public static final EntityType<Spider> SPIDER = register("spider", Builder.of(Spider::new, MobCategory.MONSTER).sized(1.4F, 0.9F).eyeHeight(0.65F).passengerAttachments(new float[] { 0.765F }).clientTrackingRange(8).notInPeaceful());
/* 340 */   public static final EntityType<Boat> SPRUCE_BOAT = register("spruce_boat", Builder.of(boatFactory(() -> Items.SPRUCE_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 341 */   public static final EntityType<ChestBoat> SPRUCE_CHEST_BOAT = register("spruce_chest_boat", Builder.of(chestBoatFactory(() -> Items.SPRUCE_CHEST_BOAT), MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
/* 342 */   public static final EntityType<Squid> SQUID = register("squid", Builder.of(Squid::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.8F).eyeHeight(0.4F).clientTrackingRange(8));
/* 343 */   public static final EntityType<Stray> STRAY = register("stray", Builder.of(Stray::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).immuneTo(new Block[] { Blocks.POWDER_SNOW }).clientTrackingRange(8).notInPeaceful());
/* 344 */   public static final EntityType<Strider> STRIDER = register("strider", Builder.of(Strider::new, MobCategory.CREATURE).fireImmune().sized(0.9F, 1.7F).clientTrackingRange(10));
/* 345 */   public static final EntityType<Tadpole> TADPOLE = register("tadpole", Builder.of(Tadpole::new, MobCategory.CREATURE).sized(0.4F, 0.3F).eyeHeight(0.19500001F).clientTrackingRange(10));
/* 346 */   public static final EntityType<Display.TextDisplay> TEXT_DISPLAY = register("text_display", Builder.of(TextDisplay::new, MobCategory.MISC).noLootTable().sized(0.0F, 0.0F).clientTrackingRange(10).updateInterval(1));
/* 347 */   public static final EntityType<PrimedTnt> TNT = register("tnt", Builder.of(PrimedTnt::new, MobCategory.MISC).noLootTable().fireImmune().sized(0.98F, 0.98F).eyeHeight(0.15F).clientTrackingRange(10).updateInterval(10));
/* 348 */   public static final EntityType<MinecartTNT> TNT_MINECART = register("tnt_minecart", Builder.of(MinecartTNT::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(new float[] { 0.1875F }).clientTrackingRange(8));
/* 349 */   public static final EntityType<TraderLlama> TRADER_LLAMA = register("trader_llama", Builder.of(TraderLlama::new, MobCategory.CREATURE).sized(0.9F, 1.87F).eyeHeight(1.7765F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 1.37D, -0.3D) }).clientTrackingRange(10));
/* 350 */   public static final EntityType<ThrownTrident> TRIDENT = register("trident", Builder.of(ThrownTrident::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
/* 351 */   public static final EntityType<TropicalFish> TROPICAL_FISH = register("tropical_fish", Builder.of(TropicalFish::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.4F).eyeHeight(0.26F).clientTrackingRange(4));
/* 352 */   public static final EntityType<Turtle> TURTLE = register("turtle", Builder.of(Turtle::new, MobCategory.CREATURE).sized(1.2F, 0.4F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 0.55625D, -0.25D) }).clientTrackingRange(10));
/* 353 */   public static final EntityType<Vex> VEX = register("vex", Builder.of(Vex::new, MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F).eyeHeight(0.51875F).passengerAttachments(new float[] { 0.7375F }).ridingOffset(0.04F).clientTrackingRange(8).notInPeaceful());
/* 354 */   public static final EntityType<Villager> VILLAGER = register("villager", Builder.of(Villager::new, MobCategory.MISC).sized(0.6F, 1.95F).eyeHeight(1.62F).clientTrackingRange(10));
/* 355 */   public static final EntityType<Vindicator> VINDICATOR = register("vindicator", Builder.of(Vindicator::new, MobCategory.MONSTER).sized(0.6F, 1.95F).passengerAttachments(new float[] { 2.0F }).ridingOffset(-0.6F).clientTrackingRange(8).notInPeaceful());
/* 356 */   public static final EntityType<WanderingTrader> WANDERING_TRADER = register("wandering_trader", Builder.of(WanderingTrader::new, MobCategory.CREATURE).sized(0.6F, 1.95F).eyeHeight(1.62F).clientTrackingRange(10));
/* 357 */   public static final EntityType<Warden> WARDEN = register("warden", Builder.of(Warden::new, MobCategory.MONSTER).sized(0.9F, 2.9F).passengerAttachments(new float[] { 3.15F }).attach(EntityAttachment.WARDEN_CHEST, 0.0F, 1.6F, 0.0F).clientTrackingRange(16).fireImmune().notInPeaceful());
/* 358 */   public static final EntityType<WindCharge> WIND_CHARGE = register("wind_charge", Builder.of(WindCharge::new, MobCategory.MISC).noLootTable().sized(0.3125F, 0.3125F).eyeHeight(0.0F).clientTrackingRange(4).updateInterval(10));
/* 359 */   public static final EntityType<Witch> WITCH = register("witch", Builder.of(Witch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.62F).passengerAttachments(new float[] { 2.2625F }).clientTrackingRange(8).notInPeaceful());
/* 360 */   public static final EntityType<WitherBoss> WITHER = register("wither", Builder.of(WitherBoss::new, MobCategory.MONSTER).fireImmune().immuneTo(new Block[] { Blocks.WITHER_ROSE }).sized(0.9F, 3.5F).clientTrackingRange(10).notInPeaceful());
/* 361 */   public static final EntityType<WitherSkeleton> WITHER_SKELETON = register("wither_skeleton", Builder.of(WitherSkeleton::new, MobCategory.MONSTER).fireImmune().immuneTo(new Block[] { Blocks.WITHER_ROSE }).sized(0.7F, 2.4F).eyeHeight(2.1F).ridingOffset(-0.875F).clientTrackingRange(8).notInPeaceful());
/* 362 */   public static final EntityType<WitherSkull> WITHER_SKULL = register("wither_skull", Builder.of(WitherSkull::new, MobCategory.MISC).noLootTable().sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
/* 363 */   public static final EntityType<Wolf> WOLF = register("wolf", Builder.of(Wolf::new, MobCategory.CREATURE).sized(0.6F, 0.85F).eyeHeight(0.68F).passengerAttachments(new Vec3[] { new Vec3(0.0D, 0.81875D, -0.0625D) }).clientTrackingRange(10));
/* 364 */   public static final EntityType<Zoglin> ZOGLIN = register("zoglin", Builder.of(Zoglin::new, MobCategory.MONSTER).fireImmune().sized(1.3964844F, 1.4F).passengerAttachments(new float[] { 1.49375F }).clientTrackingRange(8).notInPeaceful());
/* 365 */   public static final EntityType<Zombie> ZOMBIE = register("zombie", Builder.of(Zombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(new float[] { 2.0125F }).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/* 366 */   public static final EntityType<ZombieHorse> ZOMBIE_HORSE = register("zombie_horse", Builder.of(ZombieHorse::new, MobCategory.MONSTER).sized(1.3964844F, 1.6F).eyeHeight(1.52F).passengerAttachments(new float[] { 1.31875F }).clientTrackingRange(10));
/* 367 */   public static final EntityType<ZombieNautilus> ZOMBIE_NAUTILUS = register("zombie_nautilus", Builder.of(ZombieNautilus::new, MobCategory.MONSTER).sized(0.875F, 0.95F).passengerAttachments(new float[] { 1.1375F }).eyeHeight(0.2751F).clientTrackingRange(10));
/* 368 */   public static final EntityType<ZombieVillager> ZOMBIE_VILLAGER = register("zombie_villager", Builder.of(ZombieVillager::new, MobCategory.MONSTER).sized(0.6F, 1.95F).passengerAttachments(new float[] { 2.125F }).ridingOffset(-0.7F).eyeHeight(1.74F).clientTrackingRange(8).notInPeaceful());
/* 369 */   public static final EntityType<ZombifiedPiglin> ZOMBIFIED_PIGLIN = register("zombified_piglin", Builder.of(ZombifiedPiglin::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).eyeHeight(1.79F).passengerAttachments(new float[] { 2.0F }).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 378 */   public static final EntityType<Player> PLAYER = register("player", Builder.createNothing(MobCategory.MISC).noSave().noSummon().sized(0.6F, 1.8F).eyeHeight(1.62F).vehicleAttachment(Avatar.DEFAULT_VEHICLE_ATTACHMENT).clientTrackingRange(32).updateInterval(2));
/* 379 */   public static final EntityType<FishingHook> FISHING_BOBBER = register("fishing_bobber", Builder.of(FishingHook::new, MobCategory.MISC).noLootTable().noSave().noSummon().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 384 */   private static final Set<EntityType<?>> OP_ONLY_CUSTOM_DATA = Set.of(FALLING_BLOCK, COMMAND_BLOCK_MINECART, SPAWNER_MINECART); private final EntityFactory<T> factory; private final MobCategory category; private final ImmutableSet<Block> immuneTo; private final boolean serialize; private final boolean summon; private final boolean fireImmune; private final boolean canSpawnFarFromPlayer; private final int clientTrackingRange; private final int updateInterval; private final String descriptionId; private Component description;
/*     */   private final Optional<ResourceKey<LootTable>> lootTable;
/*     */   private final EntityDimensions dimensions;
/*     */   private final float spawnDimensionsScale;
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   private final boolean allowedInPeaceful;
/*     */   
/* 391 */   public static Identifier getKey(EntityType<?> type) { return BuiltInRegistries.ENTITY_TYPE.getKey(type); }
/*     */ 
/*     */ 
/*     */   
/* 395 */   public static Optional<EntityType<?>> byString(String id) { return BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.tryParse(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType(EntityFactory<T> factory, MobCategory category, boolean serialize, boolean summon, boolean fireImmune, boolean canSpawnFarFromPlayer, ImmutableSet<Block> immuneTo, EntityDimensions dimensions, float spawnDimensionsScale, int clientTrackingRange, int updateInterval, String descriptionId, Optional<ResourceKey<LootTable>> lootTable, FeatureFlagSet requiredFeatures, boolean allowedInPeaceful) {
/*     */     this.builtInRegistryHolder = BuiltInRegistries.ENTITY_TYPE.createIntrusiveHolder(this);
/* 416 */     this.factory = factory;
/* 417 */     this.category = category;
/* 418 */     this.canSpawnFarFromPlayer = canSpawnFarFromPlayer;
/* 419 */     this.serialize = serialize;
/* 420 */     this.summon = summon;
/* 421 */     this.fireImmune = fireImmune;
/* 422 */     this.immuneTo = immuneTo;
/* 423 */     this.dimensions = dimensions;
/* 424 */     this.spawnDimensionsScale = spawnDimensionsScale;
/* 425 */     this.clientTrackingRange = clientTrackingRange;
/* 426 */     this.updateInterval = updateInterval;
/* 427 */     this.descriptionId = descriptionId;
/* 428 */     this.lootTable = lootTable;
/* 429 */     this.requiredFeatures = requiredFeatures;
/* 430 */     this.allowedInPeaceful = allowedInPeaceful;
/*     */   }
/*     */   
/*     */   public T spawn(ServerLevel level, ItemStack itemStack, LivingEntity user, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp) {
/*     */     Consumer<T> postSpawnConfig;
/* 435 */     if (itemStack != null) {
/* 436 */       postSpawnConfig = createDefaultStackConfig(level, itemStack, user);
/*     */     } else {
/* 438 */       postSpawnConfig = (entity -> {
/*     */         
/*     */         });
/* 441 */     }  return (T)spawn(level, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 448 */   public static <T extends Entity> Consumer<T> createDefaultStackConfig(Level level, ItemStack itemStack, LivingEntity user) { return appendDefaultStackConfig(entity -> {  }level, itemStack, user); }
/*     */ 
/*     */ 
/*     */   
/* 452 */   public static <T extends Entity> Consumer<T> appendDefaultStackConfig(Consumer<T> initialConfig, Level level, ItemStack itemStack, LivingEntity user) { return appendCustomEntityStackConfig(appendComponentsConfig(initialConfig, itemStack), level, itemStack, user); }
/*     */ 
/*     */ 
/*     */   
/* 456 */   public static <T extends Entity> Consumer<T> appendComponentsConfig(Consumer<T> initialConfig, ItemStack itemStack) { return initialConfig.andThen(entity -> entity.applyComponentsFromItemStack(itemStack)); }
/*     */ 
/*     */   
/*     */   public static <T extends Entity> Consumer<T> appendCustomEntityStackConfig(Consumer<T> initialConfig, Level level, ItemStack itemStack, LivingEntity user) {
/* 460 */     TypedEntityData<EntityType<?>> entityData = (TypedEntityData)itemStack.get(DataComponents.ENTITY_DATA);
/* 461 */     if (entityData != null) {
/* 462 */       return initialConfig.andThen(entity -> updateCustomEntityTag(level, user, entity, entityData));
/*     */     }
/* 464 */     return initialConfig;
/*     */   }
/*     */ 
/*     */   
/* 468 */   public T spawn(ServerLevel level, BlockPos spawnPos, EntitySpawnReason spawnReason) { return (T)spawn(level, null, spawnPos, spawnReason, false, false); }
/*     */ 
/*     */   
/*     */   public T spawn(ServerLevel level, Consumer<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp) {
/* 472 */     T entity = (T)create(level, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);
/* 473 */     if (entity != null) {
/* 474 */       level.addFreshEntityWithPassengers(entity);
/* 475 */       if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 476 */         mob.playAmbientSound(); }
/*     */     
/*     */     } 
/* 479 */     return entity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T create(ServerLevel level, Consumer<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp) {
/*     */     double yOff;
/* 486 */     T entity = (T)create(level, spawnReason);
/*     */     
/* 488 */     if (entity == null) {
/* 489 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 493 */     if (tryMoveDown) {
/* 494 */       entity.setPos(spawnPos.getX() + 0.5D, (spawnPos.getY() + 1), spawnPos.getZ() + 0.5D);
/*     */       
/* 496 */       yOff = getYOffset(level, spawnPos, movedUp, entity.getBoundingBox());
/*     */     } else {
/* 498 */       yOff = 0.0D;
/*     */     } 
/*     */     
/* 501 */     entity.snapTo(spawnPos.getX() + 0.5D, spawnPos.getY() + yOff, spawnPos.getZ() + 0.5D, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
/*     */ 
/*     */     
/* 504 */     if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 505 */       mob.yHeadRot = mob.getYRot();
/* 506 */       mob.yBodyRot = mob.getYRot();
/*     */       
/* 508 */       mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), spawnReason, null); }
/*     */ 
/*     */     
/* 511 */     if (postSpawnConfig != null) {
/* 512 */       postSpawnConfig.accept(entity);
/*     */     }
/*     */     
/* 515 */     return entity;
/*     */   }
/*     */   
/*     */   protected static double getYOffset(LevelReader level, BlockPos spawnPos, boolean movedUp, AABB entityBox) {
/* 519 */     AABB aabb = new AABB(spawnPos);
/* 520 */     if (movedUp) {
/* 521 */       aabb = aabb.expandTowards(0.0D, -1.0D, 0.0D);
/*     */     }
/* 523 */     Iterable<VoxelShape> shapes = level.getCollisions(null, aabb);
/*     */     
/* 525 */     return 1.0D + Shapes.collide(Direction.Axis.Y, entityBox, shapes, movedUp ? -2.0D : -1.0D);
/*     */   }
/*     */   
/*     */   public static void updateCustomEntityTag(Level level, LivingEntity user, Entity entity, TypedEntityData<EntityType<?>> entityData) {
/* 529 */     MinecraftServer server = level.getServer();
/* 530 */     if (server == null || entity == null) {
/*     */       return;
/*     */     }
/*     */     
/* 534 */     if (entity.getType() != entityData.type()) {
/*     */       return;
/*     */     }
/*     */     
/* 538 */     if (!level.isClientSide() && entity.getType().onlyOpCanSetNbt()) if (user instanceof Player) { Player player = (Player)user; if (!server.getPlayerList().isOp(player.nameAndId()))
/*     */           return;  }
/*     */       else
/*     */       { return; }
/* 542 */         entityData.loadInto(entity);
/*     */   }
/*     */ 
/*     */   
/* 546 */   public boolean canSerialize() { return this.serialize; }
/*     */ 
/*     */ 
/*     */   
/* 550 */   public boolean canSummon() { return this.summon; }
/*     */ 
/*     */ 
/*     */   
/* 554 */   public boolean fireImmune() { return this.fireImmune; }
/*     */ 
/*     */ 
/*     */   
/* 558 */   public boolean canSpawnFarFromPlayer() { return this.canSpawnFarFromPlayer; }
/*     */ 
/*     */ 
/*     */   
/* 562 */   public MobCategory getCategory() { return this.category; }
/*     */ 
/*     */ 
/*     */   
/* 566 */   public String getDescriptionId() { return this.descriptionId; }
/*     */ 
/*     */   
/*     */   public Component getDescription() {
/* 570 */     if (this.description == null) {
/* 571 */       this.description = Component.translatable(getDescriptionId());
/*     */     }
/* 573 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 578 */   public String toString() { return getDescriptionId(); }
/*     */ 
/*     */   
/*     */   public String toShortString() {
/* 582 */     int dot = getDescriptionId().lastIndexOf('.');
/* 583 */     return (dot == -1) ? getDescriptionId() : getDescriptionId().substring(dot + 1);
/*     */   }
/*     */ 
/*     */   
/* 587 */   public Optional<ResourceKey<LootTable>> getDefaultLootTable() { return this.lootTable; }
/*     */ 
/*     */ 
/*     */   
/* 591 */   public float getWidth() { return this.dimensions.width(); }
/*     */ 
/*     */ 
/*     */   
/* 595 */   public float getHeight() { return this.dimensions.height(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 600 */   public FeatureFlagSet requiredFeatures() { return this.requiredFeatures; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T create(Level level, EntitySpawnReason reason) {
/* 606 */     if (!isEnabled(level.enabledFeatures())) {
/* 607 */       return null;
/*     */     }
/* 609 */     return (T)this.factory.create(this, level);
/*     */   }
/*     */   
/*     */   public static Optional<Entity> create(ValueInput input, Level level, EntitySpawnReason reason) {
/* 613 */     return Util.ifElse(by(input).map(type -> type.create(level, reason)), entity -> 
/* 614 */         entity.load(input), () -> 
/* 615 */         LOGGER.warn("Skipping Entity with id {}", input.getStringOr("id", "[invalid]")));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Optional<Entity> create(EntityType<?> type, ValueInput input, Level level, EntitySpawnReason reason) {
/* 620 */     Optional<Entity> entity = Optional.ofNullable(type.create(level, reason));
/* 621 */     entity.ifPresent(e -> e.load(input));
/* 622 */     return entity;
/*     */   }
/*     */   
/*     */   public AABB getSpawnAABB(double x, double y, double z) {
/* 626 */     float halfWidth = this.spawnDimensionsScale * getWidth() / 2.0F;
/* 627 */     float height = this.spawnDimensionsScale * getHeight();
/* 628 */     return new AABB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + height, z + halfWidth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlockDangerous(BlockState state) {
/* 639 */     if (this.immuneTo.contains(state.getBlock())) {
/* 640 */       return false;
/*     */     }
/* 642 */     if (!this.fireImmune && NodeEvaluator.isBurningBlock(state)) {
/* 643 */       return true;
/*     */     }
/* 645 */     return (state.is(Blocks.WITHER_ROSE) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.CACTUS) || state.is(Blocks.POWDER_SNOW));
/*     */   }
/*     */ 
/*     */   
/* 649 */   public EntityDimensions getDimensions() { return this.dimensions; }
/*     */ 
/*     */ 
/*     */   
/* 653 */   public static Optional<EntityType<?>> by(ValueInput input) { return input.read("id", CODEC); }
/*     */   
/*     */   public static Entity loadEntityRecursive(CompoundTag tag, Level level, EntitySpawnReason reason, EntityProcessor postLoad)
/*     */   {
/* 657 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/* 658 */     try { Entity entity = loadEntityRecursive(TagValueInput.create(reporter, level.registryAccess(), tag), level, reason, postLoad);
/* 659 */       reporter.close(); return entity; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 663 */      } public static Entity loadEntityRecursive(EntityType<?> type, CompoundTag tag, Level level, EntitySpawnReason reason, EntityProcessor postLoad) { ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/* 664 */     try { Entity entity = loadEntityRecursive(type, TagValueInput.create(reporter, level.registryAccess(), tag), level, reason, postLoad);
/* 665 */       reporter.close(); return entity; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1)
/*     */       { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 670 */      } public static Entity loadEntityRecursive(ValueInput input, Level level, EntitySpawnReason reason, EntityProcessor postLoad) { Objects.requireNonNull(postLoad); return (Entity)loadStaticEntity(input, level, reason).map(postLoad::process)
/* 671 */       .map(entity -> loadPassengersRecursive(entity, input, level, reason, postLoad))
/* 672 */       .orElse(null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Entity loadEntityRecursive(EntityType<?> type, ValueInput input, Level level, EntitySpawnReason reason, EntityProcessor postLoad) {
/* 677 */     Objects.requireNonNull(postLoad); return (Entity)loadStaticEntity(type, input, level, reason).map(postLoad::process)
/* 678 */       .map(entity -> loadPassengersRecursive(entity, input, level, reason, postLoad))
/* 679 */       .orElse(null);
/*     */   }
/*     */   
/*     */   private static Entity loadPassengersRecursive(Entity entity, ValueInput input, Level level, EntitySpawnReason reason, EntityProcessor postLoad) {
/* 683 */     for (ValueInput passengerTag : input.childrenListOrEmpty("Passengers")) {
/* 684 */       Entity passenger = loadEntityRecursive(passengerTag, level, reason, postLoad);
/* 685 */       if (passenger != null) {
/* 686 */         passenger.startRiding(entity, true, false);
/*     */       }
/*     */     } 
/* 689 */     return entity;
/*     */   }
/*     */   
/*     */   public static Stream<Entity> loadEntitiesRecursive(ValueInput.ValueInputList entities, Level level, EntitySpawnReason reason) {
/* 693 */     return entities.stream()
/* 694 */       .mapMulti((tag, output) -> 
/* 695 */         loadEntityRecursive(tag, level, reason, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<Entity> loadStaticEntity(ValueInput input, Level level, EntitySpawnReason reason) {
/*     */     try {
/* 704 */       return create(input, level, reason);
/* 705 */     } catch (RuntimeException e) {
/* 706 */       LOGGER.warn("Exception loading entity: ", e);
/* 707 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<Entity> loadStaticEntity(EntityType<?> type, ValueInput input, Level level, EntitySpawnReason reason) {
/*     */     try {
/* 713 */       return create(type, input, level, reason);
/* 714 */     } catch (RuntimeException e) {
/* 715 */       LOGGER.warn("Exception loading entity: ", e);
/* 716 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 721 */   public int clientTrackingRange() { return this.clientTrackingRange; }
/*     */ 
/*     */ 
/*     */   
/* 725 */   public int updateInterval() { return this.updateInterval; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 730 */   public boolean trackDeltas() { return (this != PLAYER && this != LLAMA_SPIT && this != WITHER && this != BAT && this != ITEM_FRAME && this != GLOW_ITEM_FRAME && this != LEASH_KNOT && this != PAINTING && this != END_CRYSTAL && this != EVOKER_FANGS); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 744 */   public boolean is(TagKey<EntityType<?>> tag) { return this.builtInRegistryHolder.is(tag); }
/*     */ 
/*     */ 
/*     */   
/* 748 */   public boolean is(HolderSet<EntityType<?>> holderSet) { return holderSet.contains(this.builtInRegistryHolder); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 754 */   public T tryCast(Entity entity) { return (T)((entity.getType() == this) ? entity : null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 759 */   public Class<? extends Entity> getBaseClass() { return Entity.class; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 767 */   public Holder.Reference<EntityType<?>> builtInRegistryHolder() { return this.builtInRegistryHolder; }
/*     */ 
/*     */ 
/*     */   
/* 771 */   public boolean isAllowedInPeaceful() { return this.allowedInPeaceful; }
/*     */   public static class Builder<T extends Entity> extends Object { private final EntityType.EntityFactory<T> factory; private final MobCategory category; private ImmutableSet<Block> immuneTo; private boolean serialize; private boolean summon; private boolean fireImmune;
/*     */     private boolean canSpawnFarFromPlayer;
/*     */     private int clientTrackingRange;
/*     */     
/*     */     private Builder(EntityType.EntityFactory<T> factory, MobCategory category) {
/* 777 */       this.immuneTo = ImmutableSet.of();
/* 778 */       this.serialize = true;
/* 779 */       this.summon = true;
/*     */ 
/*     */       
/* 782 */       this.clientTrackingRange = 5;
/* 783 */       this.updateInterval = 3;
/* 784 */       this.dimensions = EntityDimensions.scalable(0.6F, 1.8F);
/* 785 */       this.spawnDimensionsScale = 1.0F;
/* 786 */       this.attachments = EntityAttachments.builder();
/* 787 */       this.requiredFeatures = FeatureFlags.VANILLA_SET;
/* 788 */       this.lootTable = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("entities/"))));
/* 789 */       this.descriptionId = (id -> Util.makeDescriptionId("entity", id.identifier()));
/* 790 */       this.allowedInPeaceful = true;
/*     */ 
/*     */       
/* 793 */       this.factory = factory;
/* 794 */       this.category = category;
/* 795 */       this.canSpawnFarFromPlayer = (category == MobCategory.CREATURE || category == MobCategory.MISC);
/*     */     }
/*     */     private int updateInterval; private EntityDimensions dimensions; private float spawnDimensionsScale; private EntityAttachments.Builder attachments; private FeatureFlagSet requiredFeatures; private DependantName<EntityType<?>, Optional<ResourceKey<LootTable>>> lootTable; private final DependantName<EntityType<?>, String> descriptionId; private boolean allowedInPeaceful;
/*     */     
/* 799 */     public static <T extends Entity> Builder<T> of(EntityType.EntityFactory<T> factory, MobCategory category) { return new Builder(factory, category); }
/*     */ 
/*     */ 
/*     */     
/* 803 */     public static <T extends Entity> Builder<T> createNothing(MobCategory category) { return new Builder((t, l) -> null, category); }
/*     */ 
/*     */     
/*     */     public Builder<T> sized(float width, float height) {
/* 807 */       this.dimensions = EntityDimensions.scalable(width, height);
/* 808 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> spawnDimensionsScale(float scale) {
/* 812 */       this.spawnDimensionsScale = scale;
/* 813 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> eyeHeight(float eyeHeight) {
/* 817 */       this.dimensions = this.dimensions.withEyeHeight(eyeHeight);
/* 818 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> passengerAttachments(float... offsetYs) {
/* 822 */       for (float offsetY : offsetYs) {
/* 823 */         this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, 0.0F, offsetY, 0.0F);
/*     */       }
/* 825 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> passengerAttachments(Vec3... points) {
/* 829 */       for (Vec3 point : points) {
/* 830 */         this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, point);
/*     */       }
/* 832 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 836 */     public Builder<T> vehicleAttachment(Vec3 point) { return attach(EntityAttachment.VEHICLE, point); }
/*     */ 
/*     */ 
/*     */     
/* 840 */     public Builder<T> ridingOffset(float ridingOffset) { return attach(EntityAttachment.VEHICLE, 0.0F, -ridingOffset, 0.0F); }
/*     */ 
/*     */ 
/*     */     
/* 844 */     public Builder<T> nameTagOffset(float nameTagOffset) { return attach(EntityAttachment.NAME_TAG, 0.0F, nameTagOffset, 0.0F); }
/*     */ 
/*     */     
/*     */     public Builder<T> attach(EntityAttachment attachment, float x, float y, float z) {
/* 848 */       this.attachments = this.attachments.attach(attachment, x, y, z);
/* 849 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> attach(EntityAttachment attachment, Vec3 point) {
/* 853 */       this.attachments = this.attachments.attach(attachment, point);
/* 854 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noSummon() {
/* 858 */       this.summon = false;
/* 859 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noSave() {
/* 863 */       this.serialize = false;
/* 864 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> fireImmune() {
/* 868 */       this.fireImmune = true;
/* 869 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> immuneTo(Block... blocks) {
/* 873 */       this.immuneTo = ImmutableSet.copyOf(blocks);
/* 874 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> canSpawnFarFromPlayer() {
/* 878 */       this.canSpawnFarFromPlayer = true;
/* 879 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> clientTrackingRange(int clientChunkRange) {
/* 883 */       this.clientTrackingRange = clientChunkRange;
/* 884 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> updateInterval(int updateInterval) {
/* 888 */       this.updateInterval = updateInterval;
/* 889 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> requiredFeatures(FeatureFlag... flags) {
/* 893 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 894 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noLootTable() {
/* 898 */       this.lootTable = DependantName.fixed(Optional.empty());
/* 899 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> notInPeaceful() {
/* 903 */       this.allowedInPeaceful = false;
/* 904 */       return this;
/*     */     }
/*     */     
/*     */     public EntityType<T> build(ResourceKey<EntityType<?>> name) {
/* 908 */       if (this.serialize) {
/* 909 */         Util.fetchChoiceType(References.ENTITY_TREE, name.identifier().toString());
/*     */       }
/*     */       
/* 912 */       return new EntityType(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions.withAttachments(this.attachments), this.spawnDimensionsScale, this.clientTrackingRange, this.updateInterval, (String)this.descriptionId.get(name), (Optional)this.lootTable.get(name), this.requiredFeatures, this.allowedInPeaceful);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/* 917 */   private static EntityFactory<Boat> boatFactory(Supplier<Item> boatItem) { return (entityType, level) -> new Boat(entityType, level, boatItem); }
/*     */ 
/*     */ 
/*     */   
/* 921 */   private static EntityFactory<ChestBoat> chestBoatFactory(Supplier<Item> dropItem) { return (entityType, level) -> new ChestBoat(entityType, level, dropItem); }
/*     */ 
/*     */ 
/*     */   
/* 925 */   private static EntityFactory<Raft> raftFactory(Supplier<Item> dropItem) { return (entityType, level) -> new Raft(entityType, level, dropItem); }
/*     */ 
/*     */ 
/*     */   
/* 929 */   private static EntityFactory<ChestRaft> chestRaftFactory(Supplier<Item> dropItem) { return (entityType, level) -> new ChestRaft(entityType, level, dropItem); }
/*     */ 
/*     */ 
/*     */   
/* 933 */   public boolean onlyOpCanSetNbt() { return OP_ONLY_CUSTOM_DATA.contains(this); }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface EntityFactory<T extends Entity> {
/*     */     T create(EntityType<T> param1EntityType, Level param1Level);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */