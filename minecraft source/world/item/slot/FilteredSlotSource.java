/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ 
/*    */ public class FilteredSlotSource extends TransformedSlotSource {
/*  8 */   public static final MapCodec<FilteredSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(ItemPredicate.CODEC
/*  9 */         .fieldOf("item_filter").forGetter(()))
/* 10 */       .apply(i, FilteredSlotSource::new));
/*    */   
/*    */   private final ItemPredicate filter;
/*    */   
/*    */   private FilteredSlotSource(SlotSource slotSource, ItemPredicate filter) {
/* 15 */     super(slotSource);
/* 16 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public MapCodec<FilteredSlotSource> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected SlotCollection transform(SlotCollection slots) { return slots.filter(this.filter); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\FilteredSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */