/*    */ package net.minecraft.world.entity.animal.sheep;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class SheepColorSpawnRules {
/*    */   private static SheepColorProvider commonColors(DyeColor defaultColor) {
/* 12 */     return weighted(builder()
/* 13 */         .add(single(defaultColor), 499)
/* 14 */         .add(single(DyeColor.PINK), 1)
/* 15 */         .build());
/*    */   }
/*    */ 
/*    */   
/* 19 */   private static final SheepColorSpawnConfiguration TEMPERATE_SPAWN_CONFIGURATION = new SheepColorSpawnConfiguration(
/* 20 */       weighted(builder()
/* 21 */         .add(single(DyeColor.BLACK), 5)
/* 22 */         .add(single(DyeColor.GRAY), 5)
/* 23 */         .add(single(DyeColor.LIGHT_GRAY), 5)
/* 24 */         .add(single(DyeColor.BROWN), 3)
/* 25 */         .add(commonColors(DyeColor.WHITE), 82)
/* 26 */         .build()));
/*    */ 
/*    */   
/* 29 */   private static final SheepColorSpawnConfiguration WARM_SPAWN_CONFIGURATION = new SheepColorSpawnConfiguration(
/* 30 */       weighted(builder()
/* 31 */         .add(single(DyeColor.GRAY), 5)
/* 32 */         .add(single(DyeColor.LIGHT_GRAY), 5)
/* 33 */         .add(single(DyeColor.WHITE), 5)
/* 34 */         .add(single(DyeColor.BLACK), 3)
/* 35 */         .add(commonColors(DyeColor.BROWN), 82)
/* 36 */         .build()));
/*    */ 
/*    */   
/* 39 */   private static final SheepColorSpawnConfiguration COLD_SPAWN_CONFIGURATION = new SheepColorSpawnConfiguration(
/* 40 */       weighted(builder()
/* 41 */         .add(single(DyeColor.LIGHT_GRAY), 5)
/* 42 */         .add(single(DyeColor.GRAY), 5)
/* 43 */         .add(single(DyeColor.WHITE), 5)
/* 44 */         .add(single(DyeColor.BROWN), 3)
/* 45 */         .add(commonColors(DyeColor.BLACK), 82)
/* 46 */         .build()));
/*    */ 
/*    */   
/*    */   public static DyeColor getSheepColor(Holder<Biome> biome, RandomSource random) {
/* 50 */     SheepColorSpawnConfiguration sheepColorConfiguration = getSheepColorConfiguration(biome);
/* 51 */     return sheepColorConfiguration.colors().get(random);
/*    */   } @FunctionalInterface
/*    */   private static interface SheepColorProvider {
/*    */     DyeColor get(RandomSource param1RandomSource); } private static SheepColorSpawnConfiguration getSheepColorConfiguration(Holder<Biome> biome) {
/* 55 */     if (biome.is(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS))
/* 56 */       return WARM_SPAWN_CONFIGURATION; 
/* 57 */     if (biome.is(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS))
/* 58 */       return COLD_SPAWN_CONFIGURATION; 
/* 59 */     return TEMPERATE_SPAWN_CONFIGURATION;
/*    */   }
/*    */   private static final class SheepColorSpawnConfiguration extends Record { private final SheepColorSpawnRules.SheepColorProvider colors;
/* 62 */     private SheepColorSpawnConfiguration(SheepColorSpawnRules.SheepColorProvider colors) { this.colors = colors; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/sheep/SheepColorSpawnRules$SheepColorSpawnConfiguration;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 62 */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/sheep/SheepColorSpawnRules$SheepColorSpawnConfiguration; } public SheepColorSpawnRules.SheepColorProvider colors() { return this.colors; }
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/sheep/SheepColorSpawnRules$SheepColorSpawnConfiguration;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/sheep/SheepColorSpawnRules$SheepColorSpawnConfiguration; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/sheep/SheepColorSpawnRules$SheepColorSpawnConfiguration;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/sheep/SheepColorSpawnRules$SheepColorSpawnConfiguration;
/*    */       //   0	8	1	o	Ljava/lang/Object; } }
/*    */ 
/*    */   
/*    */   private static SheepColorProvider weighted(WeightedList<SheepColorProvider> elements) {
/* 70 */     if (elements.isEmpty()) {
/* 71 */       throw new IllegalArgumentException("List must be non-empty");
/*    */     }
/*    */     
/* 74 */     return random -> ((SheepColorProvider)elements.getRandomOrThrow(random)).get(random);
/*    */   }
/*    */ 
/*    */   
/* 78 */   private static SheepColorProvider single(DyeColor color) { return random -> color; }
/*    */ 
/*    */ 
/*    */   
/* 82 */   private static WeightedList.Builder<SheepColorProvider> builder() { return WeightedList.builder(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sheep\SheepColorSpawnRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */