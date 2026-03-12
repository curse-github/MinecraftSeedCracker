/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworksFunction extends LootItemConditionalFunction {
/* 17 */   public static final MapCodec<SetFireworksFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(
/* 18 */           ListOperation.StandAlone.codec(FireworkExplosion.CODEC, 256).optionalFieldOf("explosions").forGetter(()), ExtraCodecs.UNSIGNED_BYTE
/* 19 */           .optionalFieldOf("flight_duration").forGetter(())))
/* 20 */       .apply(i, SetFireworksFunction::new));
/*    */   
/* 22 */   public static final Fireworks DEFAULT_VALUE = new Fireworks(0, List.of());
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<FireworkExplosion>> explosions;
/*    */   private final Optional<Integer> flightDuration;
/*    */   
/*    */   protected SetFireworksFunction(List<LootItemCondition> predicates, Optional<ListOperation.StandAlone<FireworkExplosion>> explosions, Optional<Integer> flightDuration) {
/* 28 */     super(predicates);
/* 29 */     this.explosions = explosions;
/* 30 */     this.flightDuration = flightDuration;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 35 */     itemStack.update(DataComponents.FIREWORKS, DEFAULT_VALUE, this::apply);
/* 36 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private Fireworks apply(Fireworks old) {
/* 41 */     Objects.requireNonNull(old); return new Fireworks(((Integer)this.flightDuration.orElseGet(old::flightDuration)).intValue(), (List)this.explosions
/* 42 */         .map(operation -> operation.apply(old.explosions())).orElse(old.explosions()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public LootItemFunctionType<SetFireworksFunction> getType() { return LootItemFunctions.SET_FIREWORKS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetFireworksFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */