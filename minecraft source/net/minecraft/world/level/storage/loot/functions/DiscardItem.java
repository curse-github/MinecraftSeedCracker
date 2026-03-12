/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DiscardItem extends LootItemConditionalFunction {
/* 12 */   public static final MapCodec<DiscardItem> CODEC = RecordCodecBuilder.mapCodec(i -> 
/* 13 */       commonFields(i)
/* 14 */       .apply(i, DiscardItem::new));
/*    */ 
/*    */   
/* 17 */   protected DiscardItem(List<LootItemCondition> predicates) { super(predicates); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public LootItemFunctionType<DiscardItem> getType() { return LootItemFunctions.DISCARD; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected ItemStack run(ItemStack itemStack, LootContext context) { return ItemStack.EMPTY; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static LootItemConditionalFunction.Builder<?> discardItem() { return simpleBuilder(DiscardItem::new); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\DiscardItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */