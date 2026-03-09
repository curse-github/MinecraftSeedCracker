/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<SetLoreFunction.Builder>
/*    */ {
/* 65 */   private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
/* 66 */   private final ImmutableList.Builder<Component> lore = ImmutableList.builder();
/* 67 */   private ListOperation mode = ListOperation.Append.INSTANCE;
/*    */   
/*    */   public Builder setMode(ListOperation mode) {
/* 70 */     this.mode = mode;
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setResolutionContext(LootContext.EntityTarget resolutionContext) {
/* 75 */     this.resolutionContext = Optional.of(resolutionContext);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addLine(Component line) {
/* 80 */     this.lore.add(line);
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 86 */   protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   public LootItemFunction build() { return new SetLoreFunction(getConditions(), this.lore.build(), this.mode, this.resolutionContext); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */