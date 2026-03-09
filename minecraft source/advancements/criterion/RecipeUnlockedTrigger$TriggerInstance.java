/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record
/*    */   implements SimpleCriterionTrigger.SimpleInstance
/*    */ {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final ResourceKey<Recipe<?>> recipe;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/RecipeUnlockedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/RecipeUnlockedTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/RecipeUnlockedTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/RecipeUnlockedTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/RecipeUnlockedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/RecipeUnlockedTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 28 */   public TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<Recipe<?>> recipe) { this.player = player; this.recipe = recipe; } public Optional<ContextAwarePredicate> player() { return this.player; } public ResourceKey<Recipe<?>> recipe() { return this.recipe; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 33 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), Recipe.KEY_CODEC
/* 34 */         .fieldOf("recipe").forGetter(TriggerInstance::recipe))
/* 35 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 38 */   public boolean matches(RecipeHolder<?> recipe) { return (this.recipe == recipe.id()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\RecipeUnlockedTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */