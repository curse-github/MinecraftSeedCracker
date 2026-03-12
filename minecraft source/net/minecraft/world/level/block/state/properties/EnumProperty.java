/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class EnumProperty<T extends Enum<T> & StringRepresentable> extends Property<T> {
/*    */   private final List<T> values;
/*    */   private final Map<String, T> names;
/*    */   private final int[] ordinalToIndex;
/*    */   
/*    */   private EnumProperty(String name, Class<T> clazz, List<T> values) {
/* 19 */     super(name, clazz);
/*    */     
/* 21 */     if (values.isEmpty()) {
/* 22 */       throw new IllegalArgumentException("Trying to make empty EnumProperty '" + name + "'");
/*    */     }
/*    */     
/* 25 */     this.values = List.copyOf(values);
/* 26 */     T[] allEnumValues = (T[])(Enum[])clazz.getEnumConstants();
/* 27 */     this.ordinalToIndex = new int[allEnumValues.length];
/* 28 */     for (T value : allEnumValues) {
/* 29 */       this.ordinalToIndex[value.ordinal()] = values.indexOf(value);
/*    */     }
/*    */     
/* 32 */     ImmutableMap.Builder<String, T> names = ImmutableMap.builder();
/* 33 */     for (Iterator iterator = values.iterator(); iterator.hasNext(); ) { T value = (T)(Enum)iterator.next();
/* 34 */       String key = ((StringRepresentable)value).getSerializedName();
/* 35 */       names.put(key, value); }
/*    */     
/* 37 */     this.names = names.buildOrThrow();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public List<T> getPossibleValues() { return this.values; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Optional<T> getValue(String name) { return Optional.ofNullable((Enum)this.names.get(name)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public String getName(T value) { return ((StringRepresentable)value).getSerializedName(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int getInternalIndex(T value) { return this.ordinalToIndex[value.ordinal()]; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 62 */     if (this == o) {
/* 63 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 67 */     if (o instanceof EnumProperty) { EnumProperty<?> that = (EnumProperty)o; if (super.equals(o)) {
/* 68 */         return this.values.equals(that.values);
/*    */       } }
/*    */     
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int generateHashCode() {
/* 76 */     result = super.generateHashCode();
/* 77 */     return 31 * result + this.values.hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz) { return create(name, clazz, t -> true); }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz, Predicate<T> filter) { return create(name, clazz, (List)Arrays.stream((Enum[])clazz.getEnumConstants()).filter(filter).collect(Collectors.toList())); }
/*    */ 
/*    */ 
/*    */   
/*    */   @SafeVarargs
/* 91 */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz, T... values) { return create(name, clazz, List.of(values)); }
/*    */ 
/*    */ 
/*    */   
/* 95 */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz, List<T> values) { return new EnumProperty(name, clazz, values); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\EnumProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */