/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.chars.CharSet;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class BlockPatternBuilder {
/*    */   private final List<String[]> pattern;
/*    */   private final Map<Character, Predicate<BlockInWorld>> lookup;
/*    */   
/*    */   private BlockPatternBuilder() {
/* 17 */     this.pattern = Lists.newArrayList();
/* 18 */     this.lookup = Maps.newHashMap();
/*    */ 
/*    */     
/* 21 */     this.unknownCharacters = new CharOpenHashSet();
/*    */ 
/*    */     
/* 24 */     this.lookup.put(Character.valueOf(' '), blockInWorld -> true);
/*    */   }
/*    */   private int height; private int width; private final CharSet unknownCharacters;
/*    */   public BlockPatternBuilder aisle(String... aisle) {
/* 28 */     if (ArrayUtils.isEmpty(aisle) || StringUtils.isEmpty(aisle[0])) {
/* 29 */       throw new IllegalArgumentException("Empty pattern for aisle");
/*    */     }
/*    */     
/* 32 */     if (this.pattern.isEmpty()) {
/* 33 */       this.height = aisle.length;
/* 34 */       this.width = aisle[0].length();
/*    */     } 
/*    */     
/* 37 */     if (aisle.length != this.height) {
/* 38 */       throw new IllegalArgumentException("Expected aisle with height of " + this.height + ", but was given one with a height of " + aisle.length + ")");
/*    */     }
/*    */     
/* 41 */     for (String row : aisle) {
/* 42 */       if (row.length() != this.width) {
/* 43 */         throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.width + ", found one with " + row.length() + ")");
/*    */       }
/* 45 */       for (char c : row.toCharArray()) {
/* 46 */         if (!this.lookup.containsKey(Character.valueOf(c))) {
/* 47 */           this.unknownCharacters.add(c);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     this.pattern.add(aisle);
/*    */     
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 58 */   public static BlockPatternBuilder start() { return new BlockPatternBuilder(); }
/*    */ 
/*    */   
/*    */   public BlockPatternBuilder where(char character, Predicate<BlockInWorld> predicate) {
/* 62 */     this.lookup.put(Character.valueOf(character), predicate);
/* 63 */     this.unknownCharacters.remove(character);
/*    */     
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 69 */   public BlockPattern build() { return new BlockPattern(createPattern()); }
/*    */ 
/*    */ 
/*    */   
/*    */   private Predicate<BlockInWorld>[][][] createPattern() {
/* 74 */     if (!this.unknownCharacters.isEmpty()) {
/* 75 */       throw new IllegalStateException("Predicates for character(s) " + String.valueOf(this.unknownCharacters) + " are missing");
/*    */     }
/*    */     
/* 78 */     Predicate[][][] result = (Predicate[][][])Array.newInstance(Predicate.class, new int[] { this.pattern.size(), this.height, this.width });
/*    */     
/* 80 */     for (int aisle = 0; aisle < this.pattern.size(); aisle++) {
/* 81 */       for (int row = 0; row < this.height; row++) {
/* 82 */         for (int col = 0; col < this.width; col++) {
/* 83 */           result[aisle][row][col] = (Predicate)this.lookup.get(Character.valueOf((String[])this.pattern.get(aisle)[row].charAt(col)));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockPatternBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */