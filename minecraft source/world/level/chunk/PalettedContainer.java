/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.SimpleBitStorage;
/*     */ import net.minecraft.util.ThreadingDetector;
/*     */ import net.minecraft.util.ZeroBitStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PalettedContainer<T>
/*     */   extends Object
/*     */   implements PaletteResize<T>, PalettedContainerRO<T>
/*     */ {
/*     */   private static final int MIN_PALETTE_BITS = 0;
/*     */   private final Strategy<T> strategy;
/*     */   private final ThreadingDetector threadingDetector;
/*     */   
/*  39 */   public void acquire() { this.threadingDetector.checkAndLock(); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public void release() { this.threadingDetector.checkAndUnlock(); }
/*     */ 
/*     */   
/*     */   public static <T> Codec<PalettedContainer<T>> codecRW(Codec<T> elementCodec, Strategy<T> strategy, T defaultValue) {
/*  47 */     PalettedContainerRO.Unpacker<T, PalettedContainer<T>> unpacker = PalettedContainer::unpack;
/*  48 */     return codec(elementCodec, strategy, defaultValue, unpacker);
/*     */   }
/*     */   
/*     */   public static <T> Codec<PalettedContainerRO<T>> codecRO(Codec<T> elementCodec, Strategy<T> strategy, T defaultValue) {
/*  52 */     PalettedContainerRO.Unpacker<T, PalettedContainerRO<T>> unpacker = (s, data) -> unpack(s, data).map(());
/*  53 */     return codec(elementCodec, strategy, defaultValue, unpacker);
/*     */   }
/*     */   
/*     */   private static <T, C extends PalettedContainerRO<T>> Codec<C> codec(Codec<T> elementCodec, Strategy<T> strategy, T defaultValue, PalettedContainerRO.Unpacker<T, C> unpacker) {
/*  57 */     return RecordCodecBuilder.create(i -> i.group(elementCodec
/*  58 */           .mapResult(ExtraCodecs.orElsePartial(defaultValue)).listOf().fieldOf("palette").forGetter(PalettedContainerRO.PackedData::paletteEntries), Codec.LONG_STREAM
/*  59 */           .lenientOptionalFieldOf("data").forGetter(PalettedContainerRO.PackedData::storage))
/*  60 */         .apply(i, PackedData::new)).comapFlatMap(discData -> 
/*  61 */         unpacker.read(strategy, discData), palettedContainer -> 
/*  62 */         palettedContainer.pack(strategy));
/*     */   }
/*     */   
/*     */   private PalettedContainer(Strategy<T> strategy, Configuration dataConfiguration, BitStorage storage, Palette<T> palette) {
/*     */     this.threadingDetector = new ThreadingDetector("PalettedContainer");
/*  67 */     this.strategy = strategy;
/*  68 */     this.data = new Data(dataConfiguration, storage, palette);
/*     */   }
/*     */   private PalettedContainer(PalettedContainer<T> source) {
/*     */     this.threadingDetector = new ThreadingDetector("PalettedContainer");
/*  72 */     this.strategy = source.strategy;
/*  73 */     this.data = source.data.copy();
/*     */   }
/*     */   public PalettedContainer(T initialValue, Strategy<T> strategy) {
/*     */     this.threadingDetector = new ThreadingDetector("PalettedContainer");
/*  77 */     this.strategy = strategy;
/*  78 */     this.data = createOrReuseData(null, 0);
/*     */     
/*  80 */     this.data.palette.idFor(initialValue, this);
/*     */   }
/*     */   
/*     */   private Data<T> createOrReuseData(Data<T> oldData, int targetBits) {
/*  84 */     Configuration dataConfiguration = this.strategy.getConfigurationForBitCount(targetBits);
/*  85 */     if (oldData != null && dataConfiguration.equals(oldData.configuration())) {
/*  86 */       return oldData;
/*     */     }
/*     */     
/*  89 */     ZeroBitStorage zeroBitStorage = (dataConfiguration.bitsInMemory() == 0) ? new ZeroBitStorage(this.strategy.entryCount()) : new SimpleBitStorage(dataConfiguration.bitsInMemory(), this.strategy.entryCount());
/*  90 */     Palette<T> palette = dataConfiguration.createPalette(this.strategy, List.of());
/*  91 */     return new Data(dataConfiguration, zeroBitStorage, palette);
/*     */   }
/*     */ 
/*     */   
/*     */   public int onResize(int bits, T lastAddedValue) {
/*  96 */     Data<T> oldData = this.data;
/*  97 */     Data<T> newData = createOrReuseData(oldData, bits);
/*     */     
/*  99 */     newData.copyFrom(oldData.palette, oldData.storage);
/*     */     
/* 101 */     this.data = newData;
/* 102 */     return newData.palette.idFor(lastAddedValue, PaletteResize.noResizeExpected());
/*     */   }
/*     */   
/*     */   public T getAndSet(int x, int y, int z, T value) {
/* 106 */     acquire();
/*     */     try {
/* 108 */       object = getAndSet(this.strategy.getIndex(x, y, z), value); return (T)object;
/*     */     } finally {
/* 110 */       release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public T getAndSetUnchecked(int x, int y, int z, T value) { return (T)getAndSet(this.strategy.getIndex(x, y, z), value); }
/*     */ 
/*     */   
/*     */   private T getAndSet(int index, T value) {
/* 123 */     int id = this.data.palette.idFor(value, this);
/* 124 */     int oldId = this.data.storage.getAndSet(index, id);
/* 125 */     return (T)this.data.palette.valueFor(oldId);
/*     */   }
/*     */   
/*     */   public void set(int x, int y, int z, T value) {
/* 129 */     acquire();
/*     */     try {
/* 131 */       set(this.strategy.getIndex(x, y, z), value);
/*     */     } finally {
/* 133 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void set(int index, T value) {
/* 138 */     int id = this.data.palette.idFor(value, this);
/*     */     
/* 140 */     this.data.storage.set(index, id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public T get(int x, int y, int z) { return (T)get(this.strategy.getIndex(x, y, z)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected T get(int index) {
/* 150 */     Data<T> data = this.data;
/* 151 */     return (T)data.palette.valueFor(data.storage.get(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public void getAll(Consumer<T> consumer) {
/* 156 */     Palette<T> palette = this.data.palette();
/* 157 */     IntArraySet intArraySet = new IntArraySet();
/* 158 */     Objects.requireNonNull(intArraySet); this.data.storage.getAll(intArraySet::add);
/* 159 */     intArraySet.forEach(state -> consumer.accept(palette.valueFor(state)));
/*     */   }
/*     */   
/*     */   public void read(FriendlyByteBuf buffer) {
/* 163 */     acquire();
/*     */     try {
/* 165 */       int newBits = buffer.readByte();
/*     */       
/* 167 */       Data<T> newData = createOrReuseData(this.data, newBits);
/* 168 */       newData.palette.read(buffer, this.strategy.globalMap());
/* 169 */       buffer.readFixedSizeLongArray(newData.storage.getRaw());
/*     */       
/* 171 */       this.data = newData;
/*     */     } finally {
/* 173 */       release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf buffer) {
/* 179 */     acquire();
/*     */     try {
/* 181 */       this.data.write(buffer, this.strategy.globalMap());
/*     */     } finally {
/* 183 */       release();
/*     */     } 
/*     */   } @VisibleForTesting
/*     */   public static <T> DataResult<PalettedContainer<T>> unpack(Strategy<T> strategy, PalettedContainerRO.PackedData<T> discData) {
/*     */     Palette<T> palette;
/*     */     SimpleBitStorage simpleBitStorage;
/* 189 */     List<T> paletteEntries = discData.paletteEntries();
/* 190 */     int entryCount = strategy.entryCount();
/*     */     
/* 192 */     Configuration storedConfiguration = strategy.getConfigurationForPaletteSize(paletteEntries.size());
/* 193 */     int bitsOnDisc = storedConfiguration.bitsInStorage();
/*     */     
/* 195 */     if (discData.bitsPerEntry() != -1 && bitsOnDisc != discData.bitsPerEntry()) {
/* 196 */       return DataResult.error(() -> "Invalid bit count, calculated " + bitsOnDisc + ", but container declared " + discData.bitsPerEntry());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 201 */     if (storedConfiguration.bitsInMemory() == 0) {
/* 202 */       palette = storedConfiguration.createPalette(strategy, paletteEntries);
/* 203 */       simpleBitStorage = new ZeroBitStorage(entryCount);
/*     */     } else {
/* 205 */       Optional<LongStream> dataOpt = discData.storage();
/* 206 */       if (dataOpt.isEmpty()) {
/* 207 */         return DataResult.error(() -> "Missing values for non-zero storage");
/*     */       }
/* 209 */       long[] data = ((LongStream)dataOpt.get()).toArray();
/*     */       try {
/* 211 */         if (storedConfiguration.alwaysRepack() || storedConfiguration.bitsInMemory() != bitsOnDisc) {
/*     */           
/* 213 */           Palette<T> oldPalette = new HashMapPalette<T>(bitsOnDisc, paletteEntries);
/* 214 */           SimpleBitStorage oldStorage = new SimpleBitStorage(bitsOnDisc, entryCount, data);
/*     */           
/* 216 */           Palette<T> newPalette = storedConfiguration.createPalette(strategy, paletteEntries);
/*     */           
/* 218 */           int[] newContents = reencodeContents(oldStorage, oldPalette, newPalette);
/*     */           
/* 220 */           palette = newPalette;
/* 221 */           simpleBitStorage = new SimpleBitStorage(storedConfiguration.bitsInMemory(), entryCount, newContents);
/*     */         } else {
/*     */           
/* 224 */           palette = storedConfiguration.createPalette(strategy, paletteEntries);
/* 225 */           simpleBitStorage = new SimpleBitStorage(storedConfiguration.bitsInMemory(), entryCount, data);
/*     */         } 
/* 227 */       } catch (net.minecraft.util.SimpleBitStorage.InitializationException exception) {
/* 228 */         return DataResult.error(() -> "Failed to read PalettedContainer: " + exception.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     return DataResult.success(new PalettedContainer(strategy, storedConfiguration, simpleBitStorage, palette));
/*     */   }
/*     */ 
/*     */   
/*     */   public PalettedContainerRO.PackedData<T> pack(Strategy<T> strategy) {
/* 237 */     acquire();
/*     */     try {
/*     */       Optional<LongStream> values;
/* 240 */       BitStorage currentStorage = this.data.storage;
/* 241 */       Palette<T> currentPalette = this.data.palette;
/*     */       
/* 243 */       HashMapPalette<T> newPalette = new HashMapPalette<T>(currentStorage.getBits());
/* 244 */       int entryCount = strategy.entryCount();
/* 245 */       int[] newContents = reencodeContents(currentStorage, currentPalette, newPalette);
/*     */       
/* 247 */       Configuration storedConfiguration = strategy.getConfigurationForPaletteSize(newPalette.getSize());
/*     */ 
/*     */       
/* 250 */       int bitsOnDisc = storedConfiguration.bitsInStorage();
/* 251 */       if (bitsOnDisc != 0) {
/* 252 */         SimpleBitStorage storage = new SimpleBitStorage(bitsOnDisc, entryCount, newContents);
/* 253 */         values = Optional.of(Arrays.stream(storage.getRaw()));
/*     */       } else {
/* 255 */         values = Optional.empty();
/*     */       } 
/* 257 */       return new PalettedContainerRO.PackedData(newPalette.getEntries(), values, bitsOnDisc);
/*     */     } finally {
/* 259 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> int[] reencodeContents(BitStorage storage, Palette<T> oldPalette, Palette<T> newPalette) {
/* 264 */     int[] buffer = new int[storage.getSize()];
/* 265 */     storage.unpack(buffer);
/*     */     
/* 267 */     PaletteResize<T> dummyResizer = PaletteResize.noResizeExpected();
/*     */     
/* 269 */     int lastReadId = -1;
/* 270 */     int lastWrittenId = -1;
/*     */     
/* 272 */     for (int index = 0; index < buffer.length; index++) {
/* 273 */       int id = buffer[index];
/* 274 */       if (id != lastReadId) {
/* 275 */         lastReadId = id;
/* 276 */         lastWrittenId = newPalette.idFor(oldPalette.valueFor(id), dummyResizer);
/*     */       } 
/* 278 */       buffer[index] = lastWrittenId;
/*     */     } 
/* 280 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public int getSerializedSize() { return this.data.getSerializedSize(this.strategy.globalMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public int bitsPerEntry() { return this.data.storage().getBits(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 295 */   public boolean maybeHas(Predicate<T> predicate) { return this.data.palette.maybeHas(predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   public PalettedContainer<T> copy() { return new PalettedContainer(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 305 */   public PalettedContainer<T> recreate() { return new PalettedContainer(this.data.palette.valueFor(0), this.strategy); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void count(CountConsumer<T> output) {
/* 315 */     if (this.data.palette.getSize() == 1) {
/* 316 */       output.accept(this.data.palette.valueFor(0), this.data.storage.getSize());
/*     */       return;
/*     */     } 
/* 319 */     Int2IntOpenHashMap counts = new Int2IntOpenHashMap();
/* 320 */     this.data.storage.getAll(state -> counts.addTo(state, 1));
/* 321 */     counts.int2IntEntrySet().forEach(entry -> output.accept(this.data.palette.valueFor(entry.getIntKey()), entry.getIntValue()));
/*     */   }
/*     */   private static final class Data<T> extends Record { private final Configuration configuration; private final BitStorage storage; private final Palette<T> palette;
/* 324 */     private Data(Configuration configuration, BitStorage storage, Palette<T> palette) { this.configuration = configuration; this.storage = storage; this.palette = palette; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #324	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 324 */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>; } public Configuration configuration() { return this.configuration; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #324	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #324	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 324 */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>; } public BitStorage storage() { return this.storage; } public Palette<T> palette() { return this.palette; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void copyFrom(Palette<T> oldPalette, BitStorage oldStorage) {
/* 331 */       PaletteResize<T> dummyResizer = PaletteResize.noResizeExpected();
/* 332 */       for (int i = 0; i < oldStorage.getSize(); i++) {
/* 333 */         T value = (T)oldPalette.valueFor(oldStorage.get(i));
/* 334 */         this.storage.set(i, this.palette.idFor(value, dummyResizer));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 339 */     public int getSerializedSize(IdMap<T> globalMap) { return 1 + this.palette.getSerializedSize(globalMap) + this.storage.getRaw().length * 8; }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/* 343 */       buffer.writeByte(this.storage.getBits());
/* 344 */       this.palette.write(buffer, globalMap);
/* 345 */       buffer.writeFixedSizeLongArray(this.storage.getRaw());
/*     */     }
/*     */ 
/*     */     
/* 349 */     public Data<T> copy() { return new Data(this.configuration, this.storage.copy(), this.palette.copy()); } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface CountConsumer<T> {
/*     */     void accept(T param1T, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\PalettedContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */