/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Rotations;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolemState;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ public class EntityDataSerializers
/*     */ {
/*  49 */   private static final CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> SERIALIZERS = CrudeIncrementalIntIdentityHashBiMap.create(16);
/*     */   
/*  51 */   public static final EntityDataSerializer<Byte> BYTE = EntityDataSerializer.forValueType(ByteBufCodecs.BYTE);
/*     */   
/*  53 */   public static final EntityDataSerializer<Integer> INT = EntityDataSerializer.forValueType(ByteBufCodecs.VAR_INT);
/*     */   
/*  55 */   public static final EntityDataSerializer<Long> LONG = EntityDataSerializer.forValueType(ByteBufCodecs.VAR_LONG);
/*     */   
/*  57 */   public static final EntityDataSerializer<Float> FLOAT = EntityDataSerializer.forValueType(ByteBufCodecs.FLOAT);
/*     */   
/*  59 */   public static final EntityDataSerializer<String> STRING = EntityDataSerializer.forValueType(ByteBufCodecs.STRING_UTF8);
/*     */   
/*  61 */   public static final EntityDataSerializer<Component> COMPONENT = EntityDataSerializer.forValueType(ComponentSerialization.TRUSTED_STREAM_CODEC);
/*     */   
/*  63 */   public static final EntityDataSerializer<Optional<Component>> OPTIONAL_COMPONENT = EntityDataSerializer.forValueType(ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC);
/*     */   
/*  65 */   public static final EntityDataSerializer<ItemStack> ITEM_STACK = new EntityDataSerializer<ItemStack>()
/*     */     {
/*     */       public StreamCodec<? super RegistryFriendlyByteBuf, ItemStack> codec() {
/*  68 */         return ItemStack.OPTIONAL_STREAM_CODEC;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  73 */       public ItemStack copy(ItemStack value) { return value.copy(); }
/*     */     };
/*     */ 
/*     */   
/*  77 */   public static final EntityDataSerializer<BlockState> BLOCK_STATE = EntityDataSerializer.forValueType(ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY));
/*     */   
/*  79 */   private static final StreamCodec<ByteBuf, Optional<BlockState>> OPTIONAL_BLOCK_STATE_CODEC = new StreamCodec<ByteBuf, Optional<BlockState>>()
/*     */     {
/*     */       public void encode(ByteBuf output, Optional<BlockState> value) {
/*  82 */         if (value.isPresent()) {
/*  83 */           VarInt.write(output, Block.getId((BlockState)value.get()));
/*     */         } else {
/*  85 */           VarInt.write(output, 0);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<BlockState> decode(ByteBuf input) {
/*  91 */         int id = VarInt.read(input);
/*  92 */         if (id == 0) {
/*  93 */           return Optional.empty();
/*     */         }
/*  95 */         return Optional.of(Block.stateById(id));
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 100 */   public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE = EntityDataSerializer.forValueType(OPTIONAL_BLOCK_STATE_CODEC);
/*     */   
/* 102 */   public static final EntityDataSerializer<Boolean> BOOLEAN = EntityDataSerializer.forValueType(ByteBufCodecs.BOOL);
/*     */   
/* 104 */   public static final EntityDataSerializer<ParticleOptions> PARTICLE = EntityDataSerializer.forValueType(ParticleTypes.STREAM_CODEC);
/*     */   
/* 106 */   public static final EntityDataSerializer<List<ParticleOptions>> PARTICLES = EntityDataSerializer.forValueType(ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*     */   
/* 108 */   public static final EntityDataSerializer<Rotations> ROTATIONS = EntityDataSerializer.forValueType(Rotations.STREAM_CODEC);
/*     */   
/* 110 */   public static final EntityDataSerializer<BlockPos> BLOCK_POS = EntityDataSerializer.forValueType(BlockPos.STREAM_CODEC);
/*     */   
/* 112 */   public static final EntityDataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = EntityDataSerializer.forValueType(BlockPos.STREAM_CODEC.apply(ByteBufCodecs::optional));
/*     */   
/* 114 */   public static final EntityDataSerializer<Direction> DIRECTION = EntityDataSerializer.forValueType(Direction.STREAM_CODEC);
/*     */   
/* 116 */   public static final EntityDataSerializer<Optional<EntityReference<LivingEntity>>> OPTIONAL_LIVING_ENTITY_REFERENCE = EntityDataSerializer.forValueType(EntityReference.streamCodec().apply(ByteBufCodecs::optional));
/*     */   
/* 118 */   public static final EntityDataSerializer<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = EntityDataSerializer.forValueType(GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional));
/*     */   
/* 120 */   public static final EntityDataSerializer<VillagerData> VILLAGER_DATA = EntityDataSerializer.forValueType(VillagerData.STREAM_CODEC);
/*     */   
/* 122 */   private static final StreamCodec<ByteBuf, OptionalInt> OPTIONAL_UNSIGNED_INT_CODEC = new StreamCodec<ByteBuf, OptionalInt>()
/*     */     {
/*     */       public OptionalInt decode(ByteBuf input) {
/* 125 */         int v = VarInt.read(input);
/* 126 */         return (v == 0) ? OptionalInt.empty() : OptionalInt.of(v - 1);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 131 */       public void encode(ByteBuf output, OptionalInt value) { VarInt.write(output, value.orElse(-1) + 1); }
/*     */     };
/*     */ 
/*     */   
/* 135 */   public static final EntityDataSerializer<OptionalInt> OPTIONAL_UNSIGNED_INT = EntityDataSerializer.forValueType(OPTIONAL_UNSIGNED_INT_CODEC);
/*     */   
/* 137 */   public static final EntityDataSerializer<Pose> POSE = EntityDataSerializer.forValueType(Pose.STREAM_CODEC);
/*     */   
/* 139 */   public static final EntityDataSerializer<Holder<CatVariant>> CAT_VARIANT = EntityDataSerializer.forValueType(CatVariant.STREAM_CODEC);
/*     */   
/* 141 */   public static final EntityDataSerializer<Holder<ChickenVariant>> CHICKEN_VARIANT = EntityDataSerializer.forValueType(ChickenVariant.STREAM_CODEC);
/*     */   
/* 143 */   public static final EntityDataSerializer<Holder<CowVariant>> COW_VARIANT = EntityDataSerializer.forValueType(CowVariant.STREAM_CODEC);
/*     */   
/* 145 */   public static final EntityDataSerializer<Holder<WolfVariant>> WOLF_VARIANT = EntityDataSerializer.forValueType(WolfVariant.STREAM_CODEC);
/*     */   
/* 147 */   public static final EntityDataSerializer<Holder<WolfSoundVariant>> WOLF_SOUND_VARIANT = EntityDataSerializer.forValueType(WolfSoundVariant.STREAM_CODEC);
/*     */   
/* 149 */   public static final EntityDataSerializer<Holder<FrogVariant>> FROG_VARIANT = EntityDataSerializer.forValueType(FrogVariant.STREAM_CODEC);
/*     */   
/* 151 */   public static final EntityDataSerializer<Holder<PigVariant>> PIG_VARIANT = EntityDataSerializer.forValueType(PigVariant.STREAM_CODEC);
/*     */   
/* 153 */   public static final EntityDataSerializer<Holder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = EntityDataSerializer.forValueType(ZombieNautilusVariant.STREAM_CODEC);
/*     */   
/* 155 */   public static final EntityDataSerializer<Holder<PaintingVariant>> PAINTING_VARIANT = EntityDataSerializer.forValueType(PaintingVariant.STREAM_CODEC);
/*     */   
/* 157 */   public static final EntityDataSerializer<Armadillo.ArmadilloState> ARMADILLO_STATE = EntityDataSerializer.forValueType(Armadillo.ArmadilloState.STREAM_CODEC);
/*     */   
/* 159 */   public static final EntityDataSerializer<Sniffer.State> SNIFFER_STATE = EntityDataSerializer.forValueType(Sniffer.State.STREAM_CODEC);
/*     */   
/* 161 */   public static final EntityDataSerializer<WeatheringCopper.WeatherState> WEATHERING_COPPER_STATE = EntityDataSerializer.forValueType(WeatheringCopper.WeatherState.STREAM_CODEC);
/*     */   
/* 163 */   public static final EntityDataSerializer<CopperGolemState> COPPER_GOLEM_STATE = EntityDataSerializer.forValueType(CopperGolemState.STREAM_CODEC);
/*     */   
/* 165 */   public static final EntityDataSerializer<Vector3fc> VECTOR3 = EntityDataSerializer.forValueType(ByteBufCodecs.VECTOR3F);
/*     */   
/* 167 */   public static final EntityDataSerializer<Quaternionfc> QUATERNION = EntityDataSerializer.forValueType(ByteBufCodecs.QUATERNIONF);
/*     */   
/* 169 */   public static final EntityDataSerializer<ResolvableProfile> RESOLVABLE_PROFILE = EntityDataSerializer.forValueType(ResolvableProfile.STREAM_CODEC);
/*     */   
/* 171 */   public static final EntityDataSerializer<HumanoidArm> HUMANOID_ARM = EntityDataSerializer.forValueType(HumanoidArm.STREAM_CODEC);
/*     */   
/*     */   static  {
/* 174 */     registerSerializer(BYTE);
/* 175 */     registerSerializer(INT);
/* 176 */     registerSerializer(LONG);
/* 177 */     registerSerializer(FLOAT);
/* 178 */     registerSerializer(STRING);
/* 179 */     registerSerializer(COMPONENT);
/* 180 */     registerSerializer(OPTIONAL_COMPONENT);
/* 181 */     registerSerializer(ITEM_STACK);
/* 182 */     registerSerializer(BOOLEAN);
/* 183 */     registerSerializer(ROTATIONS);
/* 184 */     registerSerializer(BLOCK_POS);
/* 185 */     registerSerializer(OPTIONAL_BLOCK_POS);
/* 186 */     registerSerializer(DIRECTION);
/* 187 */     registerSerializer(OPTIONAL_LIVING_ENTITY_REFERENCE);
/* 188 */     registerSerializer(BLOCK_STATE);
/* 189 */     registerSerializer(OPTIONAL_BLOCK_STATE);
/* 190 */     registerSerializer(PARTICLE);
/* 191 */     registerSerializer(PARTICLES);
/* 192 */     registerSerializer(VILLAGER_DATA);
/* 193 */     registerSerializer(OPTIONAL_UNSIGNED_INT);
/* 194 */     registerSerializer(POSE);
/* 195 */     registerSerializer(CAT_VARIANT);
/* 196 */     registerSerializer(COW_VARIANT);
/* 197 */     registerSerializer(WOLF_VARIANT);
/* 198 */     registerSerializer(WOLF_SOUND_VARIANT);
/* 199 */     registerSerializer(FROG_VARIANT);
/* 200 */     registerSerializer(PIG_VARIANT);
/* 201 */     registerSerializer(CHICKEN_VARIANT);
/* 202 */     registerSerializer(ZOMBIE_NAUTILUS_VARIANT);
/* 203 */     registerSerializer(OPTIONAL_GLOBAL_POS);
/* 204 */     registerSerializer(PAINTING_VARIANT);
/* 205 */     registerSerializer(SNIFFER_STATE);
/* 206 */     registerSerializer(ARMADILLO_STATE);
/* 207 */     registerSerializer(COPPER_GOLEM_STATE);
/* 208 */     registerSerializer(WEATHERING_COPPER_STATE);
/* 209 */     registerSerializer(VECTOR3);
/* 210 */     registerSerializer(QUATERNION);
/* 211 */     registerSerializer(RESOLVABLE_PROFILE);
/* 212 */     registerSerializer(HUMANOID_ARM);
/*     */   }
/*     */ 
/*     */   
/* 216 */   public static void registerSerializer(EntityDataSerializer<?> serializer) { SERIALIZERS.add(serializer); }
/*     */ 
/*     */ 
/*     */   
/* 220 */   public static EntityDataSerializer<?> getSerializer(int id) { return (EntityDataSerializer)SERIALIZERS.byId(id); }
/*     */ 
/*     */ 
/*     */   
/* 224 */   public static int getSerializedId(EntityDataSerializer<?> serializer) { return SERIALIZERS.getId(serializer); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\EntityDataSerializers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */