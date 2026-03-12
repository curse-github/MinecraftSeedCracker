/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.CreativeModeTabs;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.FireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @SuppressForbidden(reason = "System.out setup")
/*     */ public class Bootstrap {
/*  38 */   public static final PrintStream STDOUT = System.out;
/*     */ 
/*     */   
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   public static final AtomicLong bootstrapDuration = new AtomicLong(-1L);
/*     */   
/*     */   public static void bootStrap() {
/*  46 */     if (isBootstrapped) {
/*     */       return;
/*     */     }
/*  49 */     isBootstrapped = true;
/*     */     
/*  51 */     start = Instant.now();
/*     */     
/*  53 */     if (BuiltInRegistries.REGISTRY.keySet().isEmpty()) {
/*  54 */       throw new IllegalStateException("Unable to load registries");
/*     */     }
/*     */     
/*  57 */     FireBlock.bootStrap();
/*  58 */     ComposterBlock.bootStrap();
/*     */     
/*  60 */     if (EntityType.getKey(EntityType.PLAYER) == null) {
/*  61 */       throw new IllegalStateException("Failed loading EntityTypes");
/*     */     }
/*     */     
/*  64 */     EntitySelectorOptions.bootStrap();
/*     */     
/*  66 */     DispenseItemBehavior.bootStrap();
/*     */     
/*  68 */     CauldronInteraction.bootStrap();
/*     */     
/*  70 */     BuiltInRegistries.bootStrap();
/*     */     
/*  72 */     CreativeModeTabs.validate();
/*     */     
/*  74 */     wrapStreams();
/*     */     
/*  76 */     bootstrapDuration.set(Duration.between(start, Instant.now()).toMillis());
/*     */   }
/*     */   
/*     */   private static <T> void checkTranslations(Iterable<T> registry, Function<T, String> descriptionGetter, Set<String> output) {
/*  80 */     Language language = Language.getInstance();
/*  81 */     registry.forEach(t -> {
/*  82 */           String id = (String)descriptionGetter.apply(t);
/*  83 */           if (!language.has(id)) {
/*  84 */             output.add(id);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static void checkGameruleTranslations(final Set<String> missing) {
/*  90 */     final Language language = Language.getInstance();
/*  91 */     GameRules rules = new GameRules(FeatureFlags.REGISTRY.allFlags());
/*  92 */     rules.visitGameRuleTypes(new GameRuleTypeVisitor()
/*     */         {
/*     */           public <T> void visit(GameRule<T> gameRule) {
/*  95 */             if (!language.has(gameRule.getDescriptionId())) {
/*  96 */               missing.add(gameRule.id());
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Set<String> getMissingTranslations() {
/* 103 */     missing = new TreeSet();
/* 104 */     checkTranslations(BuiltInRegistries.ATTRIBUTE, Attribute::getDescriptionId, missing);
/* 105 */     checkTranslations(BuiltInRegistries.ENTITY_TYPE, EntityType::getDescriptionId, missing);
/* 106 */     checkTranslations(BuiltInRegistries.MOB_EFFECT, MobEffect::getDescriptionId, missing);
/* 107 */     checkTranslations(BuiltInRegistries.ITEM, Item::getDescriptionId, missing);
/* 108 */     checkTranslations(BuiltInRegistries.BLOCK, BlockBehaviour::getDescriptionId, missing);
/* 109 */     checkTranslations(BuiltInRegistries.CUSTOM_STAT, id -> "stat." + id.toString().replace(':', '.'), missing);
/*     */     
/* 111 */     checkGameruleTranslations(missing);
/* 112 */     return missing;
/*     */   }
/*     */   
/*     */   public static void checkBootstrapCalled(Supplier<String> location) {
/* 116 */     if (!isBootstrapped) {
/* 117 */       throw createBootstrapException(location);
/*     */     }
/*     */   }
/*     */   
/*     */   private static RuntimeException createBootstrapException(Supplier<String> location) {
/*     */     try {
/* 123 */       String resolvedLocation = (String)location.get();
/* 124 */       return new IllegalArgumentException("Not bootstrapped (called from " + resolvedLocation + ")");
/* 125 */     } catch (Exception e) {
/* 126 */       RuntimeException result = new IllegalArgumentException("Not bootstrapped (failed to resolve location)");
/* 127 */       result.addSuppressed(e);
/* 128 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 133 */     checkBootstrapCalled(() -> "validate");
/*     */     
/* 135 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 136 */       getMissingTranslations().forEach(key -> LOGGER.error("Missing translations: {}", key));
/* 137 */       Commands.validate();
/*     */     } 
/*     */     
/* 140 */     DefaultAttributes.validate();
/*     */   }
/*     */   
/*     */   private static void wrapStreams() {
/* 144 */     if (LOGGER.isDebugEnabled()) {
/* 145 */       System.setErr(new DebugLoggedPrintStream("STDERR", System.err));
/* 146 */       System.setOut(new DebugLoggedPrintStream("STDOUT", STDOUT));
/*     */     } else {
/* 148 */       System.setErr(new LoggedPrintStream("STDERR", System.err));
/* 149 */       System.setOut(new LoggedPrintStream("STDOUT", STDOUT));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 154 */   public static void realStdoutPrintln(String string) { STDOUT.println(string); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\Bootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */