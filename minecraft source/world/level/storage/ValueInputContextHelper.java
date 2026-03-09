/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ public class ValueInputContextHelper {
/*     */   private final HolderLookup.Provider lookup;
/*     */   private final DynamicOps<Tag> ops;
/*     */   
/*     */   public ValueInputContextHelper(HolderLookup.Provider lookup, DynamicOps<Tag> ops) {
/*  18 */     this.emptyChildList = new ValueInput.ValueInputList(this)
/*     */       {
/*     */         public boolean isEmpty() {
/*  21 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  26 */         public Stream<ValueInput> stream() { return Stream.empty(); }
/*     */ 
/*     */ 
/*     */         
/*     */         public Iterator<ValueInput> iterator() {
/*  31 */           return Collections.emptyIterator();
/*     */         }
/*     */       };
/*     */     
/*  35 */     this.emptyTypedList = new ValueInput.TypedInputList<Object>(this)
/*     */       {
/*     */         public boolean isEmpty() {
/*  38 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  43 */         public Stream<Object> stream() { return Stream.empty(); }
/*     */ 
/*     */ 
/*     */         
/*     */         public Iterator<Object> iterator() {
/*  48 */           return Collections.emptyIterator();
/*     */         }
/*     */       };
/*     */     
/*  52 */     this.empty = new ValueInput()
/*     */       {
/*     */         public <T> Optional<T> read(String name, Codec<T> codec) {
/*  55 */           return Optional.empty();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  60 */         public <T> Optional<T> read(MapCodec<T> codec) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  65 */         public Optional<ValueInput> child(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  70 */         public ValueInput childOrEmpty(String name) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  75 */         public Optional<ValueInputList> childrenList(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  80 */         public ValueInputList childrenListOrEmpty(String name) { return ValueInputContextHelper.this.emptyChildList; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  85 */         public <T> Optional<TypedInputList<T>> list(String name, Codec<T> codec) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  90 */         public <T> TypedInputList<T> listOrEmpty(String name, Codec<T> codec) { return ValueInputContextHelper.this.emptyTypedList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  95 */         public boolean getBooleanOr(String name, boolean defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 100 */         public byte getByteOr(String name, byte defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 105 */         public int getShortOr(String name, short defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         public Optional<Integer> getInt(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 115 */         public int getIntOr(String name, int defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 120 */         public long getLongOr(String name, long defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 125 */         public Optional<Long> getLong(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 130 */         public float getFloatOr(String name, float defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 135 */         public double getDoubleOr(String name, double defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 140 */         public Optional<String> getString(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 145 */         public String getStringOr(String name, String defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 150 */         public HolderLookup.Provider lookup() { return ValueInputContextHelper.this.lookup; }
/*     */ 
/*     */ 
/*     */         
/*     */         public Optional<int[]> getIntArray(String name) {
/* 155 */           return Optional.empty();
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 160 */     this.lookup = lookup;
/* 161 */     this.ops = lookup.createSerializationContext(ops);
/*     */   }
/*     */   private final ValueInput.ValueInputList emptyChildList; private final ValueInput.TypedInputList<Object> emptyTypedList; private final ValueInput empty;
/*     */   
/* 165 */   public DynamicOps<Tag> ops() { return this.ops; }
/*     */ 
/*     */ 
/*     */   
/* 169 */   public HolderLookup.Provider lookup() { return this.lookup; }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public ValueInput empty() { return this.empty; }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public ValueInput.ValueInputList emptyList() { return this.emptyChildList; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public <T> ValueInput.TypedInputList<T> emptyTypedList() { return this.emptyTypedList; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\ValueInputContextHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */