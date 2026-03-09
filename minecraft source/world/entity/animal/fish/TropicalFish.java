/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.item.component.TooltipProvider;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class TropicalFish
/*     */   extends AbstractSchoolingFish {
/*  49 */   public static final Variant DEFAULT_VARIANT = new Variant(Pattern.KOB, DyeColor.WHITE, DyeColor.WHITE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(TropicalFish.class, EntityDataSerializers.INT);
/*     */   
/*     */   public enum Base {
/*  59 */     SMALL(0),
/*  60 */     LARGE(1);
/*     */ 
/*     */     
/*     */     private final int id;
/*     */ 
/*     */     
/*  66 */     Base(int id) { this.id = id; } }
/*     */   public static final class Variant extends Record { private final TropicalFish.Pattern pattern; private final DyeColor baseColor;
/*     */     private final DyeColor patternColor;
/*     */     
/*  70 */     public Variant(TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) { this.pattern = pattern; this.baseColor = baseColor; this.patternColor = patternColor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  70 */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant; } public TropicalFish.Pattern pattern() { return this.pattern; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;
/*  70 */       //   0	8	1	o	Ljava/lang/Object; } public DyeColor baseColor() { return this.baseColor; } public DyeColor patternColor() { return this.patternColor; }
/*  71 */     public static final Codec<Variant> CODEC = Codec.INT.xmap(Variant::new, Variant::getPackedId);
/*     */ 
/*     */     
/*  74 */     public Variant(int packedId) { this(TropicalFish.getPattern(packedId), TropicalFish.getBaseColor(packedId), TropicalFish.getPatternColor(packedId)); }
/*     */ 
/*     */ 
/*     */     
/*  78 */     public int getPackedId() { return TropicalFish.packVariant(this.pattern, this.baseColor, this.patternColor); } }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final List<Variant> COMMON_VARIANTS = List.of(new Variant[] { new Variant(Pattern.STRIPEY, DyeColor.ORANGE, DyeColor.GRAY), new Variant(Pattern.FLOPPER, DyeColor.GRAY, DyeColor.GRAY), new Variant(Pattern.FLOPPER, DyeColor.GRAY, DyeColor.BLUE), new Variant(Pattern.CLAYFISH, DyeColor.WHITE, DyeColor.GRAY), new Variant(Pattern.SUNSTREAK, DyeColor.BLUE, DyeColor.GRAY), new Variant(Pattern.KOB, DyeColor.ORANGE, DyeColor.WHITE), new Variant(Pattern.SPOTTY, DyeColor.PINK, DyeColor.LIGHT_BLUE), new Variant(Pattern.BLOCKFISH, DyeColor.PURPLE, DyeColor.YELLOW), new Variant(Pattern.CLAYFISH, DyeColor.WHITE, DyeColor.RED), new Variant(Pattern.SPOTTY, DyeColor.WHITE, DyeColor.YELLOW), new Variant(Pattern.GLITTER, DyeColor.WHITE, DyeColor.GRAY), new Variant(Pattern.CLAYFISH, DyeColor.WHITE, DyeColor.ORANGE), new Variant(Pattern.DASHER, DyeColor.CYAN, DyeColor.PINK), new Variant(Pattern.BRINELY, DyeColor.LIME, DyeColor.LIGHT_BLUE), new Variant(Pattern.BETTY, DyeColor.RED, DyeColor.WHITE), new Variant(Pattern.SNOOPER, DyeColor.GRAY, DyeColor.RED), new Variant(Pattern.BLOCKFISH, DyeColor.RED, DyeColor.WHITE), new Variant(Pattern.FLOPPER, DyeColor.WHITE, DyeColor.YELLOW), new Variant(Pattern.KOB, DyeColor.RED, DyeColor.WHITE), new Variant(Pattern.SUNSTREAK, DyeColor.GRAY, DyeColor.WHITE), new Variant(Pattern.DASHER, DyeColor.CYAN, DyeColor.YELLOW), new Variant(Pattern.FLOPPER, DyeColor.YELLOW, DyeColor.YELLOW) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Pattern
/*     */     implements StringRepresentable, TooltipProvider
/*     */   {
/* 108 */     KOB("kob", TropicalFish.Base.SMALL, 0),
/* 109 */     SUNSTREAK("sunstreak", TropicalFish.Base.SMALL, 1),
/* 110 */     SNOOPER("snooper", TropicalFish.Base.SMALL, 2),
/* 111 */     DASHER("dasher", TropicalFish.Base.SMALL, 3),
/* 112 */     BRINELY("brinely", TropicalFish.Base.SMALL, 4),
/* 113 */     SPOTTY("spotty", TropicalFish.Base.SMALL, 5),
/* 114 */     FLOPPER("flopper", TropicalFish.Base.LARGE, 0),
/* 115 */     STRIPEY("stripey", TropicalFish.Base.LARGE, 1),
/* 116 */     GLITTER("glitter", TropicalFish.Base.LARGE, 2),
/* 117 */     BLOCKFISH("blockfish", TropicalFish.Base.LARGE, 3),
/* 118 */     BETTY("betty", TropicalFish.Base.LARGE, 4),
/* 119 */     CLAYFISH("clayfish", TropicalFish.Base.LARGE, 5); public static final Codec<Pattern> CODEC; private static final IntFunction<Pattern> BY_ID; public static final StreamCodec<ByteBuf, Pattern> STREAM_CODEC; private final String name; private final Component displayName; private final TropicalFish.Base base; private final int packedId;
/*     */     static  {
/* 121 */       CODEC = StringRepresentable.fromEnum(Pattern::values);
/*     */       
/* 123 */       BY_ID = ByIdMap.sparse(Pattern::getPackedId, values(), KOB);
/*     */       
/* 125 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Pattern::getPackedId);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Pattern(String name, TropicalFish.Base base, int index) {
/* 136 */       this.name = name;
/* 137 */       this.base = base;
/* 138 */       this.packedId = base.id | index << 8;
/* 139 */       this.displayName = Component.translatable("entity.minecraft.tropical_fish.type." + this.name);
/*     */     }
/*     */ 
/*     */     
/* 143 */     public static Pattern byId(int packedId) { return (Pattern)BY_ID.apply(packedId); }
/*     */ 
/*     */ 
/*     */     
/* 147 */     public TropicalFish.Base base() { return this.base; }
/*     */ 
/*     */ 
/*     */     
/* 151 */     public int getPackedId() { return this.packedId; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 160 */     public Component displayName() { return this.displayName; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 165 */       DyeColor baseColor = (DyeColor)components.getOrDefault(DataComponents.TROPICAL_FISH_BASE_COLOR, TropicalFish.DEFAULT_VARIANT.baseColor());
/* 166 */       DyeColor patternColor = (DyeColor)components.getOrDefault(DataComponents.TROPICAL_FISH_PATTERN_COLOR, TropicalFish.DEFAULT_VARIANT.patternColor());
/*     */       
/* 168 */       ChatFormatting[] styles = { ChatFormatting.ITALIC, ChatFormatting.GRAY };
/*     */       
/* 170 */       int commonIndex = TropicalFish.COMMON_VARIANTS.indexOf(new TropicalFish.Variant(this, baseColor, patternColor));
/* 171 */       if (commonIndex != -1) {
/* 172 */         consumer.accept(Component.translatable(TropicalFish.getPredefinedName(commonIndex)).withStyle(styles));
/*     */         
/*     */         return;
/*     */       } 
/* 176 */       consumer.accept(this.displayName.plainCopy().withStyle(styles));
/* 177 */       MutableComponent colorComponent = Component.translatable("color.minecraft." + baseColor.getName());
/* 178 */       if (baseColor != patternColor) {
/* 179 */         colorComponent.append(", ").append(Component.translatable("color.minecraft." + patternColor.getName()));
/*     */       }
/* 181 */       colorComponent.withStyle(styles);
/* 182 */       consumer.accept(colorComponent);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isSchool = true;
/*     */ 
/*     */   
/* 189 */   public TropicalFish(EntityType<? extends TropicalFish> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public static String getPredefinedName(int index) { return "entity.minecraft.tropical_fish.predefined." + index; }
/*     */ 
/*     */ 
/*     */   
/* 197 */   private static int packVariant(Pattern pattern, DyeColor baseColor, DyeColor patternColor) { return pattern.getPackedId() & 0xFFFF | (baseColor.getId() & 0xFF) << 16 | (patternColor.getId() & 0xFF) << 24; }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public static DyeColor getBaseColor(int packedVariant) { return DyeColor.byId(packedVariant >> 16 & 0xFF); }
/*     */ 
/*     */ 
/*     */   
/* 205 */   public static DyeColor getPatternColor(int packedVariant) { return DyeColor.byId(packedVariant >> 24 & 0xFF); }
/*     */ 
/*     */ 
/*     */   
/* 209 */   public static Pattern getPattern(int packedVariant) { return Pattern.byId(packedVariant & 0xFFFF); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 214 */     super.defineSynchedData(entityData);
/*     */     
/* 216 */     entityData.define(DATA_ID_TYPE_VARIANT, Integer.valueOf(DEFAULT_VARIANT.getPackedId()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 221 */     super.addAdditionalSaveData(output);
/*     */     
/* 223 */     output.store("Variant", Variant.CODEC, new Variant(getPackedVariant()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 228 */     super.readAdditionalSaveData(input);
/*     */     
/* 230 */     Variant variant = (Variant)input.read("Variant", Variant.CODEC).orElse(DEFAULT_VARIANT);
/* 231 */     setPackedVariant(variant.getPackedId());
/*     */   }
/*     */ 
/*     */   
/* 235 */   private void setPackedVariant(int i) { this.entityData.set(DATA_ID_TYPE_VARIANT, Integer.valueOf(i)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 240 */   public boolean isMaxGroupSizeReached(int groupSize) { return !this.isSchool; }
/*     */ 
/*     */ 
/*     */   
/* 244 */   private int getPackedVariant() { return ((Integer)this.entityData.get(DATA_ID_TYPE_VARIANT)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 248 */   public DyeColor getBaseColor() { return getBaseColor(getPackedVariant()); }
/*     */ 
/*     */ 
/*     */   
/* 252 */   public DyeColor getPatternColor() { return getPatternColor(getPackedVariant()); }
/*     */ 
/*     */ 
/*     */   
/* 256 */   public Pattern getPattern() { return getPattern(getPackedVariant()); }
/*     */ 
/*     */   
/*     */   private void setPattern(Pattern pattern) {
/* 260 */     int base = getPackedVariant();
/* 261 */     DyeColor baseColor = getBaseColor(base);
/* 262 */     DyeColor patternColor = getPatternColor(base);
/* 263 */     setPackedVariant(packVariant(pattern, baseColor, patternColor));
/*     */   }
/*     */   
/*     */   private void setBaseColor(DyeColor baseColor) {
/* 267 */     int base = getPackedVariant();
/* 268 */     Pattern pattern = getPattern(base);
/* 269 */     DyeColor patternColor = getPatternColor(base);
/* 270 */     setPackedVariant(packVariant(pattern, baseColor, patternColor));
/*     */   }
/*     */   
/*     */   private void setPatternColor(DyeColor patternColor) {
/* 274 */     int base = getPackedVariant();
/* 275 */     Pattern pattern = getPattern(base);
/* 276 */     DyeColor baseColor = getBaseColor(base);
/* 277 */     setPackedVariant(packVariant(pattern, baseColor, patternColor));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 282 */     if (type == DataComponents.TROPICAL_FISH_PATTERN) {
/* 283 */       return (T)castComponentValue(type, getPattern());
/*     */     }
/* 285 */     if (type == DataComponents.TROPICAL_FISH_BASE_COLOR) {
/* 286 */       return (T)castComponentValue(type, getBaseColor());
/*     */     }
/* 288 */     if (type == DataComponents.TROPICAL_FISH_PATTERN_COLOR) {
/* 289 */       return (T)castComponentValue(type, getPatternColor());
/*     */     }
/*     */     
/* 292 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 297 */     applyImplicitComponentIfPresent(components, DataComponents.TROPICAL_FISH_PATTERN);
/* 298 */     applyImplicitComponentIfPresent(components, DataComponents.TROPICAL_FISH_BASE_COLOR);
/* 299 */     applyImplicitComponentIfPresent(components, DataComponents.TROPICAL_FISH_PATTERN_COLOR);
/* 300 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 305 */     if (type == DataComponents.TROPICAL_FISH_PATTERN) {
/* 306 */       setPattern((Pattern)castComponentValue(DataComponents.TROPICAL_FISH_PATTERN, value));
/* 307 */       return true;
/*     */     } 
/* 309 */     if (type == DataComponents.TROPICAL_FISH_BASE_COLOR) {
/* 310 */       setBaseColor((DyeColor)castComponentValue(DataComponents.TROPICAL_FISH_BASE_COLOR, value));
/* 311 */       return true;
/*     */     } 
/* 313 */     if (type == DataComponents.TROPICAL_FISH_PATTERN_COLOR) {
/* 314 */       setPatternColor((DyeColor)castComponentValue(DataComponents.TROPICAL_FISH_PATTERN_COLOR, value));
/* 315 */       return true;
/*     */     } 
/*     */     
/* 318 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 323 */     super.saveToBucketTag(bucket);
/* 324 */     bucket.copyFrom(DataComponents.TROPICAL_FISH_PATTERN, this);
/* 325 */     bucket.copyFrom(DataComponents.TROPICAL_FISH_BASE_COLOR, this);
/* 326 */     bucket.copyFrom(DataComponents.TROPICAL_FISH_PATTERN_COLOR, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 331 */   public ItemStack getBucketItemStack() { return new ItemStack(Items.TROPICAL_FISH_BUCKET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   protected SoundEvent getAmbientSound() { return SoundEvents.TROPICAL_FISH_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 341 */   protected SoundEvent getDeathSound() { return SoundEvents.TROPICAL_FISH_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 346 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.TROPICAL_FISH_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 351 */   protected SoundEvent getFlopSound() { return SoundEvents.TROPICAL_FISH_FLOP; }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     Variant variant;
/* 356 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     RandomSource random = level.getRandom();
/* 362 */     if (groupData instanceof TropicalFishGroupData) { TropicalFishGroupData tropicalFishGroupData = (TropicalFishGroupData)groupData;
/* 363 */       variant = tropicalFishGroupData.variant; }
/* 364 */     else if (random.nextFloat() < 0.9D)
/*     */     
/* 366 */     { variant = (Variant)Util.getRandom(COMMON_VARIANTS, random);
/* 367 */       groupData = new TropicalFishGroupData(this, variant); }
/*     */     else
/* 369 */     { this.isSchool = false;
/* 370 */       Pattern[] arrayOfPattern = Pattern.values();
/* 371 */       DyeColor[] colors = DyeColor.values();
/*     */       
/* 373 */       Pattern pattern = (Pattern)Util.getRandom(arrayOfPattern, random);
/* 374 */       DyeColor baseColor = (DyeColor)Util.getRandom(colors, random);
/* 375 */       DyeColor patternColor = (DyeColor)Util.getRandom(colors, random);
/* 376 */       variant = new Variant(pattern, baseColor, patternColor); }
/*     */ 
/*     */     
/* 379 */     setPackedVariant(variant.getPackedId());
/*     */     
/* 381 */     return groupData;
/*     */   }
/*     */   
/*     */   public static boolean checkTropicalFishSpawnRules(EntityType<TropicalFish> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 385 */     return (level.getFluidState(pos.below()).is(FluidTags.WATER) && level
/* 386 */       .getBlockState(pos.above()).is(Blocks.WATER) && (level
/*     */       
/* 388 */       .getBiome(pos).is(BiomeTags.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT) || WaterAnimal.checkSurfaceWaterAnimalSpawnRules(type, level, spawnReason, pos, random)));
/*     */   }
/*     */   
/*     */   private static class TropicalFishGroupData extends AbstractSchoolingFish.SchoolSpawnGroupData {
/*     */     private final TropicalFish.Variant variant;
/*     */     
/*     */     private TropicalFishGroupData(TropicalFish leader, TropicalFish.Variant variant) {
/* 395 */       super(leader);
/* 396 */       this.variant = variant;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\TropicalFish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */