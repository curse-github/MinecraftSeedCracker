/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ static enum Mode
/*     */ {
/* 164 */   REPLACE(FillCommand.Affector.NOOP, FillCommand.Filter.NOOP),
/* 165 */   OUTLINE(FillCommand.Affector.NOOP, (r, p, b, l) -> {
/* 166 */       if (p.getX() == r.minX() || p.getX() == r.maxX() || p.getY() == r.minY() || p.getY() == r.maxY() || p.getZ() == r.minZ() || p.getZ() == r.maxZ()) {
/* 167 */         return b;
/*     */       }
/* 169 */       return null;
/*     */     
/*     */     }),
/* 172 */   HOLLOW(FillCommand.Affector.NOOP, (r, p, b, l) -> {
/* 173 */       if (p.getX() == r.minX() || p.getX() == r.maxX() || p.getY() == r.minY() || p.getY() == r.maxY() || p.getZ() == r.minZ() || p.getZ() == r.maxZ()) {
/* 174 */         return b;
/*     */       }
/* 176 */       return FillCommand.HOLLOW_CORE;
/*     */     
/*     */     }),
/* 179 */   DESTROY((l, p) -> l.destroyBlock(p, true), FillCommand.Filter.NOOP);
/*     */   
/*     */   public final FillCommand.Filter filter;
/*     */   public final FillCommand.Affector affector;
/*     */   
/*     */   Mode(FillCommand.Affector affector, FillCommand.Filter filter) {
/* 185 */     this.affector = affector;
/* 186 */     this.filter = filter;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\FillCommand$Mode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */