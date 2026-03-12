/*    */ package net.minecraft.world.flag;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface FeatureElement
/*    */ {
/* 18 */   public static final Set<ResourceKey<? extends Registry<? extends FeatureElement>>> FILTERED_REGISTRIES = Set.of(Registries.ITEM, Registries.BLOCK, Registries.ENTITY_TYPE, Registries.GAME_RULE, Registries.MENU, Registries.POTION, Registries.MOB_EFFECT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   FeatureFlagSet requiredFeatures();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   default boolean isEnabled(FeatureFlagSet enabledFeatures) { return requiredFeatures().isSubsetOf(enabledFeatures); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\flag\FeatureElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */