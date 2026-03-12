/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FillPlayerHead extends LootItemConditionalFunction {
/* 18 */   public static final MapCodec<FillPlayerHead> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(LootContext.EntityTarget.CODEC
/* 19 */         .fieldOf("entity").forGetter(()))
/* 20 */       .apply(i, FillPlayerHead::new));
/*    */   
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public FillPlayerHead(List<LootItemCondition> predicates, LootContext.EntityTarget entityTarget) {
/* 25 */     super(predicates);
/* 26 */     this.entityTarget = entityTarget;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public LootItemFunctionType<FillPlayerHead> getType() { return LootItemFunctions.FILL_PLAYER_HEAD; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(this.entityTarget.contextParam()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 41 */     if (itemStack.is(Items.PLAYER_HEAD)) {
/* 42 */       Object object = context.getOptionalParameter(this.entityTarget.contextParam()); if (object instanceof Player) { Player dataDonor = (Player)object;
/* 43 */         itemStack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(dataDonor.getGameProfile())); }
/*    */     
/*    */     } 
/* 46 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 50 */   public static LootItemConditionalFunction.Builder<?> fillPlayerHead(LootContext.EntityTarget entityTarget) { return simpleBuilder(conditions -> new FillPlayerHead(conditions, entityTarget)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FillPlayerHead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */