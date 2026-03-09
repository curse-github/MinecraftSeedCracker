/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetWritableBookPagesFunction extends LootItemConditionalFunction {
/* 16 */   public static final MapCodec<SetWritableBookPagesFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(WritableBookContent.PAGES_CODEC
/* 17 */           .fieldOf("pages").forGetter(()), 
/* 18 */           ListOperation.codec(100).forGetter(())))
/* 19 */       .apply(i, SetWritableBookPagesFunction::new));
/*    */   
/*    */   private final List<Filterable<String>> pages;
/*    */   private final ListOperation pageOperation;
/*    */   
/*    */   protected SetWritableBookPagesFunction(List<LootItemCondition> predicates, List<Filterable<String>> pages, ListOperation pageOperation) {
/* 25 */     super(predicates);
/* 26 */     this.pages = pages;
/* 27 */     this.pageOperation = pageOperation;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 32 */     itemStack.update(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY, this::apply);
/* 33 */     return itemStack;
/*    */   }
/*    */   
/*    */   public WritableBookContent apply(WritableBookContent original) {
/* 37 */     List<Filterable<String>> newPages = this.pageOperation.apply(original.pages(), this.pages, 100);
/* 38 */     return original.withReplacedPages(newPages);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public LootItemFunctionType<SetWritableBookPagesFunction> getType() { return LootItemFunctions.SET_WRITABLE_BOOK_PAGES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetWritableBookPagesFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */