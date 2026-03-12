/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LevelChunkSection
/*     */ {
/*     */   public static final int SECTION_WIDTH = 16;
/*     */   public static final int SECTION_HEIGHT = 16;
/*     */   public static final int SECTION_SIZE = 4096;
/*     */   public static final int BIOME_CONTAINER_BITS = 2;
/*     */   private short nonEmptyBlockCount;
/*     */   private short tickingBlockCount;
/*     */   private short tickingFluidCount;
/*     */   private final PalettedContainer<BlockState> states;
/*     */   private PalettedContainerRO<Holder<Biome>> biomes;
/*     */   
/*     */   private LevelChunkSection(LevelChunkSection source) {
/*  28 */     this.nonEmptyBlockCount = source.nonEmptyBlockCount;
/*  29 */     this.tickingBlockCount = source.tickingBlockCount;
/*  30 */     this.tickingFluidCount = source.tickingFluidCount;
/*  31 */     this.states = source.states.copy();
/*  32 */     this.biomes = source.biomes.copy();
/*     */   }
/*     */   
/*     */   public LevelChunkSection(PalettedContainer<BlockState> states, PalettedContainerRO<Holder<Biome>> biomes) {
/*  36 */     this.states = states;
/*  37 */     this.biomes = biomes;
/*  38 */     recalcBlockCounts();
/*     */   }
/*     */   
/*     */   public LevelChunkSection(PalettedContainerFactory containerFactory) {
/*  42 */     this.states = containerFactory.createForBlockStates();
/*  43 */     this.biomes = containerFactory.createForBiomes();
/*     */   }
/*     */ 
/*     */   
/*  47 */   public BlockState getBlockState(int sectionX, int sectionY, int sectionZ) { return (BlockState)this.states.get(sectionX, sectionY, sectionZ); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public FluidState getFluidState(int sectionX, int sectionY, int sectionZ) { return ((BlockState)this.states.get(sectionX, sectionY, sectionZ)).getFluidState(); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public void acquire() { this.states.acquire(); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public void release() { this.states.release(); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public BlockState setBlockState(int sectionX, int sectionY, int sectionZ, BlockState state) { return setBlockState(sectionX, sectionY, sectionZ, state, true); }
/*     */ 
/*     */   
/*     */   public BlockState setBlockState(int sectionX, int sectionY, int sectionZ, BlockState state, boolean checkThreading) {
/*     */     BlockState previous;
/*  68 */     if (checkThreading) {
/*  69 */       previous = (BlockState)this.states.getAndSet(sectionX, sectionY, sectionZ, state);
/*     */     } else {
/*  71 */       previous = (BlockState)this.states.getAndSetUnchecked(sectionX, sectionY, sectionZ, state);
/*     */     } 
/*  73 */     FluidState previousFluid = previous.getFluidState();
/*  74 */     FluidState fluid = state.getFluidState();
/*     */     
/*  76 */     if (!previous.isAir()) {
/*  77 */       this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount - 1);
/*  78 */       if (previous.isRandomlyTicking()) {
/*  79 */         this.tickingBlockCount = (short)(this.tickingBlockCount - 1);
/*     */       }
/*     */     } 
/*     */     
/*  83 */     if (!previousFluid.isEmpty()) {
/*  84 */       this.tickingFluidCount = (short)(this.tickingFluidCount - 1);
/*     */     }
/*     */     
/*  87 */     if (!state.isAir()) {
/*  88 */       this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + 1);
/*  89 */       if (state.isRandomlyTicking()) {
/*  90 */         this.tickingBlockCount = (short)(this.tickingBlockCount + 1);
/*     */       }
/*     */     } 
/*     */     
/*  94 */     if (!fluid.isEmpty()) {
/*  95 */       this.tickingFluidCount = (short)(this.tickingFluidCount + 1);
/*     */     }
/*     */     
/*  98 */     return previous;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public boolean hasOnlyAir() { return (this.nonEmptyBlockCount == 0); }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public boolean isRandomlyTicking() { return (isRandomlyTickingBlocks() || isRandomlyTickingFluids()); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public boolean isRandomlyTickingBlocks() { return (this.tickingBlockCount > 0); }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public boolean isRandomlyTickingFluids() { return (this.tickingFluidCount > 0); }
/*     */   
/*     */   public void recalcBlockCounts() {
/*     */     class BlockCounter extends Object implements PalettedContainer.CountConsumer<BlockState> {
/*     */       public int nonEmptyBlockCount;
/*     */       public int tickingBlockCount;
/*     */       public int tickingFluidCount;
/*     */       
/*     */       BlockCounter(LevelChunkSection this$0) {}
/*     */       
/*     */       public void accept(BlockState state, int count) {
/* 125 */         FluidState fluid = state.getFluidState();
/*     */         
/* 127 */         if (!state.isAir()) {
/* 128 */           this.nonEmptyBlockCount += count;
/* 129 */           if (state.isRandomlyTicking()) {
/* 130 */             this.tickingBlockCount += count;
/*     */           }
/*     */         } 
/* 133 */         if (!fluid.isEmpty()) {
/* 134 */           this.nonEmptyBlockCount += count;
/* 135 */           if (fluid.isRandomlyTicking()) {
/* 136 */             this.tickingFluidCount += count;
/*     */           }
/*     */         } 
/*     */       }
/*     */     };
/*     */     
/* 142 */     BlockCounter blockCounter = new BlockCounter(this);
/* 143 */     this.states.count(blockCounter);
/*     */ 
/*     */     
/* 146 */     this.nonEmptyBlockCount = (short)blockCounter.nonEmptyBlockCount;
/* 147 */     this.tickingBlockCount = (short)blockCounter.tickingBlockCount;
/* 148 */     this.tickingFluidCount = (short)blockCounter.tickingFluidCount;
/*     */   }
/*     */ 
/*     */   
/* 152 */   public PalettedContainer<BlockState> getStates() { return this.states; }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public PalettedContainerRO<Holder<Biome>> getBiomes() { return this.biomes; }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf buffer) {
/* 160 */     this.nonEmptyBlockCount = buffer.readShort();
/* 161 */     this.states.read(buffer);
/* 162 */     PalettedContainer<Holder<Biome>> biomes = this.biomes.recreate();
/* 163 */     biomes.read(buffer);
/* 164 */     this.biomes = biomes;
/*     */   }
/*     */   
/*     */   public void readBiomes(FriendlyByteBuf buffer) {
/* 168 */     PalettedContainer<Holder<Biome>> biomes = this.biomes.recreate();
/* 169 */     biomes.read(buffer);
/* 170 */     this.biomes = biomes;
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf buffer) {
/* 174 */     buffer.writeShort(this.nonEmptyBlockCount);
/* 175 */     this.states.write(buffer);
/* 176 */     this.biomes.write(buffer);
/*     */   }
/*     */ 
/*     */   
/* 180 */   public int getSerializedSize() { return 2 + this.states.getSerializedSize() + this.biomes.getSerializedSize(); }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public boolean maybeHas(Predicate<BlockState> predicate) { return this.states.maybeHas(predicate); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) { return (Holder)this.biomes.get(quartX, quartY, quartZ); }
/*     */ 
/*     */   
/*     */   public void fillBiomesFromNoise(BiomeResolver biomeResolver, Climate.Sampler sampler, int quartMinX, int quartMinY, int quartMinZ) {
/* 192 */     PalettedContainer<Holder<Biome>> newBiomes = this.biomes.recreate();
/*     */     
/* 194 */     int size = 4;
/* 195 */     for (int x = 0; x < 4; x++) {
/* 196 */       for (int y = 0; y < 4; y++) {
/* 197 */         for (int z = 0; z < 4; z++) {
/* 198 */           newBiomes.getAndSetUnchecked(x, y, z, biomeResolver.getNoiseBiome(quartMinX + x, quartMinY + y, quartMinZ + z, sampler));
/*     */         }
/*     */       } 
/*     */     } 
/* 202 */     this.biomes = newBiomes;
/*     */   }
/*     */ 
/*     */   
/* 206 */   public LevelChunkSection copy() { return new LevelChunkSection(this); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\LevelChunkSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */