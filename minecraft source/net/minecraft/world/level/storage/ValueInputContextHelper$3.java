/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements ValueInput
/*     */ {
/*  55 */   public <T> Optional<T> read(String name, Codec<T> codec) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public <T> Optional<T> read(MapCodec<T> codec) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public Optional<ValueInput> child(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public ValueInput childOrEmpty(String name) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public Optional<ValueInput.ValueInputList> childrenList(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public ValueInput.ValueInputList childrenListOrEmpty(String name) { return ValueInputContextHelper.this.emptyChildList; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public <T> Optional<ValueInput.TypedInputList<T>> list(String name, Codec<T> codec) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public <T> ValueInput.TypedInputList<T> listOrEmpty(String name, Codec<T> codec) { return ValueInputContextHelper.this.emptyTypedList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public boolean getBooleanOr(String name, boolean defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public byte getByteOr(String name, byte defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public int getShortOr(String name, short defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public Optional<Integer> getInt(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public int getIntOr(String name, int defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public long getLongOr(String name, long defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public Optional<Long> getLong(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public float getFloatOr(String name, float defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public double getDoubleOr(String name, double defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public Optional<String> getString(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public String getStringOr(String name, String defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public HolderLookup.Provider lookup() { return ValueInputContextHelper.this.lookup; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public Optional<int[]> getIntArray(String name) { return Optional.empty(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\ValueInputContextHelper$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */