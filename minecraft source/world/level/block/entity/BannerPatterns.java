/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public class BannerPatterns {
/*   9 */   public static final ResourceKey<BannerPattern> BASE = create("base");
/*  10 */   public static final ResourceKey<BannerPattern> SQUARE_BOTTOM_LEFT = create("square_bottom_left");
/*  11 */   public static final ResourceKey<BannerPattern> SQUARE_BOTTOM_RIGHT = create("square_bottom_right");
/*  12 */   public static final ResourceKey<BannerPattern> SQUARE_TOP_LEFT = create("square_top_left");
/*  13 */   public static final ResourceKey<BannerPattern> SQUARE_TOP_RIGHT = create("square_top_right");
/*  14 */   public static final ResourceKey<BannerPattern> STRIPE_BOTTOM = create("stripe_bottom");
/*  15 */   public static final ResourceKey<BannerPattern> STRIPE_TOP = create("stripe_top");
/*  16 */   public static final ResourceKey<BannerPattern> STRIPE_LEFT = create("stripe_left");
/*  17 */   public static final ResourceKey<BannerPattern> STRIPE_RIGHT = create("stripe_right");
/*  18 */   public static final ResourceKey<BannerPattern> STRIPE_CENTER = create("stripe_center");
/*  19 */   public static final ResourceKey<BannerPattern> STRIPE_MIDDLE = create("stripe_middle");
/*  20 */   public static final ResourceKey<BannerPattern> STRIPE_DOWNRIGHT = create("stripe_downright");
/*  21 */   public static final ResourceKey<BannerPattern> STRIPE_DOWNLEFT = create("stripe_downleft");
/*  22 */   public static final ResourceKey<BannerPattern> STRIPE_SMALL = create("small_stripes");
/*  23 */   public static final ResourceKey<BannerPattern> CROSS = create("cross");
/*  24 */   public static final ResourceKey<BannerPattern> STRAIGHT_CROSS = create("straight_cross");
/*  25 */   public static final ResourceKey<BannerPattern> TRIANGLE_BOTTOM = create("triangle_bottom");
/*  26 */   public static final ResourceKey<BannerPattern> TRIANGLE_TOP = create("triangle_top");
/*  27 */   public static final ResourceKey<BannerPattern> TRIANGLES_BOTTOM = create("triangles_bottom");
/*  28 */   public static final ResourceKey<BannerPattern> TRIANGLES_TOP = create("triangles_top");
/*  29 */   public static final ResourceKey<BannerPattern> DIAGONAL_LEFT = create("diagonal_left");
/*  30 */   public static final ResourceKey<BannerPattern> DIAGONAL_RIGHT = create("diagonal_up_right");
/*  31 */   public static final ResourceKey<BannerPattern> DIAGONAL_LEFT_MIRROR = create("diagonal_up_left");
/*  32 */   public static final ResourceKey<BannerPattern> DIAGONAL_RIGHT_MIRROR = create("diagonal_right");
/*  33 */   public static final ResourceKey<BannerPattern> CIRCLE_MIDDLE = create("circle");
/*  34 */   public static final ResourceKey<BannerPattern> RHOMBUS_MIDDLE = create("rhombus");
/*  35 */   public static final ResourceKey<BannerPattern> HALF_VERTICAL = create("half_vertical");
/*  36 */   public static final ResourceKey<BannerPattern> HALF_HORIZONTAL = create("half_horizontal");
/*  37 */   public static final ResourceKey<BannerPattern> HALF_VERTICAL_MIRROR = create("half_vertical_right");
/*  38 */   public static final ResourceKey<BannerPattern> HALF_HORIZONTAL_MIRROR = create("half_horizontal_bottom");
/*  39 */   public static final ResourceKey<BannerPattern> BORDER = create("border");
/*  40 */   public static final ResourceKey<BannerPattern> CURLY_BORDER = create("curly_border");
/*  41 */   public static final ResourceKey<BannerPattern> GRADIENT = create("gradient");
/*  42 */   public static final ResourceKey<BannerPattern> GRADIENT_UP = create("gradient_up");
/*  43 */   public static final ResourceKey<BannerPattern> BRICKS = create("bricks");
/*  44 */   public static final ResourceKey<BannerPattern> GLOBE = create("globe");
/*  45 */   public static final ResourceKey<BannerPattern> CREEPER = create("creeper");
/*  46 */   public static final ResourceKey<BannerPattern> SKULL = create("skull");
/*  47 */   public static final ResourceKey<BannerPattern> FLOWER = create("flower");
/*  48 */   public static final ResourceKey<BannerPattern> MOJANG = create("mojang");
/*  49 */   public static final ResourceKey<BannerPattern> PIGLIN = create("piglin");
/*  50 */   public static final ResourceKey<BannerPattern> FLOW = create("flow");
/*  51 */   public static final ResourceKey<BannerPattern> GUSTER = create("guster");
/*     */ 
/*     */   
/*  54 */   private static ResourceKey<BannerPattern> create(String id) { return ResourceKey.create(Registries.BANNER_PATTERN, Identifier.withDefaultNamespace(id)); }
/*     */ 
/*     */   
/*     */   public static void bootstrap(BootstrapContext<BannerPattern> context) {
/*  58 */     register(context, BASE);
/*  59 */     register(context, SQUARE_BOTTOM_LEFT);
/*  60 */     register(context, SQUARE_BOTTOM_RIGHT);
/*  61 */     register(context, SQUARE_TOP_LEFT);
/*  62 */     register(context, SQUARE_TOP_RIGHT);
/*  63 */     register(context, STRIPE_BOTTOM);
/*  64 */     register(context, STRIPE_TOP);
/*  65 */     register(context, STRIPE_LEFT);
/*  66 */     register(context, STRIPE_RIGHT);
/*  67 */     register(context, STRIPE_CENTER);
/*  68 */     register(context, STRIPE_MIDDLE);
/*  69 */     register(context, STRIPE_DOWNRIGHT);
/*  70 */     register(context, STRIPE_DOWNLEFT);
/*  71 */     register(context, STRIPE_SMALL);
/*  72 */     register(context, CROSS);
/*  73 */     register(context, STRAIGHT_CROSS);
/*  74 */     register(context, TRIANGLE_BOTTOM);
/*  75 */     register(context, TRIANGLE_TOP);
/*  76 */     register(context, TRIANGLES_BOTTOM);
/*  77 */     register(context, TRIANGLES_TOP);
/*  78 */     register(context, DIAGONAL_LEFT);
/*  79 */     register(context, DIAGONAL_RIGHT);
/*  80 */     register(context, DIAGONAL_LEFT_MIRROR);
/*  81 */     register(context, DIAGONAL_RIGHT_MIRROR);
/*  82 */     register(context, CIRCLE_MIDDLE);
/*  83 */     register(context, RHOMBUS_MIDDLE);
/*  84 */     register(context, HALF_VERTICAL);
/*  85 */     register(context, HALF_HORIZONTAL);
/*  86 */     register(context, HALF_VERTICAL_MIRROR);
/*  87 */     register(context, HALF_HORIZONTAL_MIRROR);
/*  88 */     register(context, BORDER);
/*  89 */     register(context, GRADIENT);
/*  90 */     register(context, GRADIENT_UP);
/*     */     
/*  92 */     register(context, BRICKS);
/*  93 */     register(context, CURLY_BORDER);
/*  94 */     register(context, GLOBE);
/*  95 */     register(context, CREEPER);
/*  96 */     register(context, SKULL);
/*  97 */     register(context, FLOWER);
/*  98 */     register(context, MOJANG);
/*  99 */     register(context, PIGLIN);
/* 100 */     register(context, FLOW);
/* 101 */     register(context, GUSTER);
/*     */   }
/*     */   
/*     */   public static void register(BootstrapContext<BannerPattern> context, ResourceKey<BannerPattern> key) {
/* 105 */     context.register(key, new BannerPattern(key
/* 106 */           .identifier(), "block.minecraft.banner." + key
/* 107 */           .identifier().toShortLanguageKey()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BannerPatterns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */