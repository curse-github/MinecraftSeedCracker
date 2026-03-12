/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetWrittenBookPagesFunction extends LootItemConditionalFunction {
/* 18 */   public static final MapCodec<SetWrittenBookPagesFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(WrittenBookContent.PAGES_CODEC
/* 19 */           .fieldOf("pages").forGetter(()), ListOperation.UNLIMITED_CODEC
/* 20 */           .forGetter(())))
/* 21 */       .apply(i, SetWrittenBookPagesFunction::new));
/*    */   
/*    */   private final List<Filterable<Component>> pages;
/*    */   private final ListOperation pageOperation;
/*    */   
/*    */   protected SetWrittenBookPagesFunction(List<LootItemCondition> predicates, List<Filterable<Component>> pages, ListOperation pageOperation) {
/* 27 */     super(predicates);
/* 28 */     this.pages = pages;
/* 29 */     this.pageOperation = pageOperation;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     itemStack.update(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY, this::apply);
/* 35 */     return itemStack;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public WrittenBookContent apply(WrittenBookContent original) {
/* 40 */     List<Filterable<Component>> newPages = this.pageOperation.apply(original.pages(), this.pages);
/* 41 */     return original.withReplacedPages(newPages);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public LootItemFunctionType<SetWrittenBookPagesFunction> getType() { return LootItemFunctions.SET_WRITTEN_BOOK_PAGES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetWrittenBookPagesFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */