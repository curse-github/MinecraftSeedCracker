/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
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
/*     */ public class Builder
/*     */ {
/*     */   private static final CreativeModeTab.DisplayItemsGenerator EMPTY_GENERATOR = (parameters, output) -> {
/*     */     
/*     */     };
/*     */   private final CreativeModeTab.Row row;
/*     */   private final int column;
/*     */   private Component displayName;
/*     */   private Supplier<ItemStack> iconGenerator;
/*     */   private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;
/*     */   private boolean canScroll;
/*     */   private boolean showTitle;
/*     */   private boolean alignedRight;
/*     */   private CreativeModeTab.Type type;
/*     */   private Identifier backgroundTexture;
/*     */   
/*     */   public Builder(CreativeModeTab.Row row, int column) {
/* 149 */     this.displayName = Component.empty();
/* 150 */     this.iconGenerator = (() -> ItemStack.EMPTY);
/* 151 */     this.displayItemsGenerator = EMPTY_GENERATOR;
/* 152 */     this.canScroll = true;
/* 153 */     this.showTitle = true;
/* 154 */     this.alignedRight = false;
/* 155 */     this.type = CreativeModeTab.Type.CATEGORY;
/* 156 */     this.backgroundTexture = CreativeModeTab.DEFAULT_BACKGROUND;
/*     */ 
/*     */     
/* 159 */     this.row = row;
/* 160 */     this.column = column;
/*     */   }
/*     */   
/*     */   public Builder title(Component displayName) {
/* 164 */     this.displayName = displayName;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder icon(Supplier<ItemStack> iconGenerator) {
/* 169 */     this.iconGenerator = iconGenerator;
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Builder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
/* 174 */     this.displayItemsGenerator = displayItemsGenerator;
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder alignedRight() {
/* 179 */     this.alignedRight = true;
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hideTitle() {
/* 184 */     this.showTitle = false;
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public Builder noScrollBar() {
/* 189 */     this.canScroll = false;
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   protected Builder type(CreativeModeTab.Type type) {
/* 194 */     this.type = type;
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   public Builder backgroundTexture(Identifier backgroundTexture) {
/* 199 */     this.backgroundTexture = backgroundTexture;
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public CreativeModeTab build() {
/* 204 */     if ((this.type == CreativeModeTab.Type.HOTBAR || this.type == CreativeModeTab.Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR) {
/* 205 */       throw new IllegalStateException("Special tabs can't have display items");
/*     */     }
/*     */     
/* 208 */     CreativeModeTab tab = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
/* 209 */     tab.alignedRight = this.alignedRight;
/* 210 */     tab.showTitle = this.showTitle;
/* 211 */     tab.canScroll = this.canScroll;
/* 212 */     tab.backgroundTexture = this.backgroundTexture;
/* 213 */     return tab;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\CreativeModeTab$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */