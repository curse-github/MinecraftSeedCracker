/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.chars.CharArraySet;
/*     */ import it.unimi.dsi.fastutil.chars.CharSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class ShapedRecipePattern {
/*     */   private static final int MAX_SIZE = 3;
/*     */   public static final char EMPTY_SLOT = ' ';
/*  27 */   public static final MapCodec<ShapedRecipePattern> MAP_CODEC = Data.MAP_CODEC.flatXmap(ShapedRecipePattern::unpack, pattern -> 
/*     */       
/*  29 */       (DataResult)pattern.data.map(DataResult::success).orElseGet(()));
/*     */ 
/*     */   
/*  32 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipePattern> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, e -> 
/*  33 */       Integer.valueOf(e.width), ByteBufCodecs.VAR_INT, e -> 
/*  34 */       Integer.valueOf(e.height), Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC
/*  35 */       .apply(ByteBufCodecs.list()), e -> e.ingredients, ShapedRecipePattern::createFromNetwork);
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final List<Optional<Ingredient>> ingredients;
/*     */   
/*     */   private final Optional<Data> data;
/*     */   private final int ingredientCount;
/*     */   private final boolean symmetrical;
/*     */   
/*     */   public ShapedRecipePattern(int width, int height, List<Optional<Ingredient>> ingredients, Optional<Data> data) {
/*  48 */     this.width = width;
/*  49 */     this.height = height;
/*  50 */     this.ingredients = ingredients;
/*  51 */     this.data = data;
/*  52 */     this.ingredientCount = (int)ingredients.stream().flatMap(Optional::stream).count();
/*  53 */     this.symmetrical = Util.isSymmetrical(width, height, ingredients);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  58 */   private static ShapedRecipePattern createFromNetwork(Integer width, Integer height, List<Optional<Ingredient>> ingredients) { return new ShapedRecipePattern(width.intValue(), height.intValue(), ingredients, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static ShapedRecipePattern of(Map<Character, Ingredient> key, String... pattern) { return of(key, List.of(pattern)); }
/*     */ 
/*     */   
/*     */   public static ShapedRecipePattern of(Map<Character, Ingredient> key, List<String> pattern) {
/*  66 */     Data data = new Data(key, pattern);
/*  67 */     return (ShapedRecipePattern)unpack(data).getOrThrow();
/*     */   }
/*     */   
/*     */   private static DataResult<ShapedRecipePattern> unpack(Data data) {
/*  71 */     String[] shrunkPattern = shrink(data.pattern);
/*  72 */     int width = shrunkPattern[0].length();
/*  73 */     int height = shrunkPattern.length;
/*  74 */     List<Optional<Ingredient>> ingredients = new ArrayList<Optional<Ingredient>>(width * height);
/*  75 */     CharArraySet charArraySet = new CharArraySet(data.key.keySet());
/*     */     
/*  77 */     for (String line : shrunkPattern) {
/*  78 */       for (int x = 0; x < line.length(); x++) {
/*  79 */         Optional<Ingredient> ingredient; char symbol = line.charAt(x);
/*     */         
/*  81 */         if (symbol == ' ') {
/*  82 */           ingredient = Optional.empty();
/*     */         } else {
/*  84 */           Ingredient ingredientForSymbol = (Ingredient)data.key.get(Character.valueOf(symbol));
/*  85 */           if (ingredientForSymbol == null) {
/*  86 */             return DataResult.error(() -> "Pattern references symbol '" + symbol + "' but it's not defined in the key");
/*     */           }
/*  88 */           ingredient = Optional.of(ingredientForSymbol);
/*     */         } 
/*  90 */         charArraySet.remove(symbol);
/*  91 */         ingredients.add(ingredient);
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (!charArraySet.isEmpty()) {
/*  96 */       return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + String.valueOf(unusedSymbols));
/*     */     }
/*     */     
/*  99 */     return DataResult.success(new ShapedRecipePattern(width, height, ingredients, Optional.of(data)));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String[] shrink(List<String> pattern) {
/* 104 */     int left = Integer.MAX_VALUE;
/* 105 */     int right = 0;
/* 106 */     int top = 0;
/* 107 */     int bottom = 0;
/*     */     
/* 109 */     for (int i = 0; i < pattern.size(); i++) {
/* 110 */       String line = (String)pattern.get(i);
/*     */       
/* 112 */       left = Math.min(left, firstNonEmpty(line));
/* 113 */       int lastNonSpace = lastNonEmpty(line);
/* 114 */       right = Math.max(right, lastNonSpace);
/*     */ 
/*     */       
/* 117 */       if (lastNonSpace < 0) {
/* 118 */         if (top == i) {
/* 119 */           top++;
/*     */         }
/* 121 */         bottom++;
/*     */       } else {
/* 123 */         bottom = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     if (pattern.size() == bottom) {
/* 128 */       return new String[0];
/*     */     }
/*     */     
/* 131 */     String[] result = new String[pattern.size() - bottom - top];
/* 132 */     for (int line = 0; line < result.length; line++) {
/* 133 */       result[line] = ((String)pattern.get(line + top)).substring(left, right + 1);
/*     */     }
/*     */     
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   private static int firstNonEmpty(String line) {
/* 140 */     int index = 0;
/* 141 */     while (index < line.length() && line.charAt(index) == ' ') {
/* 142 */       index++;
/*     */     }
/* 144 */     return index;
/*     */   }
/*     */   
/*     */   private static int lastNonEmpty(String line) {
/* 148 */     int index = line.length() - 1;
/* 149 */     while (index >= 0 && line.charAt(index) == ' ') {
/* 150 */       index--;
/*     */     }
/* 152 */     return index;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input) {
/* 156 */     if (input.ingredientCount() != this.ingredientCount) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (input.width() == this.width && input.height() == this.height) {
/* 160 */       if (!this.symmetrical && matches(input, true)) {
/* 161 */         return true;
/*     */       }
/* 163 */       if (matches(input, false)) {
/* 164 */         return true;
/*     */       }
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matches(CraftingInput input, boolean xFlip) {
/* 171 */     for (int y = 0; y < this.height; y++) {
/* 172 */       for (int x = 0; x < this.width; x++) {
/*     */         Optional<Ingredient> expected;
/* 174 */         if (xFlip) {
/* 175 */           expected = (Optional)this.ingredients.get(this.width - x - 1 + y * this.width);
/*     */         } else {
/* 177 */           expected = (Optional)this.ingredients.get(x + y * this.width);
/*     */         } 
/* 179 */         ItemStack actual = input.getItem(x, y);
/* 180 */         if (!Ingredient.testOptionalIngredient(expected, actual)) {
/* 181 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 189 */   public int width() { return this.width; }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public int height() { return this.height; }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public List<Optional<Ingredient>> ingredients() { return this.ingredients; }
/*     */   public static final class Data extends Record { private final Map<Character, Ingredient> key; private final List<String> pattern;
/*     */     
/* 200 */     public Data(Map<Character, Ingredient> key, List<String> pattern) { this.key = key; this.pattern = pattern; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #200	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 200 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data; } public Map<Character, Ingredient> key() { return this.key; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #200	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #200	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/ShapedRecipePattern$Data;
/* 200 */       //   0	8	1	o	Ljava/lang/Object; } public List<String> pattern() { return this.pattern; }
/* 201 */     private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
/* 202 */           if (strings.size() > 3)
/* 203 */             return DataResult.error(()); 
/* 204 */           if (strings.isEmpty()) {
/* 205 */             return DataResult.error(());
/*     */           }
/* 207 */           int firstLength = ((String)strings.getFirst()).length();
/* 208 */           for (String line : strings) {
/* 209 */             if (line.length() > 3)
/* 210 */               return DataResult.error(()); 
/* 211 */             if (firstLength != line.length()) {
/* 212 */               return DataResult.error(());
/*     */             }
/*     */           } 
/* 215 */           return DataResult.success(strings);
/* 216 */         }Function.identity());
/*     */     
/* 218 */     private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> {
/* 219 */           if (symbol.length() != 1) {
/* 220 */             return DataResult.error(());
/*     */           }
/* 222 */           if (" ".equals(symbol)) {
/* 223 */             return DataResult.error(());
/*     */           }
/* 225 */           return DataResult.success(Character.valueOf(symbol.charAt(0)));
/*     */         }String::valueOf);
/*     */     
/* 228 */     public static final MapCodec<Data> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 229 */           ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(()), PATTERN_CODEC
/* 230 */           .fieldOf("pattern").forGetter(()))
/* 231 */         .apply(i, Data::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ShapedRecipePattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */