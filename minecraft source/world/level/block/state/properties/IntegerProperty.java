/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public final class IntegerProperty
/*    */   extends Property<Integer> {
/*    */   private final IntImmutableList values;
/*    */   private final int min;
/*    */   private final int max;
/*    */   
/*    */   private IntegerProperty(String name, int min, int max) {
/* 15 */     super(name, Integer.class);
/* 16 */     if (min < 0) {
/* 17 */       throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
/*    */     }
/* 19 */     if (max <= min) {
/* 20 */       throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
/*    */     }
/* 22 */     this.min = min;
/* 23 */     this.max = max;
/* 24 */     this.values = IntImmutableList.toList(IntStream.range(min, max + 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public List<Integer> getPossibleValues() { return this.values; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 34 */     if (this == o) {
/* 35 */       return true;
/*    */     }
/*    */     
/* 38 */     if (o instanceof IntegerProperty) { IntegerProperty that = (IntegerProperty)o; if (super.equals(o)) {
/* 39 */         return this.values.equals(that.values);
/*    */       } }
/*    */     
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public int generateHashCode() { return 31 * super.generateHashCode() + this.values.hashCode(); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static IntegerProperty create(String name, int min, int max) { return new IntegerProperty(name, min, max); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Integer> getValue(String name) {
/*    */     try {
/* 57 */       int value = Integer.parseInt(name);
/*    */       
/* 59 */       return (value >= this.min && value <= this.max) ? Optional.of(Integer.valueOf(value)) : Optional.empty();
/* 60 */     } catch (NumberFormatException ignored) {
/* 61 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public String getName(Integer value) { return value.toString(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getInternalIndex(Integer value) {
/* 72 */     if (value.intValue() <= this.max) {
/* 73 */       return value.intValue() - this.min;
/*    */     }
/* 75 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\IntegerProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */