/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterable;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ 
/*     */ public class StackedContents<T>
/*     */   extends Object {
/*  17 */   public final Reference2IntOpenHashMap<T> amounts = new Reference2IntOpenHashMap();
/*     */ 
/*     */   
/*  20 */   private boolean hasAtLeast(T item, int count) { return (this.amounts.getInt(item) >= count); }
/*     */ 
/*     */   
/*     */   private void take(T item, int amount) {
/*  24 */     int previous = this.amounts.addTo(item, -amount);
/*  25 */     if (previous < amount) {
/*  26 */       throw new IllegalStateException("Took " + amount + " items, but only had " + previous);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  31 */   private void put(T item, int count) { this.amounts.addTo(item, count); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public boolean tryPick(List<? extends IngredientInfo<T>> ingredients, int amount, Output<T> output) { return (new RecipePicker(ingredients)).tryPick(amount, output); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public int tryPickAll(List<? extends IngredientInfo<T>> ingredients, int maxSize, Output<T> output) { return (new RecipePicker(ingredients)).tryPickAll(maxSize, output); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public void clear() { this.amounts.clear(); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public void account(T item, int count) { put(item, count); }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<T> getUniqueAvailableIngredientItems(Iterable<? extends IngredientInfo<T>> ingredients) {
/*  52 */     List<T> result = new ArrayList<T>();
/*  53 */     for (ObjectIterator objectIterator = Reference2IntMaps.fastIterable(this.amounts).iterator(); objectIterator.hasNext(); ) { Reference2IntMap.Entry<T> availableItem = (Reference2IntMap.Entry)objectIterator.next();
/*  54 */       if (availableItem.getIntValue() > 0 && anyIngredientMatches(ingredients, availableItem.getKey())) {
/*  55 */         result.add(availableItem.getKey());
/*     */       } }
/*     */     
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   private static <T> boolean anyIngredientMatches(Iterable<? extends IngredientInfo<T>> ingredients, T item) {
/*  62 */     for (IngredientInfo<T> ingredient : ingredients) {
/*  63 */       if (ingredient.acceptsItem(item)) {
/*  64 */         return true;
/*     */       }
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getResultUpperBound(List<? extends IngredientInfo<T>> ingredients) {
/*  75 */     int min = Integer.MAX_VALUE;
/*     */     
/*  77 */     ObjectIterable<Reference2IntMap.Entry<T>> availableItems = Reference2IntMaps.fastIterable(this.amounts);
/*  78 */     label20: for (IngredientInfo<T> ingredient : ingredients) {
/*  79 */       int max = 0;
/*     */       
/*  81 */       for (ObjectIterator objectIterator = availableItems.iterator(); objectIterator.hasNext(); ) { Reference2IntMap.Entry<T> entry = (Reference2IntMap.Entry)objectIterator.next();
/*  82 */         int itemCount = entry.getIntValue();
/*  83 */         if (itemCount <= max) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  88 */         if (ingredient.acceptsItem(entry.getKey())) {
/*  89 */           max = itemCount;
/*     */         }
/*     */         
/*  92 */         if (max >= min) {
/*     */           continue label20;
/*     */         } }
/*     */ 
/*     */ 
/*     */       
/*  98 */       min = max;
/*  99 */       if (min == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 103 */     return min;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class RecipePicker
/*     */   {
/*     */     private final List<? extends StackedContents.IngredientInfo<T>> ingredients;
/*     */ 
/*     */     
/*     */     private final int ingredientCount;
/*     */ 
/*     */     
/*     */     private final List<T> items;
/*     */ 
/*     */     
/*     */     private final int itemCount;
/*     */     
/*     */     private final BitSet data;
/*     */     
/*     */     private final IntList path;
/*     */ 
/*     */     
/*     */     public RecipePicker(List<? extends StackedContents.IngredientInfo<T>> ingredients) {
/* 127 */       this.path = new IntArrayList();
/*     */ 
/*     */       
/* 130 */       this.ingredients = ingredients;
/*     */       
/* 132 */       this.ingredientCount = ingredients.size();
/* 133 */       this.items = this$0.getUniqueAvailableIngredientItems(ingredients);
/* 134 */       this.itemCount = this.items.size();
/*     */       
/* 136 */       this.data = new BitSet(visitedIngredientCount() + visitedItemCount() + satisfiedCount() + connectionCount() + residualCount());
/* 137 */       setInitialConnections();
/*     */     }
/*     */     
/*     */     private void setInitialConnections() {
/* 141 */       for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 142 */         StackedContents.IngredientInfo<T> ingredientInfo = (StackedContents.IngredientInfo)this.ingredients.get(ingredient);
/* 143 */         for (int item = 0; item < this.itemCount; item++) {
/* 144 */           if (ingredientInfo.acceptsItem(this.items.get(item))) {
/* 145 */             setConnection(item, ingredient);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean tryPick(int capacity, StackedContents.Output<T> output) {
/* 159 */       if (capacity <= 0) {
/* 160 */         return true;
/*     */       }
/*     */       
/* 163 */       int satisfiedIngredientCount = 0;
/*     */       while (true) {
/* 165 */         IntList path = tryAssigningNewItem(capacity);
/* 166 */         if (path == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 173 */         int assignedItem = path.getInt(0);
/* 174 */         StackedContents.this.take(this.items.get(assignedItem), capacity);
/*     */ 
/*     */         
/* 177 */         int satisfiedIngredient = path.size() - 1;
/* 178 */         setSatisfied(path.getInt(satisfiedIngredient));
/* 179 */         satisfiedIngredientCount++;
/*     */ 
/*     */ 
/*     */         
/* 183 */         for (int i = 0; i < path.size() - 1; i++) {
/* 184 */           if (isPathIndexItem(i)) {
/* 185 */             int item = path.getInt(i);
/* 186 */             int ingredient = path.getInt(i + 1);
/* 187 */             assign(item, ingredient);
/*     */           } else {
/* 189 */             int item = path.getInt(i + 1);
/* 190 */             int ingredient = path.getInt(i);
/* 191 */             unassign(item, ingredient);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 196 */       boolean isValidAssignment = (satisfiedIngredientCount == this.ingredientCount);
/*     */       
/* 198 */       boolean hasOutput = (isValidAssignment && output != null);
/*     */ 
/*     */       
/* 201 */       clearAllVisited();
/* 202 */       clearSatisfied();
/*     */ 
/*     */       
/* 205 */       for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 206 */         for (int item = 0; item < this.itemCount; item++) {
/* 207 */           if (isAssigned(item, ingredient)) {
/* 208 */             unassign(item, ingredient);
/* 209 */             StackedContents.this.put(this.items.get(item), capacity);
/*     */             
/* 211 */             if (hasOutput) {
/* 212 */               output.accept(this.items.get(item));
/*     */             }
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 221 */       assert this.data.get(residualOffset(), residualOffset() + residualCount()).isEmpty();
/*     */       
/* 223 */       return isValidAssignment;
/*     */     }
/*     */ 
/*     */     
/* 227 */     private static boolean isPathIndexItem(int index) { return ((index & true) == 0); }
/*     */ 
/*     */     
/*     */     private IntList tryAssigningNewItem(int capacity) {
/* 231 */       clearAllVisited();
/*     */       
/* 233 */       for (int item = 0; item < this.itemCount; item++) {
/* 234 */         if (StackedContents.this.hasAtLeast(this.items.get(item), capacity)) {
/* 235 */           IntList path = findNewItemAssignmentPath(item);
/* 236 */           if (path != null) {
/* 237 */             return path;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 242 */       return null;
/*     */     }
/*     */     
/*     */     private IntList findNewItemAssignmentPath(int startingItem) {
/* 246 */       this.path.clear();
/* 247 */       visitItem(startingItem);
/* 248 */       this.path.add(startingItem);
/*     */ 
/*     */       
/* 251 */       while (!this.path.isEmpty()) {
/* 252 */         int pathLength = this.path.size();
/* 253 */         if (isPathIndexItem(pathLength - 1)) {
/* 254 */           int itemToAssign = this.path.getInt(pathLength - 1);
/*     */ 
/*     */           
/* 257 */           for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
/* 258 */             if (!hasVisitedIngredient(ingredient) && hasConnection(itemToAssign, ingredient) && !isAssigned(itemToAssign, ingredient)) {
/* 259 */               visitIngredient(ingredient);
/* 260 */               this.path.add(ingredient);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } else {
/* 265 */           int lastAssignedIngredient = this.path.getInt(pathLength - 1);
/*     */           
/* 267 */           if (!isSatisfied(lastAssignedIngredient)) {
/* 268 */             return this.path;
/*     */           }
/*     */ 
/*     */           
/* 272 */           for (int item = 0; item < this.itemCount; item++) {
/* 273 */             if (!hasVisitedItem(item) && isAssigned(item, lastAssignedIngredient)) {
/*     */               
/* 275 */               assert hasConnection(item, lastAssignedIngredient);
/* 276 */               visitItem(item);
/* 277 */               this.path.add(item);
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 284 */         int newLength = this.path.size();
/* 285 */         if (newLength == pathLength) {
/* 286 */           this.path.removeInt(newLength - 1);
/*     */         }
/*     */       } 
/*     */       
/* 290 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 294 */     private int visitedIngredientOffset() { return 0; }
/*     */ 
/*     */ 
/*     */     
/* 298 */     private int visitedIngredientCount() { return this.ingredientCount; }
/*     */ 
/*     */ 
/*     */     
/* 302 */     private int visitedItemOffset() { return visitedIngredientOffset() + visitedIngredientCount(); }
/*     */ 
/*     */ 
/*     */     
/* 306 */     private int visitedItemCount() { return this.itemCount; }
/*     */ 
/*     */ 
/*     */     
/* 310 */     private int satisfiedOffset() { return visitedItemOffset() + visitedItemCount(); }
/*     */ 
/*     */ 
/*     */     
/* 314 */     private int satisfiedCount() { return this.ingredientCount; }
/*     */ 
/*     */ 
/*     */     
/* 318 */     private int connectionOffset() { return satisfiedOffset() + satisfiedCount(); }
/*     */ 
/*     */ 
/*     */     
/* 322 */     private int connectionCount() { return this.ingredientCount * this.itemCount; }
/*     */ 
/*     */ 
/*     */     
/* 326 */     private int residualOffset() { return connectionOffset() + connectionCount(); }
/*     */ 
/*     */ 
/*     */     
/* 330 */     private int residualCount() { return this.ingredientCount * this.itemCount; }
/*     */ 
/*     */ 
/*     */     
/* 334 */     private boolean isSatisfied(int ingredient) { return this.data.get(getSatisfiedIndex(ingredient)); }
/*     */ 
/*     */ 
/*     */     
/* 338 */     private void setSatisfied(int ingredient) { this.data.set(getSatisfiedIndex(ingredient)); }
/*     */ 
/*     */     
/*     */     private int getSatisfiedIndex(int ingredient) {
/* 342 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 343 */       return satisfiedOffset() + ingredient;
/*     */     }
/*     */ 
/*     */     
/* 347 */     private void clearSatisfied() { clearRange(satisfiedOffset(), satisfiedCount()); }
/*     */ 
/*     */ 
/*     */     
/* 351 */     private void setConnection(int item, int ingredient) { this.data.set(getConnectionIndex(item, ingredient)); }
/*     */ 
/*     */ 
/*     */     
/* 355 */     private boolean hasConnection(int item, int ingredient) { return this.data.get(getConnectionIndex(item, ingredient)); }
/*     */ 
/*     */     
/*     */     private int getConnectionIndex(int item, int ingredient) {
/* 359 */       assert item >= 0 && item < this.itemCount;
/* 360 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 361 */       return connectionOffset() + item * this.ingredientCount + ingredient;
/*     */     }
/*     */ 
/*     */     
/* 365 */     private boolean isAssigned(int item, int ingredient) { return this.data.get(getResidualIndex(item, ingredient)); }
/*     */ 
/*     */     
/*     */     private void assign(int item, int ingredient) {
/* 369 */       int residualIndex = getResidualIndex(item, ingredient);
/* 370 */       assert !this.data.get(residualIndex);
/* 371 */       this.data.set(residualIndex);
/*     */     }
/*     */     
/*     */     private void unassign(int item, int ingredient) {
/* 375 */       int residualIndex = getResidualIndex(item, ingredient);
/* 376 */       assert this.data.get(residualIndex);
/* 377 */       this.data.clear(residualIndex);
/*     */     }
/*     */     
/*     */     private int getResidualIndex(int item, int ingredient) {
/* 381 */       assert item >= 0 && item < this.itemCount;
/* 382 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 383 */       return residualOffset() + item * this.ingredientCount + ingredient;
/*     */     }
/*     */ 
/*     */     
/* 387 */     private void visitIngredient(int item) { this.data.set(getVisitedIngredientIndex(item)); }
/*     */ 
/*     */ 
/*     */     
/* 391 */     private boolean hasVisitedIngredient(int ingredient) { return this.data.get(getVisitedIngredientIndex(ingredient)); }
/*     */ 
/*     */     
/*     */     private int getVisitedIngredientIndex(int ingredient) {
/* 395 */       assert ingredient >= 0 && ingredient < this.ingredientCount;
/* 396 */       return visitedIngredientOffset() + ingredient;
/*     */     }
/*     */ 
/*     */     
/* 400 */     private void visitItem(int item) { this.data.set(getVisitiedItemIndex(item)); }
/*     */ 
/*     */ 
/*     */     
/* 404 */     private boolean hasVisitedItem(int item) { return this.data.get(getVisitiedItemIndex(item)); }
/*     */ 
/*     */     
/*     */     private int getVisitiedItemIndex(int item) {
/* 408 */       assert item >= 0 && item < this.itemCount;
/* 409 */       return visitedItemOffset() + item;
/*     */     }
/*     */     
/*     */     private void clearAllVisited() {
/* 413 */       clearRange(visitedIngredientOffset(), visitedIngredientCount());
/* 414 */       clearRange(visitedItemOffset(), visitedItemCount());
/*     */     }
/*     */ 
/*     */     
/* 418 */     private void clearRange(int offset, int count) { this.data.clear(offset, offset + count); }
/*     */ 
/*     */     
/*     */     public int tryPickAll(int maxSize, StackedContents.Output<T> output) {
/* 422 */       int mid, min = 0;
/* 423 */       int max = Math.min(maxSize, StackedContents.this.getResultUpperBound(this.ingredients)) + 1;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 428 */         mid = (min + max) / 2;
/*     */         
/* 430 */         if (tryPick(mid, null)) {
/* 431 */           if (max - min <= 1) {
/*     */             break;
/*     */           }
/* 434 */           min = mid; continue;
/*     */         } 
/* 436 */         max = mid;
/*     */       } 
/*     */ 
/*     */       
/* 440 */       if (mid > 0) {
/* 441 */         tryPick(mid, output);
/*     */       }
/*     */       
/* 444 */       return mid;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Output<T> {
/*     */     void accept(T param1T);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IngredientInfo<T> {
/*     */     boolean acceptsItem(T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\StackedContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */