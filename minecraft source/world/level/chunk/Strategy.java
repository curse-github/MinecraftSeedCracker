/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class Strategy<T>
/*    */   extends Object {
/*  8 */   private static final Palette.Factory SINGLE_VALUE_PALETTE_FACTORY = SingleValuePalette::create;
/*  9 */   private static final Palette.Factory LINEAR_PALETTE_FACTORY = LinearPalette::create;
/* 10 */   private static final Palette.Factory HASHMAP_PALETTE_FACTORY = HashMapPalette::create;
/*    */   
/* 12 */   private static final Configuration ZERO_BITS = new Configuration.Simple(SINGLE_VALUE_PALETTE_FACTORY, 0);
/*    */   
/* 14 */   private static final Configuration ONE_BIT_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 1);
/* 15 */   private static final Configuration TWO_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 2);
/* 16 */   private static final Configuration THREE_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 3);
/* 17 */   private static final Configuration FOUR_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 4);
/*    */   
/* 19 */   private static final Configuration FIVE_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 5);
/* 20 */   private static final Configuration SIX_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 6);
/* 21 */   private static final Configuration SEVEN_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 7);
/* 22 */   private static final Configuration EIGHT_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 8);
/*    */   
/*    */   private final IdMap<T> globalMap;
/*    */   private final GlobalPalette<T> globalPalette;
/*    */   protected final int globalPaletteBitsInMemory;
/*    */   private final int bitsPerAxis;
/*    */   private final int entryCount;
/*    */   
/*    */   private Strategy(IdMap<T> globalMap, int bitsPerAxis) {
/* 31 */     this.globalMap = globalMap;
/* 32 */     this.globalPalette = new GlobalPalette(globalMap);
/* 33 */     this.globalPaletteBitsInMemory = minimumBitsRequiredForDistinctValues(globalMap.size());
/* 34 */     this.bitsPerAxis = bitsPerAxis;
/* 35 */     this.entryCount = 1 << bitsPerAxis * 3;
/*    */   }
/*    */   
/*    */   public static <T> Strategy<T> createForBlockStates(IdMap<T> registry) {
/* 39 */     return new Strategy<T>(registry, 4)
/*    */       {
/*    */         public Configuration getConfigurationForBitCount(int entryBits) {
/* 42 */           switch (entryBits) { case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 50 */             new Configuration.Global(this.globalPaletteBitsInMemory, entryBits);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> Strategy<T> createForBiomes(IdMap<T> registry) {
/* 57 */     return new Strategy<T>(registry, 2)
/*    */       {
/*    */         public Configuration getConfigurationForBitCount(int entryBits) {
/* 60 */           switch (entryBits) { case 0: case 1: case 2: case 3:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 66 */             new Configuration.Global(this.globalPaletteBitsInMemory, entryBits);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public int entryCount() { return this.entryCount; }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public int getIndex(int x, int y, int z) { return (y << this.bitsPerAxis | z) << this.bitsPerAxis | x; }
/*    */ 
/*    */ 
/*    */   
/* 81 */   public IdMap<T> globalMap() { return this.globalMap; }
/*    */ 
/*    */ 
/*    */   
/* 85 */   public GlobalPalette<T> globalPalette() { return this.globalPalette; }
/*    */ 
/*    */   
/*    */   protected abstract Configuration getConfigurationForBitCount(int paramInt);
/*    */   
/*    */   protected Configuration getConfigurationForPaletteSize(int paletteSize) {
/* 91 */     int bits = minimumBitsRequiredForDistinctValues(paletteSize);
/* 92 */     return getConfigurationForBitCount(bits);
/*    */   }
/*    */ 
/*    */   
/* 96 */   private static int minimumBitsRequiredForDistinctValues(int count) { return Mth.ceillog2(count); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\Strategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */