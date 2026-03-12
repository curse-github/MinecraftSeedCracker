/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<CopyCustomDataFunction.Builder>
/*     */ {
/*     */   private final NbtProvider source;
/*     */   private final List<CopyCustomDataFunction.CopyOperation> ops;
/*     */   
/*     */   private Builder(NbtProvider source) {
/*  99 */     this.ops = Lists.newArrayList();
/*     */ 
/*     */     
/* 102 */     this.source = source;
/*     */   }
/*     */   
/*     */   public Builder copy(String sourcePath, String targetPath, CopyCustomDataFunction.MergeStrategy mergeStrategy) {
/*     */     try {
/* 107 */       this.ops.add(new CopyCustomDataFunction.CopyOperation(NbtPathArgument.NbtPath.of(sourcePath), NbtPathArgument.NbtPath.of(targetPath), mergeStrategy));
/* 108 */     } catch (CommandSyntaxException e) {
/* 109 */       throw new IllegalArgumentException(e);
/*     */     } 
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 115 */   public Builder copy(String sourcePath, String targetPath) { return copy(sourcePath, targetPath, CopyCustomDataFunction.MergeStrategy.REPLACE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected Builder getThis() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public LootItemFunction build() { return new CopyCustomDataFunction(getConditions(), this.source, this.ops); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */