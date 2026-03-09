/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface GameTestInstances
/*    */ {
/* 12 */   public static final ResourceKey<GameTestInstance> ALWAYS_PASS = create("always_pass");
/*    */   
/*    */   static void bootstrap(BootstrapContext<GameTestInstance> context) {
/* 15 */     HolderGetter<Consumer<GameTestHelper>> functions = context.lookup(Registries.TEST_FUNCTION);
/* 16 */     HolderGetter<TestEnvironmentDefinition> batches = context.lookup(Registries.TEST_ENVIRONMENT);
/* 17 */     context.register(ALWAYS_PASS, new FunctionGameTestInstance(BuiltinTestFunctions.ALWAYS_PASS, new TestData(batches.getOrThrow(GameTestEnvironments.DEFAULT_KEY), Identifier.withDefaultNamespace("empty"), 1, 1, false)));
/*    */   }
/*    */ 
/*    */   
/* 21 */   private static ResourceKey<GameTestInstance> create(String id) { return ResourceKey.create(Registries.TEST_INSTANCE, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestInstances.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */