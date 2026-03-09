/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.Nameable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class CopyNameFunction extends LootItemConditionalFunction {
/* 17 */   public static final MapCodec<CopyNameFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(LootContextArg.ENTITY_OR_BLOCK
/* 18 */         .fieldOf("source").forGetter(()))
/* 19 */       .apply(i, CopyNameFunction::new));
/*    */   
/*    */   private final LootContextArg<Object> source;
/*    */   
/*    */   private CopyNameFunction(List<LootItemCondition> predicates, LootContextArg<?> source) {
/* 24 */     super(predicates);
/* 25 */     this.source = LootContextArg.cast(source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public LootItemFunctionType<CopyNameFunction> getType() { return LootItemFunctions.COPY_NAME; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(this.source.contextParam()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 40 */     Object maybeNameable = this.source.get(context);
/*    */     
/* 42 */     if (maybeNameable instanceof Nameable) { Nameable nameable = (Nameable)maybeNameable;
/* 43 */       itemStack.set(DataComponents.CUSTOM_NAME, nameable.getCustomName()); }
/*    */     
/* 45 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public static LootItemConditionalFunction.Builder<?> copyName(LootContextArg<?> target) { return simpleBuilder(conditions -> new CopyNameFunction(conditions, target)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyNameFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */