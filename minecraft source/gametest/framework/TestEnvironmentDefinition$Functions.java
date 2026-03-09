/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Functions
/*     */   extends Record
/*     */   implements TestEnvironmentDefinition
/*     */ {
/*     */   private final Optional<Identifier> setupFunction;
/*     */   private final Optional<Identifier> teardownFunction;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #148	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #148	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #148	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Functions;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 148 */   public Functions(Optional<Identifier> setupFunction, Optional<Identifier> teardownFunction) { this.setupFunction = setupFunction; this.teardownFunction = teardownFunction; } public Optional<Identifier> setupFunction() { return this.setupFunction; } public Optional<Identifier> teardownFunction() { return this.teardownFunction; }
/* 149 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 151 */   public static final MapCodec<Functions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 152 */         .optionalFieldOf("setup").forGetter(Functions::setupFunction), Identifier.CODEC
/* 153 */         .optionalFieldOf("teardown").forGetter(Functions::teardownFunction))
/* 154 */       .apply(i, Functions::new));
/*     */ 
/*     */ 
/*     */   
/* 158 */   public void setup(ServerLevel level) { this.setupFunction.ifPresent(p -> run(level, p)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public void teardown(ServerLevel level) { this.teardownFunction.ifPresent(p -> run(level, p)); }
/*     */ 
/*     */   
/*     */   private static void run(ServerLevel level, Identifier functionId) {
/* 167 */     MinecraftServer server = level.getServer();
/* 168 */     ServerFunctionManager functions = server.getFunctions();
/* 169 */     Optional<CommandFunction<CommandSourceStack>> function = functions.get(functionId);
/* 170 */     if (function.isPresent()) {
/*     */ 
/*     */ 
/*     */       
/* 174 */       CommandSourceStack source = server.createCommandSourceStack().withPermission(LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput().withLevel(level);
/* 175 */       functions.execute((CommandFunction)function.get(), source);
/*     */     } else {
/* 177 */       LOGGER.error("Test Batch failed for non-existent function {}", functionId);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public MapCodec<Functions> codec() { return CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Functions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */