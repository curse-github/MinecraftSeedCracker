/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GameRule<T>
/*     */   extends Object
/*     */   implements FeatureElement
/*     */ {
/*     */   private final GameRuleCategory category;
/*     */   private final GameRuleType gameRuleType;
/*     */   private final ArgumentType<T> argument;
/*     */   private final GameRules.VisitorCaller<T> visitorCaller;
/*     */   private final Codec<T> valueCodec;
/*     */   private final ToIntFunction<T> commandResultFunction;
/*     */   private final T defaultValue;
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   public GameRule(GameRuleCategory category, GameRuleType gameRuleType, ArgumentType<T> argument, GameRules.VisitorCaller<T> visitorCaller, Codec<T> valueCodec, ToIntFunction<T> commandResultFunction, T defaultValue, FeatureFlagSet requiredFeatures) {
/*  37 */     this.category = category;
/*  38 */     this.gameRuleType = gameRuleType;
/*  39 */     this.argument = argument;
/*  40 */     this.visitorCaller = visitorCaller;
/*  41 */     this.valueCodec = valueCodec;
/*  42 */     this.commandResultFunction = commandResultFunction;
/*  43 */     this.defaultValue = defaultValue;
/*  44 */     this.requiredFeatures = requiredFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public String toString() { return id(); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public String id() { return getIdentifier().toShortString(); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public Identifier getIdentifier() { return (Identifier)Objects.requireNonNull(BuiltInRegistries.GAME_RULE.getKey(this)); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public String getDescriptionId() { return Util.makeDescriptionId("gamerule", getIdentifier()); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public String serialize(T value) { return value.toString(); }
/*     */ 
/*     */   
/*     */   public DataResult<T> deserialize(String value) {
/*     */     try {
/*  70 */       StringReader reader = new StringReader(value);
/*  71 */       T result = (T)this.argument.parse(reader);
/*  72 */       if (reader.canRead()) {
/*  73 */         return DataResult.error(() -> "Failed to deserialize; trailing characters", result);
/*     */       }
/*  75 */       return DataResult.success(result);
/*  76 */     } catch (CommandSyntaxException ignored) {
/*  77 */       return DataResult.error(() -> "Failed to deserialize");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public Class<T> valueClass() { return this.defaultValue.getClass(); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public void callVisitor(GameRuleTypeVisitor visitor) { this.visitorCaller.call(visitor, this); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public int getCommandResult(T value) { return this.commandResultFunction.applyAsInt(value); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public GameRuleCategory category() { return this.category; }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public GameRuleType gameRuleType() { return this.gameRuleType; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public ArgumentType<T> argument() { return this.argument; }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public Codec<T> valueCodec() { return this.valueCodec; }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public T defaultValue() { return (T)this.defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public FeatureFlagSet requiredFeatures() { return this.requiredFeatures; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gamerules\GameRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */