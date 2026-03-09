/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ 
/*     */ 
/*     */ public abstract class GameTestInstance
/*     */ {
/*     */   public static MapCodec<? extends GameTestInstance> bootstrap(Registry<MapCodec<? extends GameTestInstance>> registry) {
/*  20 */     register(registry, "block_based", BlockBasedTestInstance.CODEC);
/*  21 */     return register(registry, "function", FunctionGameTestInstance.CODEC);
/*     */   }
/*     */ 
/*     */   
/*  25 */   private static MapCodec<? extends GameTestInstance> register(Registry<MapCodec<? extends GameTestInstance>> registry, String name, MapCodec<? extends GameTestInstance> codec) { return (MapCodec)Registry.register(registry, ResourceKey.create(Registries.TEST_INSTANCE_TYPE, Identifier.withDefaultNamespace(name)), codec); }
/*     */ 
/*     */   
/*  28 */   public static final Codec<GameTestInstance> DIRECT_CODEC = BuiltInRegistries.TEST_INSTANCE_TYPE.byNameCodec().dispatch(GameTestInstance::codec, i -> i);
/*     */   
/*     */   private final TestData<Holder<TestEnvironmentDefinition>> info;
/*     */ 
/*     */   
/*  33 */   protected GameTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) { this.info = info; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public Holder<TestEnvironmentDefinition> batch() { return (Holder)this.info.environment(); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public Identifier structure() { return this.info.structure(); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public int maxTicks() { return this.info.maxTicks(); }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public int setupTicks() { return this.info.setupTicks(); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public boolean required() { return this.info.required(); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public boolean manualOnly() { return this.info.manualOnly(); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public int maxAttempts() { return this.info.maxAttempts(); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public int requiredSuccesses() { return this.info.requiredSuccesses(); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public boolean skyAccess() { return this.info.skyAccess(); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public Rotation rotation() { return this.info.rotation(); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected TestData<Holder<TestEnvironmentDefinition>> info() { return this.info; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public Component describe() { return describeType().append(describeInfo()); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected MutableComponent describeType() { return descriptionRow("test_instance.description.type", typeDescription()); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   protected Component describeInfo() { return descriptionRow("test_instance.description.structure", this.info.structure().toString())
/*  97 */       .append(descriptionRow("test_instance.description.batch", ((Holder)this.info.environment()).getRegisteredName())); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected MutableComponent descriptionRow(String translationKey, String value) { return descriptionRow(translationKey, Component.literal(value)); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected MutableComponent descriptionRow(String translationKey, MutableComponent value) { return Component.translatable(translationKey, new Object[] { value.withStyle(ChatFormatting.BLUE) }).append(Component.literal("\n")); }
/*     */   
/*     */   public abstract void run(GameTestHelper paramGameTestHelper);
/*     */   
/*     */   public abstract MapCodec<? extends GameTestInstance> codec();
/*     */   
/*     */   protected abstract MutableComponent typeDescription();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */