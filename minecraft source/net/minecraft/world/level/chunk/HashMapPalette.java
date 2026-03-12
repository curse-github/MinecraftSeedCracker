/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ 
/*     */ public class HashMapPalette<T> extends Object implements Palette<T> {
/*     */   private final CrudeIncrementalIntIdentityHashBiMap<T> values;
/*     */   private final int bits;
/*     */   
/*     */   public HashMapPalette(int bits, List<T> values) {
/*  17 */     this(bits);
/*  18 */     Objects.requireNonNull(this.values); values.forEach(this.values::add);
/*     */   }
/*     */ 
/*     */   
/*  22 */   public HashMapPalette(int bits) { this(bits, CrudeIncrementalIntIdentityHashBiMap.create(1 << bits)); }
/*     */ 
/*     */   
/*     */   private HashMapPalette(int bits, CrudeIncrementalIntIdentityHashBiMap<T> values) {
/*  26 */     this.bits = bits;
/*  27 */     this.values = values;
/*     */   }
/*     */ 
/*     */   
/*  31 */   public static <A> Palette<A> create(int bits, List<A> paletteEntries) { return new HashMapPalette(bits, paletteEntries); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int idFor(T value, PaletteResize<T> resizeHandler) {
/*  36 */     int id = this.values.getId(value);
/*  37 */     if (id == -1) {
/*  38 */       id = this.values.add(value);
/*     */       
/*  40 */       if (id >= 1 << this.bits) {
/*  41 */         id = resizeHandler.onResize(this.bits + 1, value);
/*     */       }
/*     */     } 
/*  44 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean maybeHas(Predicate<T> predicate) {
/*  49 */     for (int i = 0; i < getSize(); i++) {
/*  50 */       if (predicate.test(this.values.byId(i))) {
/*  51 */         return true;
/*     */       }
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueFor(int index) {
/*  59 */     T value = (T)this.values.byId(index);
/*  60 */     if (value == null) {
/*  61 */       throw new MissingPaletteEntryException(index);
/*     */     }
/*  63 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/*  68 */     this.values.clear();
/*  69 */     int size = buffer.readVarInt();
/*  70 */     for (int i = 0; i < size; i++) {
/*  71 */       this.values.add(globalMap.byIdOrThrow(buffer.readVarInt()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/*  77 */     int size = getSize();
/*  78 */     buffer.writeVarInt(size);
/*     */     
/*  80 */     for (int i = 0; i < size; i++) {
/*  81 */       buffer.writeVarInt(globalMap.getId(this.values.byId(i)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSerializedSize(IdMap<T> globalMap) {
/*  87 */     int size = VarInt.getByteSize(getSize());
/*     */     
/*  89 */     for (int i = 0; i < getSize(); i++) {
/*  90 */       size += VarInt.getByteSize(globalMap.getId(this.values.byId(i)));
/*     */     }
/*     */     
/*  93 */     return size;
/*     */   }
/*     */   
/*     */   public List<T> getEntries() {
/*  97 */     ArrayList<T> list = new ArrayList<T>();
/*  98 */     Objects.requireNonNull(list); this.values.iterator().forEachRemaining(list::add);
/*  99 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public int getSize() { return this.values.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public Palette<T> copy() { return new HashMapPalette(this.bits, this.values.copy()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\HashMapPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */