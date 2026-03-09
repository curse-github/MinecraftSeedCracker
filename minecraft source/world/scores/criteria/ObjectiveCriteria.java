/*     */ package net.minecraft.world.scores.criteria;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public class ObjectiveCriteria {
/*  18 */   private static final Map<String, ObjectiveCriteria> CUSTOM_CRITERIA = Maps.newHashMap();
/*  19 */   private static final Map<String, ObjectiveCriteria> CRITERIA_CACHE = Maps.newHashMap();
/*     */   
/*  21 */   public static final Codec<ObjectiveCriteria> CODEC = Codec.STRING.comapFlatMap(name -> 
/*  22 */       (DataResult)byName(
/*     */         
/*  24 */         name).map(DataResult::success).orElse(DataResult.error(())), ObjectiveCriteria::getName);
/*     */ 
/*     */ 
/*     */   
/*  28 */   public static final ObjectiveCriteria DUMMY = registerCustom("dummy");
/*  29 */   public static final ObjectiveCriteria TRIGGER = registerCustom("trigger");
/*  30 */   public static final ObjectiveCriteria DEATH_COUNT = registerCustom("deathCount");
/*  31 */   public static final ObjectiveCriteria KILL_COUNT_PLAYERS = registerCustom("playerKillCount");
/*  32 */   public static final ObjectiveCriteria KILL_COUNT_ALL = registerCustom("totalKillCount");
/*  33 */   public static final ObjectiveCriteria HEALTH = registerCustom("health", true, RenderType.HEARTS);
/*  34 */   public static final ObjectiveCriteria FOOD = registerCustom("food", true, RenderType.INTEGER);
/*  35 */   public static final ObjectiveCriteria AIR = registerCustom("air", true, RenderType.INTEGER);
/*  36 */   public static final ObjectiveCriteria ARMOR = registerCustom("armor", true, RenderType.INTEGER);
/*  37 */   public static final ObjectiveCriteria EXPERIENCE = registerCustom("xp", true, RenderType.INTEGER);
/*  38 */   public static final ObjectiveCriteria LEVEL = registerCustom("level", true, RenderType.INTEGER);
/*  39 */   public static final ObjectiveCriteria[] TEAM_KILL = { null, (new ObjectiveCriteria[16][0] = 
/*  40 */       registerCustom("teamkill." + ChatFormatting.BLACK.getName())).registerCustom("teamkill." + ChatFormatting.DARK_BLUE.getName()), (new ObjectiveCriteria[16][2] = 
/*  41 */       registerCustom("teamkill." + ChatFormatting.DARK_GREEN.getName())).registerCustom("teamkill." + ChatFormatting.DARK_AQUA.getName()), (new ObjectiveCriteria[16][4] = 
/*  42 */       registerCustom("teamkill." + ChatFormatting.DARK_RED.getName())).registerCustom("teamkill." + ChatFormatting.DARK_PURPLE.getName()), (new ObjectiveCriteria[16][6] = 
/*  43 */       registerCustom("teamkill." + ChatFormatting.GOLD.getName())).registerCustom("teamkill." + ChatFormatting.GRAY.getName()), (new ObjectiveCriteria[16][8] = 
/*  44 */       registerCustom("teamkill." + ChatFormatting.DARK_GRAY.getName())).registerCustom("teamkill." + ChatFormatting.BLUE.getName()), (new ObjectiveCriteria[16][10] = 
/*  45 */       registerCustom("teamkill." + ChatFormatting.GREEN.getName())).registerCustom("teamkill." + ChatFormatting.AQUA.getName()), (new ObjectiveCriteria[16][12] = 
/*  46 */       registerCustom("teamkill." + ChatFormatting.RED.getName())).registerCustom("teamkill." + ChatFormatting.LIGHT_PURPLE.getName()), (new ObjectiveCriteria[16][14] = 
/*  47 */       registerCustom("teamkill." + ChatFormatting.YELLOW.getName())).registerCustom("teamkill." + ChatFormatting.WHITE.getName()) };
/*     */   
/*  49 */   public static final ObjectiveCriteria[] KILLED_BY_TEAM = { null, (new ObjectiveCriteria[16][0] = 
/*  50 */       registerCustom("killedByTeam." + ChatFormatting.BLACK.getName())).registerCustom("killedByTeam." + ChatFormatting.DARK_BLUE.getName()), (new ObjectiveCriteria[16][2] = 
/*  51 */       registerCustom("killedByTeam." + ChatFormatting.DARK_GREEN.getName())).registerCustom("killedByTeam." + ChatFormatting.DARK_AQUA.getName()), (new ObjectiveCriteria[16][4] = 
/*  52 */       registerCustom("killedByTeam." + ChatFormatting.DARK_RED.getName())).registerCustom("killedByTeam." + ChatFormatting.DARK_PURPLE.getName()), (new ObjectiveCriteria[16][6] = 
/*  53 */       registerCustom("killedByTeam." + ChatFormatting.GOLD.getName())).registerCustom("killedByTeam." + ChatFormatting.GRAY.getName()), (new ObjectiveCriteria[16][8] = 
/*  54 */       registerCustom("killedByTeam." + ChatFormatting.DARK_GRAY.getName())).registerCustom("killedByTeam." + ChatFormatting.BLUE.getName()), (new ObjectiveCriteria[16][10] = 
/*  55 */       registerCustom("killedByTeam." + ChatFormatting.GREEN.getName())).registerCustom("killedByTeam." + ChatFormatting.AQUA.getName()), (new ObjectiveCriteria[16][12] = 
/*  56 */       registerCustom("killedByTeam." + ChatFormatting.RED.getName())).registerCustom("killedByTeam." + ChatFormatting.LIGHT_PURPLE.getName()), (new ObjectiveCriteria[16][14] = 
/*  57 */       registerCustom("killedByTeam." + ChatFormatting.YELLOW.getName())).registerCustom("killedByTeam." + ChatFormatting.WHITE.getName()) };
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean readOnly;
/*     */   private final RenderType renderType;
/*     */   
/*     */   private static ObjectiveCriteria registerCustom(String name, boolean readOnly, RenderType renderType) {
/*  65 */     ObjectiveCriteria result = new ObjectiveCriteria(name, readOnly, renderType);
/*  66 */     CUSTOM_CRITERIA.put(name, result);
/*  67 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  71 */   private static ObjectiveCriteria registerCustom(String name) { return registerCustom(name, false, RenderType.INTEGER); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   protected ObjectiveCriteria(String name) { this(name, false, RenderType.INTEGER); }
/*     */ 
/*     */   
/*     */   protected ObjectiveCriteria(String name, boolean readOnly, RenderType renderType) {
/*  79 */     this.name = name;
/*  80 */     this.readOnly = readOnly;
/*  81 */     this.renderType = renderType;
/*  82 */     CRITERIA_CACHE.put(name, this);
/*     */   }
/*     */ 
/*     */   
/*  86 */   public static Set<String> getCustomCriteriaNames() { return ImmutableSet.copyOf(CUSTOM_CRITERIA.keySet()); }
/*     */ 
/*     */   
/*     */   public static Optional<ObjectiveCriteria> byName(String name) {
/*  90 */     ObjectiveCriteria value = (ObjectiveCriteria)CRITERIA_CACHE.get(name);
/*  91 */     if (value != null) {
/*  92 */       return Optional.of(value);
/*     */     }
/*  94 */     int colonPos = name.indexOf(':');
/*  95 */     if (colonPos < 0) {
/*  96 */       return Optional.empty();
/*     */     }
/*  98 */     return BuiltInRegistries.STAT_TYPE.getOptional(Identifier.bySeparator(name.substring(0, colonPos), '.'))
/*  99 */       .flatMap(statType -> getStat(statType, Identifier.bySeparator(name.substring(colonPos + 1), '.')));
/*     */   }
/*     */ 
/*     */   
/* 103 */   private static <T> Optional<ObjectiveCriteria> getStat(StatType<T> statType, Identifier key) { Objects.requireNonNull(statType); return statType.getRegistry().getOptional(key).map(statType::get); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean isReadOnly() { return this.readOnly; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public RenderType getDefaultRenderType() { return this.renderType; }
/*     */   
/*     */   public enum RenderType
/*     */     implements StringRepresentable {
/* 119 */     INTEGER("integer"),
/* 120 */     HEARTS("hearts");
/*     */     
/*     */     private final String id;
/*     */     
/*     */     public static final StringRepresentable.EnumCodec<RenderType> CODEC;
/*     */     
/* 126 */     RenderType(String id) { this.id = id; }
/*     */ 
/*     */ 
/*     */     
/* 130 */     public String getId() { return this.id; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     public String getSerializedName() { return this.id; }
/*     */     
/*     */     static  {
/* 138 */       CODEC = StringRepresentable.fromEnum(RenderType::values);
/*     */     }
/*     */     
/* 141 */     public static RenderType byId(String key) { return (RenderType)CODEC.byName(key, INTEGER); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\criteria\ObjectiveCriteria.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */