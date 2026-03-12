/*    */ package net.minecraft.world.entity.animal.feline;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.StructureTags;
/*    */ import net.minecraft.world.entity.variant.MoonBrightnessCheck;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.entity.variant.StructureCheck;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ public interface CatVariants
/*    */ {
/* 20 */   public static final ResourceKey<CatVariant> TABBY = createKey("tabby");
/* 21 */   public static final ResourceKey<CatVariant> BLACK = createKey("black");
/* 22 */   public static final ResourceKey<CatVariant> RED = createKey("red");
/* 23 */   public static final ResourceKey<CatVariant> SIAMESE = createKey("siamese");
/* 24 */   public static final ResourceKey<CatVariant> BRITISH_SHORTHAIR = createKey("british_shorthair");
/* 25 */   public static final ResourceKey<CatVariant> CALICO = createKey("calico");
/* 26 */   public static final ResourceKey<CatVariant> PERSIAN = createKey("persian");
/* 27 */   public static final ResourceKey<CatVariant> RAGDOLL = createKey("ragdoll");
/* 28 */   public static final ResourceKey<CatVariant> WHITE = createKey("white");
/* 29 */   public static final ResourceKey<CatVariant> JELLIE = createKey("jellie");
/* 30 */   public static final ResourceKey<CatVariant> ALL_BLACK = createKey("all_black");
/*    */ 
/*    */   
/* 33 */   private static ResourceKey<CatVariant> createKey(String name) { return ResourceKey.create(Registries.CAT_VARIANT, Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */   
/*    */   static void bootstrap(BootstrapContext<CatVariant> context) {
/* 37 */     HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
/*    */     
/* 39 */     registerForAnyConditions(context, TABBY, "entity/cat/tabby");
/* 40 */     registerForAnyConditions(context, BLACK, "entity/cat/black");
/* 41 */     registerForAnyConditions(context, RED, "entity/cat/red");
/* 42 */     registerForAnyConditions(context, SIAMESE, "entity/cat/siamese");
/* 43 */     registerForAnyConditions(context, BRITISH_SHORTHAIR, "entity/cat/british_shorthair");
/* 44 */     registerForAnyConditions(context, CALICO, "entity/cat/calico");
/* 45 */     registerForAnyConditions(context, PERSIAN, "entity/cat/persian");
/* 46 */     registerForAnyConditions(context, RAGDOLL, "entity/cat/ragdoll");
/* 47 */     registerForAnyConditions(context, WHITE, "entity/cat/white");
/* 48 */     registerForAnyConditions(context, JELLIE, "entity/cat/jellie");
/*    */     
/* 50 */     register(context, ALL_BLACK, "entity/cat/all_black", new SpawnPrioritySelectors(
/* 51 */           List.of(new PriorityProvider.Selector(new StructureCheck(structures
/*    */                 
/* 53 */                 .getOrThrow(StructureTags.CATS_SPAWN_AS_BLACK)), 1), new PriorityProvider.Selector(new MoonBrightnessCheck(
/*    */                 
/* 55 */                 MinMaxBounds.Doubles.atLeast(0.9D)), 0))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   private static void registerForAnyConditions(BootstrapContext<CatVariant> context, ResourceKey<CatVariant> name, String texture) { register(context, name, texture, SpawnPrioritySelectors.fallback(0)); }
/*    */ 
/*    */   
/*    */   private static void register(BootstrapContext<CatVariant> context, ResourceKey<CatVariant> name, String texture, SpawnPrioritySelectors spawnConditions) {
/* 65 */     context.register(name, new CatVariant(new ClientAsset.ResourceTexture(
/* 66 */             Identifier.withDefaultNamespace(texture)), spawnConditions));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\feline\CatVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */