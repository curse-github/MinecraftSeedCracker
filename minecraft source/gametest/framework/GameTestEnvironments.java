/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GameTestEnvironments
/*    */ {
/*    */   public static final String DEFAULT = "default";
/* 14 */   public static final ResourceKey<TestEnvironmentDefinition> DEFAULT_KEY = create("default");
/*    */ 
/*    */   
/* 17 */   private static ResourceKey<TestEnvironmentDefinition> create(String name) { return ResourceKey.create(Registries.TEST_ENVIRONMENT, Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   static void bootstrap(BootstrapContext<TestEnvironmentDefinition> context) { context.register(DEFAULT_KEY, new TestEnvironmentDefinition.AllOf(List.of())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestEnvironments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */