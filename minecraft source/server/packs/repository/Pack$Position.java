/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Position
/*     */ {
/* 150 */   TOP,
/* 151 */   BOTTOM;
/*     */ 
/*     */   
/*     */   public <T> int insert(List<T> list, T value, Function<T, PackSelectionConfig> converter, boolean reverse) {
/* 155 */     Position self = reverse ? opposite() : this;
/* 156 */     if (self == BOTTOM) {
/* 157 */       int index = 0;
/* 158 */       while (index < list.size()) {
/* 159 */         PackSelectionConfig pack = (PackSelectionConfig)converter.apply(list.get(index));
/* 160 */         if (pack.fixedPosition() && pack.defaultPosition() == this) {
/* 161 */           index++;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 166 */       list.add(index, value);
/* 167 */       return index;
/*     */     } 
/* 169 */     int index = list.size() - 1;
/* 170 */     while (index >= 0) {
/* 171 */       PackSelectionConfig pack = (PackSelectionConfig)converter.apply(list.get(index));
/* 172 */       if (pack.fixedPosition() && pack.defaultPosition() == this) {
/* 173 */         index--;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 178 */     list.add(index + 1, value);
/* 179 */     return index + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public Position opposite() { return (this == TOP) ? BOTTOM : TOP; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\Pack$Position.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */