/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<SetAttributesFunction.Builder>
/*     */ {
/*     */   private final boolean replace;
/*     */   private final List<SetAttributesFunction.Modifier> modifiers;
/*     */   
/*     */   public Builder(boolean replace) {
/* 104 */     this.modifiers = Lists.newArrayList();
/*     */ 
/*     */     
/* 107 */     this.replace = replace;
/*     */   }
/*     */ 
/*     */   
/* 111 */   public Builder() { this(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected Builder getThis() { return this; }
/*     */ 
/*     */   
/*     */   public Builder withModifier(SetAttributesFunction.ModifierBuilder modifier) {
/* 120 */     this.modifiers.add(modifier.build());
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public LootItemFunction build() { return new SetAttributesFunction(getConditions(), this.modifiers, this.replace); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */