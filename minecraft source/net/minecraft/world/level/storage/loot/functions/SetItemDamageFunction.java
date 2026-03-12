/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SetItemDamageFunction extends LootItemConditionalFunction {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 22 */   public static final MapCodec<SetItemDamageFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(NumberProviders.CODEC
/* 23 */           .fieldOf("damage").forGetter(()), Codec.BOOL
/* 24 */           .fieldOf("add").orElse(Boolean.valueOf(false)).forGetter(())))
/* 25 */       .apply(i, SetItemDamageFunction::new));
/*    */   
/*    */   private final NumberProvider damage;
/*    */   private final boolean add;
/*    */   
/*    */   private SetItemDamageFunction(List<LootItemCondition> predicates, NumberProvider damage, boolean add) {
/* 31 */     super(predicates);
/* 32 */     this.damage = damage;
/* 33 */     this.add = add;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public LootItemFunctionType<SetItemDamageFunction> getType() { return LootItemFunctions.SET_DAMAGE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.damage.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 48 */     if (itemStack.isDamageableItem()) {
/* 49 */       int maxDamage = itemStack.getMaxDamage();
/* 50 */       float base = this.add ? (1.0F - itemStack.getDamageValue() / maxDamage) : 0.0F;
/* 51 */       float pct = 1.0F - Mth.clamp(this.damage.getFloat(context) + base, 0.0F, 1.0F);
/* 52 */       itemStack.setDamageValue(Mth.floor(pct * maxDamage));
/*    */     } else {
/* 54 */       LOGGER.warn("Couldn't set damage of loot item {}", itemStack);
/*    */     } 
/* 56 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 60 */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider value) { return simpleBuilder(conditions -> new SetItemDamageFunction(conditions, value, false)); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider value, boolean add) { return simpleBuilder(conditions -> new SetItemDamageFunction(conditions, value, add)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetItemDamageFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */