/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TrappedChestSection
/*     */   extends LeavesFix.Section
/*     */ {
/*     */   private IntSet chestIds;
/*     */   
/* 116 */   public TrappedChestSection(Typed<?> section, Schema inputSchema) { super(section, inputSchema); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean skippable() {
/* 121 */     this.chestIds = new IntOpenHashSet();
/*     */     
/* 123 */     for (int i = 0; i < this.palette.size(); i++) {
/* 124 */       Dynamic<?> paletteTag = (Dynamic)this.palette.get(i);
/* 125 */       String blockName = paletteTag.get("Name").asString("");
/* 126 */       if (Objects.equals(blockName, "minecraft:trapped_chest")) {
/* 127 */         this.chestIds.add(i);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     return this.chestIds.isEmpty();
/*     */   }
/*     */ 
/*     */   
/* 135 */   public boolean isTrappedChest(int block) { return this.chestIds.contains(block); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix$TrappedChestSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */