/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetCustomModelDataFunction extends LootItemConditionalFunction {
/* 25 */   private static final Codec<NumberProvider> COLOR_PROVIDER_CODEC = Codec.withAlternative(NumberProviders.CODEC, ExtraCodecs.RGB_COLOR_CODEC, net.minecraft.world.level.storage.loot.providers.number.ConstantValue::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final MapCodec<SetCustomModelDataFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(
/* 31 */           ListOperation.StandAlone.codec(NumberProviders.CODEC, 2147483647).optionalFieldOf("floats").forGetter(()), 
/* 32 */           ListOperation.StandAlone.codec(Codec.BOOL, 2147483647).optionalFieldOf("flags").forGetter(()), 
/* 33 */           ListOperation.StandAlone.codec(Codec.STRING, 2147483647).optionalFieldOf("strings").forGetter(()), 
/* 34 */           ListOperation.StandAlone.codec(COLOR_PROVIDER_CODEC, 2147483647).optionalFieldOf("colors").forGetter(())))
/* 35 */       .apply(i, SetCustomModelDataFunction::new));
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<NumberProvider>> floats;
/*    */   private final Optional<ListOperation.StandAlone<Boolean>> flags;
/*    */   private final Optional<ListOperation.StandAlone<String>> strings;
/*    */   private final Optional<ListOperation.StandAlone<NumberProvider>> colors;
/*    */   
/*    */   public SetCustomModelDataFunction(List<LootItemCondition> predicates, Optional<ListOperation.StandAlone<NumberProvider>> floats, Optional<ListOperation.StandAlone<Boolean>> flags, Optional<ListOperation.StandAlone<String>> strings, Optional<ListOperation.StandAlone<NumberProvider>> colors) {
/* 43 */     super(predicates);
/* 44 */     this.floats = floats;
/* 45 */     this.flags = flags;
/* 46 */     this.strings = strings;
/* 47 */     this.colors = colors;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 52 */     return (Set)Stream.concat(this.floats
/* 53 */         .stream(), this.colors
/* 54 */         .stream())
/*    */       
/* 56 */       .flatMap(l -> l.value().stream())
/* 57 */       .flatMap(e -> e.getReferencedContextParams().stream())
/* 58 */       .collect(Collectors.toSet());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public LootItemFunctionType<SetCustomModelDataFunction> getType() { return LootItemFunctions.SET_CUSTOM_MODEL_DATA; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   private static <T> List<T> apply(Optional<ListOperation.StandAlone<T>> operation, List<T> current) { return (List)operation.map(o -> o.apply(current)).orElse(current); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   private static <T, E> List<E> apply(Optional<ListOperation.StandAlone<T>> operation, List<E> current, Function<T, E> mapper) { return (List)operation.map(o -> {
/* 72 */           List<E> transformedReplacement = o.value().stream().map(mapper).toList();
/* 73 */           return o.operation().apply(current, transformedReplacement);
/* 74 */         }).orElse(current); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 79 */     CustomModelData component = (CustomModelData)itemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
/*    */     
/* 81 */     itemStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(
/* 82 */           apply(this.floats, component.floats(), provider -> Float.valueOf(provider.getFloat(context))), 
/* 83 */           apply(this.flags, component.flags()), 
/* 84 */           apply(this.strings, component.strings()), 
/* 85 */           apply(this.colors, component.colors(), provider -> Integer.valueOf(provider.getInt(context)))));
/*    */     
/* 87 */     return itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetCustomModelDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */