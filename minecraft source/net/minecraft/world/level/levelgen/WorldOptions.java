/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalLong;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class WorldOptions {
/* 13 */   public static final MapCodec<WorldOptions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.LONG
/* 14 */         .fieldOf("seed").stable().forGetter(WorldOptions::seed), Codec.BOOL
/*    */         
/* 16 */         .fieldOf("generate_features").orElse(Boolean.valueOf(true)).stable().forGetter(WorldOptions::generateStructures), Codec.BOOL
/* 17 */         .fieldOf("bonus_chest").orElse(Boolean.valueOf(false)).stable().forGetter(WorldOptions::generateBonusChest), Codec.STRING
/* 18 */         .lenientOptionalFieldOf("legacy_custom_options").stable().forGetter(()))
/* 19 */       .apply(i, i.stable(WorldOptions::new)));
/*    */   
/* 21 */   public static final WorldOptions DEMO_OPTIONS = new WorldOptions("North Carolina".hashCode(), true, true);
/*    */   
/*    */   private final long seed;
/*    */   
/*    */   private final boolean generateStructures;
/*    */   
/*    */   private final boolean generateBonusChest;
/*    */   private final Optional<String> legacyCustomOptions;
/*    */   
/* 30 */   public WorldOptions(long seed, boolean generateStructures, boolean generateBonusChest) { this(seed, generateStructures, generateBonusChest, Optional.empty()); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static WorldOptions defaultWithRandomSeed() { return new WorldOptions(randomSeed(), true, false); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static WorldOptions testWorldWithRandomSeed() { return new WorldOptions(randomSeed(), false, false); }
/*    */ 
/*    */   
/*    */   private WorldOptions(long seed, boolean generateStructures, boolean generateBonusChest, Optional<String> legacyCustomOptions) {
/* 42 */     this.seed = seed;
/* 43 */     this.generateStructures = generateStructures;
/* 44 */     this.generateBonusChest = generateBonusChest;
/* 45 */     this.legacyCustomOptions = legacyCustomOptions;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public long seed() { return this.seed; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean generateStructures() { return this.generateStructures; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean generateBonusChest() { return this.generateBonusChest; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean isOldCustomizedWorld() { return this.legacyCustomOptions.isPresent(); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public WorldOptions withBonusChest(boolean generateBonusChest) { return new WorldOptions(this.seed, this.generateStructures, generateBonusChest, this.legacyCustomOptions); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public WorldOptions withStructures(boolean generateStructures) { return new WorldOptions(this.seed, generateStructures, this.generateBonusChest, this.legacyCustomOptions); }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public WorldOptions withSeed(OptionalLong seed) { return new WorldOptions(seed.orElse(randomSeed()), this.generateStructures, this.generateBonusChest, this.legacyCustomOptions); }
/*    */ 
/*    */   
/*    */   public static OptionalLong parseSeed(String seedString) {
/* 77 */     seedString = seedString.trim();
/*    */     
/* 79 */     if (StringUtils.isEmpty(seedString)) {
/* 80 */       return OptionalLong.empty();
/*    */     }
/*    */     
/*    */     try {
/* 84 */       return OptionalLong.of(Long.parseLong(seedString));
/* 85 */     } catch (NumberFormatException e) {
/*    */       
/* 87 */       return OptionalLong.of(seedString.hashCode());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 92 */   public static long randomSeed() { return RandomSource.create().nextLong(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\WorldOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */