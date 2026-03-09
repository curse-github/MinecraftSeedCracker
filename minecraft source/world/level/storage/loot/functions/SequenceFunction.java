/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public class SequenceFunction implements LootItemFunction {
/* 15 */   public static final MapCodec<SequenceFunction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LootItemFunctions.TYPED_CODEC
/* 16 */         .listOf().fieldOf("functions").forGetter(()))
/* 17 */       .apply(i, SequenceFunction::new));
/*    */   
/* 19 */   public static final Codec<SequenceFunction> INLINE_CODEC = LootItemFunctions.TYPED_CODEC.listOf().xmap(SequenceFunction::new, f -> f.functions);
/*    */   
/*    */   private final List<LootItemFunction> functions;
/*    */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*    */   
/*    */   private SequenceFunction(List<LootItemFunction> functions) {
/* 25 */     this.functions = functions;
/* 26 */     this.compositeFunction = LootItemFunctions.compose(functions);
/*    */   }
/*    */ 
/*    */   
/* 30 */   public static SequenceFunction of(List<LootItemFunction> functions) { return new SequenceFunction(List.copyOf(functions)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public ItemStack apply(ItemStack stack, LootContext context) { return (ItemStack)this.compositeFunction.apply(stack, context); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 40 */     super.validate(output);
/*    */     
/* 42 */     for (int i = 0; i < this.functions.size(); i++) {
/* 43 */       ((LootItemFunction)this.functions.get(i)).validate(output.forChild(new ProblemReporter.IndexedFieldPathElement("functions", i)));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public LootItemFunctionType<SequenceFunction> getType() { return LootItemFunctions.SEQUENCE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SequenceFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */