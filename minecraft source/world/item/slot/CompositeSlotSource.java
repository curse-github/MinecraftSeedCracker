/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeSlotSource implements SlotSource {
/*    */   protected final List<SlotSource> terms;
/*    */   private final Function<LootContext, SlotCollection> compositeSlotSource;
/*    */   
/*    */   protected CompositeSlotSource(List<SlotSource> terms) {
/* 18 */     this.terms = terms;
/* 19 */     this.compositeSlotSource = SlotSources.group(terms);
/*    */   }
/*    */ 
/*    */   
/* 23 */   protected static <T extends CompositeSlotSource> MapCodec<T> createCodec(Function<List<SlotSource>, T> factory) { return RecordCodecBuilder.mapCodec(i -> i.group(SlotSources.CODEC
/* 24 */           .listOf().fieldOf("terms").forGetter(()))
/* 25 */         .apply(i, factory)); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected static <T extends CompositeSlotSource> Codec<T> createInlineCodec(Function<List<SlotSource>, T> factory) { return SlotSources.CODEC.listOf().xmap(factory, t -> t.terms); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public SlotCollection provide(LootContext context) { return (SlotCollection)this.compositeSlotSource.apply(context); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 42 */     super.validate(context);
/*    */     
/* 44 */     for (int i = 0; i < this.terms.size(); i++)
/* 45 */       ((SlotSource)this.terms.get(i)).validate(context.forChild(new ProblemReporter.IndexedFieldPathElement("terms", i))); 
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends CompositeSlotSource> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\CompositeSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */