/*     */ package net.minecraft.gametest.framework;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface TestEnvironmentDefinition {
/*     */   static MapCodec<? extends TestEnvironmentDefinition> bootstrap(Registry<MapCodec<? extends TestEnvironmentDefinition>> registry) {
/*  33 */     Registry.register(registry, "all_of", AllOf.CODEC);
/*  34 */     Registry.register(registry, "game_rules", SetGameRules.CODEC);
/*  35 */     Registry.register(registry, "time_of_day", TimeOfDay.CODEC);
/*  36 */     Registry.register(registry, "weather", Weather.CODEC);
/*  37 */     return (MapCodec)Registry.register(registry, "function", Functions.CODEC);
/*     */   }
/*     */   
/*  40 */   public static final Codec<TestEnvironmentDefinition> DIRECT_CODEC = BuiltInRegistries.TEST_ENVIRONMENT_DEFINITION_TYPE.byNameCodec().dispatch(TestEnvironmentDefinition::codec, c -> c);
/*     */   
/*  42 */   public static final Codec<Holder<TestEnvironmentDefinition>> CODEC = RegistryFileCodec.create(Registries.TEST_ENVIRONMENT, DIRECT_CODEC);
/*     */   
/*     */   default void teardown(ServerLevel level) {}
/*     */   
/*     */   void setup(ServerLevel paramServerLevel);
/*     */   
/*     */   MapCodec<? extends TestEnvironmentDefinition> codec();
/*     */   
/*     */   public static final class Weather extends Record implements TestEnvironmentDefinition { private final Type weather;
/*     */     
/*  52 */     public Weather(Type weather) { this.weather = weather; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*  52 */       //   0	8	1	o	Ljava/lang/Object; } public Type weather() { return this.weather; }
/*     */     
/*  54 */     public enum Type implements StringRepresentable { CLEAR("clear", 100000, 0, false, false),
/*  55 */       RAIN("rain", 0, 100000, true, false),
/*  56 */       THUNDER("thunder", 0, 100000, true, true); public static final Codec<Type> CODEC; private final String id; private final int clearTime; private final int rainTime; private final boolean raining; private final boolean thundering;
/*     */       
/*     */       static  {
/*  59 */         CODEC = StringRepresentable.fromEnum(Type::values);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  68 */         this.id = id;
/*  69 */         this.clearTime = clearTime;
/*  70 */         this.rainTime = rainTime;
/*  71 */         this.raining = raining;
/*  72 */         this.thundering = thundering;
/*     */       }
/*     */ 
/*     */       
/*  76 */       void apply(ServerLevel level) { level.setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       public String getSerializedName() { return this.id; } }
/*     */ 
/*     */ 
/*     */     
/*  85 */     public static final MapCodec<Weather> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Type.CODEC
/*  86 */           .fieldOf("weather").forGetter(Weather::weather))
/*  87 */         .apply(i, Weather::new));
/*     */ 
/*     */ 
/*     */     
/*  91 */     public void setup(ServerLevel level) { this.weather.apply(level); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     public void teardown(ServerLevel level) { level.resetWeatherCycle(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     public MapCodec<Weather> codec() { return CODEC; } }
/*     */    public enum Type implements StringRepresentable {
/*     */     CLEAR("clear", 100000, 0, false, false), RAIN("rain", 0, 100000, true, false), THUNDER("thunder", 0, 100000, true, true); public static final Codec<Type> CODEC; private final String id; private final int clearTime; private final int rainTime; private final boolean raining; private final boolean thundering; static  { CODEC = StringRepresentable.fromEnum(Type::values); } Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) { this.id = id; this.clearTime = clearTime; this.rainTime = rainTime; this.raining = raining;
/*     */       this.thundering = thundering; } void apply(ServerLevel level) { level.setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering); } public String getSerializedName() { return this.id; }
/* 105 */   } public static final class TimeOfDay extends Record implements TestEnvironmentDefinition { public TimeOfDay(int time) { this.time = time; } private final int time; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$TimeOfDay;
/* 105 */       //   0	8	1	o	Ljava/lang/Object; } public int time() { return this.time; }
/* 106 */     public static final MapCodec<TimeOfDay> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 107 */           .fieldOf("time").forGetter(TimeOfDay::time))
/* 108 */         .apply(i, TimeOfDay::new));
/*     */ 
/*     */ 
/*     */     
/* 112 */     public void setup(ServerLevel level) { level.setDayTime(this.time); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     public MapCodec<TimeOfDay> codec() { return CODEC; } }
/*     */   
/*     */   public static final class SetGameRules extends Record implements TestEnvironmentDefinition { private final GameRuleMap gameRulesMap;
/*     */     
/* 121 */     public SetGameRules(GameRuleMap gameRulesMap) { this.gameRulesMap = gameRulesMap; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/* 121 */       //   0	8	1	o	Ljava/lang/Object; } public GameRuleMap gameRulesMap() { return this.gameRulesMap; }
/* 122 */     public static final MapCodec<SetGameRules> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(GameRuleMap.CODEC
/* 123 */           .fieldOf("rules").forGetter(SetGameRules::gameRulesMap))
/* 124 */         .apply(i, SetGameRules::new));
/*     */ 
/*     */     
/*     */     public void setup(ServerLevel level) {
/* 128 */       GameRules gameRules = level.getGameRules();
/* 129 */       MinecraftServer server = level.getServer();
/* 130 */       gameRules.setAll(this.gameRulesMap, server);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 135 */     public void teardown(ServerLevel level) { this.gameRulesMap.keySet().forEach(gameRule -> resetRule(level, gameRule)); }
/*     */ 
/*     */ 
/*     */     
/* 139 */     private <T> void resetRule(ServerLevel level, GameRule<T> gameRule) { level.getGameRules().set(gameRule, gameRule.defaultValue(), level.getServer()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     public MapCodec<SetGameRules> codec() { return CODEC; } }
/*     */   public static final class Functions extends Record implements TestEnvironmentDefinition { private final Optional<Identifier> setupFunction;
/*     */     private final Optional<Identifier> teardownFunction;
/*     */     
/* 148 */     public Functions(Optional<Identifier> setupFunction, Optional<Identifier> teardownFunction) { this.setupFunction = setupFunction; this.teardownFunction = teardownFunction; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/* 148 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> setupFunction() { return this.setupFunction; } public Optional<Identifier> teardownFunction() { return this.teardownFunction; }
/* 149 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/* 151 */     public static final MapCodec<Functions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 152 */           .optionalFieldOf("setup").forGetter(Functions::setupFunction), Identifier.CODEC
/* 153 */           .optionalFieldOf("teardown").forGetter(Functions::teardownFunction))
/* 154 */         .apply(i, Functions::new));
/*     */ 
/*     */ 
/*     */     
/* 158 */     public void setup(ServerLevel level) { this.setupFunction.ifPresent(p -> run(level, p)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     public void teardown(ServerLevel level) { this.teardownFunction.ifPresent(p -> run(level, p)); }
/*     */ 
/*     */     
/*     */     private static void run(ServerLevel level, Identifier functionId) {
/* 167 */       MinecraftServer server = level.getServer();
/* 168 */       ServerFunctionManager functions = server.getFunctions();
/* 169 */       Optional<CommandFunction<CommandSourceStack>> function = functions.get(functionId);
/* 170 */       if (function.isPresent()) {
/*     */ 
/*     */ 
/*     */         
/* 174 */         CommandSourceStack source = server.createCommandSourceStack().withPermission(LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput().withLevel(level);
/* 175 */         functions.execute((CommandFunction)function.get(), source);
/*     */       } else {
/* 177 */         LOGGER.error("Test Batch failed for non-existent function {}", functionId);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 183 */     public MapCodec<Functions> codec() { return CODEC; } }
/*     */   
/*     */   public static final class AllOf extends Record implements TestEnvironmentDefinition { private final List<Holder<TestEnvironmentDefinition>> definitions;
/*     */     
/* 187 */     public AllOf(List<Holder<TestEnvironmentDefinition>> definitions) { this.definitions = definitions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/* 187 */       //   0	8	1	o	Ljava/lang/Object; } public List<Holder<TestEnvironmentDefinition>> definitions() { return this.definitions; }
/* 188 */     public static final MapCodec<AllOf> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TestEnvironmentDefinition.CODEC
/* 189 */           .listOf().fieldOf("definitions").forGetter(AllOf::definitions))
/* 190 */         .apply(i, AllOf::new));
/*     */ 
/*     */     
/* 193 */     public AllOf(TestEnvironmentDefinition... defs) { this(Arrays.stream(defs).map(Holder::direct).toList()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     public void setup(ServerLevel level) { this.definitions.forEach(b -> ((TestEnvironmentDefinition)b.value()).setup(level)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     public void teardown(ServerLevel level) { this.definitions.forEach(b -> ((TestEnvironmentDefinition)b.value()).teardown(level)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     public MapCodec<AllOf> codec() { return CODEC; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */