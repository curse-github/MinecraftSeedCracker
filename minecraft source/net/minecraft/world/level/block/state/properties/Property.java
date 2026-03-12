/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ 
/*     */ public abstract class Property<T extends Comparable<T>> extends Object {
/*     */   private final Class<T> clazz;
/*     */   private final String name;
/*     */   private Integer hashCode;
/*     */   private final Codec<T> codec;
/*     */   private final Codec<Value<T>> valueCodec;
/*     */   
/*     */   protected Property(String name, Class<T> clazz) {
/*  18 */     this.codec = Codec.STRING.comapFlatMap(name -> 
/*  19 */         (DataResult)getValue(name).map(DataResult::success).orElseGet(()), this::getName);
/*     */ 
/*     */ 
/*     */     
/*  23 */     this.valueCodec = this.codec.xmap(this::value, Value::value);
/*     */ 
/*     */     
/*  26 */     this.clazz = clazz;
/*  27 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*  31 */   public Value<T> value(T value) { return new Value(this, value); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public Value<T> value(StateHolder<?, ?> stateHolder) { return new Value(this, stateHolder.getValue(this)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public Stream<Value<T>> getAllValues() { return getPossibleValues().stream().map(this::value); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public Codec<T> codec() { return this.codec; }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public Codec<Value<T>> valueCodec() { return this.valueCodec; }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public Class<T> getValueClass() { return this.clazz; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public String toString() { return MoreObjects.toStringHelper(this)
/*  70 */       .add("name", this.name)
/*  71 */       .add("clazz", this.clazz)
/*  72 */       .add("values", getPossibleValues())
/*  73 */       .toString(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  78 */     if (this == o) {
/*  79 */       return true;
/*     */     }
/*     */     
/*  82 */     if (o instanceof Property) { Property<?> that = (Property)o;
/*  83 */       return (this.clazz.equals(that.clazz) && this.name.equals(that.name)); }
/*     */ 
/*     */     
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  91 */     if (this.hashCode == null) {
/*  92 */       this.hashCode = Integer.valueOf(generateHashCode());
/*     */     }
/*  94 */     return this.hashCode.intValue();
/*     */   }
/*     */ 
/*     */   
/*  98 */   public int generateHashCode() { return 31 * this.clazz.hashCode() + this.name.hashCode(); }
/*     */   public abstract List<T> getPossibleValues();
/*     */   public abstract String getName(T paramT);
/*     */   public <U, S extends StateHolder<?, S>> DataResult<S> parseValue(DynamicOps<U> ops, S state, U value) {
/* 102 */     DataResult<T> parsed = this.codec.parse(ops, value);
/* 103 */     return parsed.map(v -> (StateHolder)state.setValue(this, v)).setPartial(state);
/*     */   } public abstract Optional<T> getValue(String paramString); public abstract int getInternalIndex(T paramT);
/*     */   public static final class Value<T extends Comparable<T>> extends Record { private final Property<T> property; private final T value;
/* 106 */     public Property<T> property() { return this.property; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/Property$Value;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/Property$Value;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 106 */       //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value<TT;>; } public T value() { return (T)this.value; }
/*     */ 
/*     */ 
/*     */     
/*     */     public Value(Property<T> property, T value) {
/* 111 */       if (!property.getPossibleValues().contains(value)) {
/* 112 */         throw new IllegalArgumentException("Value " + String.valueOf(value) + " does not belong to property " + String.valueOf(property));
/*     */       }
/*     */       this.property = property;
/*     */       this.value = value;
/*     */     }
/*     */     
/* 118 */     public String toString() { return this.property.getName() + "=" + this.property.getName(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\Property.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */