/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public class LinearPalette<T>
/*     */   extends Object
/*     */   implements Palette<T> {
/*     */   private final T[] values;
/*     */   private final int bits;
/*     */   private int size;
/*     */   
/*     */   private LinearPalette(int bits, List<T> paletteEntries) {
/*  18 */     this.values = new Object[1 << bits];
/*  19 */     this.bits = bits;
/*  20 */     Validate.isTrue((paletteEntries.size() <= this.values.length), "Can't initialize LinearPalette of size %d with %d entries", new Object[] { Integer.valueOf(this.values.length), Integer.valueOf(paletteEntries.size()) });
/*  21 */     for (int i = 0; i < paletteEntries.size(); i++) {
/*  22 */       this.values[i] = paletteEntries.get(i);
/*     */     }
/*  24 */     this.size = paletteEntries.size();
/*     */   }
/*     */   
/*     */   private LinearPalette(T[] values, int bits, int size) {
/*  28 */     this.values = values;
/*  29 */     this.bits = bits;
/*  30 */     this.size = size;
/*     */   }
/*     */ 
/*     */   
/*  34 */   public static <A> Palette<A> create(int bits, List<A> paletteEntries) { return new LinearPalette(bits, paletteEntries); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int idFor(T value, PaletteResize<T> resizeHandler) {
/*  39 */     for (int i = 0; i < this.size; i++) {
/*  40 */       if (this.values[i] == value) {
/*  41 */         return i;
/*     */       }
/*     */     } 
/*     */     
/*  45 */     int index = this.size;
/*  46 */     if (index < this.values.length) {
/*  47 */       this.values[index] = value;
/*  48 */       this.size++;
/*  49 */       return index;
/*     */     } 
/*     */     
/*  52 */     return resizeHandler.onResize(this.bits + 1, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean maybeHas(Predicate<T> predicate) {
/*  57 */     for (int i = 0; i < this.size; i++) {
/*  58 */       if (predicate.test(this.values[i])) {
/*  59 */         return true;
/*     */       }
/*     */     } 
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueFor(int index) {
/*  67 */     if (index >= 0 && index < this.size) {
/*  68 */       return (T)this.values[index];
/*     */     }
/*  70 */     throw new MissingPaletteEntryException(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/*  75 */     this.size = buffer.readVarInt();
/*  76 */     for (int i = 0; i < this.size; i++) {
/*  77 */       this.values[i] = globalMap.byIdOrThrow(buffer.readVarInt());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/*  83 */     buffer.writeVarInt(this.size);
/*  84 */     for (int i = 0; i < this.size; i++) {
/*  85 */       buffer.writeVarInt(globalMap.getId(this.values[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSerializedSize(IdMap<T> globalMap) {
/*  91 */     int result = VarInt.getByteSize(getSize());
/*     */     
/*  93 */     for (int i = 0; i < getSize(); i++) {
/*  94 */       result += VarInt.getByteSize(globalMap.getId(this.values[i]));
/*     */     }
/*     */     
/*  97 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public int getSize() { return this.size; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public Palette<T> copy() { return new LinearPalette((Object[])this.values.clone(), this.bits, this.size); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\LinearPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */