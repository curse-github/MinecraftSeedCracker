/*     */ package net.minecraft.world.entity.decoration.painting;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public class PaintingVariants
/*     */ {
/*  13 */   public static final ResourceKey<PaintingVariant> KEBAB = create("kebab");
/*  14 */   public static final ResourceKey<PaintingVariant> AZTEC = create("aztec");
/*  15 */   public static final ResourceKey<PaintingVariant> ALBAN = create("alban");
/*  16 */   public static final ResourceKey<PaintingVariant> AZTEC2 = create("aztec2");
/*  17 */   public static final ResourceKey<PaintingVariant> BOMB = create("bomb");
/*  18 */   public static final ResourceKey<PaintingVariant> PLANT = create("plant");
/*  19 */   public static final ResourceKey<PaintingVariant> WASTELAND = create("wasteland");
/*  20 */   public static final ResourceKey<PaintingVariant> POOL = create("pool");
/*  21 */   public static final ResourceKey<PaintingVariant> COURBET = create("courbet");
/*  22 */   public static final ResourceKey<PaintingVariant> SEA = create("sea");
/*  23 */   public static final ResourceKey<PaintingVariant> SUNSET = create("sunset");
/*  24 */   public static final ResourceKey<PaintingVariant> CREEBET = create("creebet");
/*  25 */   public static final ResourceKey<PaintingVariant> WANDERER = create("wanderer");
/*  26 */   public static final ResourceKey<PaintingVariant> GRAHAM = create("graham");
/*  27 */   public static final ResourceKey<PaintingVariant> MATCH = create("match");
/*  28 */   public static final ResourceKey<PaintingVariant> BUST = create("bust");
/*  29 */   public static final ResourceKey<PaintingVariant> STAGE = create("stage");
/*  30 */   public static final ResourceKey<PaintingVariant> VOID = create("void");
/*  31 */   public static final ResourceKey<PaintingVariant> SKULL_AND_ROSES = create("skull_and_roses");
/*  32 */   public static final ResourceKey<PaintingVariant> WITHER = create("wither");
/*  33 */   public static final ResourceKey<PaintingVariant> FIGHTERS = create("fighters");
/*  34 */   public static final ResourceKey<PaintingVariant> POINTER = create("pointer");
/*  35 */   public static final ResourceKey<PaintingVariant> PIGSCENE = create("pigscene");
/*  36 */   public static final ResourceKey<PaintingVariant> BURNING_SKULL = create("burning_skull");
/*  37 */   public static final ResourceKey<PaintingVariant> SKELETON = create("skeleton");
/*  38 */   public static final ResourceKey<PaintingVariant> DONKEY_KONG = create("donkey_kong");
/*  39 */   public static final ResourceKey<PaintingVariant> EARTH = create("earth");
/*  40 */   public static final ResourceKey<PaintingVariant> WIND = create("wind");
/*  41 */   public static final ResourceKey<PaintingVariant> WATER = create("water");
/*  42 */   public static final ResourceKey<PaintingVariant> FIRE = create("fire");
/*  43 */   public static final ResourceKey<PaintingVariant> BAROQUE = create("baroque");
/*  44 */   public static final ResourceKey<PaintingVariant> HUMBLE = create("humble");
/*  45 */   public static final ResourceKey<PaintingVariant> MEDITATIVE = create("meditative");
/*  46 */   public static final ResourceKey<PaintingVariant> PRAIRIE_RIDE = create("prairie_ride");
/*  47 */   public static final ResourceKey<PaintingVariant> UNPACKED = create("unpacked");
/*  48 */   public static final ResourceKey<PaintingVariant> BACKYARD = create("backyard");
/*  49 */   public static final ResourceKey<PaintingVariant> BOUQUET = create("bouquet");
/*  50 */   public static final ResourceKey<PaintingVariant> CAVEBIRD = create("cavebird");
/*  51 */   public static final ResourceKey<PaintingVariant> CHANGING = create("changing");
/*  52 */   public static final ResourceKey<PaintingVariant> COTAN = create("cotan");
/*  53 */   public static final ResourceKey<PaintingVariant> ENDBOSS = create("endboss");
/*  54 */   public static final ResourceKey<PaintingVariant> FERN = create("fern");
/*  55 */   public static final ResourceKey<PaintingVariant> FINDING = create("finding");
/*  56 */   public static final ResourceKey<PaintingVariant> LOWMIST = create("lowmist");
/*  57 */   public static final ResourceKey<PaintingVariant> ORB = create("orb");
/*  58 */   public static final ResourceKey<PaintingVariant> OWLEMONS = create("owlemons");
/*  59 */   public static final ResourceKey<PaintingVariant> PASSAGE = create("passage");
/*  60 */   public static final ResourceKey<PaintingVariant> POND = create("pond");
/*  61 */   public static final ResourceKey<PaintingVariant> SUNFLOWERS = create("sunflowers");
/*  62 */   public static final ResourceKey<PaintingVariant> TIDES = create("tides");
/*  63 */   public static final ResourceKey<PaintingVariant> DENNIS = create("dennis");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PaintingVariant> context) {
/*  66 */     register(context, KEBAB, 1, 1);
/*  67 */     register(context, AZTEC, 1, 1);
/*  68 */     register(context, ALBAN, 1, 1);
/*  69 */     register(context, AZTEC2, 1, 1);
/*  70 */     register(context, BOMB, 1, 1);
/*  71 */     register(context, PLANT, 1, 1);
/*  72 */     register(context, WASTELAND, 1, 1);
/*  73 */     register(context, POOL, 2, 1);
/*  74 */     register(context, COURBET, 2, 1);
/*  75 */     register(context, SEA, 2, 1);
/*  76 */     register(context, SUNSET, 2, 1);
/*  77 */     register(context, CREEBET, 2, 1);
/*  78 */     register(context, WANDERER, 1, 2);
/*  79 */     register(context, GRAHAM, 1, 2);
/*  80 */     register(context, MATCH, 2, 2);
/*  81 */     register(context, BUST, 2, 2);
/*  82 */     register(context, STAGE, 2, 2);
/*  83 */     register(context, VOID, 2, 2);
/*  84 */     register(context, SKULL_AND_ROSES, 2, 2);
/*  85 */     register(context, WITHER, 2, 2, false);
/*  86 */     register(context, FIGHTERS, 4, 2);
/*  87 */     register(context, POINTER, 4, 4);
/*  88 */     register(context, PIGSCENE, 4, 4);
/*  89 */     register(context, BURNING_SKULL, 4, 4);
/*  90 */     register(context, SKELETON, 4, 3);
/*  91 */     register(context, EARTH, 2, 2, false);
/*  92 */     register(context, WIND, 2, 2, false);
/*  93 */     register(context, WATER, 2, 2, false);
/*  94 */     register(context, FIRE, 2, 2, false);
/*  95 */     register(context, DONKEY_KONG, 4, 3);
/*  96 */     register(context, BAROQUE, 2, 2);
/*  97 */     register(context, HUMBLE, 2, 2);
/*  98 */     register(context, MEDITATIVE, 1, 1);
/*  99 */     register(context, PRAIRIE_RIDE, 1, 2);
/* 100 */     register(context, UNPACKED, 4, 4);
/* 101 */     register(context, BACKYARD, 3, 4);
/* 102 */     register(context, BOUQUET, 3, 3);
/* 103 */     register(context, CAVEBIRD, 3, 3);
/* 104 */     register(context, CHANGING, 4, 2);
/* 105 */     register(context, COTAN, 3, 3);
/* 106 */     register(context, ENDBOSS, 3, 3);
/* 107 */     register(context, FERN, 3, 3);
/* 108 */     register(context, FINDING, 4, 2);
/* 109 */     register(context, LOWMIST, 4, 2);
/* 110 */     register(context, ORB, 4, 4);
/* 111 */     register(context, OWLEMONS, 3, 3);
/* 112 */     register(context, PASSAGE, 4, 2);
/* 113 */     register(context, POND, 3, 4);
/* 114 */     register(context, SUNFLOWERS, 3, 3);
/* 115 */     register(context, TIDES, 3, 3);
/* 116 */     register(context, DENNIS, 3, 3);
/*     */   }
/*     */ 
/*     */   
/* 120 */   private static void register(BootstrapContext<PaintingVariant> context, ResourceKey<PaintingVariant> id, int width, int height) { register(context, id, width, height, true); }
/*     */ 
/*     */   
/*     */   private static void register(BootstrapContext<PaintingVariant> context, ResourceKey<PaintingVariant> id, int width, int height, boolean hasAuthor) {
/* 124 */     context.register(id, new PaintingVariant(width, height, id
/*     */ 
/*     */           
/* 127 */           .identifier(), 
/* 128 */           Optional.of(Component.translatable(id.identifier().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW)), 
/* 129 */           hasAuthor ? Optional.of(Component.translatable(id.identifier().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY)) : Optional.empty()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 134 */   private static ResourceKey<PaintingVariant> create(String name) { return ResourceKey.create(Registries.PAINTING_VARIANT, Identifier.withDefaultNamespace(name)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\painting\PaintingVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */