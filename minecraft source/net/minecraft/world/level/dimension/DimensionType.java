/*     */ package net.minecraft.world.level.dimension;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function14;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.file.Path;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public final class DimensionType extends Record {
/*     */   private final boolean hasFixedTime;
/*     */   private final boolean hasSkyLight;
/*     */   private final boolean hasCeiling;
/*     */   private final double coordinateScale;
/*     */   private final int minY;
/*     */   private final int height;
/*     */   private final int logicalHeight;
/*     */   private final TagKey<Block> infiniburn;
/*     */   
/*  31 */   public boolean hasFixedTime() { return this.hasFixedTime; } private final float ambientLight; private final MonsterSettings monsterSettings; private final Skybox skybox; private final CardinalLightType cardinalLightType; private final EnvironmentAttributeMap attributes; private final HolderSet<Timeline> timelines; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/DimensionType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/DimensionType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/DimensionType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/DimensionType;
/*  31 */     //   0	8	1	o	Ljava/lang/Object; } public boolean hasSkyLight() { return this.hasSkyLight; } public boolean hasCeiling() { return this.hasCeiling; } public double coordinateScale() { return this.coordinateScale; } public int minY() { return this.minY; } public int height() { return this.height; } public int logicalHeight() { return this.logicalHeight; } public TagKey<Block> infiniburn() { return this.infiniburn; } public float ambientLight() { return this.ambientLight; } public MonsterSettings monsterSettings() { return this.monsterSettings; } public Skybox skybox() { return this.skybox; } public CardinalLightType cardinalLightType() { return this.cardinalLightType; } public EnvironmentAttributeMap attributes() { return this.attributes; } public HolderSet<Timeline> timelines() { return this.timelines; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final int BITS_FOR_Y = BlockPos.PACKED_Y_LENGTH;
/*     */   
/*     */   public static final int MIN_HEIGHT = 16;
/*  51 */   public static final int Y_SIZE = (1 << BITS_FOR_Y) - 32;
/*     */   
/*  53 */   public static final int MAX_Y = (Y_SIZE >> 1) - 1;
/*     */   
/*  55 */   public static final int MIN_Y = MAX_Y - Y_SIZE + 1;
/*     */ 
/*     */   
/*  58 */   public static final int WAY_ABOVE_MAX_Y = MAX_Y << 4;
/*     */   
/*  60 */   public static final int WAY_BELOW_MIN_Y = MIN_Y << 4;
/*     */   public static final class MonsterSettings extends Record { private final IntProvider monsterSpawnLightTest; private final int monsterSpawnBlockLightLimit;
/*  62 */     public MonsterSettings(IntProvider monsterSpawnLightTest, int monsterSpawnBlockLightLimit) { this.monsterSpawnLightTest = monsterSpawnLightTest; this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #62	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #62	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #62	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*  62 */       //   0	8	1	o	Ljava/lang/Object; } public IntProvider monsterSpawnLightTest() { return this.monsterSpawnLightTest; } public int monsterSpawnBlockLightLimit() { return this.monsterSpawnBlockLightLimit; }
/*     */ 
/*     */ 
/*     */     
/*  66 */     public static final MapCodec<MonsterSettings> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  67 */           IntProvider.codec(0, 15).fieldOf("monster_spawn_light_level").forGetter(MonsterSettings::monsterSpawnLightTest), 
/*  68 */           Codec.intRange(0, 15).fieldOf("monster_spawn_block_light_limit").forGetter(MonsterSettings::monsterSpawnBlockLightLimit))
/*  69 */         .apply(i, MonsterSettings::new)); }
/*     */ 
/*     */   
/*  72 */   public static final Codec<DimensionType> DIRECT_CODEC = createDirectCodec(EnvironmentAttributeMap.CODEC);
/*  73 */   public static final Codec<DimensionType> NETWORK_CODEC = createDirectCodec(EnvironmentAttributeMap.NETWORK_CODEC);
/*     */ 
/*     */   
/*  76 */   private static Codec<DimensionType> createDirectCodec(Codec<EnvironmentAttributeMap> attributeMapCodec) { return ExtraCodecs.catchDecoderException(RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/*  77 */             .optionalFieldOf("has_fixed_time", Boolean.valueOf(false)).forGetter(DimensionType::hasFixedTime), Codec.BOOL
/*  78 */             .fieldOf("has_skylight").forGetter(DimensionType::hasSkyLight), Codec.BOOL
/*  79 */             .fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling), 
/*  80 */             Codec.doubleRange(9.999999747378752E-6D, 3.0E7D).fieldOf("coordinate_scale").forGetter(DimensionType::coordinateScale), 
/*  81 */             Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y").forGetter(DimensionType::minY), 
/*  82 */             Codec.intRange(16, Y_SIZE).fieldOf("height").forGetter(DimensionType::height), 
/*  83 */             Codec.intRange(0, Y_SIZE).fieldOf("logical_height").forGetter(DimensionType::logicalHeight), 
/*  84 */             TagKey.hashedCodec(Registries.BLOCK).fieldOf("infiniburn").forGetter(DimensionType::infiniburn), Codec.FLOAT
/*  85 */             .fieldOf("ambient_light").forGetter(DimensionType::ambientLight), MonsterSettings.CODEC
/*  86 */             .forGetter(DimensionType::monsterSettings), Skybox.CODEC
/*  87 */             .optionalFieldOf("skybox", Skybox.OVERWORLD).forGetter(DimensionType::skybox), CardinalLightType.CODEC
/*  88 */             .optionalFieldOf("cardinal_light", CardinalLightType.DEFAULT).forGetter(DimensionType::cardinalLightType), attributeMapCodec
/*  89 */             .optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(DimensionType::attributes), 
/*  90 */             RegistryCodecs.homogeneousList(Registries.TIMELINE).optionalFieldOf("timelines", HolderSet.empty()).forGetter(DimensionType::timelines))
/*  91 */           .apply(i, DimensionType::new))); }
/*     */ 
/*     */   
/*  94 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<DimensionType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.DIMENSION_TYPE);
/*     */   
/*     */   public DimensionType(boolean hasFixedTime, boolean hasSkyLight, boolean hasCeiling, double coordinateScale, int minY, int height, int logicalHeight, TagKey<Block> infiniburn, float ambientLight, MonsterSettings monsterSettings, Skybox skybox, CardinalLightType cardinalLightType, EnvironmentAttributeMap attributes, HolderSet<Timeline> timelines) {
/*  97 */     if (height < 16) {
/*  98 */       throw new IllegalStateException("height has to be at least 16");
/*     */     }
/*     */     
/* 101 */     if (minY + height > MAX_Y + 1) {
/* 102 */       throw new IllegalStateException("min_y + height cannot be higher than: " + MAX_Y + 1);
/*     */     }
/*     */     
/* 105 */     if (logicalHeight > height) {
/* 106 */       throw new IllegalStateException("logical_height cannot be higher than height");
/*     */     }
/*     */     
/* 109 */     if (height % 16 != 0) {
/* 110 */       throw new IllegalStateException("height has to be multiple of 16");
/*     */     }
/*     */     
/* 113 */     if (minY % 16 != 0)
/* 114 */       throw new IllegalStateException("min_y has to be a multiple of 16");  this.hasFixedTime = hasFixedTime; this.hasSkyLight = hasSkyLight; this.hasCeiling = hasCeiling; this.coordinateScale = coordinateScale; this.minY = minY; this.height = height; this.logicalHeight = logicalHeight; this.infiniburn = infiniburn; this.ambientLight = ambientLight; this.monsterSettings = monsterSettings;
/*     */     this.skybox = skybox;
/*     */     this.cardinalLightType = cardinalLightType;
/*     */     this.attributes = attributes;
/* 118 */     this.timelines = timelines; } public static final float[] MOON_BRIGHTNESS_PER_PHASE = { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static final Codec<Holder<DimensionType>> CODEC = RegistryFileCodec.create(Registries.DIMENSION_TYPE, DIRECT_CODEC);
/*     */   
/*     */   public static double getTeleportationScale(DimensionType lastDimensionType, DimensionType newDimensionType) {
/* 125 */     double oldScale = lastDimensionType.coordinateScale();
/* 126 */     double newScale = newDimensionType.coordinateScale();
/*     */     
/* 128 */     return oldScale / newScale;
/*     */   }
/*     */   
/*     */   public static Path getStorageFolder(ResourceKey<Level> name, Path baseFolder) {
/* 132 */     if (name == Level.OVERWORLD) {
/* 133 */       return baseFolder;
/*     */     }
/* 135 */     if (name == Level.END) {
/* 136 */       return baseFolder.resolve("DIM1");
/*     */     }
/* 138 */     if (name == Level.NETHER) {
/* 139 */       return baseFolder.resolve("DIM-1");
/*     */     }
/* 141 */     return baseFolder.resolve("dimensions").resolve(name.identifier().getNamespace()).resolve(name.identifier().getPath());
/*     */   }
/*     */ 
/*     */   
/* 145 */   public IntProvider monsterSpawnLightTest() { return this.monsterSettings.monsterSpawnLightTest(); }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public int monsterSpawnBlockLightLimit() { return this.monsterSettings.monsterSpawnBlockLightLimit(); }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public boolean hasEndFlashes() { return (this.skybox == Skybox.END); }
/*     */   
/*     */   public enum Skybox
/*     */     implements StringRepresentable {
/* 157 */     NONE("none"),
/* 158 */     OVERWORLD("overworld"),
/* 159 */     END("end"); public static final Codec<Skybox> CODEC; private final String name;
/*     */     
/*     */     static  {
/* 162 */       CODEC = StringRepresentable.fromEnum(Skybox::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 167 */     Skybox(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */   
/*     */   public enum CardinalLightType
/*     */     implements StringRepresentable {
/* 177 */     DEFAULT("default"),
/* 178 */     NETHER("nether"); public static final Codec<CardinalLightType> CODEC; private final String name;
/*     */     
/*     */     static  {
/* 181 */       CODEC = StringRepresentable.fromEnum(CardinalLightType::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 186 */     CardinalLightType(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\DimensionType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */