/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ 
/*     */ public class DecoratedPotPatterns
/*     */ {
/*  14 */   public static final ResourceKey<DecoratedPotPattern> BLANK = create("blank");
/*  15 */   public static final ResourceKey<DecoratedPotPattern> ANGLER = create("angler");
/*  16 */   public static final ResourceKey<DecoratedPotPattern> ARCHER = create("archer");
/*  17 */   public static final ResourceKey<DecoratedPotPattern> ARMS_UP = create("arms_up");
/*  18 */   public static final ResourceKey<DecoratedPotPattern> BLADE = create("blade");
/*  19 */   public static final ResourceKey<DecoratedPotPattern> BREWER = create("brewer");
/*  20 */   public static final ResourceKey<DecoratedPotPattern> BURN = create("burn");
/*  21 */   public static final ResourceKey<DecoratedPotPattern> DANGER = create("danger");
/*  22 */   public static final ResourceKey<DecoratedPotPattern> EXPLORER = create("explorer");
/*  23 */   public static final ResourceKey<DecoratedPotPattern> FLOW = create("flow");
/*  24 */   public static final ResourceKey<DecoratedPotPattern> FRIEND = create("friend");
/*  25 */   public static final ResourceKey<DecoratedPotPattern> GUSTER = create("guster");
/*  26 */   public static final ResourceKey<DecoratedPotPattern> HEART = create("heart");
/*  27 */   public static final ResourceKey<DecoratedPotPattern> HEARTBREAK = create("heartbreak");
/*  28 */   public static final ResourceKey<DecoratedPotPattern> HOWL = create("howl");
/*  29 */   public static final ResourceKey<DecoratedPotPattern> MINER = create("miner");
/*  30 */   public static final ResourceKey<DecoratedPotPattern> MOURNER = create("mourner");
/*  31 */   public static final ResourceKey<DecoratedPotPattern> PLENTY = create("plenty");
/*  32 */   public static final ResourceKey<DecoratedPotPattern> PRIZE = create("prize");
/*  33 */   public static final ResourceKey<DecoratedPotPattern> SCRAPE = create("scrape");
/*  34 */   public static final ResourceKey<DecoratedPotPattern> SHEAF = create("sheaf");
/*  35 */   public static final ResourceKey<DecoratedPotPattern> SHELTER = create("shelter");
/*  36 */   public static final ResourceKey<DecoratedPotPattern> SKULL = create("skull");
/*  37 */   public static final ResourceKey<DecoratedPotPattern> SNORT = create("snort");
/*     */   
/*  39 */   private static final Map<Item, ResourceKey<DecoratedPotPattern>> ITEM_TO_POT_TEXTURE = Map.ofEntries(new Map.Entry[] { 
/*  40 */         Map.entry(Items.BRICK, BLANK), 
/*     */         
/*  42 */         Map.entry(Items.ANGLER_POTTERY_SHERD, ANGLER), 
/*  43 */         Map.entry(Items.ARCHER_POTTERY_SHERD, ARCHER), 
/*  44 */         Map.entry(Items.ARMS_UP_POTTERY_SHERD, ARMS_UP), 
/*  45 */         Map.entry(Items.BLADE_POTTERY_SHERD, BLADE), 
/*  46 */         Map.entry(Items.BREWER_POTTERY_SHERD, BREWER), 
/*  47 */         Map.entry(Items.BURN_POTTERY_SHERD, BURN), 
/*  48 */         Map.entry(Items.DANGER_POTTERY_SHERD, DANGER), 
/*  49 */         Map.entry(Items.EXPLORER_POTTERY_SHERD, EXPLORER), 
/*  50 */         Map.entry(Items.FLOW_POTTERY_SHERD, FLOW), 
/*  51 */         Map.entry(Items.FRIEND_POTTERY_SHERD, FRIEND), 
/*  52 */         Map.entry(Items.GUSTER_POTTERY_SHERD, GUSTER), 
/*  53 */         Map.entry(Items.HEART_POTTERY_SHERD, HEART), 
/*  54 */         Map.entry(Items.HEARTBREAK_POTTERY_SHERD, HEARTBREAK), 
/*  55 */         Map.entry(Items.HOWL_POTTERY_SHERD, HOWL), 
/*  56 */         Map.entry(Items.MINER_POTTERY_SHERD, MINER), 
/*  57 */         Map.entry(Items.MOURNER_POTTERY_SHERD, MOURNER), 
/*  58 */         Map.entry(Items.PLENTY_POTTERY_SHERD, PLENTY), 
/*  59 */         Map.entry(Items.PRIZE_POTTERY_SHERD, PRIZE), 
/*  60 */         Map.entry(Items.SCRAPE_POTTERY_SHERD, SCRAPE), 
/*  61 */         Map.entry(Items.SHEAF_POTTERY_SHERD, SHEAF), 
/*  62 */         Map.entry(Items.SHELTER_POTTERY_SHERD, SHELTER), 
/*  63 */         Map.entry(Items.SKULL_POTTERY_SHERD, SKULL), 
/*  64 */         Map.entry(Items.SNORT_POTTERY_SHERD, SNORT) });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static ResourceKey<DecoratedPotPattern> getPatternFromItem(Item item) { return (ResourceKey)ITEM_TO_POT_TEXTURE.get(item); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static ResourceKey<DecoratedPotPattern> create(String id) { return ResourceKey.create(Registries.DECORATED_POT_PATTERN, Identifier.withDefaultNamespace(id)); }
/*     */ 
/*     */   
/*     */   public static DecoratedPotPattern bootstrap(Registry<DecoratedPotPattern> registry) {
/*  77 */     register(registry, ANGLER, "angler_pottery_pattern");
/*  78 */     register(registry, ARCHER, "archer_pottery_pattern");
/*  79 */     register(registry, ARMS_UP, "arms_up_pottery_pattern");
/*  80 */     register(registry, BLADE, "blade_pottery_pattern");
/*  81 */     register(registry, BREWER, "brewer_pottery_pattern");
/*  82 */     register(registry, BURN, "burn_pottery_pattern");
/*  83 */     register(registry, DANGER, "danger_pottery_pattern");
/*  84 */     register(registry, EXPLORER, "explorer_pottery_pattern");
/*  85 */     register(registry, FLOW, "flow_pottery_pattern");
/*  86 */     register(registry, FRIEND, "friend_pottery_pattern");
/*  87 */     register(registry, GUSTER, "guster_pottery_pattern");
/*  88 */     register(registry, HEART, "heart_pottery_pattern");
/*  89 */     register(registry, HEARTBREAK, "heartbreak_pottery_pattern");
/*  90 */     register(registry, HOWL, "howl_pottery_pattern");
/*  91 */     register(registry, MINER, "miner_pottery_pattern");
/*  92 */     register(registry, MOURNER, "mourner_pottery_pattern");
/*  93 */     register(registry, PLENTY, "plenty_pottery_pattern");
/*  94 */     register(registry, PRIZE, "prize_pottery_pattern");
/*  95 */     register(registry, SCRAPE, "scrape_pottery_pattern");
/*  96 */     register(registry, SHEAF, "sheaf_pottery_pattern");
/*  97 */     register(registry, SHELTER, "shelter_pottery_pattern");
/*  98 */     register(registry, SKULL, "skull_pottery_pattern");
/*  99 */     register(registry, SNORT, "snort_pottery_pattern");
/*     */     
/* 101 */     return register(registry, BLANK, "decorated_pot_side");
/*     */   }
/*     */ 
/*     */   
/* 105 */   private static DecoratedPotPattern register(Registry<DecoratedPotPattern> registry, ResourceKey<DecoratedPotPattern> id, String assetId) { return (DecoratedPotPattern)Registry.register(registry, id, new DecoratedPotPattern(Identifier.withDefaultNamespace(assetId))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\DecoratedPotPatterns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */