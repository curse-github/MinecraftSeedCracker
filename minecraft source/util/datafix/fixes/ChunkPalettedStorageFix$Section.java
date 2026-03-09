/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Section
/*     */ {
/*     */   private final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> palette;
/*     */   private final List<Dynamic<?>> listTag;
/*     */   private final Dynamic<?> section;
/*     */   private final boolean hasData;
/*     */   private final Int2ObjectMap<IntList> toFix;
/*     */   private final IntList update;
/*     */   public final int y;
/*     */   private final Set<Dynamic<?>> seen;
/*     */   private final int[] buffer;
/*     */   
/*     */   public Section(Dynamic<?> section) {
/* 377 */     this.palette = CrudeIncrementalIntIdentityHashBiMap.create(32);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     this.toFix = new Int2ObjectLinkedOpenHashMap();
/*     */     
/* 384 */     this.update = new IntArrayList();
/*     */     
/* 386 */     this.seen = Sets.newIdentityHashSet();
/* 387 */     this.buffer = new int[4096];
/*     */ 
/*     */     
/* 390 */     this.listTag = Lists.newArrayList();
/* 391 */     this.section = section;
/* 392 */     this.y = section.get("Y").asInt(0);
/* 393 */     this.hasData = section.get("Blocks").result().isPresent();
/*     */   }
/*     */   
/*     */   public Dynamic<?> getBlock(int pos) {
/* 397 */     if (pos < 0 || pos > 4095) {
/* 398 */       return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */     }
/*     */     
/* 401 */     Dynamic<?> tag = (Dynamic)this.palette.byId(this.buffer[pos]);
/* 402 */     return (tag == null) ? ChunkPalettedStorageFix.MappingConstants.AIR : tag;
/*     */   }
/*     */   
/*     */   public void setBlock(int idx, Dynamic<?> blockState) {
/* 406 */     if (this.seen.add(blockState)) {
/* 407 */       this.listTag.add("%%FILTER_ME%%".equals(ChunkPalettedStorageFix.getName(blockState)) ? ChunkPalettedStorageFix.MappingConstants.AIR : blockState);
/*     */     }
/* 409 */     this.buffer[idx] = ChunkPalettedStorageFix.idFor(this.palette, blockState);
/*     */   }
/*     */   
/*     */   public int upgrade(int sides) {
/* 413 */     if (!this.hasData) {
/* 414 */       return sides;
/*     */     }
/* 416 */     ByteBuffer blocks = (ByteBuffer)this.section.get("Blocks").asByteBufferOpt().result().get();
/* 417 */     ChunkPalettedStorageFix.DataLayer data = (ChunkPalettedStorageFix.DataLayer)this.section.get("Data").asByteBufferOpt().map(buffer -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(buffer))).result().orElseGet(DataLayer::new);
/* 418 */     ChunkPalettedStorageFix.DataLayer addBlocks = (ChunkPalettedStorageFix.DataLayer)this.section.get("Add").asByteBufferOpt().map(buffer -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(buffer))).result().orElseGet(DataLayer::new);
/*     */     
/* 420 */     this.seen.add(ChunkPalettedStorageFix.MappingConstants.AIR);
/* 421 */     ChunkPalettedStorageFix.idFor(this.palette, ChunkPalettedStorageFix.MappingConstants.AIR);
/* 422 */     this.listTag.add(ChunkPalettedStorageFix.MappingConstants.AIR);
/*     */     
/* 424 */     for (int idx = 0; idx < 4096; idx++) {
/* 425 */       int xx = idx & 0xF;
/* 426 */       int yy = idx >> 8 & 0xF;
/* 427 */       int zz = idx >> 4 & 0xF;
/* 428 */       int id = addBlocks.get(xx, yy, zz) << 12 | (blocks.get(idx) & 0xFF) << 4 | data.get(xx, yy, zz);
/*     */       
/* 430 */       if (ChunkPalettedStorageFix.MappingConstants.FIX.get(id >> 4)) {
/* 431 */         addFix(id >> 4, idx);
/*     */       }
/* 433 */       if (ChunkPalettedStorageFix.MappingConstants.VIRTUAL.get(id >> 4)) {
/*     */         
/* 435 */         int s = ChunkPalettedStorageFix.getSideMask((xx == 0), (xx == 15), (zz == 0), (zz == 15));
/* 436 */         if (s == 0) {
/*     */           
/* 438 */           this.update.add(idx);
/*     */         } else {
/* 440 */           sides |= s;
/*     */         } 
/*     */       } 
/*     */       
/* 444 */       setBlock(idx, BlockStateData.getTag(id));
/*     */     } 
/*     */     
/* 447 */     return sides;
/*     */   }
/*     */   
/*     */   private void addFix(int id, int position) {
/* 451 */     IntArrayList intArrayList = (IntList)this.toFix.get(id);
/* 452 */     if (intArrayList == null) {
/* 453 */       intArrayList = new IntArrayList();
/* 454 */       this.toFix.put(id, intArrayList);
/*     */     } 
/* 456 */     intArrayList.add(position);
/*     */   }
/*     */   
/*     */   public Dynamic<?> write() {
/* 460 */     section = this.section;
/* 461 */     if (!this.hasData) {
/* 462 */       return section;
/*     */     }
/* 464 */     section = section.set("Palette", section.createList(this.listTag.stream()));
/*     */     
/* 466 */     int size = Math.max(4, DataFixUtils.ceillog2(this.seen.size()));
/* 467 */     PackedBitStorage storage = new PackedBitStorage(size, 4096);
/* 468 */     for (int j = 0; j < this.buffer.length; j++) {
/* 469 */       storage.set(j, this.buffer[j]);
/*     */     }
/*     */     
/* 472 */     section = section.set("BlockStates", section.createLongList(Arrays.stream(storage.getRaw())));
/*     */     
/* 474 */     section = section.remove("Blocks");
/* 475 */     section = section.remove("Data");
/* 476 */     return section.remove("Add");
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$Section.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */