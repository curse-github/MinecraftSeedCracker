/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetBookCoverFunction extends LootItemConditionalFunction {
/* 18 */   public static final MapCodec<SetBookCoverFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(
/* 19 */           Filterable.codec(Codec.string(0, 32)).optionalFieldOf("title").forGetter(()), Codec.STRING
/* 20 */           .optionalFieldOf("author").forGetter(()), 
/* 21 */           ExtraCodecs.intRange(0, 3).optionalFieldOf("generation").forGetter(())))
/* 22 */       .apply(i, SetBookCoverFunction::new));
/*    */   
/*    */   private final Optional<String> author;
/*    */   private final Optional<Filterable<String>> title;
/*    */   private final Optional<Integer> generation;
/*    */   
/*    */   public SetBookCoverFunction(List<LootItemCondition> predicates, Optional<Filterable<String>> title, Optional<String> author, Optional<Integer> generation) {
/* 29 */     super(predicates);
/* 30 */     this.author = author;
/* 31 */     this.title = title;
/* 32 */     this.generation = generation;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 37 */     itemStack.update(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY, this::apply);
/* 38 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private WrittenBookContent apply(WrittenBookContent original) {
/* 43 */     Objects.requireNonNull(original);
/* 44 */     Objects.requireNonNull(original);
/* 45 */     Objects.requireNonNull(original); return new WrittenBookContent((Filterable)this.title.orElseGet(original::title), (String)this.author.orElseGet(original::author), ((Integer)this.generation.orElseGet(original::generation)).intValue(), original
/* 46 */         .pages(), original
/* 47 */         .resolved());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public LootItemFunctionType<SetBookCoverFunction> getType() { return LootItemFunctions.SET_BOOK_COVER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetBookCoverFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */