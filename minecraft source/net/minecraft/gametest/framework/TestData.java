/*    */ package net.minecraft.gametest.framework;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public final class TestData<EnvironmentType> extends Record {
/*    */   private final EnvironmentType environment;
/*    */   private final Identifier structure;
/*    */   private final int maxTicks;
/*    */   private final int setupTicks;
/*    */   private final boolean required;
/*    */   
/* 13 */   public TestData(EnvironmentType environment, Identifier structure, int maxTicks, int setupTicks, boolean required, Rotation rotation, boolean manualOnly, int maxAttempts, int requiredSuccesses, boolean skyAccess) { this.environment = environment; this.structure = structure; this.maxTicks = maxTicks; this.setupTicks = setupTicks; this.required = required; this.rotation = rotation; this.manualOnly = manualOnly; this.maxAttempts = maxAttempts; this.requiredSuccesses = requiredSuccesses; this.skyAccess = skyAccess; } private final Rotation rotation; private final boolean manualOnly; private final int maxAttempts; private final int requiredSuccesses; private final boolean skyAccess; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 13 */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>; } public EnvironmentType environment() { return (EnvironmentType)this.environment; } public Identifier structure() { return this.structure; } public int maxTicks() { return this.maxTicks; } public int setupTicks() { return this.setupTicks; } public boolean required() { return this.required; } public Rotation rotation() { return this.rotation; } public boolean manualOnly() { return this.manualOnly; } public int maxAttempts() { return this.maxAttempts; } public int requiredSuccesses() { return this.requiredSuccesses; } public boolean skyAccess() { return this.skyAccess; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final MapCodec<TestData<Holder<TestEnvironmentDefinition>>> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TestEnvironmentDefinition.CODEC
/* 26 */         .fieldOf("environment").forGetter(TestData::environment), Identifier.CODEC
/* 27 */         .fieldOf("structure").forGetter(TestData::structure), ExtraCodecs.POSITIVE_INT
/* 28 */         .fieldOf("max_ticks").forGetter(TestData::maxTicks), ExtraCodecs.NON_NEGATIVE_INT
/* 29 */         .optionalFieldOf("setup_ticks", Integer.valueOf(0)).forGetter(TestData::setupTicks), Codec.BOOL
/* 30 */         .optionalFieldOf("required", Boolean.valueOf(true)).forGetter(TestData::required), Rotation.CODEC
/* 31 */         .optionalFieldOf("rotation", Rotation.NONE).forGetter(TestData::rotation), Codec.BOOL
/* 32 */         .optionalFieldOf("manual_only", Boolean.valueOf(false)).forGetter(TestData::manualOnly), ExtraCodecs.POSITIVE_INT
/* 33 */         .optionalFieldOf("max_attempts", Integer.valueOf(1)).forGetter(TestData::maxAttempts), ExtraCodecs.POSITIVE_INT
/* 34 */         .optionalFieldOf("required_successes", Integer.valueOf(1)).forGetter(TestData::requiredSuccesses), Codec.BOOL
/* 35 */         .optionalFieldOf("sky_access", Boolean.valueOf(false)).forGetter(TestData::skyAccess))
/* 36 */       .apply(i, TestData::new));
/*    */ 
/*    */   
/* 39 */   public TestData(EnvironmentType environment, Identifier structure, int maxTicks, int setupTicks, boolean required, Rotation rotation) { this(environment, structure, maxTicks, setupTicks, required, rotation, false, 1, 1, false); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public TestData(EnvironmentType environment, Identifier structure, int maxTicks, int setupTicks, boolean required) { this(environment, structure, maxTicks, setupTicks, required, Rotation.NONE); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public <T> TestData<T> map(Function<EnvironmentType, T> mapper) { return new TestData(mapper.apply(this.environment), this.structure, this.maxTicks, this.setupTicks, this.required, this.rotation, this.manualOnly, this.maxAttempts, this.requiredSuccesses, this.skyAccess); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */